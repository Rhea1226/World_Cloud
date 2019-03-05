package run;
import com.hankcs.hanlp.HanLP;
import static java.lang.Math.*;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;
import com.hankcs.hanlp.corpus.occurrence.Occurrence;
import com.hankcs.hanlp.corpus.occurrence.TermFrequency;

import demos.TextBoard;
import fileCharsetDetect.FileCharsetDetector;

import java.awt.image.*;
import java.io.*;
import java.net.URL;
import java.util.* ;
import java.util.List;
import java.util.Map.Entry;

import org.mozilla.intl.chardet.* ;

import java.awt.*;
import java.awt.Font;
import java.awt.Color;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

public class cp_WorldCloud_saviour{
	
	public static boolean found = false ;
    static final int width = 1500;  
    static final int height = 1500;  //create constant width & height
    static Random random = new Random();  //random generator for position and color(RGB)

	public static void main(String[] args) throws IOException {
		File f = new File(".");
		System.out.println(f.getAbsolutePath());
//		***********以上仅用于测试路径 test path**************
		HanLP.Config.ShowTermNature = false;  //关闭词性 close the termNature
//		******************task1*****************
		StringBuilder sb = new StringBuilder();
		Scanner input = new Scanner(System.in);
		System.out.println("Please enter the file name, the bgcolor(white or black)， "
				+ "shape(square or circle) and fontcolor(gradient)(red, green, blue) that you like:");
		String fileName = input.next();
		String bgcolorArg = input.next();
		String shapeArg = input.next();
		String fontcolorArg = input.next();
//		String fileName = "sanguo.txt";    
		charSetDetect(fileName);  //detect the charset of file
//		******************Stephen's code of reading file************
        try (BufferedReader r = new BufferedReader(new FileReader(fileName))) {
            String line;
            int    lineNum = 0;
            while ((line = r.readLine()) != null) {
               lineNum++;
               if (lineNum > 1) {
                 sb.append(System.getProperty("line.separator"));
               }
               sb.append(line);
            }
            System.out.println("Reading done");
//            System.out.println(sb.toString());    just for testing sb
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("Program ended");
            System.exit(1);
        }
//        ********the above read the file***********
//      List<Term> termList = HanLP.segment(sb.toString());  这是标准分词
//      System.out.println(termList);
        List<Term> termList1 = NotionalTokenizer.segment(sb.toString());   //去stop word的分词
        System.out.println(termList1);
//        ********the above divide the text************

//        ***************task2*********************
        Occurrence occurrence = new Occurrence();  //Occurrence是hanlp库里已经写好的内容，可以直接使用
        occurrence.addAll(termList1); 
        occurrence.compute();  //统计出频率   count the termFreq
        int maxfreq = 1;    //这的maxfreq用来存最大频率 store the maxFreq
        
        Set<Map.Entry<String, TermFrequency>> uniGram = occurrence.getUniGram();
        for (Map.Entry<String, TermFrequency> entry : uniGram){  //循环unigram里的每一数
            TermFrequency termFrequency = entry.getValue();    
            //entry.getValue得到的是如“word=2”，entry.getValue().getValue()得到的是数字“2”
            if (maxfreq<entry.getValue().getValue()) {
				maxfreq = entry.getValue().getValue();
			}
            System.out.println(termFrequency);
        }
        Map<String, TermFrequency> mapFromSet = new HashMap<String, TermFrequency>();
        for(Map.Entry<String, TermFrequency> entry : uniGram){
            mapFromSet.put(entry.getKey(), entry.getValue());
        }
        System.out.println(mapFromSet.values().toString());
//        **********the above convert the set into a map*********
        
        Map<String, TermFrequency> sorted = sortByValue(mapFromSet);
        System.out.println(sorted.values());
//        **********the above sort the map in natural way*******
        
//        **************part2_display********************
        Font font;  //create the font of text
        Color backgroundColor;  //set bgcolor
        backgroundColor = setBgcolor(bgcolorArg);
        int rgb = backgroundColor.getRGB();  //used in bufferedImage.setRGB(i, j, rgb) here
        TextBoard tb = new TextBoard(width, height, backgroundColor);   //create textboard
    	BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);  //used to create image
		Graphics2D graphics2d = bufferedImage.createGraphics();  
//		*************set bgcolor*************************
//		graphics2d.setColor(backgroundColor);
		 for (int i = 0; i < width; i++) {
		        for (int j = 0; j < height; j++) {
		            bufferedImage.setRGB(i, j, rgb);
		        }
		    }
//		 ***********this above for loop set bgcolor for every pixel of the bufferedImage********
		 ArrayList<Rect> wordsRect = new ArrayList<>();  //this is essential arraylist to store the printed word position and w&h
//		 graphics2d.setBackground(backgroundColor);
//		System.out.println(bufferedImage.getGraphics());   //test the bufferedImage 
//		System.out.println(graphics2d.getBackground());    //test the graphics2d
    	
    	for (Entry<String, TermFrequency> e: sorted.entrySet()){
    		String word = e.getKey();
    		int freq = e.getValue().getValue();
    		int FontSize;
//    		************set the fontsize using piecewise function（分段函数）****************
    		if(freq <= (int)(maxfreq*4/9)) {
    			FontSize = 15 + freq * 6;
    		}else if (freq <= (int)(maxfreq*7/9)) {
				FontSize = 25 + freq * 9;
			}else {
				FontSize = 15 * freq ;
				if(FontSize >= 90) {
					FontSize = 90;
				}
			}
//    		***********************************
    		font = new Font("微软雅黑", Font.ITALIC, FontSize);
    		graphics2d.setFont(font);
//    		**********************************************************
    		int x_length = word.length() * font.getSize();
    		int y_length = font.getSize();
    		w1: while (true){
    			int x = random.nextInt(width - x_length);
    			int y = random.nextInt(height - y_length);
    			Rect rect = new Rect(x, x+x_length, y, y+y_length);
//    			rect.Rotate(rect);
    			while(!checkShape(shapeArg, rect)) {
    				x = random.nextInt(width - x_length);
    				y = random.nextInt(height - y_length);
    				Rect tmp = new Rect(x, x+x_length, y, y+y_length);
//    				rect.Rotate(rect);
    				rect = tmp;
    			}
    			for (Rect rect2 : wordsRect)
    				if (!isValid(rect, rect2))
    					continue w1;
    			System.out.printf("x = %d, y = %d\n", x, y);
    			Color color = setFontcolor(fontcolorArg, rect);
        		graphics2d.setColor(color);
    			tb.write(word, x, y+y_length, font, color);
    			graphics2d.drawString(word, x, y+y_length);
//    			graphics2d.rotate(PI/2);
    			wordsRect.add(rect);
    			break;
    		}
    		System.out.printf("Term = %s, Freq = %d\n", e.getKey(), e.getValue().getValue());
    	}
    	
    	tb.display();
        File outputfile = new File("myOutput.png");
        ImageIO.write(bufferedImage, "png", outputfile);
        System.out.println("well done, nice job!!");
        input.close();
	}

	public static boolean isValid_1d (int x1_min, int x1_max, int x2_min, int x2_max)
	{
//		if (x1_min < x2_min)
//			return !(x1_max > x2_min);
//		else
//		{
//			return !(x2_max > x1_min);
//		}
		return ((x2_min - x1_max) * (x2_max - x1_min) > 0);
	}
	
	public static boolean isValid (Rect r1, Rect r2)

	{
//		System.out.printf("r1.x (%d, %d), r1.y (%d, %d), r2,x (%d, %d), r2.y(%d, %d)\n", r1.x_left, r1.x_right,
//				r1.y_top, r1.y_bottom, r2.x_left, r2.x_right, r2.y_top, r2.y_bottom);
		return (isValid_1d(r1.x_left, r1.x_right, r2.x_left, r2.x_right) ||
				isValid_1d(r1.y_top, r1.y_bottom, r2.y_top, r2.y_bottom));
		// return true;
	}
	
	public static Color setBgcolor(String arg) {
		if (arg.equals("white")) {
			return Color.WHITE;
		}else if(arg.equals("black")){
			return Color.BLACK;
		}else {
			System.err.println("wrong color");
			return null;
		}
	}

	public static Color setFontcolor(String arg, Rect rec) {

		int x = rec.x_left;
		int y = rec.y_top;
		int r = 0;
		int g = 0;
		int b = 0;
		if (arg.equals("gradient.red")) {
			r = 220;
			g = (int)((double)x/width*255);
			b = (int)((double)y/height*255);
			Color color = new Color(r, g, b);
			return color;
		}else if(arg.equals("gradient.green")){
			r = (int)((double)x/width*255);
			g = 255;
			b = (int)((double)y/height*255);
			Color color = new Color(r, g, b);
			return color;
		}else if(arg.equals("gradient.blue")){
			r = (int)((double)x/width*255);
			g = (int)((double)y/height*255);
			b = 225;
			Color color = new Color(r, g, b);
			return color;
		}else if(arg.equals("random.abs")){
			r = random.nextInt(255);
    		g = random.nextInt(255);
    		b = random.nextInt(255);
			Color color = new Color(r, g, b);
			return color; 
		}else {
			System.err.println("wrong color");
			return null;
		}
//****************for color*****************
//		********option 1************
//		int r = random.nextInt(255);
//		int g = random.nextInt(255);
//		int b = random.nextInt(255);
//		********option 2************
//		int r = random.nextInt(30)+220;
//		int g = random.nextInt(50)+90;
//		int b = random.nextInt(180);
//		********option 3************
//		int r = 200;
//		int g = (int)((double)x/width*255);
//		int b = (int)((double)y/height*255);
//		while (checkColor(color, backgroundColor)!=true) {
////			r = random.nextInt(255);      option 1
////			g = random.nextInt(255);
////			b = random.nextInt(255);
////			r = random.nextInt(30)+220;    option 2 
////			g = random.nextInt(50)+90;
////			b = random.nextInt(180);
//			r = (int)((double)x/width*255);      //option 3
//			g = (int)((double)y/height*255);
//			b = 200;
//			color = new Color(r, g, b);
//		}
	}
	
	public static boolean checkShape(String arg, Rect r) {
		if (arg.equals("square")) {
			return ifInSquare(r);
		}else if(arg.equals("circle")){
			return ifInCircle(r);
		}else {
			System.err.println("wrong color");
			return false;
		}
	}
	
	public static boolean ifInCircle(Rect r) {
		double centx = (r.x_left+r.x_right)/2;
		double centy = (r.y_top+r.y_bottom)/2; 
		if ((pow(centx-750, 2)+pow(centy-750, 2))<=650*650) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean ifInSquare(Rect r) {
		double x1 = r.x_left;
		double x2 = r.x_right;
		double y1 = r.y_top;
		double y2 = r.y_bottom;
//		double centx = (r.x_left+r.x_right)/2;
//		double centy = (r.y_top+r.y_bottom)/2; 
		int bound = 200;
		if (       ((x1>bound)&&(x1<1500-bound))
				&& ((x2>bound)&&(x2<1500-bound))
				&& ((y1>bound)&&(y1<1500-bound))
				&& ((y2>bound)&&(y2<1500-bound))     ) {
			return true;
		}else {
			return false;
		}
	}
	
	public static boolean checkColor(Color col, Color backgroundColor) {

		int r = col.getRed();
		int g = col.getGreen();
		int b = col.getBlue();
		int bg_r = backgroundColor.getRed();
        int bg_g = backgroundColor.getGreen();
        int bg_b = backgroundColor.getBlue();
//		*******************for Luminosity Contrast*************
//		double L1 = 0.2126 * pow(r/255, 2.2) + 0.7152 * pow(g/255, 2.2) + 0.0722 * pow(b/255, 2.2);
//		double L2 = 0.2126 * pow(bg_r/255, 2.2) + 0.7152 * pow(bg_g/255, 2.2) + 0.0722 * pow(bg_b/255, 2.2);
//		double lu ;
//		if (L1>L2) {
//			lu = (L1+0.05) / (L2+0.05);
//		}else {
//			lu = (L2+0.05) / (L1+0.05);
//		}
//		***************for Pythagorean Distance***************
//		int rd = bg_r-r;
//		int gd = bg_g-g;
//		int bd = bg_b-b;
//		&& (sqrt(rd*gd+rd*bd+gd*bd)>250) 
//	    *****************judge condition from the website provided************	
		  
//		lu > 3.3
//        ()
//		&&((abs(299*(bg_r-r)+587*(bg_g-g)+114*(bg_b-b))/1000 > 125))
        if (((abs(bg_r-r)+abs(bg_g-g)+abs(bg_b-b))>500) == true) {
			return true;
		}else {
			return false;
		}
		
//		double yiq = ((r*299)+(g*587)+(b*114))/1000;
//		return (yiq >= 128) ? true : false;
	}
	
//	**************************below is metond of sorting**************
	public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	        List<Entry<K, V>> list = new ArrayList<>(map.entrySet());
	        list.sort(Entry.comparingByValue());
	        List<Entry<K, V>> newlist = new ArrayList<>(map.entrySet());
	        int tmp = list.size();
	        for(int i=0; i<list.size(); i++) {
	        	newlist.remove(i);//因为后面的add是添加不是替换，所以要把对应位置的数remove掉
	        	newlist.add(i, list.get(tmp-1));
	        	tmp--;
	        }//通过重新排弄出逆序
	        Map<K, V> result = new LinkedHashMap<>();
	        for (Entry<K, V> entry : newlist) {
	            result.put(entry.getKey(), entry.getValue());
	        }
	        return result;	
		}
//		***************the code below is from charsetdetector*********
	private static void charSetDetect (String path) {
		    // Initalize the nsDetector() ;
		    int lang = nsPSMDetector.ALL ;
		    nsDetector det = new nsDetector( lang) ;
		    // Set an observer...
		    // The Notify() will be called when a matching charset is found.
		    det.Init( new nsICharsetDetectionObserver() {
		      public void Notify(String charset) {
		        FileCharsetDetector.found = true ;
		        System.out.println( "CHARSET = " + charset);
		      }
		    });
		    URL url = null;
		    BufferedInputStream imp = null;
		    try {
		      url = new File( path).toURI().toURL();
		      imp = new BufferedInputStream( url.openStream());
		      byte[] buf = new byte[1024] ;
		      int len;
		      boolean done = false ;
		      boolean isAscii = true ;
		         
		      while ( (len=imp.read(buf,0,buf.length)) != -1) {
		        // Check if the stream is only ascii.
		        if (isAscii)
		          isAscii = det.isAscii( buf, len);

		        // DoIt if non-ascii and not done yet.
		        if (!isAscii && !done)
		          done = det.DoIt( buf, len, false);
		      }
		      det.DataEnd();
		      imp.close();
		         
		      if (isAscii) {
		        System.out.print( "CHARSET = ASCII");
		        tryToOutput( path, "ASCII" );
		        found = true ;
		      }

		      if (!found) {
		        String prob[] = det.getProbableCharsets() ;
		        for (int i = 0; i < prob.length; i++) {
		          System.out.print( "Probable Charset = " + prob[i] );
		          tryToOutput( path, prob[i] );
		        }
		      }
		    } catch (Exception e) {
		      // TODO Auto-generated catch block
		      e.printStackTrace();
		    } 
		  }
		  
	private static void tryToOutput (String path, String charSetName) {
		    InputStream inputStream = null;
		    BufferedReader in = null;
		    Scanner ins = null; 
		    try {
		      inputStream = new FileInputStream( path);
		      in = new BufferedReader( new InputStreamReader( inputStream, charSetName ));
		      ins = new Scanner( in );
		      String s = ins.next();
		      System.out.println ( " : " + s );
		    } catch (Exception e) {
		      e.printStackTrace();
		    } finally {
		      ins.close();
		    }
		  }
		
}

