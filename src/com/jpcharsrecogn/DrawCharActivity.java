package com.jpcharsrecogn;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawCharActivity extends Activity implements OnClickListener {

	private TextView type, name;
	private ImageView clean, edit, next;
	private String extra;
	private float smallBrush, mediumBrush, largeBrush;
	private DrawCharEventView drawView;
	private int counter, value;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// set window for this Activity to full screen mode
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.draw);

		drawView = (DrawCharEventView) findViewById(R.id.drawView);

		type = (TextView) findViewById(R.id.typeText);
		name = (TextView) findViewById(R.id.nameText);
		name.setText("Please draw a character");
		clean = (ImageView) findViewById(R.id.cleanImage);
		clean.setOnClickListener(this);
		edit = (ImageView) findViewById(R.id.editImage);
		edit.setOnClickListener(this);
		next = (ImageView) findViewById(R.id.nextImage);
		next.setOnClickListener(this);

		smallBrush = getResources().getInteger(R.integer.small_size);
		mediumBrush = getResources().getInteger(R.integer.medium_size);
		largeBrush = getResources().getInteger(R.integer.large_size);

		drawView.setBrushSize(mediumBrush);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Bundle extras = getIntent().getExtras();
		extra = extras.getString("type");

		type.setText(getResources()
				.getIdentifier(extra, "string", "com.jpcharsrecogn"));
		drawView.startNew();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.nextImage:
				Bitmap bmp = drawView.getBitmap(), sbmp = null;
				int x = -1, y = -1, firstX = Integer.MAX_VALUE, firstY = Integer.MAX_VALUE, lastX = -1, lastY = -1, diffX, diffY;

				// find the used boundaries to cut useless, empty pixels
				bmp = Bitmap.createScaledBitmap(bmp, bmp.getWidth() / 2,
						bmp.getHeight() / 2, false);
				while (++x < bmp.getWidth()) {
					y = -1;
					while (++y < bmp.getHeight()) {
						if (bmp.getPixel(x, y) != 0) {
							if (firstX > x) {
								firstX = x;
							}
							if (firstY > y) {
								firstY = y;
							}
							if (lastX < x) {
								lastX = x;
							}
							if (lastY < y) {
								lastY = y;
							}
						}
					}
				}
				diffX = lastX - firstX;
				diffY = lastY - firstY;
				bmp = Bitmap.createBitmap(bmp, firstX, firstY, diffX, diffY);
				sbmp = Bitmap.createScaledBitmap(bmp, 30, 30, false);
				// create resized binary matrix
				List<Double> neuralInput = new ArrayList<Double>(900);
				y = -1;
				while (++y < sbmp.getHeight()) {
					x = -1;
					while (++x < sbmp.getWidth()) {
						if (sbmp.getPixel(x, y) == Color.BLACK) {
							//data += "1 ";
							neuralInput.add(1.0);
						} else {
							//data += "0 ";
							neuralInput.add(0.0);
						}
					}
				}
			neuralFeedforward(neuralInput);
			Intent i = new Intent(getApplicationContext(), ViewCharActivity.class);
			i.putExtra("type", extra);
			i.putExtra("counter", counter);
			i.putExtra("value", value);
			startActivity(i);
			break;

		case R.id.editImage:
			final Dialog brushDialog = new Dialog(this);
			brushDialog.setTitle("Brush size:");
			brushDialog.setContentView(R.layout.brush);
			ImageButton smallBtn = (ImageButton) brushDialog
					.findViewById(R.id.small_brush);
			smallBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(smallBrush);
					drawView.setLastBrushSize(smallBrush);
					brushDialog.dismiss();
				}
			});
			ImageButton mediumBtn = (ImageButton) brushDialog
					.findViewById(R.id.medium_brush);
			mediumBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(mediumBrush);
					drawView.setLastBrushSize(mediumBrush);
					brushDialog.dismiss();
				}
			});
			ImageButton largeBtn = (ImageButton) brushDialog
					.findViewById(R.id.large_brush);
			largeBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					drawView.setBrushSize(largeBrush);
					drawView.setLastBrushSize(largeBrush);
					brushDialog.dismiss();
				}
			});
			brushDialog.show();
			break;
		case R.id.cleanImage:
			AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
			newDialog.setTitle("New drawing");
			newDialog
					.setMessage("Start new drawing (you will lose the current drawing)?");
			newDialog.setPositiveButton("Yes",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							drawView.startNew();
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
			break;
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
		newDialog.setTitle("Back Pressed");
		newDialog
				.setMessage("Back to main screen (you will lose the current progress)?");
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
	}
	
	private void neuralFeedforward(List<Double> neuralInput) {
		int highestNetworkOutputNumber = 0;
		double highestNetworkOutputValue;
		MainActivity.neunet.setInputFirst(neuralInput);
		MainActivity.neunet.computeOutput();		
		highestNetworkOutputValue = 0.0;
        for (int j = 0; j < MainActivity.neunet.getNetworkOutput().size(); j++) {
            if (MainActivity.neunet.getNetworkOutput().get(j) > highestNetworkOutputValue) {
                highestNetworkOutputValue = MainActivity.neunet.getNetworkOutput().get(j);
                highestNetworkOutputNumber = j;
            }
        }  
		
		counter = highestNetworkOutputNumber;
		value = (int) (highestNetworkOutputValue * 100);
	}

}
