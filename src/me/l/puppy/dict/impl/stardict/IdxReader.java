package me.l.puppy.dict.impl.stardict;

import java.io.IOException;
import java.io.InputStream;

import me.l.puppy.util.Convertor;

/**
 * Reader for *.idx file
 * Entities are put one by one in *.idx file.
 * An entity includes three fields,word_str,startIdx and length
 */
class IdxReader {
	int readStr = 0, readIdx = 1, readLen = 2, idxOfreadIdx = 0,
			idxOfreadLen = 0;
	int state;
	StringBuilder sb;
	byte[] idxArray;
	byte[] lenArray;
	InputStream inputStream;

	public IdxReader(Info info, InputStream in) {
		inputStream = in;
		state = readStr;
		// offset in file can be long
		idxArray = new byte[info.idxOffsetBits / 8];
		lenArray = new byte[4];
	}

	public IdxInfo next() {
		int b = 0;
		state = readStr;
		idxOfreadIdx = 0;
		idxOfreadLen = 0;
		sb=new StringBuilder();
		
		try {
			while ((b = inputStream.read()) != -1) {
				switch (state) {
				case 0:// read string
					if (b != 0) {
						sb.append((char) b);
					} else {
						state = readIdx;
					}
					break;
				case 1:// read idx which is an integer
					if (idxOfreadIdx < idxArray.length-1) {
						idxArray[idxOfreadIdx++] = (byte) b;
					} else {
						idxArray[idxOfreadIdx]=(byte)b;
						state = readLen;
						
					}
					break;
				case 2:// read length which is an integer
					if (idxOfreadLen < lenArray.length-1) {
						lenArray[idxOfreadLen++] = (byte) b;
					} else {
						lenArray[idxOfreadLen++] = (byte) b;
						IdxInfo info = new IdxInfo();
						info.word = sb.toString();
						info.startIdx = Convertor.n2int(idxArray);
						info.length = (int) Convertor.n2int(lenArray);
						return info;
					}
					break;
				}
			}
		} catch (IOException e) {

		}
		return null;
	}
}

