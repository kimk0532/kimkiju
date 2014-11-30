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
				String s1 = input1.getText().toString();
				String s2 = input2.getText().toString();
				if(!s1.equals("") && !s2.equals("")){
					a = Double.parseDouble(s1);
					b = Double.parseDouble(s2);
					res = a + b;
					tv.setText("계산결과 : "+res);
				}
				else
					Toast.makeText(getApplicationContext(), "숫자를 입력하세요!", Toast.LENGTH_SHORT).show();
			}
		});
		btn = (Button) findViewById(R.id.btnsub);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String s1 = input1.getText().toString();
				String s2 = input2.getText().toString();
				if(!s1.equals("") && !s2.equals("")){
					a = Double.parseDouble(s1);
					b = Double.parseDouble(s2);
					res = a - b;
					tv.setText("계산결과 : "+res);
				}
				else
					Toast.makeText(getApplicationContext(), "숫자를 입력하세요!", Toast.LENGTH_SHORT).show();
			}
		});
		btn = (Button) findViewById(R.id.btnmul);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String s1 = input1.getText().toString();
				String s2 = input2.getText().toString();
				if(!s1.equals("") && !s2.equals("")){
					a = Double.parseDouble(s1);
					b = Double.parseDouble(s2);
					res = a * b;
					tv.setText("계산결과 : "+res);
				}
				else
					Toast.makeText(getApplicationContext(), "숫자를 입력하세요!", Toast.LENGTH_SHORT).show();
			}
		});
		btn = (Button) findViewById(R.id.btndiv);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String s1 = input1.getText().toString();
				String s2 = input2.getText().toString();
				if(!s1.equals("") && !s2.equals("")){
					if(s2.equals("0"))
							Toast.makeText(getApplicationContext(), "0으로 나눌 수 없습니다!", Toast.LENGTH_SHORT).show();
					else{
						a = Double.parseDouble(s1);
						b = Double.parseDouble(s2);
						res = a / b;
						tv.setText("계산결과 : "+res);
					}
				}
				else
					Toast.makeText(getApplicationContext(), "숫자를 입력하세요!", Toast.LENGTH_SHORT).show();
			}
		});
		btn = (Button) findViewById(R.id.btnmod);
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String s1 = input1.getText().toString();
				String s2 = input2.getText().toString();
				if(!s1.equals("") && !s2.equals("")){
					if(s2.equals("0"))
							Toast.makeText(getApplicationContext(), "0으로 나눌 수 없습니다!", Toast.LENGTH_SHORT).show();
					else{
						a = Double.parseDouble(s1);
						b = Double.parseDouble(s2);
						res = a % b;
						tv.setText("계산결과 : "+res);
					}
				}
				else
					Toast.makeText(getApplicationContext(), "숫자를 입력하세요!", Toast.LENGTH_SHORT).show();
			}
		});
	}

}
