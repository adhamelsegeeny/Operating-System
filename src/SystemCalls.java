import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class SystemCalls {
	Process process;

	public String getInput() {
		Scanner Sc = new Scanner(System.in);
		String input = Sc.nextLine();
		return input;
	}

	public void print(String data) {
		System.out.println(data);
	}

	

	public ArrayList<String> readFile(String filename) { // public void readFile(String filename)
		// new
		ArrayList<String> lines = new ArrayList<String>();
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
		// new
		return lines;
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

	public String readFromMemory(Process process, int variableIndex, Memory mem) {
		String res = "";
		HashMap<String, String> h = process.getVariables();
		String variableName = "";
		for (int i = 1; i <= h.keySet().size(); i++) {
			if (i == variableIndex) {
				variableName = h.keySet().toArray()[i-1].toString();
				System.out.println("AAAA " +(i-1));
				break;
			}

		}
		for (int i = 0; i < mem.getMemory().size(); i++) {
			if (mem.getMemory().get(i).getVariable()!=null && mem.getMemory().get(i).getVariable().equals(variableName)) {
				res = mem.getMemory().get(i).getData();
				break;
			}
		}

		return res;

	}

	public void writeDataToMemory(int address, String data, Memory memory) {
		memory.getMemory().get(address).setData(data);

	}
}