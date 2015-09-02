package com.banjo.net.netframe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.banjo.net.basemodules.Link;
import com.banjo.net.basemodules.Node;
import com.banjo.net.bc14ls.FunctionUtils;
import com.banjo.net.bc14ls.VisitedUrl;

public class BC14LS {
	VisitedUrl vUrls;//the urls that we had visited 
	String startUrl = "";
	String topDomain = "";// the domain of the start url
	DrawNetwork dn = null;
	HashMap<String,Integer> mapNodes = new HashMap<String,Integer>();
	int Ncount = 0;
	public BC14LS(DrawNetwork dn,String url){
		this.dn = dn;
		this.vUrls = new VisitedUrl();
		this.startUrl = url;
		Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(startUrl);
		matcher.find();
		this.topDomain = matcher.group();
		
		this.vUrls.visited.add(startUrl);
		this.mapNodes.put(startUrl, ++dn.drawGraph.count);
		DrawNetwork.printInfo("Info: "+startUrl+"\n","blue");
		addNode();
		FunctionUtils.startUrl = startUrl;
		FunctionUtils.topDomain = topDomain;
		dn.paintFlag = 0;
	}
	public BC14LS(DrawNetwork dn){
		this.vUrls = new VisitedUrl();
		this.dn = dn;
	}
	/**
	 * setURL config the basic things that we would need
	 * @param url
	 */
	public void setURL(String url){
		this.startUrl = url;
		Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)",Pattern.CASE_INSENSITIVE);
		Matcher matcher = p.matcher(startUrl);
		matcher.find();
		this.topDomain = matcher.group();
		
		this.vUrls.visited.add(startUrl);
		this.mapNodes.put(startUrl, ++dn.drawGraph.count);
		DrawNetwork.printInfo("Info: "+startUrl+"\n","blue");
		addNode();
		FunctionUtils.startUrl = startUrl;
		FunctionUtils.topDomain = topDomain;
		DrawNetwork.hasDiff = true;
	}
	/**
	 * get the key from a value in a hashmap
	 * @param value
	 * @return
	 */
	public String getKeyFrom(int value){
		Iterator<String> it = mapNodes.keySet().iterator();
		String key = "";
		while(it.hasNext()){
			key = it.next();
			if(mapNodes.get(key) == value) return key;
		}
		return null;
	}
	/**
	 * get the whole url that related with the domain
	 */
	public  void getRelateUrls(){
		long t1=System.currentTimeMillis();
		int flag = vUrls.visited.size();
		int j=0,start,end;
		//int count = 0;
		ArrayList<String> temUrls = null;
		for(int i = 0;i<vUrls.visited.size();i++){
			
			if((--flag)==0){
			//	count++;
				flag = vUrls.visited.size();
			}
			//String s = stickS(count);
			//System.out.println("------------------flag="+flag+"-----------------");
			System.out.println("------------------Nodes="+vUrls.visited.size()+"-----------------");
			//System.out.println(s+vUrls.visited.get(i));

			temUrls = FunctionUtils.getUrls(vUrls.visited.get(i));

			for(j = 0;j<temUrls.size();j++){
				/** be careful with the mapNodes and visited*/
				/**do some link function and nodes added*/
				if(!mapNodes.containsKey(temUrls.get(j))){//first, add nodes
					//add that node
					vUrls.visited.add(temUrls.get(j));
					mapNodes.put(temUrls.get(j),++dn.drawGraph.count);
					addNode();
					DrawNetwork.printInfo("Info: "+temUrls.get(j)+"\n","blue");
				}
				//then, add a direct link
				start = mapNodes.get(vUrls.visited.get(i));
				end = mapNodes.get(temUrls.get(j));
				if(dn.drawGraph.links.findLink(start, end));
					addLink(start, end);
			}
			vUrls.visited.remove(i);//as we use that link already, so he can piss out or the array
			i--;
		}
		DrawNetwork.hasDiff = false;
		String str = "Info: Crawling Over!\n"+"Info: "+
				dn.drawGraph.nodes.ags.size()+" nodes  added and "+dn.drawGraph.links.ls.size()+" links added!\n";
		DrawNetwork.printInfo(str,"blue");
		System.out.println("Cost Time: "+ (System.currentTimeMillis()-t1)/1000 + "!");
	}
	public void addNode(){
		int e_x,e_y;
		Node n;
		int width = DrawGraph.WIDTH;
		int height = DrawGraph.HEIGHT;
		Random r = new Random();
		e_x = 15+r.nextInt(width-30);
		e_y = 15+r.nextInt(height-50)+dn.drawGraph.W_Y;
		n = new Node(e_x, e_y,dn.drawGraph.count);
		dn.drawGraph.nodes.ags.add(n);
		dn.repaint();
	}
	public void addLink(int start,int end){

		Link l;
		if(start!=end){
    		l = new Link(dn.drawGraph.nodes.getNode(start).self, dn.drawGraph.nodes.getNode(end).self);
    		l.label_start = start;
    		l.label_end = end;
    		l.weight = 0;
    		l.directLink = true;//all the links are direct edge
    		dn.drawGraph.nodes.getNode(start).outDegreeAdd();
			dn.drawGraph.nodes.getNode(end).inDegreeAdd();
    		if(!dn.drawGraph.links.findLink(start, end))
    			dn.drawGraph.links.ls.add(l);
		}
		dn.repaint();
	}
	public  String stickS(int c){
		String s = "";
		for(int i=0;i<c;i++) s+="     ";
		return s;
	}
}
