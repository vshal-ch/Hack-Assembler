
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


public class Parser{
    public static final int A_INSTRUCTION = 1;
    public static final int C_INSTRUCTION = 2;
    public static final int L_INSTRUCTION = 3;
    private Scanner scanner;
    public String currentInstruction;
    private String next;
    public int currentInstrType;
    public ArrayList<String> instructions;

    public Parser(File file) throws FileNotFoundException{
        this.scanner = new Scanner(file);
        this.next = this.scanner.hasNextLine() ? this.scanner.nextLine() : null;
        while(this.next != null && (this.next.trim().length()==0 || this.next.trim().startsWith("//"))){
            this.next = this.scanner.hasNextLine() ? this.scanner.nextLine() : null;
        }
    }

    public boolean hasMorelines(){
        return this.next != null;
    }

    public void next(){
        this.currentInstruction = this.next.trim();
        this.next = this.scanner.hasNextLine() ? this.scanner.nextLine() : null;
        while(this.next != null && (this.next.trim().length()==0 || this.next.trim().startsWith("//"))){
            this.next = this.scanner.hasNextLine() ? this.scanner.nextLine() : null;
        }
    }

    public int type(){
        if(this.currentInstruction.startsWith("@")){
            this.currentInstrType = Parser.A_INSTRUCTION;
        }
        else if(this.currentInstruction.startsWith("(")){
            this.currentInstrType = Parser.L_INSTRUCTION;
        }
        else{
            this.currentInstrType = Parser.C_INSTRUCTION;
        }
        return this.currentInstrType;
    }

    public String symbol() throws Exception{
        if(this.currentInstrType == Parser.C_INSTRUCTION){
            throw new Exception("Invalid instruction type");
        }

        String symbol = null;
        if(this.currentInstrType == Parser.A_INSTRUCTION){
            symbol = this.currentInstruction.substring(1);
        }
        else{
            symbol = this.currentInstruction.substring(1,this.currentInstruction.length()-1);
        }
        return symbol;
    }

    public String[] parse_compute() throws Exception{
        if(this.currentInstrType != Parser.C_INSTRUCTION){
            throw new Exception("Invalid instruction type");
        }

        int dest_index = this.currentInstruction.indexOf("=");
        int jump_index = this.currentInstruction.indexOf(";");
        String dest = "";
        String jump = "";
        String comp = "";
        if(dest_index != -1){
            dest = this.currentInstruction.substring(0,dest_index);
            if(dest.length()==0){
                throw new Exception("Syntax error! Invalid C-Instruction.");
            }
        }
        if(jump_index != -1){
            jump = this.currentInstruction.substring(jump_index+1);
            if(jump.length()==0){
                throw new Exception("Syntax error! Invalid C-Instruction.");
            }
        }
        int eofcomp = jump_index == -1 ? this.currentInstruction.length() : jump_index;
        comp = this.currentInstruction.substring(dest_index+1,eofcomp);

        String[] compute_instr = {dest.trim(),comp.trim(),jump.trim()};
        return compute_instr;
    }
}