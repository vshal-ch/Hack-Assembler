import java.util.*;

public class SymbolTable{
    private HashMap<String, Integer> varSymTable;
    private HashMap<String, Integer> labelSymTable;
    private int varSymIndex = 16;
    public SymbolTable(){
        this.varSymTable = new HashMap<String,Integer>();
        this.labelSymTable = new HashMap<String,Integer>();
    }

    public void addLableSym(String symbol,int lineNo) throws Exception{
        // if a variable with same symbol is already defined 
        if(this.labelSymTable.get(symbol) != null){
            throw new Exception("A line symbol cannot be reused");
        }
        this.labelSymTable.put(symbol,lineNo);
    }

    public int get(String symbol){
        if(this.labelSymTable.get(symbol) != null){
            return (int) this.labelSymTable.get(symbol);
        }
        this.labelSymTable.put(symbol,varSymIndex);
        this.varSymIndex++;
        return this.varSymIndex - 1;
    }
}