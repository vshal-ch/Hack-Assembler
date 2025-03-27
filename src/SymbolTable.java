package src;

import java.util.*;

public class SymbolTable{
    private HashMap<String, Integer> symTable;
    private int varSymIndex = 16;
    public SymbolTable(){
        this.symTable = new HashMap<String,Integer>();
        this.loadPreDefSyms();
    }

    public void addLableSym(String symbol,int lineNo) throws Exception{
        // if a variable with same symbol is already defined 
        if(this.symTable.get(symbol) != null){
            throw new Exception("A label symbol cannot be reused");
        }
        this.symTable.put(symbol,lineNo);
    }

    public int get(String symbol){
        if(this.symTable.get(symbol) != null){
            return (int) this.symTable.get(symbol);
        }
        this.symTable.put(symbol,varSymIndex);
        this.varSymIndex++;
        return this.varSymIndex - 1;
    }

    private void loadPreDefSyms(){
        this.symTable.put("SP",0);
        this.symTable.put("LCL",1);
        this.symTable.put("ARG",2);
        this.symTable.put("THIS",3);
        this.symTable.put("THAT",4);
        for(int i=0;i<16;i++){
            this.symTable.put("R"+i,i);
        }
        this.symTable.put("SCREEN",16384);
        this.symTable.put("KBD",24576);
    }
}