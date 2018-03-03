// AreaSelectDemo.java
// - illustrates Dave’s AreaSelectPanel
//
// by Dave Small
// v1.0 200810.14: created
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import javax.imageio.*;
import java.io.*;
class AreaSelectDemo
{
	 public static void main( String[] args )
	 {
		 SwingUtilities.invokeLater( new Runnable() {
			 public void run()
			 {
			 createAndShowGUI();
			 }
		 } );
	 }
 public static void createAndShowGUI()
 {
	 JFrame frame = new JFrame();
	 frame.setTitle( "AreaSelectPanel Demo" );
	 frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	 BufferedImage img = null;
	 try
	 {
	 img = ImageIO.read(new File( "1.jpg" ) );
 	}
	 catch (IOException e ) {}
	 final AreaSelectPanel panel = new AreaSelectPanel( img );
	 final JButton button = new JButton( "Select" );
		 button.addActionListener( new ActionListener()
		 {
			 public void actionPerformed( ActionEvent event )
			 {
			 button.setText( "Selected " + panel.getUpperLeft() + " to " +
			 panel.getLowerRight() );
			 }
	 	} );

	 frame.getContentPane().add( panel, BorderLayout.CENTER );
	 frame.getContentPane().add( button, BorderLayout.SOUTH );
	 frame.pack();
	 frame.setVisible( true );
	 }
}