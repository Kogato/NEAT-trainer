package console;

import jNeatCommon.IOseq;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.StringTokenizer;

import training.Trainer;
import training.evaluators.MySupervisedEvaluator;
import training.evaluators.XOREvaluator;

public class TrainingConsole extends Console {

	public TrainingConsole(String nameOfExperiment, int maxNumberOfGenerations,
			boolean stopOnFirstGoodOrganism, double errorThreshold) {
		super(nameOfExperiment, maxNumberOfGenerations, stopOnFirstGoodOrganism,
				errorThreshold);
	}



	public static void main(String[] args) {
		String nameOfExperiment = "Show me greatest";
		int maxNumberOfGenerations = 500;
		boolean stopOnFirstGoodOrganism = true;
		double errorThreshold = 0.1;
		
		TrainingConsole tc = new TrainingConsole(nameOfExperiment, maxNumberOfGenerations, stopOnFirstGoodOrganism, errorThreshold);
		
		tc.train();
	}
	
	
	
	public void train(){
		//Read training data
		double[][] inputs = readValues(inputFileName);
		double[][] outputs = readValues(outputFileName);
				
		//Create evaluator		
		MySupervisedEvaluator evaluator = new MySupervisedEvaluator(inputs, outputs, errorThreshold);
				
		//Create trainer
		Trainer t = new Trainer(parameterFileName, debugParameterFileName, genomeFileName, genomeBackupFileName, lastPopulationInfoFileName, generationInfoFolder, winnerFolder, nameOfExperiment, maxNumberOfGenerations, stopOnFirstGoodOrganism,evaluator );
				
		//Train network
		t.trainNetwork();
	}
	
	        

}
