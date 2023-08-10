package com.samsthenerd.duckyperiphs.misc;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import dev.architectury.registry.registries.Registrar;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class DuckyBanners {
    public static final TagKey<BannerPattern> DUCKY_PATTERN_ITEM_KEY = TagKey.of(RegistryKeys.BANNER_PATTERN, new Identifier("ducky-periphs", "pattern_item/ducky"));
    public static final Registrar<BannerPattern> BANNER_PATTERNS = DuckyPeriphs.REGISTRIES.get().get(RegistryKeys.BANNER_PATTERN);

    public static void registerBannerPatterns(){
        BANNER_PATTERNS.register(new Identifier("ducky-periphs", "ducky"), () -> new BannerPattern("ducky"));
    }
}
