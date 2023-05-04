package com.bonesnetwork.TRANSQUE.functions;

import com.bonesnetwork.TRANSQUE.TRANSQUE;
import com.bonesnetwork.TRANSQUE.util.TRANSQUEWorldManager;
import com.bonesnetwork.TRANSQUE.util.SQLDB;
import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class TRANSQUESystemsManager {
    static int playerID;
    static String partyName, playerName, playerDestination;
    static boolean inParty;
    static String templateName;
    static String newWorldName;

    static String UUID;

    public static void tqPlayerJoin(PlayerJoinEvent event) {
        YamlDocument config = TRANSQUE.getYamlConfig();
        Logger logger = TRANSQUE.getPublicLogger();
        Player player = event.getPlayer();
        UUID = player.getUniqueId().toString();
        Connection connection = SQLDB.MySQLDB();
        if(config.contains("Players.Exceptions."+player.getName())){
            return;
        }
        try {
            PreparedStatement findStatement = connection.prepareStatement("SELECT playerID, playerName, playerDestination, inParty, partyName, partyCount FROM `" + config.getString("Database.Table") + "` WHERE UUID = ?");
            findStatement.setString(1, UUID);
            ResultSet result = findStatement.executeQuery();

            if(result.next()) {
                playerID = result.getInt("playerID");
                logger.info(String.valueOf(playerID));
                playerName = result.getString("playerName");
                logger.info(playerName);
                playerDestination = result.getString("playerDestination");
                logger.info(playerDestination);
                inParty = result.getBoolean("inParty");
                logger.info(String.valueOf(inParty));
                partyName = result.getString("partyName");
                logger.info(partyName);
            } else {
                player.sendMessage(ChatColor.DARK_RED + "Hmm It Seems You Are Not In The Database, This Is Caused Via A Fatal Error. (Please Report To Support Staff On The Discord)");
            }
            result.close();
            findStatement.close();

            PreparedStatement removeStatement = connection.prepareStatement("DELETE FROM '"+config.getString("Database.Table")+"' WHERE playerID = ?");
            findStatement.setInt(1, playerID);
            ResultSet removeResult = removeStatement.executeQuery();
            removeResult.close();
            findStatement.close();
            if(result.next()) {
                logger.info("Removed Record For: "+playerName);
            } else {
                logger.info("Couldn't Find Record For: "+playerName+" For playerID: "+playerID+". This Is A BIG Problem");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if(inParty) {
            newWorldName = config.getString("Worlds."+playerDestination+".NewWorldName").replace("{partyName}", partyName);
        }else {
            newWorldName = config.getString("Worlds."+playerDestination+".NewWorldName").replace("{partyName}", playerName);
        }

        logger.info("New World Name: "+ newWorldName);
        if(!TRANSQUEWorldManager.getIfWorldExists(newWorldName)) {
            templateName = config.getString("Worlds."+playerDestination+".Template");
            logger.info("Template Name: "+templateName);
            TRANSQUEWorldManager.createWorldWithTemplate(templateName, newWorldName, false);
        }

        TRANSQUEWorldManager.teleportPlayerToWorld(player, newWorldName);
    }

    public static boolean tqPlayerLeave(PlayerQuitEvent event) {
        YamlDocument config = TRANSQUE.getYamlConfig();
        Player player = event.getPlayer();
        World world = player.getWorld();
        String worldName = world.getName();
        if(config.contains("Worlds.Exceptions."+worldName)) {
            return false;
        }
        if(world.getPlayerCount()==0) {
            TRANSQUEWorldManager.deleteWorld(worldName);
        }
        return true;
    }
}
