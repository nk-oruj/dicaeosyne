package org.nemo.dicaeosyne;

import org.bukkit.plugin.java.JavaPlugin;
import org.nemo.dicaeosyne.commands.CommandHello;
import org.nemo.dicaeosyne.runnables.MainRunnable;

public final class Dicaeosyne extends JavaPlugin {
    private MainRunnable mainRun;

    @Override
    public void onEnable() {
        ////
        this.mainRun = new MainRunnable(this);
        getServer().getPluginManager().registerEvents(this.mainRun, this);
        this.mainRun.runTaskTimer(this, 0, 1);
        ////

        ////
        getCommand("hello").setExecutor(new CommandHello());
        ////
    }

    @Override
    public void onDisable() {
        ////
        this.mainRun.cancel();
        ////
    }
}
