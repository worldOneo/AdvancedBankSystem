package de.worldOneo.advancedBankSystem;

import de.worldOneo.advancedBankSystem.bankItems.IStoreable;
import de.worldOneo.advancedBankSystem.command.BankCommandExecutor;
import de.worldOneo.advancedBankSystem.gui.InfoGUI;
import de.worldOneo.advancedBankSystem.gui.PayGUI;
import de.worldOneo.advancedBankSystem.gui.YesNoGUI;
import de.worldOneo.advancedBankSystem.listeners.OnInventoryClickEventListener;
import de.worldOneo.advancedBankSystem.listeners.OnPlayerConnectionEvent;
import de.worldOneo.advancedBankSystem.manager.BankAccountManager;
import de.worldOneo.advancedBankSystem.manager.GUIManager;
import de.worldOneo.advancedBankSystem.manager.MySQLManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BankSystem extends JavaPlugin {
    private static BankSystem instance;
    public static final String PLUGIN_NAME = "AdvancedBankSystem";

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        try {
            createConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
        registerEventListener();
        registerCommands();
        registerGUIS();
        if (!MySQLManager.getInstance().isEnabled()) {
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        BankAccountManager.getInstance().getIdBankAccountMap().values().forEach(IStoreable::save);
    }

    private void registerEventListener() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new OnPlayerConnectionEvent(), this);
        pluginManager.registerEvents(new OnInventoryClickEventListener(), this);
    }

    private void createConfig() throws IOException {
        File config = new File("plugins" + File.separator + PLUGIN_NAME);
        InputStream configFile = getResource("config.yml");
        if (!config.exists()) {
            config.mkdir();
            Path commentetConfig = Paths.get("plugins" + File.separator + PLUGIN_NAME + File.separator + "config.yml");
            byte[] bytes = new byte[32];
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int readLength;

            while ((readLength = configFile.read(bytes)) > 0) {
                buffer.write(bytes, 0, readLength);
            }
            Files.write(commentetConfig, buffer.toByteArray());
            Bukkit.getLogger().info("Saving default config");
            configFile.close();
        }
    }

    private void registerCommands() {
        getCommand("banksystem").setExecutor(new BankCommandExecutor());
    }


    private void registerGUIS() {
        GUIManager guiManager = GUIManager.getInstance();
        guiManager.registerGUI(new PayGUI());
        guiManager.registerGUI(new YesNoGUI());
        guiManager.registerGUI(new InfoGUI());
    }

    public static BankSystem getInstance() {
        return instance;
    }
}
