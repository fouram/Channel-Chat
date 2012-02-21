package com.feildmaster.channelchat.event.player;

import com.feildmaster.channelchat.channel.Channel;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;

public class ChannelPlayerChatEvent extends PlayerChatEvent {
    private Channel chan;
    public ChannelPlayerChatEvent(Player player, Channel chan, String msg) {
        super(player, msg);
        this.chan = chan;
    }
    public Channel getChannel() {
        return chan;
    }
}
