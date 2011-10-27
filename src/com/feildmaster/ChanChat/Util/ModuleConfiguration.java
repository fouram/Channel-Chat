package com.feildmaster.chanchat.Util;

import com.feildmaster.chanchat.Chat;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Configuration class for simple configuration setting for ChannelChat Plugins
 */
public class ModuleConfiguration extends YamlConfiguration {
    private final JavaPlugin plugin;
    private final File file;
    private InputStream stream;

    /**
     * Creates a new ModuleConfiguration for ChannelChat modules.
     * Uses plugin name for file name
     *
     * @param p Plugin creating the configuration
     */
    public ModuleConfiguration(JavaPlugin p) {
        this(p, p.getDescription().getName()+".yml");
    }

    /**
     * Creates a new ModuleConfiguration for ChannelChat modules
     *
     * @param p Plugin creating the configuration
     * @param configName The name of the default YamlConfiguration file. (Ex: "PluginConfig.yml")
     */
    public ModuleConfiguration(JavaPlugin p, String configName) {
        plugin = p;
        file = new File(Chat.getModuleFolder(), configName);

        load();
        loadDefaults(configName);
    }

    /**
     * Loads the configuration from disk
     *
     * @return True if loaded successfully
     */
    public final boolean load() {
        try {
            load(file);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Saves current configuration (plus defaults) to disk.
     *
     * If defaults and configuration are empty, saves blank file.
     *
     * @return True if saved successfully
     */
    public final boolean saveDefaults() {
        try {
            options().copyDefaults(true);
            save(file);
        } catch (Exception ex) {
            return false;
        } finally {
            options().copyDefaults(false);
            return true;
        }
    }

    /**
     * Saves the configuration to disk
     *
     * @return True if saved successfully
     */
    public final boolean save() {
        try {
            save(file);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Simple function for if the Module file exists
     *
     * @return True if configuration exists on disk
     */
    public final boolean exists() {
        return file.exists();
    }

    /**
     * Loads a file from the plugin jar and sets as default
     *
     * @param filename The filename to open
     */
    public final void loadDefaults(String filename) {
        try {
            URL url = plugin.getClass().getClassLoader().getResource(filename);
            URLConnection con = url.openConnection();
            con.setUseCaches(false);
            stream = con.getInputStream();
        } catch (Exception ex) {
            stream = null;
            // Maybe add a logger message.
        }

        if(stream != null)
            setDefaults(YamlConfiguration.loadConfiguration(stream));
    }

    public final ChatColor getChatColor(String path) {
        return ChatColor.WHITE;
    }
}
