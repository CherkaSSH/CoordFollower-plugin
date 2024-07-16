package org.look.at;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class Coordinates {
    public Coordinates(int x,int z){
        this.x=x;
        this.z=z;
    }
    public Coordinates(int x,int z,dim dim){
        this.x=x;
        this.z=z;
        this.curdim=dim;
    }
    public Coordinates(BlockPos block){
        this.x=block.getX();
        this.z=block.getZ();
    }
    public Coordinates(BlockPos block,dim dim){
        this.x=block.getX();
        this.z=block.getZ();
        this.curdim=dim;
    }
    //getters
    public int getX() {return x;}
    public int getZ() {return z;}
    public dim getCurdim() {return curdim;}
    public void rotate(){
        double deltaX = this.x - player.getX();
        double deltaZ = this.z - player.getZ();
        double yaw = Math.atan2(deltaZ, deltaX);
        yaw = Math.toDegrees(yaw);
        yaw -= 90;
        player.setYRot((float) yaw);
    }
    private final int z,x;
    private dim curdim;
    private Player player = Minecraft.getInstance().player;
    public enum dim {
        over,nether
    }
}
