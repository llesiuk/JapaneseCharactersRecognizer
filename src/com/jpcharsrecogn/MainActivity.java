package com.jpcharsrecogn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.jpchar.structures.KanaSyllabary;
import com.neuralnetwork.Network;

public class MainActivity extends Activity implements OnClickListener {

	private LinearLayout hiraganaLayout, katakanaLayout, kanjiLayout;
	static public Network neunet;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set window for this Activity to full screen mode
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		
		neunet = new Network(this);

		// call the initialization elements
		initialize();
	}

	private void initialize() {
		// create instance of structure Class with static ArrayList
		@SuppressWarnings("unused")
		KanaSyllabary kanaSyllabary = new KanaSyllabary();

		// initialize interface elements with click listeners
		hiraganaLayout = (LinearLayout) findViewById(R.id.hiraganaLayout);
		hiraganaLayout.setOnClickListener(this);
		katakanaLayout = (LinearLayout) findViewById(R.id.katakanaLayout);
		katakanaLayout.setOnClickListener(this);
		kanjiLayout = (LinearLayout) findViewById(R.id.kanjiLayout);
		kanjiLayout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent i = null;
		String extra = null;
		switch (v.getId()) {
		case R.id.hiraganaLayout:
			extra = "hiragana";
			break;

		case R.id.katakanaLayout:
			extra = "katakana";
			break;

		case R.id.kanjiLayout:
			extra = "kanji";
			break;
		}
		i = new Intent(getApplicationContext(), DrawCharActivity.class);
		i.putExtra("type", extra);
		startActivity(i);
	}
}