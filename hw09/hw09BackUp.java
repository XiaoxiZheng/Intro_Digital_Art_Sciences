import java.util.Random;
import java.awt.Color;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.lang.Math.*;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.geom.Line2D;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.lang.Math;
import java.lang.Character;
import java.util.Stack;

import java.util.Scanner;
import java.io.BufferedReader;



public class hw09{
	private static final int WIDTH = 600;
	private static final int HEIGHT = 450;

	public static void main( String[] args){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				 createGUI();
			}
		});
	}
	private static void createGUI() {
		JFrame frame = new ImageFrame(WIDTH,HEIGHT);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
//####################################################################
class ImageFrame extends JFrame { 

	//private static final int INFINITE = 1000;
	private AreaSelectPanel panel;
	private JButton button;

	private int width = 600;
	private int height = 450;

	private double x = 0;
	private double y = 0;

	//threashold for divergence test
	private int tmax = 100;
	private boolean mandelbrot;
	private BufferedImage image = null;
	private int [] colorSchema = new int [100];


	//=========================
	public ImageFrame(int width, int height){
		this.setTitle("CAP 3027 2015 - HW09 -XiaoxiZheng");
		this.setSize( width, height );

		addMenu();////add a menu to the frame

		//this.setContentPane(new JScrollPane(label));

	}
	private void addMenu(){
		JMenu fileMenu = new JMenu("File Menu");
		//load IFS description
		JMenuItem mandelbrot = new JMenuItem("Mandelbrot");
		mandelbrot.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent event){
				//initial μ value @(-2 + 1.5i) ---> Top Left
				mandelbrot(-2.0,1.5,2,-1.5);	
			}
		}   );
		fileMenu.add(mandelbrot);

		JMenuItem julia = new JMenuItem("Julia Set");
		julia.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event){
			//initial Z value @(-2 + 1.5i) ---> Top Left
				double temp[] = new double [2];
				temp = promptForMiu();
				julia(-2.0,1.5, 2.0, -1.5, temp[0], temp[1]);
			}
		}   );
		fileMenu.add(julia);

		//Save image
		JMenuItem saveImage = new JMenuItem("Save Image");
		saveImage.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent event){
				saveImage();
			}
		}   );
		fileMenu.add(saveImage);		
		//Exit
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.addActionListener( new ActionListener(){
			public void actionPerformed(ActionEvent event){
				System.exit( 0 );
			}
		}	);
		fileMenu.add( exitItem);

		//attach menu to a menu bar
		JMenuBar menuBar = new JMenuBar();
		menuBar.add( fileMenu);
		this.setJMenuBar( menuBar);
	}
	
	private void mandelbrot(double ru, double iu,double ru1,double iu1){
		image = simulatedImage(width,height);
		//interpolate colors and store them in ColorSchema [] array
		interpolateColor();

		double deltaR = ru1- ru;
		double deltaI = iu1 - iu;

		double realIncr = (ru1- ru)/(width - 1);
		double imagIncr = (iu1 - iu)/(height -1);

		//initial μ value @(-2 + 1.5i) ---> Top Left
		double real = ru;
		//imgU = iu;		
		//mandelbrot algorithm 
			//looping thru a 600*450 bounded region 
	  for(int x=0; x< width; x++){
	  	double img =  iu1;
	  	//realU = realU + 4/width;
	  	for(int y=0; y<height; y++){
		  	double [] complexZ = new double [2]; // temp variable to store real and img part of z when computing;
			complexZ[0] = 0;
			complexZ[1] = 0;
			int t = 0;
			while(t!=tmax){
				//z = (z*z) + u
				//update realZ, imgZ
				complexZ = zSquarePlusMiu(complexZ[0],complexZ[1],real,img);
				
				if(sumOfSquare(complexZ[0],complexZ[1]) > 4.0) {
					break;//diverging 
				}
				else{
					++t;
				}
			}
			if(t == tmax){
				//Plot black
				double [] bitmapCoord = new double [2]; //temp variable to store the converted bitmap interpretation of complex number
				bitmapCoord = toBitmapCoord(real,deltaR,img,deltaI);
				System.out.println("(bitmapCoord(x,y): " + (int)Math.round(bitmapCoord[0])+ ", " + (int)Math.round(bitmapCoord[1]));
				//image.setRGB((int)Math.round(bitmapCoord[0]),(int)Math.round(bitmapCoord[1]),0xFF000000);
			}
			else{
				//diverged and μ is not in Mandelbrot set
				//plot μ using colorSchema[t].
				double [] bitmapCoord = new double [2]; //temp variable to store the converted bitmap interpretation of complex number
				bitmapCoord = toBitmapCoord(real,deltaR,img,deltaI);
				System.out.println("(bitmapCoord(x,y): " + (int)Math.round(bitmapCoord[0])+ ", " + (int)Math.round(bitmapCoord[1]));
				//image.setRGB((int)Math.round(bitmapCoord[0]),(int)Math.round(bitmapCoord[1]),colorSchema[t]);
			}
			img -= imagIncr;
		  }
		  real += realIncr;
		}

		//updateImage();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//displayFile(image);
				mandelbrot = true;	
				updateImage(mandelbrot,deltaR, ru, deltaI, iu,0,0);
			}
		});
	}
	private void julia(double rz, double iz, double rz1, double iz1, double rMiu_, double iMiu_){

		image = simulatedImage(width,height);
		double [] complexU = new double [2];
		complexU[0] = rMiu_;
		complexU[1] =  iMiu_;
		//interpolate colors and store them in ColorSchema [] array
		interpolateColor();
		double deltaR = rz1- rz;
		double deltaI = iz1 - iz;

		double realIncr = (rz1- rz)/(width - 1);
		double imagIncr = (iz1 - iz)/(height -1);

		//initial μ value @(-2 + 1.5i) ---> Top Left
		double real = rz;
		//imgU = iu;		
		//julia algorithm 
			//looping thru a 600*450 bounded region 
	  for(int x=0; x< width; x++){
	  	double img =  iz1;
	  	//realU = realU + 4/width;
	  	for(int y=0; y<height; y++){
		  	double [] complexZ = new double [2]; // temp variable to store real and img part of z when computing;
			complexZ[0] = real;
			complexZ[1] = img;
			int t = 0;
			while(t!=tmax){
				//z = (z*z) + u
				complexZ = zSquarePlusMiu(complexZ[0],complexZ[1],complexU[0],complexU[1]);
				
				if(sumOfSquare(complexZ[0],complexZ[1]) > 4.0) {
					break;//diverging 
				}
				else{
					++t;
				}
			}
			if(t ==tmax){
				//Plot black
				double [] bitmapCoord = new double [2]; //temp variable to store the converted bitmap interpretation of complex number
				bitmapCoord = toBitmapCoord(real,deltaR,img,deltaI);
				image.setRGB((int)Math.round(bitmapCoord[0]),(int)Math.round(bitmapCoord[1]),0xFF000000);
			}
			else{
				//diverged and μ is not in Mandelbrot set
				//plot μ using colorSchema[t].
				double [] bitmapCoord = new double [2]; //temp variable to store the converted bitmap interpretation of complex number
				bitmapCoord = toBitmapCoord(real,deltaR,img,deltaI);
				image.setRGB((int)Math.round(bitmapCoord[0]),(int)Math.round(bitmapCoord[1]),colorSchema[t]);
			}
			img -= imagIncr;
		  }
		  real += realIncr;
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				//displayFile(image);
				mandelbrot = false;	
				updateImage(mandelbrot,deltaR, rz, deltaI, iz,rMiu_,iMiu_);
			}
		});
	}
		private double [] zSquarePlusMiu(double rZ,double iZ,double rU,double iU){
		double [] answer = new double [2];
		//compute Z^2 + μ in complex form
		answer[0] = (rZ*rZ) - (iZ*iZ)+ rU;
		//answer[1] = ((rZ*iZ)+ (iZ*rZ)) + iU;
		answer[1] = (2*rZ*iZ) + iU;
		return answer;
	}
	private double magnitude(double realZ_,double imgZ_){
		double answer; 
		answer =  realZ_*realZ_ + imgZ_*imgZ_;
		return (Math.sqrt(answer));
	}
	private double sumOfSquare(double realZ_,double imgZ_){
		double answer = 0.0;
		answer = realZ_*realZ_ + imgZ_*imgZ_;
		return answer;
	}
	private double [] toBitmapCoord(double realU_,double deltaR_,double imgU_,double deltaI_){
		double answer [] = new double [2];

		answer[0] = (int)Math.round((realU_ + deltaR_ /2 ) / deltaR_* (width-1));
		answer[1] = (height -1) - (int)Math.round((imgU_ + deltaI_ /2 ) / deltaI_ * (height-1));
		/*
		if(answer[0] < 0){
			answer[0] = 0;
		}
		if(answer[1] < 0){
			answer[1] = 0;
		}
		if(answer[0] >=width){
			answer[0] = width - 1;
		}
		if(answer[1] >=height){
			answer[1] = height - 1;
		}
		*/
		return answer;
	}
	private void interpolateColor(){
		int ARGBNewGeneral = 0;
		double[] colorInfoLeft;
		double[] colorInfoRight;

		//ignores delta alpha bc in this hw has no changes in alpha 
		double deltaRGen;
		double deltaGGen;
		double deltaBGen;

		double redGeneral; //start paiting @ left
		double greenGeneral; //start paiting @ left
		double blueGeneral; //starting paiting @ left

		//color[0] = white, color[40] = red, color[100] = Blue

		//interpolate from 0---40
		//follows the rule of thumb of right hand side of canvas - left hand side of canvas 
					colorInfoLeft = extraction(16777215);//extract white
					colorInfoRight = extraction(16747520);//extract orange

					//ignores delta alpha bc in this hw has no changes in alpha 
					deltaRGen = (colorInfoRight[1] - colorInfoLeft[1])/(39); //[1]--channel for red
					deltaGGen = (colorInfoRight[2] - colorInfoLeft[2])/(39); //[2]--channel for green
					deltaBGen = (colorInfoRight[3] - colorInfoLeft[3])/(39); //[3]--channel for blue

					redGeneral = colorInfoLeft[1]; //start paiting @ left
					greenGeneral = colorInfoLeft[2]; //start paiting @ left
					blueGeneral = colorInfoLeft[3]; //starting paiting @ left

					for(int x = 0; x<39;x++){
						redGeneral = redGeneral + deltaRGen;  //bc red starts from the left
						greenGeneral = greenGeneral + deltaGGen; 
						blueGeneral = blueGeneral + deltaBGen;
						//clamping
						if(redGeneral>255){
							redGeneral = 255;
						}
						if(redGeneral<0){
							redGeneral = 0;
						}
						if(greenGeneral>255){
							greenGeneral = 255;
						}
						if(greenGeneral<0){
							greenGeneral = 0; 
						}
						if(blueGeneral>255){
							blueGeneral = 255;
						}
						if(blueGeneral<0){
							blueGeneral = 0; 
						}
						ARGBNewGeneral = toIntARGB(255,redGeneral,greenGeneral,blueGeneral);
						colorSchema[x] =ARGBNewGeneral;//record the color information in the colorArray for future use					}
				}
				//40---100
				//follows the rule of thumb of right hand side of canvas - left hand side of canvas 
					colorInfoLeft = extraction(16747520);//extract orange
					colorInfoRight = extraction(16711680);//extract red

					//ignores delta alpha bc in this hw has no changes in alpha 
					deltaRGen = (colorInfoRight[1] - colorInfoLeft[1])/(59); //[1]--channel for red
					deltaGGen = (colorInfoRight[2] - colorInfoLeft[2])/(59); //[2]--channel for green
					deltaBGen = (colorInfoRight[3] - colorInfoLeft[3])/(59); //[3]--channel for blue

					redGeneral = colorInfoLeft[1]; //start paiting @ left
					greenGeneral = colorInfoLeft[2]; //start paiting @ left
					blueGeneral = colorInfoLeft[3]; //starting paiting @ left

					for(int x = 40; x<100;x++){
						redGeneral = redGeneral + deltaRGen;  //bc red starts from the left
						greenGeneral = greenGeneral + deltaGGen; 
						blueGeneral = blueGeneral + deltaBGen;
						//clamping
						if(redGeneral>255){
							redGeneral = 255;
						}
						if(redGeneral<0){
							redGeneral = 0;
						}
						if(greenGeneral>255){
							greenGeneral = 255;
						}
						if(greenGeneral<0){
							greenGeneral = 0; 
						}
						if(blueGeneral>255){
							blueGeneral = 255;
						}
						if(blueGeneral<0){
							blueGeneral = 0; 
						}
						ARGBNewGeneral = toIntARGB(255,redGeneral,greenGeneral,blueGeneral);
						colorSchema[x] =ARGBNewGeneral;//record the color information in the colorArray for future use					}
				}

	}
	private static double[] extraction(int ARGB_){
		double[] extractionArray;
		extractionArray = new double[4];
		//extractionArray -- extraction[0] = alpha values;
							//extraction[1] = red values; & etc with ARGB
		extractionArray[0] = ARGB_>>>24; 
		extractionArray[1] = (ARGB_<<8) >>> 24;
		extractionArray[2] = (ARGB_<<16)>>>24;
		extractionArray[3] = (ARGB_<<24)>>>24;
		return (extractionArray); 
	}
	private int toIntARGB(double alpha_, double red_, double green_, double blue_){
		//System.out.println((alpha_<<24)|(red_<<16)|(green_<<8)|(blue_));

		return ((((int)alpha_)<<24)|(((int)red_)<<16)|((int)(green_)<<8)|((int)blue_));
	}
	private void saveImage(){
		try
			{
				File outputfile = new File("IFS.png");	
			   	javax.imageio.ImageIO.write(image, "png", outputfile );
			}
			catch ( IOException e )
			{
			   JOptionPane.showMessageDialog( ImageFrame.this,
				   		          "Error saving file",
							  "oops!",
							  JOptionPane.ERROR_MESSAGE );
			}
	}
	private double [] promptForMiu(){
		double [] temp = new double[2];
		double [] error = new double[2];

		error[0] = -100000;
		error[1] = 100000;

		String input1 = JOptionPane.showInputDialog("Please enter the real part of Mu ");
		String input2 = JOptionPane.showInputDialog("Please enter the imaginary part of  Mu");
		if(valideInput(input1) && valideInput(input2)){
			temp[0] = Double.parseDouble(input1);
			temp[1] = Double.parseDouble(input2);
			
			return temp;
		}
		else if (input1 == null || input2 == null){ //User clicked "Cancel"
	        System.exit(0);
	        return error;
	      }
	    else{
	     	return promptForMiu();
	    }
	}
	private boolean valideInput(String input_){
		try{
			double num = Double.parseDouble(input_);
			if(num<-1000 || num > 1000){
				JOptionPane.showMessageDialog(null, "Invalid Input", "alert", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			return true;
		}
		catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, "Invalid Input", "alert", JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
	protected BufferedImage simulatedImage(int width_,int height_){
		while (true) {
				if (width_ < 0 || height_ < 0)
					return null;
				try {
					BufferedImage img = new BufferedImage(width_,height_,BufferedImage.TYPE_INT_RGB);
					return img;
				} catch (OutOfMemoryError err) {
					JOptionPane.showMessageDialog(this, "Ran out of memory! Try using a smaller image size.");
				}
			}
	}

	public void updateImage(boolean mandelbrot_,double deltaR_, double rOld, double deltaI_, double iOld,double rMiu_,double iMiu_){
		//this.getContentPane().removeAll();
		final AreaSelectPanel panel = new AreaSelectPanel( image);
		final JButton button = new JButton( "Zoom" );
			button.addActionListener( new ActionListener()
			{
				public void actionPerformed( ActionEvent event )
				{
					button.setText( "Selected " + panel.getUpperLeft() + " to " +
					panel.getLowerRight() );

					//convert to Complex Plane.

					double r0 = panel.getUpperLeft().getX() * deltaR_ + rOld;
					double r1 = panel.getLowerRight().getX()* deltaR_ + rOld;
					//double i0 = (-(y1*(height-1)*(deltaY/150)))+height-1 - (2*deltaY/150);
					//double i1 = (-(y2*(height-1)*(deltaY/150)))+height-1 - (2*deltaY/150);
					double i0 = panel.getUpperLeft().getY() * deltaI_ + iOld;
					double i1 = panel.getLowerRight().getY() * deltaI_ + iOld;
					
					if(mandelbrot_){
						//call mandelbrot
						System.out.println("r0 : " + r0+ ", " + "r1: " + r1);
						System.out.println("i0 : " + i0+ ", " + "i1: " + i1);
						mandelbrot(r0,i0,r1,i1);
					}
					else{
						//call Julia
						julia(r0, i0, r1, i1,rMiu_,iMiu_);
					}
				}
		 	} );
		 panel.repaint();
		 this.getContentPane().add( panel, BorderLayout.CENTER );
		 this.getContentPane().add( button, BorderLayout.SOUTH );
		 this.pack();
		 this.setVisible( true );

	}
  }