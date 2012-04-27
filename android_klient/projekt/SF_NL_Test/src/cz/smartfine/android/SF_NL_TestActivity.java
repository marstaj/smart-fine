package cz.smartfine.android;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;

import cz.smartfine.R;
import cz.smartfine.android.networklayer.ConnectionProvider;
import cz.smartfine.android.networklayer.business.LoginProvider;
import cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener;
import cz.smartfine.android.networklayer.links.SecuredMobileLink;
import cz.smartfine.android.networklayer.model.LoginFailReason;
import cz.smartfine.android.networklayer.networkinterface.SimpleNetworkInterface;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class SF_NL_TestActivity extends Activity implements ILoginProviderListener{

	protected static final String PREF_BADGENUMBER_KEY_NAME = "login_badgenumber";
	protected static final String PREF_PIN_KEY_NAME = "login_pin";
	
	TextView t;
	ConnectionProvider cp;
	InetSocketAddress address = new InetSocketAddress("192.168.0.10", 25000);
	
	int login = 123456;
	int pin = 54321;
	String imei = "0123456789ABCDE";
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        t = (TextView) this.findViewById(R.id.textpole);
    }

    public void butIsCon(View theButton){
    	t.setText("BG: " + String.valueOf(PreferenceManager.getDefaultSharedPreferences(this.getBaseContext()).getInt(PREF_BADGENUMBER_KEY_NAME, -1))
    			+ " / PIN: " +String.valueOf(PreferenceManager.getDefaultSharedPreferences(this.getBaseContext()).getInt(PREF_PIN_KEY_NAME, -1)));
    }
    

    public void butLogin(View theButton){
    	LoginProvider lp = new LoginProvider(cp.getNetworkInterface(), this.getBaseContext(), this);
    	lp.login(login, pin, imei);
    	
    }
    
    
    
    public void butConnectClick(View theButton){
        InputStream in = this.getResources().openRawResource(R.raw.ssltestcert);
        
    	SecuredMobileLink link;
		try {
			link = new SecuredMobileLink(address, in, "ssltest");
	    	SimpleNetworkInterface ni = new SimpleNetworkInterface();
	    	cp = new ConnectionProvider(this.getBaseContext(), link, ni);
	    	
	    	t.setText(String.valueOf(cp.connect()));
	    	
		} catch (Exception e) {
			t.setText("Chyba 4");
		}

    } //konec metody

    public void butLogout(View theButton){
    	LoginProvider lp = new LoginProvider(cp.getNetworkInterface(), this.getBaseContext(), this);
    	lp.logout();
    	cp.disconnect();
    }
    
    public void butConAndLog(View theButton){
    	t.setText(String.valueOf(cp.connectAndLogin()));
    	
    }
    
	public void onConnectionTerminated() {
		//t.setText("EVENT: CONECTION TERMINATED");
		System.out.println("ANDROID: ACTIVITA EVENT: CONECTION TERMINATED");
	}

	public void onLoginConfirmed() {
		//t.setText("EVENT: LOGIN CONFIRMED");
		System.out.println("ANDROID: ACTIVITA EVENT: LOGIN CONFIRMED");
	}

	public void onLoginFailed(LoginFailReason reason) {
		//t.setText("EVENT: LOGIN FAILED");
		System.out.println("ANDROID: ACTIVITA EVENT: LOGIN FAILED");
	}

	public void onLogout() {
		//t.setText("EVENT: LOGOUT");
		System.out.println("ANDROID: ACTIVITA EVENT: LOGOUT");
	}

	public void onMessageSent() {
		//t.setText("EVENT: MESSAGE SENT");
		System.out.println("ANDROID: ACTIVITA EVENT: MESSAGE SENT");
	}
}