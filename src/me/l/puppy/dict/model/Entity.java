package me.l.puppy.dict.model;
/**
 * An entity for one searchs
 * @author Qianba
 *
 */
public class Entity {
    String word;
    String pronounce;
    List<String> paraphrase;
    List<String> synonym;
    List<String> antonym;

    public Entity(){
        paraphrase=new ArrayList<String>();
        synonym=new ArrayList<String>();
        antonym=new ArrayList<String>();
    }
    public String getWord(){
        return this.word;
    }
    public String getPronounce(){
        return pronounce;
    }
    public String [] getParaphrase(){
        return paraphrase.toArray();
    }
    public String [] getSynonym(){
        return synonym.toArray();
    }
    public String [] getAntonym(){
        return antonym.toArray();
    }
    public void addParaphrase(String paraphrase){
        this.paraphrase.add(paraphrase);
    }

}
