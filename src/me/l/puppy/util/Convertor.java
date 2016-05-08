package me.l.puppy.util;

public class Convertor {
	public static long n2int(byte []arr){
		long ret=0;
		
		for(int i=0;i<arr.length;i++){
			ret=((ret<<(8))|((byte)arr[i])&0xff);
		}
		return ret;
	}
}
