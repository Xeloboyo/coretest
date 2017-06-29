/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coretest;

import static core.Core.*;
import static coretest.testmain.bullets;
import static coretest.testmain.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author willi
 */

enum bulletList{
    BASIC(new Bullet(13, 0, 0, 0, 4.5f, false, 0, 0,false)),
    BIGBASIC(new Bullet(30, 0, 0, 0, 6.5f, false, 0, 0,false)),
    BIGHOMING(new Bullet(30, 0, 0, 0, 6.5f, false, 0, 8,false)),
    FASTBOUNCING(new Bullet(20, 0, 0, 0, 8.5f, false, 0, 0,true)),;
    
    
    Bullet b;

    private bulletList(Bullet b) {
        this.b = b;
    }
    
}


public abstract class Enemy {
    static Enemy test = new Enemy(new Block[][]{{},{},{}}, wx, wy, wx, wy, wx, speed) {
        @Override
        public void updateMove(float speed) {
            tick+=1.5f;
            x+=(mx-x)/6f;
            y+=(tick-y)/6f;
        }
    };
    
    Block[][] blocks;
    ArrayList <Gun> guns = new ArrayList<>();
    float x,y;
    float desx,desy;
    float vx=0,vy=0;
    
    float tick=0;
    float hp;
    
    float lifespan;

    public Enemy(Block[][] blocks, float x, float y, float desx, float desy, float hp, float lifespan) {
        this.blocks = blocks;
        this.x = x;
        this.y = y;
        this.desx = desx;
        this.desy = desy;
        this.hp = hp;
        this.lifespan = lifespan;
    }

    
    
    
    public void update(float speed){
        x+=vx*speed;
        y+=vy*speed;
        updateMove(speed);
        updateGuns(speed);
    }
    
    public abstract void updateMove(float speed);   
    public void updateGuns(float speed){
        
    }
    
    
  
}
class Pattern{
    Shot[] shots;
    float highestdamage=0;
    float avreload=99;
    float maxtime;
    
    int current = 0;
    float time;
    
    Gun g;
    public Pattern(Shot[] shots, float maxtime,Gun g) {
        Arrays.sort(shots);
        this.shots = shots;
        this.maxtime = maxtime;
        this.g=g;
        if(shots[shots.length-1].time>maxtime){
            this.maxtime=shots[shots.length-1].time;
            
        }
        for(int i = shots.length-1;i>=0;i--){
            if(highestdamage<shots[i].b.dmg){
                highestdamage=shots[i].b.dmg;
            }
        }
    }
    
    public Shot getlastShot(float time){
        float time2 = time%maxtime;
        for(int i = shots.length-1;i>=0;i--){
            if(shots[i].time-time2<0){
                return shots[i];
            }
        }
        return null;
    }
    public void update(float timedif){
        time += timedif;
        if(time>bpm/8f){
            time-=bpm/8f;
            current++;
            if(current>maxtime-1){
                current=0;
            }
        }
        
        
    }
}
class Shot implements Comparable<Shot>{
   float rotoffset;
   float time;
   float spread;
   float fspeed;
   Bullet b;
   boolean follow;
    public Shot(float rot, float time, float spread, float fspeed, Bullet b,boolean follow) {
        this.rotoffset = rot;
        this.time = time;
        this.spread = spread;
        this.fspeed = fspeed;
        this.b = b;
        this.follow = follow;
    }

    @Override
    public int compareTo(Shot o) {
        return (int)((this.time - o.time)*64);
    }
   
   
}
class Block{
    float x,y,w;
    float hp;

    public Block(float x, float y, float w, float hp) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.hp = hp;
    }
    
    public void update(float time){
        
    }
    
    public void draw(float offx,float offy){
        pushMatrix();
        translate(offx, offy);
        fill(fg);
        rect(x, y, w, w);
        
        popMatrix();
    }
    
    
}
class Gun extends Block{
    
    float rot;
    float barrelWidth;
    
    float firedelay;
    float spread;
    float fspeed;
    

    Pattern shotpattern;
    float lastfire=100;

    public Gun(float x, float y, float w, float hp, float rot, float firedelay, float spread, float fspeed, Pattern shotpattern) {
        super(x, y, w, hp);
        
        this.rot = rot;
        this.firedelay = firedelay;
        this.spread = spread;
        this.fspeed = fspeed;

        this.shotpattern = shotpattern;
        
    }

    
    
    
    
    public void fire(float offx,float offy, Bullet b){
        bullets.add(new Bullet(x, y, rot, fspeed, b));
        lastfire=0;
    }
    float lasttime = 0;
    public void update(float time){
        lastfire += time-lasttime;
                
        lasttime = time; 
        shotpattern.getlastShot(time);
    }
    
    public void draw(float offx,float offy){
        pushMatrix();
        translate(offx, offy);
        fill(fg);
        rect(x, y, w, w);
        fill(abg);
        translate(w/2f, w/2f);
        rotate(radians(rot));
        rect(-w/2f, -w/2f, w, w);
        popMatrix();
    }
    
    
    
    
}