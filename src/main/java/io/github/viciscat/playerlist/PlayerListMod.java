package io.github.viciscat.playerlist;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerListMod implements ClientModInitializer, ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("player_list");
    public static final KeyBinding PLAYER_LIST_KEY = new KeyBinding("playerList", Keyboard.KEY_TAB);

    @Override
    public void onInitializeClient() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Player list mod initialized");
    }

    @Override
    public void onInitialize() {
        //LOGGER.info("Hello common!");
    }
}
