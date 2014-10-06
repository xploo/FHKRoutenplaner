package de.damianbuecker.fhkroutenplaner.activity;

import roboguice.inject.ContentView;
import roboguice.inject.InjectView;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import de.damianbuecker.fhkroutenplaner.model.HistoryItem;

@ContentView(R.layout.history_item_detail_view)
public class HistoryItemDetailActivity extends ModifiedViewActivityImpl{

	private HistoryItem mHistoryItem;
	
	@InjectView(R.id.name)				private TextView name;
	@InjectView(R.id.editName)			private EditText editName;
	@InjectView(R.id.date)				private TextView date;
	@InjectView(R.id.editDate)			private EditText editDate;
	@InjectView(R.id.time)				private TextView time;
	@InjectView(R.id.editTime)			private EditText editTime;
	@InjectView(R.id.start)				private TextView start;
	@InjectView(R.id.editStart)			private EditText editStart;
	@InjectView(R.id.destination)		private TextView destination;
	@InjectView(R.id.editDestination)	private EditText editDestination;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if(this.getIntent().getStringExtra("selectedItem") != null) {
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
