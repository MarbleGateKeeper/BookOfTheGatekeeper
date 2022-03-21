package love.marblegate.bog.datastorage;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CapabilityEvent {
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(CoolDownCapability.class);
        event.register(PointSystemCapability.class);
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level.isClientSide()) {
            if (event.phase == TickEvent.Phase.START) {
                LazyOptional<CoolDownCapability> coolDownCap = event.player.getCapability(InstanceHolder.COOLDOWN_SYSTEM);
                coolDownCap.ifPresent(CoolDownCapability::tick);
            }
        }
    }

    // Attaching Cooldown to Player
    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            PlayerDataProvider provider = new PlayerDataProvider();
            event.addCapability(new ResourceLocation("book_of_the_gatekeeper", "point_system"), provider);
            event.addListener(provider::invalidate);
        }
    }

    //Manager Cooldown Transferring Event
    @SubscribeEvent
    public static void migrateCapDataWhenPlayerRespawn(PlayerEvent.Clone event) {
        if (!event.getPlayer().level.isClientSide()) {
            LazyOptional<PointSystemCapability> oldPointCap = event.getOriginal().getCapability(InstanceHolder.POINT_SYSTEM);
            LazyOptional<PointSystemCapability> newPointCap = event.getPlayer().getCapability(InstanceHolder.POINT_SYSTEM);
            if (event.isWasDeath()) {
                if (oldPointCap.isPresent() && newPointCap.isPresent()) {
                    newPointCap.ifPresent((newCap) -> oldPointCap.ifPresent((oldCap) -> {
                        newCap.receiveData(oldCap.offerDataPartial());
                    }));
                }
            } else {
                if (oldPointCap.isPresent() && newPointCap.isPresent()) {
                    newPointCap.ifPresent((newCap) -> oldPointCap.ifPresent((oldCap) -> {
                        newCap.receiveData(oldCap.offerDataComplete());
                    }));
                }
            }
            LazyOptional<CoolDownCapability> oldCoolDown = event.getOriginal().getCapability(InstanceHolder.COOLDOWN_SYSTEM);
            LazyOptional<CoolDownCapability> newCoolDown = event.getPlayer().getCapability(InstanceHolder.COOLDOWN_SYSTEM);
            if (oldCoolDown.isPresent() && newCoolDown.isPresent()) {
                newCoolDown.ifPresent((newCap) -> oldCoolDown.ifPresent((oldCap) -> {
                    newCap.receiveData(oldCap.offerData());
                }));
            }
        }
    }
}
