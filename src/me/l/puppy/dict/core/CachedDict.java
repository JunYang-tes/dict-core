package me.l.puppy.dict.core;
import me.l.puppy.dict.model.*;
public abstract class CachedDict implements Dict {
    Cache cache;
    public CachedDict(){
        cache=new Cache();
    }
    public Entity search(String word){
        Entity wordEntity = cache.get(word);
        if (wordEntity!=null) {
            return wordEntity;
        } else {        	
            wordEntity=search_(word);
            cache.add(wordEntity);
            return wordEntity;
        }
    }
    protected abstract Entity  search_(String word);
}
class Cache{
    public Cache() {
		// TODO Auto-generated constructor stub
	}
	public Entity get(String word){
        return null;
    }
    public void add(Entity word){
        if(word!=null){
        	
        }
    }
}