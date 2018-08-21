package com.github.brantpastore.util;

public enum Messages {
	OWNER ("Brant Pastore");
	
	private final String msg;
	
	private Messages(String s) {
		msg = s;
	}
	
	public String toString() {
		return this.msg;
	}
}
