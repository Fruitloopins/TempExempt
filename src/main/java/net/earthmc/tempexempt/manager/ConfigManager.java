package net.earthmc.tempexempt.manager;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class ConfigManager {

    public void init(FileConfiguration config) {
        config.options().setHeader(List.of("TempExempt"));

        config.addDefault("exempt_time", 5); config.setInlineComments("exempt_time", List.of("Exemption time in minutes"));

        config.options().copyDefaults(true);
    }
}
