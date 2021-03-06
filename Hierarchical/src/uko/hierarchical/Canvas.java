/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uko.hierarchical;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import javax.swing.JPanel;
import uko.hierarchical.model.Point;
import uko.hierarchical.model.Structure;
import uko.hierarchical.model.Structure.Cluster;

/**
 *
 * @author uko
 */
public class Canvas extends JPanel
{
	private Structure model;
	@Override
	public void paint(Graphics grphcs)
	{
		super.paint(grphcs);
		Dimension d = getSize();
		Graphics2D g2 = (Graphics2D) grphcs;
		g2.setColor(getBackground());
		g2.fillRect(0, 0, d.width, d.height);
	//	if(model.getClusters()!=null)
		float gradient=0;
		if(model.getClusters().size()>0)
			gradient=1.f/model.getClusters().size();
		float red=0.f;
		float blue=1.f;
		for(Cluster i : model.getClusters())
		{
			BasicStroke shelf = (BasicStroke) g2.getStroke();
			g2.setStroke(new BasicStroke(3));
			g2.setColor(new Color(red, 0.5f, blue));
			g2.draw(new Ellipse2D.Double(i.getCenter().getX()-i.getRadius(), i.getCenter().getY()-i.getRadius(), i.getRadius()*2, i.getRadius()*2));
			g2.setStroke(shelf);
			g2.setColor(new Color(red, 0.f, blue));
			for(Point j : i.getPoints())
			{
				g2.fill(new Ellipse2D.Double(j.getX()-4, j.getY()-4, 7, 7));
			}
			red+=gradient;
			blue-=gradient;
		}
		g2.setColor(getForeground());
	}
	/**
	 * @return the model
	 */
	public Structure getModel()
	{
		return model;
	}
	/**
	 * @param model the model to set
	 */
	public void setModel(Structure model)
	{
		this.model = model;
	}
}
