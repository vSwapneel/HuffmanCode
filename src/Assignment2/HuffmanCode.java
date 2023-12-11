package Assignment2;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.io.FileOutputStream;
import java.io.PrintWriter;

public class HuffmanCode {
	static int sum_1 =0, countingNodes=0;
	
	static LinkedHashMap<String, String> generatedCodes = new LinkedHashMap<>();
	static String huffmanCode="", binaryCode="";
	
	static Node head = new Node();
	
	static String I_N = "Intermediate Node";
	
	public static void main(String[] args) {
        boolean isReading = false;
        StringBuilder content = new StringBuilder();
        
        // Reading the input from the text file
		try (BufferedReader reader = new BufferedReader(new FileReader("The Overlord's Thumb by Robert Silverberg.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("*** START OF THE PROJECT GUTENBERG EBOOK THE OVERLORD'S THUMB ***")) {
                    isReading = true;
                    continue;
                }
                if (line.contains("*** END OF THE PROJECT GUTENBERG EBOOK THE OVERLORD'S THUMB ***")) {
                    break;
                }
                if (isReading) {
                	content.append(line).append(" ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
		
		// Map to count frequency of each character
        HashMap<String, Integer> charFrequencyMap = new HashMap<>();
        String text = content.toString();
        for (char c : text.toCharArray()) {
            if (charFrequencyMap.containsKey(String.valueOf(c))) {
                charFrequencyMap.put(String.valueOf(c), charFrequencyMap.get(String.valueOf(c)) + 1);
            } else {
                charFrequencyMap.put(String.valueOf(c), 1);
            }
        }

        List <Node> nodeList = new ArrayList<>();
        
        // Converting the elements of the map to nodes and adding them to a list
        for (String key : charFrequencyMap.keySet()) {
        	Node n = new Node();
        	n.left = null;
        	n.right = null;
        	n.c = key;
        	n.freq = charFrequencyMap.get(key);
        	
        	nodeList.add(n);
        }
        
        // Sorting the list based on the character frequency
    	Comparator<Node> freqComparator = Comparator.comparing(Node::getFreq);
        Collections.sort(nodeList, freqComparator);
        
        System.out.println("Frequency Values :");
    	for (Node element : nodeList) {
            System.out.println("'"+element.c +"' : "+element.freq);
        }
    	
    	// Iterative logic to create a Huffman tree, with the nodes
        while(nodeList.size() >= 2) {
            Collections.sort(nodeList, freqComparator);
            
            // This will be the parent node for the two nodes with least frequency 
        	Node mergedNode = new Node();

        	// Taking the two nodes with least frequencies
        	Node secondNode = nodeList.remove(1);
        	Node firstNode = nodeList.remove(0);
        	
        	// Assigning the nodes with least frequency as child nodes
        	mergedNode.left = firstNode;
        	mergedNode.right = secondNode;
        	mergedNode.freq = firstNode.freq+secondNode.freq;
        	mergedNode.c = I_N;
        	
        	// Adding the merged node to the node list
        	nodeList.add(mergedNode);
    
        }
        
        // head stores the root node of the tree
        head = nodeList.get(0);
        
        // Tree traversal to get the generated codes for every character in the text
        treeTraversal(head, "");
        
        System.out.println();
    	
    	List<Map.Entry<String, String>> entryList = new ArrayList<>(generatedCodes.entrySet());
        
        entryList.sort((entry1, entry2)-> Integer.compare(entry1.getValue().length(), entry2.getValue().length()));
        
        generatedCodes.clear();

        // Sorting the generated codes based on bit length for each character
        for (Map.Entry<String, String> entry : entryList) {
        	generatedCodes.put(entry.getKey(), entry.getValue());
        }
        
        System.out.println("Huffman code values :");
        for (Map.Entry<String, String> entry : generatedCodes.entrySet()) {
            System.out.println("'"+entry.getKey() + "' : " + entry.getValue());
        }
        
        // To generate the Huffman code of entire text
        generateHuffmanCode(text);
        
        // To generate a binary code of the text, 7-bits for each character
        generateBinaryCode(text);
        
        String decodedText = decodeHuffmanCodeToText();
        
        // Verifying the Huffman encoding
        if(decodedText.equals(text)) {
        	System.out.println("Huffmann code worked successfully !!");
        }
        else {
        	System.out.println("Something is wrong, I can feel it");
        }
        float netCompressionRatio =  (float) binaryCode.length()/huffmanCode.length();
        
        // Finding compression ratio and compression percentage
        System.out.println();
        System.out.println("Compression ratio is : "+netCompressionRatio);
        System.out.println("Compressed data is : "+100/netCompressionRatio +"% of the original data");
        
    }
	
	public static void treeTraversal(Node root, String s) {
		if (root.left == null && root.right == null && !root.c.equals(I_N) ) {
	            sum_1+=root.freq;
                countingNodes++;
                generatedCodes.put(root.c, s);
	            return; 
	        }
		treeTraversal(root.left, s + "0"); // Visit the left subtree
		treeTraversal(root.right, s + "1"); // Visit the right subtree
    }
	
	public static void generateHuffmanCode(String text) {
		int bitLength=0;
		for (char c : text.toCharArray()) {
			huffmanCode += generatedCodes.get(String.valueOf(c));
			bitLength += generatedCodes.get(String.valueOf(c)).length();
		}
		
		System.out.println("Length of the Huffman coded text is "+bitLength+ " bits");
		
		// print output to text file
		try {
	        FileOutputStream fileOutputStream = new FileOutputStream("Huffman Output.txt");

	        PrintWriter printWriter = new PrintWriter(fileOutputStream);

	        printWriter.println(huffmanCode);

	        printWriter.close();
	        fileOutputStream.close();

	        System.out.println("Huffman code data has been written to the file.");
		} catch(Exception e) {
            e.printStackTrace();
		}
	}
	
	public static void generateBinaryCode(String text) {
		int bitLength=0;
		for (char c : text.toCharArray()) {
			String binary = Integer.toBinaryString((int)c);
			if(binary.length() < 7) {
				String padding = "0".repeat(7 - binary.length());
	            binary = padding + binary;
			}
			binaryCode += binary;
			bitLength += binary.length();
		}
		
		System.out.println("Length of the binary coded text is "+bitLength+ " bits");
		
		// print output to text file
		try {
	        FileOutputStream fileOutputStream = new FileOutputStream("Binary Output.txt");

	        PrintWriter printWriter = new PrintWriter(fileOutputStream);

	        printWriter.println(binaryCode);

	        printWriter.close();
	        fileOutputStream.close();

	        System.out.println("Binary code data has been written to the file.");
		} catch(Exception e) {
            e.printStackTrace();
		}
	}
	
	public static String decodeHuffmanCodeToText() {
		String textValue="";
		Node n1 = new Node();
		n1= head;
		for (char c : huffmanCode.toCharArray()) {
			if(c == '0') {
				n1 = n1.left;
			}
			else {
				n1 = n1.right;
			}
			if (!n1.c.equals(I_N)) {
				textValue+=n1.c;
				n1= head;
			}
		}
//		System.out.println(textValue);
		return textValue;
	}
}
