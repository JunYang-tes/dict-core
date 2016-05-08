package me.l.puppy.dict.impl.stardict;

import java.io.File;
import me.l.puppy.dict.impl.stardict.StarDict;

public class Cfg {
	public static void InitStarDict(String path) {
		if(!path.endsWith(File.separator)){
			path+=File.separator;
		}
		File f = new File(path);
		String[] list = f.list();
		if (list != null)
			for (String p : list) {
				if (new File(path+p).isDirectory()) {
					StarDict d = new StarDict(path+p);
					me.l.puppy.dict.SimpleFactory.register("stardict."
							+ d.info.bookName, d);
				}
			}
	}
}
