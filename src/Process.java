import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Process implements Comparable<Process> {
    private PCB pcb;
    private ArrayList<String> instructions; 
    private HashMap<String,String> variables;//check
    private static final int variablesCount=3;
    private ArrayList<Integer> indices= new ArrayList<Integer>();
    private String path;

    public Process(PCB pcb, ArrayList<String> instructions){
        this.pcb = pcb;
        instructions = new ArrayList<>();
        variables = new HashMap<String,String>();//check
        //memory.memoryIsFull();
    }

    public PCB getPCB() {
        return pcb;
    }

    public void setPCB(PCB pcb) {
        this.pcb = pcb;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public static int getVariablesCount() {
        return variablesCount;
    }

    public HashMap<String, String> getVariables() {
        return variables;
    }

    public void setVariables(HashMap<String, String> variables) {
        this.variables = variables;
    }

    @Override
    public int compareTo(Process o) {
        return this.pcb.getProcessID() - o.pcb.getProcessID(); 
    }

    public void setInstructions(ArrayList<String> programCode) {  
        this.instructions = programCode;
    }

    public void setIndices(ArrayList<Integer> indices) {
        this.indices = indices;
    }

    public ArrayList<Integer> getIndices() {
        return indices;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
    

    
    
    
}
