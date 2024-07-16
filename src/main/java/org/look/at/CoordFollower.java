package org.look.at;

import net.minecraft.client.Minecraft;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CoordFollower extends Plugin {

    @Override
    public void onLoad() {
        this.getLogger().info("CoordFollower loaded");
        RusherHackAPI.getModuleManager().registerFeature(new CoordFollowerModule());
    }

    @Override
    public void onUnload() {
        this.getLogger().info("CoordFollower unloaded");
    }
}