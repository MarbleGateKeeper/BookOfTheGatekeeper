package love.marblegate.bog.datastorage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;

import java.util.*;

class CoolDownCapability {
    private Map<String, CoolDownInstance> coolDownMap;
    private Map<String, Integer> holdMap;
    private int tickCount;

    public CoolDownCapability() {
        coolDownMap = new HashMap<>();
        holdMap = new HashMap<>();
        tickCount = 0;
    }

    public boolean isReady(String type) {
        return !coolDownMap.containsKey(type);
    }

    public void setCoolDown(String type, int coolDownLength) {
        coolDownMap.put(type, new CoolDownInstance(tickCount, tickCount + coolDownLength));
    }

    public void addCoolDown(String type, int coolDownLength) {
        if (coolDownMap.containsKey(type)) {
            coolDownMap.put(type, new CoolDownInstance(coolDownMap.get(type).startTime, coolDownMap.get(type).endTime + coolDownLength));
        } else {
            coolDownMap.put(type, new CoolDownInstance(tickCount, tickCount + coolDownLength));
        }
    }

    public float getCooldownPercent(String type) {
        if (coolDownMap.containsKey(type)) {
            CoolDownInstance coolDownInstance = coolDownMap.get(type);
            float f = coolDownInstance.endTime - coolDownInstance.startTime;
            float f1 = coolDownInstance.endTime - this.tickCount;
            return Mth.clamp(f1 / f, 0.0F, 1.0F);
        } else {
            return 0.0F;
        }
    }

    public void hold(String type, int holdLength) {
        if (coolDownMap.containsKey(type)) {
            holdMap.put(type, holdLength);
        }
    }

    public boolean isHolding(String type) {
        return holdMap.containsKey(type);
    }

    public void cancelHolding(String type) {
        holdMap.remove(type);
    }

    public void holdLonger(String type, int holdLength) {
        if (holdMap.containsKey(type)) {
            holdMap.put(type, holdMap.get(type) + holdLength);
        } else {
            holdMap.put(type, holdLength);
        }
    }

    public void cancel(String type) {
        coolDownMap.remove(type);
        holdMap.remove(type);
    }

    public void tick() {
        Iterator<Map.Entry<String, Integer>> hIterator = holdMap.entrySet().iterator();
        while (hIterator.hasNext()) {
            Map.Entry<String, Integer> hEntry = hIterator.next();
            if (hEntry.getValue() == 1) {
                hIterator.remove();
            } else {
                hEntry.setValue(hEntry.getValue() - 1);
            }
        }
        Iterator<Map.Entry<String, CoolDownInstance>> cIterator = coolDownMap.entrySet().iterator();
        while (cIterator.hasNext()) {
            Map.Entry<String, CoolDownInstance> cEntry = cIterator.next();
            if (!holdMap.containsKey(cEntry.getKey())) {
                if (cEntry.getValue().endTime == tickCount) {
                    cIterator.remove();
                } else {
                    cEntry.getValue().holdInTick();
                }
            }
        }
    }

    @SuppressWarnings("all")
    public void receiveData(List<Object> data) {
        coolDownMap = (Map<String, CoolDownInstance>) data.get(0);
        holdMap = (Map<String, Integer>) data.get(1);
        tickCount = (Integer) data.get(2);
    }

    public CompoundTag serializeNBT() {
        CompoundTag ret = new CompoundTag();
        CompoundTag cMapNBT = new CompoundTag();
        CompoundTag hMapNBT = new CompoundTag();
        for (Map.Entry<String, CoolDownInstance> entry : coolDownMap.entrySet()) {
            CompoundTag cInstanceNBT = new CompoundTag();
            cInstanceNBT.putInt("start_time", entry.getValue().startTime);
            cInstanceNBT.putInt("end_time", entry.getValue().endTime);
            cMapNBT.put(entry.getKey(), cInstanceNBT);
        }
        ret.put("c_map", cMapNBT);
        for (Map.Entry<String, Integer> entry : holdMap.entrySet()) {
            hMapNBT.putInt(entry.getKey(), entry.getValue());
        }
        ret.put("h_map", hMapNBT);
        ret.putInt("tick_count", tickCount);
        return ret;
    }

    public void deserializeNBT(CompoundTag nbt) {
        CompoundTag cMapNBT = nbt.getCompound("c_map");
        coolDownMap = new HashMap<>();
        Set<String> typeSet = cMapNBT.getAllKeys();
        for (String type : typeSet) {
            CompoundTag cInstanceNBT = cMapNBT.getCompound(type);
            coolDownMap.put(type, new CoolDownInstance(cInstanceNBT.getInt("start_time"), cInstanceNBT.getInt("end_time")));
        }

        CompoundTag hMapNBT = nbt.getCompound("h_map");
        holdMap = new HashMap<>();
        typeSet = hMapNBT.getAllKeys();
        for (String type : typeSet) {
            holdMap.put(type, hMapNBT.getInt(type));
        }

        tickCount = nbt.getInt("tick_count");
    }

    public List<Object> offerData() {
        List<Object> ret = new ArrayList<>();
        ret.add(coolDownMap);
        ret.add(holdMap);
        ret.add(tickCount);
        return ret;
    }

    public int getCoolDown(String type) {
        if (coolDownMap.containsKey(type)) {
            return coolDownMap.get(type).endTime - tickCount;
        } else {
            return 0;
        }
    }

    static class CoolDownInstance {
        final int startTime;
        int endTime;

        CoolDownInstance(int startTime, int endTime) {
            this.startTime = startTime;
            this.endTime = endTime;
        }

        void holdInTick() {
            endTime++;
        }
    }
}
