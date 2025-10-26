package me.alpha432.oyvey.features.modules.combat;

import com.google.common.eventbus.Subscribe;
import me.alpha432.oyvey.event.impl.PacketEvent;
import me.alpha432.oyvey.features.modules.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class Criticals extends Module {
    private final Random random = new Random();
    private long lastPacketTime = 0;

    public Criticals() {
        super("crits", "enhances attack precision", Category.COMBAT, true, false, false);
    }

    @Subscribe
    private void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof PlayerInteractEntityC2SPacket packet && packet.type.getType() == PlayerInteractEntityC2SPacket.InteractType.ATTACK) {
            Entity entity = mc.world.getEntityById(packet.entityId);
            if (entity == null
                    || entity instanceof EndCrystalEntity
                    || !mc.player.isOnGround()
                    || mc.player.isSprinting()
                    || mc.player.fallDistance > 0.1f
                    || !(entity instanceof LivingEntity)
                    || System.currentTimeMillis() - lastPacketTime < 50 + random.nextInt(20)) {
                return;
            }

            float offset = 0.05f + random.nextFloat() * 0.1f;
            boolean bl = mc.player.horizontalCollision;

            mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                    mc.player.getX(),
                    mc.player.getY() + offset,
                    mc.player.getZ(),
                    false,
                    bl
            ));

            if (random.nextBoolean()) {
                mc.player.networkHandler.sendPacket(new PlayerMoveC2SPacket.PositionAndOnGround(
                        mc.player.getX(),
                        mc.player.getY(),
                        mc.player.getZ(),
                        true,
                        bl
                ));
            }

            if (random.nextFloat() > 0.3f) {
                mc.player.addCritParticles(entity);
            }

            lastPacketTime = System.currentTimeMillis();
        }
    }

    @Override
    public String getDisplayInfo() {
        return "Enhanced";
    }
}
