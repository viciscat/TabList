package io.github.viciscat.playerlist.mixin;

import io.github.viciscat.playerlist.PlayerList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.ClientNetworkHandler;
import net.minecraft.network.packet.login.LoginHelloPacket;
import net.minecraft.network.packet.play.ChatMessagePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientNetworkHandler.class)
public class ClientNetworkHandlerMixin {

    @Shadow private Minecraft minecraft;
    @Unique
    private static final String PREFIX = "Connected players:";
    @Unique
    private static final String PREFIX_2 = "§7Connected players:";
    @Unique
    private boolean isRetroMC = false;

    @Inject(method = "onChatMessage", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;addChatMessage(Ljava/lang/String;)V"), cancellable = true)
    public void onChatMessage(ChatMessagePacket par1, CallbackInfo ci) {
        if (!PlayerList.enabled) return;
        //System.out.println(par1.chatMessage + ";; time:" + System.currentTimeMillis());
        InGameHudAccessor inGameHud = (InGameHudAccessor) this.minecraft.inGameHud;

        if (PlayerList.commandSent && par1.chatMessage.trim().startsWith("§9There are")) {
            ci.cancel();
        }

        if (PlayerList.commandSent && par1.chatMessage.trim().startsWith("Unknown command")) {
            minecraft.inGameHud.addChatMessage("§4The server doesn't have a /list command it seems!");
            minecraft.inGameHud.addChatMessage("§4Disabling player list.");
            PlayerList.enabled = false;
            ci.cancel();
        }

        if (par1.chatMessage.startsWith(PREFIX)) {
            PlayerList.readChatTick = inGameHud.getTicks();
            PlayerList.playerListString = "";
            PlayerList.playerListString += par1.chatMessage.replace(PREFIX, "");
            if (PlayerList.commandSent) {
                ci.cancel();
            }
        } else if (par1.chatMessage.startsWith(PREFIX_2)) {
            PlayerList.readChatTick = inGameHud.getTicks();
            PlayerList.playerListString = "";
            PlayerList.playerListString += par1.chatMessage.replace(PREFIX_2, "");
            if (PlayerList.commandSent) {
                ci.cancel();
            }
        } else if (par1.chatMessage.startsWith("§7- ")) {
            if (!isRetroMC) {
                PlayerList.readChatTick = inGameHud.getTicks();
                isRetroMC = true;
                PlayerList.playerListString = "";
            }
            PlayerList.playerListString += par1.chatMessage.split(" ", 2)[1] + ",";
            System.out.println(PlayerList.playerListString);
            if (PlayerList.commandSent) {
                ci.cancel();
            }
        } else if (PlayerList.readChatTick == inGameHud.getTicks() && !isRetroMC) {
            PlayerList.playerListString += par1.chatMessage;
            if (PlayerList.commandSent) {
                ci.cancel();
            }
        } else if (isRetroMC) {
            if (PlayerList.commandSent) ci.cancel();
            isRetroMC = false;
        }
    }

    @Inject(method = "onHello", at=@At("TAIL"))
    public void serverHello(LoginHelloPacket par1, CallbackInfo ci) {
        PlayerList.enabled = true;
    }
}
