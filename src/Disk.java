import java.util.ArrayList;

public class Disk{
    private ArrayList<MemoryWord> disk;

    public Disk(){
        disk = new ArrayList<MemoryWord>();
        for(int i = 0; i < 44; i++){
            disk.add(new MemoryWord()); 
        }
    } 

    public ArrayList<MemoryWord> getDisk() {
        return disk;
    }
    
    public void printDisk(){
        for(int i=0;i<disk.size();i++){
            System.out.println(disk.get(i).getVariable()+" "+disk.get(i).getData());
        }
    }
}