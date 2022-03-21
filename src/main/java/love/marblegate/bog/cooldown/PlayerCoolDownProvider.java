package love.marblegate.bog.cooldown;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class PlayerCoolDownProvider implements ICapabilitySerializable<CompoundTag> {
    private final CoolDownCapability capability = new CoolDownCapability();
    private final LazyOptional<CoolDownCapability> optional = LazyOptional.of(() -> capability);

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
        if (cap == CooldownInstanceHolder.INSTANCE) return optional.cast();
        else return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        if (CooldownInstanceHolder.INSTANCE != null) {
            compoundNBT.put("cooldown", capability.serializeNBT());
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (CooldownInstanceHolder.INSTANCE != null) {
            capability.deserializeNBT(nbt.getCompound("cooldown"));
        }
    }
}
