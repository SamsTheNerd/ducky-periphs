package com.samsthenerd.duckyperiphs.misc;

import com.samsthenerd.duckyperiphs.DuckyPeriphs;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;

public class DuckyTags {
    public static TagKey<Item> FOCAL_PORT_FRIEND = TagKey.of(Registries.ITEM.getKey(),
        DuckyPeriphs.id("focal_port_friend"));

}
