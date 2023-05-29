import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import javax.swing.plaf.synth.SynthSeparatorUI;

public class Interpreter {
    private ArrayList<String> programCode;
    // private String programCode;
    private ArrayList<MemoryWord> variables;
    private Queue<Process> readyQueue;
    private Queue<Process> blockedQueue;
    private int resourceUsed;
    private ArrayList<Process> processes;
    private Mutex file;
    private Mutex userInput;
    private Mutex userOutput;
    private Memory memory;
    private static final int timeSlice = 2;
    private SystemCalls systemCalls;
    private String inputP3;
    private String outputP3;
    private Disk disk;
    private int time;
    private int inputP1A;
    private int inputP1B;
    private String outputP1;
    private String inputP2File;
    private String inputP2Text;

    public Interpreter() {
        time = 0;
        programCode = new ArrayList<String>();
        variables = new ArrayList<>();
        readyQueue = new LinkedList<Process>();
        blockedQueue = new LinkedList<Process>();
        processes = new ArrayList<Process>();
        file = new Mutex();
        userInput = new Mutex();
        userOutput = new Mutex();
        memory = new Memory();
        systemCalls = new SystemCalls();
        disk = new Disk();

    }

    public void loadProgram(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            programCode.add(line);

        }
        reader.close();
        createProcess(filePath);
    }

    public void createProcess(String path) {
        PCB pcb = new PCB();
        pcb.setProcessID(processes.size() + 1);
        Process process = new Process(pcb, programCode);
        process.getPCB().setProcessState(ProcessState.NEW);
        process.setInstructions(programCode);
        processes.add(process);
        readyQueue.add(process);
        
        int requiredMemory = process.getInstructions().size() + Process.getVariablesCount() + 4;
        process.setPath(path);

        if (memory.hasEnoughSpace(requiredMemory)) {
            memory.allocate(process);
            memory.loadToDisk(process, disk);
            
            memory.moveToMemory(process, disk);
            

        } else {
            System.out.println("Memory is full, process will be assigned to disk");
            memory.loadToDisk(process, disk);
        }

    }

    public Process removeProcess() {
        Process process = readyQueue.peek();
        return process;
    }

    public String getElementValue(ArrayList<MemoryWord> variables, String[] variableName) {
        String value = "";
        for (int i = 0; i < variables.size(); i++) {
            if (variables.get(i).getVariable().equals(variableName)) {
                value = variables.get(i).getData();
            }
        }
        return value;
    }

    public void executeProgram(Process process) throws InterruptedException {

        programCode = process.getInstructions();

        String currentLine = programCode.get(process.getPCB().getProgramCounter());// String currentLine =
        process.getPCB().setProgramCounter(process.getPCB().getProgramCounter() + 1);
        String[] tokens = currentLine.split(" ");
        System.out.println("Executing instruction: " + currentLine);
        if (tokens[0].equals("print")) {

            String input1 = systemCalls.readFromMemory(process, 1, memory);
            systemCalls.print(input1);
            process.getVariables().put("Variable " + process.getPCB().getProcessID() + " 3", outputP3);
            updateDisk(process, "Variable " + process.getPCB().getProcessID() + " 3", outputP3);
            memory.updateMemory(process, "Variable " + process.getPCB().getProcessID() + " 3", outputP3);

        } else if (tokens[0].equals("assign") && tokens[1].equals("a")
                && process.getPath().equals("src/resources/Program_1.txt")) {
            System.out.println("Enter from");

            inputP1A = Integer.parseInt(systemCalls.getInput());
            process.getVariables().put("Variable " + process.getPCB().getProcessID() + " 1", inputP1A + "");
            updateDisk(process, "Variable " + process.getPCB().getProcessID() + " 1", inputP1A + "");
            memory.updateMemory(process, "Variable " + process.getPCB().getProcessID() + " 1", inputP1A + "");

        }

        else if (tokens[0].equals("assign") && tokens[1].equals("b")
                && process.getPath().equals("src/resources/Program_1.txt")) {
            System.out.println("Enter to");
            inputP1B = Integer.parseInt(systemCalls.getInput());
            process.getVariables().put("Variable " + process.getPCB().getProcessID() + " 2", inputP1B + "");
            updateDisk(process, "Variable " + process.getPCB().getProcessID() + " 2", inputP1B + "");
            memory.updateMemory(process, "Variable " + process.getPCB().getProcessID() + " 2", inputP1B + "");

        }

        else if (tokens[0].equals("assign") && tokens[1].equals("a")
                && process.getPath().equals("src/resources/Program_2.txt")) {
            System.out.println("Enter new file name");

            inputP2File = systemCalls.getInput();
            process.getVariables().put("Variable " + process.getPCB().getProcessID() + " 1", inputP2File);
            updateDisk(process, "Variable " + process.getPCB().getProcessID() + " 1", inputP2File);
            memory.updateMemory(process, "Variable " + process.getPCB().getProcessID() + " 1", inputP2File);

        }

        else if (tokens[0].equals("assign") && tokens[1].equals("b")
                && process.getPath().equals("src/resources/Program_2.txt")) {
            System.out.println("Enter text to be written");

            inputP2Text = systemCalls.getInput();
            process.getVariables().put("Variable " + process.getPCB().getProcessID() + " 2", inputP2Text);
            updateDisk(process, "Variable " + process.getPCB().getProcessID() + " 2", inputP2Text);
            memory.updateMemory(process, "Variable " + process.getPCB().getProcessID() + " 2", inputP2Text);

        }

        else if (tokens[0].equals("writeFile")) {

            String input1 = systemCalls.readFromMemory(process, 1, memory);
            String input2 = systemCalls.readFromMemory(process, 2, memory);
            systemCalls.writeFile(input1, input2);
            process.getVariables().put("Variable " + process.getPCB().getProcessID() + " 3",
                    inputP2Text + " is written into file " + inputP2File);
            updateDisk(process, "Variable " + process.getPCB().getProcessID() + " 3",
                    inputP2Text + " is written into file " + inputP2File);
            memory.updateMemory(process, "Variable " + process.getPCB().getProcessID() + " 3",
                    inputP2Text + " is written into file " + inputP2File);

        } else if (tokens[0].equals("readFile")) {
            String filename = tokens[1];
            systemCalls.readFile(filename);
        } else if (tokens[0].equals("printFromTo")) {

            String input1 = systemCalls.readFromMemory(process, 1, memory);
            String input2 = systemCalls.readFromMemory(process, 2, memory);
            String result = printNumbersFromTo(Integer.parseInt(input1), Integer.parseInt(input2));
            systemCalls.print(result);
            process.getVariables().put("Variable " + process.getPCB().getProcessID() + " 3", result);
            updateDisk(process, "Variable " + process.getPCB().getProcessID() + " 3", result);
            memory.updateMemory(process, "Variable " + process.getPCB().getProcessID() + " 3", result);

        } else if (tokens[0].equals("assign") && process.getPath().equals("src/resources/Program_3.txt")) {

            if (tokens[2].equals("input")) {

                System.out.println("Enter existing file name");

                String filename = systemCalls.getInput();
                inputP3 = "src/resources/" + filename;
                process.getVariables().put("Variable " + process.getPCB().getProcessID() + " 1", inputP3);
                updateDisk(process, "Variable " + process.getPCB().getProcessID() + " 1", inputP3);
                memory.updateMemory(process, "Variable " + process.getPCB().getProcessID() + " 1", inputP3);
            }

            if (tokens[2].equals("readFile")) {

                String input1 = systemCalls.readFromMemory(process, 1, memory);
                ArrayList<String> values = systemCalls.readFile(input1);// CHECK

                String data = "";
                for (int i = 0; i < values.size(); i++) {
                    data += values.get(i);
                }
                outputP3 = data;
                process.getVariables().put("Variable " + process.getPCB().getProcessID() + " 2", data);
                updateDisk(process, "Variable " + process.getPCB().getProcessID() + " 2", data);
                memory.updateMemory(process, "Variable " + process.getPCB().getProcessID() + " 2", data);

            }
        } else if (tokens[0].equals("semWait")) {
            String mutexName = tokens[1];
            switch (mutexName) {
                case "file":
                    file.semWait(process, mutexName + "", readyQueue, blockedQueue);
                    break;
                case "userInput":
                    userInput.semWait(process, mutexName + "", readyQueue, blockedQueue);

                    break;
                case "userOutput":
                    userOutput.semWait(process, mutexName + "", readyQueue, blockedQueue);
                    break;
                default:
                    System.out.println("Invalid mutex name");
            }
        } else if (tokens[0].equals("semSignal")) {
            String mutexName = tokens[1];
            switch (mutexName) {
                case "file":
                    file.semSignal(mutexName + "", readyQueue, blockedQueue, process);
                    break;
                case "userInput":
                    userInput.semSignal(mutexName + "", readyQueue, blockedQueue, process);
                    break;
                case "userOutput":
                    userOutput.semSignal(mutexName + "", readyQueue, blockedQueue, process);
                    break;
                default:
                    System.out.println("Invalid mutex name");
            }
            // }
        }

    }

    
    private void printMemory() {
        memory.printMemory();
    }

    private void assignVariable(String variable, String value) {
        Process process = readyQueue.peek();
        process.getVariables().put(variable, value);
    }

    public ArrayList<String> getInstructions(Process process) {
        ArrayList<String> instructions = new ArrayList<>();
        instructions.add(process.getInstructions().get(process.getPCB().getProgramCounter()));
        process.getPCB().setProgramCounter(process.getPCB().getProgramCounter() + 1);

        return instructions;
    }

    public void scheduler() throws InterruptedException, IOException {

        while (!readyQueue.isEmpty()) {
            Process process = readyQueue.remove();
            if (!memory.contains(process)) {
                memory.allocate(process);
                memory.moveToMemory(process, disk);
                System.out.println("process " + process.getPCB().getProcessID() + " is moved to memory");
            }
            process.getPCB().setProcessState(ProcessState.RUNNING);
            System.out.println("Process " + process.getPCB().getProcessID() + " is running");

            for (int i = 0; i < timeSlice; i++) {
                if (process.getPCB().getProgramCounter() < process.getInstructions().size()) { // change to if
                    // String instruction =
                    // process.getInstructions().get(process.getPCB().getProgramCounter());

                    executeProgram(process);
                    
                    
                    if (blockedQueue.contains(process)) {
                        i = timeSlice;
                    }
                    if (i == timeSlice - 1) {
                        process.getPCB().setProcessState(ProcessState.READY);
                    }
                    printBlockedQueue();
                    printReadyQueue();
                    printMemory();
                    time++;

                    if (time == 1) {
                        programCode = new ArrayList<>();
                        loadProgram("src/resources/Program_1.txt");
                    }
                    if (time == 4) {
                        programCode = new ArrayList<>();
                        loadProgram("src/resources/Program_3.txt");

                    }
                }

                if (process.getPCB().getProgramCounter() == process.getInstructions().size()) {
                    process.getPCB().setProcessState(ProcessState.FINISHED);

                    i = timeSlice;

                    memory.deallocate(process);
                    processes.remove(process);

                    System.out.println("Process " + process.getPCB().getProcessID() + " is finished");
                }
                else if (process.getPCB().getProcessState().equals(ProcessState.RUNNING)) {

                    if (!readyQueue.contains(process)) {
                        readyQueue.add(process);
                    }
                    printReadyQueue();

                }
            }

            memory.removeFromMemory(process, disk);
            System.out.println("process " + process.getPCB().getProcessID() + " is removed from memory");

        }
        printBlockedQueue();
        printReadyQueue();
        printMemory();
        disk.printDisk();
    }

    public String getVariableValue(String variable) { 
        String value = "";
        for (int i = 0; i < memory.getMemory().size(); i++) {
            if (memory.getMemory().get(i).getVariable() == variable) {
                value += memory.getMemory().get(i).getData();
            }
        }
        return value;
    }

    public void assignVariableFile(String variable, ArrayList<String> values) {
        String value = "";
        for (String line : values) {
            value += line + "\n";
        }
        Process process = readyQueue.peek();
        process.getVariables().put(variable, value);
    }

    public void writeFile(String filename, String data) {
        try {
            String path = "src/resources/";
            File file = new File(path + filename);
            FileWriter writer = new FileWriter(file, true);
            writer.write(data); // writer.write(data);
            writer.close();

        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
        // new
    }
    // write to file

    public ArrayList<String> readFile(String filename) { // public void readFile(String filename)
        ArrayList<String> lines = new ArrayList<String>(); // new
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line); // new
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return lines;// new
    }

    public String printNumbersFromTo(int from, int to) {
        String result = "";
        for (int i = from + 1; i < to; i++) {

            result += i + " ";
        }
        return result;
    }

    public ArrayList<String> getProgramCode() {
        return programCode;
    }

    public ArrayList<MemoryWord> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<MemoryWord> variables) {
        this.variables = variables;
    }

    public Queue<Process> getReadyQueue() {
        return readyQueue;
    }

    public void setReadyQueue(Queue<Process> readyQueue) {
        this.readyQueue = readyQueue;
    }

    public Queue<Process> getBlockedQueue() {
        return blockedQueue;
    }

    public void setBlockedQueue(Queue<Process> blockedQueue) {
        this.blockedQueue = blockedQueue;
    }

    public int getResourceUsed() {
        return resourceUsed;
    }

    public void setResourceUsed(int resourceUsed) {
        this.resourceUsed = resourceUsed;
    }

    public void semWait(Mutex mutex) {

        // mutex.semWait();
    }

    public void semSignal(Mutex mutex) {
        // mutex.semSignal();
    }

    public void printReadyQueue() {
        System.out.println("Ready Queue: ");
        String queue = "[";
        for (Process process : readyQueue) {
            queue += process.getPCB().getProcessID() + ", ";
        }
        queue += "]";
        System.out.println(queue);
    }

    public void printBlockedQueue() {
        System.out.println("Blocked Queue: ");
        String queue = "[";
        for (Process process : blockedQueue) {
            queue += process.getPCB().getProcessID() + ", ";
        }
        queue += "]";
        System.out.println(queue);
    }

    public String printCurrentlyExecuting() {
        String currentlyExecuting = "";
        for (Process process : processes) {
            if (process.getPCB().getProcessState().equals(ProcessState.RUNNING)) {
                currentlyExecuting += "Process currently executing is: " + process.getPCB().getProcessID() + " ";
            }
        }
        return currentlyExecuting;
    }

    public void updateDisk(Process process, String key, String data) {
        int begin = 0;
        if (process.getPCB().getProcessID() == 1) {
            begin = 11;
        }
        if (process.getPCB().getProcessID() == 2) {
            begin = 25;
        }
        if (process.getPCB().getProcessID() == 3) {
            begin = 41;
        }

        for (int i = begin; i < begin + 3; i++) {
            if (disk.getDisk().get(i).getVariable() != null && disk.getDisk().get(i).getVariable().equals(key)) {
                disk.getDisk().get(i).setData(data);
            }
        }

    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Interpreter interpreter = new Interpreter();
        interpreter.loadProgram("src/resources/Program_2.txt");

        interpreter.scheduler();
    }
}
