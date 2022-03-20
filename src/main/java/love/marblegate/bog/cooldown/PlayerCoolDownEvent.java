package love.marblegate.bog.cooldown;

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
public class PlayerCoolDownEvent {
    @SubscribeEvent
    public static void registerCaps(RegisterCapabilitiesEvent event) {
        event.register(CoolDownCapability.class);
    }

    @SubscribeEvent
    public static void tick(TickEvent.PlayerTickEvent event) {
        if (!event.player.level.isClientSide()) {
            if (event.phase == TickEvent.Phase.START) {
                LazyOptional<CoolDownCapability> coolDownCap = event.player.getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
                coolDownCap.ifPresent(CoolDownCapability::tick);
            }
        }
    }

    // Attaching Cooldown to Player
    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            PlayerCoolDownProvider provider = new PlayerCoolDownProvider();
            event.addCapability(new ResourceLocation("book_of_the_gatekeeper", "cooldown"), provider);
            event.addListener(provider::invalidate);
        }
    }

    //Manager Cooldown Transferring Event
    @SubscribeEvent
    public static void migrateCapDataWhenPlayerRespawn(PlayerEvent.Clone event) {
        if (!event.getPlayer().level.isClientSide()) {
            LazyOptional<CoolDownCapability> oldCoolDown = event.getOriginal().getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
            LazyOptional<CoolDownCapability> newCoolDown = event.getPlayer().getCapability(CooldownInstanceHolder.COOL_DOWN_CAPABILITY);
            if (oldCoolDown.isPresent() && newCoolDown.isPresent()) {
                newCoolDown.ifPresent((newCap) -> oldCoolDown.ifPresent((oldCap) -> {
                    newCap.receiveData(oldCap.offerData());
                }));
            }
        }
    }
}
