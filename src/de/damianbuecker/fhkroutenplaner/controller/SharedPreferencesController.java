package de.damianbuecker.fhkroutenplaner.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

// TODO: Auto-generated Javadoc
/**
 * The Class SharedPreferencesController.
 */
public class SharedPreferencesController extends Controller {

	/** The m shared preferences. */
	private SharedPreferences mSharedPreferences;

	/** The m editor. */
	private Editor mEditor;

	/** The Constant SHAREDPREFERENCESNAME. */
	private static final String SHAREDPREFERENCESNAME = "de.damianbuecker.fhkroutenplaner";

	/**
	 * Instantiates a new shared preferences controller.
	 * 
	 * @param context
	 *            the context
	 */
	public SharedPreferencesController(Context context) {
		super(context);
		this.mSharedPreferences = this.getContext().getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE);
		this.mEditor = this.mSharedPreferences.edit();
	}

	/**
	 * Put in shared preference.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void putInSharedPreference(String key, Object value) {
		if (value != null) {
			if (this.mSharedPreferences == null) {
				this.mSharedPreferences = this.getContext().getSharedPreferences(SHAREDPREFERENCESNAME, Context.MODE_PRIVATE);
			}
			if (this.mEditor == null) {
				this.mEditor = this.mSharedPreferences.edit();
			}

			if (value instanceof Boolean) {
				this.mEditor.putBoolean(key, (Boolean) value).commit();
			} else if (value instanceof Float) {
				this.mEditor.putFloat(key, (Float) value).commit();
			} else if (value instanceof Integer) {
				this.mEditor.putInt(key, (Integer) value).commit();
			} else if (value instanceof Long) {
				this.mEditor.putLong(key, (Long) value).commit();
			} else if (value instanceof String) {
				this.mEditor.putString(key, (String) value).commit();
			} else {
				this.logMessage("ERROR", "Unsupported data type.");
			}
		}
	}

	/**
	 * Checks for shared preference.
	 * 
	 * @param key
	 *            the key
	 * @return true, if successful
	 */
	public boolean hasSharedPreference(String key) {
		return this.mSharedPreferences.contains(key);
	}

	/**
	 * Gets the boolean.
	 * 
	 * @param key
	 *            the key
	 * @return the boolean
	 */
	public boolean getBoolean(String key) {
		if (this.mSharedPreferences != null && this.hasSharedPreference(key)) {
			return this.mSharedPreferences.getBoolean(key, false);
		} else {
			return false;
		}
	}

	/**
	 * Gets the float.
	 * 
	 * @param key
	 *            the key
	 * @return the float
	 */
	public Float getFloat(String key) {
		if (this.mSharedPreferences != null && this.hasSharedPreference(key)) {
			return this.mSharedPreferences.getFloat(key, -1F);
		} else {
			return null;
		}
	}

	/**
	 * Gets the integer.
	 * 
	 * @param key
	 *            the key
	 * @return the integer
	 */
	public Integer getInteger(String key) {
		if (this.mSharedPreferences != null && this.hasSharedPreference(key)) {
			return this.mSharedPreferences.getInt(key, -1);
		} else {
			return null;
		}
	}

	/**
	 * Gets the long.
	 * 
	 * @param key
	 *            the key
	 * @return the long
	 */
	public Long getLong(String key) {
		if (this.mSharedPreferences != null && this.hasSharedPreference(key)) {
			return this.mSharedPreferences.getLong(key, -1L);
		} else {
			return null;
		}
	}

	/**
	 * Gets the string.
	 * 
	 * @param key
	 *            the key
	 * @return the string
	 */
	public String getString(String key) {
		if (this.mSharedPreferences != null && this.hasSharedPreference(key)) {
			return this.mSharedPreferences.getString(key, "ERROR");
		} else {
			return null;
		}
	}

	/**
	 * Removes the all shared preferences.
	 */
	public void removeAllSharedPreferences() {
		if (this.mEditor != null) {
			this.mEditor.clear().commit();
		}
	}
}
