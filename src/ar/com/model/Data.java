package ar.com.model;

public class Data {
	
	final private String aString;
	final private int aInt;
	final private Integer aInteger;

	public Data(String aString, int aInt, Integer aInteger) {
		this.aString = aString;
		this.aInt = aInt;
		this.aInteger = aInteger;
	}

	

	@Override
	public String toString() {
		return "Data [aString=" + aString + ", aInt=" + aInt + ", aInteger="
				+ aInteger + "]";
	}

}
