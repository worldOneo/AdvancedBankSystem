package de.worldOneo.advancedBankSystem.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

public interface IGUI {
    boolean handle(InventoryClickEvent e);

    IGUI getInstance();

    String getGUITitle();

    void open(Player player, Consumer<Object> callback);

    void open(Player player, Object obj, Consumer<Object> callback);
}
