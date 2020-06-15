package Twitter_Students;
// Date:

import java.util.List;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Date;
import twitter4j.*;       //set the classpath to lib\twitter4j-core-4.0.7.jar

public class Twitter_Driver_shell
{
   private static PrintStream consolePrint;

   public static void main (String []args) throws TwitterException, IOException
   {
      consolePrint = System.out; // this preserves the standard output so we can get to it later      
   
      // PART III - Connect
      // set classpath, edit properties file
          
      TJTwitter bigBird = new TJTwitter(consolePrint);
      
   	// Part III - Tweet
      // Create and set a String called message below
   	// Uncomment this line to test, but then recomment so that the same
   	// tweet does not get sent out over and over.
   
      
//      String message="Yo yo yo whazzup!";
//      bigBird.tweetOut(message);
      
       
   
      // PART III - Test
      // Choose a public Twitter user's handle 
      
//      Scanner scan = new Scanner(System.in);
//      consolePrint.print("Please enter a Twitter handle, do not include the @symbol --> ");
//      String twitter_handle = scan.next();
       
      // Find and print the most popular word they tweet 
//      while (!twitter_handle.equals("done"))
//      {
//         bigBird.queryHandle(twitter_handle);
//         consolePrint.println("The most common word from @" + twitter_handle + " is: " + bigBird.getMostPopularWord()+ ".");
//         consolePrint.println("The word appears " + bigBird.getFrequencyMax() + " times.");
//         consolePrint.println();
//         consolePrint.print("Please enter a Twitter handle, do not include the @ symbol --> ");
//         twitter_handle = scan.next();
//      }
//      
      // PART IV
      int fluFreq=0;
      int covidFreq=0;
      int policeBrutalityFreq=0;
      

      System.out.println("Is the flu a problem?");
      bigBird.investigate("flu");
      fluFreq = bigBird.getNumberOfTweets();
      if(bigBird.getNumberOfTweets()==0) {
    	  System.out.println("I didn't need to worry about the flu since January/1/2018 \n");
      }else {
    	  System.out.println("Oh no... this is an issue! \n");
      }

      System.out.println("Is COVID-19 a problem?");
      bigBird.investigate("covid19");
      covidFreq+=bigBird.getNumberOfTweets();
      System.out.println("Or corona virus?");
      bigBird.investigate("corona virus");
      covidFreq+=bigBird.getNumberOfTweets();
      if(bigBird.getNumberOfTweets()==0) {
    	  System.out.println("I didn't need to worry about COVID-19 since January/1/2018 \n");
      }else {
    	  System.out.println("Oh no... this is an issue! \n");
      }
      

      System.out.println("Is police brutality a problem?");
      bigBird.investigate("GeorgeFloyd");
      policeBrutalityFreq=bigBird.getNumberOfTweets();
      if(bigBird.getNumberOfTweets()==0) {
    	  System.out.println("I didn't need to worry about police brutality since January/1/2018 \n");
      }else {
    	  System.out.println("Oh no... this is an issue! \n");
      }
      
      System.out.println("Here's the tweets being used");
      
      bigBird.investigate(null);
      
      System.out.println("The most popular word in these tweets is \""+bigBird.getMostPopularWord()+"\" appearing "+bigBird.getFrequencyMax()+" times.");
   }        
}        
      
class TJTwitter 
{
   private Twitter twitter;
   private PrintStream consolePrint;
   private List<Status> statuses;
   private int numberOfTweets; 
   private List<String> terms;
   private String popularWord;
   private int frequencyMax;
  
   public TJTwitter(PrintStream console)
   {
      // Makes an instance of Twitter - this is re-useable and thread safe.
      // Connects to Twitter and performs authorizations.
      twitter = TwitterFactory.getSingleton(); 
      consolePrint = console;
      statuses = new ArrayList<Status>();
      terms = new ArrayList<String>();
   }
	
   public List<String> getTerms()
   {
      return terms;
   }
	
   public int getNumberOfTweets()
   {
      return numberOfTweets;
   }
	
   public String getMostPopularWord()
   {
      return popularWord;
   }
	
   public int getFrequencyMax()
   {
      return frequencyMax;
   }
	   
  /******************  Part III - Tweet *******************/
  /** 
   * This method tweets a given message.
   * @param String  a message you wish to Tweet out
   */
   public void tweetOut(String message) throws TwitterException, IOException
   {
      twitter.updateStatus(message);
      
   }

   
  /******************  Part III - Test *******************/
  /** 
   * This method queries the tweets of a particular user's handle.
   * @param String  the Twitter handle (username) without the @sign
   */
   @SuppressWarnings("unchecked")
   public void queryHandle(String handle) throws TwitterException, IOException
   {
      statuses.clear();
      terms.clear();
      fetchTweets(handle);
      splitIntoWords();	
      removeCommonEnglishWords();
      sortAndRemoveEmpties();
      mostPopularWord(); 
   }
	
   /** 
    * This method fetches the most recent 2,000 tweets of a particular user's handle and 
    * stores them in an arrayList of Status objects.  Populates statuses.
    * @param String  the Twitter handle (username) without the @sign
    */
   public void fetchTweets(String handle) throws TwitterException, IOException
   {
      // Creates file for dedebugging purposes
      PrintStream fileout = new PrintStream(new FileOutputStream("tweets.txt")); 
      Paging page = new Paging (1,200);
      int p = 1;
      while (p <= 2)
      {
         page.setPage(p);
         statuses.addAll(twitter.getUserTimeline(handle,page)); 
         p++;        
      }
      numberOfTweets = statuses.size();
      fileout.println("Number of tweets = " + numberOfTweets);
   }   

   /** 
    * This method takes each status and splits them into individual words.   
    * Store the word in terms.  
    */
   public void splitIntoWords()
   {
	   for(Status tweet: statuses) {
		   ArrayList<String> words = splitWords(tweet.getText());
		   for(String word: words) {
			   terms.add(word);
		   }
	   } 
   }
   
   public ArrayList<String> splitWords(String txt) {
		int start=-1;
		int end=-1;
		ArrayList<String> words = new ArrayList<String>();
		for(int i=0; i<txt.length(); i++) {
			if((i==0 || txt.charAt(i-1)=='\t'||txt.charAt(i-1)=='\n'||txt.charAt(i-1)==' ')
					&& !(txt.charAt(i)=='\t'||txt.charAt(i)=='\n'||txt.charAt(i)==' ')) {
				start=i;
				end=start;
			}
			
			if(start!=-1) {
				end++;
				if(end==txt.length() || txt.charAt(end)=='\t'||txt.charAt(end)=='\n'||txt.charAt(end)==' '){
					words.add(removePunctuation(txt.substring(start, end)));
					start=-1;
				}
			}
		}return words;
	}

   /** 
     * This method removes common punctuation from each individual word.
     * This method changes everything to lower case.
     * Consider reusing code you wrote for a previous lab.     
     * Consider if you want to remove the # or @ from your words. Could be interesting to keep (or remove).
     * @ param String  the word you wish to remove punctuation from
     * @ return String the word without any punctuation, all lower case  
     */
   public String removePunctuation( String s )
   {
	   s=s.toLowerCase();
	      for(int i=0; i<s.length(); i++) {
	    	  if(s.charAt(i)=='\''||
	    			  s.charAt(i)=='\"'||
	    			  s.charAt(i)==','||
	    			  s.charAt(i)=='.'||
	    			  s.charAt(i)=='!'||
	    			  s.charAt(i)=='?'||
	    			  s.charAt(i)=='\\'||
	    			  s.charAt(i)=='/'||
	    			  s.charAt(i)==':'||
	    			  s.charAt(i)==';'||
	    			  s.charAt(i)=='_'||
	    			  s.charAt(i)=='-'||
	    			  s.charAt(i)=='#'||
	    			  s.charAt(i)=='@'||
	    			  s.charAt(i)=='('||
	    			  s.charAt(i)==')'||
	    			  s.charAt(i)=='['||
	    			  s.charAt(i)==']'||
	    			  s.charAt(i)=='{'||
	    			  s.charAt(i)=='}'||
	    			  s.charAt(i)=='•') {
	    		  String back = s.substring(0, i);
	    		  String front = s.substring(i+1);
	    		  s=back+front;
	    		  i--;
	    	  }
	      }
	    return s;    
   }

   /** 
    * This method removes common English words from the list of terms.
    * Remove all words found in commonWords.txt  from the argument list.    
    * The count will not be given in commonWords.txt. You must count the number of words in this method.  
    * This method should NOT throw an excpetion.  Use try/catch.   
    */
   @SuppressWarnings("unchecked")
   public void removeCommonEnglishWords()
   {  
	   ArrayList<String> commonWords = new ArrayList<String>();
	   try {
		   Scanner input = new Scanner(new File("commonWords.txt"));
		   while(input.hasNext()) {
			   String word=input.nextLine();
			   commonWords.add(word);
		   }
	   } catch (FileNotFoundException e) {
		   e.printStackTrace();
	   }
	   for(String commonWord: commonWords) {
		   for(int i=0; i<terms.size(); i++) {
			   if(terms.get(i).equals(commonWord.toLowerCase())) {
				   terms.remove(i);
				   i--;
			   }
		   }
	   }
   	
   }

   /** 
    * This method sorts the words in terms in alphabetically (and lexicographic) order.  
    * You should use your sorting code you wrote earlier this year.
    * Remove all empty strings while you are at it.  
    */
   @SuppressWarnings("unchecked")
   public void sortAndRemoveEmpties()
   {
      
	   for(int i=0; i<terms.size(); i++) {
		   if(terms.get(i).equals("")) {
			   terms.remove(i);
			   i--;
		   }
	   }
	   
	   for(int i=0; i<terms.size()-1; i++) {
		   int min = i;
		   for(int j=i; j<terms.size(); j++) {
			   if(terms.get(j).compareTo(terms.get(min))<0) {
				   min=j;
			   }
		   }
		   String temp = terms.get(i);
		   terms.set(i, terms.get(min));
		   terms.set(min, temp);
	   }
   	
   }
   
   /** 
	 * This method calculates the word that appears the most times
    * Consider case - should it be case sensitive?  The choice is yours.
    * @post will popopulate the frequencyMax variable with the frequency of the most common word 
    */
   @SuppressWarnings("unchecked")
   public void mostPopularWord()
   {
	   int maxFreq = 1;
	   int freq=0;
	   for(String term: terms) {
		  for(int i=0; i<terms.size(); i++) {
			  if(term.toLowerCase().equals(terms.get(i).toLowerCase())) {
				  freq++;
				  if(freq>maxFreq) {
					  maxFreq=freq;
					  popularWord=term;
				  }
			  }
		  }freq=0;
	   }frequencyMax=maxFreq;
      
   	
   }
   
   public void query55344(Query queryMiddle, Query queryLeft, Query queryUp) {
		queryMiddle.setCount(100);
		queryMiddle.setGeoCode(new GeoLocation(44.857842, -93.427033), 1.2, Query.MILES);
		queryMiddle.setSince("2018-1-1");
		queryMiddle.setLang("en");

		queryLeft.setCount(100);
		queryLeft.setGeoCode(new GeoLocation(44.862466, -93.468919), 0.6, Query.MILES);
		queryLeft.setSince("2018-1-1");
		queryLeft.setLang("en");

		queryUp.setCount(100);
		queryUp.setGeoCode(new GeoLocation(44.879253, -93.415252), 0.7, Query.MILES);
		queryUp.setSince("2018-1-1");
		queryUp.setLang("en");
   }
   
  /******************  Part IV *******************/
   public void investigate (String keyWord)
   {
		//Divided up my zip (55344) location into 3 parts (it has a weird shape like this:  _|
	   	if(keyWord==null) {
	   		keyWord="";
	   	}
	   	Query queryMiddle = new Query(keyWord);
		Query queryLeft = new Query(keyWord);
		Query queryUp = new Query(keyWord);
		query55344(queryMiddle, queryLeft, queryUp);
		
		try {
			QueryResult middleTweets = twitter.search(queryMiddle);
			QueryResult leftTweets = twitter.search(queryLeft);
			QueryResult upTweets = twitter.search(queryUp);
			
			//processing strings
			statuses.clear();
			terms.clear();
			statuses.addAll(middleTweets.getTweets());
			statuses.addAll(leftTweets.getTweets());
			statuses.addAll(upTweets.getTweets());
		    numberOfTweets = statuses.size();
		    
		    System.out.println("The tweets: ");
		    for(Status tweet: statuses) {
	            System.out.println("@"+tweet.getUser().getName()+ ": " + tweet.getText()); 
		    }
			
		    System.out.println("number of tweets: "+numberOfTweets);
			splitIntoWords();	
		    removeCommonEnglishWords();
		    sortAndRemoveEmpties();
		    mostPopularWord();
		} catch (TwitterException e) {
			e.printStackTrace();
		}
   }
 
  /** 
   * This method determines how many people in Arlington, VA 
   * tweet about the Miami Dolphins.  Hint:  not many. :(
   */
   public void sampleInvestigate ()
   {
      Query query = new Query("Miami Dolphins");
      query.setCount(100);
      query.setGeoCode(new GeoLocation(38.8372839,-77.1082443), 5, Query.MILES);
      query.setSince("2015-12-1");
      try {
         QueryResult result = twitter.search(query);
         System.out.println("Count : " + result.getTweets().size()) ;
         for (Status tweet : result.getTweets()) {
            System.out.println("@"+tweet.getUser().getName()+ ": " + tweet.getText());  
         }
      } 
      catch (TwitterException e) {
         e.printStackTrace();
      } 
      System.out.println(); 
   }  
}