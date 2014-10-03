package de.damianbuecker.fhkroutenplaner.databaseaccess;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * The Class Docent.
 */
@DatabaseTable(tableName = "Docent")
public class Docent {

	/** The Constant DOCENT_ID. */
	public static final String DOCENT_ID = "docent_id";

	/** The Constant D_NAME. */
	public static final String D_NAME = "D_name";

	/** The Constant D_LASTNAME. */
	public static final String D_LASTNAME = "D_lastname";

	/** The dozent_id. */
	@DatabaseField(canBeNull = false, useGetSet = true, id = true, columnName = DOCENT_ID)
	private int dozent_id;

	/** The D_name. */
	@DatabaseField(canBeNull = false, useGetSet = true, columnName = D_NAME)
	private String D_name;

	/** The D_lastname. */
	@DatabaseField(canBeNull = false, useGetSet = true, columnName = D_LASTNAME)
	private String D_lastname;

	/**
	 * Instantiates a new docent.
	 */
	public Docent() {

	}

	/**
	 * Gets the dozent_id.
	 * 
	 * @return the dozent_id
	 */
	public int getDozent_id() {
		return dozent_id;
	}

	/**
	 * Sets the dozent_id.
	 * 
	 * @param dozent_id
	 *            the new dozent_id
	 */
	public void setDozent_id(int dozent_id) {
		this.dozent_id = dozent_id;
	}

	/**
	 * Gets the d_name.
	 * 
	 * @return the d_name
	 */
	public String getD_name() {
		return D_name;
	}

	/**
	 * Sets the d_name.
	 * 
	 * @param d_name
	 *            the new d_name
	 */
	public void setD_name(String d_name) {
		D_name = d_name;
	}

	/**
	 * Gets the d_lastname.
	 * 
	 * @return the d_lastname
	 */
	public String getD_lastname() {
		return D_lastname;
	}

	/**
	 * Sets the d_lastname.
	 * 
	 * @param d_lastname
	 *            the new d_lastname
	 */
	public void setD_lastname(String d_lastname) {
		D_lastname = d_lastname;
	}

}
