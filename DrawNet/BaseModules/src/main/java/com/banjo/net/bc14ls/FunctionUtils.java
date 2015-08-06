package com.banjo.net.bc14ls;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecProvider;
import org.apache.http.cookie.MalformedCookieException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.DefaultCookieSpec;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

public class FunctionUtils  
{  
	public static String startUrl = "";
	public static String topDomain = "";
	public static CloseableHttpClient usedClient = null;
	public static CloseableHttpClient clientWithoutSSL = getHttpClient();//it can be held until the program exited
	public static CloseableHttpClient clientWithSSL = getHttpClientWithSSL();//it can be held until the program exited
	private static int[] next = next("<a href=\"".toCharArray());
	
	public static ArrayList<String> getUrls(String url){
		return getHrefIn(getContentFrom(url));
		//vUrls.print();
	}
	/**  
     * get the html source of given url
     *   
     * @param url  
     * @return  
     */
    private static String getContentFrom(String url)  
    {  
    	
    	URL u = null;
    	URI uri = null;
    	if(usedClient==null)
    		usedClient = clientWithSSL;
    	try {
			u = new URL(url);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			uri = new URI(u.getProtocol(), u.getHost(), u.getPath(), u.getQuery(), null);
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        HttpGet getHttp = new HttpGet(uri);  
        String content = "";  
    
        CloseableHttpResponse response = null;  
        try
        {  
            /*get the loader of content*/
            response = usedClient.execute(getHttp);  
            HttpEntity entity = response.getEntity();
            
            if (entity != null)  
            {  
                /* trans to string*/
                content = EntityUtils.toString(entity);  
 
            }  
            if(content == null) System.out.println("NULL CONTENT");
    
        } catch (ClientProtocolException e)  
        {  
        	System.out.println("ERROR URL:"+url);
            e.printStackTrace();  
        } catch (IOException e)  
        {  System.out.println("ERROR URL:"+url);
            e.printStackTrace();  
        } finally  
        {  
            if(response!=null)
				try {
					response.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        }  
        return content;  
    }  
    public static void closeHttpClient(){
    	
	  if(usedClient!=null)
		 try {
		 	usedClient.close();
		 } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		 }
    }
    private static CloseableHttpClient getHttpClient(){
    	
    	CookieSpecProvider easySpecProvider = new CookieSpecProvider() {

    	    public CookieSpec create(HttpContext context) {

    	        return new DefaultCookieSpec() {
    	            @Override
    	            public void validate(Cookie cookie, CookieOrigin origin)
    	                    throws MalformedCookieException {
    	                // Oh, I am easy
    	            }
    	        };
    	    }

    	};
    	Registry<CookieSpecProvider> reg = RegistryBuilder.<CookieSpecProvider>create()
    	        .register(CookieSpecs.DEFAULT,
    	            new CookieSpecProvider() {
						@Override
						public CookieSpec create(HttpContext arg0) {
							// TODO Auto-generated method stub
							return null;
						}
					})
    	        .register(CookieSpecs.DEFAULT,
    	            new CookieSpecProvider() {
						
						@Override
						public CookieSpec create(HttpContext arg0) {
							// TODO Auto-generated method stub
							return null;
						}
					})
    	        .register("mySpec", easySpecProvider)
    	        .build();

    	RequestConfig requestConfig = RequestConfig.custom()
    	        .setCookieSpec("mySpec")
    	        .build();
    	
    		return HttpClients.custom()
            		.setDefaultCookieSpecRegistry(reg)
        	        .setDefaultRequestConfig(requestConfig)
        	        .build();
   }
   private static CloseableHttpClient getHttpClientWithSSL(){
   	
   	CookieSpecProvider easySpecProvider = new CookieSpecProvider() {

   	    public CookieSpec create(HttpContext context) {

   	        return new DefaultCookieSpec() {
   	            @Override
   	            public void validate(Cookie cookie, CookieOrigin origin)
   	                    throws MalformedCookieException {
   	                // Oh, I am easy
   	            }
   	        };
   	    }

   	};
   	Registry<CookieSpecProvider> reg = RegistryBuilder.<CookieSpecProvider>create()
   	        .register(CookieSpecs.DEFAULT,
   	            new CookieSpecProvider() {
						@Override
						public CookieSpec create(HttpContext arg0) {
							// TODO Auto-generated method stub
							return null;
						}
					})
   	        .register(CookieSpecs.DEFAULT,
   	            new CookieSpecProvider() {
						
						@Override
						public CookieSpec create(HttpContext arg0) {
							// TODO Auto-generated method stub
							return null;
						}
					})
   	        .register("mySpec", easySpecProvider)
   	        .build();

   	RequestConfig requestConfig = RequestConfig.custom()
   	        .setCookieSpec("mySpec")
   	        .build();
	    	//request https
	    	try {
	            SSLContext sslContext = new SSLContextBuilder()
	                                .loadTrustMaterial(null, new TrustSelfSignedStrategy() {
	                //trust all
	                public boolean isTrusted(X509Certificate[] chain,
	                                String authType) {//throws CertificateException
	                    return true;
	                }
	                    }).build();
	            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
	                    sslContext);
	            return HttpClients.custom()
	            		.setSSLSocketFactory(sslsf)
	            		.setDefaultCookieSpecRegistry(reg)
	        	        .setDefaultRequestConfig(requestConfig)
	        	        .build();
	        } catch (KeyManagementException e) {
	            e.printStackTrace();
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } catch (KeyStoreException e) {
	            e.printStackTrace();
	        }
	        return  HttpClients.createDefault();
  } 
    /**  
     * get the urls in that html source
     */
    private static ArrayList<String> getHrefIn(String content){  
    	ArrayList<String> urls = new ArrayList<String>();
       // System.out.println("begin");  
    	long t1=System.currentTimeMillis();
        String[] contents = content.split("<a href=\""); 
    	//String[] contents = splitTextAll(content, "<a href=\"");
    	//String[] contents = sundaySplit(content, "<a href=\"");
    	//String[] contents = KMPSplit(content, "<a href=\"");
    	System.out.println("Cost Time: "+ (System.currentTimeMillis()-t1) + "!");
        for (int i = 1; i < contents.length; i++){
	        	int endHref = contents[i].indexOf("\"");  
	        	if(endHref!=-1){
		            String aHref = getHrefOfInOut(contents[i].substring(  
		                    0, endHref));  
		            if (aHref != null)  
		            {  
		                String href = getHrefOfInOut(aHref);
		                if(!urls.contains(href)) urls.add(href);
		    
//		                if (!vUrls.visited.contains(href)){  
//		                    vUrls.visited.add(href);  
//		                }  
		            } 
	        	}
        }  
        return urls;
    }  
    private static String[] KMPSplit(String text,String sep){
		ArrayList<String> str = new ArrayList<String>();
		int index = 0;
		int len = sep.length();
		while((index=KMP_Index(text.toCharArray(),sep.toCharArray()))!=-1){
			str.add(text.substring(0, index));
			text = text.substring(index +len);
		}
		str.add(text);
       // for(int i=0;i<str.size();i++) System.out.println(str.get(i));
		return str.toArray(new String[str.size()]);
		
	}
    private static int KMP_Index(char[] s, char[] t) {    
        int i = 0;  
        int j = 0;  
        while (i <= s.length - 1 && j <= t.length - 1) {  
            if (j == -1 || s[i] == t[j]) {  
                i++;  
                j++;  
            } else {  
                j = next[j];  
            }  
        }  
        if (j < t.length) {  
            return -1;  
        } else  
            return i - t.length; 
    }
    private static int[] next(char[] t) {  
        int[] next = new int[t.length];  
        next[0] = -1;  
        int i = 0;  
        int j = -1;  
        while (i < t.length - 1) {  
            if (j == -1 || t[i] == t[j]) {  
                i++;  
                j++;  
                if (t[i] != t[j]) {  
                    next[i] = j;  
                } else {  
                    next[i] = next[j];  
                }  
            } else {  
                j = next[j];  
            }  
        }  
        return next;  
    }  
    private static String[] sundaySplit(String text, String sep){
		
		ArrayList<String> str = new ArrayList<String>();
		int tPos = 0,i=0;
		int sPos = 0;
		int tL = text.length();
		int sL = sep.length();
		int temPos = 0;
		while(tPos<tL-2*sL){
			if(text.charAt(tPos)!=sep.charAt(sPos)){
				if(text.charAt(tPos+sL)==sep.charAt(sL-1)){
					for(i=sL-2;i>=0;i--){
						if(text.charAt(tPos+i+1)==sep.charAt(i)){
							if(i==0){
								//System.out.println("find one!12");
								str.add(text.substring(temPos, tPos+1));
								tPos = tPos + sL+1;
								temPos = tPos;
								sPos = 0;
							}
							else continue;
						}
						else{
							tPos = tPos + sL+1;
							sPos = 0;
							break;
						}
					}
				}
				else{
					tPos++;
					sPos = 0;
				}
				
			}
			else{
				tPos++;
				sPos++;
				for(;sPos<sL;sPos++){
					if(text.charAt(tPos)==sep.charAt(sPos)){
						if(sPos == sL-1){
							//System.out.println("find one!13");
							if((temPos-1)==(tPos-sL)) str.add("");
							else str.add(text.substring(temPos, tPos - sL+1));
							tPos++;
							temPos = tPos;
							break;
						}
						else{
							tPos++;
						}
					}
					else{
						tPos = tPos - sPos +1;
						break;
					}
				}
				sPos = 0;
			}
		}
		str.add(text.substring(temPos));
		//for(i=0;i<str.size();i++) System.out.println(str.get(i));
		
		return str.toArray(new String[str.size()]); 
    }
    private static String[] splitTextAll(String line,String splitText)
	{
		int length = splitText.length();
		List<String> arr = new ArrayList<String>();
	
		int positionStart = - length;
		int positionend = line.indexOf(splitText);
	
		while (positionend>=0)
		{
			positionend = line.indexOf(splitText, positionStart + length);
			if(positionend==-1)
			{
			break;
			}
		
			arr.add(line.substring(positionStart + length, positionend));	
			positionStart = positionend;
		
		}
		
		arr.add(line.substring(positionStart + length));
		
		return arr.toArray(new String[arr.size()]);
	}
    /**  
     * trans to the formal format
     * as we know ,there are two kinds of urls , one for the inner of the website, that url use the relative path link /xx/..
     * and two for the absolute path link http://xxx.xx/xx/..
     *   
     * @param href  
     * @return  
     */
    private static String getHrefOfInOut(String href)  
    {  
        String resultHref = null;  
     
        /* absolute path */
        if (href.startsWith("http://")||href.startsWith("https://")){  //
        	if(href.indexOf(topDomain)!=-1)
        		resultHref = href;
        	else resultHref = startUrl;
        } 
        else
        {  
            /* the relative path inner our website preclude hrefs like a href="#" */
            if (href.startsWith("/")){ 
            	if(!startUrl.endsWith("/"))
            		resultHref = startUrl + href;
            	else {
            		resultHref = startUrl + href.substring(1);
            	}
            }  
        }  
        return resultHref;  
    }  
     
}