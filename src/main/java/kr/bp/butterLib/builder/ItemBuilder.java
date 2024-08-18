package kr.bp.butterLib.builder;

import com.google.common.base.Preconditions;
import kr.bp.butterLib.message.MessageColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(ItemStack item) {
        this.itemStack = item;
        this.itemMeta = item.getItemMeta();
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder(Material material, int amount) {
        itemStack = new ItemStack(material, amount);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        Preconditions.checkNotNull(name, "The name cannot be null.");
        itemMeta.setDisplayName(MessageColor.colorText(name));
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        List<String> coloredLore = lore.stream()
                .map(line -> MessageColor.colorText(line))
                .collect(Collectors.toList());
        itemMeta.setLore(coloredLore);
        return this;
    }


    public ItemBuilder setLore(String... lore) {
        return setLore(Arrays.asList(lore));
    }

    public ItemBuilder setCustomModelData(int id) {
        Preconditions.checkNotNull(Integer.valueOf(id), "CustomModelData cannot be null.");
        this.itemMeta.setCustomModelData(Integer.valueOf(id));
        return this;
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
