package de.worldOneo.advancedBankSystem.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class Utils {
    private static final String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ12345678890&";

    public static String colorize(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static String generateId() {
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();
        long time = System.currentTimeMillis();
        StringBuilder timeString = new StringBuilder();
        while (time > 0) {
            int a = (int) Math.floorMod(time, chars.length());
            timeString.append(chars.charAt(a));
            time /= chars.length();
        }
        for (int i = 5; i > 0; i--) {
            stringBuilder.append(chars.charAt(random.nextInt(chars.length())));
        }
        return stringBuilder.append("-").append(timeString.toString()).toString();
    }

    public static ItemStack getNamedItem(Material material, String name) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        itemMeta.setDisplayName(name);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public static List<Player> resolveDisplayName(String displayName) {
        return Bukkit.getOnlinePlayers().stream()
                .filter(player -> player.getDisplayName().equals(displayName))
                .map(Player::getName)
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
