package com.visualizerbd.top10downloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MainActivity extends Activity {
	
	TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.txtView);
         new DownloadData().execute("http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/topfreeapplications/limit=10/xml");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private class DownloadData extends AsyncTask<String, Void, String> {
    	
    	String myXmlData;
    	
		@Override
		protected String doInBackground(String... urls) {
			try {				
				myXmlData = downloadXML(urls[0]);				
			} catch(IOException e) {
				return "Unable to download XML file";
				
			}
			return null;
		}
		
		protected void onPostExecute(String result){
			Log.d("OnPostExecute", myXmlData);
			textView.setText(myXmlData);
		}

		private String downloadXML(String theUrl) throws IOException {
			int BUFFER_SIZE = 2000;
			InputStream is = null;
			
			String xmlContents = "";
					
			try{
				URL url = new URL(theUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(10000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				int response = conn.getResponseCode();
				Log.d("DownloadXML", "The response returns is: " + response);
				is = conn.getInputStream();
				
				InputStreamReader isr = new InputStreamReader(is);
				int charRead;
				char[] inputBuffer = new char[BUFFER_SIZE];
				try {
					while ((charRead = isr.read(inputBuffer))>0) {
						String readString = String.copyValueOf(inputBuffer, 0, charRead);
						xmlContents += readString;
						inputBuffer = new char[BUFFER_SIZE];
					}
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}				
				
			} finally {
				if(is != null)
					is.close();
			}				
			return null;
		} 	
	}
}
