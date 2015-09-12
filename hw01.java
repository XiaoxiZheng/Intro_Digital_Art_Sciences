import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.util.Random;


//"Drunken walk(sketches" on different topologies(planes)
public class hw01{

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
	private int x;
	private int y;
	Random rand = new Random();


	//=========================
	public ImageFrame(int width, int height){
		//setup the frame's attributes

		this.setTitle("CAP 3027 2015 - HW01 -XiaoxiZheng");
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
		JMenuItem infiniteP = new JMenuItem("Walk on Infinite plane");
		infiniteP.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent event){
				 x = 200; //default center point
				 y = 200;
				bufferedImageIP();	
			}
		}   );
		
		fileMenu.add( infiniteP);
		//bounded plane
		JMenuItem boundedP = new JMenuItem("Walk on bounded plane");
		boundedP.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent event){
				 x = 200;
				 y = 200; //make sure it starts @ center
				bufferedImageBP();
				
			}
		}   );
		fileMenu.add( boundedP);
		//toroidal plane
		JMenuItem toroidalP = new JMenuItem("Walk on toroidal plane");
		toroidalP.addActionListener( new ActionListener(){
			public void actionPerformed( ActionEvent event){
				 x = 200;
				 y = 200;
				bufferedImageTP();
			}
		}   );
		fileMenu.add( toroidalP);

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

	private int prompt(){ //helper method to bufferedIMage methods
		String inputVaule = JOptionPane.showInputDialog("Please assign the number of steps the drunk takes");
		//if(isInteger(inputVaule)){
			
		int steps = Integer.parseInt(inputVaule);
		//}

			
		return steps;
	}
	/*
	private boolean isInteger(String input_){
			try
		   {
		      Integer.parseInt( input_ );
		      return true;
		   }
		   catch( Exception e)
		   {
		      return false;
  		   }

	}
	*/
	private void bufferedImageIP(){
		BufferedImage image = new BufferedImage(401,401,BufferedImage.TYPE_INT_ARGB);
				for(int tempX=0; tempX<401;tempX++){ //color the canvas cream
					for(int tempY =0;tempY<401;tempY++){
						image.setRGB(tempX,tempY,0xFFFFFFEE);
					}
				}
				int steps = prompt();


				image.setRGB(x,y,0xFF000000); //set initial point to black

					int i = 1;
					while(i<=steps){
						int randX = rand.nextInt(3)-1;
						int randY = rand.nextInt(3)-1;
						

						x = randX+x;
						y = randY+y;
						if((x>0 && x<400)&& (y>0 && y<400)){ //make sure is inside boundary 
							image.setRGB(x,y,0xFF000000);
						}
		
						if(i==steps){ //last step change to red
							if((x>0 && x<400) && (y>0 && y<400)){ //make sure is inside boundary  for last step
								image.setRGB(x,y,0xFFFF0000);
							}
						//nothing is "printed" to bufferedImaged if outside the infinitive plane. 
						}
						i++;
					}
			//displayBufferedImage(image);
					displayFile(image);
	}
	private void bufferedImageBP(){
		BufferedImage image = new BufferedImage(401,401,BufferedImage.TYPE_INT_ARGB);
		int steps = prompt();
				for(int tempX=0; tempX<401;tempX++){ //color the canvas cream
					for(int tempY =0;tempY<401;tempY++){
						image.setRGB(tempX,tempY,0xFFFFFFEE);
					}
				}
				image.setRGB(x,y,0xFF000000);
					int i = 1;
					while(i<=steps){
						int randX = rand.nextInt(3)-1;
						int randY = rand.nextInt(3)-1;
						

						x = randX+x;
						y = randY+y;

						if((x>0 && x<400)&& (y>0 && y<400)){ //make sure is inside boundary 
							image.setRGB(x,y,0xFF000000);
						}

						//all other possible parameters and make sure to contain the drunk inside 

							if(x<0){	
								x = 0;
							}
							if(x>400){
								x = 400;
							}
							if(y<0){
								y = 0;
							}
							if(y>400){
								y = 400;
							}
							if(x<0 && y<0){
								x = 0;
								y = 0;
							}
							if(x<0 && y>400){
								x = 0;
								y = 400;
							}
							if(x>400 && y>400){
								x = 400;
								y = 400;
							}
							if(x>400 && y<0){
								x = 400;
								y = 0;
							}
							image.setRGB(x,y,0xFF000000);

							if(i==steps){ //check if it's last step and need to change to red 
								image.setRGB(x,y,0xFFFF0000);
							}
						i++;
					}
		//displayBufferedImage(image);
					displayFile(image);
	}
	private void bufferedImageTP(){
		BufferedImage image = new BufferedImage(401,401,BufferedImage.TYPE_INT_ARGB);
		int steps = prompt();
				for(int tempX=0; tempX<401;tempX++){ //color the canvas cream
					for(int tempY =0;tempY<401;tempY++){
						image.setRGB(tempX,tempY,0xFFFFFFEE);
					}
				}
				image.setRGB(x,y,0xFFFFFFEE);
					int i = 1;
					while(i<=steps){

						int randX = rand.nextInt(3)-1;
						int randY = rand.nextInt(3)-1;
						
						x = randX+x;
						y = randY+y;

						if((x>0 && x<400)&& (y>0 && y<400)){ //make sure is inside boundary 
							image.setRGB(x,y,0xFF000000);
						}
						//parameters are wrapped around to the other side if detect being outside the buffereImage size
						if(x<0){
							x = 400;
						}
						if(y<0){
							y=400;
						}
						if(x>400){
							x=0;
						}
						if(y>400){
							y=0;
						}

						image.setRGB(x,y,0xFF000000);

						if(i==steps){ //last step change to red
							image.setRGB(x,y,0xFFFF0000);
						}

						i++;
					}
		//displayBufferedImage(image);
		displayFile(image);
	}
/*
public class JOptionPaneERROR_MESSAGE{
	public static voic main(String[] args){
			final JPane1 panel = new JPane1();
				JOptionPane.showMessageDialog(pane1,"Please enter a valid input","Error",JOptionPane.ERROR_MESSAGE);

		}
	}
}	
*/

private void displayFile(BufferedImage image_){
		try{
			displayBufferedImage(image_);
		}
		catch(Exception excep){
			JOptionPane.showMessageDialog(null, "Invalid input", "ERROR", JOptionPane.ERROR_MESSAGE);

		}
	}


	//display BufferedImage
	public void displayBufferedImage( BufferedImage image){
		this.setContentPane( new JScrollPane(new JLabel(new ImageIcon(image))));
		this.validate();
	}

}