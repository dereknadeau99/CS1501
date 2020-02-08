import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class CrosswordFinder {

	StringBuilder[] cols;
    StringBuilder[] rows;
	char [][] board;
	private DictInterface D;
	private int width;
	
	public static void main(String[] args) throws IOException {
		
		try {
			String Dtype = args[0];
			String dict  = args[1];
			String board = args[2];
			new CrosswordFinder(Dtype, dict, board);
		} catch (Exception e) {
			System.out.println("No input args. Running default settings.");
			new CrosswordFinder();
		}
				
	}
	
	CrosswordFinder(String dtype, String dict, String board2) throws IOException {
	
		if (dtype.equalsIgnoreCase("DLB")) 				{ D = new DLB(); } 
		else if (dtype.equalsIgnoreCase("Dictionary")) 	{ D = new MyDictionary(); } 
		getCrosswordBoard(board2);
		printCW();
		solve(0,0);
		
	}
	
	public CrosswordFinder() throws IOException {
		new CrosswordFinder("DLB", "dict8.txt", "test3a.txt");
	}
	
	public void solve(int x, int y) {
		
		if (board[x][y] != '+' ) {
			setChar(x, y, board[x][y]);
			next(x,y);
			undo(x,y);
		}
		
		for (char c = 'a'; c <= 'z'; c++) {
			if (isValid(x,y,c)) {
				
				setChar(x,y,c);
				
				// found solution
				if ((x == width - 1) && (y == width - 1)) { 
					printSolution();
					System.exit(0);
				}
				
				// board not full yet
				else { next(x,y); }
				undo(x,y);
			}
		}
	}

	private boolean isValid(int x, int y, char c) {

		if (x == width - 1) {
			for (int i = 0; i < y; i++) {
				if (i != 0) {System.out.println(i);}
				if (D.searchPrefix(rows[i]) < 2) {
					return false;
				}
			}
		}
		
		if (y == width - 1) {
			for (int i = 0; i < x; i++) {
				if (i != 0) {System.out.println(i);}
				if (D.searchPrefix(cols[i]) < 2) {
					return false;
				}
			}
		}
		
		
		// final case - board is full, check if valid
		if (x == width - 1 && y == width - 1) {
			for (int i = 0; i < width; i++) {
				if (D.searchPrefix(rows[i]) < 2 || D.searchPrefix(cols[i]) < 2) {
					return false;
				}
			}
		}
		
		return true;
	}

	// recurse backwards and undo previous decisions
	private void undo(int x, int y) {
		setChar(x,y,' ');
	}
	
	// set character at correct point in each SB
	private void setChar(int x, int y, char c) {
		rows[y].setCharAt(x, c);		
		cols[x].setCharAt(y, c);
	}
	
	// choose next part of board to solve
	private void next(int x, int y) {
		if (x < width-1) { solve(x+1, y); } // move to next spot in row
		else 			 { solve(0, y+1); } // move to first spot in next row
	}

	// show crossword board format
	public void printCW() {
		
		for (int y = 0; y < width; y++) {
			for (int x = 0; x < width; x++) {
					System.out.print(board[x][y]);
			}
			System.out.println();
		}
	}
	
	// show crossword board solution
	public void printSolution() {
        for(int i = 0; i < rows.length; i++) {
            System.out.println(rows[i].toString());
        }
	}
	
	// retrieves format of crossword board and initalizes SBs
	private void getCrosswordBoard(String s) throws IOException {
		
		Scanner S = new Scanner(new FileInputStream(s));
		width = Integer.parseInt(S.next());
		
		cols  = new StringBuilder[width];
		rows  = new StringBuilder[width];
		board = new char [width] [width];
		
		for (int i = 0; i < width; i++) {
				cols[i] = new StringBuilder(); rows[i] = new StringBuilder();
			for (int j = 0; j < width; j++) {
				cols[i].append(" "); 		   rows[i].append(" ");
			}
		}
			
		for (int y = 0; y < width; y++) {
			String v = S.next();
			for (int x = 0; x < width; x++) {
				board[x][y] = v.charAt(x);
			}
		}
		S.close();
	}
	
	// fills dictionary/DLB with data
	public DictInterface getDictionary(String file, DictInterface D) throws IOException {
		Scanner S = new Scanner(new FileInputStream(file));
		while (S.hasNext()) { D.add(S.nextLine()); }
		S.close();
		return D;
	}

}
