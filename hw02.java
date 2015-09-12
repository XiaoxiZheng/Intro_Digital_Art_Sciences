import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.util.Random;


//Bilinear Interpolation using bufferedImage for Intro to DAS
public class hw02{

	private static final int WIDTH = 401;
	private static final int HEIGHT = 401;

	public static void main( String[] args){
		JFrame frame = new ImageFrame(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true); 
	}
}

//####################################################################
class ImageFrame extends JFrame{
	///private final JFileChooser chooser;
	private BufferedImage image = null;
	private int NE = 0xFF000000; 
	private int NW = 0xFF0000FF;
	private int SE = 0xFFFF0000;
	private int SW = 0xFF00FF00;


	//=========================
	public ImageFrame(int width, int height){
		//setup the frame's attributes

		this.setTitle("CAP 3027 2015 - HW02 -XiaoxiZheng");
		this.setSize( width, height );

		addMenu();////add a menu to the frame

		//setup the file chooser dialog
		//chooser = new JFileChooser();
		//chooser.setCurrentDirectory(new File("."));
	}
	private void addMenu(){
		//setup the frame's munu bar
		//File menu
		JMenu fileMenu = new JMenu("File");

		//===Infinite plane
		JMenuItem bilinearG = new JMenuItem("Bilinear gradient");
		bilinearG.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent event){
				CreateBufferedImage();
			}
		}   );
		fileMenu.add( bilinearG);
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
	private void CreateBufferedImage(){
		int square = prompt();
		BufferedImage image = new BufferedImage(square,square,BufferedImage.TYPE_INT_ARGB);
		int[][] colorArray = new int[square][square];
				//initial corner assignments 
				colorArray[0][0] = NW; 
				image.setRGB(0,0,colorArray[0][0]);//NE corner

				colorArray[square-1][0] = NE;
				image.setRGB(square-1,0,colorArray[square-1][0]);//NW corner

				colorArray[0][square-1] = SW;
				image.setRGB(0,square-1,colorArray[0][square-1]);//SE corner

				colorArray[square-1][square-1] = SE;
				image.setRGB(square-1,square-1,colorArray[square-1][square-1] );//SW corner
				

				//Right coloumn - only calculated delta Red, since that's the only color channel that was changed
				double[] NEarray = extraction(NE); //Extraction for NE
				double[] SEarray = extraction(SE);//Extraction for SE

				double deltaR = (SEarray[1] - NEarray[1])/(square-1); //the array position at [1]---is the extracted red channel 
				
				double redL =NEarray[1]; //starting point of red color channel @ Position NE 
				for(int y=0;y<square;y++){
					redL = redL + deltaR;
					//clamp to nearest legal value
					if(redL>255){
						redL = 255;
					}
					if(redL<0){
						redL = 0; 
					}
					int ARGBNewL = toIntARGB(SEarray[0],redL,SEarray[2],SEarray[3]); //only the red channel will be modified 
					//System.out.println(ARGBNewL);
					colorArray[square-1][y] = ARGBNewL;
					image.setRGB(square-1,y,ARGBNewL);
				}
				double[] SWarray = extraction(SW); //Extraction for SW
				double[] NWarray = extraction(NW);//Extraction for NW

				//left coloumn - only calculate delta green and delta blue, only channels that are modified
				double deltaG = (SWarray[2] - NWarray[2])/(square-1); //[2]--channel for green
				//double deltaB = (NWarray[3] - SWarray[3])/square; //[3]--channel for blue, (NW - SW) bc NW is initially blue, thus avoiding negaitves
				double deltaB = (SWarray[3] - NWarray[3])/(square-1); //[3]--channel for blue

				double greenR = NWarray[2]; //start paiting @ NW corner with initial green from NW corner
				double blueR = NWarray[3]; //starting paiting @ NW corner with blue with initial blue from NW corner
				for(int y=0;y<square;y++){
					greenR = greenR + deltaG; 
					blueR = blueR + deltaB;//
					//clamp to nearest legal value;
					if(greenR>255){
						greenR = 255;
					}
					if(greenR<0){
						greenR = 0; 
					}
					if(blueR>255){
						blueR = 255;
					}
					if(blueR<0){
						blueR = 0; 
					}
					
					int ARGBNewR = toIntARGB(NWarray[0],NWarray[1],greenR,blueR); //only the gren and blue channels will be modified 
					//System.out.println(ARGBNewR);
					colorArray[0][y] = ARGBNewR;//record the color information in the colorArray for future use
					image.setRGB(0,y,ARGBNewR);
				}

				//coloring row by row 
				//double deltaA = alpha is ignored in this project bc every color is completely filled 
				//follows the rule of thumb of right hand side of canvas - left hand side of canvas 
				for(int y = 0; y<square-1;y++){
					double[] colorInfoLeft = extraction(colorArray[0][y]);
					double[] colorInfoRight = extraction(colorArray[square-1][y]);

					//ignores delta alpha bc in this hw has no changes in alpha 
					double deltaRGen = (colorInfoRight[1] - colorInfoLeft[1])/(square-1); //[1]--channel for red
					double deltaGGen = (colorInfoRight[2] - colorInfoLeft[2])/(square-1); //[2]--channel for green
					double deltaBGen = (colorInfoRight[3] - colorInfoLeft[3])/(square-1); //[3]--channel for blue

					double redGeneral = colorInfoLeft[1]; //start paiting @ left
					double greenGeneral = colorInfoLeft[2]; //start paiting @ left
					double blueGeneral = colorInfoLeft[3]; //starting paiting @ left

					for(int x = 0; x<square-1;x++){
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
						int ARGBNewGeneral = toIntARGB(NWarray[0],redGeneral,greenGeneral,blueGeneral);
						colorArray[x][y] = ARGBNewGeneral;//record the color information in the colorArray for future use
						image.setRGB(x+1,y+1,ARGBNewGeneral);
					}
				}
			
		displayFile(image);		
	}
	private int prompt(){ //helper method to bufferedIMage methods
		//try catch statement for non int inputs.
		int size = 0; 
		try{
			String inputVaule = JOptionPane.showInputDialog("Please enter the input size");	
			size = Integer.parseInt(inputVaule);
			if(size<0){
				JOptionPane.showMessageDialog(null, "Invalid Input", "alert", JOptionPane.ERROR_MESSAGE);
			}
			return size;
		}
		catch(Exception e){
			JOptionPane.showMessageDialog(null, "Invalid Input", "alert", JOptionPane.ERROR_MESSAGE);
		}
		return size; //return 0 for error occured;		
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

//just incase if there are any exceptions that wasn't caught in my prompt() method
private void displayFile(BufferedImage image_){
		try{
			displayBufferedImage(image_);
		}
		catch(Exception exception){
			JOptionPane.showMessageDialog(null, "ERRR", "alert", JOptionPane.ERROR_MESSAGE);
		}
	}

	//display BufferedImage
public void displayBufferedImage( BufferedImage image){
		this.setContentPane( new JScrollPane(new JLabel(new ImageIcon(image))));
		this.validate();
	}

}