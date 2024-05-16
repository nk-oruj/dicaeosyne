package org.nemo.dicaeosyne.patches;

import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.nemo.dicaeosyne.Dicaeosyne;

import java.util.HashMap;
import java.util.UUID;

public class IronGolemWaterPatch implements Listener {
    private final HashMap<UUID, BukkitRunnable> golemMap;

    public IronGolemWaterPatch() {
        Dicaeosyne.Plugin.getServer().getPluginManager().registerEvents(this, Dicaeosyne.Plugin);
        golemMap = new HashMap<>();
    }

    private void unloadGolem(UUID golemID) {
        golemMap.get(golemID).cancel();
        golemMap.remove(golemID);
    }

    public void resolveGolem(Entity entity) {
        Damageable golem = (Damageable) entity;
        UUID golemID = golem.getUniqueId();

        if (golemMap.containsKey(golemID)) {
            if (golem.isInWater()) return;
            unloadGolem(golemID);
        }

        if (!golem.isInWater()) return;

        golemMap.put(golemID, new BukkitRunnable(){
            @Override
            public void run() {
                golem.damage(5);
            }
        });

        golemMap.get(golemID).runTaskTimer(Dicaeosyne.Plugin, 0, 20);
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event){
        for (Entity e: event.getChunk().getEntities()) {
            if (!golemMap.containsKey(e.getUniqueId())) continue;
            unloadGolem(e.getUniqueId());
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (!golemMap.containsKey(event.getEntity().getUniqueId())) return;
        event.getDrops().clear();
        unloadGolem(event.getEntity().getUniqueId());
    }
}
