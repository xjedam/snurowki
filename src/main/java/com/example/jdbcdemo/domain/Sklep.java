package com.example.jdbcdemo.domain;

public class Sklep {
	
	private Long id;
	
	private String nazwa;

	public Sklep(String nazwa) {
		this.nazwa = nazwa;
	}
	
	public Sklep() {
		
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNazwa() {
		return nazwa;
	}

	public void setNazwa(String nazwa) {
		this.nazwa = nazwa;
	}
	
	

}
