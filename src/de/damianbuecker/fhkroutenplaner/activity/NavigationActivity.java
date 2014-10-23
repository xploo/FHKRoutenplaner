package de.damianbuecker.fhkroutenplaner.activity;

import hirondelle.date4j.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import java.util.TooManyListenersException;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.damianbuecker.fhkroutenplaner.controller.NfcController;
import de.damianbuecker.fhkroutenplaner.controller.SharedPreferencesController;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Room;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Roomtype;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Tag;
import de.damianbuecker.fhkroutenplaner.model.HistoryItem;

/**
 * The Class NavigationActivity.
 */
@ContentView(R.layout.nfcconnector_activity)
public class NavigationActivity extends ModifiedViewActivityImpl implements OnItemSelectedListener {

	/** The m text view. */
	@InjectView(R.id.txtV_nfc_hidden)
	private TextView mTextView;

	@InjectView(R.id.btn_nfc_route)
	private Button btnGo;

	@InjectView(R.id.mTvDescriptionFinish)
	private TextView mTvDescriptionFinish;

	@InjectView(R.id.mTvDescriptionGoButton)
	private TextView mTvDescriptionGoButton;

	/** The m text view floor. */
	@InjectView(R.id.txtV_nfc_floor_out)
	private TextView mTextViewFloor;

	/** The m text view description. */
	@InjectView(R.id.txtV_nfc_description_out)
	private TextView mTextViewDescription;

	@InjectView(R.id.nfc_spinner_roomtype)
	private Spinner mSpinnerRoomtype;

	@InjectView(R.id.nfc_spinner_room)
	private Spinner mSpinnerRoom;

	/** The room spinner data. */
	@SuppressWarnings("rawtypes")
	private List roomtypeSpinnerData, roomSpinnerData;

	/** The database helper. */
	private DatabaseHelper databaseHelper;

	/** The m nfc adapter. */
	private NfcAdapter mNfcAdapter;

	/** The tag list. */
	private List<Tag> tagList;

	/** The alert dialog. */
	private AlertDialog alertDialog;

	/** The m shared preferences controller. */
	private SharedPreferencesController mSharedPreferencesController;

	/** The m nfc controller. */
	private NfcController mNfcController;

	/** The Constant ALERT_DIALOG_TITLE. */
	private static final String ALERT_DIALOG_TITLE = "Position";

	/** The Constant ALERT_DIALOG_MESSAGE. */
	private static final String ALERT_DIALOG_MESSAGE = "Please attach your Phone to RFID-Tag";

	/** The Constant ERROR_MESSAGE_NFC_UNSUPPORTED. */
	private static final String ERROR_MESSAGE_NFC_UNSUPPORTED = "This device doesn't support NFC.";

	/** The Constant ERROR_MESSAGE_NFC_DISABLED. */
	private static final String ERROR_MESSAGE_NFC_DISABLED = "NFC is disabled.";

	/** The Constant INTENT_EXTRA_START_ID. */
	private static final String INTENT_EXTRA_START_ID = "Start_ID";

	/** The Constant INTENT_EXTRA_START_FLOOR. */
	private static final String INTENT_EXTRA_START_FLOOR = "Start_floor";

	/** The Constant HISTORY_ITEM_NAME_PREFIX. */
	private static final String HISTORY_ITEM_NAME_PREFIX = "Navigation ";

	private String endID;

	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.mSharedPreferencesController = new SharedPreferencesController(this);
		this.mTextView.setText("");

		this.mSharedPreferencesController.putInSharedPreference(SHARED_PREFERENCE_ROUTE_RUNNING, false);
		this.btnGo.setVisibility(View.INVISIBLE);
		this.mSpinnerRoom.setVisibility(View.INVISIBLE);
		this.mTvDescriptionFinish.setVisibility(View.INVISIBLE);
		this.mTvDescriptionGoButton.setVisibility(View.INVISIBLE);

		if (this.mTextView.getText().equals("")) {

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
			alertDialogBuilder.setTitle(ALERT_DIALOG_TITLE);
			alertDialogBuilder.setMessage(ALERT_DIALOG_MESSAGE).setCancelable(false);

			// <<< For testing purposes only
			alertDialogBuilder.setNeutralButton("Magic Beans", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					NavigationActivity.this.mTextView.setText("1");
				}
			});
			// End >>>

			alertDialogBuilder.setNegativeButton("Zurück", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {					
					finish();

				}
			});
			this.alertDialog = alertDialogBuilder.create();
			this.alertDialog.show();

		}
		this.mTextView.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					if (s.length() > 0) {
						NavigationActivity.this.start(s.toString());
						alertDialog.dismiss();						
						addRoomtypeSpinner();					

					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		if (this.databaseHelper == null) {
			this.databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);

			this.mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
			if (this.mNfcAdapter == null) {
				Toast.makeText(this, ERROR_MESSAGE_NFC_UNSUPPORTED, Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			if (!this.mNfcAdapter.isEnabled()) {
				Toast.makeText(this, ERROR_MESSAGE_NFC_DISABLED, Toast.LENGTH_LONG).show();
			} else {
			}
			this.mNfcController = new NfcController(this.mTextView);

			this.mNfcController.handleIntent(getIntent(), this);

		}
	}

	@SuppressWarnings("rawtypes")
	public void setRoomtypeSpinner() {

		if (!(this.getIntent().getExtras() == null)) {
			this.endID = this.getIntent().getExtras().getString("endID");
			String[] splitResult = endID.split(" ");

			try {
				List<Tag> listTag = this.databaseHelper.getTagById(splitResult[0]);

				Integer roomID = listTag.get(0).getRoom_ID();
				List<Room> listRoom = this.databaseHelper.getRoomById(String.valueOf(roomID));

				Integer roomTypeId = listRoom.get(0).getRoomtype_ID();
				List<Roomtype> listRoomType = this.databaseHelper.getRoomtypeById(String.valueOf(roomTypeId));

				String SpinnerInput = String.valueOf(roomTypeId) + " " + listRoomType.get(0).getDescription();
				
				if (this.mSpinnerRoomtype.getAdapter() == null) {				
				}

				ArrayAdapter myAdap = (ArrayAdapter) mSpinnerRoomtype.getAdapter();

				int index = 0;
				for (int i = 0; i < myAdap.getCount(); i++) {					
					if (myAdap.getItem(i).equals(SpinnerInput)) {
						index = i;
					}
				}

				mSpinnerRoomtype.setSelection(index);
			} catch (SQLException e) {				
				e.printStackTrace();
			}

		}
	}

	@SuppressWarnings("rawtypes")
	public void setSpinner() {

		if (!(this.getIntent().getExtras() == null)) {
			this.endID = this.getIntent().getExtras().getString("endID");
			
			try {
				List<Tag> listTag = this.databaseHelper.getTagById(endID);
				Integer roomID = listTag.get(0).getRoom_ID();
				List<Room> listRoom = this.databaseHelper.getRoomById(String.valueOf(roomID));		
				
				if (this.mSpinnerRoom.getAdapter() == null) {					
				}

				ArrayAdapter myAdap = (ArrayAdapter) mSpinnerRoom.getAdapter();
				
				int index = 0;
				for (int i = 0; i < myAdap.getCount(); i++) {					
					if (myAdap.getItem(i).equals(endID+" "+listRoom.get(0).getDescription())) {
						index = i;
					}
				}						
				
				mSpinnerRoom.setSelection(index);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	/**
	 * Start.
	 * 
	 * @param s
	 *            the s
	 * @throws SQLException
	 *             the SQL exception
	 */
	private void start(String s) throws SQLException {
		this.tagList = this.databaseHelper.getTagById(s);

		if ((tagList.get(0) != null) && ((tagList.get(0).getFloor() != null))) {
			this.mTextViewFloor.setText(tagList.get(0).getFloor().toString());
			this.mTextViewDescription.setText(tagList.get(0).getDescription());
		}
	}

	/**
	 * Adds the roomtype spinner.
	 */
	@SuppressWarnings("unchecked")
	private void addRoomtypeSpinner() {
		try {
			if (this.roomtypeSpinnerData == null) {
				this.roomtypeSpinnerData = new ArrayList<Integer>();
				roomtypeSpinnerData.add("Bitte den Raumtyp des Zielortes wählen!");
				for (int i = 0; i < databaseHelper.getRoomtypeSpinner().size(); i++) {
					roomtypeSpinnerData.add(databaseHelper.getRoomtypeSpinner().get(i));
				}
			}

			ArrayAdapter<String> RoomtypeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roomtypeSpinnerData);
			RoomtypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			this.mSpinnerRoomtype.setAdapter(RoomtypeAdapter);
			this.mSpinnerRoomtype.setOnItemSelectedListener(this);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds the room spinner.
	 * 
	 * @param roomtypeID
	 *            the roomtype id
	 */

	@SuppressWarnings("unchecked")
	public void addRoomSpinner(Integer roomtypeID) {
		this.roomSpinnerData = null;
		try {
			// <------ TV neu
			Integer buffer = databaseHelper.getRoomSpinner(roomtypeID, this.mTextView.getText().toString()).size();
			this.roomSpinnerData = new ArrayList<Integer>();
			this.roomSpinnerData.add("Bitte Ziel Raum wählen!");
			for (int i = 0; i < buffer; i++) {
				// <---------- TV neu
				roomSpinnerData.add(databaseHelper.getRoomSpinner(roomtypeID, this.mTextView.getText().toString()).get(i));
				// ----------------->
			}
			ArrayAdapter<String> RoomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, roomSpinnerData);
			RoomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			this.mSpinnerRoom.setAdapter(RoomAdapter);
			this.mSpinnerRoom.setOnItemSelectedListener(this);

			roomSpinnerData = null;
		} catch (SQLException e) {
			// tv.setText(e.toString());
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

		Spinner spinner = (Spinner) parent;
		if (spinner.getId() == R.id.nfc_spinner_roomtype) {

			this.mSpinnerRoomtype.setSelection(position);
			setRoomtypeSpinner();

			if (String.valueOf(this.mSpinnerRoomtype.getSelectedItem()).equals("Bitte den Raumtyp des Zielortes wählen!")) {

			} else {

				String selState = (String) this.mSpinnerRoomtype.getSelectedItem();
				String[] splitResult = selState.split(" ");
				addRoomSpinner(Integer.parseInt(splitResult[0]));
				setSpinner();
				this.mTvDescriptionFinish.setVisibility(View.VISIBLE);
				this.mSpinnerRoom.setVisibility(View.VISIBLE);
			}
		} else if (spinner.getId() == R.id.nfc_spinner_room) {
			if (this.mSpinnerRoom.getSelectedItem().equals("Bitte Ziel Raum wählen!")) {

			} else {
				
				this.mSpinnerRoom.setSelection(position);
				this.mTvDescriptionGoButton.setVisibility(View.VISIBLE);
				this.btnGo.setVisibility(View.VISIBLE);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android
	 * .widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		/**
		 * Disposes the DatabaseHelper.
		 */
		if (this.databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			this.databaseHelper = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onResume()
	 */
	@SuppressWarnings("static-access")
	@Override
	protected void onResume() {
		super.onResume();

		NavigationActivity.this.btnGo.setVisibility(View.INVISIBLE);

		/**
		 * It's important, that the activity is in the foreground (resumed).
		 * Otherwise an IllegalStateException is thrown.
		 */
		this.mNfcController = new NfcController(this.mTextView);
		this.mNfcController.setupForegroundDispatch(this, mNfcAdapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onPause()
	 */
	@SuppressWarnings("static-access")
	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * Call this before onPause, otherwise an IllegalArgumentException is
		 * thrown as well.
		 */
		this.mNfcController = new NfcController(this.mTextView);
		this.mNfcController.stopForegroundDispatch(this, mNfcAdapter);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onNewIntent(android.content.Intent)
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		/**
		 * This method gets called, when a new Intent gets associated with the
		 * current activity instance. Instead of creating a new activity,
		 * onNewIntent will be called. For more information have a look at the
		 * documentation.
		 * 
		 * In our case this method gets called, when the user attaches a Tag to
		 * the device.
		 */
		this.mNfcController = new NfcController(this.mTextView);
		this.mNfcController.handleIntent(intent, this);
	}

	/**
	 * On click_ go.
	 * 
	 * @param v
	 *            the v
	 */
	public void onClick_GO(View v) {

		NavigationActivity.this.btnGo.setVisibility(View.INVISIBLE);

		Intent intent = new Intent(this, DisplayMapsActivity.class);
		String[] splitResult = String.valueOf(this.mSpinnerRoom.getSelectedItem()).split(" ");

		if (this.mSharedPreferencesController == null) {
			this.mSharedPreferencesController = new SharedPreferencesController(this);
		}
		if (this.mSharedPreferencesController.hasSharedPreference(SHARED_PREFERENCE_FIRST_RUN)) {
			if (this.mSharedPreferencesController.getBoolean(SHARED_PREFERENCE_FIRST_RUN)) {
				intent.putExtra("End_ID", splitResult[0]);
			}
		}		

		intent.putExtra(INTENT_EXTRA_START_ID, String.valueOf(this.mTextView.getText().toString()));
		intent.putExtra(INTENT_EXTRA_START_FLOOR, String.valueOf(this.mTextViewFloor.getText().toString()));

		this.mSharedPreferencesController.putInSharedPreference(SHARED_PREFERENCE_LAST_DESTINATION, splitResult[0]);	
		this.mSharedPreferencesController.putInSharedPreference(SHARED_PREFERENCE_FIRST_RUN, false);

		DateTime today = DateTime.today(TimeZone.getDefault());
		long date = today.getMilliseconds(TimeZone.getDefault());

		DateTime now = DateTime.now(TimeZone.getDefault());
		long time = now.getMilliseconds(TimeZone.getDefault());

		StringBuffer name = new StringBuffer("");
		name.append(HISTORY_ITEM_NAME_PREFIX).append("nach"+ this.mSpinnerRoom.getSelectedItem());

		StringBuffer start = new StringBuffer("");
		start.append(this.mTextViewDescription.getText());

		StringBuffer destination = new StringBuffer("");
		destination.append(this.mSpinnerRoom.getSelectedItem());

		HistoryItem item = new HistoryItem();
		item.setName(name.toString());
		item.setDate(date);
		item.setTimestamp(time);
		item.setStart(start.toString());
		item.setDestination(destination.toString());
		item.writeToDatabase(this);	
		
		startActivity(intent);
		Toast.makeText(this, "Route wird berechnet.", Toast.LENGTH_LONG).show();

	}
}
