package com.banjo.net.netframe;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.*;
import javax.xml.bind.ValidationException;

import com.banjo.net.basemodules.DirectedNet;
import com.banjo.net.basemodules.Link;
import com.banjo.net.basemodules.Links;
import com.banjo.net.basemodules.Net;
import com.banjo.net.basemodules.Node;
import com.banjo.net.basemodules.Nodes;
import com.banjo.net.basemodules.UndirectedNet;
import com.banjo.net.nettools.MatrixFactory;

public class DrawGraph {
	public static int WIDTH = 648;
	public static int HEIGHT = 478;
	Nodes nodes = new Nodes();
	Links links = new Links();
	Net net = null;
	MatrixFactory mf = new MatrixFactory();

	JPanel graph = new JPanel();
	
	JPanel palette = new JPanel();
	JPanel blank = new JPanel();
	
	JTextField start = new JTextField(2);
	JLabel s = new JLabel("Start point:");
	JTextField end = new JTextField(2);
	JLabel e = new JLabel("End point:");
	JTextField weight = new JTextField(2);
	JLabel w = new JLabel("Weight:");
	
	JButton link = new JButton("Link");
	JButton unlink = new JButton("Unlink");
	
	JRadioButton undirect = new JRadioButton("Undirect");
	JRadioButton direct = new JRadioButton("Direct");
	ButtonGroup type = new ButtonGroup();
	
	JButton netDone = new JButton("NetDone!");
	
	JLabel f = new JLabel("Matrixes:");
	JComboBox<String> functions = new JComboBox<String>();
	
	public int count=0;//record the number of nodes
	public int W_Y = 70;//just for the press action, make a right position of the node
    GridBagConstraints cons = null;
    ListMenuAction lma = new ListMenuAction();
    ButtonAction ba = new ButtonAction();
    MouseAction mousea = new MouseAction();
	public DrawGraph(){
		graph.setLayout(new GridBagLayout());
		constructGraph();
	}
	private void constructGraph(){
        //set background color of the palette
		palette.setBackground(Color.white);
		palette.addMouseListener(mousea);//for the press action
		palette.addMouseMotionListener(mousea);//for the dragged node
		
		//Base input area settings
		e.setHorizontalAlignment(JLabel.RIGHT);
		start.setHorizontalAlignment(JTextField.CENTER);
		s.setHorizontalAlignment(JLabel.RIGHT);
		end.setHorizontalAlignment(JTextField.CENTER);
		w.setHorizontalAlignment(JLabel.RIGHT);
		weight.setHorizontalAlignment(JTextField.CENTER);
		
		//operation on the base input module
		link.addActionListener(ba);
		unlink.addActionListener(ba);
		
		//the radiobutton group, choose different edge and network
        type.add(undirect);
        type.add(direct);
        undirect.setSelected(true);
        
		netDone.addActionListener(ba);

		 //Functions part 1: Matrix part
		functions.addItem("AdjMatrix");
		functions.addItem("ReaMatrix");
		functions.addItem("CocitMatrix");
		functions.addItem("CoupMatrix");
		functions.addItem("LaplacMatrix");
		functions.addActionListener(lma);
		
		setGraphCompponents();
	}
	private void setGraphCompponents() {
		// TODO Auto-generated method stub
		cons = new GridBagConstraints();
		
		//Add the palette
		cons.fill = GridBagConstraints.BOTH;//for the extra space 
		
		cons.gridx = 0;
		cons.gridy = 0;
		cons.weightx = 1;
		cons.weighty = 1;
		cons.gridwidth = 1;
		cons.gridheight = 10;
		graph.add(palette,cons);
		
		//Add the operation control panel
		
		//Add start,end,weight
		cons.insets = new Insets(10, 10, 0, 0);
		cons.gridx = 1;
		cons.gridy = 0;
		cons.gridwidth = 1;
		cons.gridheight = 1;
		cons.weightx = 0;
		cons.weighty = 0;
		graph.add(s,cons);
		
		cons.gridx = 2;
		cons.gridy = 0;
		graph.add(start,cons);
		
		cons.gridx = 1;
		cons.gridy = 1;
		graph.add(e,cons);
		
		cons.gridx = 2;
		cons.gridy = 1;
		graph.add(end,cons);
		
		cons.gridx = 1;
		cons.gridy = 2;
		graph.add(w,cons);
		
		cons.gridx = 2;
		cons.gridy = 2;
		graph.add(weight,cons);
		

		cons.gridx = 1;
		cons.gridy = 3;
		cons.gridwidth = 1;
		graph.add(link,cons);
		
		cons.insets = new Insets(10, 0, 0, 0);
		cons.gridx = 2;
		cons.gridy = 3;
		cons.gridwidth = 1;
		graph.add(unlink,cons);
		cons.insets = new Insets(10, 10, 0, 0);
		
		cons.gridx = 1;
		cons.gridy = 4;
		cons.gridwidth = 2;
		graph.add(undirect,cons);
		
		cons.gridx = 1;
		cons.gridy = 5;
		cons.gridwidth = 2;
		graph.add(direct,cons);
		
		cons.gridx = 1;//NetDone
		cons.gridy = 6;
		graph.add(netDone,cons);
		
		cons.gridx = 1;//Functions
		cons.gridy = 7;
		cons.gridwidth = 2;
		graph.add(f,cons);
		
		cons.gridx = 1;//Functions list
		cons.gridy = 8;
		cons.gridwidth = 2;
		cons.weightx = 0;
		graph.add(functions,cons);
		
		cons.insets = new Insets(5, 0, 0, 0);
		cons.gridx = 1;
		cons.gridy = 9;
		cons.gridwidth = 2;
		cons.weightx = 0;
		graph.add(blank,cons);
		
	}
	private class ButtonAction implements ActionListener {//the listeners for buttons

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource()==link){//link between two nodes
				int label_start = Integer.parseInt(start.getText());
				int label_end = Integer.parseInt(end.getText());
				int w = Integer.parseInt(weight.getText());
				if(w==0){
					try {
						throw new ValidationException("ERROR:<---Weight:Value valid--->\n");
					} catch (ValidationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if(label_start > count || label_end > count|| label_start < 1|| label_end < 1) DrawNetwork.printInfo( "ERROR:<---Beyond Arrange--->\n", "red");
				
				else {
					Link l = new Link(nodes.getNode(label_start).self,nodes.getNode(label_end).self);
					l.label_start = label_start;
					l.label_end = label_end;
					l.weight = w;
					if(direct.isSelected()){
						l.directLink = true;//the signal of direct link to be marked, the default settins is undirect
						nodes.getNode(label_start).outDegreeAdd();
	        			nodes.getNode(label_end).inDegreeAdd();
					}
					else{
						nodes.getNode(label_start).outDegreeAdd();
	        			nodes.getNode(label_end).outDegreeAdd();
	        			nodes.getNode(label_start).inDegreeAdd();
	        			nodes.getNode(label_end).inDegreeAdd();
					}
						links.ls.add(l);
					String str = "A Link Added: StartAgent: Agent " + label_start +", EndAgent: Agent " + label_end +",Weight: " + w + " ;\n";
					DrawNetwork.printInfo(str);
				}
			//	repaint();
				DrawNetwork.hasDiff = true;
			}
			else if(e.getSource() == unlink){//unlink an edge
				boolean flag = false,direct = true;
				int label_start = Integer.parseInt(start.getText());
				int label_end = Integer.parseInt(end.getText());
				if(label_start > count || label_end > count|| label_start < 1|| label_end < 1) DrawNetwork.printInfo("ERROR:<---Beyond Arrange--->\n","red");
				else{
					for(int i = 0;i<links.ls.size();i++){
						Link l = links.ls.get(i);
						if(l.label_start==label_start && l.label_end==label_end){
							direct = l.directLink;
							links.ls.remove(i);
							flag = true;
							break;
						}
					}
					if(!flag) DrawNetwork.printInfo( "Warning:No edge to be removed(No such edge in the net)!\n","blue");
					else {
						if(direct){
							nodes.getNode(label_start).outDegreeDec();
		        			nodes.getNode(label_end).inDegreeDec();
						}
						else{
							nodes.getNode(label_start).outDegreeDec();
		        			nodes.getNode(label_end).outDegreeDec();
		        			nodes.getNode(label_start).inDegreeDec();
		        			nodes.getNode(label_end).inDegreeDec();
						}
						//repaint();
						DrawNetwork.hasDiff = true;
						DrawNetwork.printInfo( "Info:Unlink an edge!\n","blue");
					}
				}
			}
			else if(e.getSource() == netDone){//to make sure that the net is done ,then can generate a net described in the palette
					if(undirect.isSelected()){
						net = new UndirectedNet("MyNet", nodes.ags, links.ls,Net.UNDIRECT_NETWORK);
					}
					else{
						net = new DirectedNet("MyNet", nodes.ags, links.ls,Net.DIRECT_NETWORK);
					}
					DrawNetwork.printInfo("A Net has builded!\n"+net.toString()+"\n");//+net.print()
					//repaint();
					DrawNetwork.hasDiff = true;
			}
			else{
				System.out.println("--------------");
			}
			
		}
		
	}
	
	private class ListMenuAction implements ActionListener{//for the List listeners, mainly for the function part

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == functions){
				if(net!=null){
					String s = "";
				switch(functions.getSelectedIndex()){//print selected matrix type
					case 0:s = "Adjacency Matrix:\n" + net.printMatrix(mf.getMatrix(net, MatrixFactory.ADJ_MATRIX));break;
					case 1:s = "Reachable matrix:\n" + net.printMatrix(mf.getMatrix(net, MatrixFactory.REA_MATRIX));break;
					case 2:s = "Cocitation Matrix:\n" + net.printMatrix(mf.getMatrix(net, MatrixFactory.COCITATION_MATRIX));break;
					case 3:s = "Bibliographic Coupling Matrix:\n" + net.printMatrix(mf.getMatrix(net, MatrixFactory.COUPLE_MATRIX));break;
					case 4:
						if(net.type == Net.UNDIRECT_NETWORK){
							s = "Graph Laplacian Matrix:\n" + net.printMatrix(mf.getMatrix(net, MatrixFactory.LAPLACIAN_MATRIX));break;
						}
						else s = "Error:Not Undirected Network!\n";

				}
				//repaint();
				DrawNetwork.hasDiff = true;
				DrawNetwork.printInfo(s,"blue");
			}
			}
		}
		
	}
	private class MouseAction implements MouseListener,MouseMotionListener{
		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
		
			}
		@Override
		public void mousePressed(MouseEvent e) {//add a node or select a node that will be dragged
			// TODO Auto-generated method stub
			int x = e.getX();
			int y = e.getY()+W_Y;
			
			if(nodes.getNode(x, y)==null){
				count++;
				String str = "Agent " + count +" added: x = " + x +",y = " +y + " ;\n";
				DrawNetwork.printInfo(str);
				nodes.ags.add(new Node(x,y,count));
			}
			else{
				Node n = nodes.getNode(x, y);
				n.self_color = Color.orange;
			}
			//repaint();
			DrawNetwork.hasDiff = true;
		}
		@Override
		public void mouseReleased(MouseEvent e) {//add completed or have the node to a better position
			// TODO Auto-generated method stub
			int x = e.getX();
			int y = e.getY()+W_Y;
			if(nodes.getNode(x, y)!=null){
				nodes.getNode(x, y).self_color = nodes.getNode(x, y).default_Color;
			}
			//repaint();
			DrawNetwork.hasDiff = true;
		}
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
			
		}
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mouseDragged(MouseEvent e) {//dragged a node to a right position,update the node's position
			// TODO Auto-generated method stub
			int x = e.getX();
			int y = e.getY()+W_Y;
			// TODO Auto-generated method stub
			if(nodes.getNode(x, y)!=null){
				Node n = nodes.getNode(x, y);
				n.self.x = x;
				n.self.y = y;
				if(x - n.size/2 <0) n.self.x = n.size/2;
				if(x+n.size/2 > palette.getWidth()) n.self.x = palette.getWidth() - n.size/2;
				if(y - n.size/2 - W_Y <0) n.self.y = n.size/2 + W_Y;
				if(y + n.size/2 > palette.getHeight()+W_Y) n.self.y = palette.getHeight()+ W_Y - n.size/2;
				//repaint();
				DrawNetwork.hasDiff = true;
			}
			
		}
		@Override
		public void mouseMoved(MouseEvent e) {
		
		}
		
	}

}
