package ubiquitaku.enchantbottlecompression;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class EnchantBottleCompression extends JavaPlugin {
    FileConfiguration config;
    String prefix;
    boolean ebc;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        config = getConfig();
        prefix = config.getString("prefix","§l[§a§lEBC§r§l]§r");
        ebc = config.getBoolean("EBC",false);
        saveConfig();
        config = getConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("ebc")) {
            if (!sender.hasPermission("ebc.op")) {
                sender.sendMessage("§c§lあなたはこのコマンドを実行する権限を持っていません");
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage(prefix+"/ebc <on or off> : EBCの使用の許可を出すもしくは停止します");
                return true;
            }
            if (args[0].equals("on")) {
                if (ebc) {
                    sender.sendMessage(prefix+"既にonになっています");
                    return true;
                }
                ebc = true;
                config.set("EBC",true);
                sender.sendMessage(prefix+"onにしました");
                return true;
            }
            if (args[0].equals("off")) {
                if (!ebc) {
                    sender.sendMessage(prefix+"既にoffになっています");
                    return true;
                }
                ebc = false;
                config.set("EBC",false);
                sender.sendMessage(prefix+"offにしました");
                return true;
            }
        }
        return true;
    }

    @EventHandler
    public void onCompression(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR) {
            return;
        }
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.EXPERIENCE_BOTTLE) {
            return;
        }
        if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                return;
            }
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§a§lEnchantmentBottle")) {
                //１個消して64個のepボトル渡す
                return;
            }
        }
        if (e.getPlayer().getInventory().getItemInMainHand().getAmount() == 64) {
            //64個消して１個の圧縮ボトルを渡す
        }

    }
}
