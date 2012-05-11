/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.smartfine.pc.query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Pomocná třída pro vytvoření parametrů dotazu.
 * @author Pavel Brož
 */
public class QueryParameterBuilder {

    private Map<String, Object> insertedParams = new HashMap<String, Object>();
    
    public QueryParameterBuilder putObject(String key, Object value){
        insertedParams.put(key, value);
        return this;
    }
    public QueryParameterBuilder putString(String key, String value){
        putObject(key, value);
        return this;
    }
    
    public QueryParameterBuilder putInt(String key, int value){
        putObject(key, value);
        return this;
    }
    
    public QueryParameterBuilder putLong(String key, long value){
        putObject(key, value);
        return this;
    }
    
    public QueryParameterBuilder putIntCollection(String key, List<Integer> value){
        putObject(key, value);
        return this;
    }
    
    public String getParametersString(){
        ArrayList<String> params = new ArrayList<String>();
        StringBuilder strParams = new StringBuilder();
        
        for (Map.Entry<String, Object> param : insertedParams.entrySet()) {
            if (param.getValue() instanceof List){
                params.add(param.getKey() + "=" + createCollection((List<Integer>)param.getValue()));
            }else{
                params.add(param.getKey() + "=" + param.getValue().toString());
            }
        }
        
        for (int i = 0; i < params.size(); i++) {
            strParams.append(params.get(i));
            if (i != params.size()-1){
                strParams.append(';');
            }
        }
        
        return strParams.toString();
    }
    
    private String createCollection(List<Integer> list){
        StringBuilder str = new StringBuilder("{");
        
        for (int i = 0; i < list.size(); i++) {
            str.append(list.get(i));
            if (i != list.size()-1){
                str.append(',');
            }
        }
        
        str.append('}');
        
        return str.toString();
    }
}
