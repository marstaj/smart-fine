package cz.smartfine.pc.query;

import cz.smartfine.model.MobileDeviceDB;
import java.util.List;

/**
 * Posluchač události příchodu zprávy se seznamem zařízení.
 * @author Pavel Brož
 */
public interface IDeviceListLoaderListener {
    
    public void onConnectionTerminated();
    
    /**
     * Handler události příchodu zprávy se seznamem zařízení.
     * @param devices 
     */
    public void onDeviceListReceived(List<MobileDeviceDB> devices);
    
    /**
     * Handler události příchodu zprávy s chybou.
     * @param message 
     */
    public void onErrorReceived(String message);
}
