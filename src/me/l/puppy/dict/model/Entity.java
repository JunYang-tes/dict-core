package me.l.puppy.dict.model;

import java.util.ArrayList;
import java.util.List;

/**
 * An entity for one search
 * 
 * @author Qianba
 * 
 */
public class Entity {
	String word;
	List<String> pronounce;
	List<String> paraphrase;
	List<String> synonym;
	List<String> antonym;

	public Entity() {
		pronounce = new ArrayList<String>();
		paraphrase = new ArrayList<String>();
		synonym = new ArrayList<String>();
		antonym = new ArrayList<String>();
	}

	public String getWord() {
		return this.word;
	}
	
	private String [] toArray(List<String> list){
		String []ret=new String[list.size()];
		int idx=0;
		for(String s : list){
			ret[idx++]=s;
		}
		return ret;
	}

	public String[] getPronounce() {
		return toArray(pronounce);
	}

	public String[] getParaphrase() {
		return toArray(paraphrase);
	}

	public String[] getSynonym() {
		return toArray(this.synonym);
	}

	public String[] getAntonym() {
		return toArray(this.antonym);
	}

	public void addParaphrase(String paraphrase) {
		this.paraphrase.add(paraphrase);
	}

	public void addPronounce(String pronounce) {
		this.pronounce.add(pronounce);
	}

	public void addSynonym(String synonym) {
		this.synonym.add(synonym);
	}

	public void addAntonym(String antonym) {
		this.antonym.add(antonym);
	}

	public void setWord(String word) {
		this.word = word;
	}

}
