package com.example.controllers;

import com.example.models.User;

public class UserController extends Thread {
	private User user;
	private int action;
	
	public static final int SIGN_IN = 1;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
	
	public void run(){
		switch (action){
		case SIGN_IN:
				user.signIn();
				break;		
		}
	}
}
