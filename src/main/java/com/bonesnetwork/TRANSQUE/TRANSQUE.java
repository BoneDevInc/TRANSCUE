package com.bonesnetwork.TRANSQUE;

import com.bonesnetwork.TRANSQUE.cmd.CmdExecutor;
import com.bonesnetwork.TRANSQUE.listeners.PlayerJoinQuitEventListener;
import com.bonesnetwork.TRANSQUE.listeners.onPluginMessageReceivedListener;
import com.bonesnetwork.TRANSQUE.util.NoChat;
import com.bonesnetwork.TRANSQUE.util.SQLDB;
import com.onarandombox.MultiverseCore.MultiverseCore;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import dev.dejvokep.boostedyaml.spigot.SpigotSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class TRANSQUE extends JavaPlugin {
    private static TRANSQUE plugin;

    private static Logger logger;
    private static YamlDocument config;
    private static MultiverseCore mvCore;

    @Override
    public void onEnable() {
        plugin = this;
        mvCore = (MultiverseCore) Bukkit.getServer().getPluginManager().getPlugin("Multiverse-Core");
        logger = Bukkit.getLogger();
        initConfig();
        SQLDB.openConnection();
        registerEvents();
        registerCommands();
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
        SQLDB.closeConnection();
    }

    public static TRANSQUE getPlugin() {return plugin;}

    public static Logger getPublicLogger() {return logger;}
    public static YamlDocument getYamlConfig() {return config;}
    public static MultiverseCore getMvCore() {return mvCore;}

    public void initConfig() {
        try {
            config = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"), GeneralSettings.builder().setSerializer(SpigotSerializer.getInstance()).build(), LoaderSettings.DEFAULT, DumperSettings.DEFAULT, UpdaterSettings.DEFAULT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void registerEvents() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new onPluginMessageReceivedListener());

        Bukkit.getServer().getPluginManager().registerEvents(new NoChat(),this);
        Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinQuitEventListener(),this);
    }

    public void registerCommands() {
        getCommand("TQ").setExecutor(new CmdExecutor());
    }
}

