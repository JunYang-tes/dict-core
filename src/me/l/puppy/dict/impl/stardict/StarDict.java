/**
 * @author Qianba
 */
package me.l.puppy.dict.impl.stardict;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Properties;

import me.l.puppy.dict.core.CachedDict;
import me.l.puppy.dict.model.DictInfo;
import me.l.puppy.dict.model.Entity;
import me.l.puppy.util.ConfigedRegParser;
import me.l.puppy.util.Parser;

public class StarDict extends CachedDict {
	Dict dict;
	Info info;
	IdxSearcher idxSeacher;
	IdxReader idxReader;
	boolean loaded = false;

	/**
	 * 
	 * @param path
	 *            path to a dictionary.It is a directory including
	 *            *.idx,*.dict,*.info,*.pattern(optical)
	 */
	public StarDict(String path) {
		String path2idx = null;
		String path2dict = null;
		String path2info = null;
		idxSeacher = null;
		idxReader = null;
		Parser parser = null;
		dict = null;
		info = null;
		if (!path.endsWith(File.separator))
			path += File.separator;

		File p = new File(path);
		String[] list = p.list();
		if (list != null)
			for (String file : list) {
				if (file.endsWith(".idx")) {
					// if path2idx is not null ,then this path is not a valid
					// dictionary
					path2idx = path + file;
				} else if (file.endsWith(".ifo")) {
					path2info = path + file;
				} else if (file.endsWith(".dict")) {
					path2dict = path + file;
				} else if (file.endsWith("pattern")) {
					Properties prop = new Properties();
					try {
						prop.load(new FileInputStream(path + file));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					parser = new ConfigedRegParser(prop);
				}
			}
		try {
			if (path2info != null)
				info = new Info(new FileInputStream(path2info));
			// TODO else error handle
			if (path2idx != null)
				idxReader = new IdxReader(info, new FileInputStream(path2idx));
			if (path2dict != null)
				dict = new Dict(new RandomAccessFile(path2dict, "r"), parser);

		} catch (FileNotFoundException e) {
		}
	}

	private void load() {
		this.idxSeacher = new IdxSearcher(idxReader);
		loaded = true;
	}

	@Override
	protected Entity search_(String word) {
		if (!loaded)
			this.load();
		IdxInfo info = this.idxSeacher.search(word);
		return this.dict.search(info);
	}

	@Override
	public DictInfo getDictInfo() {
		DictInfo info = new DictInfo("StarDict", this.info.author,
				this.info.bookName, this.info.description, this.info.version,
				this.info.email);
		return info;
	}
}
