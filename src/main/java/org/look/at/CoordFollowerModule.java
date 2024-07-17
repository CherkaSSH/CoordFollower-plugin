package org.look.at;

import net.minecraft.world.entity.player.Player;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.events.player.EventPlayerUpdate;
import org.rusherhack.client.api.feature.module.Module;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.core.command.annotations.CommandExecutor;
import org.rusherhack.core.event.subscribe.Subscribe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoordFollowerModule extends Module {
    public CoordFollowerModule() {
        super("CoordFollower", ModuleCategory.MISC);
    }

    private int index = 0;
    List<Coordinates> coordinates= new ArrayList<>();

    @Subscribe
    public void onUpdate(EventPlayerUpdate event){}


    public Player player() {
        Player player = mc.player;
        if (player != null) {
            return player;
        } else {
            throw new IllegalStateException("Player is null");
        }
    }

    //commands
    @CommandExecutor(subCommand = "add")
    @CommandExecutor.Argument({"x","z"})
    public void add(Optional<Integer> x, Optional<Integer> z){
        if(x.isPresent() && z.isPresent()){
            coordinates.add(new Coordinates(x.get(),z.get()));
        }else{
            coordinates.add(new Coordinates(player().blockPosition()));
        }
    }

    @CommandExecutor(subCommand = "del")
    @CommandExecutor.Argument("№")
    public void del(Optional<Integer> n){
        if(n.isPresent()){
            coordinates.remove(n.get());
        }else{
            coordinates.remove(coordinates.size()-1);
        }
    }

    @CommandExecutor(subCommand = "list")
    public void list(){
        for(int i=0;i<=coordinates.size()-1;i++){
            getLogger().info("№"+i+1+" X: "+coordinates.get(i).getX()+", Z:"+coordinates.get(i).getZ());
        }
    }
}