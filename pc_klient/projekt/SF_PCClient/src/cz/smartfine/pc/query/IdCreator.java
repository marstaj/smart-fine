package cz.smartfine.pc.query;

/**
 * Vytváří unikátní id pro dotazy na server.
 * @author Pavel Brož
 */
public class IdCreator {
    
    private static int lastId = -1;
    
    public static synchronized int getID(){
        return ++lastId;
    }
}
