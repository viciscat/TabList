package io.github.viciscat.playerlist.mixin;

import io.github.viciscat.playerlist.PlayerListMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Shadow public KeyBinding[] allKeys;

    @Inject(method = "<init>(Lnet/minecraft/client/Minecraft;Ljava/io/File;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/GameOptions;load()V"))
    public void PlayerList$init(Minecraft file, File par2, CallbackInfo ci) {
        List<KeyBinding> bruh = new ArrayList<>(List.of(allKeys));
        bruh.add(PlayerListMod.PLAYER_LIST_KEY);
        allKeys = bruh.toArray(allKeys);
    }

    @Inject(method = "getKeybindName", at = @At("HEAD"), cancellable = true)
    public void PlayerList$text(int index, CallbackInfoReturnable<String> cir) {
        if (allKeys[index].equals(PlayerListMod.PLAYER_LIST_KEY)) {
            cir.setReturnValue("Player List");
        }
    }
}
