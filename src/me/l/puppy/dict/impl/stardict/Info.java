/**
 * 
 * Information of an dictionary.
 *
 */
package me.l.puppy.dict.impl.stardict;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

import me.l.puppy.dict.exceptions.DictNotAvaliable;

class Info {
	public String version;
	public int wordCount;
	public int idxFileSize;
	public int idxOffsetBits = 32;
	public String bookName;
	public String author;
	public String email;
	public String description;
	public String date;
	public String sameTypeSequence;

	public Info(InputStream in) {
		java.io.BufferedReader reader = new java.io.BufferedReader(
				new java.io.InputStreamReader(in));
		String line;
		Field[] fields = Info.class.getFields();
		try {
			while ((line = reader.readLine()) != null) {
				String[] pair = line.split("=");
				if (pair.length == 2) {
					for (Field f : fields) {
						if (f.getName().toLowerCase()
								.equals(pair[0].toLowerCase())) {
							try {
								if (f.getType() == int.class) {
									f.set(this, Integer.parseInt(pair[1]));
								} else {
									f.set(this, pair[1]);
								}

							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
							}
						}
					}
				}
			}
		} catch (IOException e) {
			throw new DictNotAvaliable("Can't read information ofdictionary");
		}
	}
}

