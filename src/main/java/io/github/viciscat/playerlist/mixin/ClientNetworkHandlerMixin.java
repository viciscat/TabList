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
    private static final String prefix = "Connected players:";
    @Unique
    private static final String prefix2 = "§7Connected players:";

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

        if (par1.chatMessage.startsWith(prefix)) {
            PlayerList.readChatTick = inGameHud.getTicks();
            PlayerList.playerListString = "";
            PlayerList.playerListString += par1.chatMessage.replace(prefix, "");
            if (PlayerList.commandSent) {
                ci.cancel();
            }
        } else if (par1.chatMessage.startsWith(prefix2)) {
            PlayerList.readChatTick = inGameHud.getTicks();
            PlayerList.playerListString = "";
            PlayerList.playerListString += par1.chatMessage.replace(prefix2, "");
            if (PlayerList.commandSent) {
                ci.cancel();
            }
        } else if (PlayerList.readChatTick == inGameHud.getTicks()) {
            PlayerList.playerListString += par1.chatMessage;
            if (PlayerList.commandSent) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "onHello", at=@At("TAIL"))
    public void serverHello(LoginHelloPacket par1, CallbackInfo ci) {
        PlayerList.enabled = true;
    }
}
