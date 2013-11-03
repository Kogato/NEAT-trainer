 package training.evaluators;

import java.util.Vector;

import jneat.evolution.Organism;
import jneat.neuralNetwork.NNode;
import jneat.neuralNetwork.Network;


public class MySupervisedEvaluator implements iEvaluator {
	//Training parameters
	private double[][] trainingSets;
	private double[][] expectedOutputs;
	private double threshold;
	private int numberOfTrainingSets;
	private double[][] actualOutputs;
	
	//Organism
	private Network network;
	private int networkDepth;
	private Vector<NNode> outputNodes;
	private int numberOfOutputNodes;
	
	public MySupervisedEvaluator(double[][] trainingSets, double[][] expectedOutputs, double threshold){
		this.trainingSets=trainingSets;
		this.expectedOutputs = expectedOutputs;
		this.threshold = threshold;
	}
	
	public void testOrganism(Organism organism){
		
		boolean success = runSimulation(organism);
		
		if (success){
			for (int i = 0; i < numberOfTrainingSets; i++){
				System.out.println();
				System.out.println("Training set " + i);
				
				//Print inputs
				double[] in = trainingSets[i];
				System.out.print("Input:           ");
				printValues(in);
				System.out.println();
				
				//Print expected outputs								
				double[] expOut = expectedOutputs[i];
				System.out.print("Expected output: ");
				printValues(expOut);
				System.out.println();
				
				//Print actual outputs				
				double[] actOut = actualOutputs[i];
				System.out.print("Actual output:   ");
				printValues(actOut);
				System.out.println();				
			}
		}
	}
	
	private void printValues(double[] values){
		for (double d : values){
			System.out.format("%.5f; ", d);
		}
	}
	
	/**
	 * Evaluates an organism and returns true if the organism has found and answer
	 * @param organism
	 * @return
	 */
	 public boolean evaluate(Organism organism){
		 boolean success = false;
		 double errorsum = 0.0;
		 
		 success = runSimulation(organism);
	  
		 //Calculate errors
		 double[][] errors = new double[numberOfTrainingSets][numberOfOutputNodes];
		 if (success) {
			 for (int trainingSet = 0; trainingSet < numberOfTrainingSets; trainingSet++){
				 for (int i = 0; i < outputNodes.size(); i++){
					 double expectedOut = (double) expectedOutputs[trainingSet][i];
					 double actualOut = (double) actualOutputs[trainingSet][i];
					 errors[trainingSet][i] = Math.abs(expectedOut - actualOut);
					 errorsum += (double) Math.abs(expectedOut - actualOut);
				 }
			 }
			 
			organism.setFitness(Math.pow((numberOfTrainingSets - errorsum), 2));
			organism.setError(errorsum);
			
		 } else {
			errorsum = 999.0;
			organism.setFitness(0.001);
			organism.setError(errorsum);
		 }
		 
		 //Test if a winner is found
		 for (int trainingSet = 0; trainingSet < numberOfTrainingSets; trainingSet++){
			 for (int i = 0; i < outputNodes.size(); i++){
				 if (errors[trainingSet][i] > threshold){
					organism.setWinner(false);
					return false;
				 }				 
			 }
		 }
		 organism.setWinner(true);
		 return true;	  
	  }
	 
	 private boolean propagateSignal(Network net, int net_depth, double[] inputValues){
		boolean success = false;
		 // first activation from sensor to first level of neurons
		 net.load_sensors(inputValues);
		 success = net.activate();
	 
		 // next activation until last level is reached !
		 // use depth to ensure relaxation
	 
		for (int relax = 0; relax <= net_depth; relax++){
			success = net.activate();
		}
		
		return success;
	 }
	 
	 private void readOrganism(Organism organism){
		 network = organism.net;
		 networkDepth = network.max_depth(); //The max depth of the network to be activated
		 outputNodes = network.getOutputs();
		 numberOfOutputNodes = outputNodes.size();
	 }
	 
	 private boolean runSimulation(Organism organism){
		 boolean success = false;
		 		 
		 //Import objects from organism
		 readOrganism(organism);
		 
		 //Read input about training
		 numberOfTrainingSets = trainingSets.length;
		 actualOutputs = new double[numberOfTrainingSets][numberOfOutputNodes]; //Stores the output from the training sets
		 	  
		 // for each training set, propagate signal .... and compute results
		 for (int trainingSet = 0; trainingSet < numberOfTrainingSets; trainingSet++){
			 //propagate input signal forward
			 success = propagateSignal(network, networkDepth, trainingSets[trainingSet] );
			 		 
			//Read the output value			
			for (int i = 0; i < outputNodes.size(); i++){
				actualOutputs[trainingSet][i] = outputNodes.get(i).getActivation();
			}
			network.flush();
		 }	
		 
		 return success;
	 }

}
