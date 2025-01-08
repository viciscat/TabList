package io.github.viciscat.playerlist.mixin;

import io.github.viciscat.playerlist.PlayerList;
import net.minecraft.client.util.ScreenScaler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

@Mixin(InGameHud.class)
public class InGameHudMixin extends DrawContext {
    @Unique
    private static final Color TEXT_COLOR = new Color(255, 255, 255);
    @Unique
    private static final Color COLUMN_COLOR = new Color(255, 255, 255, 50);
    @Unique
    private static final Color TAB_COLOR = new Color(0, 0, 0, 100);
    @Shadow private Minecraft minecraft;

    @Shadow private int ticks;
    @Unique private static int maxWidth = 0;

    @Inject(method = "render", at=@At("TAIL"))
    public void PlayerList$render(float bl, boolean fuckof, int j, int par4, CallbackInfo ci) {
        if (!PlayerList.isTabPressed() || !PlayerList.enabled) {
            return;
        }

        TextRenderer textRenderer = this.minecraft.textRenderer;
        if (ticks == PlayerList.readChatTick+1) {
            maxWidth = 0;
            PlayerList.players.clear();
            for (String s : PlayerList.playerListString.split(",")) {
                String replace = s.trim().replace("&", "§");
                maxWidth = Math.max(maxWidth, textRenderer.getWidth(replace));
                PlayerList.players.add(replace);
            }
            PlayerList.commandSent = false;
        }

        ScreenScaler widthHeightProvider = new ScreenScaler(this.minecraft.options, this.minecraft.displayWidth, this.minecraft.displayHeight);
        int width = widthHeightProvider.getScaledWidth();
        int height = widthHeightProvider.getScaledHeight();

        final int maxPlayerPerColumn = 20;

        int columnCount = PlayerList.players.size() / maxPlayerPerColumn + 1;
        int columnWidth = maxWidth + 2;
        int tabWidth = columnWidth *columnCount + 10 *(columnCount+1);
        int tabHeight = maxPlayerPerColumn*9+2+20;
        int left = (width - tabWidth) / 2;
        this.fill(left, 10, tabWidth+left, tabHeight + 10, TAB_COLOR.getRGB());
        for (int i = 0; i < columnCount; i++) {
            int columnLeft = left + 10 + (columnWidth+10)*i;
            this.fill(columnLeft, 20, columnLeft + columnWidth, tabHeight, COLUMN_COLOR.getRGB());
        }
        for (int i = 0; i < PlayerList.players.size(); i++) {
            int columnLeft = left + 10 + (columnWidth+10)*(i/maxPlayerPerColumn);
            textRenderer.drawWithShadow(PlayerList.players.get(i), columnLeft+1, 21+(i%maxPlayerPerColumn)*9, TEXT_COLOR.getRGB());
        }

    }
}
