package me.l.puppy.dict.impl.stardict;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import me.l.puppy.dict.core.SearchStrategy;

public abstract class IdxSearcher {
	public IdxSearcher(IdxReader reader){

	}
	public abstract IdxInfo search(String string);
	public abstract List<IdxInfo> search(String string,SearchStrategy strategy,int maxResults);
}