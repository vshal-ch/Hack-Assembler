import java.io.*;
import java.util.*;
import src.*;

public class HackAssembler{
    public static HashMap<String,String> configMap = new HashMap<>();
    public static void main(String args[]) throws Exception{
        if(args.length == 0){
            System.out.println("Usage: java HackAssembler Prog.asm [-o Prog.hack]");
            return;
        }

        String outFileName = "Prog.hack";
        if(args.length == 3){
            if(args[1].trim().equals("-o")){
                outFileName = args[2];
            }
        }

        load_config();

        String asmFileName = args[0];
        File asmFile = new File(asmFileName);
        File outFile = new File(outFileName);
        System.out.println("Input file: "+asmFile.getPath());
        System.out.println("Output file: "+outFile.getPath());

        Parser parser;
        try{
            parser = new Parser(asmFile);
        }
        catch(FileNotFoundException e){
            System.out.println("File not found at "+asmFile.getAbsolutePath()+" "+e);
            return;
        }

        SymbolTable symTable = new SymbolTable();
        ArrayList<String> instructions = new ArrayList<String>();
        int lineNo = 0;

        while(parser.hasMorelines()){
            parser.next();
            int type = parser.type();
            String bin16bit = "";
            if(type == Parser.L_INSTRUCTION){
                symTable.addLableSym(parser.symbol(),lineNo);
                continue;
            }
            instructions.add(parser.currentInstruction);
            lineNo++;            
        }

        Converter.load_maps(configMap);
        BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
        System.out.println("Writing binary instructions to: "+outFile.getPath());

        for(String instruction: instructions){
            parser.currentInstruction = instruction;
            int type = parser.type();
            String bin16bit = "";
            if(type == Parser.L_INSTRUCTION){
                continue;
            }
            else if(type == Parser.A_INSTRUCTION){
                String symbol = parser.symbol();
                int number;
                try{
                    number = Integer.parseInt(symbol);
                }
                catch(NumberFormatException e){
                    number = symTable.get(symbol);
                }
                try{
                    bin16bit = Converter.convert_a(Integer.toString(number));
                }
                catch(NumberFormatException e){
                    System.out.println("Invalid A-Instruction!");
                    break;
                }
            }
            else if(type==Parser.C_INSTRUCTION){
                String[] c_instr = parser.parse_compute();
                bin16bit = Converter.convert_c(c_instr);
            }
            writer.write(bin16bit+"\n");
        }

        System.out.println("Binary instructions writen to: "+outFile.getPath());

        writer.close();
    }

    public static void load_config() throws Exception{
        File configFile = new File("./res/config.txt");
        Scanner configScanner = new Scanner(configFile);
        System.out.println("Loading config file");
        while(configScanner.hasNextLine()){
            String line = configScanner.nextLine();
            if(!line.contains("=")){
                throw new Exception("Invalid config file!");
            }
            String[] config = line.split("=");
            configMap.put(config[0],config[1].substring(1,config[1].length()-1));
        }
        System.out.println("Loaded config file");
        configScanner.close();
    }
}