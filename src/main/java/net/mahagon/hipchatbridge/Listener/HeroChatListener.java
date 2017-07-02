package net.mahagon.hipchatbridge.Listener;

import com.dthielke.herochat.ChannelChatEvent;
import net.mahagon.hipchatbridge.HipChatBridge;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.json.simple.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HeroChatListener implements Listener {

    private HipChatBridge plugin;
    private FileConfiguration config;

    public HeroChatListener(HipChatBridge plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;
    }

    @EventHandler
    public void onChannelChatEvent(ChannelChatEvent event) {
        if (!event.getSender().getActiveChannel().isMuted()) {
            if (config.getBoolean(event.getChannel().getName().concat(".active"), false) && config.getBoolean(event.getChannel().getName().concat(".send"), false)) {
                Bukkit.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject obj = new JSONObject();
                            obj.put("color", config.getString(event.getChannel().getName().concat(".color")));
                            obj.put("message", ("[" + event.getChannel().getName() + "]" + event.getSender().getName() + ": " + event.getMessage()).replaceAll("§.", ""));
                            obj.put("notify", config.getBoolean(event.getChannel().getName().concat(".notify"), false));
                            obj.put("message_format", "text");
                            String msg = obj.toString();
                            String link = config.getString(event.getChannel().getName().concat(".url")).concat("?auth_token=").concat(config.getString(event.getChannel().getName().concat(".auth_token")));
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
        }

    }
}
