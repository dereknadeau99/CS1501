/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *
 *************************************************************************/

public class LZWmod {

	private static final int R = 256;		// number of input chars
	private static int L = 512;       		// number of codewords = 2^W
	private static int W = 9;         		// codeword width
	private static final int MAX_W = 16;

	public static void compress(boolean reset) {

		TSTmod<Integer> st = new TSTmod<Integer>();

		for (int i = 0; i < R; i++) {
			st.put(new StringBuilder("" + (char) i), i);
		} // fill codebook with ASCII
		int code = R+1;  // R is codeword for EOF

		BinaryStdOut.write(reset);	

		//initialize the current string
		StringBuilder current = new StringBuilder();
		//read and append the first char
		char c = BinaryStdIn.readChar();	
		current.append(c);
		Integer codeword = st.get(current);

		while (!BinaryStdIn.isEmpty()) {

			codeword = st.get(current);
			c = BinaryStdIn.readChar();
			current.append(c);

			if (!st.contains(current)) {

				// write code to comp file
				BinaryStdOut.write(codeword, W);	

				if (W < MAX_W && code >= L) {

					W++;
					L <<= 1;
					// increase code width

				} else if (reset && W >= MAX_W && code >= L) {

					// reset codebook
					W = 9;
					L = 512;
					TSTmod<Integer> temp = new TSTmod<Integer>();
					for (int i = 0; i < R; i++) {
						temp.put(new StringBuilder("" + (char) i), i);
					} st = temp;
					code = R + 1;

				}

				if (code < L) {   // Add to symbol table if not full
					st.put(current, code++);
				} 

				current = current.delete(0, current.length());
				current.append(c);

			}

		}
		codeword = st.get(current);
		BinaryStdOut.write(codeword, W);
		BinaryStdOut.write(R, W); 
		BinaryStdOut.close();
	}

    public static void expand() {
    	
        boolean reset = BinaryStdIn.readBoolean();
        String[] st = new String[L];
        int code; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (code = 0; code < R; code++) {
            st[code] = "" + (char) code;
        } st[code++] = ""; // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
        	
            if (code >= L && W < MAX_W) {
            	
            	// increase code width
                W++;
                L <<= 1;
                String[] temp = new String[L];
                for (int k = 0; k < st.length; k++) { temp[k] = st[k]; }
                st = temp;
                
            } else if (code >= L && W >= MAX_W && reset) {

            	// reset codebook
            	W = 9;
                L = 512;
                String[] temp = new String[L];
                for (int k = 0; k < R; k++) { temp[k] = "" + (char) k; }
                temp[R] = "";
                st = temp;
                code = R + 1;
                
            }
            
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) { break; }
            String s = st[codeword];
            if (code == codeword) { s = val + val.charAt(0); }  // special case hack
            if (code < L)     { st[code++] = val + s.charAt(0); }
            val = s;

        }
        
        BinaryStdOut.close();
        
    }

	public static void main(String[] args) {

		boolean r = false;
		try   { r = args[1].equals("r"); } 
		catch (Exception e) { r = false; }
		
		if      (args[0].equals("-")) compress(r);
		else if (args[0].equals("+")) expand();
		else throw new RuntimeException("Illegal command line argument");
	
	}

}
