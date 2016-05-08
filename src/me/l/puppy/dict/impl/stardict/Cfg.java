package me.l.puppy.dict.impl.stardict;

import java.io.File;

import me.l.puppy.dict.exceptions.DictNotAvaliable;
import me.l.puppy.dict.impl.stardict.StarDict;
import me.l.puppy.logger.Logger;
import me.l.puppy.util.Convertor;

public class Cfg {
	public static void InitStarDict(String path) {
		if (!path.endsWith(File.separator)) {
			path += File.separator;
		}
		File f = new File(path);
		String[] list = f.list();
		if (list != null) {
			for (String p : list) {
				if (new File(path + p).isDirectory()) {
					try {
						StarDict d = new StarDict(path + p);
						me.l.puppy.dict.SimpleFactory.register("stardict."
								+ d.info.bookName, d);
					} catch (DictNotAvaliable e) {
						Logger.W(e.getMessage(),
								Convertor.callStack(e.getStackTrace()));
					}
				}
			}
		}
	}
}
