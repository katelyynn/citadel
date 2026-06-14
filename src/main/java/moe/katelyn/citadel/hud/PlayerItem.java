package moe.katelyn.citadel.hud;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import moe.katelyn.citadel.CitadelClient;
import moe.katelyn.citadel.CitadelTeamRegistry;
import moe.katelyn.citadel.PlayerHero;
import moe.katelyn.citadel.hero.HeroPoser;
import moe.katelyn.citadel.mixin.MannequinMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientMannequin;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerFaceExtractor;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ResolvableProfile;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerItem {
    private final int index;
    private int x;
    private int y;
    private boolean hovered;

    private PlayerInfo playerInfo;
    private String name;
    private int ping;
    private UUID uuid;
    private CitadelTeamRegistry.Team team;

    private long lastScroll = 0;
    private int scrollOffset = 0;
    private static final int SCROLL_SPEED = 500;
    private static final int MAX_CHARS = 8;

    public PlayerItem(int index) {
        this.index = index;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(PlayerInfo info) {
        this.playerInfo = info;
        this.name = info.getProfile().name();
        this.ping = info.getLatency();
        this.uuid = info.getProfile().id();
        this.team = CitadelTeamRegistry.getTeam(uuid);
    }

    public void render(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean tab) {
        if (playerInfo == null) return;

        Font font = Minecraft.getInstance().font;

        if (tab) {
            graphics.fillGradient(x, y, x + PlayerList.WIDTH, y + PlayerList.HEIGHT + PlayerList.PADDING, team.getColour(), 0x25111111);
        }

        int avatarExtra = 10;

        int avatarX = x + PlayerList.AVATAR_X;
        int avatarY = y + PlayerList.PADDING;

        int avatarPadding = PlayerList.PADDING;

        // this is meant to be smaller than the avatar, not a border
        graphics.fill(avatarX, avatarY + avatarPadding, avatarX + PlayerList.AVATAR_WIDTH, avatarY + PlayerList.AVATAR_WIDTH + avatarPadding, team.getColour());

        Minecraft client = Minecraft.getInstance();
        GameProfile profile = playerInfo.getProfile();

        FakePlayer fakePlayer = new FakePlayer(client.level, profile);

        ResolvableProfile resolvableProfile = ResolvableProfile.createResolved(profile);
        client.getSkinManager().createLookup(profile, false);

        Identifier hero = Identifier.tryParse(CitadelClient.getPlayerHero(uuid));

        Quaternionf rotation = HeroPoser.rotate(hero);
        Quaternionf xRotation = HeroPoser.rotate2(hero);
        rotation.mul(xRotation);

        EntityRenderState renderState = extractRenderState(fakePlayer);
        if (renderState == null) return;

        if (renderState instanceof AvatarRenderState avatarRenderState) {
            HeroPoser.pose(avatarRenderState, hero);
        }

        Vector3f translation = new Vector3f(0.0f, renderState.boundingBoxHeight / 2.0f + 1.3f, 0.0f);

        graphics.entity(renderState, PlayerList.AVATAR_WIDTH, translation, rotation, xRotation, avatarX - avatarExtra, avatarY - avatarPadding, avatarX + PlayerList.AVATAR_WIDTH + avatarExtra, avatarY + PlayerList.AVATAR_WIDTH + avatarPadding);

        //PlayerFaceExtractor.extractRenderState(graphics, playerInfo.getSkin().body().texturePath(), avatarX, avatarY, PlayerList.AVATAR_WIDTH, true, false, -1);
        long now = System.currentTimeMillis();

        if (now - lastScroll > SCROLL_SPEED) {
            scrollOffset++;
            lastScroll = now;

            int maxOffset = name.length() + 1;
            if (scrollOffset > maxOffset) {
                scrollOffset = 0;
            }
        }

        if (!tab) {
            return;
        }

        String displayName = name;

        int textWidth = font.width(displayName);
        int textX = x + (PlayerList.WIDTH - textWidth) / 2;
        int textY = y + PlayerList.PADDING + PlayerList.AVATAR_WIDTH + avatarPadding + PlayerList.PADDING;

        graphics.text(font, Component.literal(displayName), textX, textY, 0xFFFFFFFF, true);

        String heroName = hero.getPath();
        int heroWidth = font.width(heroName);
        int heroX = x + (PlayerList.WIDTH - heroWidth) / 2;
        int heroY = y + PlayerList.PADDING + PlayerList.AVATAR_WIDTH + avatarPadding + PlayerList.PADDING + 10;

        graphics.text(font, Component.literal(heroName), heroX, heroY, 0xFFDDDDDD, true);
    }

    private static EntityRenderState extractRenderState(final LivingEntity entity) {
        EntityRenderDispatcher entityRenderDispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, ?> renderer = entityRenderDispatcher.getRenderer(entity);

        if (renderer == null) return null;

        EntityRenderState renderState = renderer.createRenderState(entity, 1.0F);
        renderState.shadowPieces.clear();
        renderState.outlineColor = 0;
        return renderState;
    }
}
