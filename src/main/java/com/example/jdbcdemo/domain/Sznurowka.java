package com.example.jdbcdemo.domain;

public class Sznurowka {
	
	private Long id = null;
	
	private String producent;
	
	private int dlugosc;
	
	private String kolor;
	
	private int grubosc;
	
	private Sklep sklep = null;
	
	public Sznurowka(String producent, int dlugosc, String kolor, int grubosc) {
		super();
		this.producent = producent;
		this.dlugosc = dlugosc;
		this.kolor = kolor;
		this.grubosc = grubosc;
	}
	
	public Sznurowka() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getProducent() {
		return producent;
	}

	public void setProducent(String producent) {
		this.producent = producent;
	}

	public int getDlugosc() {
		return dlugosc;
	}

	public void setDlugosc(int dlugosc) {
		this.dlugosc = dlugosc;
	}

	public String getKolor() {
		return kolor;
	}

	public void setKolor(String kolor) {
		this.kolor = kolor;
	}

	public int getGrubosc() {
		return grubosc;
	}

	public void setGrubosc(int grubosc) {
		this.grubosc = grubosc;
	}

	public Sklep getSklep() {
		return sklep;
	}

	public void setSklep(Sklep sklep) {
		this.sklep = sklep;
	}

}
