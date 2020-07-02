package de.worldOneo.advancedBankSystem.manager;

import de.worldOneo.advancedBankSystem.gui.IGUI;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

public class GUIManager {
    private static final GUIManager instance = new GUIManager();
    private final Map<String, IGUI> titleGUIMap = new HashMap<>();
    private final Map<Class<? extends IGUI>, String> clazzTitleMap = new HashMap<>();

    private GUIManager() {

    }

    public void registerGUI(IGUI igui) {
        titleGUIMap.put(igui.getGUITitle(), igui);
        clazzTitleMap.put(igui.getClass(), igui.getGUITitle());
    }

    public IGUI getGui(Class<? extends IGUI> clazz) {
        return titleGUIMap.get(clazzTitleMap.get(clazz)).getInstance();
    }

    public void handle(InventoryClickEvent e) {
        if (titleGUIMap.get(e.getView().getTitle()) == null) {
            return;
        }
        titleGUIMap.get(e.getView().getTitle()).handle(e);
    }

    public static GUIManager getInstance() {
        return instance;
    }
}
