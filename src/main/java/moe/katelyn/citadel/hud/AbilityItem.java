package moe.katelyn.citadel.hud;

import moe.katelyn.citadel.Ability;
import moe.katelyn.citadel.Citadel;
import moe.katelyn.citadel.CitadelClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AbilityItem {
    private static final Identifier WIDGET = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability.png");
    private static final Identifier WIDGET_TEXT = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability_text.png");
    private static final Identifier WIDGET_LOCKED = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability_locked.png");
    private static final Identifier WIDGET_HOVER = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability_hover.png");
    private static final Identifier UPGRADE = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/upgrade.png");
    private static final Identifier UPGRADE_TREE = Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/upgrade_tree.png");
    private static final int WIDTH = AbilityHud.WIDTH;
    private static final int HEIGHT = AbilityHud.HEIGHT;

    private final int index;
    private Identifier icon;

    private Ability ability;
    private boolean unlocked;
    private int level;
    private int cooldown;
    private int cooldownPercent;

    private int x;
    private int y;
    private boolean hovered;

    private long clientTimer = 0;

    private boolean wasLeftPressed = false;

    public AbilityItem(int index) {
        this.index = index;
        this.icon = null;

        this.unlocked = false;
        this.level = 0;
        this.cooldown = 0;
        this.cooldownPercent = 0;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Ability getAbility() { return ability; }

    public boolean isUnlocked() { return unlocked; }
    public int getLevel() { return level; }

    public void update(Ability ability, boolean unlocked, int level, int cooldown, int cooldownPercent) {
        Citadel.LOGGER.info("AbilityItem.update called - ability {}", ability.getName());
        this.ability = ability;
        this.unlocked = unlocked;
        this.level = level;
        this.cooldown = cooldown;
        this.cooldownPercent = cooldownPercent;

        this.icon = Objects.requireNonNullElseGet(ability.getIcon(), () -> Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "textures/gui/ability/placeholder.png"));
        Citadel.LOGGER.info("AbilityItem update: icon: {}, unlocked: {}", this.icon, this.unlocked);

        if (cooldown > 0) {
            this.clientTimer = System.currentTimeMillis() + (cooldown * 50L);
        } else {
            this.clientTimer = 0;
        }
    }

    public void tick() {
        if (clientTimer > 0) {
            long remaining = clientTimer - System.currentTimeMillis();
            if (remaining <= 0) {
                clientTimer = 0;
                cooldown = 0;
                cooldownPercent = 0;
            } else {
                cooldown = (int) (remaining / 50);
                if (ability != null) {
                    int maxCooldown = ability.getUpgrade(level).getCooldown();
                    cooldownPercent = maxCooldown > 0 ? (cooldown * 100) / maxCooldown : 0;
                }
            }
        }
    }

    public void reset() {
        this.ability = null;
        this.unlocked = false;
        this.level = 0;
        this.cooldown = 0;
        this.cooldownPercent = 0;
        this.clientTimer = 0;
    }

    public void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean tab) {
        int startY = y;

        if (index > 0 && index < 3) {
            startY = y - 5;
        }

        int fullHeight = AbilityHud.ABILITY_HEIGHT;

        tick();

        final Font font = Minecraft.getInstance().font;

        this.hovered = mouseX >= x && mouseY >= y - fullHeight && mouseX < x + WIDTH && mouseY < y + HEIGHT + AbilityHud.PADDING && tab;

        Minecraft client = Minecraft.getInstance();
        boolean isLeftPressed = client.mouseHandler.isLeftPressed();

        if (client.mouseHandler.isLeftPressed() && hovered && isLeftPressed && !wasLeftPressed) {
            if (!unlocked) {
                ClientPlayNetworking.send(new Citadel.UnlockAbilityPayload(ability.getId().toString()));
            } else if (level < 3) {
                ClientPlayNetworking.send(new Citadel.UpgradeAbilityPayload(ability.getId().toString()));
            }
        }

        wasLeftPressed = isLeftPressed;

        if (unlocked) {
            if (cooldown > 0) {
                graphics.blit(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, WIDGET, x, startY, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT, 0x99FFFFFF);
            } else {
                graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGET, x, startY, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
            }
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGET_LOCKED, x, startY, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
        }

        if (cooldown > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, icon, x + 3, startY + 3, 0, 0, 14, 14, 14, 14, 0x33FFFFFF);
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, icon, x + 3, startY + 3, 0, 0, 14, 14, 14, 14, 0xC0FFFFFF);
        }

        String keybind = String.valueOf(index + 1);

        Component keybindText = Component.literal(keybind).withStyle(style -> style.withFont(new FontDescription.Resource(Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "ability_small"))));
        int keybindWidth = font.width(keybindText);

        if (cooldown > 0 && unlocked) {
            int cooldownHeight = (int)(HEIGHT * (cooldownPercent / 100.0));
            graphics.blit(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, WIDGET, x, startY, 0, 0, WIDTH, cooldownHeight, WIDTH, HEIGHT, 0x33000000);

            String cooldownText = String.valueOf((cooldown / 20) + 1);

            int textWidth = font.width(cooldownText);
            int textX = x + (WIDTH - textWidth) / 2;
            int textY = startY + (HEIGHT - font.lineHeight) / 2;

            graphics.text(font, cooldownText, textX + 1, textY + 1, 0xC0000000, false);
        }

        if (hovered) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGET_HOVER, x - 1, startY - 1, 0, 0, WIDTH + 2, HEIGHT + 2, WIDTH + 2, HEIGHT + 2);
        }

        if (level >= 0 && ability.getUpgrade(level).getCooldown() > 0) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, WIDGET_TEXT, x + 6, startY + HEIGHT - 2, 0, 0, 8, 7, 8, 7);
            graphics.text(font, keybindText, x + (WIDTH - keybindWidth) / 2 + 1, startY + HEIGHT - 3, 0xC0000000, false);
        }

        if (tab) renderUpgrades(graphics, y);
    }

    public void renderUpgrades(GuiGraphicsExtractor graphics, int y) {
        final Font font = Minecraft.getInstance().font;
        int startHeight = y - HEIGHT - AbilityHud.ABILITY_SPACER - AbilityHud.UPGRADE_HEIGHT;

        Component level3 = Component.literal("A5").withStyle(style -> style.withFont(new FontDescription.Resource(Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "ability_small"))));
        Component level2 = Component.literal("A2").withStyle(style -> style.withFont(new FontDescription.Resource(Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "ability_small"))));
        Component level1 = Component.literal("A1").withStyle(style -> style.withFont(new FontDescription.Resource(Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "ability_small"))));
        Component checked = Component.literal("B").withStyle(style -> style.withFont(new FontDescription.Resource(Identifier.fromNamespaceAndPath(Citadel.MOD_ID, "ability_small"))));

        int level3Height = startHeight;
        int level2Height = startHeight + (AbilityHud.UPGRADE_HEIGHT + AbilityHud.UPGRADE_SPACING);
        int level1Height = startHeight + ((AbilityHud.UPGRADE_HEIGHT + AbilityHud.UPGRADE_SPACING) * 2);

        int fontHeight = 4;
        int innerPadding = -1;

        // level 3
        if (level >= 3) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, UPGRADE_TREE, x, level3Height, 0, 0, WIDTH, AbilityHud.UPGRADE_HEIGHT, WIDTH, AbilityHud.UPGRADE_HEIGHT);
            graphics.text(font, checked, x + 8, level3Height + innerPadding, 0xFFAD96BD, false);
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED, UPGRADE, x, level3Height, 0, 0, WIDTH, AbilityHud.UPGRADE_HEIGHT, WIDTH, AbilityHud.UPGRADE_HEIGHT);
            graphics.text(font, level3, x + 5, level3Height + innerPadding, 0xFFBDB1A0, false);
        }

        // level 2
        if (level >= 2) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, UPGRADE_TREE, x, level2Height, 0, 0, WIDTH, AbilityHud.UPGRADE_HEIGHT, WIDTH, AbilityHud.UPGRADE_HEIGHT);
            graphics.text(font, checked, x + 8, level2Height + innerPadding, 0xFFAD96BD, false);
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED, UPGRADE, x, level2Height, 0, 0, WIDTH, AbilityHud.UPGRADE_HEIGHT, WIDTH, AbilityHud.UPGRADE_HEIGHT);
            graphics.text(font, level2, x + 5, level2Height + innerPadding, 0xFFBDB1A0, false);
        }

        // level 1
        if (level >= 1) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, UPGRADE_TREE, x, level1Height, 0, 0, WIDTH, AbilityHud.UPGRADE_HEIGHT, WIDTH, AbilityHud.UPGRADE_HEIGHT);
            graphics.text(font, checked, x + 8, level1Height + innerPadding, 0xFFAD96BD, false);
        } else {
            graphics.blit(RenderPipelines.GUI_TEXTURED, UPGRADE, x, level1Height, 0, 0, WIDTH, AbilityHud.UPGRADE_HEIGHT, WIDTH, AbilityHud.UPGRADE_HEIGHT);
            graphics.text(font, level1, x + 5, level1Height + innerPadding, 0xFFBDB1A0, false);
        }
    }

    public void renderTooltip(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean tab) {
        if (!tab) return;

        final Font font = Minecraft.getInstance().font;

        if (hovered && ability != null) {
            List<Component> textLines = new ArrayList<>();

            String keybind = CitadelClient.getKeybindString(index);

            textLines.add(Component.literal(ability.getName()));
            textLines.add(Component.literal(ability.getDescription()));
            textLines.add(Component.literal(""));
            textLines.add(Component.literal(keybind));

            List<ClientTooltipComponent> tooltipComponents = textLines.stream()
                    .map(component -> ClientTooltipComponent.create(component.getVisualOrderText()))
                    .toList();

            graphics.tooltip(font, tooltipComponents, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null);
        }
    }
}
