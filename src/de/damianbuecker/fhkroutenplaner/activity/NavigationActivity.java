package de.damianbuecker.fhkroutenplaner.activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

import de.damianbuecker.fhkroutenplaner.controller.NFCController;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.databaseaccess.Tag;

public class NavigationActivity extends ModifiedViewActivityImpl implements OnItemSelectedListener {

	private Spinner SpinnerRoomtype, SpinnerRoom;
	private List roomtypeSpinnerData, roomSpinnerData;
	private DatabaseHelper databaseHelper;
	private TextView mTextView;
	private TextView mTextViewFloor, mTextViewDescription;

	private NfcAdapter mNfcAdapter;
	private static final String MIME_TEXT_PLAIN = "text/plain";
	private static final String TAG = "NfcDemo";
	private Integer RESULT = 0;
	private Integer buf = 0;
	private List<Tag> tagList = null;
	private SharedPreferences prefs;

	private NFCController nfccon;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nfcconnector_activity);		
		mTextView = (TextView) findViewById(R.id.txtV_nfc_hidden);
		
		prefs = getSharedPreferences("de.damianbuecker.fhkroutenplaner",
				MODE_PRIVATE);
		
		this.mTextView.setText("");
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
						// Start
						NavigationActivity.this.start(s.toString());
						

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
			nfccon = new NFCController(this.mTextView);
			
			nfccon.handleIntent(getIntent(),this);

		}

		addRoomtypeSpinner();
	}

	private void start(String s) throws SQLException {
		this.mTextViewFloor = (TextView) findViewById(R.id.txtV_nfc_floor_out);
		this.mTextViewDescription = (TextView) findViewById(R.id.txtV_nfc_description_out);
		
		Log.v("was steht im tag",s);

		this.tagList = this.databaseHelper.getTagById(s);
		
		if((tagList.get(0) != null) && ((tagList.get(0).getFloor() != null))) {
			Log.v("List_Floor",tagList.get(0).getFloor().toString());
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
			Integer buffer = databaseHelper.getRoomSpinner(roomtypeID).size();
			this.roomSpinnerData = new ArrayList<Integer>();

			for (int i = 0; i < buffer; i++) {
				Log.v("RoomSpinnerTest3", "testest");
				roomSpinnerData.add(databaseHelper.getRoomSpinner(roomtypeID)
						.get(i));
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

	@Override
	protected void onResume() {
		super.onResume();
		/**
		 * It's important, that the activity is in the foreground (resumed).
		 * Otherwise an IllegalStateException is thrown.
		 */
		nfccon = new NFCController(this.mTextView);
		nfccon.setupForegroundDispatch(this, mNfcAdapter);
	}

	@Override
	protected void onPause() {
		/**
		 * Call this before onPause, otherwise an IllegalArgumentException is
		 * thrown as well.
		 */
		nfccon = new NFCController(this.mTextView);
		nfccon.stopForegroundDispatch(this, mNfcAdapter);

		super.onPause();
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
		nfccon = new NFCController(this.mTextView);
		nfccon.handleIntent(intent,this);
	}

	public void onClick_GO(View v) {
		this.SpinnerRoom = (Spinner) findViewById(R.id.nfc_spinner_room);
		
		// SpinnerData in SharedPref schreiben.

		Intent intent = new Intent("android.intents.NFCGO");
		String selectedItem = String.valueOf(this.SpinnerRoom.getSelectedItem());
		String[] splitResult = selectedItem.split(" ");
		
		if (prefs.getBoolean("firstrun", true)) {
		intent.putExtra("End_ID",splitResult[0]);
		}
		
		Log.v("ROOMSPINNER AUSGABE",String.valueOf(this.SpinnerRoom.getSelectedItem()));
		Log.v("ROOMSPINNER NACH SPLIT",splitResult[0]);
		
		
		intent.putExtra("Start_ID",String.valueOf(this.mTextView.getText().toString()));
		intent.putExtra("Start_floor", String.valueOf(this.mTextViewFloor.getText().toString()));
		
		prefs.edit().putString("lastDestination",splitResult[0]).commit();
		startActivity(intent);
	}
}
