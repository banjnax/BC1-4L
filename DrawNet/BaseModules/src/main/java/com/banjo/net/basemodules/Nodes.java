package com.banjo.net.basemodules;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

public class Nodes {
	public ArrayList<Node> ags;
	public Nodes(){
		this.ags = new ArrayList<Node>();
	}
	public void paint(Graphics g){
		Color c = g.getColor();
		Iterator<Node> it = ags.iterator();
		while(it.hasNext()){
			Node ag = it.next();
			g.setColor(ag.self_color);
			ag.paint(g);
		}
		g.setColor(c);
	}
	public boolean findNode(int label){
		for(int i=0;i<ags.size();i++){
			if(ags.get(i).number==label) return true;
		}
		return false;
	}
	public Node getNode(int label){
		for(int i=0;i<ags.size();i++){
			if(ags.get(i).number == label) return ags.get(i);
		}
		return null;
	}
	public Node getNode(int x,int y){//the point(x,y) in one of the node
		Rectangle r = new Rectangle(x-1,y-1,2,2);
		Node n = null;
		Rectangle r1 = null;
		for(int i=0;i<ags.size();i++){
			n = ags.get(i);
			r1 = new Rectangle(n.self.x-n.size/2,n.self.y-n.size/2,n.size,n.size);
			if(r1.intersects(r)) return ags.get(i);
		}
		return null;
	}
}
