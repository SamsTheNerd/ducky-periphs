package com.samsthenerd.duckyperiphs.peripherals.InventoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.inventory.Inventory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.InvalidIdentifierException;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

// ported from Advanced Peripherals InventoryManager (Apache-2.0)
// https://github.com/Seniorendi/AdvancedPeripherals
// Making this Peripheral Cross Mod Loader Available
public class InventoryManagerPeripheral implements IPeripheral {

    private final InventoryManagerTile imTile;

    InventoryManagerPeripheral(InventoryManagerTile tile){
        imTile = tile;
    }

    @Override
    public String getType(){
        return "inventory_manager";
    }

    @Override
    public Set<String> getAdditionalTypes(){
        return Set.of("inventoryManager");
    }

    @Override
    public boolean equals(@Nullable IPeripheral other){
        return this == other;
    }

    @LuaFunction
    public final String getOwner(){
        PlayerEntity owner = imTile.getOwnerPlayer();
        return owner == null ? null : owner.getName().getString();
    }

    @LuaFunction(mainThread = true)
    public final MethodResult addItemToPlayer(String invDirection, Map<?, ?> item) throws LuaException {
        Filter filter;
        try {
            filter = Filter.parse(item);
        } catch(Filter.BadFilter | LuaException e){
            return MethodResult.of(0, e.getMessage());
        }
        Direction direction = requireDirection(invDirection);

        Inventory fromInv = getAdjacentInventory(direction);
        if(fromInv == null)
            return MethodResult.of(0, "INVENTORY_FROM_INVALID");

        PlayerEntity owner = requireOwner();
        int moved = moveContainerToPlayer(fromInv, owner.getInventory(), filter);
        return MethodResult.of(moved);
    }

    @LuaFunction(mainThread = true)
    public final MethodResult removeItemFromPlayer(String invDirection, Map<?, ?> item) throws LuaException {
        Filter filter;
        try {
            filter = Filter.parse(item);
        } catch(Filter.BadFilter | LuaException e){
            return MethodResult.of(0, e.getMessage());
        }
        Direction direction = requireDirection(invDirection);

        Inventory toInv = getAdjacentInventory(direction);
        if(toInv == null)
            return MethodResult.of(0, "INVENTORY_TO_INVALID");

        PlayerEntity owner = requireOwner();
        int moved = movePlayerToContainer(owner.getInventory(), toInv, filter);
        return MethodResult.of(moved);
    }

    @LuaFunction(value = {"list", "getItems"}, mainThread = true)
    public final List<Map<String, Object>> getItems() throws LuaException {
        PlayerEntity owner = requireOwner();
        List<Map<String, Object>> items = new ArrayList<>();
        DefaultedList<ItemStack> main = owner.getInventory().main;
        for(int i = 0; i < main.size(); i++){
            if(!main.get(i).isEmpty())
                items.add(stackToObjectWithSlot(main.get(i), i));
        }
        return items;
    }

    @LuaFunction(value = {"listChest", "getItemsChest"}, mainThread = true)
    public final MethodResult getItemsChest(String target) throws LuaException {
        Direction direction = requireDirection(target);
        Inventory inv = getAdjacentInventory(direction);
        if(inv == null)
            return MethodResult.of(null, "INVENTORY_TO_INVALID");

        List<Map<String, Object>> items = new ArrayList<>();
        for(int slot = 0; slot < inv.size(); slot++){
            if(!inv.getStack(slot).isEmpty())
                items.add(stackToObjectWithSlot(inv.getStack(slot), slot));
        }
        return MethodResult.of(items);
    }

    @LuaFunction(mainThread = true)
    public final List<Map<String, Object>> getArmor() throws LuaException {
        PlayerEntity owner = requireOwner();
        List<Map<String, Object>> items = new ArrayList<>();
        DefaultedList<ItemStack> armor = owner.getInventory().armor;
        for(int i = 0; i < armor.size(); i++){
            ItemStack stack = armor.get(i);
            if(!stack.isEmpty())
                items.add(stackToObjectWithSlot(stack, getArmorSlotFor(stack)));
        }
        return items;
    }

    @LuaFunction(mainThread = true)
    public final boolean isPlayerEquipped() throws LuaException {
        PlayerEntity owner = requireOwner();
        for(ItemStack stack : owner.getInventory().armor){
            if(!stack.isEmpty())
                return true;
        }
        return false;
    }

    @LuaFunction(mainThread = true)
    public final boolean isWearing(int index) throws LuaException {
        PlayerEntity owner = requireOwner();
        int slot = index >= 100 && index <= 103 ? index - 100 : index;
        if(slot < 0 || slot > 3)
            return false;
        return !owner.getInventory().armor.get(slot).isEmpty();
    }

    @LuaFunction(mainThread = true)
    public final int getEmptySpace() throws LuaException {
        PlayerEntity owner = requireOwner();
        int empty = 0;
        for(ItemStack stack : owner.getInventory().main){
            if(stack.isEmpty())
                empty++;
        }
        return empty;
    }

    @LuaFunction(mainThread = true)
    public final boolean isSpaceAvailable() throws LuaException {
        return getEmptySpace() > 0;
    }

    @LuaFunction(mainThread = true)
    public final int getFreeSlot() throws LuaException {
        return requireOwner().getInventory().getEmptySlot();
    }

    @LuaFunction(mainThread = true)
    public final Map<String, Object> getItemInHand() throws LuaException {
        return itemToObject(requireOwner().getMainHandStack().copy());
    }

    @LuaFunction(mainThread = true)
    public final Map<String, Object> getItemInOffHand() throws LuaException {
        return itemToObject(requireOwner().getOffHandStack().copy());
    }


    private PlayerEntity requireOwner() throws LuaException {
        PlayerEntity owner = imTile.getOwnerPlayer();
        if(owner == null)
            throw new LuaException("The Inventory Manager doesn't have a memory card or it isn't bound to a player.");
        return owner;
    }

    private Direction requireDirection(String side) throws LuaException {
        Direction dir = parseDirection(side);
        if(dir == null)
            throw new LuaException(side + " is not a valid side.");
        return dir;
    }

    private Direction parseDirection(String raw){
        if(raw == null)
            return null;
        String side = raw.toLowerCase(Locale.ROOT);
        Direction cardinal = Direction.byName(side);
        if(cardinal != null)
            return cardinal;
        Direction front = imTile.getCachedState().get(InventoryManagerBlock.FACING);
        switch(side){
            case "front":
                return front;
            case "back":
                return front.getOpposite();
            case "left":
                return front.rotateYClockwise();
            case "right":
                return front.rotateYCounterclockwise();
            case "top":
            case "up":
                return Direction.UP;
            case "bottom":
            case "down":
                return Direction.DOWN;
            default:
                return null;
        }
    }

    private Inventory getAdjacentInventory(Direction direction){
        World world = imTile.getWorld();
        if(world == null)
            return null;
        return HopperBlockEntity.getInventoryAt(world, imTile.getPos().offset(direction));
    }

    private static int getArmorSlotFor(ItemStack stack){
        if(stack.getItem() instanceof ArmorItem armorItem){
            EquipmentSlot slotType = armorItem.getSlotType();
            switch(slotType){
                case FEET:
                    return 100;
                case LEGS:
                    return 101;
                case CHEST:
                    return 102;
                case HEAD:
                    return 103;
                default:
                    break;
            }
        }
        return 0;
    }

    private static class SlotRef {
        final DefaultedList<ItemStack> list;
        final int index;

        SlotRef(DefaultedList<ItemStack> list, int index){
            this.list = list;
            this.index = index;
        }

        static SlotRef of(PlayerInventory inv, int slot){
            if(slot >= 100 && slot <= 103)
                return new SlotRef(inv.armor, slot - 100);
            if(slot == 36)
                return new SlotRef(inv.offHand, 0);
            if(slot >= 0 && slot < inv.main.size())
                return new SlotRef(inv.main, slot);
            return null;
        }
    }

    private static int moveContainerToPlayer(Inventory fromInv, PlayerInventory pInv, Filter filter){
        int needs = filter.count;
        int moved = 0;
        int size = fromInv.size();
        for(int i = 0; i < size; i++){
            if(filter.fromSlot >= 0 && i != filter.fromSlot)
                continue;
            ItemStack srcStack = fromInv.getStack(i);
            if(srcStack.isEmpty() || !filter.test(srcStack))
                continue;
            while(needs > 0 && !srcStack.isEmpty()){
                int take = Math.min(needs, srcStack.getCount());
                ItemStack moving = srcStack.copy();
                moving.setCount(take);
                insertIntoPlayer(pInv, moving, filter.toSlot);
                int inserted = take - moving.getCount();
                if(inserted <= 0)
                    break;
                srcStack.decrement(inserted);
                fromInv.markDirty();
                moved += inserted;
                needs -= inserted;
            }
            if(needs <= 0)
                break;
        }
        return moved;
    }

    private static int movePlayerToContainer(PlayerInventory pInv, Inventory toInv, Filter filter){
        int needs = filter.count;
        int moved = 0;
        List<SlotRef> sources = new ArrayList<>();
        if(filter.fromSlot >= 0){
            SlotRef ref = SlotRef.of(pInv, filter.fromSlot);
            if(ref != null)
                sources.add(ref);
        } else {
            for(int i = 0; i < pInv.main.size(); i++)
                sources.add(new SlotRef(pInv.main, i));
            for(int i = 0; i < pInv.offHand.size(); i++)
                sources.add(new SlotRef(pInv.offHand, i));
            for(int i = 0; i < pInv.armor.size(); i++)
                sources.add(new SlotRef(pInv.armor, i));
        }
        for(SlotRef ref : sources){
            ItemStack srcStack = ref.list.get(ref.index);
            if(srcStack.isEmpty() || !filter.test(srcStack))
                continue;
            while(needs > 0 && !srcStack.isEmpty()){
                int take = Math.min(needs, srcStack.getCount());
                ItemStack moving = srcStack.copy();
                moving.setCount(take);
                insertIntoContainer(toInv, moving, filter.toSlot);
                int inserted = take - moving.getCount();
                if(inserted <= 0)
                    break;
                srcStack.decrement(inserted);
                pInv.markDirty();
                moved += inserted;
                needs -= inserted;
            }
            if(needs <= 0)
                break;
        }
        return moved;
    }

    private static void insertIntoPlayer(PlayerInventory pInv, ItemStack moving, int toSlot){
        if(toSlot >= 0){
            SlotRef ref = SlotRef.of(pInv, toSlot);
            if(ref == null)
                return;
            ItemStack dst = ref.list.get(ref.index);
            if(dst.isEmpty()){
                ref.list.set(ref.index, moving.split(Math.min(moving.getCount(), moving.getMaxCount())));
                pInv.markDirty();
            } else {
                mergeStacks(dst, moving, dst.getMaxCount());
                pInv.markDirty();
            }
            return;
        }
        pInv.insertStack(moving);
    }

    private static void insertIntoContainer(Inventory inv, ItemStack moving, int toSlot){
        if(toSlot >= 0){
            if(toSlot >= inv.size())
                return;
            ItemStack dst = inv.getStack(toSlot);
            if(dst.isEmpty()){
                inv.setStack(toSlot, moving.split(Math.min(moving.getCount(), moving.getMaxCount())));
            } else {
                mergeStacks(dst, moving, dst.getMaxCount());
            }
            inv.markDirty();
            return;
        }
        for(int i = 0; i < inv.size() && !moving.isEmpty(); i++){
            mergeStacks(inv.getStack(i), moving, inv.getMaxCountPerStack());
        }
        for(int i = 0; i < inv.size() && !moving.isEmpty(); i++){
            if(inv.getStack(i).isEmpty()){
                inv.setStack(i, moving.split(Math.min(moving.getCount(), Math.min(inv.getMaxCountPerStack(), moving.getMaxCount()))));
            }
        }
        inv.markDirty();
    }

    private static void mergeStacks(ItemStack dst, ItemStack moving, int cap){
        if(dst.isEmpty() || moving.isEmpty())
            return;
        if(!ItemStack.canCombine(dst, moving))
            return;
        int max = Math.min(cap, dst.getMaxCount());
        int canAdd = Math.min(moving.getCount(), max - dst.getCount());
        if(canAdd > 0){
            dst.increment(canAdd);
            moving.decrement(canAdd);
        }
    }

    private Map<String, Object> stackToObjectWithSlot(ItemStack stack, long slot){
        Map<String, Object> props = itemToObject(stack);
        if(props != null)
            props.put("slot", slot);
        return props;
    }

    @Nullable
    private static Map<String, Object> itemToObject(@Nullable ItemStack stack){
        if(stack == null || stack.isEmpty())
            return null;
        Map<String, Object> props = new HashMap<>();
        props.put("name", Registries.ITEM.getId(stack.getItem()).toString());
        List<String> tags = new ArrayList<>();
        stack.getItem().getRegistryEntry().streamTags().forEach(tag -> tags.add("#" + tag.id()));
        props.put("tags", tags);
        if(stack.hasNbt())
            props.put("nbt", nbtToLua(stack.getNbt()));
        props.put("count", stack.getCount());
        props.put("displayName", stack.getName().getString());
        props.put("maxStackSize", stack.getMaxCount());
        return props;
    }

    private static Object nbtToLua(NbtElement element){
        switch(element.getType()){
            case NbtElement.COMPOUND_TYPE: {
                NbtCompound compound = (NbtCompound) element;
                Map<String, Object> map = new HashMap<>();
                for(String key : compound.getKeys())
                    map.put(key, nbtToLua(compound.get(key)));
                return map;
            }
            case NbtElement.LIST_TYPE: {
                NbtList list = (NbtList) element;
                List<Object> out = new ArrayList<>();
                for(NbtElement el : list)
                    out.add(nbtToLua(el));
                return out;
            }
            case NbtElement.BYTE_ARRAY_TYPE: {
                byte[] bytes = ((net.minecraft.nbt.NbtByteArray) element).getByteArray();
                List<Integer> out = new ArrayList<>();
                for(byte b : bytes)
                    out.add((int) b);
                return out;
            }
            case NbtElement.INT_ARRAY_TYPE: {
                int[] ints = ((net.minecraft.nbt.NbtIntArray) element).getIntArray();
                List<Integer> out = new ArrayList<>();
                for(int i : ints)
                    out.add(i);
                return out;
            }
            case NbtElement.LONG_ARRAY_TYPE: {
                long[] longs = ((net.minecraft.nbt.NbtLongArray) element).getLongArray();
                List<Long> out = new ArrayList<>();
                for(long l : longs)
                    out.add(l);
                return out;
            }
            case NbtElement.BYTE_TYPE:
                return ((net.minecraft.nbt.NbtByte) element).byteValue();
            case NbtElement.SHORT_TYPE:
                return ((net.minecraft.nbt.NbtShort) element).shortValue();
            case NbtElement.INT_TYPE:
                return ((net.minecraft.nbt.NbtInt) element).intValue();
            case NbtElement.LONG_TYPE:
                return ((net.minecraft.nbt.NbtLong) element).longValue();
            case NbtElement.FLOAT_TYPE:
                return ((net.minecraft.nbt.NbtFloat) element).floatValue();
            case NbtElement.DOUBLE_TYPE:
                return ((net.minecraft.nbt.NbtDouble) element).doubleValue();
            default:
                return element.asString();
        }
    }


    static final class Filter {
        private Item item = null;
        private TagKey<Item> tag = null;
        private NbtCompound nbt = null;
        private int count = 64;
        private int fromSlot = -1;
        private int toSlot = -1;

        boolean test(ItemStack stack){
            if(item != null && !stack.isOf(item))
                return false;
            if(tag != null && !stack.isIn(tag))
                return false;
            if(nbt != null){
                NbtCompound stackNbt = stack.getNbt() != null ? stack.getNbt() : new NbtCompound();
                if(!stackNbt.equals(nbt))
                    return false;
            }
            return true;
        }

        static class BadFilter extends RuntimeException {
            BadFilter(String message){
                super(message);
            }
        }

        static Filter parse(Map<?, ?> lua) throws LuaException {
            Filter filter = new Filter();
            if(lua == null || lua.isEmpty())
                return filter;

            Object nameObj = lua.get("name");
            if(nameObj != null){
                String name = nameObj.toString();
                if(name.startsWith("#")){
                    Identifier tagId;
                    try {
                        tagId = new Identifier(name.substring(1));
                    } catch(InvalidIdentifierException e){
                        throw new LuaException("ITEM_NOT_FOUND");
                    }
                    filter.tag = TagKey.of(RegistryKeys.ITEM, tagId);
                } else {
                    Identifier id = Identifier.tryParse(name);
                    Item parsed = id == null ? Items.AIR : Registries.ITEM.get(id);
                    if(id == null || (parsed == Items.AIR && !"minecraft:air".equals(id.toString())))
                        throw new LuaException("ITEM_NOT_FOUND");
                    filter.item = parsed;
                }
            }

            Object nbtObj = lua.get("nbt");
            if(nbtObj != null){
                try {
                    filter.nbt = StringNbtReader.parse(nbtObj.toString());
                } catch(Exception e){
                    throw new LuaException("bad field \"nbt\", expected an NBT string");
                }
            }

            filter.count = getInt(lua, "count", filter.count);
            filter.fromSlot = getInt(lua, "fromSlot", filter.fromSlot);
            filter.toSlot = getInt(lua, "toSlot", filter.toSlot);
            return filter;
        }

        private static int getInt(Map<?, ?> lua, String key, int fallback){
            Object val = lua.get(key);
            if(val instanceof Number num)
                return num.intValue();
            return fallback;
        }
    }
}
