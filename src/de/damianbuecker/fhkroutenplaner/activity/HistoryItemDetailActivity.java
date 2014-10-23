package de.damianbuecker.fhkroutenplaner.activity;

import com.j256.ormlite.android.apptools.OpenHelperManager;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import de.damianbuecker.fhkroutenplaner.controller.DateTimeController;
import de.damianbuecker.fhkroutenplaner.databaseaccess.DatabaseHelper;
import de.damianbuecker.fhkroutenplaner.model.HistoryItem;

/**
 * The Class HistoryItemDetailActivity.
 */
@ContentView(R.layout.history_item_detail_view)
public class HistoryItemDetailActivity extends ModifiedViewActivityImpl {

	/** The m history item. */
	private HistoryItem mHistoryItem;

	/** The name. */
	@InjectView(R.id.name)
	private TextView name;

	/** The edit name. */
	@InjectView(R.id.editName)
	private EditText editName;

	/** The date. */
	@InjectView(R.id.date)
	private TextView date;

	/** The edit date. */
	@InjectView(R.id.editDate)
	private EditText editDate;

	/** The time. */
	@InjectView(R.id.time)
	private TextView time;

	/** The edit time. */
	@InjectView(R.id.editTime)
	private EditText editTime;

	/** The start. */
	@InjectView(R.id.start)
	private TextView start;

	/** The edit start. */
	@InjectView(R.id.editStart)
	private EditText editStart;

	/** The destination. */
	@InjectView(R.id.destination)
	private TextView destination;

	/** The edit destination. */
	@InjectView(R.id.editDestination)
	private EditText editDestination;
	
	@InjectView(R.id.startNavigationButton)
	private Button startNavigationButton;
	
	@InjectView(R.id.btnSaveName)
	private Button btnSaveName;

	private DateTimeController mDateTimeController;
	
	private DatabaseHelper mDatabaseHelper;
	
	private Integer historyId; 
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

		
		if (this.getIntent().getStringExtra("selectedItem") != null) {
			this.mHistoryItem = new HistoryItem();
			this.mHistoryItem = this.mHistoryItem.fromJson(this.getIntent().getStringExtra("selectedItem"));
		}
		this.historyId = this.mHistoryItem.getId();
		this.editName.setText(this.mHistoryItem.getName());
		this.editDate.setText(String.valueOf(this.mDateTimeController.toDate(this.mHistoryItem.getDate())));
		this.editTime.setText(String.valueOf(this.mDateTimeController.toDate(this.mHistoryItem.getTimestamp())));
		this.editStart.setText(this.mHistoryItem.getStart());
		this.editDestination.setText(this.mHistoryItem.getDestination());
		this.startNavigationButton.setOnClickListener(new ButtonListener());
		this.btnSaveName.setOnClickListener(new ButtonListener());
	}
	

	private class ButtonListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.startNavigationButton) {
				Intent intent = new Intent(v.getContext(), NavigationActivity.class);
				String[] splitResult = String.valueOf(HistoryItemDetailActivity.this.editDestination.getText()).split(" ");
				intent.putExtra("endID", splitResult[0]);
				startActivity(intent);
			}
			else if(v.getId() == R.id.btnSaveName){
				HistoryItemDetailActivity.this.logMessage("INFO", "HistoryItemID : "+historyId);
				HistoryItemDetailActivity.this.logMessage("INFO", "HistoryItemNewName : "+HistoryItemDetailActivity.this.editName.getText().toString());
				HistoryItemDetailActivity.this.mDatabaseHelper.updateHistoryItemName(historyId,HistoryItemDetailActivity.this.editName.getText().toString());
				
				
			}
		}
	}
}

