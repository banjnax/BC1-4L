package com.banjo.net.netframe;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import Jama.Matrix;

import com.banjo.net.nettools.MatrixFactory;

public class DrawChart {
	JPanel jchartp = new JPanel();
	JLabel chartType = new JLabel("Chart Type:");
	JComboBox<String> cType = new JComboBox<String>();
	JLabel dataSet = new JLabel("Data Set:");
	JComboBox<String> dSet = new JComboBox<String>();
	JButton draw = new JButton("Draw");
	JPanel blank = new JPanel();

	ChartPanel chartp = null;
	DrawGraph dg = null;
	MatrixFactory mf = new MatrixFactory();
	JFreeChart chart = null;
	DefaultCategoryDataset DataSet = new DefaultCategoryDataset();
	public DrawChart(DrawGraph dg){
		this.dg = dg;
		jchartp.setLayout(new GridBagLayout());
        chart = ChartFactory.createBarChart3D("Catogram",
                "Degrees", "Nodes", DataSet, PlotOrientation.VERTICAL, 
                false, false, false);
        chartp = new ChartPanel(chart);
        
        cType.addItem("BarChart3D");
        
        dSet.addItem("Nodes-Degree");
        
        setComponent();
        addListerens();
	}
	public void setComponent(){
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		
		cons.gridx = 0;
		cons.gridy = 0;
		cons.weighty = 1;
		cons.weightx = 1;
		cons.gridheight = 6;
		jchartp.add(chartp, cons);
		
		cons.insets = new Insets(10, 10, 0, 0);
		cons.gridx = 1;
		cons.gridy = 0;
		cons.weighty = 0;
		cons.weightx = 0;
		cons.gridheight = 1;
		jchartp.add(chartType,cons);
		
		cons.gridx = 1;
		cons.gridy = 1;
		jchartp.add(cType,cons);
		
		cons.gridx = 1;
		cons.gridy = 2;
		jchartp.add(dataSet,cons);
		
		cons.gridx = 1;
		cons.gridy = 3;
		jchartp.add(dSet,cons);
		
		cons.gridx = 1;
		cons.gridy = 4;
		jchartp.add(draw,cons);
		 
		cons.gridx = 1;
		cons.gridy = 5;
		cons.weighty = 1;
		jchartp.add(blank,cons);
	}
	public void addListerens(){
		draw.addActionListener(new ButtonAction());
	}
	public void draw(){
		//construct the DataSet
		if(dg.net != null){
	        Matrix degreeMatrix = mf.getCocitation(dg.net);
	        for(int i = 0;i<dg.count;i++){//Construct the degree of nodes chart data
	        	DataSet.addValue(degreeMatrix.get(i, i), "number", (i+1)+"");
	        }
		}
		else DrawNetwork.printInfo("Error:Data Set is null!\n", "red");
	}
	private class ButtonAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == draw){
				draw();
			}
		}
		
	}
}
