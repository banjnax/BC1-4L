package com.banjo.net.bc14ls;

import java.util.ArrayList;

public class VisitedUrl {
	public ArrayList<String> visited = new ArrayList<String>();
	public void print(){
		for(int i = 0;i<visited.size();i++) System.out.println(visited.get(i));
	}
}
