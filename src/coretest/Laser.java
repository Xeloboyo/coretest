/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coretest;

/**
 *
 * @author willi
 */
public class Laser{
    float x, y ,rot,vel ,dmg;
    float pretime;
    boolean isfastbullet=false;
    boolean enemy = true;
    Bullet split=null;
    int splitamount;
    int splitanglespread;
    int splitanglestart;
    boolean spliteven;
    
    Laser lsplit=null;
    int lsplitamount;
    int lsplitanglespread;
    int lsplitanglestart;
    boolean lspliteven;

    public Laser(float x, float y, float rot, float vel, float dmg,float pretime) {
        this.x = x;
        this.y = y;
        this.rot = rot;
        this.vel = vel;
        this.dmg = dmg;
        this.pretime = pretime;
    }
    
    
    public void update(float speed){
        if(pretime>0){
            pretime-=speed;
        }
        if(enemy){
        
        }else{
        
        }
    }
    
    
    public void display(){
        
    }
    
    
    
}