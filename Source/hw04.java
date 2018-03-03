import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import javax.imageio.*;
import javax.swing.*;

public class hw04{

	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;

	public static void main( String[] args){
		JFrame frame = new ImageFrame(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true); 
	}
}

//####################################################################

class ImageFrame extends JFrame{
	private final JFileChooser chooser;
	private BufferedImage image = null;
	private int width;
	private int height;
	private int imgSize;
	private int userInput;

	//=========================
	public ImageFrame(int width, int height){
		//setup the frame's attributes

		this.setTitle("CAP 3027 2015 - HW04 -XiaoxiZheng");
		this.setSize( width, height );

		addMenu();////add a menu to the frame

		//setup the file chooser dialog
		chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
	}
	private void addMenu(){
		//setup the frame's munu bar
		//File menu
		JMenu fileMenu = new JMenu("File");

		JMenuItem openItem = new JMenuItem("Load source image");
		openItem.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent event){
				openNcreateBufferedImage();
			}
		}   );
		fileMenu.add( openItem);
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
	//open() - choose a file, load, and display the image

	private void openNcreateBufferedImage(){
		File file = getFile();
		if(file != null){
			BufferedImage bImage = validateBimg_File(file);
			width = bImage.getWidth();
			height = bImage.getHeight();

			imgSize = deterImgSize(width,height);
			//get img pixel resouces.
			BufferedImage newBImage = new BufferedImage(imgSize,imgSize,BufferedImage.TYPE_INT_RGB);
			Graphics2D target = (Graphics2D)newBImage.createGraphics();
			target.setBackground(Color.BLACK);

			int x = 0;
			int y = 0;

			userInput = prompt();

			//force clam the userInput into smaller than imgSize.
			if(userInput>imgSize){
				userInput = imgSize-1;
			}

			circle(bImage,target,userInput,imgSize,x,y);
			
			displayFile( newBImage );
		}
	}
	private void circle(BufferedImage bImageT, Graphics2D target_,int userInput_,int size_,int x_,int y_){
			while(!(size_<=userInput_)){
				double [] circColor = calcAvg(bImageT,size_,x_,y_);
				Color customColor = new Color((int)circColor[1],(int)circColor[2],(int)circColor[3]);
				target_.setColor(customColor);
				Ellipse2D circle = new Ellipse2D.Float(x_,y_,size_,size_);
				target_.fill(circle);
				size_ = size_/2;

				circle(bImageT,target_,userInput_,size_,x_,y_);
				circle(bImageT,target_,userInput_,size_,x_+size_,y_);
				circle(bImageT,target_,userInput_,size_,x_,y_+size_);
				circle(bImageT,target_,userInput_,size_,x_+size_,y_+size_);
			}
	}
	private double [] calcAvg(BufferedImage bImage_,int imgSize_,int x_, int y_){
		int alpha = 0;
		int red = 0;
		int green = 0;
		int blue = 0;

		double avgColors[] = new double [4];

		for(int i = x_; i<(x_+imgSize_ -1);i++){
			for(int j = y_; j<(y_+imgSize_-1);j++){
				//alpha = alpha+ getAlpha(bImage_,i,j);
				red = red+ getRed(bImage_,i,j);
				green = green+ getGreen(bImage_,i,j);
				blue = blue+ getBlue(bImage_,i,j);
			}
		}

		//avgColors[0] = alpha/imgSize_; //alpha
		avgColors[1] = red/(imgSize_*imgSize_); //red
		avgColors[2] = green/(imgSize_*imgSize_); //green
		avgColors[3] = blue/(imgSize_*imgSize_); //blue

		return avgColors;
		
	}
	//private int getAlpha(BufferedImage image_,int x,int y){
	//	return image_.getRGB(x,y)>>>24;
	//}
	private int getRed(BufferedImage image_,int x,int y){
		return image_.getRGB(x,y)<<8>>> 24;
	}
	private int getGreen(BufferedImage image_,int x,int y){
		return image_.getRGB(x,y)<<16>>> 24;

	}
	private int getBlue(BufferedImage image_,int x,int y){
		return image_.getRGB(x,y)<<24>>> 24;

	}
	private File getFile(){
		File file = null;
		if(chooser.showOpenDialog(this) ==JFileChooser.APPROVE_OPTION){
			file = chooser.getSelectedFile();
		}
		return file;
	}
	private BufferedImage validateBimg_File(File file){
		//BufferedImage bImage = new BufferedImage(0,0,0xFF000000);
		try{
				BufferedImage bImage_ = ImageIO.read(file);
				return bImage_;
			}
				catch(IOException exception){
				JOptionPane.showMessageDialog( this, exception);
			}
			return null;
	}

	private int deterImgSize(int width_, int height_){
		int imgSizeTemp;
		if(width_<=height_){
			imgSizeTemp = width_;
		}
		else{
			imgSizeTemp = height_;
		}

		return imgSizeTemp;
	}
	private void setBG_black(int size_, BufferedImage image_){
		for(int x = 0; x<size_; x++){
			for(int y =0; y<size_; y++){
				image_.setRGB(x,y,0xFF000000);
			}
		}
} 
	//open a file selected by the user.
	private int prompt(){ //helper method to bufferedIMage methods
		//try catch statement for non int inputs.
		int size = 0; 
		try{
			String inputVaule = JOptionPane.showInputDialog("Please enter the input size");	
			size = Integer.parseInt(inputVaule);
			if(size<=0){
				JOptionPane.showMessageDialog(null, "Invalid Input, please re-enter", "alert", JOptionPane.ERROR_MESSAGE);
				return 0;
			}
			return size;
		}
		catch(NumberFormatException e){
			JOptionPane.showMessageDialog(null, "Invalid Input, please re-enter", "alert", JOptionPane.ERROR_MESSAGE);
			return prompt();
		}
		//return size; //return 0 for error occured;		
	}
	private void displayFile(BufferedImage image_){
		try{
			displayBufferedImage(image_);
		}
		catch(Exception exception){
			JOptionPane.showMessageDialog( this, exception);
		}
	}

	public void displayBufferedImage( BufferedImage image){
		this.setContentPane( new JScrollPane(new JLabel(new ImageIcon(image))));
		this.validate();
	}
}