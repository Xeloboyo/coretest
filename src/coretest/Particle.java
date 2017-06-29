/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coretest;

import core.*;
import static core.Core.*;
import static coretest.testmain.*;

/**
 *
 * @author willi
 */
abstract class Particle{
  float x,y,vx,vy,size;
  float life =1 ;
  boolean remove=false;
  Particle(float x,float y,float vx,float vy,float s){
    this.x=x;
    this.y=y;
    this.vx=vx;
    this.vy=vy;
    this.size=s;
  }
  Color fore =fg;
  Particle(float x,float y,float vx,float vy,float s,Color c){
    this.x=x;
    this.y=y;
    this.vx=vx;
    this.vy=vy;
    this.size=s;
    this.fore=c;
  }
  abstract void update(float speed);
  abstract void draw();
}

class Explosion extends Particle{
  float as=0;
  Explosion(float x,float y,float s,Color c){
    super(x,y,0,0,s,c);
  }
  void update(float speed){
    as+= (size-as)/(3f/speed);
    if(size-as<0.1){
      remove=true;
    }
  }
  void draw(){
      noFill();
      stroke(fore);
      strokeWeight((size-as)/2f);
      ellipse(x,y,as,as);
  }
}

class Debris extends Particle{
  float as=1;
  Debris(float x,float y,float vx,float vy,float s,Color c){
    super(x,y,vx,vy,s,c);
  }
  void update(float speed){
    as = life-=0.08*speed;
    x+=vx*speed;
    y+=vy*speed;
    vy+=0.2f*speed;
    if(life<=0){
      remove=true;
    }
  }
  void draw(){
      noFill();
      stroke(fore);
      strokeWeight((size*as));
      line(x,y,x-vx*2,y-vy*2);
  }
}
class Stuff extends Particle{
  float as=1;
  Stuff(float x,float y,float speed,float s,Color c){
    super(x,y,0,speed,s,c);
  }
  void update(float speed){
    
    
    y+=vy*speed;
    
    if(y-vy*2*size>=wh+my-wy){
      remove=true;
    }
  }
  void draw(){
      noFill();
      stroke(fore);
      strokeWeight((size*as));
      line(x,y,x-vx*2,y-vy*2*size);
  }
}
class Trail extends Particle{
  float[] xs;
  float[] ys;
  Bullet b;
  Trail(float x,float y,int asize,float size,Color c,Bullet b){
    super(x,y,0,0,size,c);
    xs = new float[asize];
    ys = new float[asize];
    this.b=b;
    for(int i = 0;i<asize;i++){
        xs[i] = x;
        ys[i] = y;
    }
  }
  float  tick= 0;
  void update(float speed){
    tick+=speed;
    for(int i = xs.length-1;i>0;i--){
        xs[i] = xs[i-1];
        ys[i] = ys[i-1];
    }
    xs[0] = x;
    ys[0] = y;
    x=b.x-b.vx*b.sizemult;
    y=b.y-b.vy*b.sizemult;
    if(b.destroy){
        remove=true;
    }
    
  }
  void draw(){
      
      stroke(fore);
      
      int y=0;
        for(int i = xs.length-1;i>0;i--){
            if(abs(i-tick)%15<5){
                continue;
            }
            if(y<5){
                strokeWeight(size*y/5f);
            }else{
                strokeWeight(size);
            }
            line(xs[i], ys[i],xs[i-1], ys[i-1]);
            y++;
        }
     
  }
}