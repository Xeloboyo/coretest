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

enum blockenum{
  CENTRE,GUN,SHIELD,NONE;
}
/**
 *
 * @author willi
 */
public class Player {
  blockenum[][] blocks = new blockenum[3][5];
  float x,y;
  //float ax,ay;
  float shield=0;
  float shieldcap=2;
  
  int shieldam=1;
  final int shieldrad= 40;
  Player(float x,float y){
    for(int i = 0;i<3;i++){
      for(int j = 0;j<5;j++){
        blocks[i][j]=blockenum.NONE;
      }
    }
    blocks[1][3] = blockenum.CENTRE;
    this.x=x;
    this.y=y;
    blocks[1][2] = blockenum.GUN;
    
    blocks[1][4] = blockenum.SHIELD;
  }
  
  float tick = 0;
  float shieldspin = 0;
  float lastfire=100;
  float firetrans = 0;
  
  PVector movement = new PVector(0,1);
  
  float repel=0;
  
  boolean dead=false;
  
  void move(float xvel,float yvel){
    x+=xvel;
    y+=yvel;
  }
  void moveTo(float x,float y){
    if(y>my-wy+wh){
      y=my-wy+wh;
    }else if(y<my-wy){
      y=my-wy;
    }
    
    if(x>mx-wx+ww-5){
      x=mx-wx+ww-5;
    }else if(x<mx-wx+5){
      x=mx-wx+5;
    }  
      
    this.x+=(x-this.x)/5f;
    this.y+=(y-this.y)/5f;
    
    
    
  }
  void update(float speed){
    float diff = (PI/5f)*speed;
    tick+=diff;
    shieldspin+=diff*5*(sin(tick/6f)+1.1);
    lastfire+=diff;
    firetrans/=2f;
    
    shield += shieldam*0.004f;
    if(shield>shieldcap){
        shield = shieldcap;
    }
    invincible--;
  }
  
  void fire(){
    if(lastfire>5){
      //println(lastfire);
      firetrans=1;
      lastfire=0;
      for(int i = 0;i<3;i++){
        for(int j = 0;j<5;j++){
           switch(blocks[i][j]){
          
            case GUN:
               Bullet b = new Bullet(x-18+i*12+6,y-30+j*12+6,0,10,20);
                b.enemy = false;
                //b.follow= 18f;
                //b.repel=0.4f;
                bullets.add(b);
            break;
           }
        }
      }
    }
  }
    
  
  void display(){
    
    pushMatrix();
    translate(x-18,y-12*2.5f);
    float ax,ay;
    for(int i = 0;i<3;i++){
      for(int j = 0;j<5;j++){
        ax=i*12;
        ay=j*12;
        switch(blocks[i][j]){
          case CENTRE:
            fill(60);
            rect(ax+1,ay+1,10,10);
            fill(100);
            arc(ax+6,ay+1,10,10,0,180);
          break;
          case GUN:
            fill(fg);
            rect(ax+4,ay+firetrans*6,4,6-firetrans*6);
            fill(60);
            rect(ax+2,ay+5,8,5);
            
          break;
          case SHIELD:
            fill(60);
            rect(ax+3,ay+3,6,6);
            
            noFill();
            stroke(fg);
            arc(ax+6,ay+6,18,18,shieldspin,shieldspin+90);
            //noStroke();
          break;
        }
      }
    }
    if(repel>0){
      stroke(abg);
      noFill();
      strokeWeight(2);
      arc(18,12*2.5f,80,80,shieldspin,shieldspin+180);
      //noStroke();
    }
    if(invincible>0){
      stroke(abg);
      noFill();
      strokeWeight(1);
      arc(18,12*2.5f,80,80,0,invincible*3);
      arc(18,12*2.5f,80,80,-invincible*3,0);
      //noStroke();
    }
    popMatrix();
    textSize(12);
    fill(abg);
    textAlign(TEXTLEFT);
    text("Shield", mx-wx+10, 15);
    if(shield>0){
        
        float t =shield;
      for(int i = 0;i<shield;i++){
          if(t>=1){
            fill(abg);
            rect(mx-wx+10+i*8,40,6,6);
          }else{
            fill(fg);
            rect(mx-wx+10+i*8,40,6,6*t);
          }
          t--;
      }
    }
  }
  
  boolean touches(float x,float y,float r){
      return sqrd(x-this.x)+sqrd(y-this.y)<sqrd(r+10);
  }
  int invincible = 0;
  void damage(){
      if(invincible<0){
        if(shield>=1){
            shield--;
        }else{
            shield=0;
            dead=true;
        }  
        invincible = 50;
      }
      
  }
}
