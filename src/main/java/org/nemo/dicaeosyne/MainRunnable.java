package org.nemo.dicaeosyne;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

import org.nemo.dicaeosyne.commands.CommandLogin;
import org.nemo.dicaeosyne.patches.IronGolemWaterPatch;
import org.nemo.dicaeosyne.patches.PlayerAccountPatch;

public class MainRunnable extends BukkitRunnable implements Listener {

    public final PlayerAccountPatch playerAccountPatch;
    public final IronGolemWaterPatch ironGolemWaterPatch;

    public MainRunnable() {
        playerAccountPatch = new PlayerAccountPatch();
        ironGolemWaterPatch = new IronGolemWaterPatch();
    }

    @Override
    public void run() {
        for (World world : Dicaeosyne.Plugin.getServer().getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getType() != EntityType.IRON_GOLEM) continue;
                ironGolemWaterPatch.resolveGolem(entity);
            }
        }
    }
}
