package Twitter_Students;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Testing_Stuff {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		ArrayList<String> commonWords = getCommonWords();
		System.out.println(commonWords);
	}
	
	public static ArrayList<String> getCommonWords(){
		ArrayList<String> input = new ArrayList<String>();
		try {
			Scanner scn = new Scanner(new File("story.txt"));
			while(scn.hasNextLine()) {
				input.add(scn.nextLine());
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("file not found");
			e.printStackTrace();
		}
		return input;
	}
	
	
}
