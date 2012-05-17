package com.example.jdbcdemo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import com.example.jdbcdemo.domain.Sklep;
import com.example.jdbcdemo.domain.Sznurowka;

public class SznurowkaManagerTest {
	SznurowkaManager sm = new SznurowkaManager(); 
	
	private final static String PRODUCENT1 = "Sznurowex";
	private final static String PRODUCENT2 = "Sznur Corp.";
	private final static String PRODUCENT3 = "Wiezadla Sp. Z.O.O.";
	private final static int DLUGOSC1 = 235;
	private final static int DLUGOSC2 = 322;
	private final static int DLUGOSC3 = 270;
	private final static String KOLOR1 = "Czerwony";
	private final static String KOLOR2 = "Czarny";
	private final static String KOLOR3 = "Bialy";
	private final static int GRUBOSC1 = 5;
	private final static int GRUBOSC2 = 10;
	private final static String SKLEP_NAZWA1 = "U Jadzi";
	private final static String SKLEP_NAZWA2 = "Butex";
	
	@Test
	public void checkConnection() {
		assertNotNull(sm.getConnection());
	}
	
	@Test
	public void addgetTest() {
		Sznurowka s1 = new Sznurowka(PRODUCENT1, DLUGOSC1, KOLOR1, GRUBOSC1);
		Sznurowka s2 = new Sznurowka(PRODUCENT2, DLUGOSC2, KOLOR2, GRUBOSC2);
		Sznurowka s3 = new Sznurowka(PRODUCENT3, DLUGOSC3, KOLOR3, GRUBOSC2);

		assertEquals(1,sm.addSznurowka(s1));
		assertEquals(1,sm.addSznurowka(s2));
		assertEquals(1,sm.addSznurowka(s3));
	
		assertNotNull(s1.getId());
		assertNotNull(s2.getId());
		assertNotNull(s3.getId());

		sm.commitChanges();

		Sznurowka s1db = sm.getSznurowka(s1.getId());
		Sznurowka s2db = sm.getSznurowka(s2.getId());
		Sznurowka s3db = sm.getSznurowka(s3.getId());

		assertEquals(s1.getProducent(), s1db.getProducent());
		assertEquals(s1.getDlugosc(), s1db.getDlugosc());
		assertEquals(s1.getKolor(), s1db.getKolor());
		assertEquals(s1.getGrubosc(), s1db.getGrubosc());

		assertEquals(s2.getProducent(), s2db.getProducent());
		assertEquals(s2.getDlugosc(), s2db.getDlugosc());
		assertEquals(s2.getKolor(), s2db.getKolor());
		assertEquals(s2.getGrubosc(), s2db.getGrubosc());

		assertEquals(s3.getProducent(), s3db.getProducent());
		assertEquals(s3.getDlugosc(), s3db.getDlugosc());
		assertEquals(s3.getKolor(), s3db.getKolor());
		assertEquals(s3.getGrubosc(), s3db.getGrubosc());

		Sklep sk1 = new Sklep(SKLEP_NAZWA1);
		Sklep sk2 = new Sklep(SKLEP_NAZWA2);

		assertEquals(1, sm.addSklep(sk1));
		assertEquals(1, sm.addSklep(sk2));

		assertNotNull(sk1.getId());
		assertNotNull(sk2.getId());

		sm.commitChanges();

	}
	
	@Test
	public void updateTest() {
		Sznurowka s1 = new Sznurowka(PRODUCENT1, DLUGOSC1, KOLOR1, GRUBOSC1);
		sm.addSznurowka(s1);
		
		s1.setKolor(KOLOR2);
		s1.setGrubosc(GRUBOSC2);
		s1.setProducent(PRODUCENT2 + " update_test");
		s1.setDlugosc(DLUGOSC2);
		
		assertEquals(1, sm.updateSznurowka(s1));
		
		sm.commitChanges();
		Sznurowka s1db = sm.getSznurowka(s1.getId());
		
		assertEquals(s1.getProducent(), s1db.getProducent());
		assertEquals(s1.getDlugosc(), s1db.getDlugosc());
		assertEquals(s1.getKolor(), s1db.getKolor());
		assertEquals(s1.getGrubosc(), s1db.getGrubosc());
	}
	
	@Test
	public void batchGetTest() {
		Sznurowka s1 = new Sznurowka(PRODUCENT1, DLUGOSC1, KOLOR1, GRUBOSC1);
		Sznurowka s2 = new Sznurowka(PRODUCENT2, DLUGOSC2, KOLOR2, GRUBOSC2);
		Sznurowka s3 = new Sznurowka(PRODUCENT3, DLUGOSC3, KOLOR3, GRUBOSC2);
		
		assertEquals(1,sm.addSznurowka(s1));
		assertEquals(1,sm.addSznurowka(s2));
		assertEquals(1,sm.addSznurowka(s3));
		
		sm.commitChanges();
		
		List<Sznurowka> lista = sm.getAllSznurowki();
		assertNotNull(lista);
		
		int newOnes = 0;
		for(Sznurowka s: lista) {
			if(s.getId().equals(s1.getId())) {
				newOnes++; 
				assertEquals(s1.getProducent(), s.getProducent());
				assertEquals(s1.getDlugosc(), s.getDlugosc());
				assertEquals(s1.getKolor(), s.getKolor());
				assertEquals(s1.getGrubosc(), s.getGrubosc());
			}
			if(s.getId().equals(s2.getId())) {
				newOnes++; 
				assertEquals(s2.getProducent(), s.getProducent());
				assertEquals(s2.getDlugosc(), s.getDlugosc());
				assertEquals(s2.getKolor(), s.getKolor());
				assertEquals(s2.getGrubosc(), s.getGrubosc());
			}
			if(s.getId().equals(s3.getId())) {
				newOnes++; 
				assertEquals(s3.getProducent(), s.getProducent());
				assertEquals(s3.getDlugosc(), s.getDlugosc());
				assertEquals(s3.getKolor(), s.getKolor());
				assertEquals(s3.getGrubosc(), s.getGrubosc());
			}
		}
		assertEquals(3, newOnes);
	}
	
	@Test
	public void deleteTest() {
		Sznurowka s1 = new Sznurowka(PRODUCENT1, DLUGOSC1, KOLOR1, GRUBOSC1);
		assertEquals(1,sm.addSznurowka(s1));
		sm.commitChanges();
		
		assertEquals(1, sm.deleteSznurowka(s1.getId()));
		sm.commitChanges();
		Sznurowka s1db = sm.getSznurowka(s1.getId());
		assertEquals(null, s1db);
	}
	
	@Test
	public void kupISprzedajSznurowkiTest() {
		Sznurowka s1 = new Sznurowka(PRODUCENT1, DLUGOSC1, KOLOR1, GRUBOSC1);
		Sznurowka s2 = new Sznurowka(PRODUCENT2, DLUGOSC2, KOLOR2, GRUBOSC2);
		Sznurowka s3 = new Sznurowka(PRODUCENT3, DLUGOSC3, KOLOR3, GRUBOSC2);
		Sznurowka s4 = new Sznurowka(PRODUCENT1, DLUGOSC2, KOLOR3, GRUBOSC1);
		
		assertEquals(1,sm.addSznurowka(s1));
		assertEquals(1,sm.addSznurowka(s2));
		assertEquals(1,sm.addSznurowka(s3));
		assertEquals(1,sm.addSznurowka(s4));
		
		Sklep sk1 = new Sklep(SKLEP_NAZWA1);
		Sklep sk2 = new Sklep(SKLEP_NAZWA2);

		assertEquals(1, sm.addSklep(sk1));
		assertEquals(1, sm.addSklep(sk2));
		
		sm.commitChanges();
		
		assertEquals(1, sm.kupSznurowke(s1, sk1.getId()));
		assertEquals(1, sm.kupSznurowke(s2, sk1.getId()));
		assertEquals(1, sm.kupSznurowke(s3, sk2.getId()));
		assertEquals(1, sm.kupSznurowke(s4, sk2.getId()));
		
		sm.commitChanges();
		
		Sznurowka s1db = sm.getSznurowka(s1.getId());
		Sznurowka s2db = sm.getSznurowka(s2.getId());
		Sznurowka s3db = sm.getSznurowka(s3.getId());
		Sznurowka s4db = sm.getSznurowka(s4.getId());
		
		assertEquals(sk1.getId(), s1db.getSklep().getId());
		assertEquals(sk1.getNazwa(), s1db.getSklep().getNazwa());
		assertEquals(sk1.getId(), s2db.getSklep().getId());
		assertEquals(sk1.getNazwa(), s2db.getSklep().getNazwa());
		assertEquals(sk2.getId(), s3db.getSklep().getId());
		assertEquals(sk2.getNazwa(), s3db.getSklep().getNazwa());
		assertEquals(sk2.getId(), s4db.getSklep().getId());
		assertEquals(sk2.getNazwa(), s4db.getSklep().getNazwa());
		
		assertEquals(1, sm.sprzedajSznurowke(s1.getId()));
		assertEquals(1, sm.sprzedajSznurowke(s3.getId()));
		
		sm.commitChanges();
		
		s1db = sm.getSznurowka(s1.getId());
		s3db = sm.getSznurowka(s3.getId());

		assertEquals(null, s1db.getSklep());
		assertEquals(null, s3db.getSklep());
	}
	
	
}
