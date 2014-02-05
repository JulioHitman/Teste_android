package com.example.views;

import com.example.teste.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class MainView extends Activity {

	protected EditText email;
	protected EditText password;

	protected Button sign_in;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		email = (EditText) findViewById(R.id.email);
		password = (EditText) findViewById(R.id.password);
		
		sign_in = (Button) findViewById(R.id.sign_in);

	}
}
