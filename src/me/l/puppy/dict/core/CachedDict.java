package me.l.puppy.dict.core;
import java.util.HashMap;
import java.util.List;
import me.l.puppy.dict.model.*;
import me.l.puppy.util.Heap;
public abstract class CachedDict implements Dict {
    Cache cache;
    public CachedDict(){
        cache=new LinearCache(100);
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
    public List<Entity> search(String word,SearchStrategy strategy,int maxResults){
        return this.search_(word,strategy,maxResults);
    }
    protected abstract Entity  search_(String word);
    protected abstract List<Entity> search_(String word,SearchStrategy strategy,int maxResults);
}

class HeapCache extends Cache {
    Heap<HeapItem> searchFrq;
    HashMap<String ,MyInteger> cacheIdx;//fast fint where the word loated in heap
    public HeapCache(int capacity){
        super(capacity);
        cacheIdx = new HashMap<String,MyInteger>();
        /*searchFrq=new Heap<HeapItem>(capacity,(idx,item)->{
            cacheIdx.get(item.word.getWord()).value++;
        });*/
    }
    public Entity get(String word){
        MyInteger idx=cacheIdx.get(word);
        if(idx!=null){
            HeapItem ret= searchFrq.get(idx.value);
            ret.searchTimes++;
            return ret.word;
        }
        return null;
    }
    public void add(Entity word){
        if(cacheIdx.size()==capacity){
            Entity root=searchFrq.root().word;
            cacheIdx.remove(root.getWord());
        }
        HeapItem item=new HeapItem(word,0);
        int idx=searchFrq.add(item);
        cacheIdx.put(word.getWord(),new MyInteger(idx));
    }
    class HeapItem implements Comparable<HeapItem>{
        public Entity word;
        public int searchTimes;
        public HeapItem(Entity word,int searchTimes){
            this.word=word;
            this.searchTimes=searchTimes;
        }
        @Override
        public int compareTo(HeapItem to){
            return this.searchTimes - to.searchTimes;
        }
    }
    class MyInteger{
        public int value;
        public MyInteger(int value){
            this.value=value;
        }
    }
}
class LinearCache extends Cache{
    
    public LinearCache(int capacity){
        super(capacity);
    }
    public void add(Entity word){}
    public Entity get(String word){return null;}
}