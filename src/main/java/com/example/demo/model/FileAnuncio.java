package com.example.demo.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class FileAnuncio {
	
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private String type;

@Lob		
private byte[] data;

public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public String getType() {
	return type;
}

public void setType(String type) {
	this.type = type;
}

public byte[] getData() {
	return data;
}

public void setData(byte[] data) {
	this.data = data;
}




}
