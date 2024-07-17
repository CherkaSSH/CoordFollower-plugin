package org.look.at;

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
    private final int z,x;
    private dim curdim;
    public enum dim {
        over,nether
    }
}
