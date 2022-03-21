package love.marblegate.bog.datastorage;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
class InstanceHolder {
    // Holding the Capability Instances
    public static Capability<PointSystemCapability> POINT_SYSTEM = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static Capability<CoolDownCapability> COOLDOWN_SYSTEM = CapabilityManager.get(new CapabilityToken<>() {
    });
}
