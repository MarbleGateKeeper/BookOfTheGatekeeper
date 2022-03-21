package love.marblegate.bog.datastorage;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class PlayerDataProvider implements ICapabilitySerializable<CompoundTag> {
    private final CoolDownCapability coolDownCapability = new CoolDownCapability();
    private final LazyOptional<CoolDownCapability> coolDownCapabilityLazyOptional = LazyOptional.of(() -> coolDownCapability);
    private final PointSystemCapability pointSystemCapability = new PointSystemCapability();
    private final LazyOptional<PointSystemCapability> pointSystemCapabilityLazyOptional = LazyOptional.of(() -> pointSystemCapability);

    public void invalidate() {
        coolDownCapabilityLazyOptional.invalidate();
        pointSystemCapabilityLazyOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == InstanceHolder.COOLDOWN_SYSTEM) return coolDownCapabilityLazyOptional.cast();
        if (cap == InstanceHolder.POINT_SYSTEM) return pointSystemCapabilityLazyOptional.cast();
        else return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        if (InstanceHolder.COOLDOWN_SYSTEM != null) {
            compoundNBT.put("cooldown_system", coolDownCapability.serializeNBT());
        }
        if (InstanceHolder.POINT_SYSTEM != null) {
            compoundNBT.put("point_system", pointSystemCapability.serializeNBT());
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (InstanceHolder.COOLDOWN_SYSTEM != null) {
            coolDownCapability.deserializeNBT(nbt.getCompound("cooldown_system"));
        }
        if (InstanceHolder.POINT_SYSTEM != null) {
            pointSystemCapability.deserializeNBT(nbt.getCompound("point_system"));
        }
    }
}
