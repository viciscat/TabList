package io.github.viciscat.playerlist;

import io.github.viciscat.playerlist.mixin.InGameHudAccessor;
import io.github.viciscat.playerlist.mixin.MinecraftAccessor;

import java.util.ArrayList;
import java.util.List;

public class PlayerList {
    public static int readChatTick = 0;
    public static String playerListString = "";
    public static int lastSend = 0;
    public static final List<String> players = new ArrayList<>();
    private static boolean isTabPressed = false;
    public static boolean commandSent = false;
    public static boolean enabled = true;

    public static void onTabPressed() {
        if (MinecraftAccessor.getInstance().player == null) return;
        isTabPressed = true;
        if (!PlayerList.enabled) return;
        InGameHudAccessor inGameHud = (InGameHudAccessor) MinecraftAccessor.getInstance().inGameHud;
        if (MinecraftAccessor.getInstance().isWorldRemote() && inGameHud.getTicks() > lastSend+50) {
            MinecraftAccessor.getInstance().player.sendChatMessage("/test");
            lastSend = inGameHud.getTicks();
            commandSent = true;
        }
    }

    public static void onTabReleased() {
        isTabPressed = false;
    }

    public static boolean isTabPressed() {
        return isTabPressed;
    }
}
