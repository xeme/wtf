package com.wtf.client;

public class Pair <T, U>
{
	private final T first;
	private final U second;

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
} 
