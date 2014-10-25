package de.damianbuecker.fhkroutenplaner.activity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import de.damianbuecker.fhkroutenplaner.controller.DateTimeController;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.model.HistoryItem;

/**
 * The Class HistoryItemDetailActivity.
 */
@ContentView(R.layout.history_item_detail_view)
public class HistoryItemDetailActivity extends ModifiedViewActivityImpl {

	/** The local history item. */
	private HistoryItem mHistoryItem;

	/** The local text view name. */
	@InjectView(R.id.name)
	private TextView mTextViewName;

	/** The local text view edit name. */
	@InjectView(R.id.editName)
	private EditText mEditTextEditName;

	/** The local text view date. */
	@InjectView(R.id.date)
	private TextView mTextViewDate;

	/** The local edit text edit date. */
	@InjectView(R.id.editDate)
	private EditText mEditTextEditDate;

	/** The local text view time. */
	@InjectView(R.id.time)
	private TextView mTextViewTime;

	/** The local edit text edit time. */
	@InjectView(R.id.editTime)
	private EditText mEditTextEditTime;

	/** The local text view start. */
	@InjectView(R.id.start)
	private TextView mTextViewStart;

	/** The local edit text edit start. */
	@InjectView(R.id.editStart)
	private EditText mEditTextEditStart;

	/** The local text view destination. */
	@InjectView(R.id.destination)
	private TextView mTextViewDestination;

	/** The local edit text edit destination. */
	@InjectView(R.id.editDestination)
	private EditText mEditTextEditDestination;
	
	/** The local button start navigation. */
	@InjectView(R.id.startNavigationButton)
	private Button mButtonStartNavigation;
	
	/** The local button save name. */
	@InjectView(R.id.btnSaveName)
	private Button mButtonSaveName;

	private DateTimeController mDateTimeController;
	
	private DatabaseHelper mDatabaseHelper;
	
	private Integer mIntegerHistoryId; 
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onCreate(android.os.Bundle)
	 */
	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(this.mDateTimeController==null){
			mDateTimeController = new DateTimeController(this);
		}
		if(this.mDatabaseHelper == null){
			mDatabaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);;
		}		

		
		if (this.getIntent().getStringExtra(SELECTED_ITEM) != null) {
			this.mHistoryItem = new HistoryItem();
			this.mHistoryItem = this.mHistoryItem.fromJson(this.getIntent().getStringExtra(SELECTED_ITEM));
		}
		this.mIntegerHistoryId = this.mHistoryItem.getId();
		this.mEditTextEditName.setText(this.mHistoryItem.getName());
		this.mEditTextEditDate.setText(String.valueOf(this.mDateTimeController.toDate(this.mHistoryItem.getDate()).format(DATE_FORMAT_DD_MM_YYYY)));
		this.mEditTextEditTime.setText(String.valueOf(this.mDateTimeController.toDate(this.mHistoryItem.getTimestamp()).format(TIME_FORMAT_HH_MM_SS)));
		this.mEditTextEditStart.setText(this.mHistoryItem.getStart());
		this.mEditTextEditDestination.setText(this.mHistoryItem.getDestination());
		this.mButtonStartNavigation.setOnClickListener(new ButtonListener());
		this.mButtonSaveName.setOnClickListener(new ButtonListener());
	}
	

	private class ButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.startNavigationButton) {
				Intent intent = new Intent(v.getContext(), NavigationActivity.class);
				String[] splitResult = String.valueOf(HistoryItemDetailActivity.this.mEditTextEditDestination.getText()).split(WHITESPACE);
				intent.putExtra(END_ID, splitResult[0]);
				startActivity(intent);
			}
			else if(v.getId() == R.id.btnSaveName){
				HistoryItemDetailActivity.this.logMessage(INFO, "HistoryItemID : "+mIntegerHistoryId);
				HistoryItemDetailActivity.this.logMessage(INFO, "HistoryItemNewName : "+HistoryItemDetailActivity.this.mEditTextEditName.getText().toString());
				HistoryItemDetailActivity.this.mDatabaseHelper.updateHistoryItemName(mIntegerHistoryId,HistoryItemDetailActivity.this.mEditTextEditName.getText().toString());
				
				
			}
		}
	}
}

