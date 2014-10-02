package de.damianbuecker.fhkroutenplaner.databaseaccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
/*
 * Anlegen der Tabelle Dozent
 */
@DatabaseTable(tableName = "Docent")
public class Docent {
	
	public static final String DOCENT_ID = "docent_id";
	public static final String D_NAME = "D_name";
	public static final String D_LASTNAME = "D_lastname";
	
	/*
	 * Anlegen der Datenbank Spalten
	 * 
	 */

	@DatabaseField(canBeNull = false, useGetSet = true, id = true, columnName = DOCENT_ID)
	private int dozent_id;

	@DatabaseField(canBeNull = false, useGetSet = true, columnName = D_NAME)
	private String D_name;

	@DatabaseField(canBeNull = false, useGetSet = true, columnName = D_LASTNAME)
	private String D_lastname;

	public Docent() {

	}
	/*
	 * Getter und Setter der tabelle Docent
	 */

	public int getDozent_id() {
		return dozent_id;
	}

	public void setDozent_id(int dozent_id) {
		this.dozent_id = dozent_id;
	}

	public String getD_name() {
		return D_name;
	}

	public void setD_name(String d_name) {
		D_name = d_name;
	}

	public String getD_lastname() {
		return D_lastname;
	}

	public void setD_lastname(String d_lastname) {
		D_lastname = d_lastname;
	}

}
