import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.util.Random;

public class hw03{

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
	private Random rand = new Random();
	private int randX;
	private int randY;

	private int size;
	private int seeds;
	private	int particles;
	private	int steps;

	private int[] particleX;
	private int[] particleY;

	//=========================
	public ImageFrame(int width, int height){
		//setup the frame's attributes

		this.setTitle("CAP 3027 2015 - HW03 -XiaoxiZheng");
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

		//===Crystal on Toroid plane
		JMenuItem crystalT = new JMenuItem("Crystal(toroid)");
		crystalT.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent event){
				CreateBufferedImageT();
			}
		}   );
		fileMenu.add(crystalT);

		//===Crystal on bounded Plane
		JMenuItem crystalBP = new JMenuItem("Crystal(bounded plane)");
		crystalBP.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent event){
				CreateBufferedImageBP();
			}
		}   );
		fileMenu.add(crystalBP);

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
	private void CreateBufferedImageT(){
		size = promptForSize();
		//System.out.println("size: " +size);
		seeds = promptForSeeds();
		particles = promptForParticles();
		//System.out.println("Particles: " +particles);
		steps = promtForSteps();

		int[] particleX = new int[particles];
		int[] particleY = new int[particles];

		BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		setBG_white(size,image);
		setSeedPixel(seeds,image);//set seed pixels to red randomly
		
		//setParticlePixel(particles,image, particleX[], particlY[]);
		//set starting location for particles
		for(int b=0; b<particles;b++){
			 int randX2 = rand.nextInt(size-1);
			 int randY2 = rand.nextInt(size-1);
			 particleX[b] = randX2;
			 particleY[b] = randY2;
			//image.setRGB(particleX[b],particleY[b],0xFF000000);
		}
		for(int s=0;s<steps;s++){
			//for(int x=0;x<size-1;x++ ){//loop thru the canvas, since technically every 
			for(int i=0;i<particles;i++){
				if(isStuckT(particleX[i],particleY[i],image)){
					//if t's already stuck
					//if(image.getRGB(particleX[i],particleY[i])!= 0xFF000000){ //if it isn't black already
						image.setRGB(particleX[i],particleY[i],0xFF000000); //set it to black
					//}
					
				}
				//else if((isStuck(particleX[i],particleY[i],image))==false){ //for every particle that is not stuck
				else{ //if its a free particle
					int randX_ = rand.nextInt(3)-1; //generating neigbbors[-1,1]
					int randY_ = rand.nextInt(3)-1; //generating neighbors[-1,1]

					int x = particleX[i]+ randX_;
					int y = particleY[i]+randY_;
					//before changing particleX[i],particleY[i] change it back to white.
					//image.setRGB(particleX[i],particleY[i],0xFFFFFFFF);
					//set (x,y) to wrap around if outside of boundary ---bc tortorial plane
						if(x<0){
							x = size-1;
						}
						if(y<0){
							y = size-1;
						}
						if(x>(size-1)){
							x=0;
						}
						if(y>size-1){
							y=0;
						}
						//update position info. 
						particleX[i] = x;
						particleY[i] = y;
						//image.setRGB(particleX[i],particleY[i],0xFF000000);
				}
			}
		}
		displayFile(image);
	}
	private void CreateBufferedImageBP(){
		size = promptForSize();
		seeds = promptForSeeds();
		particles = promptForParticles();
		steps = promtForSteps();

		int[] particleX = new int[particles];
		int[] particleY = new int[particles];

		BufferedImage image = new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
		setBG_white(size,image);
		//set seed pixels to red randomly
		setSeedPixel(seeds,image);
		//set particle pixels and set them to the created
		for(int b=0; b<particles;b++){
			 randX = rand.nextInt(size-1);
			 randY = rand.nextInt(size-1);
			 particleX[b] = randX;
			 particleY[b] = randY;
			//image.setRGB(randX,randY,0xFF000000);
		}
		
		for(int s=0;s<steps;s++){
			for(int i=0;i<particles;i++){
				if(isStuckBP(particleX[i],particleY[i],image)){
					image.setRGB(particleX[i],particleY[i],0xFF000000);
				}
				else{
				//((isStuck(particleX[i],particleY[i],image))==false){ //for every particle that is not stuck		
					int randX_ = rand.nextInt(3)-1; //generating neigbbors[-1,1]
					int randY_ = rand.nextInt(3)-1; //generating neighbors[-1,1]

					int x = particleX[i]+ randX_;
					int y = particleY[i]+randY_;
					//set (x,y) to wrap around if outside of boundary ---bc tortorial plane
						if(x<0){
							x = 0;
						}
						if(y<0){
							y = 0;
						}
						if(x>(size-1)){
							x=size-1;
						}
						if(y>size-1){
							y=size-1;
						}
						//update position info. 
						particleX[i] = x;
						particleY[i] = y;
						//image.setRGB(x,y,0xFF000000);
				}
			}
		}
		displayFile(image);
	}
	private boolean isStuckT(int x, int y, BufferedImage image_){
		boolean isTrue = false;
		int A;
		int B;
		for(int i=-1;i<2;i++){
			A = x+i;
			B = y+i;
			if(A<0){
				A= size-1;
			}
			if(B<0){
				B= size-1;
				}
			if(A>(size-1)){
				A=0;
			}
			if(B>size-1){
				B=0;
			}
			if((image_.getRGB(A,y) != 0xFFFFFFFF) || (image_.getRGB(x,B)!=0xFFFFFFFF)){
				return true;
			}
		}
		
		//System.out.println("isStuck: " + isTrue);
		//if it didn't break out of the loop of having it true..than return false;
		return isTrue;
	}

	private boolean isStuckBP(int x, int y, BufferedImage image_){
		boolean isTrue = false;
		int A;
		int B;
		for(int i=-1;i<2;i++){
			A = x+i;
			B = y+i;
			if(A<0){
				A= 0;
			}
			if(B<0){
				B= 0;
				}
			if(A>(size-1)){
				A= size-1;
			}
			if(B>size-1){
				B= size-1;
			}
			if((image_.getRGB(A,y) != 0xFFFFFFFF) || (image_.getRGB(x,B)!=0xFFFFFFFF)){
				//System.out.println("True" );
				return true;
			}
		}
		
		//System.out.println("isStuck: " + isTrue);
		return isTrue;
	}

	//set seeds to red randomly
	private void setSeedPixel(int seeds_, BufferedImage image_){
		for(int a=0; a<seeds_; a++){
			 int randX1 = rand.nextInt(size-1);
			 int randY1 = rand.nextInt(size-1);
			image_.setRGB(randX1,randY1,0xFFFF0000);
		}
	}
	private int promptForSize(){ //helper method to bufferedIMage methods
		//try catch statement for non int inputs.
		String input = JOptionPane.showInputDialog("Please enter the size of your canvas");
		if(valideInput(input)){
			int size = Integer.parseInt(input);
			return size;
		}
		else{
			return promptForSize(); //if input was invalide, prompt for size again.
		}
		
	}
	private int promptForSeeds(){ //helper method to bufferedIMage methods
		//try catch statement for non int inputs.
		String input = JOptionPane.showInputDialog("Please enter the number of seeds");
		if(valideInput(input)){
			int seeds_ = Integer.parseInt(input);
			return seeds_;
		}
		else{
			return promptForSeeds(); //if input was invalide, prompt for size again.
		}
		
	}
	private int promptForParticles(){ //helper method to bufferedIMage methods
		//try catch statement for non int inputs.
		String input = JOptionPane.showInputDialog("Please enter the number of particles");
		if(valideInput(input)){
			int particles_ = Integer.parseInt(input);
			return particles_;
		}
		else{
			return promptForParticles(); //if input was invalide, prompt for size again.
		}
	}
	private int promtForSteps(){
		String input = JOptionPane.showInputDialog("Please enter the number of steps particles are allowed to take");
		if(valideInput(input)){
			int steps_ = Integer.parseInt(input);
			return steps_;
		}
		else{
			return promptForSteps(); //if input was invalid, prompt for size again.
		}
	}
	private boolean valideInput(String input_){
		try{	
			int num = Integer.parseInt(input_);
			if(num<0){
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

private void setBG_white(int size_, BufferedImage image_){
	for(int x = 0; x<size_; x++){
		for(int y =0; y<size_; y++){
			image_.setRGB(x,y,0xFFFFFFFF);
		}
	}
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

