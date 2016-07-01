package com.lishate.message;

public class MessageSeqFactory {

	private static short seq = 0;
	private static Object obj = new Object();
	private static final short maxSeq = 0x7FFF;
	
	public static short GetNextSeq(){
		synchronized(obj){
			if(seq >= maxSeq || seq < 0){
				seq = 0;
			}
			else{
				seq = (short) (seq + 1);
			}
			return seq;
		}
	}
}
