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
package net.kornr.swit.button;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import javax.imageio.ImageIO;
import net.kornr.swit.util.LRUMap;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.markup.html.form.ImageButton;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.util.resource.FileResourceStream;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.value.ValueMap;

/**
 * WebResource that manages the button creation requests. It is responsible for the button cache 
 * and delegates the creation to the ButtonTemplate object registered. 
 * <p>
 * To optimize the performance of the button generation, this object uses 2 cache. The first cache is the normal cache, 
 * used for buttons that are statically defined in your application. This cache provides optimal performances, as the button
 * image is only generated once, the first time it's requested, but it stays forever in memory. Another cache, the temporary cache
 * is used to store button objects that are likely to change during the lifetime of your application, either because the text
 * is dynamically modified (for instance because it's typed by the user, or because it's time-related), or because the button template
 * itself is changing.
 * <p>
 * Note that both cache are implemented using an LRU cache with limited slots. This is to prevent excessive memory consumption, even for the normal cache. 
 * 
 * When an object is not anymore in the cache, it is generated again. You should take care of defining a "normal cache" size that always exceeds the number of static
 * pairs [template, text] that your application uses.  
 * <p>
 * If you're not sure, don't modify the cache sizes: the default normal cache size should be enough for most web applications, and you probably won't need
 * to use the temporary cache.
 * <p> 
 * Both caches store the button generated on the filesystem, in the directory specified in "javax.servlet.context.tempdir". You can force the generated files 
 * clean up by calling the static method cleanUpFiles(). 
 */
public class ButtonResource extends WebResource 
{
	static private long s_counter = System.currentTimeMillis();
	static String s_buttonNameId = "buttons";
	static private File s_tempDir = null;

	static class LRUFileMap extends LRUMap<Long, File>
	{
		public LRUFileMap(int maxsize) {
			super(maxsize);
		}

		@Override
		protected void onRemove(Entry<Long, File> entry) 
		{
			entry.getValue().delete();
		}
	}

	static private Map<Long, ButtonResourceKey> s_cache = new LRUMap<Long,ButtonResourceKey>(1000);
	static private Map<ButtonResourceKey, Long> s_keyCache = new LRUMap<ButtonResourceKey, Long>(1000);
	static private LRUFileMap s_fileCache = new LRUFileMap(5000);

	static private Map<Long, ButtonResourceKey> s_tempCache = new LRUMap<Long,ButtonResourceKey>(1000);
	static private Map<ButtonResourceKey, Long> s_tempKeyCache = new LRUMap<ButtonResourceKey, Long>(1000);
	static private LRUFileMap s_tempFileCache = new LRUFileMap(1000);

	public ButtonResource()
	{
	}

	synchronized static private Long getUniqueId(ButtonResourceKey key)
	{
		Long keyid = s_keyCache.get(key);
		if (keyid == null)
		{
			keyid = s_counter++;
			key.setId(keyid);
			s_keyCache.put(key, keyid);
			s_cache.put(keyid, key);
		}
		return keyid;
	}

	/**
	 * Creates a Wicket Image object that is bound to the given button generator and text. 
	 * This method registers the template and the text in the normal cache, and should only be used for
	 * buttons which template and text are not dynamically created by your application (because it stays in cache for
	 * all the life of your application).
	 * <p>
	 * This is the normal method to call if you're using it in a web application with a static button.
	 * 
	 * @param id the wicket:id of the image 
	 * @param template the button generator to use
	 * @param text the text of the button
	 * @return an Image
	 */
	static public org.apache.wicket.markup.html.image.Image getImage(String id, ButtonTemplate template, String text)
	{
		return new org.apache.wicket.markup.html.image.Image(id, ButtonResource.getReference(), ButtonResource.getValueMap(template, text));
	}

	/**
	 * Creates a Wicket image button in the normal cache. Like getImage(), but returns an ImageButton. 
	 * @param id
	 * @param template
	 * @param text
	 * @return
	 */
	static ImageButton getImageButton(String id, ButtonTemplate template, String text)
	{
		return new ImageButton(id, ButtonResource.getReference(), ButtonResource.getValueMap(template, text));
	}

	/**
	 * Creates a Wicket Image object that is bound to the given button generator and text. 
	 * This method registers the template and the text in the temporary cache. This method should be used if either your template or
	 * text is dynamically changed at runtime: it is stored in a temporary LRU cache, and will be discarded from memory when the cache limit is 
	 * reached.
	 * 
	 * @param id the wicket:id of the image 
	 * @param template the button generator to use
	 * @param text the text of the button
	 * @return an Image
	 */
	static public org.apache.wicket.markup.html.image.Image getTemporaryImage(String id, ButtonTemplate template, String text)
	{
		return new org.apache.wicket.markup.html.image.Image(id, ButtonResource.getReference(), ButtonResource.getTemporaryValueMap(template, text, false));
	}
	
	synchronized static private Long getTemporaryId(ButtonResourceKey key)
	{
		Long keyid = s_tempKeyCache.get(key);
		if (keyid == null)
		{
			keyid = s_counter++;
			key.setId(keyid);
			s_tempKeyCache.put(key, keyid);
			s_tempCache.put(keyid, key);
		}

		return keyid;
	}

	/**
	 * Provides a ResourceReference for a ButtonResource Object. Note that this reference is not enough to reference a
	 * button, you must also use a ValueMap parameter, as provided by getValueMap().
	 * 
	 * @return a ResourceReference for a button
	 */
	static public ResourceReference getReference()
	{
		return new ResourceReference(ButtonResource.class, "buttons") {
			@Override
			protected Resource newResource() 
			{
				return new ButtonResource();
			}
		};
	}

	/**
	 * Return a ValueMap to associate to a ButtonResource ResourceReference.
	 * This methods registers the pair [template, text] if it it not already in the normal cache.
	 * @param template
	 * @param text
	 * @return
	 */
	static public ValueMap getValueMap(ButtonTemplate template, String text)
	{
		Long id = getUniqueId(new ButtonResourceKey(template, text));
		ValueMap map = new ValueMap();
		map.put("id", id.toString());
		return map;
	}

	/**
	 * Return a ValueMap for a pair [template, text]. The pair is stored in the temporary cache.
	 * 
	 * @param template the ButtonTemplate object to use
	 * @param text the text of the button
	 * @param download true if the resource should be sent to the browser as an attachment, false (the default) to send it normally as an inline image. 
	 * @return A ValueMap to use with a ButtonResource ResourceReference 
	 */
	static public ValueMap getTemporaryValueMap(ButtonTemplate template, String text, boolean download)
	{
		return getTemporaryValueMap(template, text, download, null);
	}
	
	/**
	 * Return a ValueMap for a pair [template, text]. The pair is stored in the temporary cache.
	 * 
	 * @param template the ButtonTemplate object to use
	 * @param text the text of the button
	 * @param download true if the resource should be sent to the browser as an attachment, false (the default) to send it normally as an inline image. 
	 * @param filename if download is true, this specified the filename under which the button image should be sent to the web browser. Can be null, for a default value.
	 * @return A ValueMap to use with a ButtonResource ResourceReference 
	 */
	static public ValueMap getTemporaryValueMap(ButtonTemplate template, String text, boolean download, String filename)
	{
		Long id = getTemporaryId(new ButtonResourceKey(template, text));
		ValueMap map = new ValueMap();
		map.put("id", id.toString());
		if (download)
			map.put("download", "please");
		if (download && filename!=null)
			map.put("filename", filename);
		return map;
	}


	@Override
	public IResourceStream getResourceStream() 
	{
		ValueMap map = this.getParameters();
		long id = map.getLong("id", -1);

		if (id>-1)
		{
			
			File cachedfile = s_fileCache.get(id);
			if (cachedfile == null)
				cachedfile = s_tempFileCache.get(id);

			if (cachedfile != null && cachedfile.exists())
			{
				return new FileResourceStream(cachedfile);
			}

			boolean temporary = false;

			ButtonResourceKey k = s_cache.get(id);
			if (k == null)
			{
				k = s_tempCache.get(id);
				temporary = true;
			}

			File f = createImageFile(k);
			if (temporary)
				s_tempFileCache.put(id,f);
			else
				s_fileCache.put(id, f);
			
			return new FileResourceStream(f);
		}
		// TODO Auto-generated method stub
		return null;
	}

	private File createImageFile(ButtonResourceKey k)
	{
		BufferedImage img = k.getTemplate().getImage(k.getText());
		File dir = getTempDir();
		File file = new File(dir, Long.toHexString(k.getId())+".png");
		try {
			ImageIO.write(img, "png", file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	static private File getTempDir()
	{
		if (s_tempDir == null)
		{
			try {
				File tempdir = (File)WebApplication.get().getServletContext().getAttribute("javax.servlet.context.tempdir");
				if (tempdir == null)
				{
					tempdir = new File(System.getProperty("java.io.tmpdir"));
				}
				File dir = new File(tempdir, ButtonResource.class.getCanonicalName()); 
				dir.mkdirs();
				
				// We don't care for a race condition happening here, because even if it happens, it won't hurt.
				s_tempDir = dir;
			} catch (Exception exc)
			{
				exc.printStackTrace();
			}
		}

		return s_tempDir;
	}

	static public void cleanUpFiles()
	{
		File dir = getTempDir();
		if (dir != null)
		{
			for (File f: dir.listFiles())
			{
				f.delete();
			}
		}
	}

	@Override
	protected void setHeaders(WebResponse response) 
	{
		ValueMap map = this.getParameters();
		String download = map.getString("download", null);
		if (download != null)
		{
			String name = map.getString("filename", "image");
			response.getHttpServletResponse().addHeader("content-disposition","attachment; filename="+name+".png");
			response.setContentType("application/octet-stream");
		}
		super.setHeaders(response);
	}

}
