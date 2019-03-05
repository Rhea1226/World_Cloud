package run;
import com.hankcs.hanlp.HanLP;
import static java.lang.Math.*;
//import draw;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NotionalTokenizer;
import com.hankcs.hanlp.corpus.occurrence.Occurrence;
import com.hankcs.hanlp.corpus.occurrence.TermFrequency;

import demos.TextBoard;

import java.awt.image.*;
import java.io.*;
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


public class WordCloudGenerator {
	public static void main(String[] args) throws IOException {
//		File f = new File(".");
//		System.out.println(f.getAbsolutePath());
		HanLP.Config.ShowTermNature = false;  //关闭词性
		
		
		
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
            
//            String term = entry.getKey();
//            System.out.println(term);
//            termFrequency.increase();                           +1
//            termFrequency.increase(4);	                      +4
//            System.out.println(termFrequency.getFrequency());  这get到的是数字
//            System.out.println(termFrequency.getKey());	这get到的是前面这个文字
        }
//        Set<Map.Entry<String, TermFrequency>> set = map.entrySet();

        Map<String, TermFrequency> mapFromSet = new HashMap<String, TermFrequency>();
        for(Map.Entry<String, TermFrequency> entry : uniGram){
            mapFromSet.put(entry.getKey(), entry.getValue());
        }
        System.out.println(mapFromSet.values().toString());
//        **********the above convert the set into a map*********
        
        Map<String, TermFrequency> sorted = sortByValue(mapFromSet);
        System.out.println(sorted.values());
//        Map<String, TermFrequency> BigToSmall= new HashMap<String, TermFrequency>();
//        **********the above sort the map in natural way*******

        
        
//        **************part2_display********************
        Font font;
        TextBoard tb = new TextBoard(1500, 1500, Color.white);
    	Random random = new Random();
    	List<Rectangle2D> reclist = new ArrayList<>();
    	Rectangle2D rec;
//        int i = 0;
//    	  double theta = 0;
    	
//    	double[] angle = {0, 90, 180, 270}; 

//        for (Map.Entry<String, TermFrequency> entry : uniGram)
//        {
//            TermFrequency termFrequency = entry.getValue();
//            font = new Font("宋体", Font.BOLD, termFrequency.getFrequency()*10);
//            tb.write(termFrequency.getKey(), i, i, font, Color.black);
//            
//            i += 10;
//        }
        for( Entry<String, TermFrequency> map1 : sorted.entrySet()) {
        	TermFrequency freq = map1.getValue();
        	int FontSize = (int) freq.getFrequency()*25;
        	font = new Font("幼圆", Font.BOLD, FontSize);
        	
        	if(reclist.isEmpty()==true) {
        		boolean coljudge = false;
        		int r = 0;
        		int g = 0;
        		int b = 0;
        		while(coljudge==false) {
        			r = random.nextInt(200)+55;
        			g = random.nextInt(20);
        			b = random.nextInt(20);
        			double l = 0.2126 * pow(r/255, 2.2) + 0.7152 * pow(g/255, 2.2) + 0.0722 * pow(b/255, 2.2);
        			int rd = 255-r;
        			int gd = 255-g;
        			int bd = 255-b;
        			
        			if(        ((255-r)+(255-g)+(255-b))>500
        					&& (299*(255-r)+587*(255-g)+114*(255-b))/1000 > 125
        					&& ((1+0.05)/(l+0.05))>5
        					&& (sqrt(rd*gd+rd*bd+gd*bd)>250)      ) {
        				coljudge = true;
        			}
        		}
        		
        		Color c = new Color(r, g, b);
        		

//        		double ang = random.nextInt(3);
        		tb.write(freq.getKey(), 750, 750, font, c);
        		rec = tb.getBounds(freq.getKey(), font, 750, 750);
        		reclist.add(rec);
        	}else {
//        		boolean judge = true;
//        		while(judge == false) {boolean coljudge = false;
        		boolean coljudge = false;
        		int r = 0;
        		int g = 0;
        		int b = 0;
        		while(coljudge==false) {
        			r = random.nextInt(200)+55;
        			g = random.nextInt(20);
        			b = random.nextInt(20);
        			double l = 0.2126 * pow(r/255, 2.2) + 0.7152 * pow(g/255, 2.2) + 0.0722 * pow(b/255, 2.2);
        			int rd = 255-r;
        			int gd = 255-g;
        			int bd = 255-b;
        			
        			if(        ((255-r)+(255-g)+(255-b))>500
        					&& (299*(255-r)+587*(255-g)+114*(255-b))/1000 > 125
        					&& ((1+0.05)/(l+0.05))>5
        					&& (sqrt(rd*gd+rd*bd+gd*bd)>250)      ) {
        				coljudge = true;
        			}
        		}
        		Color c = new Color(r, g, b);
        		boolean judge = false;
        		int xpos = 0;
        		int ypos = 0;
        		double rotationAngle = (double)(random.nextInt(2)-1)*90;
//        		double radius = 0;
//        		double theta = 0;
        			while(judge != true) {
        				judge = true;        		
        				xpos = random.nextInt(750)+250;
                		ypos = random.nextInt(750)+250;
//        				xpos = (int)(radius*Math.cos(theta))+750;
//        				ypos = (int)(radius*Math.sin(theta))+750;
//        				radius+=10;
//        				theta+=Math.PI/12;
//        				xpos = ;
//        				ypos = ；
//        				ang = random.nextInt(3);
        				rec = tb.getBounds(freq.getKey(), font, xpos, ypos);
        				double x1 = rec.getX();
        				double y1 = rec.getY();
        				double w1 = rec.getWidth();
        				double h1 = rec.getHeight();
        				double x2 = x1+w1;
        				double y2 = y1+h1;
        				
        				for(Rectangle2D item : reclist) {
        					double x3 = item.getX();
        					double y3 = item.getY();
        					double w2 = item.getWidth();
        					double h2 = item.getHeight();
        					double x4 = x3+w2;
        					double y4 = y3+h2;
        					if(  (  (((x1>x3)&&(x1<x4))||((x2>x3)&&(x2<x4)))    &&    ((y1>y3)&&(y1<y4))||((y2>y3)&&(y2<y4))  )==true) {
        						judge = false;
        						break;
        					}
        				}
        				
        			}
        			rec = tb.getBounds(freq.getKey(), font, xpos, ypos);
        			tb.write(freq.getKey(), xpos, ypos, rotationAngle, font, c);
        			reclist.add(rec);
                }
        }
//        tb.setVisible(true);
        tb.setVisible(false);
        tb.display();
//        BasicSwing swing = new BasicSwing();
//        swing.setVisible(true);/
        
//        ************************************
        System.out.println(tb.getColorModel());
//        BufferedImage bufferedImage = new BufferedImage(1920, 1080, 2);
//        bufferedImage = new BufferedImage();
        
//        File outputfile = new File("image.jpg");
//        ImageIO.write(bufferedImage, "jpg", outputfile);



        
        
        System.out.println("done!");
        
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
		
}
		


