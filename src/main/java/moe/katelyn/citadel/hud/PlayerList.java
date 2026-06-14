package moe.katelyn.citadel.hud;

import moe.katelyn.citadel.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerList {
    public static final int WIDTH = 50;
    public static final int HEIGHT = 100;
    public static final int SPACING = 1;

    public static final int AVATAR_WIDTH = 28;

    public static final int TEAM_SCORE_WIDTH = 44;
    public static final int TEAM_SCORE_HEIGHT = 16;
    public static final int TEAM_DIVIDER_WIDTH = 10;

    public static final int TEAM_WIDTH = ((WIDTH + SPACING) * 6) - SPACING;

    public static final int FULL_WIDTH = (TEAM_WIDTH * 2) + (TEAM_SCORE_WIDTH * 2) + (TEAM_DIVIDER_WIDTH);

    public static final int TOP_PADDING = 8;
    public static final int PADDING = 4;

    public static final int AVATAR_X = (PlayerList.WIDTH - PlayerList.AVATAR_WIDTH) / 2;

    public static final int TEAM_SCORE_Y = PADDING + PADDING + (AVATAR_WIDTH - TEAM_SCORE_HEIGHT) / 2;

    private final List<PlayerItem> entries = new ArrayList<>();

    public PlayerList() {

    }

    public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();

        if (client.player == null) return;

        update(client);

        int screenWidth = client.getWindow().getGuiScaledWidth();
        int screenHeight = client.getWindow().getGuiScaledHeight();
        int x = (screenWidth - FULL_WIDTH) / 2;
        int y = 0;

        int mouseX = (int) (client.mouseHandler.xpos() * client.getWindow().getGuiScaledWidth() / client.getWindow().getScreenWidth());
        int mouseY = (int) (client.mouseHandler.ypos() * client.getWindow().getGuiScaledHeight() / client.getWindow().getScreenHeight());

        Font font = Minecraft.getInstance().font;

        boolean tab = CitadelClient.getInstance().isTabHeld(Minecraft.getInstance().getWindow().handle());

        graphics.fill(x, y, x + FULL_WIDTH, y + 1, CitadelTeamRegistry.Team.AMBER.getColour());

        final int teamAmberScoreX = x + TEAM_WIDTH;
        final int teamSapphireScoreX = teamAmberScoreX + TEAM_DIVIDER_WIDTH + TEAM_SCORE_WIDTH;

        graphics.fill(teamAmberScoreX - AVATAR_X, y + TEAM_SCORE_Y, teamAmberScoreX + TEAM_SCORE_WIDTH, y + TEAM_SCORE_Y + TEAM_SCORE_HEIGHT, CitadelTeamRegistry.Team.AMBER.getColour());
        graphics.text(font, Component.literal("0k"), teamAmberScoreX + PADDING, y + TEAM_SCORE_Y + 4, 0x88000000, false);

        graphics.fill(teamSapphireScoreX, y + TEAM_SCORE_Y, teamSapphireScoreX + TEAM_SCORE_WIDTH + AVATAR_X, y + TEAM_SCORE_Y + TEAM_SCORE_HEIGHT, CitadelTeamRegistry.Team.SAPPHIRE.getColour());

        int currentX = x;
        int index = 1;
        for (PlayerItem item : entries) {
            if (index == 7) currentX += TEAM_SCORE_WIDTH + TEAM_DIVIDER_WIDTH + TEAM_SCORE_WIDTH - SPACING;

            item.setPos(currentX, y);
            item.render(graphics, mouseX, mouseY, tab);
            currentX += WIDTH + SPACING;
            index++;
        }
    }

    private void update(Minecraft client) {
        List<PlayerInfo> players  = new ArrayList<>(client.getConnection().getOnlinePlayers());

        entries.clear();

        for (int i = 0; i < 12; i++) {
            int virtualI = i;

            if (i > players.size() - 1) virtualI = 0;

            PlayerItem item = new PlayerItem(virtualI);
            item.update(players.get(virtualI));
            entries.add(item);
        }
    }
}
