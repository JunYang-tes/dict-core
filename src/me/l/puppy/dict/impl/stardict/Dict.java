package me.l.puppy.dict.impl.stardict;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import me.l.puppy.dict.model.Entity;
import me.l.puppy.util.Parser;

class Dict {
	BufferedInputStream in;
	Parser parser;

	public Dict(InputStream in, Parser parser) {
		this.in = new BufferedInputStream(in);
		this.parser = parser;
		in.mark(0);
	}

	public Entity search(IdxInfo info) {
		if (info != null) {
			try {
				this.in.reset();
				this.in.skip(info.startIdx);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int len = info.length;
			StringBuilder sb = new StringBuilder();
			int b = 0;
			try {
				while (len > 0 && (b = in.read()) != -1) {
					len--;
					sb.append(b);
				}
				if (this.parser == null) {
					Entity en = new Entity();
					en.setWord(info.word);
					en.addParaphrase(sb.toString());
				}
				Entity en = this.parser.toEntity(sb.toString());
				en.setWord(info.word);
				return en;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
}