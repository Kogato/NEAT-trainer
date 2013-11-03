 package training.evaluators;

import java.util.Vector;

import jneat.evolution.Organism;
import jneat.neuralNetwork.NNode;
import jneat.neuralNetwork.Network;


public class MySupervisedEvaluator implements iEvaluator {
	double[][] trainingSets;
	double[][] expectedOutputs;
	double threshold;
	
	public MySupervisedEvaluator(double[][] trainingSets, double[][] expectedOutputs, double threshold){
		this.trainingSets=trainingSets;
		this.expectedOutputs = expectedOutputs;
		this.threshold = threshold;
	}
	
	/**
	 * Evaluates an organism and returns true if the organism has found and answer
	 * @param organism
	 * @return
	 */
	 public boolean evaluate(Organism organism){
		 boolean success = false;
		 double errorsum = 0.0;
		 
		 //Import objects from organism
		 Network network = organism.net;
		 int networkDepth = network.max_depth(); //The max depth of the network to be activated
		 Vector<NNode> outputNodes = network.getOutputs();
		 int numberOfOutputNodes = outputNodes.size();
		 
		 //Read input about training
		 int numberOfTrainingSets = trainingSets.length;
		 double[][] actualOutputs = new double[numberOfTrainingSets][numberOfOutputNodes]; //Stores the output from the training sets
		 	  
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
			 
			organism.setFitness(Math.pow((numberOfOutputNodes - errorsum), 2));
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

}
