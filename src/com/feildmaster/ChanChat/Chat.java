package com.feildmaster.chanchat;

import com.feildmaster.chanchat.Chan.Channel;
import com.feildmaster.chanchat.Chan.ChannelManager;
import com.feildmaster.chanchat.Commands.*;
import com.feildmaster.chanchat.Listeners.*;
import com.feildmaster.chanchat.Util.*;
import java.io.File;
import org.blockface.stats.CallHome;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

// TODO: Default "set" channel
// TODO: /cc help or something
public class Chat extends JavaPlugin {
    private static Chat plugin;
    private static File module_folder = new File("plugins/ChannelChat/modules");
    private ChatConfig config;
    private ChanConfig cConfig;

    public void onDisable() {
        save();

        getServer().getLogger().info(getDescription().getName()+" v"+getDescription().getVersion()+" disabled");
    }

    public void onEnable() {
        plugin = this;
        CallHome.load(this);
        PluginManager pm = getServer().getPluginManager();

        // Events
        MonitorPlayerListener monitor = new MonitorPlayerListener();
        pm.registerEvent(Type.PLAYER_JOIN, monitor, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_QUIT, monitor, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_KICK, monitor, Priority.Monitor, this);
        pm.registerEvent(Type.PLAYER_CHAT, new ChatListener(), Priority.Highest, this);
        pm.registerEvent(Type.PLAYER_CHAT, new EarlyChatListener(), Priority.Lowest, this);

        // Setup configs. :D
        config = new ChatConfig(new File(getDataFolder(), "config.yml"));
        cConfig = new ChanConfig(new Configuration(new File(getDataFolder(), "channels.yml")));

        // Commands
        getCommand("channel-admin").setExecutor(new AdminCommand());
        getCommand("channel-chat").setExecutor(new ChatCommand());
        getCommand("channel-join").setExecutor(new JoinCommand());
        getCommand("channel-leave").setExecutor(new LeaveCommand());
        getCommand("channel-create").setExecutor(new CreateCommand());
        getCommand("channel-delete").setExecutor(new DeleteCommand());
        getCommand("channel-add").setExecutor(new AddCommand());
        getCommand("channel-set").setExecutor(new SetCommand());

        // AutoJoin Channels!
        for(Player player : getServer().getOnlinePlayers())
            for(Channel chan : ChannelManager.getManager().getAutoChannels())
                chan.addMember(player);

        getServer().getLogger().info(getDescription().getName()+" v"+getDescription().getVersion()+" enabled");
    }

    public static File getModuleFolder() {
        return module_folder;
    }

    public void save() {
        config.save();
        cConfig.save();
    }
    public void reload() {
        config.reload();
        cConfig.reload(true);
    }

    public static Chat plugin() {
        return plugin;
    }

    public static String error(String msg) {
        return format(ChatColor.RED,msg);
    }
    public static String info(String msg) {
        return format(ChatColor.YELLOW, msg);
    }

    public static String format(ChatColor color, String msg) {
        return "["+plugin.getDescription().getName()+"] "+color+msg;
    }

    static {
        org.bukkit.configuration.serialization.ConfigurationSerialization.registerClass(Channel.class);
        //Channel.class.getDeclaredClasses(); // !!! Register subclasses
    }
}

