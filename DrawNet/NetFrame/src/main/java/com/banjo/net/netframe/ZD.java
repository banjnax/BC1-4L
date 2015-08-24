package com.banjo.net.netframe;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.DatasetChangeEvent;
import org.jfree.data.general.DatasetChangeListener;
import org.jfree.data.xy.DefaultXYDataset;

import Jama.Matrix;

public class ZD extends JFrame implements DatasetChangeListener{

	private static final long serialVersionUID = 1L;
	public boolean op_Ready = false;
	public boolean my_Ready = false;
	public ButtonAction ba = new ButtonAction();
	
	public JPanel zdPanel = new JPanel();
	public JPanel jchartp = new JPanel();
	
	public DefaultXYDataset dataSet = new DefaultXYDataset();
	public double[][] data = new double[2][1000];
	public JFreeChart chart =  ChartFactory.createScatterPlot("Payoffs",
            "Oponent", "Yourself",dataSet, PlotOrientation.VERTICAL, 
            false, false, false);//ChartFactory.createScatterPlot("Payoffs",
            //"Oponent", "Yourself", DataSet, PlotOrientation.VERTICAL, 
         //   false, false, false);
	XYPlot xyplot = chart.getXYPlot();
	ValueAxis valueaxis = xyplot.getRangeAxis(); 
	ValueAxis domainaxis = xyplot.getDomainAxis();
	public ChartPanel chartp = new ChartPanel(chart);
	public boolean adapt = false;
	
	public int opS = 0;//1,2,3->pin,ext,self
	public double myScore = 0;
	public double opScore = 0;
	public int count = 1;
	public double beta = -0.1;
	public double lambda = 0.2;
	public double chi = 2.5;
	public double[] Sy =new double[]{3,5,0,1}; 
	public double[] Sx = new double[]{3,0,5,1};
	public double[] p = new double[4];
	public double[] mp = new double[4];
	public boolean mpFlag = false;
	public int state = 2;//0==cc;1=cd;2=dc;3=dd
	public int myState = 1;
	
	public boolean mySelf = false;
	
	JLabel opponent = new JLabel("Opponent:");
	JRadioButton s_Pin = new JRadioButton("Pin Strategy");
	JRadioButton s_Extort = new JRadioButton("Extortion Strategy");
	ButtonGroup strategy = new ButtonGroup();
	JLabel p1 = new JLabel("p1:");
	JTextField t_P1 = new JTextField(3);
	JLabel p2 = new JLabel("p2:");
	JTextField t_P2 = new JTextField(3);
	JLabel p3 = new JLabel("p3:");
	JTextField t_P3 = new JTextField(3);
	JLabel p4 = new JLabel("p4:");
	JTextField t_P4 = new JTextField(3);
	JButton o_Set = new JButton("Set");
	
	JLabel score = new JLabel("Score");
	JTextField o_S = new JTextField(4);
	JLabel vs = new JLabel("Vs");
	JTextField my_S = new JTextField(4);
	
	JLabel h = new JLabel("Record:");
	static JTextPane his = new JTextPane();
	JScrollPane jcp = new JScrollPane(his);
	
	JLabel self = new JLabel("Myself:");
	
	JRadioButton s_Coop = new JRadioButton("Cooperate");
	JRadioButton s_Def = new JRadioButton("Defect");
	ButtonGroup my_Strategy = new ButtonGroup();
	
	JLabel my_p1 = new JLabel("p1:");
	JTextField my_t_P1 = new JTextField(3);
	JLabel my_p2 = new JLabel("p2:");
	JTextField my_t_P2 = new JTextField(3);
	JLabel my_p3 = new JLabel("p3:");
	JTextField my_t_P3 = new JTextField(3);
	JLabel my_p4 = new JLabel("p4:");
	JTextField my_t_P4 = new JTextField(3);
	JButton play = new JButton("Play");
	JButton adaptPlay = new JButton("Adapt");
	public ZD(){
		launch();
	}
	public void launch(){

		zdPanel.setLayout(new GridBagLayout());
		zdPanel.setBackground(Color.white);
		chart.setBackgroundPaint(Color.white);
		chartp.setBackground(Color.white);
		chart.getXYPlot().setBackgroundPaint(Color.WHITE);
		valueaxis .setLowerBound(1);
		valueaxis .setUpperBound(5);
		
		domainaxis .setLowerBound(1);
		domainaxis .setUpperBound(5);
		
//		data[0][0] = 0.0;
//		data[1][0] = 0.0;
//		data[0][1] = 5.0;
//		data[1][1] = 5.0;
		dataSet.addSeries("test", data);
		dataSet.addChangeListener(this);
		
		opponent.setHorizontalAlignment(JLabel.CENTER);
		opponent.setBackground(Color.YELLOW);
		self.setHorizontalAlignment(JLabel.CENTER);
		self.setBackground(Color.GREEN);
		
		
		s_Pin.setHorizontalAlignment(JRadioButton.LEFT);
		s_Extort.setHorizontalAlignment(JRadioButton.LEFT);
		strategy.add(s_Pin);
		strategy.add(s_Extort);
		s_Coop.setHorizontalAlignment(JRadioButton.LEFT);
		s_Def.setHorizontalAlignment(JRadioButton.LEFT);
		my_Strategy.add(s_Coop);
		my_Strategy.add(s_Def);
		
		p1.setHorizontalAlignment(JLabel.LEFT);
		p2.setHorizontalAlignment(JLabel.LEFT);
		p3.setHorizontalAlignment(JLabel.LEFT);
		p4.setHorizontalAlignment(JLabel.LEFT);
		t_P1.setHorizontalAlignment(JTextField.CENTER);
		t_P2.setHorizontalAlignment(JTextField.CENTER);
		t_P3.setHorizontalAlignment(JTextField.CENTER);
		t_P4.setHorizontalAlignment(JTextField.CENTER);
		
		my_p1.setHorizontalAlignment(JLabel.LEFT);
		my_p2.setHorizontalAlignment(JLabel.LEFT);
		my_p3.setHorizontalAlignment(JLabel.LEFT);
		my_p4.setHorizontalAlignment(JLabel.LEFT);
		my_t_P1.setHorizontalAlignment(JTextField.CENTER);
		my_t_P2.setHorizontalAlignment(JTextField.CENTER);
		my_t_P3.setHorizontalAlignment(JTextField.CENTER);
		my_t_P4.setHorizontalAlignment(JTextField.CENTER);
		
		
		score.setHorizontalAlignment(JLabel.CENTER);
		vs.setHorizontalAlignment(JLabel.CENTER);
		o_S.setHorizontalAlignment(JLabel.CENTER);
		my_S.setHorizontalAlignment(JLabel.CENTER);
		//define some style about the history content
        Style def = his.getStyledDocument().addStyle(null, null);//this style define a normal style
        StyleConstants.setFontFamily(def, "verdana");
        StyleConstants.setFontSize(def, 10);
        Style normal = his.addStyle("normal", def);//name "def" to be normal
        
        //Error
        Style s1 = his.addStyle("red", normal);//"red" style based on the "normal" style ,add color attribute to the "red" style
        StyleConstants.setForeground(s1, Color.RED);
       
        //normal
        Style s2 = his.addStyle("blue", normal);
        StyleConstants.setForeground(s2, Color.BLUE);
        
		h.setHorizontalAlignment(JLabel.LEFT);
        his.setParagraphAttributes(normal, true);
		jcp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		jchartp.add(chartp);
		//jchartp.setPreferredSize(new Dimension(530,300));
		
		setComponent();
		
		printInfo("Typical IDP:\nRSTP:(3,0,5,1)\n","blue");
		printInfo("Score:0 Vs 0\n");
		o_S.setText("0");
		my_S.setText("0");

		o_Set.addActionListener(ba);
		play.addActionListener(ba);
		adaptPlay.addActionListener(ba);
		
		//new Thread(new ChartChnage()).start();
	}
	public void setComponent(){
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;//for the extra space 
		
		cons.weighty = 1;
		cons.weightx = 1;
		
		cons.gridx = 0;
		cons.gridy = 0;
		cons.gridwidth = 11;
		zdPanel.add(jchartp, cons);
		
		cons.weighty = 0.02;
		cons.weightx = 0.8;
		
		cons.gridx = 0;
		cons.gridy = 1;
		cons.gridwidth = 4;
		zdPanel.add(opponent,cons);
		
		cons.gridx = 0;
		cons.gridy = 2;
		zdPanel.add(s_Pin,cons);
		
		cons.gridx = 0;
		cons.gridy = 3;
		zdPanel.add(s_Extort,cons);
		
		cons.gridwidth = 1;
		
		cons.gridx = 0;
		cons.gridy = 4;
		zdPanel.add(p1,cons);
		
		cons.gridx = 1;
		cons.gridy = 4;
		zdPanel.add(t_P1,cons);
		
		cons.gridx = 2;
		cons.gridy = 4;
		zdPanel.add(p2,cons);
		
		cons.gridx = 3;
		cons.gridy = 4;
		zdPanel.add(t_P2,cons);
		
		cons.gridx = 0;
		cons.gridy = 5;
		zdPanel.add(p3,cons);
		
		cons.gridx = 1;
		cons.gridy = 5;
		zdPanel.add(t_P3,cons);
		
		cons.gridx = 2;
		cons.gridy = 5;
		zdPanel.add(p4,cons);
		
		cons.gridx = 3;
		cons.gridy = 5;
		zdPanel.add(t_P4,cons);
		
		cons.gridx = 0;
		cons.gridy = 6;
		cons.gridwidth = 4;
		zdPanel.add(o_Set,cons);
		
		cons.weightx = 1;
		cons.gridx = 4;
		cons.gridy = 1;
		cons.gridwidth = 3;
		zdPanel.add(score, cons);
		
		cons.gridx = 4;
		cons.gridy = 2;
		cons.gridwidth = 1;
		zdPanel.add(o_S, cons);
		
		cons.gridx = 5;
		cons.gridy = 2;
		zdPanel.add(vs, cons);
		
		cons.gridx = 6;
		cons.gridy = 2;
		zdPanel.add(my_S, cons);
		
		cons.gridwidth = 3;
		
		cons.gridx = 4;
		cons.gridy = 3;
		zdPanel.add(h, cons);
		
		cons.gridx = 4;
		cons.gridy = 4;
		cons.gridheight = 3;
		zdPanel.add(jcp, cons);
		
		cons.weighty = 0;
		cons.gridx = 7;
		cons.gridy = 1;
		cons.gridheight = 1;
		cons.gridwidth = 4;
		zdPanel.add(self, cons);
		
		cons.gridx = 7;
		cons.gridy = 2;
		zdPanel.add(s_Coop, cons);
		
		cons.gridx = 7;
		cons.gridy = 3;
		zdPanel.add(s_Def, cons);
		
		cons.gridwidth =1;
		
		cons.gridx = 7;
		cons.gridy = 4;
		zdPanel.add(my_p1, cons);
		
		cons.gridx = 8;
		cons.gridy = 4;
		zdPanel.add(my_t_P1, cons);
		
		cons.gridx = 9;
		cons.gridy = 4;
		zdPanel.add(my_p2, cons);
		
		cons.gridx = 10;
		cons.gridy = 4;
		zdPanel.add(my_t_P2, cons);
		
		cons.gridx = 7;
		cons.gridy = 5;
		zdPanel.add(my_p3, cons);
		
		cons.gridx = 8;
		cons.gridy = 5;
		zdPanel.add(my_t_P3, cons);
		
		cons.gridx = 9;
		cons.gridy = 5;
		zdPanel.add(my_p4, cons);
		
		cons.gridx = 10;
		cons.gridy = 5;
		zdPanel.add(my_t_P4, cons);
		
		cons.gridwidth = 2;
		
		cons.gridx = 7;
		cons.gridy = 6;
		zdPanel.add(play, cons);
		
		cons.gridx = 9;
		cons.gridy = 6;
		zdPanel.add(adaptPlay, cons);
	}
	public static void printInfo(String info,String type){
		try {
			his.getDocument().insertString(his.getDocument().getLength(),
			         info, his.getStyle(type));//get the history handle and add content to it
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void printInfo(String info){//default "normal" style
		try {
			his.getDocument().insertString(his.getDocument().getLength(),
			         info, his.getStyle("normal"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void getPinP(){
		
		for(int i=0;i<4;i++) p[i] = Sy[i]*beta+lambda;
		
		p[0]+=1;
		p[1]+=1;
	}
	public void getExtP(){
		double x1 = 1.0/((chi-1)*(Sy[0]-Sy[3]));
		double x2 = 1.0/(Sy[3]-Sy[2]+chi*(Sy[1]-Sy[3]));
		double x3 = 1.0/(chi*(Sy[3]-Sy[2])+Sy[1]-Sy[3]);
		double mini = (x1<x2?x1:x2)<x3?(x1<x2?x1:x2):x3;
		double fai = mini/2;
		
		p[0] = 1-fai*((chi-1)*(Sy[0]-Sy[3]));
		p[1] = 1-fai*(Sy[3]-Sy[2]+chi*(Sy[1]-Sy[3]));
		p[2] = fai*(chi*(Sy[3]-Sy[2])+Sy[1]-Sy[3]);
		p[3] = 0;
		
	}
	public void getSelf(){
		p[0] = 	Double.parseDouble(t_P1.getText());
		p[1] = 	Double.parseDouble(t_P2.getText());
		p[2] = 	Double.parseDouble(t_P3.getText());
		p[3] = 	Double.parseDouble(t_P4.getText());
	}
	private class ButtonAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == o_Set){

				if(s_Pin.isSelected()) {
					opS = 1;
					printInfo("Pin Strategy is selected!\n","blue");
					printInfo("Init State:C Vs C!\nPin:"+(-1*lambda/beta)+"\n","blue");
				}
				else if(s_Extort.isSelected()) {
					opS =2;
					printInfo("Extortion Strategy is selected!\n","blue");
					printInfo("Init State:C Vs C!\nExtortion:"+chi+"\n","blue");
				}
				else{
					opS =3;
					printInfo("Self Strategy is selected!\n","blue");
				}
			}
			else if(e.getSource() == play){
				switch(opS){
					case 1:getPinP();playGame();break;
					case 2:getExtP();playGame();break;
					case 3:getSelf();playGame();break;
				}
			}
			else if(e.getSource() == adaptPlay){
				//getExtP();
				p[0]=1;
				p[1]=0;
				p[2]=0;
				p[3]=1;
				playAdapt();
			}
			else{
				
			}
			
		}
		
	}
	public void playAdapt(){//p,q
		
		double s_Y,s_X,step=0.01,part1,part2,part3,part4;
		Random r = new Random();
		s_Coop.setEnabled(false);
		s_Def.setEnabled(false);
		my_t_P1.setEnabled(false);
		my_t_P2.setEnabled(false);
		my_t_P3.setEnabled(false);
		my_t_P4.setEnabled(false);
		play.setEnabled(false);
		
		mp[0] = r.nextDouble();
		mp[1] = r.nextDouble();
		mp[2] = r.nextDouble();
		mp[3] = r.nextDouble();
		count = 0;
		clearData();
		adapt = true;
		for(int i=0;i<900;i++){
			s_Y = getD(Sy,mp)/getD(new double[]{1,1,1,1},mp);
			s_X = getD(Sx,mp)/getD(new double[]{1,1,1,1},mp);
			//printInfo("EY: "+s_Y+",EX: "+s_X+".\n", "blue");
			part1 = (6-6*mp[1])/(-2+2*mp[1]-mp[3]-mp[2]+2*mp[0]-2*mp[0]*mp[1]+mp[0]*mp[2]+mp[3]*mp[1])-(-6+6*mp[1]+6*mp[0]-6*mp[0]*mp[1]-3*mp[3]+3*mp[3]*mp[1])/Math.pow((-2+2*mp[1]-mp[3]-mp[2]+2*mp[0]-2*mp[0]*mp[1]+mp[0]*mp[2]+mp[3]*mp[1]),2)*(2-2*mp[1]+mp[2]);
			part2 = (6-6*mp[0]+3*mp[3])/(-2+2*mp[1]-mp[3]-mp[2]+2*mp[0]-2*mp[0]*mp[1]+mp[0]*mp[2]+mp[3]*mp[1])-(-6+6*mp[1]+6*mp[0]-6*mp[0]*mp[1]-3*mp[3]+3*mp[3]*mp[1])/Math.pow((-2+2*mp[1]-mp[3]-mp[2]+2*mp[0]-2*mp[0]*mp[1]+mp[0]*mp[2]+mp[3]*mp[1]),2)*(2-2*mp[0]+mp[3]);
			part3 = -(-6+6*mp[1]+6*mp[0]-6*mp[0]*mp[1]-3*mp[3]+3*mp[3]*mp[1])/Math.pow((-2+2*mp[1]-mp[3]-mp[2]+2*mp[0]-2*mp[0]*mp[1]+mp[0]*mp[2]+mp[3]*mp[1]),2)*(-1+mp[0]);
			part4 = (-3+3*mp[1])/(-2+2*mp[1]-mp[3]-mp[2]+2*mp[0]-2*mp[0]*mp[1]+mp[0]*mp[2]+mp[3]*mp[1])-(-6+6*mp[1]+6*mp[0]-6*mp[0]*mp[1]-3*mp[3]+3*mp[3]*mp[1])/Math.pow((-2+2*mp[1]-mp[3]-mp[2]+2*mp[0]-2*mp[0]*mp[1]+mp[0]*mp[2]+mp[3]*mp[1]),2)*(-1+mp[1]);

			if(part1>0 && mp[0]+step*part1<1 && mp[0]+step*part1>0)mp[0]+=step*part1;
			if(part2>0 && mp[1]+step*part1<1 && mp[1]+step*part1>0)mp[1]+=step*part2;
			if(part3>0 && mp[2]+step*part1<1 && mp[2]+step*part1>0)mp[2]+=step*part3;
			if(part4>0 && mp[3]+step*part1<1 && mp[3]+step*part1>0)mp[3]+=step*part4;
			
			data[0][count] = s_Y;
			data[1][count] = s_X;
			count++;
		}
		chart.fireChartChanged();
		s_Coop.setEnabled(true);
		s_Def.setEnabled(true);
		my_t_P1.setEnabled(true);
		my_t_P2.setEnabled(true);
		my_t_P3.setEnabled(true);
		my_t_P4.setEnabled(true);
		play.setEnabled(true);
//		//clearData();
//		data[0][0] =0;
//		data[1][0] =0;
//		data[0][1] =5;
//		data[1][1] =5;
//		count = 1;
	}
	public void clearData(){
		for(int i=0;i<1000;i++) {
			data[0][i] = 0;
			data[1][i] = 0;
		}
	}
	public double getD(double[] f,double[] q){
		double[][] temD = new double[4][4];
		temD[0][0] = -1+p[0]*q[0]; temD[0][1] = -1+p[0]; temD[0][2] = -1+q[0]; temD[0][3] = f[0];
		temD[1][0] = p[1]*q[2]; temD[1][1] = -1+p[1]; temD[1][2] = q[2]; temD[1][3] = f[1];
		temD[2][0] = p[2]*q[1]; temD[2][1] = p[2]; temD[2][2] = -1+q[1]; temD[2][3] = f[2];
		temD[3][0] = p[3]*q[3]; temD[3][1] = p[3]; temD[3][2] = q[3]; temD[3][3] = f[3];
		Matrix d = new Matrix(temD);
		return d.det();
	}
	public void playGame(){//RSTP:3,0,5,1,pin:2,beta=-0.1,lambda=0.2;
		
		if(!s_Def.isSelected()&&!s_Coop.isSelected()&&!mpFlag){
			mp[0] =  Double.parseDouble(my_t_P1.getText());
			mp[1] =  Double.parseDouble(my_t_P2.getText());
			mp[2] =  Double.parseDouble(my_t_P3.getText());
			mp[3] =  Double.parseDouble(my_t_P4.getText());
			mpFlag = true;
		}
		if(mpFlag){
			int myOption = 0;//0=coor
			int opOption = 0;//0=coor
			Random r = new Random();
			if(r.nextDouble()>p[state]) opOption = 1;
			if(r.nextDouble()>mp[myState]) myOption = 1;
			
			if(myOption==0&&opOption==0){//00
				state = 0;
				myState = 0;
				myScore+=Sy[myState];
				opScore+=Sy[state];
				printInfo("Round "+ count +" : C Vs C!\n");
				my_S.setText(String.format("%.2f", myScore/count));
				o_S.setText(String.format("%.2f", opScore/count));
			}
			else if(myOption==0&&opOption==1){//cd
				state = 2;//dc for op
				myState = 1;
				myScore+=Sy[state];
				opScore+=Sy[myState];
				printInfo("Round "+ count +" : D Vs C!\n");
				my_S.setText(String.format("%.2f", myScore/count));
				o_S.setText(String.format("%.2f", opScore/count));
			}
			else if(myOption==1&&opOption==0){
				state = 1;//dc for op
				myState = 2;
				myScore+=Sy[state];
				opScore+=Sy[myState];
				printInfo("Round "+ count +" : C Vs D!\n");
				my_S.setText(String.format("%.2f", myScore/count));
				o_S.setText(String.format("%.2f", opScore/count));
			}
			else{
				state = 3;//dc for op
				myState = 3;
				myScore+=Sy[state];
				opScore+=Sy[myState];
				printInfo("Round "+ count +" : D Vs D!\n");
				my_S.setText(String.format("%.2f", myScore/count));
				o_S.setText(String.format("%.2f", opScore/count));
			}
			
		}
		else{
			
			int myOption = 0;//0=coor
			int opOption = 0;//0=coor
			Random r = new Random();
			if(s_Def.isSelected()) myOption = 1;//1=def
			if(r.nextDouble()>p[state])  opOption = 1;//1=def
			
			if(myOption==0&&opOption==0){//00
				state = 0;
				myScore+=Sy[state];
				opScore+=Sy[0];
				printInfo("Round "+ count +" : C Vs C!\n");
				my_S.setText(String.format("%.2f", myScore/count));
				o_S.setText(String.format("%.2f", opScore/count));
			}
			else if(myOption==0&&opOption==1){//cd
				state = 2;//dc for op
				myScore+=Sy[state];
				opScore+=Sy[1];
				printInfo("Round "+ count +" : D Vs C!\n");
				my_S.setText(String.format("%.2f", myScore/count));
				o_S.setText(String.format("%.2f", opScore/count));
			}
			else if(myOption==1&&opOption==0){
				state = 1;//dc for op
				myScore+=Sy[state];
				opScore+=Sy[2];
				printInfo("Round "+ count +" : C Vs D!\n");
				my_S.setText(String.format("%.2f", myScore/count));
				o_S.setText(String.format("%.2f", opScore/count));
			}
			else{
				state = 3;//dc for op
				myScore+=Sy[state];
				opScore+=Sy[3];
				printInfo("Round "+ count +" : D Vs D!\n");
				my_S.setText(String.format("%.2f", myScore/count));
				o_S.setText(String.format("%.2f", opScore/count));
			}
		}
		data[0][count+1] = opScore/count;
		data[1][count+1] = myScore/count;
		count++;
		chart.fireChartChanged();
	}
	@Override
	public void datasetChanged(DatasetChangeEvent arg0) {
		// TODO Auto-generated method stub
		//get min max in each coor
		double x_min=data[0][0],x_max=data[0][0],y_min=data[1][0],y_max=data[1][0];
		for(int i=0;i<900;i++){
			if(x_min>data[0][i]) x_min = data[0][i];
			if(x_max<data[0][i]) x_max = data[0][i];
			
			if(y_min>data[1][i]) y_min = data[1][i];
			if(y_max<data[1][i])y_max = data[1][i];
		}
		if(!adapt){
			dataSet.addSeries("test", data);
		}
		else{
			double [][] sx = new double[2][900];
			double [][] sy = new double[2][900];
			for(int i=0;i<900;i++){
				sx[0][i] = i;
				sx[1][i] = data[0][i];
				
				sy[0][i] = i;
				sy[1][i] = data[1][i];
			}
			dataSet.addSeries("test", sx);
			dataSet.addSeries("sy", sy);
		}
		adapt = false;
		valueaxis .setLowerBound(y_min);
		valueaxis .setUpperBound(y_max);
		
		domainaxis .setLowerBound(x_min);
		domainaxis .setUpperBound(x_max);
//		
	}
}
