package cz.smartfine.networklayer.model.pc;

/**
 * Vrací výsledek dotazu.
 *
 * @author Pavel Brož
 */
public class QueryResult {

    private QueryState state;
    private Object result;

    public QueryResult() {
    }

    
    public QueryResult(QueryState state, Object result) {
        this.state = state;
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public QueryState getState() {
        return state;
    }

    public void setState(QueryState state) {
        this.state = state;
    }
}
