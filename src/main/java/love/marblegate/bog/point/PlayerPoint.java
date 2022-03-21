package love.marblegate.bog.point;

import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class PlayerPoint {
    public double get(Player player, String type) {
        LazyOptional<PointSystemCapability> pointCap = player.getCapability(PointSystemInstanceHolder.INSTANCE);
        AtomicDouble ret = new AtomicDouble(0.0);
        pointCap.ifPresent(pointSystemCapability -> ret.set(pointSystemCapability.get(type)));
        return ret.get();
    }

    public void set(Player player, String type, double point) {
        LazyOptional<PointSystemCapability> pointCap = player.getCapability(PointSystemInstanceHolder.INSTANCE);
        pointCap.ifPresent(pointSystemCapability -> pointSystemCapability.set(type,point));
    }

    public void add(Player player, String type, double point) {
        LazyOptional<PointSystemCapability> pointCap = player.getCapability(PointSystemInstanceHolder.INSTANCE);
        pointCap.ifPresent(pointSystemCapability -> pointSystemCapability.add(type,point));
    }

    public void consume(Player player, String type, double point) {
        LazyOptional<PointSystemCapability> pointCap = player.getCapability(PointSystemInstanceHolder.INSTANCE);
        pointCap.ifPresent(pointSystemCapability -> pointSystemCapability.consume(type,point));
    }

    public void manipulate(Player player, String type, Function<Double,Double> function) {
        LazyOptional<PointSystemCapability> pointCap = player.getCapability(PointSystemInstanceHolder.INSTANCE);
        pointCap.ifPresent(pointSystemCapability -> pointSystemCapability.manipulate(type,function));
    }
}
