

import java.util.ArrayList;

public class Memory{
    private ArrayList<MemoryWord> memory;
    private static final int MAX_MEMORY = 40;
    private Disk disk;
    Process process1;
    Process process2;

    public Memory(){
        memory = new ArrayList<MemoryWord>(MAX_MEMORY);
        for(int i = 0; i < MAX_MEMORY; i++){
            memory.add(new MemoryWord()); 
        }
        
    }

    public int requiredMemory(Process process){
        return process.getInstructions().size() + Process.getVariablesCount()+4;
    }

    public boolean isFull(){
        for(int i = 0; i < MAX_MEMORY; i++){
            if(memory.get(i).getVariable()==null&& memory.get(i).getData()==null){
                return false;
            }
        }
        return true;
    }

    public boolean isEmpty(){
        for(int i = 0; i < MAX_MEMORY; i++){
            if(memory.get(i).getVariable()!=null&& memory.get(i).getData()!=null){
                return false;
            }
        }
        return true;
    }

    public boolean hasEnoughSpace(int requiredMemory){
        boolean flag = false;
        int count = 0;
        for(int i = 0; i < MAX_MEMORY; i++){
            if(memory.get(i).getVariable()==null && memory.get(i).getData()==null){
                count++;
            }
        }
        if(count >= requiredMemory){
            flag= true;
        }
        return flag;
    }


    public void removeFromMemory(Process process,Disk disk){
        for(int i = 0; i < process.getIndices().size(); i++){
            memory.get(process.getIndices().get(i)).setVariable(null);
            memory.get(process.getIndices().get(i)).setData(null);
        }
    }


        public void loadToDisk(Process process,Disk disk){
            
            int processId = process.getPCB().getProcessID();
            int begin=0;
            int instructionNumber=0;

            

            int startAddress=0;
            for(int i=0;i<disk.getDisk().size();i++){
                if(disk.getDisk().get(i).getVariable()==null && disk.getDisk().get(i).getData()==null){
                    startAddress=i;
                    begin=startAddress;
                    break;
                }
            }

            for(int i=startAddress;i<process.getInstructions().size()+startAddress;i++){
                
                disk.getDisk().get(i).setVariable("Instruction "+instructionNumber+" :");
                disk.getDisk().get(i).setData(process.getInstructions().get(instructionNumber));

                instructionNumber++;
                begin++;
            }
        
                
                disk.getDisk().get(begin).setVariable("Process ID: ");
                disk.getDisk().get(begin).setData(Integer.toString(process.getPCB().getProcessID()));
                begin++;
                disk.getDisk().get(begin).setVariable("Process State: ");
                disk.getDisk().get(begin).setData(process.getPCB().getProcessState()+"");
                begin++;
                disk.getDisk().get(begin).setVariable("Process Counter: ");
                disk.getDisk().get(begin).setData(process.getPCB().getProgramCounter()+"");
                begin++;
                disk.getDisk().get(begin).setVariable("Memory Boundaries: ");
                if(process.getPCB().getMemoryBoundaries()==null){
                    disk.getDisk().get(begin).setData(null);
                }
                else{
                    disk.getDisk().get(begin).setData(process.getPCB().getMemoryBoundaries()[0]+"-"+process.getPCB().getMemoryBoundaries()[1]);
                }

                begin++;
                
                for(int i=0;i<3;i++){
                    disk.getDisk().get(begin).setVariable("Variable "+processId+" "+ (i+1));
                    disk.getDisk().get(begin).setData(null);
                    begin++;

                }

            }


        public void moveToMemory2 (Process process,Disk disk){
            
            int processId= process.getPCB().getProcessID();
            process.setIndices(new ArrayList<Integer>());
            int begin=0;
            boolean flag=false;
            int startAddress=-1;
            int endAddress=-1;

            if(processId==1){
                begin= 0;
            }
            if(processId==2){
                begin= 14;
            }
            if(processId==3){
                begin= 28;
            }
            //System.out.println("begin " +begin);
            
            for(int i=0;i<40;i++){
                if(memory.get(i).getVariable()== null && memory.get(i).getData()==null){
                    
                    memory.get(i).setVariable(disk.getDisk().get(begin).getVariable());
                    //System.out.println("VARIABLE "+disk.getDisk().get(begin).getVariable());
                    memory.get(i).setData(disk.getDisk().get(begin).getData());
                    //System.out.println("anyth " + disk.getDisk().get(begin).getVariable());
                    process.getIndices().add(i);

                    begin++;
                    if(!flag){
                        startAddress =i;
                        flag=true;
                    }
                    
                    if (begin==14 || begin==28 ||begin==44){
                        
                        endAddress =i;
                        break;
                    }
                        // disk.getDisk().get(j).setVariable(null);
                        // disk.getDisk().get(j).setData(null);
                    
                }
            }

            process.getPCB().setMemoryBoundaries(new int [] {startAddress,endAddress});
            
        }



        public void moveToMemory(Process process,Disk disk){
            
            int processId= process.getPCB().getProcessID();
            process.setIndices(new ArrayList<Integer>());
            int begin=0;
            boolean flag=false;
            int startAddress=-1;
            int endAddress=-1;

            for(int i=0;i<disk.getDisk().size();i++){
                if(disk.getDisk().get(i).getVariable().equals("Process ID: ")){
                    if(disk.getDisk().get(i).getData().equals(Integer.toString(processId))){
                        if(process.getPath().equals("src/resources/Program_3.txt")){
                            begin=i-9;
                            break;
                        }
                        else{
                            begin=i-7;
                            break;
                        }
                    }
                }
            }
            
            for(int i=0;i<40;i++){
                if(memory.get(i).getVariable()== null && memory.get(i).getData()==null){
                    
                    memory.get(i).setVariable(disk.getDisk().get(begin).getVariable());
                    memory.get(i).setData(disk.getDisk().get(begin).getData());
                    process.getIndices().add(i);

                    if (disk.getDisk().get(begin).getVariable().contains("Variable 1 3")
                    || disk.getDisk().get(begin).getVariable().contains("Variable 2 3")||
                    disk.getDisk().get(begin).getVariable().contains("Variable 3 3") ){
                        
                        endAddress =i;
                        break;
                    }
                        

                    begin++;
                    if(!flag){
                        startAddress =i;
                        flag=true;
                    }
                    
                   
                }
            }

            process.getPCB().setMemoryBoundaries(new int [] {startAddress,endAddress});
            
        }


    public void allocate(Process process){
        int requiredMemory = requiredMemory(process);
        int startAddress = -1;
        int endAddress = -1;
        boolean flag= false;
        for(int i=0;i<memory.size();i++){
            if((memory.get(i).getVariable()==null && memory.get(i).getData()==null)){
                if(flag==false){
                    startAddress=i;
                    flag=true;   
                }
                requiredMemory--;
                if(requiredMemory<0){
                    break;
                }
                
            }
            endAddress=i;
        }
        if(startAddress!=-1&&endAddress!=-1){
            process.getPCB().setMemoryBoundaries(new int[]{startAddress,endAddress});
        }
    }


    public void loadToMemory(Process process){
        //int requiredMemory = requiredMemory(process);
        int startAddress = process.getPCB().getMemoryBoundaries()[0];
        //int endAddress = process.getPCB().getMemoryBoundaries()[1];
        for(int i=0;i<process.getInstructions().size();i++){
            for(int j=startAddress;j<memory.size();j++){
                if(memory.get(j).getVariable()==null && memory.get(j).getData()==null){
                    memory.get(j).setVariable("Instruction "+i+" :");
                    memory.get(j).setData(process.getInstructions().get(i));
                    process.getIndices().add(j);
                    startAddress++;
                    break;
                }
            }
            
            
        }
            for(int j=startAddress;j<memory.size();j++){
                if(memory.get(j).getVariable()==null&&memory.get(j).getData()==null){
                    memory.get(j).setVariable("Process ID: ");
                    memory.get(j).setData(Integer.toString(process.getPCB().getProcessID()));
                    process.getIndices().add(j);
                    startAddress++;
                    break;
                }
            }
            
            for(int j=startAddress;j<memory.size();j++){
                if(memory.get(j).getVariable()==null&&memory.get(j).getData()==null){
                    memory.get(j).setVariable("Process State: ");
                    memory.get(j).setData(process.getPCB().getProcessState()+"");
                    process.getIndices().add(j);
                    startAddress++;
                    break;
                }
            }

            for(int j=startAddress;j<memory.size();j++){
                if(memory.get(j).getVariable()==null&&memory.get(j).getData()==null){
                    memory.get(j).setVariable("Process Counter: ");
                    memory.get(j).setData(process.getPCB().getProgramCounter()+"");
                    process.getIndices().add(j);
                    startAddress++;
                    break;
                }
            }

            for(int j=startAddress;j<memory.size();j++){
                if(memory.get(j).getVariable()==null&&memory.get(j).getData()==null){
                    memory.get(j).setVariable("Memory Boundaries: ");
                    memory.get(j).setData(process.getPCB().getMemoryBoundaries()[0]+"-"+process.getPCB().getMemoryBoundaries()[1]);
                    process.getIndices().add(j);
                    startAddress++;
                    break;
                }
            }

            

                for(int i=0;i<3;i++){
                    for(int j=startAddress;j<memory.size();j++){
                        if(memory.get(j).getVariable()==null&&memory.get(j).getData()==null){
                            memory.get(j).setVariable("Variable "+process.getPCB().getProcessID()+" "+ (i+1));
                            memory.get(j).setData(null);
                            process.getIndices().add(j);
                            break;
                        }
                    }
    
                }

                if(process.getPCB().getMemoryBoundaries()[1]==startAddress){
                    System.out.println("everything is fine");
                }
    }

    

    public int findAvailableMemory(int requiredSize){
        int freeBlocks=0;
        for(int i=0;i<memory.size();i++){
            if(memory.get(i)==null||(memory.get(i).getVariable()==null&&memory.get(i).getData()==null)){
                freeBlocks++;
            }
        }
        if(freeBlocks==requiredSize){
            return 0;
        }
        else{
            return -1;
        }

        }

        
    
    public void deallocate(Process process,Disk disk){
        int startAddress = process.getPCB().getMemoryBoundaries()[0];
        int endAddress = process.getPCB().getMemoryBoundaries()[1];
        for(int i = startAddress; i < endAddress; i++){
            memory.get(i).setVariable(null);
            memory.get(i).setData(null);
        }
        
        process.getPCB().setMemoryBoundaries(null);
        for(int i=0;i<disk.getDisk().size();i++){
            if(disk.getDisk().get(i).getVariable().equals("Process ID: ")){
                disk.getDisk().get(i+3).setData(null);
                disk.getDisk().get(i+1).setData(ProcessState.FINISHED+"");
            }

        }
    }

    public void putToDisk(Process process){
        int requiredMemory = requiredMemory(process);
        int startAddress = process.getPCB().getMemoryBoundaries()[0];
        int endAddress = process.getPCB().getMemoryBoundaries()[1];
        while(startAddress<endAddress){
            int i=0;
            while(i<process.getInstructions().size()){
                disk.getDisk().get(startAddress).setVariable("Instruction "+i+1+" :");
                disk.getDisk().get(startAddress).setData(process.getInstructions().get(i));
                startAddress++;
                i++;
            }
            i=0;
            // while(i<Process.getVariablesCount()){
            //     disk.getDisk().get(startAddress).setVariable(process.getVariables().get(i).getVariable());
            //     disk.getDisk().get(startAddress).setData(process.getVariables().get(i).getData()+"");
            //     startAddress++;
            //     i++;
            // }
            disk.getDisk().get(startAddress).setVariable("Process ID: ");
            disk.getDisk().get(startAddress).setData(Integer.toString(process.getPCB().getProcessID()));
            startAddress++;
            disk.getDisk().get(startAddress).setVariable("Process State: ");
            disk.getDisk().get(startAddress).setData(process.getPCB().getProcessState()+"");
            startAddress++;
            disk.getDisk().get(startAddress).setVariable("Process Counter: ");
            disk.getDisk().get(startAddress).setData(process.getPCB().getProgramCounter()+"");
            startAddress++;
            disk.getDisk().get(startAddress).setVariable("Memory Boundaries: ");
            disk.getDisk().get(startAddress).setData(process.getPCB().getMemoryBoundaries()[0]+"-"+process.getPCB().getMemoryBoundaries()[1]);
            startAddress++;
        }
    }
    
    public void printMemory(){
        for(int i = 0; i < MAX_MEMORY; i++){
            if(memory.get(i)==null || memory.get(i).getVariable()==null){
                break;
            }
            System.out.println(memory.get(i).getVariable() + " " + memory.get(i).getData());
            
        }
    }

    public ArrayList<MemoryWord> getMemory() {
        return memory;
    }

    public void setMemory(ArrayList<MemoryWord> memory) {
        this.memory = memory;
    }

    public void updateMemory(Process process,String key, String data){
        int startAddress = process.getPCB().getMemoryBoundaries()[0];
        
        int endAddress = process.getPCB().getMemoryBoundaries()[1];
        
        
        while(startAddress<=endAddress){
            if(memory.get(startAddress).getVariable()!=null && memory.get(startAddress).getVariable().equals(key)){
                
                SystemCalls sc= new SystemCalls();

                sc.writeDataToMemory(startAddress, data, this);
                break;
            }
            startAddress++;
        }
    }

    public boolean contains(Process process){
        for(int i=0;i<memory.size();i++){
            if(memory.get(i).getVariable()=="Process ID: "){
                if(memory.get(i).getData().equals(process.getPCB().getProcessID()+"")){
                    return true;
                }
            }
        }
        return false;
    }
    

    }








    



    
   


    
