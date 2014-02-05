package com.example.teste;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.views.MainView;

public class MainActivity extends MainView {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sign_in.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				Toast toast = Toast.makeText(MainActivity.this,"Blabla", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, Gravity.CENTER, Gravity.CENTER);
				toast.show();
			}
			
		} );
		
	}

	
	
}
