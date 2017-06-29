/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package coretest;

import core.*;
import static core.Core.*;

/**
 *
 * @author willi
 */
public class Util {
  public static PVector LinesIntersect(float linestartx,float linestarty, float lineendx,float lineendy, float rx, float ry,int deg,float off)
  {
    deg=deg%360;
    PVector CmP = new PVector(rx - linestartx, ry - linestarty);
    PVector r = new PVector(lineendx - linestartx, lineendy - linestarty);
    
    float rxs = r.x * (INTsin(deg)) - r.y * (INTcos(deg));
 
    
 
    if (rxs == 0f)
      return null; // Lines are parallel.
 
    float rxsr = 1f / rxs;
    
    float t = (CmP.x * (-INTsin(deg)) - CmP.y * (INTcos(deg))) * rxsr;
    float u = (CmP.x * r.y - CmP.y * r.x) * rxsr;
    u+=off;
      if ( (t >= 0f) && (t <= 1f)&&u>=0){
      return new PVector(rx+(INTcos(deg))*u,ry+(-INTsin(deg))*u);
    }
    return null;
  }
  public static float getDisSqrd(float lx,float ly,float lx2,float ly2, float px,float py){
    
      
    float u = ((px-lx)*(lx2-lx) + (py-ly)*(ly2-ly))/(distsqrd(lx,ly,lx2,ly2));
    u = clamp(u, 0, 1);
    return distsqrd(lx+u*(lx2-lx),ly+u*(ly2-ly),px,py);
  }
  public static float getDis(float lx,float ly,float lx2,float ly2, float px,float py){
    
    
    return sqrt(getDisSqrd(lx, ly, lx2, ly2, px, py));
  }
  public static float intersectDis(float x,float y, float x2, float y2, 
                        float lx,float ly, float lx2,float ly2){
      if(doIntersect(x, y, x2, y2, lx, ly, lx2, ly2)){
          return 0;
      }
      float t = getDisSqrd(lx, ly, lx2, ly2,x, y);
      
      t = min(getDisSqrd(lx, ly, lx2, ly2,x2, y2),t);
      t = min(getDisSqrd(x, y, x2, y2,lx, ly),t);
      t = min(getDisSqrd(x, y, x2, y2,lx2, ly2),t);
      
      return t;
  }
  
  // A C++ program to check if two given line segments intersect

 
// Given three colinear points p, q, r, the function checks if
// point q lies on line segment 'pr'
    public static boolean onSegment(float px,float py, float qx,float qy, float rx,float ry)
    {
        if (qx <= max(px, rx) && qx >= min(px, rx) &&
            qy <= max(py, ry) && qy >= min(py, ry))
           return true;

        return false;
    }
 
// To find orientation of ordered triplet (p, q, r).
// The function returns following values
// 0 --> p, q and r are colinear
// 1 --> Clockwise
// 2 --> Counterclockwise
    public static int orientation(float px,float py, float qx,float qy, float rx,float ry)
    {
        
        float val = (qy - py) * (rx - qx) -
                  (qx - px) * (ry - qy);

        if (val == 0) return 0;  // colinear

        return (val > 0)? 1: 2; // clock or counterclock wise
    }
 
// The main function that returns true if line segment 'p1q1'
// and 'p2q2' intersect.
    public static boolean doIntersect(float x,float y, float x2, float y2, 
                        float lx,float ly, float lx2,float ly2){
        // Find the four orientations needed for general and
        // special cases
        int o1 = orientation(x,y, x2,y2, lx,ly);
        int o2 = orientation(x,y, x2,y2, lx2,ly2);
        int o3 = orientation(lx,ly, lx2,ly2, x,y);
        int o4 = orientation(lx,ly, lx2,ly2, x2,y2);

        // General case
        if (o1 != o2 && o3 != o4)
            return true;

        // Special Cases
        // x,y, x2,y2 and lx,ly are colinear and lx,ly lies on segment x,yx2,y2
        if (
         (o1 == 0 && onSegment(x,y, lx,ly, x2,y2))|| //return true;

        // x,y, x2,y2 and lx,ly are colinear and lx2,ly2 lies on segment x,yx2,y2
         (o2 == 0 && onSegment(x,y, lx2,ly2, x2,y2))|| //return true;

        // lx,ly, lx2,ly2 and x,y are colinear and x,y lies on segment lx,lylx2,ly2
         (o3 == 0 && onSegment(lx,ly, x,y, lx2,ly2))|| //return true;

         // lx,ly, lx2,ly2 and x2,y2 are colinear and x2,y2 lies on segment lx,lylx2,ly2
         (o4 == 0 && onSegment(lx,ly, x2,y2, lx2,ly2)) 
        )
        {
         return true;
        }
        
        
        return false; // Doesn't fall in any of the above cases
    }
   /* boolean isPolygonsIntersecting(Polygon a, Polygon b)
    {
    for (int x=0; x<2; x++)
    {
        Polygon polygon = (x==0) ? a : b;

        for (int i1=0; i1<polygon.getPoints().length; i1++)
        {
            int   i2 = (i1 + 1) % polygon.getPoints().length;
            Point p1 = polygon.getPoints()[i1];
            Point p2 = polygon.getPoints()[i2];

            Point normal = new Point(p2.y - p1.y, p1.x - p2.x);

            double minA = Double.POSITIVE_INFINITY;
            double maxA = Double.NEGATIVE_INFINITY;

            for (Point p : a.getPoints())
            {
                double projected = normal.x * p.x + normal.y * p.y;

                if (projected < minA)
                    minA = projected;
                if (projected > maxA)
                    maxA = projected;
            }

            double minB = Double.POSITIVE_INFINITY;
            double maxB = Double.NEGATIVE_INFINITY;

            for (Point p : b.getPoints())
            {
                double projected = normal.x * p.x + normal.y * p.y;

                if (projected < minB)
                    minB = projected;
                if (projected > maxB)
                    maxB = projected;
            }

            if (maxA < minB || maxB < minA)
                return false;
        }
    }

    return true;
}*/
  
  
}
