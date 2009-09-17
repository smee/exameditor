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

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import net.kornr.swit.button.ButtonTemplate;
import net.kornr.swit.wicket.border.graphics.BorderMaker;
import net.kornr.swit.wicket.border.graphics.RoundedBorderMaker;


public class BorderUtilSwingFrame extends JFrame 
{
	private BorderMaker m_border;
	
	public BorderUtilSwingFrame(Long id)
	{
		super("Button Templates Tester");
		m_border = BorderMaker.get(id);
		
		BorderLayout bl = new BorderLayout();
		bl.setHgap(20);
		bl.setVgap(20);
		this.getContentPane().setLayout(bl);
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		BorderPane pane = new BorderPane(m_border);
		container.add(pane);
		this.getContentPane().add(container, BorderLayout.CENTER);
		container.setBorder(new EmptyBorder(20,20,20,20));
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void display()
	{
		this.setSize(600,300);
		this.setVisible(true);		
	}
	
	static public void main(String[] args)
	{
		Long s_border = RoundedBorderMaker.register(1, 1, new Color(0xC5,0xC5,0xC5), new Color(0xF8,0xf8,0xf7));
		Long s_centerBorderId = RoundedBorderMaker.register(32, 12, new Color(97,111,154), new Color(215,222,255));

		
//		BorderMaker s_centerBorderId = new RoundedBorderMaker(6, 2, 60, 5f, new Color(97,111,154), new Color(215,222,255));
		new RoundedBorderMaker(30,5,60,3f,Color.red, Color.blue);
		BorderUtilSwingFrame f = new BorderUtilSwingFrame(s_centerBorderId);
		f.setSize(600,300);
		f.setVisible(true);
	}
}
