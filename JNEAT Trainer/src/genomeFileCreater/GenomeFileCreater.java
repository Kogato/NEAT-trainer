package genomeFileCreater;

import jNeatCommon.IOseq;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;

public class GenomeFileCreater {

	public static void main(String[] args) {
		String fileName = new File("").getAbsolutePath() + "\\data\\Tester.txt";
		int inputNodes = 2;
		int outputNodes = 1;
		
		GenomeFileCreater gfc = new GenomeFileCreater();
		gfc.createGenomeFile(fileName, inputNodes, outputNodes);

	}
	
	private void createGenomeFile(String fileName, int inputNodes, int outputNodes){
		String s = "";
		String delimiter ="\n";
		
		//Write start of file
		s = "genomestart 1 " + delimiter;
		s = s + "trait 1 0.1 0 0 0 0 0 0 0 " + delimiter;
		
		//Add bias node
		int nodesAdded = 1;
		s += "node " + nodesAdded + " 0 1 3 " + delimiter;
		
		for (int i = 1; i <= inputNodes; i++){
			nodesAdded++;
			s += "node " + nodesAdded + " 0 1 1 " + delimiter;
		}
		
		for (int i = 1; i <= outputNodes; i++){
			nodesAdded++;
			s += "node " + nodesAdded + " 0 0 2 " + delimiter;
		}
		
		//Add genes
		int genesAdded = 1;
		for (int out = 1; out <= outputNodes; out++){
			for (int in = 1; in <= inputNodes + 1; in++){
				s += "gene " + genesAdded + " " + in + " " + out + " 0 0 " + genesAdded + " 0 1 " + delimiter;
				genesAdded++;
			}
		}
		
		//Add close line
		s+= "genomeend 1";
		
		IOseq writer = new IOseq(fileName);
		writer.IOseqOpenW(false);
		for (String line : s.split("\n")){
	    	writer.IOseqWrite(line);
	    }
		writer.IOseqCloseW();
		
		
	}
	
	
}
