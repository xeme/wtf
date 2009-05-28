package com.wtf.client;

import java.util.Date;

public class Post {
	private String _author;
	private String _content;
	private Date _date;
	
	public Post(String author, String content, Date date) {
		_author = author;
		_content = content;
		_date = date;
	}

	public String getAuthor() {
		return _author;
	}

	public String getContent() {
		return _content;
	}

	public Date getDate() {
		return _date;
	}
}
