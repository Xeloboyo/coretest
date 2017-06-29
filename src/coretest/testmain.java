/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coretest;
import core.*;
import static core.Core.*;
import static core.Window.*;
import java.io.File;
import java.util.ArrayList;
import kuusisto.tinysound.Music;
import kuusisto.tinysound.TinySound;
import kuusisto.tinysound.internal.MemMusic;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author willi
 */
public class testmain {
public static Color bg = color(219,210,195);
public static Color fg = color(180,50,50);
public static Color abg = color(64,61,57);
public static Color abg2 = color(104,99,92);


static float wx=200,wy=300,ww=400,wh=600;
static float awx=200,awy=300,aww=400,awh=600;
static float mx,my;

static float speed = 1f;
static float ldmg=0;

//player
static Player p1;
///bbullets
static ArrayList<Bullet> bullets = new ArrayList();
static ArrayList<Particle> fx = new ArrayList();

static boolean firing =false;
static float xmove=0,ymove=0;

static float shakex=0,shakey=0,shakeam=0;

static float beatno = 0;
static float bpm = 180;

//title 0, play 1, playdead 2

static int screen = 0;
    /**
     * @param args the command line arguments
     */
     
     static void setup(){
         Window.size(1000,600); 
        
        mx = width/2f;
        my = height/2f;
        p1 = new Player(mx,my+100);
        textFont(createFont("Manteka",48));
         textSize(20);
     }
     
    static float deathtimer=0; 
    public static void main(String[] args) {
        setup();
        TinySound.init();
        File f = new File("music\\test.wav");
        //println(f.exists()+"ESOT");
        MemMusic test = (MemMusic)TinySound.loadMusic(f);
        test.setLoop(true);
        test.setLoopPositionBySeconds(1);
        test.play(true);
        
        
        Window.keyaction = new KeyAction() {
            @Override
            public void keyPressed() {
                for(char key:Window.key){
                    switch(key){
                        case ' ':
                          firing = true;
                        break;
                        
                        
                    }
                }
            }

            @Override
            public void keyReleased() {
                for(char key:Window.key){
                    switch(key){
                        case ' ':
                          firing = false;
                        break;
                        
                        
                    }
                }
            }

            @Override
            public void mousePressed() {
                switch(screen){
                    case 0:
                        if(isWithin(mouseX, mouseY, mx-200,my-90,400,150)){
                            screen=1;
                        }
                        break;
                }
            }

            @Override
            public void mouseReleased() {
            }
        };
        
        
        
        background(220);
        strokeWeight(2);
        //PImage p = PImage.loadImage("img\\test2.png");
        background(abg);
        int duration=0;
        int lastduration = 0;
        
        float deathsize = 0;
        //setSeed(2);
        while(!Window.closeRequested){
            beatno+=bpm*(Window.frametime/(60000000000f));
            switch(screen){
                
                //title
                case 0:
                    fill(bg);
                    noStroke();
                    rect(mx-200,my-90,400,150);
                    fill(fg);
                    textSize(48);
                    textAlign(TEXTCENTER);
                    text("Start",mx,my-40);
                    break;
                case 2:// ondeath
                    
                    deathsize += (wh+45-deathsize)/20f;
                    deathtimer++;
                    for(int i = 0;i<bullets.size();i++){
                        
                        if(bullets.get(i).isIn(p1.x, p1.y, deathsize)){
                            bullets.get(i).destroy=true;
                            //i--;
                        }
                    }
                    if(deathtimer>100){
                        p1 = new Player(mx,my+100);
                        
                        screen=1;
                        deathtimer=0;
                    }
                    
                case 1: //play
                    wh += (awh-wh)/2f;
                    ww += (aww-ww)/2f;
                    wx += (awx-wx)/2f;
                    wy += (awy-wy)/2f;
                    translate(shakex, shakey);
                    shakex = random(-shakeam, shakeam);
                    shakey = random(-shakeam, shakeam);
                    shakeam/=1.3f;
                    textAlign(TEXTLEFT);
                    strokeWeight(ldmg);
                    stroke(fg);
                    fill(bg);
                    rect(mx-wx,my-wy,ww,wh);
                    if(screen==2){
                        strokeWeight(2);
                        stroke(abg);
                        noFill();
                        ellipse(p1.x, p1.y, deathsize*2, deathsize*2);
                    }
                    for(int i = 0;i<bullets.size();i++){
                        bullets.get(i).updateRepel(speed);
                        
                    }
                    for(int i = 0;i<bullets.size();i++){
                        bullets.get(i).update(speed);
                        if(bullets.get(i).destroy){
                            bullets.get(i).ondeath();
                            bullets.remove(i);
                            i--;
                        }
                    }
                    
                        for(int i = 0;i<fx.size();i++){
                            fx.get(i).update(speed);
                            if(fx.get(i).remove){
                                fx.remove(i);
                                i--;
                            }
                        }
                        
                        for(Particle p:fx){
                            p.draw();
                        }
                        for(Bullet b:bullets){
                            b.display();
                        }
                        fill(20);
                        textAlign(TEXTRIGHT);
                        
                        text(duration+"",mx-wx+ww,my-wy+5);
                        text(lastduration+"",mx-wx+ww,my-wy+20);
                        text((int)beatno+"",mx-wx+ww,my-wy+50);
                    if(screen!=2){
                        duration++;
                        strokeWeight(ldmg);
                        ldmg/=3f;
                        p1.update(speed);
                        p1.display();
                        if(true){
                          p1.fire();
                        }
                        p1.moveTo(mouseX,mouseY);
                        
                        if(p1.dead){
                            deathsize = 0;
                            shakeam+=24;
                            lastduration = (int)max(lastduration,duration);
                            duration = 0;
                            screen=2;
                            fx.add(new Explosion(p1.x,p1.y,32,abg));
                            fx.add(new Explosion(p1.x,p1.y,132,abg));
                            fx.add(new Explosion(p1.x,p1.y,102,fg));
                            for(int i = 0;i<10;i++)
                                fx.add(new Debris(p1.x,p1.y,random(-5f,5f),random(-5f,5f),2,abg));
                        }
                    }
                    break;
                
                
                  
            }
            
            if(random(15)<1&&(screen==1||screen==2)){
                fx.add(new Stuff(mx-wx, my-wy, 34, random(9)+2, abg));
            }
            if(random(15)<1&&(screen==1||screen==2)){
                fx.add(new Stuff(mx+wx, my-wy, 34, random(9)+2, abg));
            }
            if(random(5)<1&&screen==1){
                Bullet b = new Bullet(random(mx-wx,mx-wx+ww),1,random(170,190),4,10);
                if(random(10)<1){
                    b.follow=8;
                    b.dmg=20;
                    b.setDir(random(170,190),2.3f);
                    b.setShootable(true);
                }else if(random(60)<1){
                    b.follow=0;
                    b.dmg=30;
                    b.repel=2.4f;
                    b.setDir(180,2);
                    b.setShootable(true);
                    b.hp=65;
                }else if(random(70)<1){
                    b.follow=0;
                    b.dmg=90;
                    
                    b.setDir(180,1.3f);
                    b.setShootable(true);
                    b.hp=165;
                    
                    if(random(2)<1){
                        b.split = bulletList.BIGHOMING.b;
                    }else{
                        b.split = bulletList.FASTBOUNCING.b;
                    }
                    b.splitamount=5;
                    b.spliteven=true;
                    b.splitanglestart=0;
                    b.splitanglespread=90;
                    
                }else if(random(70)<1){
                    b= new Bullet(random(mx-wx,mx-wx+ww),1,random(170,190),4, bulletList.FASTBOUNCING.b);
                    
                }

                bullets.add(b);
            }
            Window.updateDraw();
        }
        TinySound.shutdown();
    }
    
    
    static float getangle(float startx,float starty,float endx,float endy){
        float t = 180-degrees(atan2(startx-endx,starty-endy));
        if(t<0){
          t+=360;
        }

        return t;
    }
    
}
