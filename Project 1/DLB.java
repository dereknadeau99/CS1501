
public class DLB implements DictInterface {

	Node head = new Node(' ');
	
	public DLB() {}
	
	public DLB(String s) {
		this.add(s);
	}
	
	public boolean add(String s) {
		if (head.c == ' ') { head.c = s.charAt(0); } 
		return add_R(s, 0, head);
	}
	
	private boolean add_R(String s, int i, Node curr) {
		
//		System.out.print(curr.c);
		
		char C = s.charAt(i);
		
		if (curr.c != C) {
			
			if (curr.sibling == null) {
				curr.sibling = new Node(C);
			}
			return add_R(s, i, curr.sibling);
			
		} else {
			
			if (curr.child == null) {
				try {
					curr.child = new Node(s.charAt(i+1));
				} catch (StringIndexOutOfBoundsException e) {
					curr.word = true;
//					System.out.println();
					return true;
				}
			}
			return add_R(s, i+1, curr.child);
			
		}
		
	}

	@Override
	public int searchPrefix(StringBuilder s) {
		return searchPrefix_R(s, head, 0);
	}
	
	public int searchPrefix_R(StringBuilder s, Node curr, int i) {
		
		int PREFIX = 1, WORD = 2, R = 0;
		
		if (s.charAt(i) == curr.c) {
			if (i+1 == s.length()) {
				if (curr.word) {
					R += WORD;
				}
				if (curr.child != null) {
					R += PREFIX;
				}
				return R;
			} else {
				if (curr.child != null) {
					return searchPrefix_R(s, curr.child, i+1);
				} else {
					return 0;
				}
			}
		} else {
			if (curr.sibling == null) {
				return 0;
			} else {
				return searchPrefix_R(s, curr.sibling, i);
			}
		}
		
	}

	@Override
	public int searchPrefix(StringBuilder s, int start, int end) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private class Node {
		
		char c;
		Node sibling;
		Node child;
		boolean word = false;
		
		Node(char c) {
			this.c = c;
		}
		
	}	

}
