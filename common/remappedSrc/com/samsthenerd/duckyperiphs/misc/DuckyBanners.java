package com.samsthenerd.duckyperiphs.misc;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;

import dev.architectury.registry.registries.Registrar;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DuckyBanners {
    public static final TagKey<BannerPattern> DUCKY_PATTERN_ITEM_KEY = TagKey.of(Registry.BANNER_PATTERN_KEY, new Identifier("ducky-periphs", "pattern_item/ducky"));
    public static final Registrar<BannerPattern> BANNER_PATTERNS = DuckyPeriphs.REGISTRIES.get().get(Registry.BANNER_PATTERN_KEY);

    public static void registerBannerPatterns(){
        BANNER_PATTERNS.register(new Identifier("ducky-periphs", "ducky"), () -> new BannerPattern("ducky"));
    }
}
