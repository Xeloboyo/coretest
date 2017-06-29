/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coretest;
import core.*;
import static core.Core.*;
import static core.Window.height;
import static core.Window.width;
import static coretest.testmain.*;
import java.util.ArrayList;
/**
 *
 * @author willi
 */
public class Bullet{
    float dmg;
    float x,y,vx=0,vy=0;
    float rot;float vel;
    boolean enemy=true;
    //amount in degrees per 10 ticks
    float follow=0;
    float repel = 0;
    //splitting
    float amount=0;
    float delay = 100000;
    boolean destroy=false;
    boolean shootable=false;
    float hp=20f;

    float size=1,sizemult=0;
    
    
    Bullet split=null;
    int splitamount;
    int splitanglespread;
    int splitanglestart;
    boolean spliteven;
    
    int bounce=0;

    public Bullet(float dmg, float x, float y, float rot, float vel, boolean shootable, float hp,float follow,int bounce) {
        this.dmg = dmg;
        this.x = x;
        this.y = y;
        this.rot = rot;
        this.vel = vel;
        size=sqrt(dmg)*2;
        sizemult = size/vel;
        setShootable(shootable);
        this.hp = hp;
        this.follow = follow;
        vx = sin(radians(rot))*vel;
        vy = -cos(radians(rot))*vel;
        this.bounce=bounce;
    }
    
    public Bullet(float dmg, float x, float y, float rot, float vel, boolean shootable, float hp,float follow,int bounce, 
            Bullet Split, int splitamount, int splitanglespr, int splitanglestart,boolean spliteven) {
        this.dmg = dmg;
        this.x = x;
        this.y = y;
        this.rot = rot;
        this.vel = vel;
        size=sqrt(dmg)*2;
        sizemult = size/vel;
        setShootable(shootable);
        this.hp = hp;
        this.follow = follow;
        vx = sin(radians(rot))*vel;
        vy = -cos(radians(rot))*vel;
        this.bounce=bounce;
        this.split=Split;
        this.splitamount=splitamount;
        this.splitanglespread =splitanglespr;
        this.splitanglestart = splitanglestart;
        this.spliteven = spliteven;
    }
  
    Bullet(float x,float y,float rot,float vel,float dmg){
        this.x=x;
        this.y=y;
        this.rot=rot;
        this.vel=vel;
        this.dmg=dmg;
        vx = sin(radians(rot))*vel;
        vy = -cos(radians(rot))*vel;

        size=sqrt(dmg)*2;
        sizemult = size/vel;
    }

    Bullet(float x,float y,float rot,float vel,Bullet b){
        this.x=x;
        this.y=y;
        this.rot=rot;
        this.vel=vel;
        this.dmg=b.dmg;
        
        
        this.follow=b.follow;
        vx = sin(radians(rot))*vel;
        vy = -cos(radians(rot))*vel;

        size=sqrt(dmg)*2;
        sizemult = size/vel;
        this.setShootable(b.shootable);
        this.bounce=b.bounce;
        this.split=b.split;
        this.splitamount=b.splitamount;
        this.splitanglespread =b.splitanglespread;
        this.splitanglestart = b.splitanglestart;
        this.spliteven = b.spliteven;
    }
    public void setShootable(boolean shootable) {
        if(enemy){
          c =(fg);
        }else{
            c=abg;
        }
        this.shootable = shootable;
        size=sqrt(dmg)*5;
        sizemult = size/vel;
        fx.add(new Trail(x, y, (int)(5+(dmg/10)),sizemult/3f, c, this));
    }
    
    void setDir(float rot,float vel){
        this.rot=rot;
        this.vel=vel;
        vx = sin(radians(rot))*vel;
        vy = -cos(radians(rot))*vel;
    }
    void setDir(float velx,float vely,float totalvel){
        float t = dist(0, 0, velx, vely);
        vx = (velx/t)*totalvel;
        vy = (vely/t)*totalvel;
        rot = 180-degrees(atan2(vx,vy));
    }
    void ondeath(){
        fx.add(new Explosion(x,y,dmg*2+4,enemy?fg:abg));
        fx.add(new Debris(x,y,random(-dmg/3f,dmg/3f),random(-dmg/3f,dmg/3f),2,enemy?fg:abg));
        
        if(split!=null){
            float t = splitanglestart-splitanglespread/2f;
            for(int i = 0;i<splitamount;i++){
                if(!spliteven){
                    t= random(splitanglestart-splitanglespread/2f, splitanglestart+splitanglespread/2f);
                }
                bullets.add(new Bullet(x, y, t, split.vel, split));
                t+=splitanglespread/(float)(splitamount-1);
            }
        }
    }
    float lived = 0;
    void updateRepel(float speed){
        if(repel!=0){
            Bullet b;
            for(int i = 0;i<bullets.size();i++){
                b = bullets.get(i);
                if(enemy!=b.enemy&&b.isIn(x, y, repel*30)){
                    float d = dist(x, y, b.x, b.y);
                    bullets.get(i).setDir(b.vx+(b.x-x)/d, b.vy+(b.y-y)/d, bullets.get(i).vel);
                }
            }
        }
    }
    void update(float speed){
        lived++;
        x+=vx*speed;
        y+=vy*speed;
        if(y>my-wy+wh||y<my-wy){
          if(bounce>0){
              bounce--;
              vy=-vy;
              setDir(vx,vy,vel);
              y = clamp(y, my-wy,my-wy+wh);
          }else{ 
            destroy=true;
          }
        }

        if(x>mx-wx+ww||x<mx-wx){
          if(bounce>0){
              bounce--;
              vx=-vx;
              setDir(vx,vy,vel);
              x = clamp(x, mx-wx,mx-wx+ww);
          }else{ 
            destroy=true;
          }  
          
        }
        if(follow!=0){
          float anglediff=0;
          float angle=0;
          if(enemy){
              angle = getangle(p1.x,p1.y,x,y);
              anglediff= angle-rot;
          }else{
            Bullet b;
            Bullet b4=null;
            float maxdis=999999999;
            for(int i = 0;i<bullets.size();i++){
                b = bullets.get(i);
            
                if(b.enemy&&b.shootable&&b.isIn(x, y, follow*20)){
                    float  t= maxdis;
                    maxdis=min(distsqrd(x, y, b.x, b.y),maxdis);
                    if(t!=maxdis){
                        b4 = b;
                    }
                }
            }
            if(b4!=null){
                angle = getangle(b4.x,b4.y,x,y);
                anglediff= angle-rot;
            }
          }
          float a = abs(anglediff);
          
          if(rot<90&&angle>270){
              angle-=360f;
              anglediff= angle-rot;
          }
          if(rot>270&&angle<90){
              
              angle+=360f;
              anglediff= angle-rot;
          }
          
          anglediff = anglediff>0?(clamp(follow/10f,0,a*speed)):(clamp(-follow/10f,-a*speed,0));
          //println(anglediff);
          setDir(rot+anglediff,vel);

        }
        
        if(enemy){
          c =(fg);
          if(shootable){
              for(int i = 0;i<bullets.size();i++){
                if(!bullets.get(i).enemy&&bullets.get(i).isIn(this)){
                    bullets.get(i).destroy=true;
                    hp-=bullets.get(i).dmg;
                    if(hp<0){
                        destroy=true;
                    }
                    return;
                }
              }
          }
          if(p1.touches(x, y, dmg/4f +1)){
              destroy=true;
              p1.damage();
              shakeam += 10;
          }



        }else{
          c =(abg);
        }
    }
    Color c;
    void display(){
        float t = dmg/4f +1;

            strokeWeight(t);
            noFill();

            stroke(c);

        line(x,y,x-vx*sizemult,y-vy*sizemult);
        if(t>5){
            noStroke();

            fill(c);

            ellipse(x, y, t, t);
            
            
        }
        if(repel!=0){
            repelani/=(1.3f);
            if(repelani<0.01){
                repelani=1;
            }
            float r = t+t*(1-repelani)*(abs(repel)+1);
            stroke(c);
            strokeWeight(2*repelani+1);
            arc(x, y, r, r, rot-90, rot+90);
        }
      //noStroke();
    }
    
    //animations
    float repelani = 0;
    
    boolean isIn(float x,float y,float r){
        return Util.getDisSqrd(this.x, this.y, this.x-vx*sizemult,this.y-vy*sizemult, x, y)<r*r;
        //return sqrd(x-(this.x-vx*sizemult/2f))+sqrd(y-(this.y-vy*sizemult/2f))<sqrd(r+size);
    }
    
    boolean isIn(Bullet b){
        
        float t = (dmg/4f +1 + b.dmg/4f +1)/2f;
        
        return Util.intersectDis(this.x, this.y, this.x-vx*sizemult,this.y-vy*sizemult, b.x, b.y, b.x-b.vx*b.sizemult,b.y-b.vy*b.sizemult)<sqrd(t*2);
        //return sqrd(x-(this.x-vx*sizemult/2f))+sqrd(y-(this.y-vy*sizemult/2f))<sqrd(r+size);
    }
    
    boolean isIn(float x,float y,float x2,float y2){
        
        float t = (dmg/4f +1)/2f;
        
        return Util.intersectDis(this.x, this.y, this.x-vx*sizemult,this.y-vy*sizemult, x, y, x2,y2)<sqrd(t*2);
        //return sqrd(x-(this.x-vx*sizemult/2f))+sqrd(y-(this.y-vy*sizemult/2f))<sqrd(r+size);
    }
}
