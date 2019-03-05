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
import java.awt.geom.Rectangle2D;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.text.StyledEditorKit.FontSizeAction;

class Rect{
	public int x_left;
	public int x_right;
	public int y_top;
	public int y_bottom;
	
	public Rect(int x, int i, int y, int j) {
		x_left = x;
		x_right = i;
		y_top = y;
		y_bottom = j;
	}
}

public class cp_WorldCloud {
	public static boolean found = false ;
	public static void main(String[] args) throws IOException {
		HanLP.Config.ShowTermNature = false;  //关闭词性
		charSetDetect("test.txt");
		
//		******************task1*****************
		StringBuilder sb = new StringBuilder();
//		Scanner input = new Scanner(System.in);
//		System.out.println("Please enter the file name:");
//		String fileName = input.nextLine();
		String fileName = "test.txt";
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
//            System.out.println(sb.toString());
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("Program ended");
            System.exit(1);
        }
//        ********the above read the file***********
//      List<Term> termList = HanLP.segment(sb.toString());  标准分词
//      System.out.println(termList);
        List<Term> termList1 = NotionalTokenizer.segment(sb.toString());   //去stop word的分词
        System.out.println(termList1);
//        ********the above divide the text************

    
//        ***************task2*********************
        Occurrence occurrence = new Occurrence();
        occurrence.addAll(termList1);
        occurrence.compute();
        
        Set<Map.Entry<String, TermFrequency>> uniGram = occurrence.getUniGram();
        for (Map.Entry<String, TermFrequency> entry : uniGram){
            TermFrequency termFrequency = entry.getValue();
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
        int width = 1500;
        int height = 1500;
        TextBoard tb = new TextBoard(width, height, Color.BLACK);   //create textboard
        Font font;
    	Random random = new Random();
    	BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics2d = bufferedImage.createGraphics();
		Color backgroundColor = Color.WHITE;
		graphics2d.setBackground(backgroundColor);
//    	Graphics graphics = new graphc
//    	List<Rectangle2D> reclist = new ArrayList<>();
//    	Rectangle2D rec;
    	
    	ArrayList<Rect> wordsRect = new ArrayList<>();
    	
    	for (Entry<String, TermFrequency> e: sorted.entrySet()){
    		String word = e.getKey();
    		int freq = e.getValue().getValue();
    		int FontSize;
    		if(freq <= 2) {
    			FontSize = 10 + freq * 5;
    		}else if (freq <= 4) {
				FontSize = 30 + freq * 10;
			}else {
				FontSize = 30 * freq ;
			}
    		
    		font = new Font("黑体", Font.PLAIN, FontSize);
    		graphics2d.setFont(font);
//    		****************for color*****************
    		boolean coljudge = false;
    		int r = 0;
    		int g = 0; 
    		int b = 0;
    		int bg_r = backgroundColor.getRed();
    		int bg_g = backgroundColor.getGreen();
    		int bg_b = backgroundColor.getBlue();
    		
    		while(coljudge==false) {
    			r = random.nextInt(200)+55;
    			g = random.nextInt(50);
    			b = random.nextInt(50);
//    			*******************for Luminosity Contrast*************
    			double L1 = 0.2126 * pow(r/255, 2.2) + 0.7152 * pow(g/255, 2.2) + 0.0722 * pow(b/255, 2.2);
    			double L2 = 0.2126 * pow(bg_r/255, 2.2) + 0.7152 * pow(bg_g/255, 2.2) + 0.0722 * pow(bg_b/255, 2.2);
    			double lmax = max(((L1+0.05) / (L2+0.05)), ((L2+0.05) / (L1+0.05)));
//    			***************for Pythagorean Distance***************
    			int rd = bg_r-r;
    			int gd = bg_g-g;
    			int bd = bg_b-b;
//    		    *****************judge condition************	
    			if(        ((bg_r-r)+(bg_g-g)+(bg_b-b))>500
    					&& (299*(bg_r-r)+587*(bg_g-g)+114*(bg_b-b))/1000 > 125
    					&& lmax >5
    					&& (sqrt(rd*gd+rd*bd+gd*bd)>250)      ) {
    				coljudge = true;
    			}
    		}
//    		************create the color**************
    		Color c = new Color(r, g, b);          
    		graphics2d.setColor(c);
//    		**********************************************************
    		int x_length = word.length() * font.getSize();
    		int y_length = font.getSize();
    		w1: while (true){
//    			int x = random.nextInt(width - x_length);
//    			int y = random.nextInt(height - y_length);
    			int x = random.nextInt(500)+500;
    			int y = random.nextInt(500)+500;
    			Rect rect = new Rect(x, x+x_length, y, y+y_length);
    			for (Rect rect2 : wordsRect)
    				if (!isValid(rect, rect2))
    					continue w1;
    			System.out.printf("x = %d, y = %d\n", x, y);
    			tb.write(word, x, y+y_length, font, c);
    			graphics2d.drawString(word, x, y+y_length);
    			wordsRect.add(rect);
    			break;
    		}


    		System.out.printf("Term = %s, Freq = %d\n", e.getKey(), e.getValue().getValue());
    	}
    	
    	tb.display();
        
        
        File outputfile = new File("myOutput.jpg");
        ImageIO.write(bufferedImage, "png", outputfile);
    	
    	
//        for( Entry<String, TermFrequency> map1 : sorted.entrySet()) {
//        	TermFrequency freq = map1.getValue();
//        	int FontSize = (int) freq.getFrequency()*10;
//        	font = new Font("幼圆", Font.BOLD, FontSize);
//        	
//        	if(reclist.isEmpty()==true) {
//        		boolean coljudge = false;
//        		int r = 0;
//        		int g = 0; 
//        		int b = 0;
//        		while(coljudge==false) {
//        			r = random.nextInt(200)+55;
//        			g = random.nextInt(20);
//        			b = random.nextInt(20);
//        			double l = 0.2126 * pow(r/255, 2.2) + 0.7152 * pow(g/255, 2.2) + 0.0722 * pow(b/255, 2.2);
//        			int rd = 255-r;
//        			int gd = 255-g;
//        			int bd = 255-b;
//        			
//        			if(        ((255-r)+(255-g)+(255-b))>500
//        					&& (299*(255-r)+587*(255-g)+114*(255-b))/1000 > 125
//        					&& ((1+0.05)/(l+0.05))>5
//        					&& (sqrt(rd*gd+rd*bd+gd*bd)>250)      ) {
//        				coljudge = true;
//        			}
//        		}
//        		
//        		Color c = new Color(r, g, b);
//        		
//
//        		double ang = random.nextInt(3);
//        		tb.write(freq.getKey(), 400, 400, font, c);
//        		rec = tb.getBounds(freq.getKey(), font, 750, 750);
//        		reclist.add(rec);
//        	}else {
//        		boolean judge = true;
//        		while(judge == false) {boolean coljudge = false;
//        		boolean coljudge = false;
//        		int r = 0;
//        		int g = 0;
//        		int b = 0;
//        		while(coljudge==false) {
//        			r = random.nextInt(200)+55;
//        			g = random.nextInt(20);
//        			b = random.nextInt(20);
//        			double l = 0.2126 * pow(r/255, 2.2) + 0.7152 * pow(g/255, 2.2) + 0.0722 * pow(b/255, 2.2);
//        			int rd = 255-r;
//        			int gd = 255-g;
//        			int bd = 255-b;
//        			
//        			if(        ((255-r)+(255-g)+(255-b))>500
//        					&& (299*(255-r)+587*(255-g)+114*(255-b))/1000 > 125
//        					&& ((1+0.05)/(l+0.05))>5
//        					&& (sqrt(rd*gd+rd*bd+gd*bd)>250)      ) {
//        				coljudge = true;
//        			}
//        		}
//        		Color c = new Color(r, g, b);
//        		boolean judge = false;
//        		int xpos = 0;
//        		int ypos = 0;
//        		double rotationAngle = (double)(random.nextInt(2)-1)*90;
//        		double radius = 0;
//        		double theta = 0;
//        			while(judge != true) {
//        				judge = true;        		
//        				xpos = random.nextInt(500)+250;
//        				ypos = random.nextInt(500)+250;
//        				xpos = (int)(radius*Math.cos(theta))+750;
//        				ypos = (int)(radius*Math.sin(theta))+750;
//        				radius+=10;
//        				theta+=Math.PI/12;
//        				xpos = ;
//        				ypos = ；
//        				ang = random.nextInt(3);
//        				rec = tb.getBounds(freq.getKey(), font, xpos, ypos);
//        				double x1 = rec.getX();
//        				double y1 = rec.getY();
//        				double w1 = rec.getWidth();
//        				double h1 = rec.getHeight();
//        				double x2 = x1+w1;
//        				double y2 = y1+h1;
//        				
//        				for(Rectangle2D item : reclist) {
//        					double x3 = item.getX();
//        					double y3 = item.getY();
//        					double w2 = item.getWidth();
//        					double h2 = item.getHeight();
//        					double x4 = x3+w2;
//        					double y4 = y3+h2;
//        					if(  (  (((x1>x3)&&(x1<x4))||((x2>x3)&&(x2<x4)))    &&    ((y1>y3)&&(y1<y4))||((y2>y3)&&(y2<y4))  )==true) {
//        						judge = false;
//        						break;
//        					}
//        				}
//        				
//        			}
//        			rec = tb.getBounds(freq.getKey(), font, xpos, ypos);
//        			tb.write(freq.getKey(), xpos, ypos, rotationAngle, font, c);
//        			reclist.add(rec);
//                }
//        }
//        tb.setVisible(true);
//        tb.setVisible(false);
//        tb.display();
//        BasicSwing swing = new BasicSwing();
//        swing.setVisible(true);/
//        
//        ************************************
//        System.out.println(tb.getColorModel());

        System.out.println("done!");
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
        	
        	
//        System.out.println(occurrence.getTermFrequency("童年"));
//        	double radius = i + theta;
//            Double xp = radius*Math.cos(theta);
//            Double yp = radius*Math.sin(theta);
//            int x = xp.intValue();
//            int y = yp.intValue();
//            System.out.println(tb.getBounds(freq.getKey(), font, radius*Math.cos(theta)+750, radius*Math.sin(theta)+750));
//            Rectangle2D rectangle2d = tb.getBounds(freq.getKey(), font, Xini, Yini);
//            double height = rectangle2d.getHeight();
//            double width  = rectangle2d.getWidth();
//            double X      = rectangle2d.getX();
//            double Y      = rectangle2d.getY();
//            tb.write(freq.getKey(), (int)(X+width), (int)(Y+height), font, Color.blue);
//            tb.write(freq.getKey(), i, i, font, Color.black);
//            X += width;
//            Y += height;
//            i += 10;
//            theta += Math.PI/12;


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
		


