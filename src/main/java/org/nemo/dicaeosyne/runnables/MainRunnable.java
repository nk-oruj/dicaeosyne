package org.nemo.dicaeosyne.runnables;

import org.bukkit.World;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class MainRunnable extends BukkitRunnable implements Listener {

    private JavaPlugin plugin;
    private Map<UUID, BukkitRunnable> golemMap;

    public MainRunnable(JavaPlugin plugin) {
        this.plugin = plugin;
        this.golemMap = new HashMap<UUID, BukkitRunnable>();
    }

    private void UnloadGolem(UUID golemID) {
        golemMap.get(golemID).cancel();
        golemMap.remove(golemID);
    }

    private void ResolveGolem(Entity entity) {
        Damageable golem = (Damageable) entity;
        UUID golemID = golem.getUniqueId();

        if (golemMap.containsKey(golemID)) {
            if (golem.isInWater()) return;
            UnloadGolem(golemID);
        }

        if (!golem.isInWater()) return;

        golemMap.put(golemID, new BukkitRunnable(){
            @Override
            public void run() {
                golem.damage(5);
            }
        });

        golemMap.get(golemID).runTaskTimer(plugin, 0, 20);
    }

    @Override
    public void run() {
        for (World world : plugin.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() != EntityType.IRON_GOLEM) continue;
                ResolveGolem(entity);
            }
        }
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event){
        for (Entity e: event.getChunk().getEntities()) {
            if (!golemMap.containsKey(e.getUniqueId())) continue;
            UnloadGolem(e.getUniqueId());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!golemMap.containsKey(event.getEntity().getUniqueId())) return;
        event.getDrops().clear();
        UnloadGolem(event.getEntity().getUniqueId());
    }
}
