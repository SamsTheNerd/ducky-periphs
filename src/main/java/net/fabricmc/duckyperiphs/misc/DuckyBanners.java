package net.fabricmc.duckyperiphs.misc;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

public class DuckyBanners {
    public static final RegistryKey<BannerPattern> DUCKY = RegistryKey.of(Registry.BANNER_PATTERN_KEY, new Identifier("ducky-periphs", "ducky"));
    public static final TagKey<BannerPattern> DUCKY_PATTERN_ITEM_KEY = TagKey.of(Registry.BANNER_PATTERN_KEY, new Identifier("ducky-periphs", "pattern_item/ducky"));

    public static void registerBannerPatterns(){
        Registry.register(Registry.BANNER_PATTERN, DUCKY, new BannerPattern("ducky"));
    }
}
