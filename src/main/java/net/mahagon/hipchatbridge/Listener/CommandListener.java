package net.mahagon.hipchatbridge.Listener;

import net.mahagon.hipchatbridge.HipChatBridge;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommandListener implements Listener {

    private HipChatBridge plugin;
    private FileConfiguration config;

    public CommandListener(HipChatBridge plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event){
        for (String command:config.getKeys(false)) {
            if (!command.startsWith("/")) {
                continue;
            }
            if (event.getMessage().toLowerCase().startsWith(command.toLowerCase())){
                if (config.getBoolean(command.concat(".active"), false) && (config.getInt(command.concat(".min_arguments"), -1) == -1 || event.getMessage().substring(command.length()).split(" ").length > config.getInt(command.concat(".min_arguments"), -1))){
                    Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONObject obj = new JSONObject();
                                obj.put("color", config.getString(command.concat(".color")));
                                obj.put("message", ("[" + config.getString(command.concat(".prefix")) + "]" + event.getPlayer().getName() + ": " + event.getMessage().substring(command.length())).replaceAll("§.", ""));
                                obj.put("notify", config.getBoolean(command.concat(".notify"), false));
                                obj.put("message_format", "text");
                                String msg = obj.toString();
                                String link = config.getString(command.concat(".url")).concat("?auth_token=").concat(config.getString(command.concat(".auth_token")));
                                URL url = new URL(link);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("POST");
                                connection.setRequestProperty("Content-Length", Integer.toString(msg.getBytes("UTF-8").length));
                                connection.setRequestProperty("Content-Type", "application/json");
                                connection.setRequestProperty("Accept-Charset", "UTF-8");
                                connection.setUseCaches(false);
                                connection.setDoInput(true);
                                connection.setDoOutput(true);

                                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(wr, "UTF-8"));
                                writer.write(msg);
                                writer.flush();
                                writer.close();
                                wr.flush();
                                wr.close();
                                if (connection.getResponseCode() >= 400)
                                    Bukkit.getLogger().warning("[" + plugin.getName().toString() + "] HTTP ERROR: " + connection.getResponseCode() + " " + link);
                                connection.disconnect();
                            } catch (Exception e) {
                                Bukkit.getLogger().warning("[" + plugin.getName().toString() + "] " + e.getMessage());
                            }
                        }
                    });
                }
                break;
            }
        }
    }
}
