package love.marblegate.bog.cooldown;

import com.google.common.util.concurrent.AtomicDouble;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

import java.util.concurrent.atomic.AtomicBoolean;

public class PlayerCoolDown {
    public static boolean isReady(Player player, String type){
        LazyOptional<CoolDownCapability> pointCap = player.getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
        AtomicBoolean ret = new AtomicBoolean(true);
        pointCap.ifPresent(coolDownCapability -> ret.set(coolDownCapability.isReady(type)));
        return ret.get();
    }

    public static void setCoolDown(Player player, String type, int coolDownLength) {
        LazyOptional<CoolDownCapability> pointCap = player.getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
        pointCap.ifPresent(coolDownCapability -> coolDownCapability.setCoolDown(type,coolDownLength));
    }

    public static void addCoolDown(Player player, String type, int coolDownLength) {
        LazyOptional<CoolDownCapability> pointCap = player.getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
        pointCap.ifPresent(coolDownCapability -> coolDownCapability.addCoolDown(type,coolDownLength));
    }

    public static float getCooldownPercent(Player player, String type) {
        LazyOptional<CoolDownCapability> pointCap = player.getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
        AtomicDouble ret = new AtomicDouble(0.00);
        pointCap.ifPresent(coolDownCapability -> ret.set(coolDownCapability.getCooldownPercent(type)));
        return (float) ret.get();
    }

    public static void hold(Player player, String type, int holdLength) {
        LazyOptional<CoolDownCapability> pointCap = player.getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
        pointCap.ifPresent(coolDownCapability -> coolDownCapability.hold(type,holdLength));
    }

    public static boolean isHolding(Player player, String type){
        LazyOptional<CoolDownCapability> pointCap = player.getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
        AtomicBoolean ret = new AtomicBoolean(false);
        pointCap.ifPresent(coolDownCapability -> ret.set(coolDownCapability.isHolding(type)));
        return ret.get();
    }

    public static void cancelHolding(Player player, String type) {
        LazyOptional<CoolDownCapability> pointCap = player.getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
        pointCap.ifPresent(coolDownCapability -> coolDownCapability.cancelHolding(type));
    }

    public static void holdLonger(Player player, String type, int holdLength) {
        LazyOptional<CoolDownCapability> pointCap = player.getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
        pointCap.ifPresent(coolDownCapability -> coolDownCapability.holdLonger(type,holdLength));
    }

    public static void cancel(Player player, String type) {
        LazyOptional<CoolDownCapability> pointCap = player.getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
        pointCap.ifPresent(coolDownCapability -> coolDownCapability.cancel(type));
    }
}
