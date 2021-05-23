package ubiquitaku.enchantbottlecompression;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class EnchantBottleCompression extends JavaPlugin implements Listener {
    FileConfiguration config;
    String prefix;
    boolean ebc;
    ItemStack normalBottle = new ItemStack(Material.EXPERIENCE_BOTTLE);
    ItemStack ebcStack = new ItemStack(Material.EXPERIENCE_BOTTLE);
    ItemMeta ebcMeta;


    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginManager().registerEvents(this,this);
        saveDefaultConfig();
        config = getConfig();
        prefix = config.getString("prefix","§l[§a§lEBC§r§l]§r");
        ebc = config.getBoolean("EBC",false);
        saveConfig();
        config = getConfig();
        ebcMeta = ebcStack.getItemMeta();
        ebcMeta.setDisplayName("§a§lEnchantmentBottle");
        ebcMeta.setLore(Arrays.asList("圧縮されたエンチャント便","メインハンドに持っているときは使用できない","しかし不具合防止のためそれ以外では使えるが","ただのエンチャント瓶と効果は変わらない"));
        ebcStack.setItemMeta(ebcMeta);
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
                saveConfig();
                config = getConfig();
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
                saveConfig();
                config = getConfig();
                return true;
            }
        }
        return true;
    }

    @EventHandler
    public void onCompression(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR) {
            if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.EXPERIENCE_BOTTLE) {
                return;
            }
            e.setCancelled(true);
            if (e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
                if (!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
                    return;
                }
                if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§a§lEnchantmentBottle")) {
                    //１個消して64個のepボトル渡す
                    if (!ebc) {
                        e.getPlayer().sendMessage(prefix + "現在停止されているため交換できません");
                        return;
                    }
                    e.getPlayer().getInventory().removeItem(ebcStack);
                    for (int i = 0; i < 64; i++) {
                        e.getPlayer().getInventory().addItem(normalBottle);
                    }
                    e.getPlayer().sendMessage(prefix + "ボトルを解凍しました");
                    return;
                }
            }
            if (e.getPlayer().getInventory().getItemInMainHand().getAmount() == 64) {
                //64個消して１個の圧縮ボトルを渡す
                if (!ebc) {
                    e.getPlayer().sendMessage(prefix + "現在停止されているため交換できません");
                    return;
                }
                for (int i = 0; i < 64; i++) {
                    e.getPlayer().getInventory().removeItem(normalBottle);
                }
                e.getPlayer().getInventory().addItem(ebcStack);
                e.getPlayer().sendMessage(prefix + "ボトルを圧縮しました");
            }
        }
    }

//    @EventHandler
//    public void onUse(PlayerInteractEvent e) {
//        if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
//            return;
//        }
//        if (!e.getPlayer().getInventory().getItemInMainHand().hasItemMeta()) {
//            return;
//        }
//        if (!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasDisplayName()) {
//            return;
//        }
//        if (!e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getDisplayName().equals("§a§lEnchantmentBottle")) {
//            return;
//        }
//        e.getPlayer().sendMessage(prefix+"そのアイテムを使うことはできません");
//        e.setCancelled(true);
//    }
}
