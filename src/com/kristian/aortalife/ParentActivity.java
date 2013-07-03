package com.kristian.aortalife;

import com.kristian.aortalife.models.User;
import android.os.Bundle;
import android.view.Window;
import android.app.Activity;

public class ParentActivity extends Activity {
	protected String token;
	protected String fullname;
	protected boolean isLogin;
	protected User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		this.init();
	}
	
	private void init(){
		this.user = new User(this);
		this.isLogin = this.user.isExist();
		this.fullname = this.user.getName();
		this.token = this.user.getToken();
	}
	
	protected void doLogout(){
		this.user.deleteUser();
	}
}
