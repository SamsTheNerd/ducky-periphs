package com.samsthenerd.duckyperiphs.mixin;

import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Mouse.class)
public interface MixinMouseLockAccessor {
    @Accessor("cursorLocked")
    void setCursorLocked(boolean locked);
}
