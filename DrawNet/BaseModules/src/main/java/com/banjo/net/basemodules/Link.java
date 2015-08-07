package com.banjo.net.basemodules;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Link implements Serializable{

	private static final long serialVersionUID = 1L;
	public Point start;
	public Point end;
	public int label_start;
	public int label_end;
	public int weight;
	public boolean directLink = false;
	public Color self_color = Color.LIGHT_GRAY;
	public Link(Point s,Point e){
		//this.start = new Point(s.x,s.y);
		//this.end = new Point(e.x,e.y);
		this.start = s;
		this.end = e;
	}
	public void paint(Graphics g){
		Color c = g.getColor();
		g.setColor(self_color);
		if(directLink){
			this.paintk(g, start.x, start.y, end.x, end.y);
		}
		g.drawLine(start.x, start.y, end.x, end.y);
		if(weight!=0)
			g.drawString(weight+"", (start.x+end.x)/2, (start.y+end.y)/2);
		g.setColor(c);
	}
	
      public   void  paintk(Graphics g,  int  x1,  int  y1,  int  x2,  int  y2)  {

         double  H  =   10 ;  //the height of the arrow
          double  L  =   4 ; //half of the down
          int  x3  =   0 ;
         int  y3  =   0 ;
         int  x4  =   0 ;
         int  y4  =   0 ;
         double  awrad  =  Math.atan(L  /  H);  //angle    
          double  arraow_len  =  Math.sqrt(L  *  L  +  H  *  H); //Length   
          double [] arrXY_1  =  rotateVec(x2  -  x1, y2  -  y1, awrad,  true , arraow_len);
         double [] arrXY_2  =  rotateVec(x2  -  x1, y2  -  y1,  - awrad,  true , arraow_len);
         double  x_3  =  x2  -  arrXY_1[ 0 ];  // (x3,y3)
          double  y_3  =  y2  -  arrXY_1[ 1 ];
         double  x_4  =  x2  -  arrXY_2[ 0 ]; // (x4,y4)
          double  y_4  =  y2  -  arrXY_2[ 1 ];

        Double X3  =   new  Double(x_3);
        x3  =  X3.intValue();
        Double Y3  =   new  Double(y_3);
        y3  =  Y3.intValue();
        Double X4  =   new  Double(x_4);
        x4  =  X4.intValue();
        Double Y4  =   new  Double(y_4);
        y4  =  Y4.intValue();


         g.drawLine(x1, y1, x2, y2);

         g.setColor(Color.white);
         g.fillPolygon(new int[]{x2,x3,x4},new int[]{y2,y3,y4},3);
         g.setColor(self_color);
        // g.drawLine(x2, y2, x3, y3);

        // g.drawLine(x2, y2, x4, y4);
    } 

     public   double [] rotateVec( int  px,  int  py,  double  ang,  boolean  isChLen,
             double  newLen)  {

         double  mathstr[]  =   new   double [ 2 ];
         //rotation
          double  vx  =  px  *  Math.cos(ang)  -  py  *  Math.sin(ang);
         double  vy  =  px  *  Math.sin(ang)  +  py  *  Math.cos(ang);
         if  (isChLen)  {
             double  d  =  Math.sqrt(vx  *  vx  +  vy  *  vy);
            vx  =  vx  /  d  *  newLen;
            vy  =  vy  /  d  *  newLen;
            mathstr[ 0 ]  =  vx;
            mathstr[ 1 ]  =  vy;
        } 
         return  mathstr;
    }
     
	
}
