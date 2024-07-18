package org.look.at;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.plugin.Plugin;

public class CoordFollower extends Plugin {

    @Override
    public void onLoad()
    {
        this.getLogger().info("CoordFollower loaded");
        RusherHackAPI.getModuleManager().registerFeature(new CoordFollowerModule());
    }

    @Override
    public void onUnload() {this.getLogger().info("CoordFollower unloaded");}
}