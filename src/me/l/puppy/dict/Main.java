package me.l.puppy.dict;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Scanner;

import me.l.puppy.dict.core.Dict;
import me.l.puppy.dict.model.Entity;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.out.println("Need dictionary path");
		} else {
			if(hasOp(args,"--help") || hasOp(args,"-h")){
				printUsage();
				return;
			}
			String path = getOp(args, "--dpath");
			if (path == null) {
				System.out
						.println("You need using --dpath to specify where the dictonary located");
				return;
			}
			if(new File(path).isFile()){
				System.out.println("The argument of --dpath should be a path to dirctory but got a file");
				return;
			}
			me.l.puppy.dict.impl.stardict.Cfg.InitStarDict(path);
			String name = getOp(args, "--name");
			if (hasOp(args, "--show-dict")) {
				String[] dicts = SimpleFactory.getNames();
				for (String p : dicts) {
					System.out.println(p);
				}
				return;
			}
			if (name == null || (name = name.trim()).length() == 0) {
				System.out
						.println("Using --name specify which one you want to use");
				return;
			} else {
				System.out.println(name);

				Dict dict = SimpleFactory.getDict(name);
				if (dict == null) {
					System.out.println("No such dictionary");
					return;
				}
				loop(SimpleFactory.getDict(name));
			}

		}
	}

	private static void printUsage() {
		System.out.println("Usage:");
		System.out.println("progam [-h|--help] --dpath <path to dictory> <-show--dict|--name <dict-name>>");
		System.out.println("--dpath\tspecify where to locate dictionaries");
		System.out.println("--name\tspecify where one you want to use this time");
		System.out.println("--show-dict\tshow all avaiable dictionaries");
	}

	static boolean hasOp(String[] args, String op) {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals(op)) {
				return true;
			}
		}
		return false;
	}

	static String getOp(String[] args, String op) {
		for (int i = 0; i < args.length - 1; i++) {
			if (args[i].equals(op)) {
				return args[i + 1];
			}
		}
		return null;
	}

	static void loop(Dict dict) {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			String word = scanner.nextLine();
			word = word.trim();
			if (word.length() > 0)
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
