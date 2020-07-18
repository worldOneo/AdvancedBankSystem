package de.worldOneo.advancedBankSystem.gui;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public abstract class AbstractInputGUI<T> extends AbstractGUI {
    private Consumer<T> successConsumer;

    public AbstractInputGUI(Player player) {
        super(player);
    }

    public void setSuccessConsumer(Consumer<T> successConsumer) {
        this.successConsumer = successConsumer;
    }

    public Consumer<T> getSuccessConsumer() {
        return successConsumer;
    }

    protected void commitValue() {
        if (successConsumer != null) {
            successConsumer.accept(getValue());
        }
    }

    public abstract T getValue();
}
