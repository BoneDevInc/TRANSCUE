package com.bonesnetwork.TRANSQUE.util;

import com.bonesnetwork.TRANSQUE.TRANSQUE;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;


public class TRANSQUEWorldManager {
    public static boolean createWorldWithTemplate(String oldWorldName, String newWorldName, boolean unloadWorld) {
        MVWorldManager worldManager = TRANSQUE.getMvCore().getMVWorldManager();
        if(getIfWorldExists(oldWorldName)) {
            worldManager.loadWorld(oldWorldName);
        }
        worldManager.cloneWorld(oldWorldName,newWorldName);
        if(unloadWorld) {
            worldManager.unloadWorld(oldWorldName);
        }

        return true;
    }

    public static boolean getIfWorldExists(String worldName) {
        MVWorldManager worldManager = TRANSQUE.getMvCore().getMVWorldManager();
        if(worldManager.getMVWorld(worldName)==null){
            return false;
        }
        return true;
    }

    public static boolean teleportPlayerToWorld(Player player, String worldName) {
        MVWorldManager worldManager = TRANSQUE.getMvCore().getMVWorldManager();
        Location location = worldManager.getMVWorld(worldName).getSpawnLocation();
        player.teleport(location);
        return true;
    }

    public static boolean deleteWorld(String worldName) {
        MVWorldManager worldManager = TRANSQUE.getMvCore().getMVWorldManager();
        return worldManager.deleteWorld(worldName);
    }

    public static World getCBWorld(String worldName) {
        MVWorldManager worldManager = TRANSQUE.getMvCore().getMVWorldManager();
        return worldManager.getMVWorld(worldName).getCBWorld();
    }
}
