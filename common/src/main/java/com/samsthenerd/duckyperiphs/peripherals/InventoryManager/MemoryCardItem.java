package com.samsthenerd.duckyperiphs.peripherals.InventoryManager;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

// ported from Advanced Peripherals InventoryManager (Apache-2.0)
// https://github.com/Seniorendi/AdvancedPeripherals
// Making this Peripheral Cross Mod Loader Available
public class MemoryCardItem extends Item {

    public static final String OWNER_ID_KEY = "owner_id";
    public static final String OWNER_NAME_KEY = "owner_name";

    public MemoryCardItem(Settings settings){
        super(settings.maxCount(1));
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand){
        ItemStack stack = user.getStackInHand(hand);
        if(!world.isClient()){
            NbtCompound nbt = stack.getOrCreateNbt();
            if(nbt.containsUuid(OWNER_ID_KEY)){
                nbt.remove(OWNER_ID_KEY);
                nbt.remove(OWNER_NAME_KEY);
                user.sendMessage(Text.translatable("text.ducky-periphs.memory_card.removed_player"), true);
            } else {
                nbt.putUuid(OWNER_ID_KEY, user.getUuid());
                nbt.putString(OWNER_NAME_KEY, user.getName().getString());
                user.sendMessage(Text.translatable("text.ducky-periphs.memory_card.added_player"), true);
            }
        }
        return TypedActionResult.success(stack, world.isClient());
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context){
        NbtCompound nbt = stack.getNbt();
        if(nbt != null && nbt.contains(OWNER_ID_KEY)){
            String name = nbt.contains(OWNER_NAME_KEY) ? nbt.getString(OWNER_NAME_KEY) : nbt.getUuid(OWNER_ID_KEY).toString();
            tooltip.add(Text.translatable("item.ducky-periphs.memory_card.tooltip.bound", name).formatted(Formatting.GRAY));
        }
    }
}
