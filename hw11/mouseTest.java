// MouseTest.java
// - illustrates a technique for interactively selecting and deselecting
// areas on an "image" using the mouse. SIngle click to set, double click
// to clear.
//
// by Dave Small
// v1.0 200611.01: created
// v1.1 200711.26: GUI creation moved to EDT
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics2D;
//######################################################################
class MouseTest
{
 static private final int WIDTH = 400;
 static private final int HEIGHT = 400;
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
 frame.setTitle( "Mouse test" );
 frame.setSize( WIDTH, HEIGHT );
 frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
 frame.setContentPane(
 new MousePanel( new BufferedImage( WIDTH, HEIGHT,
 BufferedImage.TYPE_INT_ARGB ) ) );
 frame.validate();
 frame.setVisible( true );
 }
}
class MousePanel extends JPanel
{
 static private final int BLACK = 0xFF000000;
 static private final int WHITE = 0xFFFFFFFF;
 private BufferedImage image;
 private Graphics2D g2d;
 private final int WIDTH;
 private final int HEIGHT;
 private boolean isARGBColor( Point p, int argb )
 {
 return (image.getRGB( p.x, p.y ) == argb );
 }
 private void drawSquare( Point point, Color color )
 {
 int x = ((point.x >> 4) << 4);
 int y = ((point.y >> 4) << 4);

 g2d.setColor( color );
 g2d.fillRect( x, y, 16, 16 );
 repaint();
 }
 public MousePanel( BufferedImage image )
 {
 this.image = image;
 WIDTH = image.getWidth();
 HEIGHT = image.getHeight();
 g2d = image.createGraphics();
 g2d.fillRect( 0, 0, WIDTH, HEIGHT );

 addMouseListener( new MouseAdapter()
 {
 public void mousePressed( MouseEvent event )
 {
 // set the current square to black
 Point point = event.getPoint();
 if ( isARGBColor( point, WHITE ) )
 drawSquare( point, Color.BLACK );
 }

 public void mouseClicked( MouseEvent event )
 {
 // remove the current square if double clicked
 if ( event.getClickCount() >= 2 )
 {
 Point point = event.getPoint();
 if ( isARGBColor( point, BLACK ) )
 drawSquare( point, Color.WHITE );
 }
 }
 } );

 addMouseMotionListener( new MouseMotionListener()
 {
 public void mouseMoved(MouseEvent event)
 {
	 // set the mouse cursor to cross hairs if it is inside
	 // a black rectangle

	 Point point = event.getPoint();
	 if ( isARGBColor( point, BLACK ) )
	 setCursor( Cursor.getDefaultCursor() );
	 else
	 setCursor( Cursor.getPredefinedCursor (Cursor.CROSSHAIR_CURSOR) );
 }

 public void mouseDragged(MouseEvent event)
{
	 }
	 } );
 }

 public void paintComponent( Graphics g )
 {
	 super.paintComponent( g );
	 g.drawImage( image, 0, 0, null );
 }
}