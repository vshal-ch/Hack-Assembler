import java.util.*;
import java.io.*;

public class Converter{
    private static HashMap<String,String> compInstMap = new HashMap<>();
    private static HashMap<String,String> jumpInstMap = new HashMap<>();

    public static void load_maps(HashMap config) throws Exception{
        String computeFilePath = (String) config.get("compute_map_file");
        if(computeFilePath == null){
            throw new Exception("key compute_map_file not found in config file");
        }
        
        //Loading Compute Instruction map
        File computeInstrMapFile = new File(computeFilePath);
        Scanner instrMapScanner = new Scanner(computeInstrMapFile);
        System.out.println("Loading Compute Instruction map");
        while(instrMapScanner.hasNextLine()){
            String line = instrMapScanner.nextLine();
            if(!line.contains("=")){
                throw new Exception("Invalid map file!");
            }
            String[] instr = line.split("=");
            compInstMap.put(instr[0],instr[1]);
        }
        System.out.println("Loaded Compute Instruction map");

        //Loading jump Instruction map
        String jumpFilePath = (String) config.get("jump_map_file");
        File jumpInstrMapFile = new File(jumpFilePath);
        instrMapScanner = new Scanner(jumpInstrMapFile);
        System.out.println("Loading jump Instruction map");
        while(instrMapScanner.hasNextLine()){
            String line = instrMapScanner.nextLine();
            if(!line.contains("=")){
                throw new Exception("Invalid map file!");
            }
            String[] instr = line.split("=");
            jumpInstMap.put(instr[0],instr[1]);
        }
        System.out.println("Loaded jump Instruction map");
        instrMapScanner.close();
    }

    public static String convert_a(String symbol){
        int number = Integer.parseInt(symbol);
        return String.format("%16s", Integer.toBinaryString(number)).replace(' ', '0');
    }

    private static String get_dest(String dest) throws Exception{
        if(dest.length()>3){
            throw new Exception("Invalid dest in C-Instruction");
        }
        char[] dest_array = {'0','0','0'};
        for(int i=0;i<dest.length();i++){
            if(dest.charAt(i)=='A'){
                if(dest_array[0] == '1'){
                    throw new Exception("Invalid dest in C-Instruction");
                }
                else{
                    dest_array[0] = '1';
                }
            }
            else if(dest.charAt(i)=='D'){
                if(dest_array[1] == '1'){
                    throw new Exception("Invalid dest in C-Instruction");
                }
                else{
                    dest_array[1] = '1';
                }
            }
            else if(dest.charAt(i)=='M'){
                if(dest_array[2] == '1'){
                    throw new Exception("Invalid dest in C-Instruction");
                }
                else{
                    dest_array[2] = '1';
                }
            }
            else{
                throw new Exception("Invalid dest in C-Instruction");
            }
        }
        return new String(dest_array);
    }

    private static String get_comp(String comp) throws Exception{
        String compBinStr = compInstMap.get(comp);
        if(compBinStr == null){
            throw new Exception("Invalid comp in C-Instruction");
        }
        return compBinStr;
    }

    private static String get_jump(String jump) throws Exception{
        if(jump != null && jump.equals("")){
            return "000";
        }
        String jumpBinStr = jumpInstMap.get(jump);
        if(jumpBinStr == null){
            throw new Exception("Invalid jump in C-Instruction");
        }
        return jumpBinStr;
    }

    public static String convert_c(String[] c_instr) throws Exception{
        StringBuilder sb = new StringBuilder("111");
        String dest = c_instr[0];
        String comp = c_instr[1];
        String jump = c_instr[2];

        sb.append(get_comp(comp));
        sb.append(get_dest(dest));
        sb.append(get_jump(jump));
        return sb.toString();
    }
}