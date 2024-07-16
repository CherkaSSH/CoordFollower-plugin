package org.look.at;

import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.feature.module.Module;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.core.event.subscribe.Subscribe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CoordFollowerModule extends Module {
    public CoordFollowerModule() {
        super("CoordFollower", ModuleCategory.MISC);
        coorddir = getCoordsDirectory();
    }
    Path coorddir;
    @Subscribe

    public static Path getCoordsDirectory() {
        Path path = RusherHackAPI.getConfigPath().getParent().resolve("coords");
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create coords directory", e);
            }
        }
        return path;
    }
}