package com.wtf.client.dto;

import java.io.Serializable;
import java.util.Date;

public class PostDTO implements Serializable {
  private String author;
	private String content;
	private Date date;
	
	protected PostDTO() {}
	
	public PostDTO(String author, String content, Date date) {
	  this.author = author;
		this.content = content;
		this.date = date;
	}
	
	public void setAuthor(String author) {
    this.author = author;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void setDate(Date date) {
    this.date = date;
  }

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public Date getDate() {
		return date;
	}
}
