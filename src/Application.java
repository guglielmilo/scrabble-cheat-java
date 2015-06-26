import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Application {

	public static void main(String[] args) {
		
		// Serialization
		/*File serialfile = new File("french.dic");
		if(!readSerialDic(diclist, serialfile)){
			createDic(diclist, dicfile);
			writeSerialDic(diclist, serialfile);
		}*/
		
		List<ScrabbleWord> diclist = new ArrayList<ScrabbleWord>();
		String file = "./dic/french_utf-8.txt";
		//File dicfile = new File("french-dic.txt");
		String inputchar = null;
		String inputfit = null;
		
		// run loop control
		int run = 1;
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
			
			// loading dic data
			//if(!readDic(diclist, dicfile)){
				
				// loading dic with file
				while(!createDic(diclist, new File(file))){
					
					// catch user input
					System.out.print("Dic file : ");
					file = br.readLine();
				}
				//writeDic(diclist, dicfile);
			//}
			
			// run
			while(run == 1){
				
				// catch user input
				System.out.print("Available Letters [a-z] : ");
				inputchar = br.readLine();
				while(!inputchar.matches("[a-zA-Z]+$")){
					System.err.print("Possible characters [a-z/A-Z] : ");
					inputchar = br.readLine();
				}
				System.out.print("Special Fitting [$/.] : ");
				inputfit = br.readLine();
				while(!inputfit.matches("^[a-zA-Z$.]+$")){
					System.err.print("Possible characters [a-z/A-Z/$/.]: ");
					inputfit = br.readLine();
				}
				// syntax '$' = '.*'
				inputfit = inputfit.replace("$", ".*");
		
				// search letters into the dic
				List<ScrabbleWord> wordlist = analyseDic(diclist, inputchar, inputfit);
				
				// sorting results
				wordlist.sort(null);
		
				showConsole(wordlist);
				
				// search done
				run = -1;
				
				// runtime continue
				while(run == -1){
					System.out.print("Continue [Y/N] : ");
					inputchar = br.readLine();
					if(inputchar.contains("Y"))
						run = 1;
					else if(inputchar.contains("N"))
						run = 0;
					else
						run = -1;
				}
				
			}
		} catch (IOException e) {
			System.err.println("User input error");
			e.printStackTrace();
		}
	}
	
	private static void showConsole(List<ScrabbleWord> wordlist){
		
		if (wordlist.size() == 0)
			System.out.println("No results");
		else {
			int index = 0;
			for (int i = 0; i < wordlist.size(); i++) {

				if (i == 0
						|| wordlist.get(i).getValue() != wordlist
								.get(i - 1).getValue())
					System.out.print("[" + wordlist.get(i).getValue() + "]");
				if (i != wordlist.size() - 1
						&& wordlist.get(i).getValue() == wordlist.get(i + 1)
								.getValue()) {
					System.out.print(wordlist.get(i));
				} else {
					System.out.println(wordlist.get(i));
					index++;
				}
				if (index == 10)
					break;
			}
		}
	}
		
	private static boolean analyseWord(String word, String input){
		
		if(input.length() < word.length())
			return false;
		
		StringBuilder userchar = new StringBuilder(input);
		
		// parsing word letters
		for (int k = 0; k < word.length(); k++) {
						
			boolean contains = false;
			
			// analyzing letter one by one
			for (int c = 0; c < userchar.length(); c++) {
				
				if(word.charAt(k) == userchar.charAt(c)){
					contains = true;
					userchar.deleteCharAt(c);
					break;
				}
			}
			// does not contains the letter analyzed
			if(!contains)
				return false;
		}
		return true;			
	}
	
	private static List<ScrabbleWord> analyseDic(List<ScrabbleWord> diclist, String inputchar, String inputfit){
		
		List<ScrabbleWord> wordlist = new LinkedList<ScrabbleWord>();
		int index = 0;

		for (int i = 0; i < diclist.size(); i++) {

			// analyzing word one by one
			if (analyseWord(diclist.get(i).getWord(), inputchar)) {
				
				if(inputfit.length() == 0 || diclist.get(i).getWord().matches(inputfit)){
					wordlist.add(index, diclist.get(i));
					index++;
				}
			}
		}
		return wordlist;
	}

	private static boolean createDic(List<ScrabbleWord> diclist, File file){
		
		System.out.print("Creating dic... ");
		
		long timestart = System.currentTimeMillis();
		
		try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
			
			String wordstr;
			int index = 0;
			
			while ((wordstr = br.readLine()) != null) {
				ScrabbleWord word = new ScrabbleWord(wordstr);
				diclist.add(index++, word);
			}
             
        } catch (FileNotFoundException e) {
        	System.err.println(file+" not found");
        	return false;
		} catch (IOException e) {
			System.err.println("Unable to access "+file);
			return false;
		}
		
		long time = System.currentTimeMillis()-timestart;
		System.out.println("done ["+time+"ms]");
		return true;
	}
	
	@SuppressWarnings("unused")
	private static void writeSerialDic(List<ScrabbleWord> diclist, File file){
		
		System.out.print("Writing dic...");
		
		long timestart = System.currentTimeMillis();
		
		try(FileOutputStream fs = new FileOutputStream(file)) {

            ObjectOutputStream os = new ObjectOutputStream(fs);
             
            os.writeObject(diclist);
             
            os.close();
             
        } catch (FileNotFoundException e) {
        	System.err.print("\n"+file+" cannot be created");
        } catch (IOException e) {
        	System.err.print("\nUnable to access "+file);
        }

		long time = System.currentTimeMillis()-timestart;
		System.out.println(" done ["+time+"ms]");
	}
	
	@SuppressWarnings({ "unused", "unchecked" })
	private static boolean readSerialDic(List<ScrabbleWord> diclist, File file){
		
		System.out.print("Reading dic... ");
		
		long timestart = System.currentTimeMillis();
		
		try(FileInputStream fi = new FileInputStream(file)) {
            
            ObjectInputStream os = new ObjectInputStream(fi);
            
            diclist = (List<ScrabbleWord>) os.readObject();
            
            os.close();

    		long time = System.currentTimeMillis()-timestart;
    		System.out.println(" done ["+time+"ms]");
    		
            return true;
             
        } catch (FileNotFoundException e) {
        	System.err.println(file+" not found");
        	return false;
        } catch (IOException e) {
        	System.err.println("Unable to access "+file);
        	return false;
        } catch (ClassNotFoundException e) {
        	System.err.println("Dic class not found");
        	return false;
        }
	}
	
	@SuppressWarnings("unused")
	private static void writeDic(List<ScrabbleWord> diclist, File file){
		
		System.out.print("Writing dic... ");
		
		long timestart = System.currentTimeMillis();
		
		try(BufferedWriter br = new BufferedWriter(new FileWriter(file))) {

			for(int i=0; i<diclist.size(); i++){
				br.write(diclist.get(i).getWordstr());
				br.write("/");
				br.write(diclist.get(i).getWord());
				br.write("/");
				br.write(String.valueOf(diclist.get(i).getValue()));
				br.newLine();
			}
             
        } catch (FileNotFoundException e) {
        	System.err.print(file+" cannot be created");
        } catch (IOException e) {
        	System.err.print("Unable to access "+file);
        }

		long time = System.currentTimeMillis()-timestart;
		System.out.println("done ["+time+"ms]");
	}
	
	@SuppressWarnings("unused")
	private static boolean readDic(List<ScrabbleWord> diclist, File file){
		
		System.out.print("Reading dic... ");
		
		long timestart = System.currentTimeMillis();
		
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {

			String line;
			
			// until the last line
			while ((line = br.readLine()) != null) {
				
				String[] parts = line.split("/");
				ScrabbleWord word = new ScrabbleWord(parts[0],parts[1],Integer.parseInt(parts[2]));
				diclist.add(word);
			}

			long time = System.currentTimeMillis() - timestart;
			System.out.println("done [" + time + "ms]");

			return true;

        } catch (FileNotFoundException e) {
        	System.err.println(file+" not found");
        	return false;
        } catch (IOException e) {
        	System.err.println("Unable to access "+file);
        	return false;
        }
	}

}
