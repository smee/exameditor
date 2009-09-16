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
package net.kornr.swit.button.devutil;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import net.kornr.swit.button.ButtonTemplate;

/**
 * Just a casual class for quickly testing a button drawing during the developments. I let it here, as it may be useful someday.
 * 
 * @author Rodrigo Reyes
 *
 */
public class ButtonFrame extends JFrame 
{
	private ButtonTemplate m_template;
	
	//static private String[] s_texts = new String[] { "Some test1", "SOME TEST1", "Another Test Here", "Funtag", "Dropline" }; 
	//static private String[] s_texts = new String[] { "My Button Here", "Some test1", "A super long label for a button" };
	
	static private String[] s_texts = new String[] { "ubuntu", "My Button Here", "My Button", "Another Button Here"};
	
	public ButtonFrame(ButtonTemplate template)
	{
		super("Button Templates Tester");
		m_template = template;
		
		this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
		for (String txt : s_texts)
		{
			ImageIcon icon = new ImageIcon(m_template.getImage(txt));
			this.getContentPane().add(new JLabel(txt, icon, JLabel.CENTER));
		}
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void display()
	{
		this.setSize(600,300);
		this.setVisible(true);		
	}
	
	static public void main(String[] args)
	{
		ButtonFrame f = new ButtonFrame(null);
		f.setSize(600,300);
		f.setVisible(true);
	}
}
