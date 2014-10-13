package de.damianbuecker.fhkroutenplaner.activity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see roboguice.activity.RoboActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (this.getIntent().getStringExtra("selectedItem") != null) {
			this.mHistoryItem = new HistoryItem();
			this.mHistoryItem = this.mHistoryItem.fromJson(this.getIntent().getStringExtra("selectedItem"));
		}

		this.editName.setText(this.mHistoryItem.getName());
		this.editDate.setText(String.valueOf(this.mHistoryItem.getDate()));
		this.editTime.setText(String.valueOf(this.mHistoryItem.getTimestamp()));
		this.editStart.setText(this.mHistoryItem.getStart());
		this.editDestination.setText(this.mHistoryItem.getDestination());
	}
}
