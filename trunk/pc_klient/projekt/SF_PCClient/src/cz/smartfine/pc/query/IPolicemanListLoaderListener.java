package cz.smartfine.pc.query;

import cz.smartfine.model.PolicemanDB;
import java.util.List;

/**
 * Posluchač události příchodu zprávy se seznamem policistů.
 * @author Pavel Brož
 */
public interface IPolicemanListLoaderListener {
    
    public void onConnectionTerminated();
    
    /**
     * Handler události příchodu zprávy se seznamem policistů.
     * @param policemen 
     */
    public void onPolicemanListReceived(List<PolicemanDB> policemen);
    
    /**
     * Handler události příchodu zprávy s chybou.
     * @param message 
     */
    public void onErrorReceived(String message);
}
