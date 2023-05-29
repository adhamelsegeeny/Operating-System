import java.util.PriorityQueue;
import java.util.Queue;

// public class Mutex {
//     private boolean resourceAvailable;
//     private Queue<Process> blockedQueue = new PriorityQueue<>();

//     public Mutex() {
//         resourceAvailable = true;
//     }

//     public synchronized void semWait(Process process) {
//         if (resourceAvailable) {
//             resourceAvailable = false;
//             System.out.println("Process " + process.getPCB().getProcessID() + " acquired the resource.");
//         } 
//         else {
//             blockedQueue.add(process);
//             process.getPCB().setProcessState(ProcessState.BLOCKED);
//             System.out.println("Process " + process.getPCB().getProcessID() + " is blocked and added to the blocked queue.");
//             while(!resourceAvailable){
//             try {
//                 wait();
//             } catch (InterruptedException e) {
//                 e.printStackTrace();
//             }
//           }
//         }
//     }
    // public synchronized void semWait(){
    //     if(resourceAvailable){
    //         resourceAvailable = false;
    //         System.out.println("Resource acquired.");
    //     }
    //     else{
    //         try{
    //             wait();
    //         }catch(InterruptedException e){
    //             e.printStackTrace();
    //         }
    //     }
    // }

    // public synchronized void semSignal() {
    //     if (blockedQueue.isEmpty()) {
    //         resourceAvailable = true;
    //         System.out.println("Resource released.");
    //     } else {
    //         Process nextProcess = blockedQueue.remove();
    //         nextProcess.getPCB().setProcessState(ProcessState.READY);// or running?
    //         //add to ready queue?
    //        System.out.println("Process " + nextProcess.getPCB().getProcessID() + " is unblocked and acquires the resource.");
    //         notify();
    //     }
    // }
    // public synchronized void semSignal(){
    //     resourceAvailable = true;
    //     System.out.println("Resource released.");
    //     notify();
    // }
//}
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class Mutex {
    boolean resourceAvailability=true;
    // private Map<String, Queue<Process>> blockedQueues;
    private Queue<Process> blockedQueue;
    Process process;

    public Mutex() {
        blockedQueue= new LinkedList<Process>() {
            
        };
        
    }

    public void semWait(Process process, String resource, Queue<Process> readyQueue, Queue<Process> blockedQueue) {
        
        if (resourceAvailability) {
            resourceAvailability=false;
            System.out.println("Process " + process.getPCB().getProcessID() + " acquired the resource: " + resource);
            this.process=process;
        } else {
           
            if(!blockedQueue.contains(process)){

                this.blockedQueue.add(process);
                blockedQueue.add(process);
                readyQueue.remove(process);
            }
            process.getPCB().setProcessState(ProcessState.BLOCKED);
            System.out.println("Process " + process.getPCB().getProcessID() + " is blocked and added to the blocked queue for resource: " + resource);
            
        }
    }

    public void semSignal(String resource,Queue<Process> readyQueue, Queue<Process> blockedQueue,Process process ) {
        

        if(process!=this.process){
            System.out.println("Cannot semSignal due to different processes");
            return;
        }
        if (this.blockedQueue.isEmpty()) {
            resourceAvailability=true;
            process=null;
            System.out.println("Resource " + resource + " released.");
        } else {
            if(!this.blockedQueue.isEmpty()){

            Process p= this.blockedQueue.remove();
            blockedQueue.remove(p);
            p.getPCB().setProcessState(ProcessState.READY);
            readyQueue.add(p);
            this.process=p;
            System.out.println("Process " + p.getPCB().getProcessID() + " is unblocked and acquires the resource: " + resource);
            }
            
        }
    }
}

    

