package love.marblegate.bog.point;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
class PointSystemInstanceHolder {
    // Holding the Capability Instances
    public static Capability<PointSystemCapability> INSTANCE = CapabilityManager.get(new CapabilityToken<>() {});
}
