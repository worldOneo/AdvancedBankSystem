package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.manager.GUIManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class AbstractGUI implements IGUI {
    private final Player player;

    public AbstractGUI(Player player) {
        this.player = player;
    }

    public void open() {
        GUIManager.getInstance().open(this);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean handle(InventoryClickEvent e) {
        e.setCancelled(true);
        return true;
    }
}
