package com.example.calculator;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	EditText input1;
	EditText input2;
	TextView tv;
	double a, b, res;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		input1 = (EditText) findViewById(R.id.editText1);
		input2 = (EditText) findViewById(R.id.editText2);
		tv = (TextView) findViewById(R.id.textView1);
		Button btn = (Button) findViewById(R.id.btnsum);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String s = input1.getText().toString();
				a = Double.parseDouble(s);
				s = input2.getText().toString();
				b = Double.parseDouble(s);
				res = a + b;
				tv.setText("계산결과 : "+res);
			}
		});
		btn = (Button) findViewById(R.id.btnsub);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String s = input1.getText().toString();
				a = Double.parseDouble(s);
				s = input2.getText().toString();
				b = Double.parseDouble(s);
				res = a - b;
				tv.setText("계산결과 : "+res);
			}
		});
		btn = (Button) findViewById(R.id.btnmul);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String s = input1.getText().toString();
				a = Double.parseDouble(s);
				s = input2.getText().toString();
				b = Double.parseDouble(s);
				res = a * b;
				tv.setText("계산결과 : "+res);
			}
		});
		btn = (Button) findViewById(R.id.btndiv);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String s = input1.getText().toString();
				a = Double.parseDouble(s);
				s = input2.getText().toString();
				b = Double.parseDouble(s);
				try {
					res = a / b;
				} catch (ArithmeticException e) {
					Toast.makeText(getApplicationContext(), "0으로 나눌수 없습니다.", Toast.LENGTH_SHORT).show();
				}
				tv.setText("계산결과 : "+res);
			}
		});
		btn = (Button) findViewById(R.id.btnmod);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String s = input1.getText().toString();
				a = Double.parseDouble(s);
				s = input2.getText().toString();
				b = Double.parseDouble(s);
				res = a % b;
				tv.setText("계산결과 : "+res);
			}
		});
	}

}
