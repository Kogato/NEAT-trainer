package console;

import jNeatCommon.IOseq;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.StringTokenizer;

import training.Trainer;
import training.evaluators.MySupervisedEvaluator;
import training.evaluators.XOREvaluator;

public class TrainingConsole {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dataFolder = "C:\\Users\\Simon\\Documents\\MarioFun\\jneat\\data";
		String parameterFileName = dataFolder + "\\parameters";
		String debugParameterFileName = dataFolder + "\\parameters.imported";
		int numberOfGenerations = 1000;
		String nameOfExperiment = "XOR";
		boolean stopOnFirstGoodOrganism = true;
		String genomeBackupFileName = dataFolder + "\\starterGenome.read" ;
		String lastPopulationInfoFileName = dataFolder + "\\population.LastGeneration";
		String generationInfoFolder = dataFolder + "\\Generation Info";
		String winnerFolder = dataFolder + "\\Winners";
		
		String genomeFileName = dataFolder + "\\Training data\\" + nameOfExperiment + "\\starterGenome.txt";
		String inputFileName = dataFolder + "\\Training data\\" + nameOfExperiment + "\\inputvalues.txt";
		String outputFileName= dataFolder + "\\Training data\\" + nameOfExperiment + "\\outputvalues.txt";
		
		double[][] inputs = readValues(inputFileName);
		double[][] outputs = readValues(outputFileName);
		
		MySupervisedEvaluator evaluator = new MySupervisedEvaluator(inputs, outputs, 0.5);
		
		Trainer t = new Trainer(parameterFileName, debugParameterFileName, genomeFileName, genomeBackupFileName, lastPopulationInfoFileName, generationInfoFolder, winnerFolder, nameOfExperiment, numberOfGenerations, stopOnFirstGoodOrganism,evaluator );
		
		t.trainNetwork();
		
	}
	
	private static double[][] readValues(String filename){
		boolean ret = true; 
		 String xline;
		 IOseq xFile;
		 StringTokenizer st;
		 String s1;
		 double[][] result = null;
		 
		 
		 ArrayList<ArrayList<Double>> values = new ArrayList<>();
		 
		 	  
		 xFile = new IOseq(filename);
		 ret = xFile.IOseqOpenR();
		 if (ret) {
			try {
				xline = xFile.IOseqRead();
				int lineNumber = 0;
				while ( xline != "EOF"){
					ArrayList<Double> lineValues = new ArrayList<>();
					values.add(lineValues); 
					st = new StringTokenizer(xline);					
					while (st.hasMoreTokens()){
						s1 = st.nextToken();
						double value = Double.parseDouble(s1);
						values.get(lineNumber).add(value);					
					}
					xline = xFile.IOseqRead();
					lineNumber++;
				}
			} 
				catch (Throwable e) {
				  System.err.println(e);
			   }
		 
			xFile.IOseqCloseR();
			result = new double[values.size()][values.get(0).size()];
			for (int i = 0; i < values.size(); i++){
				for (int j = 0; j < values.get(0).size(); j++){
					result[i][j] = values.get(i).get(j);
				}
			}
		 
		 } else{
			System.err.print("\n : error during open " + filename);
		 }
		 
		 return result; 
	  }                    

}
