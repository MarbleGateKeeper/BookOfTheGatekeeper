package love.marblegate.bog.utils;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.Map;

public class EnchantmentUtils {
    public static boolean has(Enchantment enchantment, ItemStack itemStack) {
        Map<Enchantment, Integer> enchantList = EnchantmentHelper.getEnchantments(itemStack);
        return enchantList.containsKey(enchantment);
    }

    public static boolean has(Enchantment enchantment, LivingEntity livingEntity, EquipmentSlot slot) {
        return has(enchantment, livingEntity.getItemBySlot(slot));
    }

    public static int get(Enchantment enchantment, ItemStack itemStack) {
        Map<Enchantment, Integer> enchantList = EnchantmentHelper.getEnchantments(itemStack);
        return enchantList.getOrDefault(enchantment, 0);
    }

    public static int get(Enchantment enchantment, LivingEntity livingEntity, EquipmentSlot slot) {
        return get(enchantment, livingEntity.getItemBySlot(slot));
    }

    public static int totalInArmor(Enchantment enchantment, LivingEntity livingEntity) {
        int ret = 0;
        for (ItemStack itemStack : livingEntity.getArmorSlots()) {
            ret += get(enchantment, itemStack);
        }
        return ret;
    }

    public static void erase(Enchantment enchantment, ItemStack itemStack) {
        Map<Enchantment, Integer> enchantList = EnchantmentHelper.getEnchantments(itemStack);
        enchantList.remove(enchantment);
        EnchantmentHelper.setEnchantments(enchantList, itemStack);
    }

    public static void set(Enchantment enchantment, int level, ItemStack itemStack) {
        if (level < 0)
            throw new IllegalArgumentException("Enchantment Level Cannot < 0!");
        Map<Enchantment, Integer> enchantList = EnchantmentHelper.getEnchantments(itemStack);
        enchantList.put(enchantment, level);
        EnchantmentHelper.setEnchantments(enchantList, itemStack);

    }
}
