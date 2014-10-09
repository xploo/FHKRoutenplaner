package de.damianbuecker.fhkroutenplaner.activity;

import hirondelle.date4j.DateTime;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.damianbuecker.fhkroutenplaner.controller.NfcController;
import de.damianbuecker.fhkroutenplaner.controller.SharedPreferencesController;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Tag;
import de.damianbuecker.fhkroutenplaner.model.HistoryItem;

@ContentView(R.layout.nfcconnector_activity)
public class NavigationActivity extends ModifiedViewActivityImpl implements
		OnItemSelectedListener {

	@InjectView(R.id.txtV_nfc_hidden)			TextView mTextView;
	private Spinner SpinnerRoomtype, SpinnerRoom;
	private List roomtypeSpinnerData, roomSpinnerData;
	private DatabaseHelper databaseHelper;
	private TextView mTextViewFloor, mTextViewDescription;
	private NfcAdapter mNfcAdapter;
	private static final String MIME_TEXT_PLAIN = "text/plain";
	private static final String TAG = "NfcDemo";
	private Integer RESULT = 0;
	private Integer buf = 0;
	private List<Tag> tagList = null;
	private SharedPreferences prefs;
	private AlertDialog alertDialog;

	private SharedPreferencesController mSharedPreferencesController;
	private NfcController mNfcController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.mSharedPreferencesController = new SharedPreferencesController(this);
		this.mTextView.setText("");
		if (this.mTextView.getText().equals("")) {
			
			Log.v("CHECK ALERTDIAG","woop");

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);		
			alertDialogBuilder.setTitle("Position");		
			alertDialogBuilder.setMessage("Please attach your Phone to RFID-Tag").setCancelable(false);
			this.alertDialog = alertDialogBuilder.create();
			this.alertDialog.show();	
		
		}
		this.mTextView.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
						
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				try {
					if (s.length() > 0) {
						NavigationActivity.this.start(s.toString());
						alertDialog.dismiss();
						//<----- 
						addRoomtypeSpinner();
						//----->
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
		if (this.databaseHelper == null) {
			this.databaseHelper = OpenHelperManager.getHelper(this,
					DatabaseHelper.class);

			mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
			if (mNfcAdapter == null) {
				// Stop here, we definitely need NFC
				Toast.makeText(this, "This device doesn't support NFC.",
						Toast.LENGTH_LONG).show();
				finish();
				return;
			}
			if (!mNfcAdapter.isEnabled()) {
				Toast.makeText(this, "NFC is disabled.", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(this, "R.string.explanation", Toast.LENGTH_LONG)
						.show();
			}
			mNfcController = new NfcController(this.mTextView);

			mNfcController.handleIntent(getIntent(),this);

		}
		
	}

	private void start(String s) throws SQLException {
		this.mTextViewFloor = (TextView) findViewById(R.id.txtV_nfc_floor_out);
		this.mTextViewDescription = (TextView) findViewById(R.id.txtV_nfc_description_out);

		Log.v("was steht im tag", s);

		this.tagList = this.databaseHelper.getTagById(s);

		if ((tagList.get(0) != null) && ((tagList.get(0).getFloor() != null))) {
			Log.v("List_Floor", tagList.get(0).getFloor().toString());
			this.mTextViewFloor.setText(tagList.get(0).getFloor().toString());
			this.mTextViewDescription.setText(tagList.get(0).getDescription());
		}
	}

	private void addRoomtypeSpinner() {

		this.SpinnerRoomtype = (Spinner) findViewById(R.id.nfc_spinner_roomtype);
		try {
			if (this.roomtypeSpinnerData == null) {
				this.roomtypeSpinnerData = new ArrayList<Integer>();
				for (int i = 0; i < databaseHelper.getRoomtypeSpinner().size(); i++) {

					roomtypeSpinnerData.add(databaseHelper.getRoomtypeSpinner()
							.get(i));
					Log.v("SpinnerData1Testausgabe", String.valueOf(i));
				}
			}

			ArrayAdapter<String> RoomtypeAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_item,
					roomtypeSpinnerData);
			RoomtypeAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			SpinnerRoomtype.setAdapter(RoomtypeAdapter);
			SpinnerRoomtype.setOnItemSelectedListener(this);

			// addRoomSpinner(Integer.parseInt(RESULTSPINNER));

		} catch (SQLException e) {
			// tv.setText(e.toString());
			e.printStackTrace();
		}
	}

	public void addRoomSpinner(Integer roomtypeID) {

		this.SpinnerRoom = (Spinner) findViewById(R.id.nfc_spinner_room);
		this.roomSpinnerData = null;
		try {
			//<------ TV neu
			Integer buffer = databaseHelper.getRoomSpinner(roomtypeID, this.mTextView.getText().toString()).size();
			this.roomSpinnerData = new ArrayList<Integer>();

			for (int i = 0; i < buffer; i++) {
				Log.v("RoomSpinnerTest3", "testest");
				
				//<---------- TV neu
				roomSpinnerData.add(databaseHelper.getRoomSpinner(roomtypeID,this.mTextView.getText().toString())
						.get(i));
				//----------------->
				Log.v("SpinnerData2Testausgabe", String.valueOf(i));
			}

			ArrayAdapter<String> RoomAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_spinner_item, roomSpinnerData);
			RoomAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			SpinnerRoom.setAdapter(RoomAdapter);
			SpinnerRoom.setOnItemSelectedListener(this);
			roomSpinnerData = null;

		} catch (SQLException e) {
			// tv.setText(e.toString());
			e.printStackTrace();
		}
	}

	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		Spinner spinner = (Spinner) parent;
		if (spinner.getId() == R.id.nfc_spinner_roomtype) {
			// do this
			SpinnerRoomtype.setSelection(position);
			String selState = (String) SpinnerRoomtype.getSelectedItem();

			String[] splitResult = selState.split(" ");

			Log.v("ITEMSELECTED", selState);
			addRoomSpinner(Integer.parseInt(splitResult[0]));

		} else if (spinner.getId() == R.id.nfc_spinner_room) {
			// do this
			SpinnerRoom.setSelection(position);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

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

	@SuppressWarnings("static-access")
	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * It's important, that the activity is in the foreground (resumed).
		 * Otherwise an IllegalStateException is thrown.
		 */
		mNfcController = new NfcController(this.mTextView);
		mNfcController.setupForegroundDispatch(this, mNfcAdapter);
	}

	@SuppressWarnings("static-access")
	@Override
	protected void onPause() {
		super.onPause();
		/**
		 * Call this before onPause, otherwise an IllegalArgumentException is
		 * thrown as well.
		 */
		mNfcController = new NfcController(this.mTextView);
		mNfcController.stopForegroundDispatch(this, mNfcAdapter);

		
	}

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
		mNfcController = new NfcController(this.mTextView);
		mNfcController.handleIntent(intent,this);
	}

	public void onClick_GO(View v) {
		this.SpinnerRoom = (Spinner) findViewById(R.id.nfc_spinner_room);

		Intent intent = new Intent("android.intents.NFCGO");
		String[] splitResult = String.valueOf(this.SpinnerRoom.getSelectedItem()).split(" ");

		if(this.mSharedPreferencesController == null) {
			this.mSharedPreferencesController = new SharedPreferencesController(this);
		}
		if(this.mSharedPreferencesController.hasSharedPreference("firstrun")) {
			if(this.mSharedPreferencesController.getBoolean("firstrun")) {
			intent.putExtra("End_ID", splitResult[0]);
		}
		}

		Log.v("ROOMSPINNER AUSGABE",
				String.valueOf(this.SpinnerRoom.getSelectedItem()));
		Log.v("ROOMSPINNER NACH SPLIT", splitResult[0]);
		this.logInfo("ROOMSPINNER AUSGABE - " + String.valueOf(this.SpinnerRoom.getSelectedItem()));
		this.logInfo("ROOMSPINNER NACH SPLIT - " + splitResult[0]);


		intent.putExtra("Start_ID",String.valueOf(this.mTextView.getText().toString()));
		intent.putExtra("Start_floor", String.valueOf(this.mTextViewFloor.getText().toString()));
		
		this.mSharedPreferencesController.putInSharedPreference("lastDestination", splitResult[0]);
		
		DateTime today = DateTime.today(TimeZone.getDefault());
		long date = today.getMilliseconds(TimeZone.getDefault());

		DateTime now = DateTime.now(TimeZone.getDefault());
		long time = now.getMilliseconds(TimeZone.getDefault());
		
		StringBuffer name = new StringBuffer("");
		name.append("Navigation ").append(today.toString());
		
		StringBuffer start = new StringBuffer("");
		start.append(this.mTextViewDescription.getText());
		
		StringBuffer destination = new StringBuffer("");
		destination.append(this.SpinnerRoom.getSelectedItem());
		
		HistoryItem item = new HistoryItem();
		item.setName(name.toString());
		item.setDate(date);
		item.setTimestamp(time);
		item.setStart(start.toString());
		item.setDestination(destination.toString());
		item.create(this);
		
		this.logInfo("item: " + item.getName() + " " + item.getDate() + " " + item.getTimestamp() + " " + item.getStart() + " " + item.getDestination());

		// TODO: DatumsController
		startActivity(intent);
	}
}
