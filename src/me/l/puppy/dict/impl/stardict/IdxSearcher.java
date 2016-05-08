package me.l.puppy.dict.impl.stardict;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Idx searcher
 * 
 * 
 */
class IdxSearcher {
	List<IdxInfo> idxInfos;

	public IdxSearcher(IdxReader reader) {
		idxInfos = new ArrayList<IdxInfo>();
		IdxInfo info;
		while ((info = reader.next()) != null) {
			idxInfos.add(info);
		}
	}

	public IdxInfo search(String string) {
		IdxInfo idx = new IdxInfo();
		idx.word = string;
		IdxInfo tmp;
		int l = 0, h = idxInfos.size() - 1;
		int m = (h + l) / 2;
		int comp = 0;
		while (l <= h) {
			m = (h + l) / 2;
			tmp = idxInfos.get(m);
			comp = idx.compareTo(tmp);
			if (comp == 0) {
				return tmp;
			} else if (comp > 0) {
				l = m + 1;
			} else {
				h = m - 1;
			}
		}
		return null;
	}

}