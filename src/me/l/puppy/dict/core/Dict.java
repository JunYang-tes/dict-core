package me.l.puppy.dict.core;
import me.l.puppy.dict.model.*;
import java.util.List;

public interface Dict {
    Entity search(String word);
    List<Entity> search(String word,SearchStrategy strategy,int maxResults);
    DictInfo getDictInfo();
}
