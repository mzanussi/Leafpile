package com.michaelzanussi.leafpile;

public class SignedUnsigned {
	
	public int signed(int w) {
		if ((w & 0x8000) == 0x8000) {
			return (w-65536);
		} else {
			return w;
		}
	}

	public static void main(String[] args) {
		
		//int[] data = {18636, 14592, 7814, 58533};	// 48cc, 3900, 1E86, E4A5		
		//int[] data = {5002, 25376, 1348, 13978, 57669};	
		//ZSCII frotz = new V3ZSCII();
		//String string = frotz.decode(data);
		//System.out.println("~" + string + "~");

		/*SignedUnsigned su = new SignedUnsigned();
		
		int i = 0;
		System.out.println(i + "= " + su.signed(i));
		System.out.println(i + "(short)= " + ((short)i));
		
		i = 32767;	// 0000 0000 0000 0000 0111 1111 1111 1111
		System.out.println(i + "= " + su.signed(i));	// 32767
		System.out.println(i + "(short)= " + ((short)i));
		
		i = 32768;	// 0000 0000 0000 0000 1000 0000 0000 0000
		System.out.println(i + "= " + su.signed(i));	// -32768
		System.out.println(i + "(short)= " + ((short)i));
		
		i = 32769;	// 0000 0000 0000 0000 1000 0000 0000 0001
		System.out.println(i + "= " + su.signed(i));	// -32767
		System.out.println(i + "(short)= " + ((short)i));
		
		i = 65535;	// 0000 0000 0000 0000 1111 1111 1111 1111
		System.out.println(i + "= " + su.signed(i));	// -1
		System.out.println(i + "(short)= " + ((short)i));
		
		i = -1;	// 
		System.out.println(i + "= " + su.signed(i));	// 
		System.out.println(i + "(short)= " + ((short)i));
		
		i = -2;	// 
		System.out.println(i + "= " + su.signed(i));	// 
		System.out.println(i + "(short)= " + ((short)i));
		
		i = -1000;	// 
		System.out.println(i + "= " + su.signed(i));	// 
		System.out.println(i + "(short)= " + ((short)i));
		
		i = -100000;	// 
		System.out.println(i + "= " + su.signed(i));	// 
		System.out.println(i + "(short)= " + ((short)i));*/
		
	}

}
