package me.l.puppy.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

import me.l.puppy.dict.model.Entity;

public class RegParser implements Parser {

	protected Pattern pronounce;
	protected Pattern paraphrase;
	protected Pattern synonym;
	protected Pattern antonym;

	public RegParser() {

	}

	public RegParser(Pattern pronounce, Pattern paraphrase, Pattern synonym,
			Pattern antonym) {
		this.pronounce = pronounce;
		this.paraphrase = paraphrase;
		this.synonym = synonym;
		this.antonym = antonym;
	}

	@Override
	public Entity toEntity(String string) {
		Entity ret = new Entity();
		/**
		 * This is a striped thing that we can not using callback in functions'
		 * parameters. I don't want to use interface or reflector for this
		 * simple task. Using lambda in java 8 can simplify the following codes.
		 * **/
		Matcher matcher = pronounce.matcher(string);
		while (matcher.find()) {
			ret.addPronounce(matcher.group());
		}
		matcher = paraphrase.matcher(string);
		while (matcher.find()) {
			ret.addParaphrase(matcher.group());
		}
		matcher = synonym.matcher(string);
		while (matcher.find()) {
			ret.addSynonym(matcher.group());
		}
		matcher = antonym.matcher(string);
		while (matcher.find()) {
			ret.addAntonym(matcher.group());
		}
		return ret;
	}

}
