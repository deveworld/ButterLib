package kr.bp.butterLib;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}