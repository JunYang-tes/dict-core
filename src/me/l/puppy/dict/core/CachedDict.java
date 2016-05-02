package me.l.puppy.dict;
import me.l.puppy.model.Entity;
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
            Entity wordEntity=new Entity();
            cache.add(wordEntity);
            wordEntity.setWord(word);
            search(wordEntity);
            return wordEntity;
        }
    }
    protected abstract void search(Entity wordEntity);
}
private class Cache{
    public Entity get(String word){
        return null;
    }
    public void add(Entity word){
        
    }
}