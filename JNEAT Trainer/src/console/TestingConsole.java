 package console;

import java.util.StringTokenizer;

import jNeatCommon.IOseq;
import jneat.evolution.Organism;
import jneat.neuralNetwork.Genome;
import training.evaluators.MySupervisedEvaluator;

public class TestingConsole extends Console {

	public static void main(String[] args) {
		String nameOfExperiment ="Show me greatest";
		TestingConsole tc = new TestingConsole(nameOfExperiment, 1, false, 0);
		
		int geneNumber = 0;
		String genomeFileName = tc.winnerFolder + "\\" + nameOfExperiment + "_win " + geneNumber;
		//String genomeFileName = tc.dataFolder + "\\TestGenome.txt";
		tc.testOrganism(genomeFileName);
	}
	
	public TestingConsole(String nameOfExperiment, int maxNumberOfGenerations,
			boolean stopOnFirstGoodOrganism, double errorThreshold) {
		super(nameOfExperiment, maxNumberOfGenerations, stopOnFirstGoodOrganism,
				errorThreshold);
	}
	
	private void testOrganism(String genomeFileName){
		//Open the file with the genome data
		IOseq starterGenomeFile = new IOseq(genomeFileName);
		boolean ret = starterGenomeFile.IOseqOpenR();
		
		if (ret){
			//Create starter genome
			Genome testGenome = createGenome(starterGenomeFile);
			
			//Read training data
			double[][] inputs = readValues(inputFileNameTesting);
			double[][] outputs = readValues(outputFileNameTesting);
							
			//Create evaluator		
			MySupervisedEvaluator evaluator = new MySupervisedEvaluator(inputs, outputs, errorThreshold);
			
			//Run test
			Organism organism = new Organism(0, testGenome, 1);
			evaluator.testOrganism(organism);
			
		} else{
			System.out.println("Error during opening of " + genomeFileName);
		}		
	}
	
	private Genome createGenome (IOseq starterGenomeFile){
		String curWord;
		
		System.out.println("Read genome");
			
		//Read file
		String line = starterGenomeFile.IOseqRead();
		StringTokenizer st = new StringTokenizer(line);
			
		//Skip first word in file
		curWord = st.nextToken();
		
		//Read ID of the genome
		curWord=st.nextToken();
		int id = Integer.parseInt(curWord);
			
		//Create the genome
		System.out.println("Create genome id " + id);
		Genome startGenome = new Genome (id,starterGenomeFile);
			
		//Backup initial genome
		//Probably used for debugging
		startGenome.print_to_filename(genomeBackupFileName);
			
		return startGenome;
			
	}

}
