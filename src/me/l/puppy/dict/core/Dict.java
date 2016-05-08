package me.l.puppy.dict.core;
import me.l.puppy.dict.model.*;

public interface Dict {
    Entity search(String word);
    DictInfo getDictInfo();
}
