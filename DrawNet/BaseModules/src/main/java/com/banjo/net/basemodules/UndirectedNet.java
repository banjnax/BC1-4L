package com.banjo.net.basemodules;

import java.io.Serializable;
import java.util.ArrayList;

public class UndirectedNet extends Net  implements Serializable{

	private static final long serialVersionUID = 1L;

	public UndirectedNet(String name,ArrayList<Node> nodes,ArrayList<Link> links,int type){
		super(name,nodes,links,type);
	}
	
	@Override
	public String print(){
		return "AdjacentMatrix:\n"+printMatrix(adjMatrix.useMatrix) + "\nReachableMatrix:\n"+printMatrix(reaMatrix.useMatrix);
	}
}
