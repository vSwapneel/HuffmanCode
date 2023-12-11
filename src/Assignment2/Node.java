package Assignment2;

// class for nodes of Huffman tree

public class Node {

	Node left;
	Node right;
	String c;
	int freq;
	public Node() {
		this.left = null;
		this.right = null;
	}
	
	public int getFreq() {
        return freq;
    }

}
