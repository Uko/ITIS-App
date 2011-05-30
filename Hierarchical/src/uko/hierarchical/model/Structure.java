/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uko.hierarchical.model;

import java.awt.Paint;
import java.util.ArrayList;

/**
 *
 * @author uko
 */
public class Structure
{
	/**
	 * @return the clusters
	 */
	public ArrayList<Cluster> getClusters()
	{
		return clusters;
	}
	public static class Cluster
	{
		private ArrayList<Point> points;
		private Point center;
		private double radius;
		private double size;
		public Cluster()
		{
			points=new ArrayList();
			radius=0;
			center=new Point();
		}
		public Cluster(ArrayList<Point> points, Point center, double radius, double size)
		{
			this.points = points;
			this.center = center;
			this.radius = radius;
			this.size = size;
		}
		public Cluster(Point point)
		{
			points=new ArrayList();
			points.add(point);
			center=new Point(point);
		}
		/**
		 * @return the points
		 */
		public ArrayList<Point> getPoints()
		{
			return points;
		}
		/**
		 * @return the center
		 */
		public Point getCenter()
		{
			return center;
		}
		/**
		 * @return the radius
		 */
		public double getRadius()
		{
			return radius;
		}
		/**
		 * @return the size
		 */
		public double getSize()
		{
			return size;
		}
		void addPoint(Point point)
		{
			points.add(point);
			center=new Point((center.getX()+point.getX())/2,(center.getY()+point.getY())/2);
			radius=-1;
			size=-1;
			for (Point i:points)
			{
				for(Point j : points)
					if(i!=j)
						if(size<Point.distance(i, j))
							size=Point.distance(i, j);
				if(radius<Point.distance(i, center))
					radius=Point.distance(i, center);
			}
		}
		void removePoint(Point point)
		{
			points.remove(point);
			double x=0;
			double y=0;
			for(Point i:points)
			{
				x+=i.getX();
				y+=i.getY();
			}
			center=new Point(x/points.size(), y/points.size());
			radius=-1;
			size=-1;
			for (Point i:points)
			{
				for(Point j : points)
					if(i!=j)
						if(size<Point.distance(i, j))
							size=Point.distance(i, j);
				if(radius<Point.distance(i, center))
					radius=Point.distance(i, center);
			}
		}
	}
	private ArrayList<Cluster> clusters;
	public Structure()
	{
		clusters=new ArrayList<Cluster>();
	}
	public void addPoint(Point point)
	{
		clusters.add(new Cluster(point));
	}
	public void aglomerate()
	{
		double minSize = Double.POSITIVE_INFINITY;
		Cluster maxI=null;
		Cluster maxJ=null;
		for(Cluster i:clusters)
			for(Cluster j:clusters)
			{
				if(i!=j)
				{
					if(minSize>Point.distance(i.getCenter(), j.getCenter()))
					{
						minSize=Point.distance(i.getCenter(), j.getCenter());
						maxI=i;
						maxJ=j;
					}
				}
			}
		if((maxI!=null)&&(maxJ!=null))
		{
			clusters.remove(maxI);
			clusters.remove(maxJ);
			ArrayList<Point> temp=new ArrayList<Point>();
			for(Point i:maxI.getPoints())
				temp.add(i);
			for(Point j:maxJ.getPoints())
				temp.add(j);
			double maxPointSize=-1;
			Point center= new Point((maxI.center.getX()+maxJ.getCenter().getX())/2, (maxI.getCenter().getY()+maxJ.getCenter().getY())/2);
			double maxCenterSize=-1;
			for(Point i:temp)
			{
				for(Point j:temp)
				{
					if(i!=j)
					{
						if(maxPointSize<Point.distance(i, j))
						{
							maxPointSize=Point.distance(i, j);
						}
					}	
				}
				if(maxCenterSize<Point.distance(center, i))
					maxCenterSize=Point.distance(center, i);
			}		
				clusters.add(new Cluster(temp, new Point((maxI.center.getX()+maxJ.center.getX())/2, (maxI.center.getY()+maxJ.center.getY())/2), maxCenterSize, maxPointSize));
		}	
	}
	public void devimizate()
	{
		if(clusters!=null)
		{
			Cluster bigestCluster = clusters.get(0);
			for(Cluster i : clusters)
			{
				if(i.getSize()>bigestCluster.getSize())
					bigestCluster=i;
			}
			if(bigestCluster.getSize()>0)
			{
				Cluster temp = new Cluster(farthest(bigestCluster.getPoints()));
				bigestCluster.removePoint(temp.getPoints().get(0));
				while(true)
				{
					Point migrator = null;
					double largerDist = Double.NEGATIVE_INFINITY;
					for(Point i:bigestCluster.getPoints())
					{
						double result=midDist(i, bigestCluster.getPoints())-midDist(i, temp.getPoints());
						if(result>largerDist)
						{
							largerDist=result;
							migrator=i;
						}
					}
					if(largerDist<0)
					{
						clusters.add(temp);
						return;
					}
					else
					{
						bigestCluster.removePoint(migrator);
						temp.addPoint(migrator);
					}
				}
			}
		}
	}
	public Point farthest(ArrayList<Point> points)
	{
		double maxMidDist = -1;
		Point farthestPoint = null;
		for(Point i : points)
		{
			double sumDist = 0;
			for(Point j : points)
			{
				if(i!=j)
					sumDist+=Point.distance(i, j);
			}
			if(sumDist/(points.size()-1)>maxMidDist)
			{
				maxMidDist=sumDist/(points.size()-1);
				farthestPoint=i;
			}	
		}
		return farthestPoint;
	}
	public double midDist(Point theOne, ArrayList<Point> points)
	{
		Point farthestPoint = null;
		double sumDist = 0;
		for(Point j : points)
		{
			if(theOne!=j)
				sumDist+=Point.distance(theOne, j);
		}	
		return sumDist/(points.size());
		
	}
}
