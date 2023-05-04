package com.bonesnetwork.TRANSQUE.listeners;

import com.bonesnetwork.TRANSQUE.TRANSQUE;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Logger;

public class onPluginMessageReceivedListener implements PluginMessageListener {
    private Logger logger;
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        logger = TRANSQUE.getPlugin().getLogger();
        logger.info(channel);
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        logger.info(subChannel);
        if (subChannel.equals("MyChannel")) {
            short len = in.readShort();
            byte[] msgbytes = new byte[len];
            in.readFully(msgbytes);

            DataInputStream msgin = new DataInputStream(new ByteArrayInputStream(msgbytes));
            try {
                String playerName = msgin.readUTF();
                logger.info(playerName);
                String worldName = msgin.readUTF();
                logger.info(worldName);
                double x = msgin.readDouble();
                double y = msgin.readDouble();
                double z = msgin.readDouble();
                logger.info(x + ", " + y + ", " + z);
                // Use the Multiverse API to teleport the player to the specified world and location
                Player player1 = Bukkit.getPlayer(playerName);

                player1.teleport(new Location(Bukkit.getWorld("world"),x,y,z));
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

    }
}
