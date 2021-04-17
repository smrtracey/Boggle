/*
Sean Tracey
18743769

	Boggle:
	A word game where the player is given 2 minutes to find as many words as they can in a 4 by 4 grid made up of 16 shuffled 6 faced dice.
	
	-On start, create a new Boggle object called game.
	-Get input from the user to either start the game or get instructions on how to play.
	-If instructions, print them to the screen, if game, run the game. Else, prompt user to enter valid choice.
	-Create a new gameboard with random selection and print it to the screen. Start a 2 minute timer.
	-While the timer is running, get string inputs from player.
	-If these inputs are valid words, calculate their score and add them to the foundWords list.
	-Include number entries for special methods to end the game early and to print the gameboard again.(0 and 1)
	-When the time is up, end the game and show user their score as well as options to print the foundWords and invalid words lists.
	-Also include options to play again or exit the game.
*/

import java.util.*;
import java.io.*;


public class Game{
	public static void main(String[] args)
	{
		Boggle game = new Boggle();
		Scanner sc = new Scanner(System.in);

		System.out.println("Welcome to the game.");
		System.out.println("To start the game, press 1.");
		System.out.println("For game info, press 2.");
		
		int choice = sc.nextInt();
		boolean valid = false;
		while(!valid)
		{

			if(choice ==1)
			{
				game.run();
			}

			if(choice ==2)
			{
				
				showRules();
				System.out.println("Press 1 to play or 2 to exit");
				choice = sc.nextInt();

				if(choice ==1)
				{
					game.run();
					valid = true;
				}

				else if(choice ==2)
				{	
					System.out.println("Testing quit.");
					System.exit(0);
				}

				else
				{
					System.out.println("Please choose between 1 and 2.");
					choice = sc.nextInt();
				}
			}
			
		}

	}

	public static void showRules()
	{
		System.out.println("The goal of the game is to get as many points as possible");
		System.out.println("You will have 2 minutes");
		System.out.println("To gain points, the player must create words from the randomly assorted letters in a grid of dice");
		System.out.println("The longer the word, the higher the point value");
		System.out.println("Words must be formed using adjacent letters-the letters must touch- either horizontally, vertically or diagonally");
		System.out.println("Each dice can be used only once per word");
		System.out.println("Words must be at least 3 letters long and in the dictionary.\n\n");

		System.out.println("No. of Letters | Points per Word");
		System.out.println("       3       |        1");
		System.out.println("       4       |        1");
		System.out.println("       5       |        2");
		System.out.println("       6       |        3");
		System.out.println("       7       |        5");
		System.out.println("       8+      |        11\n\n");
	}
}

class Boggle {
	private GameBoard board;
	private int score;
	private Stack foundWords;
	private Stack notWords;
	private int timeLeft = 120;//2 minute game.
	private boolean gameOver = false;

	Scanner sc = new Scanner(System.in);
	
	//constructor
	public Boggle(){
		board = new GameBoard();
		score = 0;
		foundWords = new Stack();
		notWords = new Stack();
	}
	
	public void run(){
		
		System.out.println("Enter as many words as you see on the board OR press 0 to quit. \n");
		System.out.println("If you want to print the board again, press 1.");
		board.show();

		Scanner sc = new Scanner(System.in);
		
		// Using java timer and TimerTask to set a 2 minute game clock.
		Timer timer = new Timer();
		timer.schedule(new TimerTask(){
			public void run(){
				endgame();
			}
		}, timeLeft * 1000);//when the time is up, end the game.
		
		Timer counter = new Timer();
		counter.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				timeLeft--;
			}
		}, 1000, 1000);

		while(true){

			while(!gameOver)
			{
			String word = sc.nextLine();
			word = word.replaceAll("\\s+","");//Remove white space from players entry to help with matching Strings later.

			//If player exits manually
			if (word.equals("0"))
			{
				endgame();
			}

			//print board again if player presses 1.
			if (word.equals("1"))
			{
				board.show();
			}
			
			//A valid word must be at least 3 letters long
			if (word.length() < 3 && !word.equals("1") && !word.equals("0"))
			{
				System.out.println("Words must have at least 3 letters!");
				continue;
			}
			
			//Can't use the same word more than once
			if (alreadyFound(word))
			{
				System.out.println("You used that word already!");
				continue;
			}
			
			//If the word is possible and in the dictionary, add it's score and add it to the foundWords list.
			if (board.search(word))
			{
				score += getScore(word);
				foundWords.push(word);
			}

			//If the word does not exist, add it to the notWords list.
			if(!board.search(word))
			{
				notWords.push(word);
			}
		
		}

		}
	}
	
	//Pretty straight forward.Different length words give different scores.
	public int getScore(String word){
		if (word.length() < 7)
		{
			return word.length() - 2;
		} 
		else if (word.length() == 7)
		{
			return 6;
		}
		else if (word.length() == 8)
		{
			return 10;
		}
		else
		{
			return 15;
		}
	}
	
	//method to make sure word has not been played before
	public boolean alreadyFound(String word){
		
			return foundWords.alreadyFound(word);
	}

	public void endgame()
	{
		gameOver = true;
		System.out.println("Thanks for playing! Hope you had fun");
		System.out.println("Your score is: " + score);

		endMenu();
	}

	public void endMenu()
	{
		System.out.println("Please use the number keys to select an option");
		System.out.println("1.Show found words");
		System.out.println("2.Show invalid words");
		System.out.println("3.Play again");
		System.out.println("4.Exit");

		int option = sc.nextInt();

		if(option ==1)
		{
			System.out.println("Words found:");
			foundWords.display();

			endMenu();

		}

		else if(option == 2)
		{

			if(!notWords.isEmpty())
			{
				System.out.println("These aren't words...");
				notWords.display();
			}

			else
			{
				System.out.println("No invalid words found");
			}
			
			endMenu();
		}

		else if(option == 3)
		{
			Boggle game = new Boggle();
			game.run();
		}

		else if(option == 4)
		{
			System.exit(0);
		}
	}	
}

class GameBoard
{
	
	FileIO reader = new FileIO();
    String[] dictionary = reader.load("C:/dictionary.txt");

    //Game dice with possible faces.Changed these to Strings as the substring() method seemed to be working more
    //consistantly than the charAt() method. 
    String[][][] dice ={
			{{"A","A","E","E","N","G"},{"A","H","S","P","C","O"},{"A","S","P","F","F","K"},{"O","B","J","O","A","B"}},
			{{"I","O","T","M","U","C"},{"R","Y","V","D","E","L"},{"L","R","E","I","X","D"},{"E","I","U","N","E","S"}},
			{{"W","N","G","E","E","H"},{"L","N","H","N","R","Z"},{"T","S","T","I","Y","D"},{"O","W","T","O","A","T"}},
			{{"E","R","T","T","Y","L"},{"T","O","E","S","S","I"},{"T","E","R","W","H","V"},{"N","U","I","H","M","Q"}}
		};


	//Creates the gameBoard by randomly selecting a face from each dice then placing them randomly within the array.
    String[][]gameboard = shuffle(dice);

	public String[][] shuffle(String[][][] dice)
	{

		Random rand = new Random();

		String[][] gameboard = new String[4][4];

		for(int i = 0;i<4;i++)
		{
			for(int j = 0;j<4;j++)
			{
				int roll = (int)(Math.random()*6+1);

				gameboard[i][j] = dice[i][j][roll-1];
			}
		}
		
		for (int i = 3; i >0; i--) {
			for(int j= 3;j<0;j--)
			{
				int rand1 = rand.nextInt(i+1);
				int rand2 = rand.nextInt(j+1);

				String temp = gameboard[i][j];
				gameboard[i][j] = gameboard[rand1][rand2];
				gameboard[rand1][rand2] = temp;
			}
		}

		return gameboard;
	}

	//This method just prints the Array with some lines for readability purposes.
	public void show()
    {

		System.out.println("-------------------");
		for(int i = 0;i <4;i++)
		{
			for(int j = 0;j < 4;j++)
			{
				System.out.print(" | " + gameboard[i][j]);
			}

			System.out.print( " | ");
			System.out.println();
		}

		System.out.println("-------------------");
	}


	/*
	The searching is broken into three methods.
	First, search the array for the first letter of the word.When it's found, call the second searching method
	to check the surrounding squares to see if the next character in the word is on one of them.
	Finally, if the word can be made with the given dice, call the third search function which will
	use a binary search to find the word in the dictionary.
	Only if all searchs return true will the word be added to the foundWords list.
	*/

	public boolean search(String word)
	{

		for(int i = 0;i<4;i++)
		{
			for(int j = 0;j<4;j++)
			{
				
				if(gameboard[i][j].equalsIgnoreCase(word.substring(0,1)))//if we find the first letter of the word.
				{
					
					String matchingWord = "" + word.substring(0,1);

					//need to keep track of the squares already used.Can't use the same square twice.
					boolean[][] checked = new boolean[4][4];
					checked[i][j]= true;
					
					if(boardSearch(word,matchingWord,i,j,checked))
					{
						return true;
					}
				}
			}
		}
		
		return false;
	}


	public boolean boardSearch(String word, String matchingWord, int row, int col,boolean[][] checked)
	{

		if(matchingWord.equals(word))
		{
			return true;
		}

		//Start from the previous row/column unless the first letter is in the first row or column.
		int startingRow = row-1;

		if(startingRow<0)
		{
			startingRow =0;
		}

		int startingCol = col-1;

		if(startingCol <0)
		{
			startingCol = 0;
		}


		for(int i = startingRow;i<=row+1 && i<4;i++)
		{

			for(int j = startingCol;j<=col+1 && j<4;j++)
			{

				if(gameboard[i][j].equalsIgnoreCase(word.substring(matchingWord.length(), matchingWord.length()+1)))
				{
					String newWord = matchingWord + word.substring(matchingWord.length(),matchingWord.length()+1);
					checked[i][j] = true;

					//If the word the player entered can be made on the board, then search the dictionary for it.
					if(boardSearch(word,newWord,i,j,checked))
					{	
						return dictionarySearch(word,dictionary);
					}

				}	
			}
		}
				
		return false;
		
	}

	//Binary search Method for dictionary.
	public boolean dictionarySearch(String word,String[] dictionary){

		//Dictionary is all lower case so make sure players word match.
		word = word.toLowerCase();
		
		int left = 0, right = dictionary.length-1;
		while(left<=right)
		{
			int middle = left +(right-left)/2;
			
			//Remove white space to avoid probelms with compareTo method.
			int result = word.compareTo(dictionary[middle].replaceAll("\\s+",""));

			if(result ==0)
			{
				return true;
			}

			if(result>0)
			{
				left =middle+1;
			}

			else
			{
				right = middle-1;
			}
			
		}
		return false;

	}
}

class Link 
{
	public String data;
    public Link next; 

	public Link(String data)
	{ 
		this.data = data; 
	}

	public void displayLink()
	{
		System.out.println(this.data);
	}
}

class LinkedList 
{		
	private Link first; 
	public LinkedList()
	{ 
			first = null; 
	}

	public boolean isEmpty()
	{ 
		return (first==null);
	}

	public void insertHead(String data)
	{ 
		Link newLink = new Link(data);
		newLink.next = first; // newLink --> old first
		first = newLink; // first --> newLink
	}

	public boolean alreadyFound(String data){
		Link current = first; // start with first link
		while(current!=null)
		{
			if(current.data.equalsIgnoreCase(data))
			{
				return true;
			};

			current=current.next;
		}
		return false;
	}

	public void display(){
		Link current = first; 
		while(current!=null)
		{
			current.displayLink(); 
			current=current.next;
		}
	}}

//Abstraction for the LinkedList.
class Stack
{
	private LinkedList list;
	public Stack()	
	{ 
		list = new LinkedList();
	}

	public void push(String data)
	{ 
		list.insertHead(data);
	}

	public void display()
	{
		list.display();
	}

	public boolean isEmpty()
	{
		return list.isEmpty();
	}

	public boolean alreadyFound(String data)
	{
		return list.alreadyFound(data);
	}
} 

class FileIO{

  public String[] load(String file) {
    File aFile = new File(file);
    StringBuffer contents = new StringBuffer();
    BufferedReader input = null;
    try {
      input = new BufferedReader( new FileReader(aFile) );
      String line = null;
      int i = 0;
      while (( line = input.readLine()) != null){
        contents.append(line);
        i++;
        contents.append(System.getProperty("line.separator"));
      }
    }
    catch (FileNotFoundException ex) {
      System.out.println("Can't find the file - are you sure the file is in this location: "+file);
      ex.printStackTrace();
    }
    catch (IOException ex){
      System.out.println("Input output exception while processing file");
      ex.printStackTrace();
    }
    finally {
      try {
        if (input!= null) {
          input.close();
        }
      }
      catch (IOException ex) {
        System.out.println("Input output exception while processing file");
        ex.printStackTrace();
      }
    }
    String[] array = contents.toString().split("\n");
    for(String s: array){
        s.trim();
    }
    return array;
  }


  public void save(String file, String[] array) throws FileNotFoundException, IOException {

    File aFile = new File(file);
    Writer output = null;
    try {
      output = new BufferedWriter( new FileWriter(aFile) );
      for(int i=0;i<array.length;i++){
        output.write( array[i] );
        output.write(System.getProperty("line.separator"));
      }
    }
    finally {
      if (output != null) output.close();
    }
  }
}