package com.example.test;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



public class MainActivity extends Activity {
	
	private Button sButton;
	private EditText editText;

	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.setThreadPolicy(policy); 
        sButton = (Button) findViewById(R.id.button1);
        editText = (EditText)findViewById(R.id.editText1);
        
        sButton.setOnClickListener(new View.OnClickListener(){
       	 //private Button.OnClickListener ButtonCapture = new Button.OnClickListener(){
       			
       			public void onClick(View v){
       					
       				HttpClient httpclient = new DefaultHttpClient();
       				HttpResponse response;
       				String responseString = null;
       				try {
       				 HttpPost httpPost = new HttpPost("http://api.wunderground.com/api/36b799dc821d5836/conditions/q/PA/Horsham.json");
       	             response = httpclient.execute(httpPost);
       				    StatusLine statusLine = response.getStatusLine();
       				    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
       				        ByteArrayOutputStream out = new ByteArrayOutputStream();
       				        response.getEntity().writeTo(out);
       				        out.close();
       				        responseString = out.toString();
       				     String combine = JSONAnalysis(responseString);
       				     String temp = combine.split(";")[0];
       				     String weather = combine.split(";")[1];
       				     
       				     editText.setText("temp is: "+temp + "F"+"\n" + "weather is: "+weather);
       				    } else{
       				        //Closes the connection.
       				        response.getEntity().getContent().close();
       				        throw new IOException(statusLine.getReasonPhrase());
       				    }
       				} catch (ClientProtocolException e) {
       				    e.printStackTrace();
       				} catch (IOException e) {
       				    e.printStackTrace();
       				}
       			 	
                            }
                        });
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public String JSONAnalysis(String jsonString)
    {
    	String combine="1";
    	String temperature="";
    	String weather="";
    	JSONObject jsonObj;
		try {
			jsonObj = new JSONObject(jsonString);
			JSONObject  obser=jsonObj.getJSONObject("current_observation");    	
	    	
	    	temperature=obser.getString("temp_f");
	    	weather = obser.getString("weather");
	       	
	    	combine = temperature+";"+weather;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
    
    	
    	return combine;
    }
    
}
