package com.example.resourcelistener;

import com.example.models.User;

public interface UserListener	extends	RestfulResourceListener {
	
	public void signedIn(User user);
}
