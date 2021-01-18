package test.test;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Ocelot;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import test.test.database.mySQL;
import test.test.database.mySQLData;

import java.sql.SQLException;
import java.util.Random;

public final class Test extends JavaPlugin implements Listener {
    public mySQL SQL;
    public mySQLData data;
    private String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private String name = "";
    private Creature ocelot;
    private ItemStack leather = new ItemStack(Material.LEATHER);

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.SQL = new mySQL();
        this.data = new mySQLData(this);

        try {
            SQL.connect();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Database not connected");
        }
        if (SQL.isConnected()){
            Bukkit.getLogger().info("Database connected");
        }
    }

    @Override
    public void onDisable() {
        SQL.disconnect();
    }

    @EventHandler
    public void entityDeathHandler (EntityDeathEvent event){
        if(event.getEntity().getType() == EntityType.ZOMBIE){
            ocelot = event.getEntity().getWorld().spawn(event.getEntity().getLocation(), Ocelot.class);
            for (int x = 0; x < 5; x++){
                name += symbols.charAt(new Random().nextInt(symbols.length()));
            }
            ocelot.setCustomName(name);
            ocelot.setTarget(event.getEntity().getKiller());
            name = "";
        }
        else if (event.getEntity() == ocelot){
            data.createData(event.getEntity().getKiller().getName(), ocelot.getName(), java.util.Calendar.getInstance().getTime());
            ItemMeta meta = leather.getItemMeta();
            meta.setDisplayName(event.getEntity().getKiller().getName());
            leather.setItemMeta(meta);
            ocelot.getWorld().dropItem(ocelot.getLocation(), leather);
        }
    }

    @EventHandler
    public void pickupItemHandler (PlayerPickupItemEvent event){
        for (Player player : Bukkit.getOnlinePlayers()){
            if (event.getItem().getItemStack().getItemMeta().getDisplayName().equals(player.getName())){
                ItemMeta meta = event.getItem().getItemStack().getItemMeta();
                meta.setDisplayName(event.getPlayer().getName());
                event.getItem().getItemStack().setItemMeta(meta);
            }
        }
    }
}