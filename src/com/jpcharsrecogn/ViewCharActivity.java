package com.jpcharsrecogn;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jpchar.structures.KanaSyllabary;

public class ViewCharActivity extends Activity implements OnClickListener {

	private LinearLayout viewLayout;
	private TextView type, name, progressText;
	private ImageView character;
	private ProgressBar progressBar;
	private String extra;
	private int counter, value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set window for this Activity to full screen mode
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.view);

		Bundle extras = getIntent().getExtras();
		extra = extras.getString("type");
		counter = extras.getInt("counter");		
		value = extras.getInt("value");
		
		type = (TextView) findViewById(R.id.typeText);
		name = (TextView) findViewById(R.id.nameText);
		character = (ImageView) findViewById(R.id.charImage);
		viewLayout = (LinearLayout) findViewById(R.id.viewLayout);
		viewLayout.setOnClickListener(this);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressText = (TextView) findViewById(R.id.progressText);
	}

	@Override
	protected void onResume() {
		super.onResume();

		type.setText(getResources()
				.getIdentifier(extra, "string", "com.jpcharsrecogn"));
		progressText.setText(getResources().getString(R.string.progress)+ " " + value + "%");
		if (value < 20) {
			progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.redprogress));
		} else if (value < 60) {
			progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.yellowprogress));
		} else {
			progressBar.setProgressDrawable(getResources().getDrawable(R.drawable.greenprogress));
		}
		progressBar.setMax(100);
		progressBar.setProgress(value);

		if (extra.contains("hiragana")) {
			name.setText(KanaSyllabary.syllabaryHiragana.get(counter).getName());
			character.setImageResource(getResources().getIdentifier(
					extra.substring(0, 1)
							+ KanaSyllabary.syllabaryHiragana.get(counter)
									.getCode(), "drawable", "com.jpcharsrecogn"));
		} else if (extra.contains("katakana")) {
			name.setText(KanaSyllabary.syllabaryKatakana.get(counter).getName());
			character.setImageResource(getResources().getIdentifier(
					extra.substring(0, 1)
							+ KanaSyllabary.syllabaryKatakana.get(counter)
									.getCode(), "drawable", "com.jpcharsrecogn"));
		} else {
			name.setText(KanaSyllabary.kanji.get(counter).getName());
			character.setImageResource(getResources().getIdentifier(
					KanaSyllabary.kanji.get(counter).getKanjiCode(), "drawable",
					"com.jpcharsrecogn"));
		}
		
	}

	@Override
	public void onClick(View v) {
		finish();
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
		newDialog.setTitle("Back Pressed");
		newDialog
				.setMessage("Do you want to get back to the main screen?");
		newDialog.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						close();
						dialog.dismiss();
					}
				});
		newDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		newDialog.show();
	}

	private void close() {
		super.onBackPressed();
		Intent i = new Intent(this, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(i);
	}

}
