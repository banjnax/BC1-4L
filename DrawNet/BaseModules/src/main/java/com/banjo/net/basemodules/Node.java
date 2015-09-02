package com.banjo.net.basemodules;

import java.awt.Color;
import java.awt.Graphics;
import java.io.Serializable;

public class Node implements Serializable{

	private static final long serialVersionUID = 1L;
	public Point self;
	public String label;
	public int number;
	int count=0;
	public  int size = 20;
	public Color self_color = Color.black;
	public Color default_Color = Color.black;
	public int inDgree = 0;
	public int outDgree = 0;
	public Node(int x,int y, int s){
		this.self = new Point(x,y);
		this.label = s+"";
		this.number = s;
		while(s!=0){
			s/=10;
			count++;
		}
	}
	/**
	 * paint paint the node itself
	 * @param g, the graphical pen from the top
	 */
	public void paint(Graphics g){
		Color c =g.getColor();
		g.setColor(self_color);
		g.fillOval(self.x-size/2, self.y-size/2, size, size);
		g.setColor(Color.white);
		g.drawString(label, self.x-3-count*2, self.y+4);
		g.setColor(c);
	}
	/**
	 * inDegreeAdd when a link is added, then this method will be executed;we make the inDgree attr of the node added one
	 */
	public void inDegreeAdd(){
		inDgree++;
		this.size+=(4*(inDgree/10));//the size increase 4 when the indgree increase 10
	}
	public void outDegreeAdd(){
		outDgree++;
	}
	/**
	 * inDegreeDec when a link is added, then this method will be executed;we make the inDgree attr of the node reduce one
	 */
	public void inDegreeDec(){
		inDgree--;
		this.size+=(2*(inDgree/10));//the size increase 4 when the indgree increase 10
	}
	public void outDegreeDec(){
		outDgree--;
	}
}
