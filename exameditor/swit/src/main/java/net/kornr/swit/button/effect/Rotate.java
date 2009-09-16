/*
 * Copyright 2009 Rodrigo Reyes reyes.rr at gmail dot com
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package net.kornr.swit.button.effect;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Rotates an image at any angle, resizing the image to fit the rotated image.
 * 
 * @author Rodrigo Reyes
 *
 */
public class Rotate extends Effect {

	private double m_angle = 0d;
	private boolean m_antiAlias = true;
	private double m_antiAliasFactor = 2d;
	
	public Rotate()
	{
	}
	
	public Rotate(double angle)
	{
		m_angle = angle;
	}
	
	@Override
	public BufferedImage apply(BufferedImage org) 
	{
		if (m_antiAlias)
		{
			FactorScale scale = new FactorScale(m_antiAliasFactor);
			org = scale.apply(org);
		}
		
		Dimension dim = calculateNewSize(org.getWidth(),org.getHeight());
		
		BufferedImage result = this.newBuffer((int)Math.ceil(dim.getWidth()), (int)Math.ceil(dim.getHeight()));
		Graphics2D g = this.createInitializedGraphics2D(result);

		double shiftx = (dim.getWidth()-org.getWidth())/2;
		double shifty = (dim.getHeight()-org.getHeight())/2;

		g.translate(shiftx, shifty);
		g.rotate(m_angle, org.getWidth()/2, org.getHeight()/2);
		g.drawImage(org, 0,0, null);

		if (m_antiAlias)
		{
			FactorScale scale = new FactorScale(1d/m_antiAliasFactor);
			result = scale.apply(result);
		}

		return result;
	}
	
	private Dimension calculateNewSize(double width, double height)
	{
		Dimension result = new Dimension();
		double minx=Double.MAX_VALUE, miny=Double.MAX_VALUE;
		double maxx=-99999, maxy=-9999;
		
		double centerx = width/2d, centery = height/2d;
		Point2D[] points = new Point2D[] { new Point2D.Double(0,0),new Point2D.Double(width,0),new Point2D.Double(width,height), new Point2D.Double(0,height) }; 
		for (Point2D p: points)
		{
			translateRotation(p);
			minx = Math.min(minx, p.getX());
			miny = Math.min(miny, p.getY());
			maxx = Math.max(maxx, p.getX());
			maxy = Math.max(maxy, p.getY());
		}
		
		result.setSize(maxx - minx, maxy - miny);
		
		return result;
	}
	
	private void translateRotation(Point2D point)
	{
		double px = (point.getX()*Math.cos(m_angle)) - (point.getY()*Math.sin(m_angle));
		double py = (point.getX()*Math.sin(m_angle)) + (point.getY()*Math.cos(m_angle));
		point.setLocation(px, py);
	}
	
	public double getAngle() {
		return m_angle;
	}
	
	public void setAngle(double angle) {
		m_angle = angle;
	}
	
}
