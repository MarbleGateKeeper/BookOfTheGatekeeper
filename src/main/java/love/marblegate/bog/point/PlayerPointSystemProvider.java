package love.marblegate.bog.point;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class PlayerPointSystemProvider implements ICapabilitySerializable<CompoundTag> {
    private final PointSystemCapability capability = new PointSystemCapability();
    private final LazyOptional<PointSystemCapability> optional = LazyOptional.of(() -> capability);

    public void invalidate() {
        optional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == PointSystemInstanceHolder.INSTANCE) return optional.cast();
        else return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        if (PointSystemInstanceHolder.INSTANCE != null) {
            compoundNBT.put("point_system",capability.serializeNBT());
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (PointSystemInstanceHolder.INSTANCE != null) {
            capability.deserializeNBT(nbt.getCompound("point_system"));
        }
    }
}
