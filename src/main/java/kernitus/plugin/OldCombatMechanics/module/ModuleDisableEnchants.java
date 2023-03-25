package kernitus.plugin.OldCombatMechanics.module;

import kernitus.plugin.OldCombatMechanics.OCMMain;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModuleDisableEnchants extends Module {

    protected List<Enchantment> enchants = new ArrayList<>();
    protected boolean isWhitelist = false;

    public ModuleDisableEnchants(OCMMain plugin) {
        super(plugin, "disable-enchants");
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryOpen(final InventoryOpenEvent event) {
        final HumanEntity viewer = event.getPlayer();
        for (final ItemStack item : event.getInventory()) {
            removeEnchants(item, viewer);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onInventoryInteraction(final InventoryClickEvent event) {
        removeEnchants(event.getCurrentItem(), event.getWhoClicked());
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemPickup(final EntityPickupItemEvent event) {
        removeEnchants(event.getItem().getItemStack(), event.getEntity());
    }

    @Override
    public void reload() {
        this.isWhitelist = module().getBoolean("is-whitelist", false);
        this.enchants = getEnchants();
        this.debug("Loaded enchants: " + this.enchants.size());
    }

    private void removeEnchants(final ItemStack item, final Entity owner) {
        if (item == null || owner == null) {
            return;
        }
        final Map<Enchantment, Integer> enchants = item.getEnchantments();
        for (final Enchantment enchant : enchants.keySet()) {
            if (!this.enchants.contains(enchant) && !isWhitelist) {
                this.debug("Enchant is allowed: " + enchant.toString());
                continue;
            }
            item.removeEnchantment(enchant);
            owner.sendMessage(ChatColor.RED + "The enchantment "
                    + enchant.getKey().getKey() + " is disabled and has "
                    + "been removed from your item!");
        }
    }

    private List<Enchantment> getEnchants(){
        return module().getStringList("enchants").stream()
                .map(id -> Enchantment.getByKey(NamespacedKey.minecraft(id)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
