package com.example.jdbcdemo.service;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.example.jdbcdemo.domain.Sklep;
import com.example.jdbcdemo.domain.Sznurowka;

public class SznurowkaManager {
	
	private Connection connection;

	private String url = "jdbc:hsqldb:hsql://localhost/workdb";
	
	private String createTableSznurowka = "CREATE TABLE Sznurowka(id bigint GENERATED BY DEFAULT AS IDENTITY," +
			" producent varchar(30), dlugosc integer, kolor varchar(30)," +
			" grubosc integer, sklep_id bigint)";
	private String createTableSklep = "CREATE TABLE Sklep(id bigint GENERATED BY DEFAULT AS IDENTITY," +
			" nazwa varchar(30))";
	
	private Statement statement;
	
	private PreparedStatement addSznurowkaStatement;
	private PreparedStatement addSklepStatement;
	private PreparedStatement updateSznurowkaStatement;
	private PreparedStatement deleteSznurowkaStatement;
	private PreparedStatement getAllSznurowkasStatement;
	private PreparedStatement getSznurowkaStatement;
	private PreparedStatement kupSznurowkeStatement;
	private PreparedStatement sprzedajSznurowkeStatement;	
	
	public SznurowkaManager() {
		try {
			connection = DriverManager.getConnection(url);
			statement = connection.createStatement();

			ResultSet rs = connection.getMetaData().getTables(null, null, null,
					null);
			boolean tableExists = false;
			while (rs.next()) {
				if ("Sznurowka".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
					tableExists = true;
					break;
				}
			}
			
			if (!tableExists)
				statement.executeUpdate(createTableSznurowka);

			//Tabela sklep
			rs = connection.getMetaData().getTables(null, null, null, null);
			tableExists = false;
			while (rs.next()) {
				if ("Sklep".equalsIgnoreCase(rs.getString("TABLE_NAME"))) {
					tableExists = true;
					break;
				}
			}

			if (!tableExists)
				statement.executeUpdate(createTableSklep);
			
			connection.setAutoCommit(false);
			
			addSznurowkaStatement = connection
					.prepareStatement("INSERT INTO Sznurowka (producent, dlugosc," +
							" kolor, grubosc) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			addSklepStatement = connection
					.prepareStatement("INSERT INTO Sklep (nazwa) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			deleteSznurowkaStatement = connection
					.prepareStatement("DELETE FROM Sznurowka where id = ?");
			getAllSznurowkasStatement = connection
					.prepareStatement("SELECT id, producent, dlugosc, kolor, grubosc FROM Sznurowka");
			updateSznurowkaStatement = connection
					.prepareStatement("UPDATE Sznurowka SET producent = ? , " +
							"dlugosc = ? , kolor = ? , grubosc = ? " +
							"WHERE id = ?");
			getSznurowkaStatement = connection.prepareStatement("Select s.id, s.producent, s.dlugosc," +
					"s.kolor, s.grubosc, s.sklep_id, sk.nazwa from " +
					"Sznurowka s LEFT JOIN Sklep sk on s.sklep_id = sk.id where s.id = ?");
			kupSznurowkeStatement = connection.prepareStatement("UPDATE Sznurowka SET sklep_id = ? " +
					"WHERE id = ?");
			sprzedajSznurowkeStatement = connection.prepareStatement("UPDATE Sznurowka SET sklep_id = null " +
					"WHERE id = ?");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	Connection getConnection() {
		return connection;
	}
	
	public int addSznurowka(Sznurowka s) {
		int count = 0;
		try {
			addSznurowkaStatement.setString(1, s.getProducent());
			addSznurowkaStatement.setInt(2, s.getDlugosc());
			addSznurowkaStatement.setString(3, s.getKolor());
			addSznurowkaStatement.setInt(4, s.getGrubosc());

			count = addSznurowkaStatement.executeUpdate();
			
			ResultSet generatedKeys = addSznurowkaStatement.getGeneratedKeys();
			if(generatedKeys.next()) {
				s.setId(generatedKeys.getLong(1));
			}
			else {
				System.out.println("Blad pobierania id");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int addSklep(Sklep s) {
		int count = 0;
		try {
			addSklepStatement.setString(1, s.getNazwa());
			count = addSklepStatement.executeUpdate();
			
			ResultSet generatedKeys = addSklepStatement.getGeneratedKeys();
			if(generatedKeys.next()) {
				s.setId(generatedKeys.getLong(1));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public List<Sznurowka> getAllSznurowki() {
		List<Sznurowka> sznurowki = new ArrayList<Sznurowka>();
	
		try {
			ResultSet rs = getAllSznurowkasStatement.executeQuery();
	
			while (rs.next()) {
				Sznurowka s = new Sznurowka();
				s.setId(rs.getLong("id"));
				s.setProducent(rs.getString("producent"));
				s.setDlugosc(rs.getInt("dlugosc"));
				s.setKolor(rs.getString("kolor"));
				s.setGrubosc(rs.getInt("grubosc"));
				sznurowki.add(s);
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sznurowki;
	}
	
	public Sznurowka getSznurowka(Long id) {
		Sznurowka s = null;
		Sklep sk = new Sklep();
		try {
			getSznurowkaStatement.setLong(1, id);
			ResultSet rs = getSznurowkaStatement.executeQuery();
			if(rs.next()) {
				s = new Sznurowka();
				s.setId(rs.getLong("id"));
				s.setProducent(rs.getString("producent"));
				s.setDlugosc(rs.getInt("dlugosc"));
				s.setKolor(rs.getString("kolor"));
				s.setGrubosc(rs.getInt("grubosc"));
				if(rs.getLong("sklep_id") != 0) {
					sk.setId(rs.getLong("sklep_id"));
					sk.setNazwa(rs.getString("nazwa"));
					s.setSklep(sk);
				}
			}
	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}
	
	public int deleteSznurowka(Long id) {
		int count = 0;
		try {
			deleteSznurowkaStatement.setLong(1, id);

			count = deleteSznurowkaStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int updateSznurowka(Sznurowka s) {
		int count = 0;
		try {
			updateSznurowkaStatement.setString(1, s.getProducent());
			updateSznurowkaStatement.setInt(2, s.getDlugosc());
			updateSznurowkaStatement.setString(3, s.getKolor());
			updateSznurowkaStatement.setInt(4, s.getGrubosc());
			
			updateSznurowkaStatement.setLong(5, s.getId());

			count = updateSznurowkaStatement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int kupSznurowke(Sznurowka s, Long idSklepu) {
		int count = 0;
		try {
			kupSznurowkeStatement.setLong(1, idSklepu);
			kupSznurowkeStatement.setLong(2, s.getId());
			count = kupSznurowkeStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int sprzedajSznurowke(Long id) {
		int count = 0;
		try {
			sprzedajSznurowkeStatement.setLong(1, id);
			count = sprzedajSznurowkeStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public void commitChanges() {
		try {
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
