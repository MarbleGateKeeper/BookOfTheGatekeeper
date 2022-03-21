package love.marblegate.bog.point;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class PlayerCoolDownEvent {
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(PointSystemCapability.class);
    }


    // Attaching Cooldown to Player
    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            PlayerPointSystemProvider provider = new PlayerPointSystemProvider();
            event.addCapability(new ResourceLocation("book_of_the_gatekeeper", "point_system"), provider);
            event.addListener(provider::invalidate);
        }
    }

    //Manager Cooldown Transferring Event
    @SubscribeEvent
    public static void migrateCapDataWhenPlayerRespawn(PlayerEvent.Clone event) {
        if (!event.getPlayer().level.isClientSide()) {
            LazyOptional<PointSystemCapability> oldCoolDown = event.getOriginal().getCapability(PointSystemInstanceHolder.INSTANCE);
            LazyOptional<PointSystemCapability> newCoolDown = event.getPlayer().getCapability(PointSystemInstanceHolder.INSTANCE);
            if (oldCoolDown.isPresent() && newCoolDown.isPresent()) {
                newCoolDown.ifPresent((newCap) -> oldCoolDown.ifPresent((oldCap) -> {
                    newCap.receiveData(oldCap.offerData());
                }));
            }
        }
    }
}
