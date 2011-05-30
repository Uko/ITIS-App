/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uko.hierarchical.model;

/**
 *
 * @author uko
 */
public class Point
{
	private double x;
	private double y;
	/**
	 * @return the x
	 */
	public double getX()
	{
		return x;
	}
	/**
	 * @param x the x to set
	 */
	public void setX(double x)
	{
		this.x = x;
	}
	/**
	 * @return the y
	 */
	public double getY()
	{
		return y;
	}
	/**
	 * @param y the y to set
	 */
	public void setY(double y)
	{
		this.y = y;
	}
	public Point(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	public Point()
	{
		this(0,0);
	}
	public Point(Point point)
	{
		this.x=point.getX();
		this.y=point.getY();
	}
	public static double distance(Point a, Point b)
	{
		return Math.sqrt((a.getX()-b.getX())*(a.getX()-b.getX())+(a.getY()-b.getY())*(a.getY()-b.getY()));
	}
}
