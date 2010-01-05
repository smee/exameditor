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
package net.kornr.swit.wicket.border.graphics.devutil;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import net.kornr.swit.wicket.border.graphics.BorderMaker;
import net.kornr.swit.wicket.border.graphics.ImageMap;


public class BorderPane extends JPanel 
{
	private BorderMaker m_border;
	
	public BorderPane(BorderMaker border)
	{
		m_border = border;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int width = this.getWidth();
		int height = this.getHeight();
		
		Graphics2D g2 = (Graphics2D)g.create();
		g2.setPaint(Color.white);
		g2.fillRect(0, 0, width, height);
		
		ImageMap map = m_border.getImageMap();
		Rectangle full = map.getZone("full");
		
//		BufferedImage img = m_border.createImage(full);
		
		BufferedImage tl = m_border.createImage(map.getZone(ImageMap.TOP_LEFT));
		g2.drawImage(tl, 0, 0, null);
		
		BufferedImage tr = m_border.createImage(map.getZone(ImageMap.TOP_RIGHT));
		g2.drawImage(tr, width-tr.getWidth(), 0, null);
		
		BufferedImage br = m_border.createImage(map.getZone(ImageMap.BOTTOM_RIGHT));
		g2.drawImage(br, width-br.getWidth(), height-br.getHeight(), null);

		BufferedImage bl = m_border.createImage(map.getZone(ImageMap.BOTTOM_LEFT));
		g2.drawImage(bl, 0, height-bl.getHeight(), null);

		BufferedImage t = m_border.createImage(map.getZone(ImageMap.TOP));
		g2.setClip(tl.getWidth(), 0, width - (tl.getWidth()+tr.getWidth()), height);
		for (int i=0; i<width; i+=t.getWidth())
			g2.drawImage(t, i, 0, null);
		
		BufferedImage b = m_border.createImage(map.getZone(ImageMap.BOTTOM));
		g2.setClip(bl.getWidth(), height - b.getHeight(), width - (bl.getWidth()+br.getWidth()), b.getHeight());
		for (int i=0; i<width; i+=b.getWidth())
			g2.drawImage(b, i, height-bl.getHeight(), null);
		
		BufferedImage l = m_border.createImage(map.getZone(ImageMap.LEFT));
		g2.setClip(0, tl.getHeight(), l.getWidth(), height-(bl.getHeight()+tl.getHeight()));
		for (int i=0; i<height; i+=l.getHeight())
			g2.drawImage(l, 0, i, null);

		BufferedImage r = m_border.createImage(map.getZone(ImageMap.RIGHT));
		g2.setClip(width-r.getWidth(), tr.getHeight(), r.getWidth(), height-(tr.getHeight()+br.getHeight()));
		for (int i=0; i<height; i+=r.getHeight())
			g2.drawImage(r, width-r.getWidth(), i, null);

		
		g2.setClip(null);
		
//		g.drawImage(img, 50, 50, null);
	}

}
