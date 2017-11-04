import java.io.File;
import java.util.regex.*;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

/////////////////////////////////////////////////////////////////////////////
//Semester:         CS367 Fall 2017 
//PROJECT:          CS 367 Programming Assignment 2: Train Hub
//FILE:             TrainGenerator.java
//
//TEAM:    p2pair 2
//Authors: (Manuel Takeshi Gomez AND Mikayla Buford)
//Author1: (Manuel Takeshi Gomez, gomez22@wisc.edu, gomez22, 001)
//Author2: (Mikayla Buford, mbuford@wisc.edu, mbuford, 001)
//
//---------------- OTHER ASSISTANCE CREDITS----------------------------------- 
//Persons: N/A  
//
//Online sources: https://stackoverflow.com/questions/7378451/java-regex-
//match-count
// 
//Advice from StackOverflow on how to look for a pattern of multiple regexes.
//In this case, it was used in the getIncomingTrainFromFile() method to check 
//for multiple commas per file line; needed for format checking. 
////////////////////////////80 columns wide //////////////////////////////////

/**
 * This class provide methods for generating a Train.
 * A train is either generated from a random number generator in the getIncomingTrain() method or from a text file 
 * in the getIncomingTrainFromFile(String filename) method.
 * 
 * <p>Bugs: N/A
 * 
 * @see Config
 * @author Manuel Takeshi Gomez
 * @author Mikayla Buford
 */
public class TrainGenerator {
	
	/**
	 * Get a new train generated randomly.
	 * The constant variables for determining how many cargo, 
	 * what cargo and how heavy are in {@link Config}.
	 * 
	 * @return a train generated randomly
	 */
	public static Train getIncomingTrain(){
		Train incomingTrain = new Train("TrainHub"); //The destination of all incoming trains is TrainHub.
		Random rand = new Random(System.currentTimeMillis());

		// How many freight cars
		int cartNum = rand.nextInt(Config.MAX_CART_NUM - Config.MIN_CART_NUM + 1) + Config.MIN_CART_NUM;

		for (int i = 0; i < cartNum; i++) {
			// What kind of cargo
			int loadIndex = rand.nextInt(Config.CARGO_ARRAY.length);
			String load = Config.CARGO_ARRAY[loadIndex];

			// How heavy
			int weight = rand.nextInt(Config.MAX_WEIGHT - Config.MIN_WEIGHT + 1) + Config.MIN_WEIGHT;

			// Where to
			int destIndex = rand.nextInt(Config.DEST_ARRAY.length);
			String dest = Config.DEST_ARRAY[destIndex];
			
			incomingTrain.add(new CargoCar(load, weight, dest));
		}
		
		return incomingTrain;
	}
	
	/**
	 * Get a new train generated from a file.
	 * Files for generating a train must have the format as follow
	 * <p>
	 * {destination},{cargo},{weight}<br>
	 * {destination},{cargo},{weight}<br>
	 * ...
	 * <p>
	 * where {destination} is a string among Config.DEST_ARRAY,
	 * {cargo} is a string among Config.CARGO_ARRAY,
	 * and {weight} is a string for any positive integer.
	 * <p>
	 * Ignore the line if it is not matched in this format. 
	 * See the sample in/outputs provided in the assignment description to get more details.
	 * 
	 * @param filename train input file name
	 * @return a train generated from a file
	 */
	public static Train getIncomingTrainFromFile(String filename) {
		Train fileTrain = new Train(null); //The train received from the file of the main class.
		File file = null;				   //The file object created from the String filename.
		Scanner scnr = null;			   //The scanner to read in the file object.
		String trainfromFile;			   //Each train from the file consists of a destination, cargo, and weight.
		CargoCar trainCar;				   //Each car from the train. 
		String destination;   			   //The specific destination of each car from the train.
		String cargo;					   //The specific cargo/material of each car from the train.
		int weight;						   //The weight of each car.				
		
		try {
			file = new File(filename);
			scnr = new Scanner(file);
			
			while (scnr.hasNextLine()) {
				trainfromFile = scnr.nextLine().trim(); //Each line represents a separate cargo car with specific data.

				if (!trainfromFile.contains(",") || trainfromFile.isEmpty()) {
					continue;
				}
				//Trying to find multiple commas: "xxx, xxx,,, xxx" is invalid input to check against.  
				String comma = trainfromFile;
				Pattern pattern = Pattern.compile(",");
				Matcher matcher = pattern.matcher(comma);
				int count = 0;
				
				while (matcher.find()) {
					count++;
				}
				
				if (count > 2) {
					continue;
				}
				
				String trainData[] = trainfromFile.split(","); //We can now properly split the String.
				
				if (trainData.length > 3) { //There should only be 3 items per line: destination, cargo, weight
					continue;
				}
				
				destination = trainData[0].trim(); 
				cargo = trainData[1].trim();	   
				
				try {
					weight = Integer.parseInt(trainData[2].trim()); 
				} catch(NumberFormatException e) {
					continue; //Skip the weight if it's in format of "xx xxxx x"- where x is an integer.
				}
								
				trainCar = new CargoCar(cargo, weight, destination);
				fileTrain.add(trainCar);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + filename);
		} finally {
			scnr.close();
		}
		
		return fileTrain;
	}
}
