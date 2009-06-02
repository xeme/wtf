package com.wtf.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Pair <T, U> implements IsSerializable {
	private T first;
	private U second;
	
	protected Pair() {}

	public Pair(T f, U s)	{
		this.first = f;
		this.second = s;
	}

	public T first()	{
		return first;
	}
	public U second() {
		return second;
	}
	
	public void setFirst(T f)	{
		first = f;
	}
	public void setSecond(U s) {
		second = s;
	}
} 
