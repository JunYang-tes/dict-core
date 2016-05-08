package me.l.puppy.util;

public class Convertor {
	public static long n2int(byte[] arr) {
		long ret = 0;

		for (int i = 0; i < arr.length; i++) {
			ret = ((ret << (8)) | ((byte) arr[i]) & 0xff);
		}
		return ret;
	}

	public static String[] callStack(StackTraceElement[] st) {
		String[] ret = new String[st.length];
		int idx = 0;
		for (StackTraceElement t : st) {
			ret[idx++] = t.getMethodName() + "@" + t.getClassName();
		}
		return ret;
	}
}
