package com.kristian.aortalife.connections;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import zephyr.android.HxMBT.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class Bluetooth extends ConnectListenerImpl{
	private Handler NewHandler; 
	private String activity = null; 
	private String key = null;
	private String token = null;
	private int HR_SPD_DIST_PACKET =0x26;
	private int count = 0;
	private boolean isLogin = false;
	
	private final int HEART_RATE = 0x100;
	private HRSpeedDistPacketInfo HRSpeedDistPacket = new HRSpeedDistPacketInfo();
	
	public Bluetooth(Handler handler, String activity, String key, String token, boolean login) {
		super(handler, null);
		
		if (this.activity == null)
			this.activity = activity;
		
		if (this.key == null)
			this.key = key;
		
		if (this.token == null)
			this.token = token;
		
		this.NewHandler = handler;
		this.isLogin = login;
	}
	
	public void Connected(ConnectedEvent<BTClient> eventArgs) {
		ZephyrProtocol protocol = new ZephyrProtocol(eventArgs.getSource().getComms());
		
		protocol.addZephyrPacketEventListener(new ZephyrPacketListener() {
			public void ReceivedPacket(ZephyrPacketEvent eventArgs) {
				ZephyrPacketArgs msg = eventArgs.getPacket();
				count++;
				
				if (HR_SPD_DIST_PACKET==msg.getMsgID()){
					if (count == 2){
						count = 0;
						byte [] DataArray = msg.getBytes();
						int HRate =  HRSpeedDistPacket.GetHeartRate(DataArray);
						int Bat = HRSpeedDistPacket.GetBatteryChargeInd(DataArray);
						
						Message text1 =NewHandler.obtainMessage(HEART_RATE);
						Bundle b1 = new Bundle();
						
						b1.putString("HeartRate", String.valueOf(HRate));
						b1.putString("Battery", String.valueOf(Bat));
						text1.setData(b1);
						
						NewHandler.sendMessage(text1);
						
						sendData(String.valueOf(HRate));
					}
				}
				
			}
		});
	}
	
	//send data 
	public void sendData(String hr){
		if (this.isLogin){
			String timestamp =  String.valueOf(System.currentTimeMillis());
	    	HttpClient httpclient = new DefaultHttpClient();    
	    	HttpPost httppost = new HttpPost("http://192.168.0.101:3000/api/v1/activities.json");
	        try {               
		         List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);        
		         nameValuePairs.add(new BasicNameValuePair("token", token));        
		         nameValuePairs.add(new BasicNameValuePair("time", timestamp));     
		         nameValuePairs.add(new BasicNameValuePair("key", key));     
		         nameValuePairs.add(new BasicNameValuePair("heartrate", hr));     
		         nameValuePairs.add(new BasicNameValuePair("activity", activity));     
		
		         httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));               
		         httpclient.execute(httppost);   
	        } catch (ClientProtocolException e) {	
	        } catch (IOException e){          
	        }
		}
    }	
}