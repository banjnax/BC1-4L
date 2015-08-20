package com.banjo.zd;

import java.awt.Color;
import java.awt.Dimension;
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
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ZD extends JFrame{

	private static final long serialVersionUID = 1L;
	private static int G_WIDTH = 500;
	private static int G_HEIGHT = 500;
	private static int G_X = 400;
	private static int G_Y = 100;
	public boolean op_Ready = false;
	public boolean my_Ready = false;
	public ButtonAction ba = new ButtonAction();
	
	public JPanel jchartp = new JPanel();
	public DefaultCategoryDataset DataSet = new DefaultCategoryDataset();
	public JFreeChart chart =  ChartFactory.createLineChart("Payoffs",
            "rounds", "Payoff", DataSet, PlotOrientation.VERTICAL, 
            true, false, false);
	public ChartPanel chartp = new ChartPanel(chart);
	
	public int opS = 0;//1,2,3->pin,ext,self
	public double myScore = 0;
	public double opScore = 0;
	public int count = 1;
	public double beta = -0.1;
	public double lambda = 0.2;
	public double chi = 2.5;
	public int[] Sy =new int[]{3,5,0,1}; 
	public int[] Sx = new int[]{3,0,5,1};
	public double[] p = new double[4];
	public int state = 0;//0==cc;1=cd;2=dc;3=dd
	
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
	
	JLabel h = new JLabel("History:");
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ZD().launch();
	}
	public void launch(){
		this.setSize(G_WIDTH,G_HEIGHT);
		this.setLocation(G_X,G_Y);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());
		this.setVisible(true);
		this.setResizable(false);
		this.setBackground(Color.WHITE);
		
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
		
		//chart.getCategoryPlot().setBackgroundPaint(Color.WHITE);
		chart.getCategoryPlot().
		jchartp.add(chartp);
		jchartp.setPreferredSize(new Dimension(530,300));
		
		setComponent();
		
		printInfo("Typical IDP:\nRSTP:(3,0,5,1)\n","blue");
		printInfo("Score:0 Vs 0\n");
		o_S.setText("0");
		my_S.setText("0");
		
		o_Set.addActionListener(ba);
		play.addActionListener(ba);
	}
	public void setComponent(){
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;//for the extra space 
		
		cons.weighty = 1;
		cons.weightx = 1;
		
		cons.gridx = 0;
		cons.gridy = 0;
		cons.gridwidth = 11;
		this.add(jchartp, cons);
		
		cons.weighty = 0.02;
		cons.weightx = 0.6;
		
		cons.gridx = 0;
		cons.gridy = 1;
		cons.gridwidth = 4;
		this.add(opponent,cons);
		
		cons.gridx = 0;
		cons.gridy = 2;
		this.add(s_Pin,cons);
		
		cons.gridx = 0;
		cons.gridy = 3;
		this.add(s_Extort,cons);
		
		cons.gridwidth = 1;
		
		cons.gridx = 0;
		cons.gridy = 4;
		this.add(p1,cons);
		
		cons.gridx = 1;
		cons.gridy = 4;
		this.add(t_P1,cons);
		
		cons.gridx = 2;
		cons.gridy = 4;
		this.add(p2,cons);
		
		cons.gridx = 3;
		cons.gridy = 4;
		this.add(t_P2,cons);
		
		cons.gridx = 0;
		cons.gridy = 5;
		this.add(p3,cons);
		
		cons.gridx = 1;
		cons.gridy = 5;
		this.add(t_P3,cons);
		
		cons.gridx = 2;
		cons.gridy = 5;
		this.add(p4,cons);
		
		cons.gridx = 3;
		cons.gridy = 5;
		this.add(t_P4,cons);
		
		cons.gridx = 0;
		cons.gridy = 6;
		cons.gridwidth = 4;
		this.add(o_Set,cons);
		
		cons.weightx = 1;
		cons.gridx = 4;
		cons.gridy = 1;
		cons.gridwidth = 3;
		this.add(score, cons);
		
		cons.gridx = 4;
		cons.gridy = 2;
		cons.gridwidth = 1;
		this.add(o_S, cons);
		
		cons.gridx = 5;
		cons.gridy = 2;
		this.add(vs, cons);
		
		cons.gridx = 6;
		cons.gridy = 2;
		this.add(my_S, cons);
		
		cons.gridwidth = 3;
		
		cons.gridx = 4;
		cons.gridy = 3;
		this.add(h, cons);
		
		cons.gridx = 4;
		cons.gridy = 4;
		cons.gridheight = 3;
		this.add(jcp, cons);
		
		cons.weighty = 0;
		cons.gridx = 7;
		cons.gridy = 1;
		cons.gridheight = 1;
		cons.gridwidth = 4;
		this.add(self, cons);
		
		cons.gridx = 7;
		cons.gridy = 2;
		this.add(s_Coop, cons);
		
		cons.gridx = 7;
		cons.gridy = 3;
		this.add(s_Def, cons);
		
		cons.gridwidth =1;
		
		cons.gridx = 7;
		cons.gridy = 4;
		this.add(my_p1, cons);
		
		cons.gridx = 8;
		cons.gridy = 4;
		this.add(my_t_P1, cons);
		
		cons.gridx = 9;
		cons.gridy = 4;
		this.add(my_p2, cons);
		
		cons.gridx = 10;
		cons.gridy = 4;
		this.add(my_t_P2, cons);
		
		cons.gridx = 7;
		cons.gridy = 5;
		this.add(my_p3, cons);
		
		cons.gridx = 8;
		cons.gridy = 5;
		this.add(my_t_P3, cons);
		
		cons.gridx = 9;
		cons.gridy = 5;
		this.add(my_p4, cons);
		
		cons.gridx = 10;
		cons.gridy = 5;
		this.add(my_t_P4, cons);
		
		cons.gridwidth = 4;
		
		cons.gridx = 7;
		cons.gridy = 6;
		this.add(play, cons);
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
	}
	private class ButtonAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == o_Set){
				for(int i=0;i<10;i++)
				DataSet.addValue(4+i, "number", i+"");
				repaint();
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
			if(e.getSource() == play){
				switch(opS){
				case 1:getPinP();playGame();break;
				case 2:getExtP();playGame();break;
				case 3:getSelf();playGame();break;
				}
			}
		}
		
	}
	public void playGame(){//RSTP:3,0,5,1,pin:2,beta=-0.1,lambda=0.2;
		int myOption = 0;//0=coor
		int opOption = 0;//0=coor
		Random r = new Random();
		if(s_Def.isSelected()) myOption = 1;//1=def
		if(r.nextFloat()>p[state])  opOption = 1;//1=def
		
		if(myOption==0&&opOption==0){//00
			state = 0;
			myScore+=Sy[state];
			opScore+=Sy[0];
			printInfo("Round "+ count +" : C Vs C!\n");
			my_S.setText(String.format("%.2f", myScore/count));
			o_S.setText(String.format("%.2f", opScore/count));
			count++;
		}
		else if(myOption==0&&opOption==1){//cd
			state = 2;//dc for op
			myScore+=Sy[state];
			opScore+=Sy[1];
			printInfo("Round "+ count +" : D Vs C!\n");
			my_S.setText(String.format("%.2f", myScore/count));
			o_S.setText(String.format("%.2f", opScore/count));
			count++;
		}
		else if(myOption==1&&opOption==0){
			state = 1;//dc for op
			myScore+=Sy[state];
			opScore+=Sy[2];
			printInfo("Round "+ count +" : C Vs D!\n");
			my_S.setText(String.format("%.2f", myScore/count));
			o_S.setText(String.format("%.2f", opScore/count));
			count++;
		}
		else{
			state = 3;//dc for op
			myScore+=Sy[state];
			opScore+=Sy[3];
			printInfo("Round "+ count +" : D Vs D!\n");
			my_S.setText(String.format("%.2f", myScore/count));
			o_S.setText(String.format("%.2f", opScore/count));
			count++;
		}
	}
}
