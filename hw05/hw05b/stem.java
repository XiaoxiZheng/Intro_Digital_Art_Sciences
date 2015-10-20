
import java.util.Random;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.lang.Math.*;
import java.lang.Math;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.*;
import java.awt.geom.Line2D;

public class stem{

	//private BufferedImage image = null;
	private Random rand;
	private Line2D.Double line2d;

	private double randX;

	public int size;
	public double x;
	public double y;

	public double alpha;
	public double beta;
	public double theta;
	public double deltaTheta;
	public int rho;

	public int deltaRho;
	public int direction;
	public double tao;

	public stem(int size_,double alpha_, double deltaTheta_, int deltaRho_){
		size = size_;

		x = size/2;
		y = size/2;
		alpha = alpha_;
		beta = 1-alpha;
		deltaTheta = deltaTheta_;
		deltaRho = deltaRho_;

		line2d = new Line2D.Double();
		rand = new Random();
	}
	public void drawFirstStep(Graphics2D target_, Color color_){
			target_.setColor(color_);
			BasicStroke stroke = new BasicStroke(6);
			target_.setStroke(stroke);

			theta = Math.PI/2;
			rho = 1;
			beta = 1-alpha;
			x = size/2;
			y = size/2;
			double rand1 = rand.nextDouble();
			if (rand1>=0.5){
				direction = -1;
			} else {
				direction = 1;
			}
			double[] coord = new double[2];
			coord = toCartesian(rho,theta*direction);
			//Line2D line2d = new Line2D.Double(x,y,x,y-coord[1]);
			//Line2D line2d = new Line2D.Double(x, y, x, y+rho);
			line2d.setLine(x, y, x, y+rho);
			//System.out.println(x);
			target_.draw(line2d);

			x = line2d.getX2();
			y = line2d.getY2();
	}
	public void singleStemAlg(Graphics2D target_,float basicStrokeWeight_,Color color_){
				BasicStroke stroke= new BasicStroke(basicStrokeWeight_);
				target_.setStroke( stroke );
				target_.setColor(color_);
				if (direction == -1) {
					tao = alpha;
				} else {
					tao = beta;
				}
				randX = rand.nextDouble();
				if (randX>tao) {
					direction = 1;
				} else {
					direction = -1;
				}
				//compute offset
				double randT = rand.nextDouble();
				rho=rho+deltaRho;
				theta = (deltaTheta*randT*direction)+theta;
				double[] newCoord = new double[2];
				newCoord = toCartesian(rho, theta);


				line2d.setLine(x, y, x+newCoord[0], y-newCoord[1]);
				target_.draw(line2d);
				x = line2d.getX2();
				y= line2d.getY2();
	}
	private double[] toCartesian(double rho_, double theta_){
		double [] tempA = new double[2];
		double x = Math.cos(theta_)*rho;
		double y = Math.sin(theta_)*rho;
		tempA[0] = x;
		tempA[1] = y;
		return tempA;
	}
}