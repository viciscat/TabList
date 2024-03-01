package io.github.viciscat.playerlist.mixin;

import io.github.viciscat.playerlist.PlayerListMod;
import io.github.viciscat.playerlist.PlayerList;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow public abstract boolean isWorldRemote();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;getEventKey()I", ordinal = 0, remap = false))
    public void PlayerList$tabKeybind(CallbackInfo ci) {
        if (isWorldRemote() && Keyboard.getEventKey() == PlayerListMod.PLAYER_LIST_KEY.code) {
            if (Keyboard.getEventKeyState()) {
                PlayerList.onTabPressed();
            } else {
                PlayerList.onTabReleased();
            }
        }
    }


}
