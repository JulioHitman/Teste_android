package com.example.teste;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.controllers.UserController;
import com.example.models.User;
import com.example.views.MainView;

public class MainActivity extends MainView {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sign_in.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String emails = email.getText().toString();
				String passwords = password.getText().toString();
				
				user.setEmail(emails);
				user.setPassword(passwords);
				
				UserController userController = new UserController();
				userController.setUser(user);
				userController.setAction(UserController.SIGN_IN);
				
				userController.start();												
				
			}
			
		} );
		
	}
	public void signedIn(final User user){
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				
				Toast toast = Toast.makeText(MainActivity.this,"blobblob", Toast.LENGTH_SHORT);
				toast.setGravity(Gravity.CENTER, Gravity.CENTER, Gravity.CENTER);
				toast.show();
				
			}
		});
	}

	
	
}
