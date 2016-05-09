package me.l.puppy.dict.core;
import me.l.puppy.dict.model.*;
public abstract class CachedDict implements Dict {
    Cache cache;
    public CachedDict(){
        cache=new LinearCache();
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
/*
class Cache{
    int capacity;
    HashMap<String,Entity> cache;
    public Cache(int capacity) {
        this.capacity=capacity;
        cache=new HashMap(capacity+(0.3*capacity));
    }
	public Entity get(String word){
        return null;
    }
    public void add(Entity word){
        if(word!=null){
        	if(cache.containsKey(word.getWord())){
                cache.put(word.getWord(),word);
            }else{
                if(cache.size()<this.capacity)
                    cache.put(word.getWord(),word);
                else{

                }
            }
        }
    }
}*/

class HeapCache extends Cache {
    Heap<HeapItem> searchFrq;
    HashMap<String ,Integer> cacheIdx;//fast fint where the word loated in heap
    public HeapCache(int capacity){
        searchFrq=new Heap<HeapItem>(capacity);
    }
    public Entity get(String word){
        Integer idx=cacheIdx.get(word);
        if(idx!=null){
            HeapItem ret= searchFrq.get(idx.intValue());
            ret.searchTimes++;
            int adjusted=searchFrq.adjust(idx.intValue());
            cacheIdx.put(word,new Integer(adjusted));
            return ret.word;
        }
        return null
    }
    public void add(Entity word){
        if(cacheIdx.size()==capacity){
             Entity root=searchFrq.root();
            cacheIdx.remove(root.getWord());
        }
        int idx=searchFrq.add(word)
        HeapItem item=new HeapItem(word,idx);
        cacheIdx.put(word.getWord(),new Integer(idx));
    }
    class HeapItem implements Comparable<HeapItem>{
        public Entity word;
        public int searchTimes;
    }
}
class LinearCache extends Cache{
    
    public LinearCache(int capacity){

    }
}