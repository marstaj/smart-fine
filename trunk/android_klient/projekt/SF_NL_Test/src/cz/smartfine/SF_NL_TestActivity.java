package cz.smartfine;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;

import cz.smartfine.networklayer.ConnectionProvider;
import cz.smartfine.networklayer.business.LoginProvider;
import cz.smartfine.networklayer.business.listeners.ILoginProviderListener;
import cz.smartfine.networklayer.links.SecuredMobileLink;
import cz.smartfine.networklayer.model.LoginFailReason;
import cz.smartfine.networklayer.networkinterface.SimpleNetworkInterface;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SF_NL_TestActivity extends Activity implements ILoginProviderListener{
	
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
    	
    }
    

    public void butLogin(View theButton){
    	LoginProvider lp = new LoginProvider(cp.getNetworkInterface(), getApplicationContext(), this);
    	lp.login(login, login, imei);
    	
    }
    
    
    
    public void butConnectClick(View theButton){
        InputStream in = this.getResources().openRawResource(R.raw.ssltestcert);
        
    	SecuredMobileLink link;
		try {
			link = new SecuredMobileLink(address, in, "ssltest");
	    	SimpleNetworkInterface ni = new SimpleNetworkInterface();
	    	cp = new ConnectionProvider(getApplicationContext(), link, ni);
	    	
	    	t.setText(String.valueOf(cp.connect()));
	    	
		} catch (KeyManagementException e) {
			t.setText("Chyba 1");
		} catch (KeyStoreException e) {
			t.setText("Chyba 2");
		} catch (CertificateException e) {
			t.setText("Chyba 3");
		} catch (IOException e) {
			t.setText("Chyba 4");
		}

    } //konec metody

	public void onConnectionTerminated() {
		//t.setText("EVENT: CONECTION TERMINATED");
	}

	public void onLoginConfirmed() {
		//t.setText("EVENT: LOGIN CONFIRMED");
	}

	public void onLoginFailed(LoginFailReason reason) {
		//t.setText("EVENT: LOGIN FAILED");
	}

	public void onLogout() {
		//t.setText("EVENT: LOGOUT");
	}

	public void onMessageSent() {
		//t.setText("EVENT: MESSAGE SENT");
	}
}