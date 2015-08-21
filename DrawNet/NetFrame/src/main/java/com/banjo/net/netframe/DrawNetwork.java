package com.banjo.net.netframe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.banjo.net.basemodules.Link;
import com.banjo.net.basemodules.Net;
import com.banjo.net.basemodules.Node;

public class DrawNetwork extends JFrame{
/**
 * @author banjo
 */
	private static final long serialVersionUID = 1L;
	private static int G_WIDTH = 750;
	private static int G_HEIGHT = 800;
	private static int G_X = 500;
	private static int G_Y = 100;
	public static Logger logger = Logger.getLogger(DrawNetwork.class.getName());
	public static boolean hasDiff = false;
	Image vImage=null;
	Image offScreenImage = null;
	Graphics gImage = null;
	
	JMenuBar menuBar = new JMenuBar();
	JMenu file = new JMenu("File");
	JMenu edit = new JMenu("Edit");
	JMenu extra = new JMenu("Extra");
	
	JMenuItem _new = new JMenuItem("New Net");
	JMenu Open = new JMenu("Open..");
	JMenuItem _loadNet = new JMenuItem("Load Net");
	JMenuItem _loadData = new JMenuItem("Load Data..");
	JMenuItem _loadXml = new JMenuItem("Load XML");
	JMenu Save = new JMenu("Save as ..");
	JMenuItem _saveNet = new JMenuItem("Net Object");
	JMenuItem _saveData = new JMenuItem("Net Data..");
	JMenuItem _saveXml = new JMenuItem("XML File..");
	JMenuItem _clear = new JMenuItem("Clear Palette");
	JMenuItem _bc14ls = new JMenuItem("BC14L Spider");
	JMenuItem _findWebsit = new JMenuItem("Find Node");
	JTabbedPane jtp = new JTabbedPane(JTabbedPane.TOP);//to clear the different faces to the users for different functions presentation
	
	JPanel hisPart = new JPanel();
	JLabel h = new JLabel("History:");
	static JTextPane his = new JTextPane();
	JScrollPane jcp = new JScrollPane(his);
	JButton clear = new JButton("Clear History");
	
	FileDialog openFileDialog = new FileDialog(this,"Open File",FileDialog.LOAD);
    FileDialog saveFileDialog = new FileDialog(this,"Save File As",FileDialog.SAVE);
    MenuAction ma = new MenuAction();
    int delay = 100;
    Timer timer = new Timer(delay, new TimerAction());//repaint the frame by a timer
    
    DrawGraph drawGraph = new DrawGraph();//have the graph handle
    DrawChart drawChart = new DrawChart(drawGraph);
    ZD zd = new ZD();
    
    static BC14LS bcls = null;
    int paintFlag = 0;//the selected tap
    Image logo = new ImageIcon("E:\\complex.png").getImage();
    int paletteWidth;
    int paletteHeight;
    boolean extraFlag = false;
	public static void main(String[] args) {
		new DrawNetwork().launch();
	}
	public void launch(){
		//config log4j
		PropertyConfigurator.configure ("bc14log.properties");
		
		//outline of the frame
		this.setSize(G_WIDTH,G_HEIGHT);
		this.setLocation(G_X,G_Y);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
		//this.setResizable(false);
		this.setResizable(true);
		
		this.setLayout(new BorderLayout());//new GridBagLayout()
		this.setMinimumSize(new Dimension(G_WIDTH,G_HEIGHT));
		this.setTitle("NetWork");
		this.setIconImage(logo);
		this.pack();
		this.addComponentListener(new ResizeListener());
		
		setComponent();//we should add the component first, this can mend the pro of resize the frame

		jtp.addChangeListener(new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane j = (JTabbedPane)e.getSource();
				if(j.getSelectedIndex() == 0){
					paintFlag = 0;
					repaint();
				}
				else paintFlag = 1;
			}
		});
		
		timer.start();
		bcls = new BC14LS(this);// "http://www.labcomplex.org/"
		new Thread(new Crawl()).run();
	}
	public void setComponent(){
		
		//menu part
		file.add(_new);
		file.add(Open);
		Open.add(_loadNet);
		Open.add(_loadData);
		Open.add(_loadXml);
		file.add(Save);
		Save.add(_saveNet);
		Save.add(_saveData);
		Save.add(_saveXml);
		
		edit.add(_clear);

		extra.add(_bc14ls);
		extra.add(_findWebsit);

		_new.addActionListener(ma);
		_saveNet.addActionListener(ma);
		_saveData.addActionListener(ma);
		_saveXml.addActionListener(ma);
		
		_loadNet.addActionListener(ma);
		_loadData.addActionListener(ma);
		_loadXml.addActionListener(ma);

		_clear.addActionListener(ma);

		_bc14ls.addActionListener(ma);
		_findWebsit.addActionListener(ma);
		
		menuBar.add(file);
		menuBar.add(edit);
		menuBar.add(extra);
		
		jtp.add(drawGraph.graph,"Graph");
		jtp.add(drawChart.jchartp,"Chart");
		jtp.add(zd.zdPanel,"ZD");
		
		//History part : show the operations
		
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
        
		hisPart.setLayout(new GridBagLayout());
		
		h.setHorizontalAlignment(JLabel.LEFT);
        his.setParagraphAttributes(normal, true);
		jcp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		jcp.setPreferredSize(new Dimension(100,100));
		clear.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				his.setText("");
			}
		});
		
		GridBagConstraints cons = new GridBagConstraints();
		cons.fill = GridBagConstraints.BOTH;
		cons.gridx = 0;
		cons.gridy = 0;
		hisPart.add(h,cons);
		
		cons.gridx = 0;
		cons.gridy = 1;
		cons.weightx = 1;
		cons.gridwidth = 2;
		hisPart.add(jcp,cons);
		
		cons.gridx = 1;
		cons.gridy = 2;
		cons.fill = GridBagConstraints.NONE;
		cons.anchor = GridBagConstraints.EAST;
		cons.weightx = 0;
		cons.gridwidth = 1;
		hisPart.add(clear,cons);
		
		this.add(menuBar,BorderLayout.NORTH);
		this.add(jtp,BorderLayout.CENTER);
		this.add(hisPart,BorderLayout.SOUTH);
	}
	public void paint(Graphics g){
		offScreenImage = this.createImage(G_WIDTH, G_HEIGHT);
		gImage = offScreenImage.getGraphics();
        gImage.setColor(gImage.getColor());
        gImage.fillRect(0, 0, G_WIDTH, G_HEIGHT); 
        super.paint(gImage);
//paint the things to be painted in different modules
        if(paintFlag == 0){
	        drawGraph.nodes.paint(gImage);//paint the component in this palette
	        drawGraph.links.paint(gImage);
        }
		
		this.setBackground(Color.white);
        g.drawImage(offScreenImage, 0, 0, null);     
	}
	public static void printInfo(String info,String type){
		if(type=="red") logger.error(info);
		else logger.info(info);
		try {
			his.getDocument().insertString(his.getDocument().getLength(),
			         info, his.getStyle(type));//get the history handle and add content to it
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}
	}
	public static void printInfo(String info){//default "normal" style
		logger.info(info);
		try {
			his.getDocument().insertString(his.getDocument().getLength(),
			         info, his.getStyle("normal"));
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error(e);
		}
	}
	public static void doCrawling(){
		bcls.getRelateUrls();
	}
	public  void changeCoor(double wi, double he){
		int nLen = drawGraph.nodes.ags.size();
		for(int i = 0;i<nLen;i++){
			drawGraph.nodes.ags.get(i).self.x*=wi;
			drawGraph.nodes.ags.get(i).self.y*=he;
		}
	}
	private class TimerAction implements ActionListener{
		
		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			//compare the different part and repaint that
			if(hasDiff){
				repaint();
				hasDiff = false;
			}
		}
		
	}
	private class MenuAction implements ActionListener{//for the above menu include "file", "edit" and so on

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == _saveNet){//file->save, to save as object
				saveFileDialog.setVisible(true);
				String fileName = saveFileDialog.getDirectory()+saveFileDialog.getFile();//open the file select dialog
				
		           if(fileName!=null){
		        	   try {
						ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
						if(drawGraph.net != null){
							oos.writeObject(drawGraph.net);
							oos.flush();
							oos.close();
							printInfo( "Info:A net saved!"+fileName+".txt\n","blue");
						}
						else{
							printInfo( "Warning:Nothing saved!\n");
							oos.close();
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		                  
		          }
		           
			}
			else if(e.getSource() == _loadNet){//open as object
				openFileDialog.setVisible(true);
				String fileName = openFileDialog.getDirectory()+openFileDialog.getFile();
				ObjectInputStream ois = null;
		           if(fileName!=null){
		        	   try {
						ois = new ObjectInputStream(new FileInputStream(fileName));
						try {
							drawGraph.net = (Net)ois.readObject();
							drawGraph.count = drawGraph.net.col;
							
							Iterator<Node> ita = drawGraph.net.nodes.iterator();
							Iterator<Link> itl = drawGraph.net.links.iterator();
							
							while(ita.hasNext()) drawGraph.nodes.ags.add(ita.next());
							while(itl.hasNext()) drawGraph.links.ls.add(itl.next());
							printInfo("Info:A net loaded!\n", "blue");
							//repaint();
							DrawNetwork.hasDiff = true;
						} catch (ClassNotFoundException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							printInfo("Error:Not object file!\n", "red");
						}
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		        	   finally{
		        		  if(ois != null)
							try {
								ois.close();
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
		        	   }
		                  
		          }
			}
			else if(e.getSource() == _loadData){//load data that stored in numbers
				drawGraph.net = null;
				drawGraph.links.ls.removeAll(drawGraph.links.ls);
				drawGraph.nodes.ags.removeAll(drawGraph.nodes.ags);
				drawGraph.count = 0;
				//repaint();
				DrawNetwork.hasDiff = true;
				printInfo("Info:The palette is cleared!\n","blue");
				openFileDialog.setVisible(true);
				String fileName = openFileDialog.getDirectory()+openFileDialog.getFile();
				try {
		            Scanner in = new Scanner(new File(fileName));
	        		
	        		int width = drawGraph.palette.getWidth();
	        		int height = drawGraph.palette.getHeight();
	        		Random r =new Random();
	        		
		            while (in.hasNextLine()) {
		                String[] str = in.nextLine().trim().split(",");
		        		int start = Integer.parseInt(str[0]);
		        		int end = Integer.parseInt(str[1]);
		        		int weight = Integer.parseInt(str[2]);
		        		int direct = Integer.parseInt(str[3]);
		        		
		        		int e_x,e_y,s_x,s_y;
		        		Node n1,n2;
		        		Link l;
		        		
		        		if(start == end){
		        			e_x = 15+r.nextInt(width-30);
			        		e_y = 15+r.nextInt(height-50)+drawGraph.W_Y;
		        			n1 = new Node(e_x, e_y, start);
		        			drawGraph.nodes.ags.add(n1);
		        			drawGraph.count++;
		        		}
		        		else{
		        			
			        		if(!drawGraph.nodes.findNode(start)){
			        			s_x = 15+r.nextInt(width-30);
				        		s_y = 15+r.nextInt(height-50)+drawGraph.W_Y;
			        			n1 = new Node(s_x, s_y, start);
			        			drawGraph.nodes.ags.add(n1);
			        			drawGraph.count++;
			        		}
			        		if(!drawGraph.nodes.findNode(end)){
			        			e_x = 15+r.nextInt(width-30);
				        		e_y = 15+r.nextInt(height-50)+drawGraph.W_Y;
			        			n2 = new Node(e_x, e_y, end);
			        			drawGraph.nodes.ags.add(n2);
			        			drawGraph.count++;
			        		}
			        		
			        		l = new Link(drawGraph.nodes.getNode(start).self, drawGraph.nodes.getNode(end).self);
			        		l.label_start = start;
			        		l.label_end = end;
			        		l.weight = weight;
			        		if(direct == 1){
			        			l.directLink = true;
			        			drawGraph.nodes.getNode(start).outDegreeAdd();
			        			drawGraph.nodes.getNode(end).inDegreeAdd();
			        		}
			        		else{
			        			drawGraph.nodes.getNode(start).outDegreeAdd();
			        			drawGraph.nodes.getNode(end).outDegreeAdd();
			        			drawGraph.nodes.getNode(start).inDegreeAdd();
			        			drawGraph.nodes.getNode(end).inDegreeAdd();
			        		}
			        		
			        		if(!drawGraph.links.findLink(start, end))
			        			drawGraph.links.ls.add(l);
		        		}
		            }
		            in.close();
		            printInfo("Info:Data loaded!\n","blue");
		           // repaint();
		           DrawNetwork.hasDiff = true;
		        } catch (FileNotFoundException ex) {
		            ex.printStackTrace();
		        }
				
			}
			else if(e.getSource() == _saveData){
	
				saveFileDialog.setVisible(true);
				String fileName = saveFileDialog.getDirectory()+saveFileDialog.getFile();//open the file select dialog
				
		           if(fileName!=null){
		        	   try {
						FileOutputStream fos = new FileOutputStream(fileName);
						ArrayList<Integer> savedNodes = new ArrayList<Integer>();
						String newline = System.getProperty("line.separator");
						if(drawGraph.nodes != null){
							for(int i=0;i<drawGraph.links.ls.size();i++){
								String s = "";
								Link l = drawGraph.links.ls.get(i);
								int start = l.label_start;
								int end = l.label_end;
								int w = l.weight;
								int type;
								if(l.directLink)
									type = 1;
								else type = 0;
								s = start+","+end+","+w+","+type+newline;
								fos.write(s.getBytes());
								if(!savedNodes.contains(start))
									savedNodes.add(start);
								if(!savedNodes.contains(end))
									savedNodes.add(end);
							}
							for(int i = 0;i<drawGraph.nodes.ags.size();i++){
								if(!savedNodes.contains(drawGraph.nodes.ags.get(i).number)){
									String s = drawGraph.nodes.ags.get(i).number+","+drawGraph.nodes.ags.get(i).number+","+0+","+0;
									fos.write(s.getBytes());
									if(i<drawGraph.nodes.ags.size()-1)
										fos.write(newline.getBytes());
								}
							}
							fos.flush();
							fos.close();
							printInfo( "Info:A net data saved!"+fileName+"\n","blue");
						}
						else{
							printInfo( "Warning:Nothing saved!\n");
							fos.close();
						}
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
		                  
		          }
				
			}
			else if(e.getSource() == _clear || e.getSource() == _new){//clear the history content
				drawGraph.net = null;
				drawGraph.links.ls.removeAll(drawGraph.links.ls);
				drawGraph.nodes.ags.removeAll(drawGraph.nodes.ags);
				drawGraph.count = 0;
				printInfo("Info:The palette is cleared!\n","blue");
				//repaint();
				DrawNetwork.hasDiff = true;
			}
			else if(e.getSource() == _saveXml){
				saveFileDialog.setVisible(true);
				String fileName = saveFileDialog.getDirectory()+saveFileDialog.getFile();//open the file select dialog
				fileName+=".xml";
				Element root = new Element(drawGraph.net.toString());
				Document doc = new Document(root);
				ArrayList<Integer> savedNodes = new ArrayList<Integer>();
				for(int i=0;i<drawGraph.links.ls.size();i++){
					Link l = drawGraph.links.ls.get(i);
					Element el = new Element("Link");
					Element es = new Element("Start").setText(l.label_start+"");
					Element ee = new Element("End").setText(l.label_end+"");
					Element ew = new Element("Weight").setText(l.weight+"");
					Element et = new Element("Type").setText(l.directLink+"");
					el.addContent(es);
					el.addContent(ee);
					el.addContent(ew);
					el.addContent(et);
					root.addContent(el);
					if(!savedNodes.contains(l.label_start)) savedNodes.add(l.label_start);
					if(!savedNodes.contains(l.label_end)) savedNodes.add(l.label_end);
				}
				for(int i=0;i<drawGraph.nodes.ags.size();i++){
					if(!savedNodes.contains(drawGraph.nodes.ags.get(i).number)){
						Element el = new Element("Node");
						Element ep = new Element("Label").setText(drawGraph.nodes.ags.get(i).number+"");
						el.addContent(ep);
						root.addContent(el);
					}
				}
				XMLOutputter XMLOut = new XMLOutputter();  
		        try {  
		            Format f = Format.getPrettyFormat();  
		            f.setEncoding("UTF-8");//default=UTF-8  
		            XMLOut.setFormat(f);  
		            FileOutputStream fos = new FileOutputStream(fileName);
		            XMLOut.output(doc, fos);  
		            fos.close();
		            DrawNetwork.printInfo("Info:The XML file had been saved! "+fileName+"\n", "blue");
		        } catch (Exception ep) {  
		            ep.printStackTrace();  
		        }  
			}
			else if(e.getSource() == _loadXml){
				drawGraph.net = null;
				drawGraph.links.ls.removeAll(drawGraph.links.ls);
				drawGraph.nodes.ags.removeAll(drawGraph.nodes.ags);
				drawGraph.count = 0;
				//repaint();
				DrawNetwork.hasDiff = true;
				
				openFileDialog.setVisible(true);
				String fileName = openFileDialog.getDirectory()+openFileDialog.getFile();;
				SAXBuilder bulider = new SAXBuilder();
				try {
					Document doc = bulider.build(fileName);
					Element root = doc.getRootElement();
					List<?> ls = root.getChildren("Link");
					List<?> ns = root.getChildren("Node");
					
					Random r = new Random();
					int width = drawGraph.palette.getWidth();
	        		int height = drawGraph.palette.getHeight();
	        		int e_x,e_y,s_x,s_y;
	        		Node n1,n2;
	        		Element nd,lk;
	        		Link l;
	        		
	        		for(int i=0;i<ns.size();i++){//Add node to the nodes
	        			nd = (Element)ns.get(i);
	        			e_x = 15+r.nextInt(width-30);
		        		e_y = 15+r.nextInt(height-50)+drawGraph.W_Y;
	        			n1 = new Node(e_x, e_y, Integer.parseInt(nd.getChildText("Label")));
	        			drawGraph.nodes.ags.add(n1);
	        			drawGraph.count++;
	        		}
					for(int i=0;i<ls.size();i++){//Add Links to the links
						lk = (Element)ls.get(i);
						
		        		int start = Integer.parseInt(lk.getChildText("Start"));
		        		int end = Integer.parseInt(lk.getChildText("End"));
		        		int weight = Integer.parseInt(lk.getChildText("Weight"));
		        		String direct = lk.getChildText("Type");
	        		
		        		if(!drawGraph.nodes.findNode(start)){
		        			s_x = 15+r.nextInt(width-30);
			        		s_y = 15+r.nextInt(height-50)+drawGraph.W_Y;
		        			n1 = new Node(s_x, s_y, start);
		        			drawGraph.nodes.ags.add(n1);
		        			drawGraph.count++;
		        		}
		        		if(!drawGraph.nodes.findNode(end)){
		        			e_x = 15+r.nextInt(width-30);
			        		e_y = 15+r.nextInt(height-50)+drawGraph.W_Y;
		        			n2 = new Node(e_x, e_y, end);
		        			drawGraph.nodes.ags.add(n2);
		        			drawGraph.count++;
		        		}
		        		
		        		l = new Link(drawGraph.nodes.getNode(start).self, drawGraph.nodes.getNode(end).self);
		        		l.label_start = start;
		        		l.label_end = end;
		        		l.weight = weight;
		        		if(direct == "True"){
		        			l.directLink = true;
		        			drawGraph.nodes.getNode(start).outDegreeAdd();
		        			drawGraph.nodes.getNode(end).inDegreeAdd();
		        		}
		        		if(direct == "False"){
		        			drawGraph.nodes.getNode(start).outDegreeAdd();
		        			drawGraph.nodes.getNode(end).outDegreeAdd();
		        			drawGraph.nodes.getNode(start).inDegreeAdd();
		        			drawGraph.nodes.getNode(end).inDegreeAdd();
		        		}
		        		if(!drawGraph.links.findLink(start, end))
		        			drawGraph.links.ls.add(l);
		        	
				}
					DrawNetwork.printInfo("XML Loaded completed!\n", "blue");
					repaint();
				} catch (JDOMException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			else if(e.getSource() == _bc14ls){
				drawGraph.net = null;
				drawGraph.links.ls.removeAll(drawGraph.links.ls);
				drawGraph.nodes.ags.removeAll(drawGraph.nodes.ags);
				drawGraph.count = 0;
				
				String url = JOptionPane.showInputDialog("Let's crawling!");
				if(url!=null){
					bcls.setURL(url);
					extraFlag = true;
				}
			}
			else if(e.getSource() == _findWebsit){
				int label=-1;
				String number;
				if((number=JOptionPane.showInputDialog(_findWebsit,"Input the Node Label:"))!=null)
					label = Integer.parseInt(number);
				String website = bcls.getKeyFrom(label);
				if(website != null)
					printInfo("Info: The Website is "+website+" , Enjoy it!\n", "blue");
				else 
					printInfo("Error:No Sunch Node that you input!\n", "red");
			}
			else{
				System.out.println("YOU CAN ADD ANTHER MENU HERE!!");
			}
		}
		
	}
	private class ResizeListener implements ComponentListener{

		@Override
		public void componentResized(ComponentEvent e) {
			// TODO Auto-generated method stub
			G_HEIGHT = getHeight();//update the paint part
			G_WIDTH = getWidth();
			int width= drawGraph.palette.getWidth();//update the palette part
			int height = drawGraph.palette.getHeight();
			changeCoor(1.0*width/DrawGraph.WIDTH,1.0*height/DrawGraph.HEIGHT);
			DrawGraph.WIDTH = width;
			DrawGraph.HEIGHT = height;
			//validate();
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	private class Crawl implements Runnable{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(true){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(extraFlag){
					doCrawling();
					extraFlag = false;
				}
				
			}
		}
		
	}
}
