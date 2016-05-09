package me.l.puppy.dict.core;
import me.l.puppy.dict.model.*;
public abstract class Cache {
   protected int capacity;
   public Cache(int capacity){
        this.capacity=capacity;
   }
   public abstract Entity get(String word);
   public abstract void add(Entity entity);
}
