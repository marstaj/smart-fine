package cz.smartfine.android;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import cz.smartfine.android.model.Law;
import cz.smartfine.android.model.Ticket;
import cz.smartfine.android.model.Waypoint;
import cz.smartfine.android.networklayer.ConnectionProvider;
import cz.smartfine.android.networklayer.business.LoginProvider;
import cz.smartfine.android.networklayer.business.listeners.IAuthenticationProtocolListener;
import cz.smartfine.android.networklayer.business.listeners.ILoginProviderListener;
import cz.smartfine.android.networklayer.business.listeners.ISMSParkingProtocolListener;
import cz.smartfine.android.networklayer.business.listeners.ISPCCheckProtocolListener;
import cz.smartfine.android.networklayer.dataprotocols.*;
import cz.smartfine.android.networklayer.links.SecuredMobileClientLink;
import cz.smartfine.networklayer.model.mobile.AuthenticationFailReason;
import cz.smartfine.networklayer.model.mobile.LoginFailReason;
import cz.smartfine.networklayer.model.mobile.SMSParkingInfo;
import cz.smartfine.networklayer.model.mobile.SPCInfo;
import cz.smartfine.networklayer.networkinterface.SimpleNetworkInterface;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Date;

public class SF_NL_TestActivity extends Activity implements
        ILoginProviderListener, ISMSParkingProtocolListener,
        ISPCCheckProtocolListener, IAuthenticationProtocolListener {

    protected static final String PREF_BADGENUMBER_KEY_NAME = "login_badgenumber";
    protected static final String PREF_PIN_KEY_NAME = "login_pin";
    TextView t;
    ConnectionProvider cp;
    InetSocketAddress address = new InetSocketAddress("192.168.0.10", 25000);
    int login = 123456;
    int pin = 54321;
    String imei = "0123456789ABCDE";

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        t = (TextView) this.findViewById(R.id.textpole);
    }

    public void butIsCon(View theButton) {
        t.setText("BG: "
                + String.valueOf(PreferenceManager.getDefaultSharedPreferences(
                this.getBaseContext()).getInt(
                PREF_BADGENUMBER_KEY_NAME, -1))
                + " / PIN: "
                + String.valueOf(PreferenceManager.getDefaultSharedPreferences(
                this.getBaseContext()).getInt(PREF_PIN_KEY_NAME, -1)));
    }

    public void butLogin(View theButton) {
        LoginProvider lp = new LoginProvider(cp.getNetworkInterface(),
                this.getBaseContext(), this);
        lp.login(login, pin, imei);

    }

    public void butConnectClick(View theButton) {
        InputStream in = this.getResources().openRawResource(R.raw.ssltestcert);

        SecuredMobileClientLink link;
        try {
            link = new SecuredMobileClientLink(address, in, "ssltest");
            SimpleNetworkInterface ni = new SimpleNetworkInterface();
            cp = new ConnectionProvider(this.getBaseContext(), link, ni);

            t.setText(String.valueOf(cp.connect()));

        } catch (Exception e) {
            t.setText("Chyba 4");
        }

    } // konec metody

    public void butLogout(View theButton) {
        LoginProvider lp = new LoginProvider(cp.getNetworkInterface(),
                this.getBaseContext(), this);
        lp.logout();
        cp.disconnect();
    }

    public void butSendTicket(View theButton) {
        t.setText("ticket send");
        TicketSyncProtocol p = new TicketSyncProtocol(cp.getNetworkInterface());
        Ticket t = new Ticket();

        t.setBadgeNumber(login);
        t.setSpz("ABCDEF");
        t.setCity("praha");
        t.setStreet("Příliš žluťoučký kůň úpěl ďábelské ódy");
        t.setDate(new Date());

        Law l = new Law();
        l.setCollection(10);
        l.setLawNumber(20);
        l.setLetter("a");
        l.setParagraph(30);
        l.setRuleOfLaw(40);

        t.setLaw(l);
        t.setLocation("lokace");
        t.setMoveableDZ(true);
        t.setMpz("cz");
        t.setNumber(987);
        t.setPrinted(true);
        t.setSpzColor("bílá");
        t.setTow(true);
        t.setVehicleBrand("škoda");
        t.setVehicleType("osobní");

        try {
            p.sendTicket(t, 987654);
        } catch (IOException e) {
            System.out.println("ANDROID: PROBLÉM PŘI SERIALIZACI LÍSTKU");
        }
    }

    public void butConAndLog(View theButton) {
        t.setText(String.valueOf(cp.connectAndLogin()));
    }

    public void butSendPSMS(View theButton) {
        t.setText("psms send");
        SMSParkingProtocol p = new SMSParkingProtocol(cp.getNetworkInterface(), this);
        p.checkParking("abcdef");
    }

    public void butSendPPK(View theButton) {
        t.setText("ppk send");
        SPCCheckProtocol p = new SPCCheckProtocol(cp.getNetworkInterface(), this);
        p.checkSPC("987654321"); //"0123456789"
    }

    public void butSendGeo(View theButton) {
        t.setText("geo send");
        GeoDataProtocol p = new GeoDataProtocol(cp.getNetworkInterface());

        ArrayList<Waypoint> wps = new ArrayList<Waypoint>();

        long now = (new Date()).getTime();
        Waypoint wp;

        for (int i = 0; i < 2500; i++) {
            wp = new Waypoint();
            wp.setLongtitude(i);
            wp.setLatitude(i * 2);
            wp.setTime(now + (i * 10000));
            wps.add(wp);
        }
        try {
            p.sendGeoData(wps);
        } catch (IOException e) {
            System.out.println("ANDROID: PROBLÉM PŘI SERIALIZACI GEO DAT");
        }
    }

    public void butSendAuth(View theButton) {
        t.setText("auth send");
        AuthenticationProtocol p = new AuthenticationProtocol(
                cp.getNetworkInterface(), this);
        p.authenticate(987654, 56789);
    }

    public void onConnectionTerminated() {
        // t.setText("EVENT: CONECTION TERMINATED");
        System.out.println("ANDROID: ACTIVITA EVENT: CONECTION TERMINATED");
    }

    public void onLoginConfirmed() {
        // t.setText("EVENT: LOGIN CONFIRMED");
        System.out.println("ANDROID: ACTIVITA EVENT: LOGIN CONFIRMED");
    }

    public void onLoginFailed(LoginFailReason reason) {
        // t.setText("EVENT: LOGIN FAILED");
        System.out.println("ANDROID: ACTIVITA EVENT: LOGIN FAILED + REASON: " + reason.toString());
    }

    public void onLogout() {
        // t.setText("EVENT: LOGOUT");
        System.out.println("ANDROID: ACTIVITA EVENT: LOGOUT");
    }

    public void onMessageSent() {
        // t.setText("EVENT: MESSAGE SENT");
        System.out.println("ANDROID: ACTIVITA EVENT: MESSAGE SENT");
    }

    @Override
    public void onAuthenticationConfirmed() {
        System.out.println("ANDROID: ACTIVITA EVENT: AUTHENTICATION CONFIRMED");

    }

    @Override
    public void onAuthenticationFailed(AuthenticationFailReason reason) {
        System.out.println("ANDROID: ACTIVITA EVENT: AUTHENTICATION FAILED");

    }

    @Override
    public void onReceivedSPCInfo(SPCInfo spcInfo) {
        System.out.println("ANDROID: ACTIVITA EVENT: PPK INFO NUMBER:"
                + spcInfo.getSpcNumber() + " STATUS: "
                + spcInfo.getSpcStatus().toString());

    }

    @Override
    public void onReceivedSMSParkingInfo(SMSParkingInfo parkingInfo) {
        System.out.println("ANDROID: ACTIVITA EVENT: SMS INFO SPZ:"
                + parkingInfo.getVehicleRegistrationPlate() + " STATUS: "
                + parkingInfo.getParkingStatus().toString() + " SINCE: "
                + parkingInfo.getParkingSince().toString() + " UNTIL: "
                + parkingInfo.getParkingUntil().toString());

    }
}