package me.l.puppy.dict.impl.stardict;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import me.l.puppy.dict.model.Entity;
import me.l.puppy.util.Parser;

class Dict {
	BufferedInputStream in;
	Parser parser;
	Charset charset = Charset.forName("UTF-8");
	int pushbacked = -1;
	UTF8Reader reader;
	public Dict(InputStream in, Parser parser) {
		this.in = new BufferedInputStream(in);
		this.parser = parser;
		this.in.mark(0);
		reader=new UTF8Reader();
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
			StringBuilder sb = new StringBuilder();

			int bufLen = 10;
			byte[] buffer = new byte[info.length > bufLen ? bufLen
					: info.length];
			int totalRead = 0;
			int read = 0;
			int offset = 0;
			while (totalRead < info.length) {
				read = 0;
				offset = 0;
				while ((read = this.reader.readUTF8(buffer, offset)) != 0) {
					offset += read;
					totalRead += read;
				}
				sb.append(this.charset.decode(ByteBuffer
						.wrap(buffer, 0, offset)));
			}

			if (this.parser == null) {
				Entity en = new Entity();
				en.setWord(info.word);
				en.addParaphrase(sb.toString());
				return en;
			}
			Entity en = this.parser.toEntity(sb.toString());
			en.setWord(info.word);
			return en;

		}
		return null;
	}
	
	class UTF8Reader{
		int firstByte = 0;
		int pushbacked=-1;
		
		int[] masks = new int[] { 0xE0, 0xF0, 0xF8, 0xFC, 0xFE };
		int[] check = new int[] { 0xC0, 0xE0, 0xF0, 0xF8, 0xFC };
		public int readUTF8(byte[] buff,int offset){
			if (offset >= buff.length) {
				return 0;
			}
			int readCounter = 0;
			int state = firstByte;
			int otherByteCnt = 2;
			int b=-1;
			for (int i = offset; i < buff.length; i++) {
				readCounter++;
				try {
					if (pushbacked != -1) {
						b = pushbacked;
						pushbacked = -1;
					} else {
						b = Dict.this.in.read();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (b != -1) {
					if (state == firstByte) { // the first byte
						otherByteCnt = 0;
						for (int j = 0; j < masks.length; j++) {
							if ((b & masks[j]) == check[j]) {
								otherByteCnt = j + 1;
								break;
							}
						}
						state = otherByteCnt;// 0 is also firstByte ,1 2 3 4 5 6 is
												// otherBytes
						if (i + otherByteCnt + 1 >= buff.length) {
							pushbacked = b;
							return readCounter - 1;
						}
						buff[i] = (byte) b;
					} else {
						// the other bytes
						if (otherByteCnt > 0) {
							buff[i] = (byte) b;
							otherByteCnt--;
						} else {
							state = firstByte;
							pushbacked = b;
							i--;
							readCounter--;
						}
					}
				} else {
					return readCounter - 1;
				}
			}
			return readCounter;
		}
	}

	public int readUTF8(byte[] buff, int offset) {
		if (offset >= buff.length) {
			return 0;
		}
		int readCounter = 0;
		int b = 0;
		int firstByte = 0;
		int otherByteCnt = 2;
		int state = firstByte;
		int[] masks = new int[] { 0xE0, 0xF0, 0xF8, 0xFC, 0xFE };
		int[] check = new int[] { 0xC0, 0xE0, 0xF0, 0xF8, 0xFC };

		for (int i = offset; i < buff.length; i++) {
			readCounter++;
			try {
				if (pushbacked != -1) {
					b = pushbacked;
					pushbacked = -1;
				} else {
					b = this.in.read();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (b != -1) {
				if (state == firstByte) { // the first byte
					otherByteCnt = 0;
					for (int j = 0; j < masks.length; j++) {
						if ((b & masks[j]) == check[j]) {
							otherByteCnt = j + 1;
							break;
							
						}
					}
					state = otherByteCnt;// 0 is also firstByte ,1 2 3 4 5 6 is
											// otherBytes
					if (i + otherByteCnt + 1 >= buff.length) {
						pushbacked = b;
						return readCounter - 1;
					}
					buff[i] = (byte) b;
				} else {
					// the other bytes
					if (otherByteCnt > 0) {
						buff[i] = (byte) b;
						otherByteCnt--;
					} else {
						state = firstByte;
						pushbacked = b;
						i--;
						readCounter--;
					}
				}
			} else {
				return readCounter - 1;
			}
		}

		return readCounter;
	}
}