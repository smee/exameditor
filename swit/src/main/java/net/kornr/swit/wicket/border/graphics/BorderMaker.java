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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import net.kornr.swit.util.LRUMap;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.model.Model;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.value.ValueMap;




/**
 * The BorderMaker is the root class for creating pictures suitable for HTML layout. Classes inheriting BorderMaker 
 * create pictures that can be divided in 9 parts, each part can be used as a stand alone picture to create borders
 * for an html block.
 * <p>
 * Borders are created using a factory class that returns an numeric ID that uniquely identifies the Border. Some 
 * Border are combined with other borders using this ID.
 * </p>
 * <p>
 * Examples:
 * 
 * <pre>
 *   Long roundId = RoundedBorderMaker.register(6, 3, Color.magenta, Color.red);
 *   TableImageBorder border = new TableImageBorder("test", roundId, Color.yellow);
 * </pre>
 * 
 * <pre>
 *   Long glossy = GlossyRoundedBorderMaker.register(6, 12, Color.blue, Color.blue);
 *   Long shadow = GenericShadowBorder.register(glossy, 3, 1, 16, null);
 *   TableImageBorder border = new TableImageBorder("test", shadow, Color.yellow);
 * </pre>
 * 
 * A blue glossy rounded border, with a shadow, and a 32pixel left margin, and a 64 pixels right margin.
 * <pre>
 *   Long glossy = GlossyRoundedBorderMaker.register(6, 12, Color.blue, Color.blue);
 *   Long shadow = GenericShadowBorder.register(glossy, 3, 1, 16, null);
 *   Long margin = MarginBorder.register(shadow, 0, 0, 32, 64, Color.pink);
 *   TableImageBorder border = new TableImageBorder("test", margin, Color.yellow);
 * </pre>
 * 
 * It is also possible to reference directly an image:
 * <pre>
 *  Long margin = MarginBorder.register(shadow, 0, 0, 32, 64, Color.pink);
 *  Long mypic = SizedBorder.register(margin, 640, 480);
 *  String myImageUrl = BorderMaker.getUrl(mypic, "full", false);
 * </pre>
 * </p>
 *  <p>
 *  Registered borders stay in memory forever, which is usually what most applications want. If for some reason the border 
 *  is only used temporarily, the static registerTemporary() method uses a LRU cache, with a limited number of slots, 
 *  least recent borders being discarded when no more slots are available.
 *  </p>
 * @author Rodrigo
 *
 */
abstract public class BorderMaker extends WebResource
{
	protected Long m_id;
	private HashMap<String,File> m_cache = new HashMap<String,File>();
	private int m_width;
	private int m_height;
	
	private static HashMap<Long, BorderMaker> s_regCache = new HashMap<Long, BorderMaker>();
	private static Map<Long, BorderMaker> s_regTemporaryCache = new LRUMap<Long, BorderMaker>(1000);
	
	private static long s_counterName = System.currentTimeMillis();
	
	public abstract BufferedImage createIndexedImage(Rectangle part);
	public abstract BufferedImage createImage(Rectangle part);

	protected BorderMaker()
	{
		
	}
	
	protected BorderMaker(int width, int height)
	{
		m_width = width;
		m_height = height;
	}
	
	/**
	 * Defines the size of the LRU cache for temporary borders. Setting this value discards the temporary cache.
	 * 
	 */
	synchronized static public void setTemporaryCacheSize(int temporaryCachesize)
	{
		s_regTemporaryCache = new LRUMap<Long, BorderMaker>(temporaryCachesize);
	}
	
	/**
	 * Registers a border in a temporary cache. Not for normal use cases. 
	 * @return a id that uniquely identifies the border in the cache.
	 */
	synchronized static public Long registerTemporary(BorderMaker bm)
	{
		Long l = s_counterName++;
		bm.m_id = l;
		s_regTemporaryCache.put(l, bm);
		return l;	
	}	
	
	/**
	 * Registers a border in the global cache.
	 * @return a id that uniquely identifies the border in the cache.
	 */
	synchronized static public Long register(BorderMaker bm)
	{
		Long l = s_counterName++;
		bm.m_id = l;
		s_regCache.put(l, bm);
		return l;	
	}
	
	static public BorderMaker get(Long id)
	{
		return s_regCache.get(id);
	}

	/**
	 * 
	 */
	public static void install(String mapping)
	{
		WebApplication.get().getSharedResources().add(BorderMaker.class, "image", null, null, new BorderMaker() {
			@Override
			public BufferedImage createImage(Rectangle part) {
				return null;
			}
			@Override
			public BufferedImage createIndexedImage(Rectangle part) {
				return null;
			}
			@Override
			public ImageMap getImageMap() {
				return null;
			}
		});
		ResourceReference ref = getReference();
		WebApplication.get().mountSharedResource(mapping, ref.getSharedResourceKey());
	}
	
	public static ResourceReference getReference()
	{
		return new ResourceReference(BorderMaker.class, "image") {

			@Override
			protected Resource newResource() 
			{
				return new BorderMaker() {
					@Override
					public BufferedImage createImage(Rectangle part) {
						return null;
					}
					@Override
					public BufferedImage createIndexedImage(Rectangle part) {
						return null;
					}
					@Override
					public ImageMap getImageMap() {
						return null;
					}
					
				};
			}
		};
	}
	
	public static org.apache.wicket.markup.html.image.Image getImage(String id, Long imageId, String type, boolean indexed)
	{
		ValueMap args = new ValueMap();
		args.put("border", type);
		args.put("id", imageId.toString());
		args.put("type", indexed?"indexed":"rgb");
		ResourceReference ref = getReference();
		org.apache.wicket.markup.html.image.Image img = new org.apache.wicket.markup.html.image.Image(id, ref, args);
		
		Dimension dim = BorderMaker.get(imageId).getMapSize(type);
		
		img.add(new AttributeModifier("width", true, new Model<String>(""+dim.width)));
		img.add(new AttributeModifier("height", true, new Model<String>(""+dim.height)));
		return img;
	}
	
	public static String getUrl(Long imageId, String type, boolean indexed)
	{
		ValueMap args = new ValueMap();
		args.put("border", type);
		args.put("id", imageId.toString());
		args.put("type", indexed?"indexed":"rgb");
		ResourceReference ref = getReference();
		return RequestCycle.get().urlFor(ref, args).toString();
	}
	
	static protected BorderMaker retrieveFromCache(Long id)
	{
		return s_regCache.get(id);
	}
	
	@Override
	public IResourceStream getResourceStream() 
	{
		ValueMap map = this.getParameters();
		String border = map.getString("border", "full");
		Long id = map.getAsLong("id");
		String indx = map.getString("type", "rgb");
		
		BorderMaker processor = this;
		if (id!=null)
		{
			processor = s_regCache.get(id);
			
			if (processor==null)
				processor = s_regTemporaryCache.get(id);
		}
		return processor.process(border, indx.equals("indexed"));
	}

	protected String getUniqueName(String border, boolean indexedImage)
	{
		return (indexedImage?"indexed":"rgb")+"-"+border;
	}

	synchronized public IResourceStream process(String border, boolean indexedImage)
	{
		String filename = getUniqueName(border, indexedImage);
		boolean debug = false;
		
		if (border.equals("debug"))
		{
			border = "full";
			debug = true;
		}
		
		File result = m_cache.get(filename);
		if (result != null)
		{
			return new FileResourceStream(result);
		}
		
		ImageMap map = getImageMap();
		
		if (indexedImage)
		{
			BufferedImage img = createIndexedImage(map.getZone(border));
			File f = createFile(img, "gif", filename);
			m_cache.put(filename, f);
			return new FileResourceStream(f);
		}
		else
		{
			BufferedImage img = createImage(map.getZone(border));
			
			if (debug)
				debugMap(img, map);
			
			File f = createFile(img, "png", filename);
			
			m_cache.put(filename, f);
			return new FileResourceStream(f);			
		}
	
	}
	
	private void debugMap(BufferedImage img, ImageMap map)
	{
		Graphics2D g = img.createGraphics();
		g.setColor(new Color(30,255,255,200));
		
		String[] vars = { "tl", "t", "tr", "r", "br", "b", "bl", "l" };
		for (String s : vars)
		{
			Rectangle r = map.getZone(s);
			g.drawRect(r.x, r.y, r.width, r.height);
		}
		
	}
	
	/**
	 * Default image map is 0,1/3,2/3,3/3 mapping
	 * @return
	 */
	abstract public ImageMap getImageMap();

//	public BufferedImage convertRGBAToIndexed(BufferedImage src) {
//		BufferedImage dest = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_BYTE_INDEXED, createModel(new Color(231,20,189)));
//		Graphics g = dest.getGraphics();
//		g.setColor(new Color(231,20,189));
//		g.fillRect(0, 0, dest.getWidth(), dest.getHeight()); //fill with a hideous color and make it transparent
//		// dest = makeTransparent(dest,0,0);
//		dest.createGraphics().drawImage(src,0,0, null);
//		return dest;
//	}

//	public BufferedImage makeTransparent(BufferedImage image, int x, int y) {
//		ColorModel cm = image.getColorModel();
//		if (!(cm instanceof IndexColorModel))
//			return image; //sorry...
//		IndexColorModel icm = (IndexColorModel) cm;
//		WritableRaster raster = image.getRaster();
//		int pixel = raster.getSample(x, y, 0); //pixel is offset in ICM's palette
//		int size = icm.getMapSize();
//		byte[] reds = new byte[size];
//		byte[] greens = new byte[size];
//		byte[] blues = new byte[size];
//		icm.getReds(reds);
//		icm.getGreens(greens);
//		icm.getBlues(blues);
//		IndexColorModel icm2 = new IndexColorModel(8, size, reds, greens, blues, pixel);
//		icm2 = createModel(new Color(231,20,189));
//		return new BufferedImage(icm2, raster, image.isAlphaPremultiplied(), null);
//	}

	public File createFile(BufferedImage img, String format, String name)
	{
		final String baseTempPath = System.getProperty("java.io.tmpdir");
		File dir = new File(baseTempPath, BorderMaker.class.getCanonicalName());
		File subdir = new File(dir, m_id.toString());
		subdir.mkdirs();
		File result = new File(subdir, name);
		// result.delete();

		try {
			ImageIO.write(img, format, result);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;

	}

	public Dimension getMapSize(String part)
	{
		Rectangle p = getImageMap().getZone(part);
		return new Dimension(p.width, p.height);
	}

	public int getWidth() {
		return m_width;
	}

	public void setWidth(int width) 
	{
		m_width = width;
	}

	public int getHeight() {
		return m_height;
	}

	public void setHeight(int height) {
		m_height = height;
	}

}
