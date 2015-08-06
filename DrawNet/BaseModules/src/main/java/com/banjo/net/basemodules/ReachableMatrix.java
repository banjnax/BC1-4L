package com.banjo.net.basemodules;

import java.util.ArrayList;
import java.util.Iterator;

public class ReachableMatrix extends BaseMatrix{

	private static final long serialVersionUID = 1L;
	public ArrayList<Link> links;
	public ReachableMatrix(int row,int col, ArrayList<Link> links){
		super(row,col);
		this.links = links;
		constructMatrix();
	}
	public void constructMatrix(){
		Iterator<Link> it = this.links.iterator();
		while(it.hasNext()){
			Link l = it.next();
			this.matrix[l.label_end-1][l.label_start-1] = 1;//A(ij)=j->i
			if(!l.directLink)
				this.matrix[l.label_start-1][l.label_end-1] = 1;
		}
	}
	
}
