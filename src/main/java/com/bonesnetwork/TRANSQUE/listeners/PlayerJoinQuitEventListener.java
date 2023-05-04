package com.bonesnetwork.TRANSQUE.listeners;

import com.bonesnetwork.TRANSQUE.functions.TRANSQUESystemsManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitEventListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        TRANSQUESystemsManager.tqPlayerJoin(event);}
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        TRANSQUESystemsManager.tqPlayerLeave(event);}
}
