package love.marblegate.bog.cooldown;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
class CooldownInstanceHolder {
    // Holding the Capability Instances
    public static Capability<CoolDownCapability> COOL_DOWN_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
}
