package x.y.z;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.util.math.Vec3d;
import java.util.Random;

public class step extends a {
    private final b<Float> c = d(new b<>("a", 2.0f, 0.5f, 2.5f));
    private final b<Boolean> e = d(new b<>("b", false));
    private final b<Float> f = d(new b<>("c", 0.1f, 0.0f, 0.3f, v -> e.g()));
    private float h;
    private final Random i = new Random();

    public step() {
        super("step", "b", j.k, true, false, false);
    }

    @Override
    public void l() {
        if (m.n == null || m.o == null) return;
        h = (float) m.n.p(EntityAttributes.STEP_HEIGHT);
    }

    @Override
    public void q() {
        if (m.n == null || m.o == null) return;
        m.n.r(EntityAttributes.STEP_HEIGHT).s(h);
    }

    @Override
    public void t() {
        if (m.n == null || m.o == null || m.n.u() || m.n.v()) return;
        
        if (m.n.w() && x()) {
            float y = c.g();
            if (e.g()) {
                y += (i.nextFloat() * f.g() * (i.nextBoolean() ? 1 : -1));
                y = Math.max(0.5f, Math.min(y, 2.5f));
            }
            m.n.r(EntityAttributes.STEP_HEIGHT).s(y);
            
            if (y > 1.0f) {
                Vec3d z = m.n.aa();
                if (m.o.ab(m.n.ac(), m.n.ad() + y, m.n.ae()) && !m.o.ab(m.n.ac(), m.n.ad() + 1.0, m.n.ae())) {
                    m.n.af().ag(new PlayerMoveC2SPacket.PositionAndOnGround(
                        m.n.ac(), m.n.ad() + (y - 1.0), m.n.ae(), m.n.w()));
                }
            }
        } else {
            m.n.r(EntityAttributes.STEP_HEIGHT).s(h);
        }
    }

    private boolean x() {
        Vec3d z = m.n.aa();
        return Math.abs(z.x) > 0.01 || Math.abs(z.z) > 0.01;
    }
}
