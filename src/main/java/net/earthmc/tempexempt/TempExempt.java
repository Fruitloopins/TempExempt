package net.earthmc.tempexempt;

import net.earthmc.tempexempt.command.TempExemptCommand;
import net.earthmc.tempexempt.manager.ConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TempExempt extends JavaPlugin {
    public static TempExempt INSTANCE;

    @Override
    public void onEnable() {
        INSTANCE = this;

        ConfigManager configManager = new ConfigManager();
        configManager.init(getConfig());
        saveConfig();

        getCommand("tempexempt").setExecutor(new TempExemptCommand());
    }
}
