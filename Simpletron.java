import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Simpletron{
    private static final int NUM_REGISTERS = 100;
    private final int[] registers;
    private int instructionCounter;
    private int operand;
    private int opCode;
    private int accumulator;
    private String path;
    private boolean running;
    private int currentInstruction;

    public Simpletron(String path){
        this.path = path;
        registers = new int[NUM_REGISTERS];
        instructionCounter = 0;
        running = false;
        loadProgram(this.path);
        runProgram();
    }
    public void runProgram(){
        displayProgramHeader();
        running = true;
        while(instructionCounter < NUM_REGISTERS && running == true){
            currentInstruction = registers[instructionCounter];
            if(currentInstruction == 4300){
                break;
            }
            //parse
            parseInstruction(currentInstruction);
            //execute
            performInstruction(opCode);
        }
        //core dump
        performCoreDump();
    }
    public void loadProgram(String path){
        File program = new File(path);
        try{
            Scanner scanner = new Scanner(program);
            int i = 0;
            while (scanner.hasNext()){
                String inst = scanner.next();
                inst = inst.substring(1);
                if (inst.equals("99999")){
                    registers[i] = 4300;
                    break;
                } else {
                    registers[i] = Integer.parseInt(inst);
                }
                i++;
            }
        } catch (IOException e){
            System.err.println("File not found.");
            System.err.println(e.getStackTrace());
        }
    }
    public void parseInstruction(int word){
        //split the word into two parts
        operand = word % 100;
        opCode = word / 100;
    }
    public void performInstruction(int op){
        //branch variable bc idk ig i need that bc everything breaks if i dont
        boolean branched = false;
        switch(op){
            case 10:
                //use Scanner to read from terminal or from file
                Scanner scanner = new Scanner(System.in);
                System.out.println("Read number: ");
                int readNum = scanner.nextInt();
                registers[operand] = readNum;
                break;
            case 11:
                System.out.println(registers[operand]);
                break;
            case 20:
                accumulator = registers[operand];
                break;
            case 21:
                registers[operand] = accumulator;
                break;
            case 30:
                accumulator+=registers[operand];
                break;
            case 31:
                accumulator-=registers[operand];
                break;
            case 32:
                if (registers[operand]==0) {
                    System.out.println("cannot divide by zero");
                }else{
                    accumulator/=registers[operand];
                }
                break;
            case 33:
                accumulator*=registers[operand];
                break;
            case 40:
                instructionCounter = operand;
                branched = true;
                break;
            case 41:
                if(accumulator<0){
                    instructionCounter = operand;
                    branched = true;
                }
                break;
            case 42:
                if(accumulator==0){
                    instructionCounter = operand;
                    branched = true;
                }
                break;
            case 43:
                running = false;
                break;
            default:
                displayFatalErrorMsg("invalid command");
                System.out.println(operand);
                System.out.println(op);
                running = false;
                break;
      }
      if(!branched && running){
        instructionCounter++;
      }
    }
    public void displayProgramHeader(){
        System.out.println("*** Welcome to Simpletron!                    ***");
        System.out.println("***Please enter your program one instruction! ***");
        System.out.println("*** (or data word) at a time. I will display  ***");
        System.out.println("*** the location number and a question mark (?). ***");
        System.out.println("*** You then type the word for that location. ***");
        System.out.println("*** Type -99999 to stop entering your program. ***");
    }
    public void displayFatalErrorMsg(String msg){
        System.out.println("fatal error!!!");
        System.out.println(msg);
    }
    public void performCoreDump(){
        System.out.println("\nREGISTERS:");
        System.out.printf("accumulator:          %+05d%n", accumulator);
        System.out.printf("instructionCounter:   %02d%n", instructionCounter);
        System.out.printf("instructionRegister:  %+05d%n", registers[instructionCounter]);
        System.out.printf("operationCode:        %02d%n", opCode);
        System.out.printf("operand:              %02d%n", operand);
    }
}