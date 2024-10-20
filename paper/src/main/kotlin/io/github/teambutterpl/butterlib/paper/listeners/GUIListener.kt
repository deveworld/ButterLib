package io.github.teambutterpl.butterlib.paper.listeners

import io.github.teambutterpl.butterlib.paper.ButterLib
import io.github.teambutterpl.butterlib.paper.ButterLibrary
import io.github.teambutterpl.butterlib.paper.gui.BaseGUI
import io.github.teambutterpl.butterlib.paper.listeners.BaseListener
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack


class GUIListener(plugin: ButterLib) : BaseListener(plugin) {
    @EventHandler
    fun onInventoryClick(event: InventoryClickEvent) {
        val inventory: Inventory = event.clickedInventory ?: return
        for (gui in ButterLibrary.getGUIManager().getGUIs()) {
            val holder = inventory.getHolder(false) ?: continue
            if (gui.isInstance(holder)) {
                if (holder as? BaseGUI == null) continue
                event.isCancelled = holder.preventUserChange
                holder.onClick(event)
            }
        }
    }

    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val inventory: Inventory = event.inventory
        for (gui in ButterLibrary.getGUIManager().getGUIs()) {
            val holder = inventory.getHolder(false) ?: continue
            if (gui.isInstance(holder)) {
                if (holder as? BaseGUI == null) continue
                holder.onClose(event)
            }
        }
    }
}