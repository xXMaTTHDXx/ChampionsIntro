package io.matthd.championsintro;

import io.matthd.championsintro.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Matthew on 2016-03-02.
 */
public class ChampionsIntro extends JavaPlugin {

    private static ChampionsIntro instance;
    private IntroManager manager;

    @Override
    public void onEnable(){
        instance = this;
        manager = new IntroManager();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerListener(), this);
    }

    @Override
    public void onDisable(){
        instance = null;
    }

    public static ChampionsIntro getInstance() {
        return instance;
    }

    public IntroManager getManager(){
        return manager;
    }
}
