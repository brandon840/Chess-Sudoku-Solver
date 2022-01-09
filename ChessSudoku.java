import java.util.*;
import java.io.*;
 
 
public class ChessSudoku
{
	/* SIZE is the size parameter of the Sudoku puzzle, and N is the square of the size.  For 
	 * a standard Sudoku puzzle, SIZE is 3 and N is 9. 
	 */
	public int SIZE, N;
 
	/* The grid contains all the numbers in the Sudoku puzzle.  Numbers which have
	 * not yet been revealed are stored as 0. 
	 */
	public int grid[][];
 
	/* Booleans indicating whether of not one or more of the chess rules should be 
	 * applied to this Sudoku. 
	 */
	public boolean knightRule;
	public boolean kingRule;
	public boolean queenRule;
 
	// Field that stores the same Sudoku puzzle solved in all possible ways
	public HashSet<ChessSudoku> solutions = new HashSet<ChessSudoku>();
 
 
	/* The solve() method should remove all the unknown characters ('x') in the grid
	 * and replace them with the numbers in the correct range that satisfy the constraints
	 * of the Sudoku puzzle. If true is provided as input, the method should find finds ALL 
	 * possible solutions and store them in the field named solutions. */
	public void solve(boolean allSolutions) {
 
		ArrayList<Integer> possibleNumbers = new ArrayList<Integer>();
 
		int[][] gridCopy;
		gridCopy=deepCopy(this.grid);
 
 
		int emptySquares = 0; //This will tell us how many blank squares we have to fill
 
		for (int i=0;i<N;i++){
			for (int j=0;j<N;j++){
 
				if (gridCopy[i][j] == 0) {emptySquares++;}
			}
		}
 
		for (int i=0;i<N;i++){
			for (int j=0;j<N;j++){
 
				if (gridCopy[i][j] == 0) {
 
					possibleNumbers=getPossibleNumbers(gridCopy, i, j);
 
					for (Integer num: possibleNumbers){
						problemSolved(gridCopy,emptySquares-1,i,j,num,allSolutions);
					}
 
				}
			}
		}
 
 
	}
 
	private void problemSolved (int[][] grid, int squares, int row, int column, int number, boolean allSolutions){
 
		grid[row][column]=number;//update grid
 
		if (squares==0){
			if (allSolutions){
				ChessSudoku temp = new ChessSudoku(SIZE);
				temp.queenRule = this.queenRule;
				temp.kingRule = this.kingRule;
				temp.knightRule = this.knightRule;
				temp.grid = deepCopy(grid);
				this.solutions.add(temp);
			}
			this.grid = deepCopy(grid);
 
			grid[row][column]=0;
			return;
		}
 
 
		ArrayList<Integer> possibleNumbers;
 
		for (row=0;row<N;row++) {
			for (column = 0; column < N; column++) {
 
				if (grid[row][column] == 0) {
 
					possibleNumbers = getPossibleNumbers(grid, row, column);
 
					if (possibleNumbers.size() == 0) {
						grid[row][column] = 0;
						return;
					}
 
					for (Integer num : possibleNumbers) {
						problemSolved(grid, squares - 1, row, column, num, allSolutions);
 
					}
 
					//If we haven't returned after going through all possibilities, we must backtrack
					grid[row][column] = 0;
					return;
 
				}
			}
		}
	}
 
	private int[][] deepCopy(int[][] original){
		if (original == null) {
			return null;
		}
 
		int[][] result = new int[original.length][];
 
		for (int i = 0; i < original.length; i++) {
			result[i] = Arrays.copyOf(original[i], original[i].length);
		}
		return result;
	}
 
	private ArrayList<Integer> getPossibleNumbers(int[][] grid, int row, int column){
 
		ArrayList<Integer> numbers = new ArrayList<Integer>();
 
		numbers = checkRowsColumns(grid,row,column);
		numbers = checkBox(numbers, grid, row, column);
 
		if (knightRule){numbers = checkKnightRule(numbers,grid,row,column);}
		if (kingRule){numbers = checkKingRule(numbers,grid,row,column);}
 
		//We only check for the biggest integer, aka N
		if (queenRule && numbers.contains(N) && !checkQueenRule(grid,row,column)){
			for (int i=0;i<numbers.size();i++){
				if (numbers.get(i)==N){numbers.remove(i);}
			}
		}
 
		return numbers;
	}
 
	private ArrayList<Integer> checkRowsColumns(int[][] grid, int row, int column){
 
 
		ArrayList<Integer> numbers = new ArrayList<Integer>();
 
		for(int num=1;num<=N;num++){
 
			boolean legal = true;
 
			for(int j=0;j<N;j++)
			{
 
				if (grid[row][j]==num){
					legal=false;
					break;
				}
			}
 
			if (legal){
				for(int j=0;j<N;j++){
					if (grid[j][column]==num){
						legal=false;
						break;
					}
				}
			}
 
			if (legal) {numbers.add(num);}
		}
 
		return numbers;
	}
 
	private ArrayList<Integer> checkBox(ArrayList<Integer> numbers, int[][] grid, int row, int column){
 
		ArrayList<Integer> temp = new ArrayList<Integer>();
 
		int startRow = row - (row%(SIZE));
		int endRow = startRow + SIZE;
		int startColumn = column - (column%(SIZE));
		int endColumn = startColumn + SIZE;
 
		for (Integer num: numbers){
			boolean legal=true;
 
			for (int i=startRow;i<endRow;i++){
				for(int j=startColumn;j<endColumn;j++){
					if(grid[i][j]==num){
						legal=false;
						break;
					}
 
				}
				if (!legal){break;}
			}
 
			if (legal){temp.add(num);}
		}
 
		return temp;
 
 
	}
 
	private ArrayList<Integer> checkKnightRule (ArrayList<Integer> numbers, int[][] grid, int row, int column){
		ArrayList<Integer> temp = new ArrayList<Integer>();
 
		for (Integer num:numbers){
			boolean legal = true;
 
			//Up 2 right 1
			if (row-2 >= 0 && column+1 < N && grid[row-2][column+1]==num) {legal=false;}
 
			//Up 2 left 1
			else if(row-2 >= 0 && column-1 >= 0 && grid[row-2][column-1]==num) {legal=false;}
 
			//Right 2 up 1
			else if(row-1 >= 0 && column+2 < N && grid[row-1][column+2]==num) {legal=false;}
 
			//Right 2 down 1
			else if(row+1 < N && column+2 < N && grid[row+1][column+2]==num) {legal=false;}
 
			//Down 2 right 1
			else if(row+2 < N && column+1 < N && grid[row+2][column+1]==num) {legal=false;}
 
			//Down 2 left 1
			else if(row+2 < N && column-1 >= 0 && grid[row+2][column-1]==num) {legal=false;}
 
			//Left 2 up 1
			else if(row-1 >= 0 && column-2 >= 0 && grid[row-1][column-2]==num) {legal=false;}
 
			//Left 2 down 1
			else if(row+1 < N && column-2 >= 0 && grid[row+1][column-2]==num) {legal=false;}
 
			if (legal){temp.add(num);}
		}
 
		return temp;
	}
	private ArrayList<Integer> checkKingRule (ArrayList<java.lang.Integer> numbers, int[][] grid, int row, int column){
		ArrayList<Integer> temp = new ArrayList<Integer>();
 
		for (Integer num:numbers){
			boolean legal = true;
 
			//Top right
			if ((row-1>=0) && (column+1 < N) && (grid[row-1][column+1]==num)){legal=false;}
			//Top left
			else if((row-1>=0) && (column-1 >= 0) && (grid[row-1][column-1]==num)){legal=false;}
			//Bottom right
			else if((row+1<N) && (column+1 < N) && (grid[row+1][column+1]==num)){legal=false;}
			//Bottom left
			else if((row+1<N) && (column-1 >= 0) && (grid[row+1][column-1]==num)){legal=false;}
 
			if (legal){temp.add(num);}
		}
 
		return temp;
	}
	private boolean checkQueenRule (int[][] grid, int row, int column){
 
		ArrayList<Integer> temp = new ArrayList<Integer>();
 
		int i=row;
		int j=column;
 
		//Top right diagonal
		while (i>=0 && j<N){
 
			if (grid[i][j]==N) {return false;}
 
			i--;
			j++;
		}
		i=row;
		j=column;
		//Top left diagonal
		while (i>=0 && j>=0){
 
			if (grid[i][j]==N) {return false;}
 
			i--;
			j--;
		}
		i=row;
		j=column;
		//Bottom left diagonal
		while (i<N && j>=0){
 
			if (grid[i][j]==N) {return false;}
 
			i++;
			j--;
		}
		i=row;
		j=column;
 
		//Bottom right diagonal
		while (i<N && j<N){
 
			if (grid[i][j]==N) {return false;}
 
			i++;
			j++;
		}
 
		return true;
	}
 
 
	/*****************************************************************************/
	/* NOTE: YOU SHOULD NOT HAVE TO MODIFY ANY OF THE METHODS BELOW THIS LINE. */
	/*****************************************************************************/
 
	/* Default constructor.  This will initialize all positions to the default 0
	 * value.  Use the read() function to load the Sudoku puzzle from a file or
	 * the standard input. */
	public ChessSudoku( int size ) {
		SIZE = size;
		N = size*size;
 
		grid = new int[N][N];
		for( int i = 0; i < N; i++ ) 
			for( int j = 0; j < N; j++ ) 
				grid[i][j] = 0;
	}
 
 
	/* readInteger is a helper function for the reading of the input file.  It reads
	 * words until it finds one that represents an integer. For convenience, it will also
	 * recognize the string "x" as equivalent to "0". */
	static int readInteger( InputStream in ) throws Exception {
		int result = 0;
		boolean success = false;
 
		while( !success ) {
			String word = readWord( in );
 
			try {
				result = Integer.parseInt( word );
				success = true;
			} catch( Exception e ) {
				// Convert 'x' words into 0's
				if( word.compareTo("x") == 0 ) {
					result = 0;
					success = true;
				}
				// Ignore all other words that are not integers
			}
		}
 
		return result;
	}
 
 
	/* readWord is a helper function that reads a word separated by white space. */
	static String readWord( InputStream in ) throws Exception {
		StringBuffer result = new StringBuffer();
		int currentChar = in.read();
		String whiteSpace = " \t\r\n";
		// Ignore any leading white space
		while( whiteSpace.indexOf(currentChar) > -1 ) {
			currentChar = in.read();
		}
 
		// Read all characters until you reach white space
		while( whiteSpace.indexOf(currentChar) == -1 ) {
			result.append( (char) currentChar );
			currentChar = in.read();
		}
		return result.toString();
	}
 
 
	/* This function reads a Sudoku puzzle from the input stream in.  The Sudoku
	 * grid is filled in one row at at time, from left to right.  All non-valid
	 * characters are ignored by this function and may be used in the Sudoku file
	 * to increase its legibility. */
	public void read( InputStream in ) throws Exception {
		for( int i = 0; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				grid[i][j] = readInteger( in );
			}
		}
	}
 
 
	/* Helper function for the printing of Sudoku puzzle.  This function will print
	 * out text, preceded by enough ' ' characters to make sure that the printint out
	 * takes at least width characters.  */
	void printFixedWidth( String text, int width ) {
		for( int i = 0; i < width - text.length(); i++ )
			System.out.print( " " );
		System.out.print( text );
	}
 
 
	/* The print() function outputs the Sudoku grid to the standard output, using
	 * a bit of extra formatting to make the result clearly readable. */
	public void print() {
		// Compute the number of digits necessary to print out each number in the Sudoku puzzle
		int digits = (int) Math.floor(Math.log(N) / Math.log(10)) + 1;
 
		// Create a dashed line to separate the boxes 
		int lineLength = (digits + 1) * N + 2 * SIZE - 3;
		StringBuffer line = new StringBuffer();
		for( int lineInit = 0; lineInit < lineLength; lineInit++ )
			line.append('-');
 
		// Go through the grid, printing out its values separated by spaces
		for( int i = 0; i < N; i++ ) {
			for( int j = 0; j < N; j++ ) {
				printFixedWidth( String.valueOf( grid[i][j] ), digits );
				// Print the vertical lines between boxes 
				if( (j < N-1) && ((j+1) % SIZE == 0) )
					System.out.print( " |" );
				System.out.print( " " );
			}
			System.out.println();
 
			// Print the horizontal line between boxes
			if( (i < N-1) && ((i+1) % SIZE == 0) )
				System.out.println( line.toString() );
		}
	}
 
 
	/* The main function reads in a Sudoku puzzle from the standard input, 
	 * unless a file name is provided as a run-time argument, in which case the
	 * Sudoku puzzle is loaded from that file.  It then solves the puzzle, and
	 * outputs the completed puzzle to the standard output. */
	public static void main( String args[] ) throws Exception {
		InputStream in = new FileInputStream("queenSudokuEasy3x3.txt");
 
		// The first number in all Sudoku files must represent the size of the puzzle.  See
		// the example files for the file format.
		int puzzleSize = readInteger( in );
		if( puzzleSize > 100 || puzzleSize < 1 ) {
			System.out.println("Error: The Sudoku puzzle size must be between 1 and 100.");
			System.exit(-1);
		}
 
		ChessSudoku s = new ChessSudoku( puzzleSize );
		
		// You can modify these to add rules to your sudoku
		s.knightRule = false;
		s.kingRule = false;
		s.queenRule = true;
		
		// read the rest of the Sudoku puzzle
		s.read( in );
 
		System.out.println("Before the solve:");
		s.print();
		System.out.println();
 
		// Solve the puzzle by finding one solution.
		s.solve(true);
 
		// Print out the (hopefully completed!) puzzle
		System.out.println("After the solve:");
		s.print();
	}
}
