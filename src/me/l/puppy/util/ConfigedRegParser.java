package me.l.puppy.util;

import java.lang.reflect.Field;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Using properties file to specify regular expression
 * 
 * 
 */
public class ConfigedRegParser extends RegParser {
	public ConfigedRegParser(Properties cfg) {
		for (Field f : ConfigedRegParser.class.getFields()) {
			if (f.getType() == Pattern.class) {
				String reg = cfg.getProperty(f.getName());
				if (reg != null) {
					try {
						f.set(this, Pattern.compile(reg));
					} catch (Exception e) {

					}
				}
			}
		}
	}

}
