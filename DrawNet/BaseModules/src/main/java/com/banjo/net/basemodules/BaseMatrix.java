package com.banjo.net.basemodules;

import java.io.Serializable;

import Jama.*;

public class BaseMatrix  implements Serializable{

	private static final long serialVersionUID = 1L;
	public int row;
	public int col;
	double[][] matrix;//our matrix data
	public Matrix useMatrix;//the matrix with rich functions
	public BaseMatrix(int row,int col){
		this.row = row;
		this.col = col;
		this.matrix = new double[row][col];
		this.useMatrix = new Matrix(matrix);
	}
}
