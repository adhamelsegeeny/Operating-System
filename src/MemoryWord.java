import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryWord {
     private String variable;
     private String data;

     public MemoryWord(){
            variable = null;
            data=null;
     }
    public MemoryWord(String variable,String data){
                this.variable = variable;
                this.data=data;
    }

   public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    // public String getProcessID() {
    //         return processID;
    // }

    // public void setProcessID(String processID) {
    //         this.processID = processID;
    // }


    // private String variable;
    // private Comparable value;
   // private HashMap<String,Comparable> word;
   // private PCB pcb; // CHECK

    // public MemoryWord(){
    //     word = new HashMap<String,Comparable>();
    //     // this.variable = variable;
    //     // this.value = value;
    //     // this.pcb = pcb;// CHECK
    // }
    // public HashMap<String,Comparable> getWord() {
    //     return word;
    // }
    // public void setWord(HashMap<String,Comparable> word) {
    //     this.word = word;
    // }

    // public Comparable getValue(String variable) {
    //     return word.get(variable);
    // }

   
    // public void setVariable(String variable) {
    //     this.variable = variable;
    // }
    // public Comparable getValue() {
    //     return word.get(value);
    // }
    // public void setValue(Comparable value) {
    //     this.value = value;
    // }
   // public PCB getPcb() {
     //     return pcb;
     // }
     // public void setPcb(PCB pcb) {
     //     this.pcb = pcb;
     // }

}
