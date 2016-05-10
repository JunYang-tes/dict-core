package me.l.puppy.dict.impl.stardict;

class IdxInfo implements Comparable<IdxInfo> {
	public String word;
	public long startIdx;
	public int length;

	@Override
	public int compareTo(IdxInfo o) {
		return this.word.compareTo(o.word);
	}

	@Override
	public String toString() {
		return word;
	}
}
