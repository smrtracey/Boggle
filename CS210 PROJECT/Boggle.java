import java.util.*;
public class Boggle{
	public static void main(String[] args)
	{
		
		Game game = new Game();
		Scanner sc = new Scanner(System.in);

		System.out.println("Welcome to the game.");
		System.out.println("For game info, type \"info\".");
		System.out.println("To start the game, type \"play\".");


		String choice = sc.nextLine();
		boolean valid = false;
		while(!valid)
		{
			if(choice.equals("info"))
			{
				
				showRules();
				System.out.println("Type play to play or quit to exit");
				choice = sc.nextLine();
			}

			else if(choice.equals("play"))
			{
				game.run();
				valid = true;
			}

			else if(choice.equals("quit"))
			{
				System.exit(0);
			}

			else
			{
				System.out.println("Please choose between \"info\" and \"play\".");
				choice = sc.nextLine();
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

class Game {
	private GameBoard board;
	private int score;
	private Stack foundWords;
	private int timeLeft = 120;//2 minute game.
	

	//constructor
	public Game(){
		board = new GameBoard();
		score = 0;
		foundWords = new Stack();
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
				System.out.println("Time up! Your final score is " + score);
				System.out.println("Words found:");
				foundWords.display();
				System.exit(0);
			}
		}, timeLeft * 1000);//after 2 minutes, end the game and print the score and valid words found.
		
		Timer counter = new Timer();
		counter.scheduleAtFixedRate(new TimerTask(){
			public void run(){
				timeLeft--;
			}
		}, 1000, 1000);

		while(true){
			String word = sc.nextLine();
			word = word.replaceAll("\\s+","");//Remove white space from players entry to help with matching Strings later.


			//If player exits manually
			if (word.equals("0")){
				System.out.println("Thanks for Playing! Your score is: " + score);
				System.out.println("Words found: ");
				foundWords.display();//Print words in the list.
				System.exit(0);
			}

			//print board again if player presses 1.
			if (word.equals("1")){
				board.show();
			}
			
			
			//A valid word must be at least 3 letters long
			if (word.length() < 3 && !word.equals("1")){
				System.out.println("debugging: word is " +word);
				System.out.println("Words must have at least 3 letters!");
				continue;
			}
			
			//Can't use the same word more than once
			if (alreadyFound(word)){
				System.out.println("You used that word already!");
				continue;
			}
			
			//If the word is possible and in the dictionary, add it to the foundWords list.
			if (board.search(word)){
				score += getScore(word);
				foundWords.push(word);
			}
			
		}
	}
	
	//Pretty straight forward.Different length words give different scores.
	private int getScore(String word){
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
	private boolean alreadyFound(String word){
		
			return foundWords.alreadyFound(word);
	}	
}

class GameBoard
{
	
	FileIO reader = new FileIO();
    String[] dictionary = reader.load("C:/Users/seanm/OneDrive/Desktop/CS210 PROJECT/dictionary.txt");
    //Game dice with possible faces. Changed these from character to strings to include the "Qu" face.
    //This means I'll be using substrings and .equals() methods instead of .charAt().
    String[][][] dice ={
			{{"A","A","E","E","N","G"},{"A","H","S","P","C","O"},{"A","S","P","F","F","K"},{"O","B","J","O","A","B"}},
			{{"I","O","T","M","U","C"},{"R","Y","V","D","E","L"},{"L","R","E","I","X","D"},{"E","I","U","N","E","S"}},
			{{"W","N","G","E","E","H"},{"L","N","H","N","R","Z"},{"T","S","T","I","Y","D"},{"O","W","T","O","A","T"}},
			{{"E","R","T","T","Y","L"},{"T","O","E","S","S","I"},{"T","E","R","W","H","V"},{"N","U","I","H","M","Qu"}}
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

		
		//Randomize order of letters in grid.
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
					//TODO Try this with Boolean array 

					//List<int[]> used = new ArrayList<int[]>();
					//int[] pos = {i,j};

					//used.add(pos);

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
				
				//check that this square hasn't been used.
			/*	if(hasBeenUsed(i,j,used))
				{
					continue;
				}
			*/

				if(gameboard[i][j].equalsIgnoreCase(word.substring(matchingWord.length(), matchingWord.length()+1)))
				{
					String newWord = matchingWord + word.substring(matchingWord.length(),matchingWord.length()+1);
					checked[i][j] = true;

					//If the word the player entered can be made on the board-
					if(boardSearch(word,newWord,i,j,checked))
					{	
						return dictionarySearch(word,dictionary);
					}

				}	
			}
		}
				
		return false;
		
	}


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
	public String data; // data item
    public Link next; // next link in list

	public Link(String data)
	{ 
		this.data = data; // initialize data
	}

	public void displayLink()
	{
		System.out.println(this.data);
	}
}

class LinkedList 
{		
	private Link first; // ref to first link
	public LinkedList()
	{ // constructor
			first = null; // no links on list yet
	}

	public boolean isEmpty()
	{ // true if list is empty
	return (first==null);
	}

	public void insertHead(String data)
	{ // make new link
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
		}; //print out the link
		current=current.next;
		//keep going until you come to the end
		}
		return false;
	}

	public void display(){
		Link current = first; // start with first link
		while(current!=null)
		{
		current.displayLink(); //print out the link
		current=current.next;
		//keep going until you come to the end
		}
	}
}

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

	public boolean alreadyFound(String data)
	{
		return list.alreadyFound(data);
	}
} 