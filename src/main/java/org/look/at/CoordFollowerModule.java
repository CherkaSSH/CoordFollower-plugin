package org.look.at;

import net.minecraft.world.entity.player.Player;
import org.look.at.important.Coordinates;
import org.look.at.important.Utils;
import org.rusherhack.client.api.RusherHackAPI;
import org.rusherhack.client.api.events.player.EventPlayerUpdate;
import org.rusherhack.client.api.feature.command.ModuleCommand;
import org.rusherhack.client.api.feature.module.ModuleCategory;
import org.rusherhack.client.api.feature.module.ToggleableModule;
import org.rusherhack.core.command.annotations.CommandExecutor;
import org.rusherhack.core.event.subscribe.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CoordFollowerModule extends ToggleableModule {
    public CoordFollowerModule() {super("CoordFollower", ModuleCategory.MISC);}

    private int index = 0;
    private List<Coordinates> coordinates= new ArrayList<>();
    Utils.mathUtils mathUtils;
    Utils.fileUtils fileUtils;
    ToggleableModule autowalk = (ToggleableModule) RusherHackAPI.getModuleManager().getFeature("AutoWalk").get();

    @Subscribe
    void onUpdate(EventPlayerUpdate event){
        if (mathUtils.getXZDistanceToBlock(player(),coordinates.get(index))>=10)
        {
            player().setXRot(mathUtils.rotateXFloat(player(),coordinates.get(index)));
        }
        if (mathUtils.getXZDistanceToBlock(player(),coordinates.get(index))<10&&coordinates.size()>index)
        {
            index++;
        }
        else
        {
            this.setToggled(false);
        }
    }
    @Override
    public void onEnable(){
        if(!coordinates.isEmpty()){
            autowalk.setToggled(true);
        }
    }

    @Override
    public void onDisable(){
        if(!coordinates.isEmpty()){
            autowalk.setToggled(true);
        }
    }

    public Player player() {
        Player player = mc.player;
        if (player != null) {
            return player;
        } else {
            throw new IllegalStateException("Player is null");
        }
    }


    @Override
    public ModuleCommand createCommand(){
        return new ModuleCommand(this){
            //commands
            @CommandExecutor(subCommand = "add")
            @CommandExecutor.Argument({"x","z"})
            private void add(Optional<Integer> x, Optional<Integer> z){
                if(x.isPresent() && z.isPresent()){
                    coordinates.add(new Coordinates(x.get(),z.get()));
                }else{
                    coordinates.add(new Coordinates(player().blockPosition()));
                }
            }

            @CommandExecutor(subCommand = "del")
            @CommandExecutor.Argument("№")
            private void del(Optional<Integer> n){
                if(n.isPresent()){
                    coordinates.remove(n.get());
                }else{
                    coordinates.remove(coordinates.size()-1);
                }
            }

            @CommandExecutor(subCommand = "list")
            private void list(){
                for(int i=0;i<=coordinates.size()-1;i++){
                    getLogger().info("№"+i+1+" X: "+coordinates.get(i).getX()+", Z:"+coordinates.get(i).getZ());
                }
            }

            @CommandExecutor(subCommand = "save")
            @CommandExecutor.Argument({"name","pass"})
            private void save(String name, String pass) throws Exception {
                Utils.fileUtils.save(coordinates,pass,name+".coords");
            }

            @CommandExecutor(subCommand = "load")
            @CommandExecutor.Argument({"name","pass"})
            private void load(String name,String pass) throws Exception {
                coordinates = (List<Coordinates>) fileUtils.load(pass,name+".coords");
            }
        };
    }
}