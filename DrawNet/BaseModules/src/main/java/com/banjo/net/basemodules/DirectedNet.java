package com.banjo.net.basemodules;

import java.io.Serializable;
import java.util.ArrayList;

public class DirectedNet extends Net  implements Serializable{

	private static final long serialVersionUID = 1L;

	public DirectedNet(String name,ArrayList<Node> nodes,ArrayList<Link> links,int type){
		super(name,nodes,links,type);
	}
	/**
	 * print print some basic of the Net
	 */
	@Override
	public String print(){
		return "AdjacentMatrix:\n"+printMatrix(adjMatrix.useMatrix) + "\nReachableMatrix:\n"+printMatrix(reaMatrix.useMatrix);
	}
	
}
