package com.example.views;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.teste.R;

public class MainView extends BaseView {

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
