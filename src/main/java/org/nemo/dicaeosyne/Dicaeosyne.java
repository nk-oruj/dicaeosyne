package org.nemo.dicaeosyne;

import org.bukkit.plugin.java.JavaPlugin;

public final class Dicaeosyne extends JavaPlugin {
    public static JavaPlugin Plugin;
    private static MainRunnable Main;

    @Override
    public void onEnable() {
        Dicaeosyne.Plugin = this;

        Dicaeosyne.Main = new MainRunnable();
        Dicaeosyne.Main.runTaskTimer(this, 0, 1);
    }

    @Override
    public void onDisable() {
        Dicaeosyne.Main.cancel();
    }
}
