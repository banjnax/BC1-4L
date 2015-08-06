package com.banjo.net.basemodules;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;

public class Links {
	public ArrayList<Link> ls;
	public Links(){
		ls = new ArrayList<Link>();
	}
	public void paint(Graphics g){
		Iterator<Link> it = ls.iterator();
		while(it.hasNext())it.next().paint(g);
	}
	public boolean findLink(int start,int end){
		for(int i=0;i<ls.size();i++){
			if(ls.get(i).label_start==start && ls.get(i).label_end==end) return true;
		}
		return false;
	}
	public Link getLink(int start,int end){
		for(int i=0;i<ls.size();i++) if(ls.get(i).label_start == start && ls.get(i).label_end == end) return ls.get(i);
		return null;
	}
}
