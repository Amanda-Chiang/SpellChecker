import java.util.ArrayList;
import java.util.Scanner;

public class spellChecker {

	public static void main(String [] args)
    {
        ArrayList<tup> data = new ArrayList<tup>();
        ArrayList<tup> permuts1 = new ArrayList<tup>();
        ArrayList<tup> permuts2 = new ArrayList<tup>();
        ArrayList<String> discard = new ArrayList<String>();

        createDict(data);
        
        // option to add custom words to dictionary
        System.out.println("Would you like to add your own words to your dictionary? (yes/no)");
        Scanner s1 = new Scanner(System.in);
        String yesno = s1.nextLine();
        if (yesno.equals("yes")) {
            System.out.println("Please type the words you want to add in a list separated by commas, no spaces (ex: bye,help,hi)");
            Scanner s2 = new Scanner(System.in);
            String custAdd = s2.nextLine();
            customDict(custAdd, data);
        }
        
        // INPUT WRONG WORD: the user inputs the word they want to correct
        System.out.println("What typo would you like to check?");
        Scanner s = new Scanner(System.in);
        String wrong = s.nextLine();

        if (isIn(wrong,data)) {
            System.out.println("That's already a word");
        }
        else {
            System.out.println("The best correction is: " + check(wrong,permuts1,permuts2,data,discard));
        }

    }


    // create a method that will check the first time and, if no word is found, check a second time
    public static String check(String input, ArrayList<tup> p1, ArrayList<tup> p2, ArrayList<tup> dict,ArrayList<String> trash) {
        int lowestChanges;
        int mostCommon = 0;
        String bestWord = "--";
        permute(input,p1,trash);
        for (int i = 0; i < p1.size(); i++) {
            // if the permutation is a correct word and its numChanges is less than lowestchanges
            if (isIn(p1.get(i).word,dict)) {
                int currInd = getInd(p1.get(i).word,dict);
                if (dict.get(currInd).count > mostCommon) {
                    bestWord = p1.get(i).word;
                    lowestChanges = 1;
                    mostCommon = dict.get(currInd).count;
                }
            }
            else {
                permute(p1.get(i).word,p2,trash);
            }
        }

        if (! bestWord.equals("--")) {
            return bestWord;
        }

        for (int i = 0; i < p2.size(); i++) {
            // if the permutation is a correct word and its numChanges is less than lowestChanges
            if (isIn(p2.get(i).word,dict)) {
                int currInd = getInd(p2.get(i).word,dict);
                if (dict.get(currInd).count > mostCommon) {
                    bestWord = p2.get(i).word;
                    lowestChanges = 2;
                    mostCommon = dict.get(currInd).count;
                }
            }
            else {
                trash.add(p2.get(i).word);
                p2.remove(i);
                i--;
            }
        }
        
        return bestWord;
    }

    // input a word and list to add permutations to, output list of various permutations
    public static void permute(String w, ArrayList<tup> list, ArrayList<String> trash) {
        removeLet(w,list,trash);
        addLet(w,list,trash);
        repLet(w,list,trash);
    }

    public static void removeLet(String w, ArrayList<tup> out, ArrayList<String> trash) {
        // CURRENT ERROR: Num of changes not properly being calculated, increasing too much
        // iterate through the possible letter removals for this input string and add them to the 
        // list of permuted words
        for (int i = 0; i < w.length(); i++) {
            String sub = w.substring(0,i) + w.substring(i+1);
            if (isIn(w, out)) {
                int currInd = getInd(w,out);
                tup curr = out.get(currInd);
                // find the list object with the same word and set the count one larger
                if (out.get(currInd).count > 2) {
                    trash.add(out.get(currInd).word);
                    out.remove(currInd);
                }
                else {
                    tup p = new tup(sub,curr.count+1);
                    out.add(p);
                }
            }
            else if (! strIsIn(sub,trash)){
                // else if not in trash, add a new tuple with the new word and count 1 to the data
                tup p = new tup(sub,1);
                out.add(p);            
            }
            
        }
    }

    // tested, works
    // iterates through word trying replacing each character with all the alphabet
    public static void repLet(String w, ArrayList<tup> out, ArrayList<String> trash) {
    String abc = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < w.length();i++) {
            for (int u = 0; u < abc.length(); u++) {
                String sub = w.substring(0,i) + abc.charAt(u) + w.substring(i+1);
                if (isIn(w,out)) {
                    int currInd = getInd(w,out);
                    tup curr = out.get(currInd);
                    // dump the permutation if it's been changed too many times
                    if (out.get(currInd).count > 2) {
                        trash.add(out.get(currInd).word);
                        out.remove(currInd);
                    }
                    // find the list object with the same word and set the count one larger
                    else {
                        tup p = new tup(sub,curr.count+1);
                        out.add(p);
                    }
                }
                else if (! strIsIn(sub,trash)){
                    // else if not in trash, add a new tuple with the new word and count 1 to the data
                    tup p = new tup(sub,1);
                    out.add(p);            
                }
            }
        }
    }

    // tested, works
    // iterates through different spots on the word, trying adding each letter in the alphabet
    public static void addLet(String w, ArrayList<tup> out, ArrayList<String> trash) {
        // need to change to add letters rather than replace
        String abc = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < w.length()+1;i++) {
            for (int u = 0; u < abc.length(); u++) {
                String sub = w.substring(0,i) + abc.charAt(u) + w.substring(i);
                if (isIn(w,out)) {
                    int currInd = getInd(w,out);
                    tup curr = out.get(currInd);
                    // find the list object with the same word and set the count one larger
                    if (out.get(currInd).count > 2) {
                        trash.add(out.get(currInd).word);
                        out.remove(currInd);
                    }
                    else {
                        tup p = new tup(sub,curr.count+1);
                        out.add(p);
                    }
                }
                else if (! strIsIn(sub,trash)){
                    // else if not in trash, add a new tuple with the new word and count 1 to the data
                    tup p = new tup(sub,1);
                    out.add(p);            
                }
            }
        }
    }

    // tested, works
    // checks if an inputted String is in any tup within a list of tuples
    public static boolean isIn(String w, ArrayList<tup> list) {
    	for (int i = 0; i < list.size(); i++) {
            if(list.get(i).has(w)) {
                return true;
            }
        }
        return false;
    }

    public static boolean strIsIn(String w, ArrayList<String> list) {
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).equals(w)) {
                return true;
            }
        }
        return false;
    }

    // tested, works
    // check ArrayList of tups to find the index of the tup with the input String
    public static int getInd(String a, ArrayList<tup> list) {
        if (isIn(a,list)) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).word.equals(a)) {
                    return i;
                }
            }
        }
        return -1;
    }

    // tested, works
    // loops through file and creates a tuple correctList w/ each unique word and its instances
    public static void createDict(ArrayList<tup> dict) {
        FileReader fr1 = new FileReader("book1.txt");

        // create a list of "correct words"
        // iterate through words in a file
        while (fr1.hasNextWord()) {
            String nextWord = fr1.getNextWord();

            // if already in the data, just add one to its count
            if (isIn(nextWord, dict)) {
                int currInd = getInd(nextWord,dict);
                tup curr = dict.get(currInd);
                // find the list object with the same word and set the count one larger
                dict.get(currInd).setCount(curr.count + 1);
           }
            else {
                // else, add a new tuple with the new word and count 1 to the data
                tup t = new tup(nextWord,1);
                dict.add(t);
            }
        }
    }

    // tested, works
    // input: string of words separated by commas that want to be added to the dictionary
    public static void customDict(String input, ArrayList<tup> dict) {
        int start = 0;
        int end = 0;
        String w = "";
        for (int i = 0; i < input.length(); i++) {
            if (input.substring(i,i+1).equals(",")) {
                end = i;
                w = input.substring(start,end);
                // if already in the data, just add one to its count
                if (isIn(w, dict)) {
                    int currInd = getInd(w,dict);
                    tup curr = dict.get(currInd);
                    // find the list object with the same word and set the count one larger
                    dict.get(currInd).setCount(curr.count + 1);
               }
                else {
                    // else, add a new tuple with the new word and count 1 to the data
                    tup t = new tup(w,1);
                    dict.add(t);
                }
                start = i+1;
                continue;
            }
            else if (i == input.length()-1) {
                end = i+1;
                w = input.substring(start,end);
                // if already in the data, just add one to its count
                if (isIn(w, dict)) {
                    int currInd = getInd(w,dict);
                    tup curr = dict.get(currInd);
                    // find the list object with the same word and set the count one larger
                    dict.get(currInd).setCount(curr.count + 1);
               }
                else {
                    // else, add a new tuple with the new word and count 1 to the data
                    tup t = new tup(w,1);
                    dict.add(t);
                }
            }

        }
    }

}