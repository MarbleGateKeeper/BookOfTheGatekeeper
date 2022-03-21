package love.marblegate.bog.point;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;

import java.util.*;
import java.util.function.Function;

class PointSystemCapability {
    /**
     * If the type name starts with "_", the point resets after death.
     */
    private Map<String, Double> pMap;

    public PointSystemCapability() {
        pMap = new HashMap<>();
    }

    public double get(String type) {
        return pMap.getOrDefault(type,0.0);
    }

    public void set(String type, double point) {
        if(point<0)
            throw new IllegalArgumentException("Point Value Cannot < 0");
        pMap.put(type,point);
    }

    public void add(String type, double point) {
        if(point<0)
            throw new IllegalArgumentException("Point Value Cannot < 0");
        pMap.put(type,pMap.getOrDefault(type,0.0)+point);
    }

    public void consume(String type, double point) {
        if(point<0)
            throw new IllegalArgumentException("Point Value Cannot < 0");
        pMap.put(type,Math.max(pMap.getOrDefault(type,0.0)-point,0.0));
    }

    public void manipulate(String type, Function<Double,Double> function) {
        pMap.put(type,Math.max(function.apply(pMap.getOrDefault(type,0.0)),0.0));
    }

    public void receiveData(Map<String, Double> data){
        pMap = data;
    }

    public CompoundTag serializeNBT(){
        CompoundTag ret = new CompoundTag();
        for (Map.Entry<String, Double> entry:pMap.entrySet()) {
            ret.putDouble(entry.getKey(),entry.getValue());
        }
        return ret;
    }

    public void deserializeNBT(CompoundTag nbt){
        Map<String, Double> data = new HashMap<>();
        Set<String> typeSet = nbt.getAllKeys();
        for(String type:typeSet){
            data.put(type,nbt.getDouble(type));
        }
        pMap = data;
    }

    public Map<String, Double> offerData(){
        return Maps.filterEntries(pMap, input -> !input.getKey().startsWith("_"));
    }
}
