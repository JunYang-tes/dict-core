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
import java.util.List;
import java.util.ArrayList;

import me.l.puppy.dict.core.CachedDict;
import me.l.puppy.dict.exceptions.DictNotAvaliable;
import me.l.puppy.dict.model.DictInfo;
import me.l.puppy.dict.model.Entity;
import me.l.puppy.util.ConfigedRegParser;
import me.l.puppy.util.Parser;
import me.l.puppy.dict.core.SearchStrategy;
import me.l.puppy.dict.impl.stardict.IdxSearcherSeq;

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
			if(path2info == null || path2idx == null || path2dict ==null)
				throw new DictNotAvaliable("Dictionary is not complete");
			info = new Info(new FileInputStream(path2info));
			idxReader = new IdxReader(info, new FileInputStream(path2idx));
			dict = new Dict(new RandomAccessFile(path2dict, "r"), parser);

		} catch (FileNotFoundException e) {
			throw new DictNotAvaliable("Dictionary is not complete");
		}
	}

	private void load() {
		this.idxSeacher = new IdxSearcherKeyTree(idxReader);
		//this.idxSeacher=new IdxSearcherSeq(idxReader);
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
	protected List<Entity> search_(String word,SearchStrategy strategy,int maxResults){
		if(!loaded)
			this.load();
		List<IdxInfo> infos = this.idxSeacher.search(word,strategy,maxResults);
		List<Entity> entities=new ArrayList<Entity>();
		for(IdxInfo info:infos){
			Entity en=this.dict.search(info);
			if(en!=null){
				entities.add(en);
			}
		}
		return entities;
	}

	@Override
	public DictInfo getDictInfo() {
		DictInfo info = new DictInfo("StarDict", this.info.author,
				this.info.bookName, this.info.description, this.info.version,
				this.info.email);
		return info;
	}
}
