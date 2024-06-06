package fr.maxlego08.essentials.nms.r3_1_20;

import fr.maxlego08.essentials.api.NmsUtils;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NmsHelper implements NmsUtils {
    @Override
    public String getItemStackTags(ItemStack itemStack) {
        CompoundTag nbtTagCompound = new CompoundTag();
        net.minecraft.world.item.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        CompoundTag nbt = nmsItemStack.save(nbtTagCompound);
        return nbt.getCompound("tag").toString();
    }
}
