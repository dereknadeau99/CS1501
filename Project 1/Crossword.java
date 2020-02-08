import java.io.*;
import java.util.*;

public class Crossword {

    DictInterface D;
    char[][] board;
    StringBuilder[] cols;
    StringBuilder[] rows;
    int width;
    
    public static void main(String[] args) throws IOException {
        new Crossword(args);
    }
    
    public Crossword(String[] args) throws IOException {
        //Read in the dictionary
        Scanner file = new Scanner(new FileInputStream(args[0]));
        D = new MyDictionary();
        while(file.hasNext()) { D.add(file.nextLine()); }
        file.close();

        //Read in game board as 2D char array
        Scanner in = new Scanner(new File(args[1]));
        width = Integer.parseInt(in.nextLine());
        board = new char[width][width];
        for(int i = 0; i < width; i++)
        {
            String curr = in.nextLine();
            for(int j = 0; j < width; j++)
            {
                board[i][j] = curr.charAt(j);
            }
        }
        in.close();
        cols = new StringBuilder[width];
        rows = new StringBuilder[width];
        //populate row and column arrays
        for(int i = 0; i < width; i++)
        {
            cols[i] = new StringBuilder();
            rows[i] = new StringBuilder();
        }
        solve(0,0);
    }

    public void solve(int row, int col)
    {
        if(board[row][col] == '-')
        {
            rows[row].append('-');
            cols[col].append('-');
            //Moving to next spot in row
            if(col < width-1)
            {
                solve(row, col+1);
            }
            //Move down to next row
            else
            {
                solve(row+1, 0);
            }
            rows[row].deleteCharAt(rows[row].length()-1);
            cols[col].deleteCharAt(cols[col].length()-1);
        }
        for(char c = 'a'; c <= 'z'; c++)
        {
            if(isValid(row, col, c, rows, cols))
            {
                rows[row].append(c);
                cols[col].append(c);

                //Solution found!
                if(col == width-1 && row == width-1)
                {
                    System.out.println("Solution found!");
                    printSol();
                    System.exit(0);
                }
                //Not on the last space so keep going
                else
                {
                    //Move to next space in row
                    if(col < width-1)
                    {
                        solve(row, col+1);
                    }
                    //Move down to next row
                    else
                    {
                        solve(row+1, 0);
                    }
                }
                rows[row].deleteCharAt(rows[row].length()-1);
                cols[col].deleteCharAt(cols[col].length()-1);
            }    
        }
    }

    public boolean isValid(int row, int col, char c, StringBuilder[] rows, StringBuilder[] cols) {

        if(board[row][col] != '+' && board[row][col] != c) {
            return false;
        }

        if(row < width && col < width) {
            rows[row].append(c);
            cols[col].append(c);
        } else {
        	return false;
        }

        //Case 1: At last spot. Both row and col must be words
        if(col == width-1 && row == width-1)
        {
            int rowRes = D.searchPrefix(rows[row], rows[row].lastIndexOf("-")+1, rows[row].length()-1);
            int colRes = D.searchPrefix(cols[col], cols[col].lastIndexOf("-")+1, cols[col].length()-1);
            rows[row].deleteCharAt(rows[row].length()-1);
            cols[col].deleteCharAt(cols[col].length()-1);
            return (rowRes == 2 || rowRes == 3) && (colRes == 2 || colRes == 3);
        }

        //Case 2: On the right edge but not last spot. Row must be word and col must be prefix
        else if(col == width-1 || board[row][col+1] == '-') {
            int rowRes = D.searchPrefix(rows[row], rows[row].lastIndexOf("-")+1, rows[row].length()-1);
            int colRes = D.searchPrefix(cols[col], cols[col].lastIndexOf("-")+1, cols[col].length()-1);
            rows[row].deleteCharAt(rows[row].length()-1);
            cols[col].deleteCharAt(cols[col].length()-1);
            return (rowRes == 2 || rowRes == 3) && (colRes == 1 || colRes == 3);
        }

        //Case 3: On bottom edge but not last spot. Col must be word and row must be prefix.
        else if(row == width-1 || board[row+1][col] == '-') {      
            
        	int rowRes = D.searchPrefix(rows[row], rows[row].lastIndexOf("-")+1, rows[row].length()-1);
            int colRes = D.searchPrefix(cols[col], cols[col].lastIndexOf("-")+1, cols[col].length()-1);
            rows[row].deleteCharAt(rows[row].length()-1);
            cols[col].deleteCharAt(cols[col].length()-1);
            return (colRes == 2 || colRes == 3) && (rowRes == 1 || rowRes == 3); 
            
        } 
        
      //Case 4: not on edge, both must be prefixes
        else { 
            
        	int rowRes = D.searchPrefix(rows[row], rows[row].lastIndexOf("-")+1, rows[row].length()-1);
            int colRes = D.searchPrefix(cols[col], cols[col].lastIndexOf("-")+1, cols[col].length()-1);
            rows[row].deleteCharAt(rows[row].length()-1);
            cols[col].deleteCharAt(cols[col].length()-1);
            return (rowRes == 1 || rowRes == 3) && (colRes == 1 || colRes == 3);
        }

    }

    public void printSol() {
        for(int i = 0; i < rows.length; i++) {
            System.out.println(rows[i].toString());
        }
    }
    
}
