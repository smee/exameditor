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
package net.kornr.swit.wicket.border.graphics;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * Some Graphics effects
 * 
 * @author Rodrigo Reyes
 *
 */
public class GfxEffects 
{
	public static BufferedImage createSimilar(BufferedImage img)
	{
		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		return result;
	}
	
	static public BufferedImage newBuffer(int width, int height)
	{
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}

	public static BufferedImage addMargin(BufferedImage img, int left, int right, int top, int bottom)
	{
		BufferedImage result = new BufferedImage(img.getWidth()+left+right, img.getHeight()+top+bottom, img.getType());
		Graphics2D g = createInitializedGraphics2DQuality(result);
		g.drawImage(img, left, top, null);
		return result;
	}

	public static BufferedImage getClipped(BufferedImage img, int x, int y, int width, int height)
	{
		BufferedImage result = new BufferedImage(width, height, img.getType());
		Graphics2D g = createInitializedGraphics2DQuality(result);
		g.translate(-x, -y);
		g.drawImage(img, 0, 0, null);
		return result;
	}

	
//	public static BufferedImage convertEmbossToMask(BufferedImage org)
//	{
//		BufferedImage result = new BufferedImage(org.getWidth(), org.getHeight(), BufferedImage.TYPE_INT_ARGB);
//	    for (int x = 0; x < org.getWidth(); x++) 
//	    {
//	    	for (int y = 0; y < org.getHeight(); y++) 
//		    {
//	    		int p = org.getRGB(x,y);
//	    		int shade = convertARGBToGrey(p) & 0xFF;
//
//	    		if (shade == 0x7F )
//	    			result.setRGB(x, y, 0);
//	    		else
//    			result.setRGB(x, y, 0xFF000000 + (shade<<16) + (shade<<8) + shade);
//		    }
//		}
//		return result;
//	}

//	static public BufferedImage createAlphaMaskFromColor(BufferedImage org, Color col, int threshold)
//	{
//		BufferedImage result = new BufferedImage(org.getWidth(), org.getHeight(), BufferedImage.TYPE_INT_ARGB);
//
//	    for (int x = 0; x < org.getWidth(); x++) 
//	    {
//	    	for (int y = 0; y < org.getHeight(); y++) 
//		    {
//	    		int p = org.getRGB(x,y);
//	    		int a = (p>>24)&0xFF;
//	    		int r = (p>>16)&0xFF;
//	    		int g = (p>>8)&0xFF;
//	    		int b = (p)&0xFF;
//
//	    		if (a > 0 && areSimilar(p, col.getRGB(), 1))
//	    			result.setRGB(x,y, 0xFFFFFFFF);
//	    		else
//	    			result.setRGB(x,y, 0xFF000000);
//		    }
//		}
//		return result;
//		
//	}
	
	public static BufferedImage getAlphaMask(BufferedImage org, float brightness, Color color)
	{
		BufferedImage result = new BufferedImage(org.getWidth(), org.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int basecolor = color.getRGB() & 0xFFFFFF;
		
	    for (int x = 0; x < org.getWidth(); x++) 
	    {
	    	for (int y = 0; y < org.getHeight(); y++) 
		    {
	    		int p = org.getRGB(x,y);
				result.setRGB(x, y, (p&0xFF000000) + basecolor);
		    }
		}
		return result;
	}
	
	public static BufferedImage blurFixed(BufferedImage org, int size)
	{
		BufferedImage tempbuf = new BufferedImage(org.getWidth()+size*2, org.getHeight()+size*2, BufferedImage.TYPE_INT_ARGB_PRE);

		Graphics2D tg = createInitializedGraphics2DQuality(tempbuf);
		tg.drawImage(org, size, size, null);
		
		BufferedImage result = createSimilar(tempbuf);
		result = blur(tempbuf, result, size);

		return getClipped(result, size, size, org.getWidth(), org.getHeight());
	}
	
	public static BufferedImage blur(BufferedImage org, BufferedImage target, int size)
	{
		int size2 = size*size;
		float[] matrix = new float[size2];
		for (int i = 0; i < (size2); i++)
			matrix[i] = (1.0f/(float)size2);
		
		ConvolveOp op = new ConvolveOp(new Kernel(size,size,matrix), ConvolveOp.EDGE_ZERO_FILL, null);

		return op.filter(org, target);
	}

//	public static BufferedImage emboss(BufferedImage img, int size)
//	{
//		int size2 = size*size;
//		float[] matrix = new float[] {
//				1, 1 ,1,
//				1, 0.f, -1,
//				-1, -1, -1
//		};
//		ConvolveOp op = new ConvolveOp(new Kernel((int)Math.sqrt(matrix.length),(int)Math.sqrt(matrix.length),matrix), ConvolveOp.EDGE_NO_OP, null);
//		return op.filter(img, null);
//	}

//	public static BufferedImage shift(BufferedImage img, int x, int y)
//	{
//		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
//		result.createGraphics().drawImage(img, x, y, new Color(0,0,0,0), null);
//		return result;
//	}
	
//	public static BufferedImage invertBrightness(BufferedImage img)
//	{
//		BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
//		
//	    for (int x = 0; x < img.getWidth(); x++) 
//	    {
//	    	for (int y = 0; y < img.getHeight(); y++) 
//		    {
//	    		int p = img.getRGB(x,y);
//	    		int val = 0xFFFFFF-(p&0xFFFFFF) + (p&0xFF000000);
//	    		int shade = 0xFF - (convertARGBToGrey(p)&0xFF);
//				result.setRGB(x, y, (p&0xFF000000) + (shade<<16)+(shade<<8)+shade);
//		    }
//		}
//		return result;
//	}

//	public static BufferedImage modifyBrightness(BufferedImage img, float ratio)
//	{
//		float[] matrix = new float[9];
//		matrix[4] = ratio;
//		
//		ConvolveOp op = new ConvolveOp(new Kernel(3,3,matrix), ConvolveOp.EDGE_NO_OP, null);
//
//		return op.filter(img, null);
//	}

//	public static BufferedImage mixOnGray(BufferedImage target, BufferedImage mask, int gray)
//	{
//		BufferedImage result = new BufferedImage(target.getWidth(), target.getHeight(), BufferedImage.TYPE_INT_ARGB);
//	    for (int x = 0; x < result.getWidth(); x++) 
//	    {
//	    	for (int y = 0; y < result.getHeight(); y++) 
//		    {
//	    		int col = target.getRGB(x, y);
//	    		int maskval = convertARGBToGrey(mask.getRGB(x, y)) & 0xFF;
//	    		int diff = maskval - 0x7F;
//	    		float ratio = ((float)maskval/255f) + 0.5f;
//	    		
//	    		int nc = darken(col, ratio*ratio);
//	    		
//	    		result.setRGB(x, y, nc);
//		    }
//	    }
//	    
//	    return result;
//	}
	
	
	
	public static void mixOn(BufferedImage img1, BufferedImage target, float ratio)
	{
	    for (int x = 0; x < img1.getWidth() && x < target.getWidth(); x++) 
	    {
	    	for (int y = 0; y < img1.getHeight() && y<target.getHeight(); y++) 
		    {
	    		int p1 = img1.getRGB(x,y);
	    		int p2 = target.getRGB(x, y);
	    		
	    		int a1 = (p1>>24)&0xFF;
	    		int shade = GfxEffects.convertARGBToGrey(p1) & 0xFF;
	    		
//	    		int alpha = (((p1&0xFF000000)>>24)&0xFF+((p2&0xFF000000)>>24)&0xFF);
//	    		alpha = Math.max((((p1&0xFF000000)>>24)&0xFF), ((p2&0xFF000000)>>24)&0xFF);
//	    		int red = (((p1&0xFF0000)>>16)+((p2&0xFF0000)>>16));
//	    		int green = (((p1&0xFF00)>>8)+((p2&0xFF00)>>8));
//	    		int blue = (((p1&0xFF))+((p2&0xFF)));
	    		
	    		int red = (p2&0xFF0000)>>16;
	    		int green = (p2&0xFF00)>>8;
	    		int blue = p2&0xFF;

	    		red += (int)((float)a1*ratio);
	    		green += (int)((float)a1*ratio);
	    		blue += (int)((float)a1*ratio);
	    		
	    		red = Math.max(0, Math.min(255, red));
	    		green = Math.max(0, Math.min(255, green));
	    		blue = Math.max(0, Math.min(255, blue));
	    		
				target.setRGB(x, y, (p2 & 0xFF000000) + (red<<16) + (green<<8) + blue);
	    		// target.setRGB(x, y, p1);
	    		
		    }
		}
	}

	
	public static BufferedImage mix(BufferedImage img1, BufferedImage img2, int alphaMix)
	{
		BufferedImage result = new BufferedImage(img1.getWidth(), img1.getHeight(), BufferedImage.TYPE_INT_ARGB);
		
	    for (int x = 0; x < img1.getWidth() && x < img2.getWidth(); x++) 
	    {
	    	for (int y = 0; y < img1.getHeight() && y<img2.getWidth(); y++) 
		    {
	    		int p1 = img1.getRGB(x,y);
	    		int p2 = img2.getRGB(x, y);
	    		
//	    		int alpha = (((p1&0xFF000000)>>24)&0xFF+((p2&0xFF000000)>>24)&0xFF)/2;
//	    		alpha = Math.max((((p1&0xFF000000)>>24)&0xFF), ((p2&0xFF000000)>>24)&0xFF);
//	    		int red = (((p1&0xFF0000)>>16)+((p2&0xFF0000)>>16))/2;
//	    		int green = (((p1&0xFF00)>>8)+((p2&0xFF00)>>8))/2;
//	    		int blue = (((p1&0xFF))+((p2&0xFF)))/2;
//	    		 
//				result.setRGB(x, y, (alpha<<24) + (red<<16) + (green<<8) + blue);
	    		
	    		result.setRGB(x, y, mixColor(p1,p2));
		    }
		}
	    return result;
	}
	
	

	public static int mixColor(int p1, int p2)
	{
		int alpha = (((p1&0xFF000000)>>24)&0xFF+((p2&0xFF000000)>>24)&0xFF)/2;
		// alpha = Math.max((((p1&0xFF000000)>>24)&0xFF), ((p2&0xFF000000)>>24)&0xFF);
		int red = (((p1&0xFF0000)>>16)+((p2&0xFF0000)>>16))/2;
		int green = (((p1&0xFF00)>>8)+((p2&0xFF00)>>8))/2;
		int blue = (((p1&0xFF))+((p2&0xFF)))/2;
		
		return (alpha<<24) + (red<<16) + (green<<8) + blue;
	}
	
	
	public static BufferedImage applyConvolution(BufferedImage img, float[]matrix)
	{
		ConvolveOp op = new ConvolveOp(new Kernel((int)Math.sqrt(matrix.length),(int)Math.sqrt(matrix.length),matrix), ConvolveOp.EDGE_NO_OP, null);
		return op.filter(img, null);		
	}
	
	public static void fill(BufferedImage img, Color c)
	{
		Graphics2D g = img.createGraphics();
		g.setColor(c);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
	}

	
	public static void clear(BufferedImage img)
	{
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(0,0,0,0));
		g.setComposite(AlphaComposite.Clear);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
	}
	
	public static void copyOn(BufferedImage source, BufferedImage target, Color background)
	{
		if (background == null)
			background = new Color(0,0,0,0);
		Graphics2D g = target.createGraphics();
		g.setComposite(AlphaComposite.SrcOver);
		g.drawImage(source, 0, 0, background, null);
	}
	
	static public int convertARGBToGrey(int color)
	{
		int result = color&0xFF000000;

		// Respect the brightness of each color component
		float red = 0.299f * (float)((color>>16)&(int)0xFF);
		float green = 0.587f * (float)((color>>8)&(int)0xFF);
		float blue = 0.114f * (float)((color)&(int)0xFF);
		int brightness = (int)(red+green+blue) & 0xFF;
		
		return result + (brightness<<16) + (brightness<<8) + brightness;
	}

	public static int getColorTransition(int color1, int color2, float ratio)
	{
		float alpha = ((float)((color1>>24)&(int)0xFF)*(1.0f-ratio)) + ((float)((color2>>24)&(int)0xFF)*ratio);
		float red   = ((float)((color1>>16)&(int)0xFF)*(1.0f-ratio)) + ((float)((color2>>16)&(int)0xFF)*ratio);
		float green = ((float)((color1>>8)&(int)0xFF)*(1.0f-ratio)) + ((float)((color2>>8)&(int)0xFF)*ratio);
		float blue  = ((float)(color1&0xFF)*(1.0f-ratio)) + ((float)(color2&0xFF)*ratio);

		int a = (int)alpha;
		int r = (int)red;
		int g = (int)green;
		int b = (int)blue;
		
		int result = (a<<24)&0xFF000000 + ((r<<16)&0xFF0000) + ((g<<8)&0xFF00) + (b&0xFF);
		return result;
	}

	public static Color getColorTransition(Color color1, Color color2, float ratio)
	{
		float alpha = ((float)((color1.getAlpha())&(int)0xFF)*(1.0f-ratio)) + ((float)((color2.getAlpha())&(int)0xFF)*ratio);
		float red   = ((float)((color1.getRed())&(int)0xFF)*(1.0f-ratio)) + ((float)((color2.getRed())&(int)0xFF)*ratio);
		float green = ((float)((color1.getGreen())&(int)0xFF)*(1.0f-ratio)) + ((float)((color2.getGreen())&(int)0xFF)*ratio);
		float blue  = ((float)(color1.getBlue()&0xFF)*(1.0f-ratio)) + ((float)(color2.getBlue()&0xFF)*ratio);

		int a = (int)alpha;
		int r = (int)red;
		int g = (int)green;
		int b = (int)blue;
		
		return new Color(r,g,b,a);
	}

	public static int adjustBrightness(int col, float ratio)
	{
		float red = (float)((col>>16)&0xFF) * ratio;
		float green = (float)((col>>8)&0xFF) * ratio;
		float blue = (float)(col&0xFF) * ratio;
		if (red > 255)
			red=255;
		if (green > 255)
			green=255;
		if (blue > 255)
			blue=255;
		return (col&0xFF000000) + ((int)red<<16) + ((int)green<<8) + (int)blue;
	}

	public static Color adjustBrightness(Color col, float ratio)
	{
		float red = (float)col.getRed() * ratio;
		float green = (float)col.getGreen() * ratio;
		float blue = (float)col.getBlue() * ratio;
		red = Math.min(red, 255.0f);
		green = Math.min(green, 255.0f);
		blue = Math.min(blue, 255.0f);
		
		return new Color((int)red, (int)green, (int)blue, col.getAlpha());
	}
	
	static public void main(String[]args)
	{
		Color col1 = new Color(10,50,100);
		Color col2 = new Color(200,180, 160);
		float ratio = 0.5f;
		
		Color col3 = getColorTransition(col1, col2, ratio);
	}

	/**
	 * Replace the alpha value of each pixel of the first image with the corresponding alpha value of the pixels of the second image.
	 * @param org
	 * @param alphaMask
	 * @return an image identical to org, but with the alpha value of each pixel substituted with the alpha values of alphaMask
	 */
	public static BufferedImage substituteAlpha(BufferedImage org, BufferedImage alphaMask)
	{
		BufferedImage result = new BufferedImage(org.getWidth(), org.getHeight(), BufferedImage.TYPE_INT_ARGB);

		for (int x = 0; x < org.getWidth() && x < alphaMask.getWidth(); x++) 
	    {
	    	for (int y = 0; y < org.getHeight() && y<alphaMask.getHeight(); y++) 
		    {
	    		int p1 = org.getRGB(x,y);
	    		int p2 = alphaMask.getRGB(x, y);
	    		int a1 = p1&0xFF000000;
	    		int a2 = p2&0xFF000000;
	    		
	    		if (a1!=0) // Only apply the alpha mask if the pixel from the source is not transparent
	    		{
		    		int resultpixel = (p1 & 0xFFFFFF) | a2;
					result.setRGB(x, y, resultpixel);
	    		}
		    }
		}
		
		return result;
	}

	static public boolean areSimilar(Color c1, Color c2, int thres)
	{
		int r = Math.abs(c1.getRed()-c2.getRed());
		int g = Math.abs(c1.getGreen()-c2.getGreen());
		int b = Math.abs(c1.getBlue()-c2.getBlue());
		
		return (r<=thres) && (g<=thres) && (b<=thres);
	}

	static public boolean areSimilar(int c1, int c2, int thres)
	{
		int r = Math.abs((c1>>16&0xFF)-(c2>>16&0xFF));
		int g = Math.abs((c1>>8&0xFF)-(c2>>8&0xFF));
		int b = Math.abs((c1&0xFF)-(c2&0xFF));
		
		return (r<=thres) && (g<=thres) && (b<=thres);
	}

	static public Graphics2D createInitializedGraphics2DQuality(BufferedImage img)
	{
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		return g;
	}

	static public void displayDebug(BufferedImage img, String title)
	{
		JFrame frame = new JFrame("title");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(new JLabel(title, new ImageIcon(img), JLabel.CENTER));
		frame.setVisible(true);
	}
	
	static public BufferedImage createShadow(BufferedImage img, float size, Color shadowColor)
	{
		BufferedImage buffer = GfxEffects.addMargin(img, (int)size, (int)size, (int)size, (int)size);
		Graphics2D g = createInitializedGraphics2DQuality(buffer);
		g.drawImage(img, 0, 0, null);
		buffer = GfxEffects.getAlphaMask(buffer, 1.0f, shadowColor);
		
		int size2 = (int)(size*size);
		float[] matrix = new float[size2];
		for (int i = 0; i < (size2); i++)
			matrix[i] = (1.0f/(float)size2);

		ConvolveOp op = new ConvolveOp(new Kernel((int)size,(int)size,matrix), ConvolveOp.EDGE_ZERO_FILL, null);
		BufferedImage target = createSimilar(buffer);

		return op.filter(buffer, target);
	}

}
