package moe.katelyn.citadel.hud;

import moe.katelyn.citadel.*;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public class AbilityHud {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 20;
    public static final int SPACING = 6;

    public static final int UPGRADE_HEIGHT = 7;
    public static final int UPGRADE_SPACING = 1;

    public static final int UPGRADE_AMOUNT = 3;

    public static final int PADDING = 4;

    // the spacer makes way for the -5 offset in the middle
    public static final int ABILITY_SPACER = 5;
    public static final int ABILITY_HEIGHT = ((UPGRADE_HEIGHT + UPGRADE_SPACING) * UPGRADE_AMOUNT) - UPGRADE_SPACING + PADDING + HEIGHT + ABILITY_SPACER;

    public static final int CONTAINER_HEIGHT = PADDING + ABILITY_HEIGHT + PADDING;

    private final List<AbilityItem> widgets = new ArrayList<>();
    private Identifier currentHeroId = null;
    private boolean init = false;

    public AbilityHud() {
        for (int i = 0; i < 4; i++) {
            widgets.add(new AbilityItem(i));
        }
    }

    public void render(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        Minecraft client = Minecraft.getInstance();

        boolean tab = updateTabState(client);

        if (client.player == null) return;
        if (client.options.hideGui) return;

        if (!init) {
            loadFromPlayer(client.player);
            init = true;
        }

        int screenWidth = client.getWindow().getGuiScaledWidth();
        int screenHeight = client.getWindow().getGuiScaledHeight();
        int totalWidth = widgets.size() * (WIDTH + SPACING) - SPACING;
        int startX = (screenWidth - totalWidth) / 2;
        int y = screenHeight - CONTAINER_HEIGHT - 10;

        int currentX = startX;
        int mouseX = (int) (client.mouseHandler.xpos() * client.getWindow().getGuiScaledWidth() / client.getWindow().getScreenWidth());
        int mouseY = (int) (client.mouseHandler.ypos() * client.getWindow().getGuiScaledHeight() / client.getWindow().getScreenHeight());

        if (tab) {
            graphics.fill(startX - PADDING, y + CONTAINER_HEIGHT + PADDING, startX + totalWidth + PADDING, y, 0xFF2F2D28);
            graphics.fill(startX - PADDING, y, startX + totalWidth + PADDING, y + 2, 0xFFCE91FF);
        }

        for (AbilityItem widget : widgets) {
            widget.setPos(currentX, y + CONTAINER_HEIGHT - HEIGHT - PADDING);
            widget.render(graphics, mouseX, mouseY, tab);
            currentX += WIDTH + SPACING;
        }

        for (AbilityItem widget : widgets) {
            widget.renderTooltip(graphics, mouseX, mouseY, tab);
        }
    }

    private void loadFromPlayer(Player player) {
        PlayerHero playerHero = player.getAttached(Citadel.PLAYER_HERO);
        if (playerHero == null) return;

        Identifier heroId = playerHero.getHero();

        if (!heroId.equals(currentHeroId)) {
            currentHeroId = heroId;
        }

        Hero hero = CitadelHeroRegistry.get(heroId);
        if (hero == null) return;

        List<Ability> abilities = hero.getAbilities();

        for (int i = 0; i < widgets.size() && i < abilities.size(); i++) {
            AbilityItem widget = widgets.get(i);
            Ability ability = abilities.get(i);
            Identifier abilityId = ability.getId();

            boolean unlocked = playerHero.isAbilityAvailable(abilityId);
            int level = playerHero.getUpgrade(abilityId);
            int cooldown = playerHero.getRemainingCooldown(abilityId);
            int maxCooldown = unlocked ? ability.getUpgrade(level).getCooldown() : 0;
            int cooldownPercent = (maxCooldown > 0 && cooldown > 0) ? (cooldown * 100) / maxCooldown : 0;

            widget.update(ability, unlocked, level, cooldown, cooldownPercent);
        }
    }

    public void updateAbility(int index, String id, int cooldown, int level, boolean unlocked) {
        if (index >= 0 && index < widgets.size()) {
            AbilityItem widget = widgets.get(index);

            Minecraft client = Minecraft.getInstance();
            if (client.player == null) return;

            Identifier heroId = Identifier.tryParse(id);
            if (heroId == null) return;

            PlayerHero playerHero = client.player.getAttached(Citadel.PLAYER_HERO);
            if (playerHero == null) return;

            if (!heroId.equals(currentHeroId)) {
                playerHero.changeHero(heroId);
                playerHero.getAbilities().clear();
            }

            Hero hero = CitadelHeroRegistry.get(heroId);
            if (hero == null) return;

            Ability ability = hero.getAbilities().get(index);

            if (ability != null) {
                int maxCooldown = ability.getUpgrade(level).getCooldown();
                int cooldownPercent = (maxCooldown > 0 && cooldown > 0) ? (cooldown * 100) / maxCooldown : 0;

                widget.update(ability, unlocked, level, cooldown, cooldownPercent);
            }
        }
    }

    public void onHeroChange(Identifier newHeroId) {
        this.currentHeroId = newHeroId;
    }

    public AbilityItem getWidget(int index) {
        if (index >= 0 && index < widgets.size()) {
            return widgets.get(index);
        }
        return null;
    }

    public boolean isTabHeld(long handle) {
        return GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_TAB) == GLFW.GLFW_PRESS;
    }

    private boolean updateTabState(Minecraft client) {
        long handle = client.getWindow().handle();
        boolean current = isTabHeld(handle);

        boolean inUI = client.screen != null || client.getOverlay() != null;

        if (current || inUI) {
            client.mouseHandler.releaseMouse();
        } else {
            client.mouseHandler.grabMouse();
        }

        if (inUI) return false;
        return current;
    }

    private void refreshWidgets() {
        for (AbilityItem widget : widgets) {
            widget.reset();
        }
    }
}
