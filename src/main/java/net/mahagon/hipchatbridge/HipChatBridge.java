package net.mahagon.hipchatbridge;

import net.mahagon.hipchatbridge.Listener.CommandListener;
import net.mahagon.hipchatbridge.Listener.HeroChatListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class HipChatBridge extends JavaPlugin {

    private FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        if (!new File(this.getDataFolder() + File.separator + "config.yml").exists()) {
            if (config.getConfigurationSection("room 1") == null) {
                config.addDefault("room 1.active", false);
                config.addDefault("room 1.auth_token", "INSERTYOURTOKENHERE");
                config.addDefault("room 1.url", "https://hipchat.mahagon.net/v2/room/7/notification");
                config.addDefault("room 1.color", "grey");
                config.addDefault("room 1.notify", false);
                config.addDefault("room 1.send", true);
                config.addDefault("room 1.receive", true);
            }
            if (config.getConfigurationSection("/pe new") == null) {
                config.addDefault("/pe new.active", false);
                config.addDefault("/pe new.auth_token", "INSERTYOURTOKENHERE");
                config.addDefault("/pe new.url", "https://hipchat.mahagon.net/v2/room/7/notification");
                config.addDefault("/pe new.color", "yellow");
                config.addDefault("/pe new.min_arguments", 1);
                config.addDefault("/pe new.prefix", "New ticket");
                config.addDefault("/pe new.notify", true);
            }
        }

        config.options().copyDefaults(true);
        saveConfig();

        HeroChatListener heroChatListener = new HeroChatListener(this, config);
        Bukkit.getServer().getPluginManager().registerEvents(heroChatListener, this);
        CommandListener commandListener = new CommandListener(this, config);
        Bukkit.getServer().getPluginManager().registerEvents(commandListener, this);

        //TODO: Das Empfangen von Hipchatmsgs
        //TODO: Basic Commands einbauen wie /list usw.
    }

}
