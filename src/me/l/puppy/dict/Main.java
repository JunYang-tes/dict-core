package me.l.puppy.dict;

import java.nio.ByteBuffer;
import java.util.Scanner;

import me.l.puppy.dict.core.Dict;
import me.l.puppy.dict.model.Entity;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str="ab我你它";
		ByteBuffer bf= java.nio.charset.Charset.forName("UTF-8").encode(str);
		byte[] bs=new byte[]{97,98,-26,-120,-111,-28,-67,-96};
		System.out.println(java.nio.charset.Charset.forName("UTF-8").decode(ByteBuffer.wrap(bs)));
		
		if (args.length == 0) {
			System.out.println("Need dictionary path");
		} else {
			me.l.puppy.dict.impl.stardict.Cfg.InitStarDict(args[0]);
			String[] dicts = SimpleFactory.getNames();
			if (dicts.length > 0)
				loop(SimpleFactory.getDict(dicts[0]));
			else
				System.out.println("there is no dictionary.");
		}
	}

	static void loop(Dict dict) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String word = scanner.nextLine();
			out(dict.search(word));
		}
	}

	static void out(Entity word) {
		if (word != null) {
			System.out.println(word.getWord());
			for (String p : word.getPronounce()) {
				System.out.print(p + " ");
			}
			for (String p : word.getParaphrase()) {
				System.out.println(p);
			}
		} else {
			System.out.println("Not found");
		}
	}

}
