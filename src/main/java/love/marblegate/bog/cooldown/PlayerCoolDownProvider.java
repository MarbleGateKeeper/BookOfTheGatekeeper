package love.marblegate.bog.cooldown;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

class PlayerCoolDownProvider implements ICapabilitySerializable<CompoundTag> {
    private final CoolDownCapability coolDownCapability = new CoolDownCapability();
    private final LazyOptional<CoolDownCapability> coolDownOptional = LazyOptional.of(() -> coolDownCapability);

    public void invalidate() {
        coolDownOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return getCapability(cap);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        if (cap == CooldownInstanceHolder.COOL_DOWN_CAPABILITY) return coolDownOptional.cast();
        else return LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundNBT = new CompoundTag();
        if (CooldownInstanceHolder.COOL_DOWN_CAPABILITY != null) {
            compoundNBT.put("cooldown",coolDownCapability.serializeNBT());
        }
        return compoundNBT;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (CooldownInstanceHolder.COOL_DOWN_CAPABILITY != null) {
            coolDownCapability.deserializeNBT(nbt.getCompound("cooldown"));
        }
    }
}
