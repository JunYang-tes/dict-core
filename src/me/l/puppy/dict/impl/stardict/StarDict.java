/**
 * @author Qianba
 */
package me.l.puppy.dict.impl.stardict;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
		IdxReader idxReader = null;
		Parser parser = null;
		dict = null;
		info = null;
		File p = new File(path);
		for (String file : p.list()) {
			if (file.endsWith(".idx")) {
				// if path2idx is not null ,then this path is not a valid
				// dictionary
				path2idx = file;
			} else if (file.endsWith(".info")) {
				path2info = file;
			} else if (file.endsWith(".dict")) {
				path2dict = file;
			} else if (file.endsWith("pattern")) {
				Properties prop = new Properties();
				try {
					prop.load(new FileInputStream(file));
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
				dict = new Dict(new FileInputStream(path2dict), parser);
			this.idxSeacher = new IdxSearcher(idxReader);

		} catch (FileNotFoundException e) {
		}
	}
	
	@Override
	protected Entity search_(String word) {
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
