package de.worldOneo.advancedBankSystem.gui;

import de.worldOneo.advancedBankSystem.utils.Utils;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class ValueSelectorGUI extends AbstractGUI {
    @Override
    public IGUI getInstance() {
        return this;
    }



    /**
     * @param callback is called with the selected value.
     */
    @Override
    public void open(Player player, Consumer<Object> callback) {
        super.open(player, callback);
    }

    @Override
    public String getGUITitle() {
        return Utils.colorize("&a&rSelect Value");
    }
}
