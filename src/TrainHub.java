/////////////////////////////////////////////////////////////////////////////
// Semester:         CS367 Fall 2017 
// PROJECT:          CS 367 Programming Assignment 2: Train Hub
// FILE:             TrainHub.java
//
// TEAM:    p2pair 2
// Authors: (Manuel Takeshi Gomez AND Mikayla Buford)
// Author1: (Manuel Takeshi Gomez, gomez22@wisc.edu, gomez22, 001)
// Author2: (Mikayla Buford, mbuford@wisc.edu, mbuford, 001)
// 
//---------------- OTHER ASSISTANCE CREDITS 
//Persons: N/A 
// 
//Online sources: https://stackoverflow.com/questions/6203411/comparing-strings
//-by-their-alphabetical-order
// 
//This advice from StackOverflow helped us in alphabetically sorting the newly
//added cargo cars in the processIncomingTrain() method. 
//////////////////////////// 80 columns wide //////////////////////////////////

/**
 * This class represents a train hub and provides the common operations
 * needed for managing the incoming and outgoing trains.
 * 
 * It has a LinkedList of Train as a member variable and manages it.
 * 
 * <p>Bugs: N/A
 * 
 * @see LinkedList
 * @see Train
 * @see Config
 * 
 * @author Manuel Takeshi Gomez
 * @author Mikayla Buford
 */
public class TrainHub {
	
	/** The internal data structure of a hub is a linked list of Trains */
	private LinkedList<Train> trains;
	
	/**
	 * Constructs and initializes TrainHub object
	 */
	public TrainHub(){
		this.trains = new LinkedList<Train>();
	}
	
	/**
	 * This method processes the incoming train.
	 * It iterates through each of the cargo car of the incoming train.
	 * If there is an outgoing train in the train list going to the 
	 * destination city of the cargo car, then it removes the cargo car 
	 * from the incoming train and adds the cargo car at the correct location 
	 * in the outgoing train.  The correct location is to become the first
	 * of the matching cargo cars, with the cargo cars in alphabetical order 
	 * by their cargo name.
	 * 
	 * If there is no train going to the destination city, 
	 * it creates a new train and adds the cargo to this train.
	 * 
	 * @param train Incoming train (list or cargo cars)
	 */
	public void processIncomingTrain(Train train){
		int posOutgoing = 0;			   //Position based-index of each cargo in the outgoing train.
		int alpha = 0;					   //Represents lexicographical order of the cargo cars from outgoing train.
		CargoCar incomingCar;			   //The car from incoming train we will add to outgoing train.
		CargoCar outgoingCar;			   //The cars from outgoing train we have to iterate through during insertion.
		Train incoming = train;            //The incoming train passed in from the TrainHub (going to train hub).
		Train outgoing = new Train(null);  //Outgoing train we are adding to.
		LinkedListIterator<CargoCar> itr = incoming.iterator(); 
		
		while (itr.hasNext()) {
			incomingCar = itr.next();
			outgoing = findTrain(incomingCar.getDestination().trim()); 
			
			if (outgoing != null) { //If there is a match between destinations, then proceed to add cars.				
				incoming.removeCargo(incomingCar.getName()); //Remove cargo car from incoming train.
				LinkedListIterator<CargoCar> itr2 = outgoing.iterator();
				
				posOutgoing = 0;
				while (itr2.hasNext()) {
					outgoingCar = itr2.next();
					alpha = incomingCar.getName().compareToIgnoreCase(outgoingCar.getName()); 
					
					/*If incomingCar lexicographically precedes/is equal to an outgoing car, 
					 *add incoming car at its position.*/
					if (alpha <= 0) { 
						outgoing.add(posOutgoing, incomingCar);
						break;		//Only add in the incomingCar once.
					}
					//If adding to position at the end of the outgoing car, use regular add method.
					else if (posOutgoing == outgoing.numCargoCars()-1) {
						outgoing.add(incomingCar);
						break;
					}
					//Lexicographically surpasses outgoing car, then go to the next one in the outgoing train. 
					else {
						posOutgoing++;
					}
				}
			}
			else { //If the no outgoing train from TrainHub is going to incomingCar destination, then make one.
				Train unmatchedTrain = new Train(null); //Separate back-up train going to exclusive destination.
				incoming.removeCargo(incomingCar.getName()); 
				unmatchedTrain.add(incomingCar);
				unmatchedTrain.setDestination(incomingCar.getDestination().trim());
				trains.add(unmatchedTrain);
				continue; 
			}
		}
	}
	
	/**
	 * This method tries to find the train in the list of trains, departing to the given destination city.
	 * 
	 * @param dest Destination city for which train has to be found.
	 * @return  Pointer to the train if the train going to the given destination exists. Otherwise returns null.
	 */
	public Train findTrain(String dest){
		Train trainLocator = null;  //The outgoing train we are checking for destination match with the incoming train. 
		
		if (dest == null) {
			throw new IllegalArgumentException();
		}
		else {
			for (Train selected : this.trains) {
				
				if (selected.getDestination().equalsIgnoreCase(dest)) {
					trainLocator = selected; //trainLocator now references/points to the train with matched destination.
					break;
				}
			}
		}
		
		return trainLocator;
	}
	
	/**
	 * This method removes the first cargo car going to the given 
	 * destination city and carrying the given cargo.
	 * 
	 * @param dest Destination city
	 * @param name Cargo name
	 * @return If there is a train going to the the given destination and is carrying the given cargo, 
	 * it returns the cargo car. Else it returns null.
	 */
	public CargoCar removeCargo(String dest, String name){
		Train outgoing = findTrain(dest); //The outgoing train of destination dest.
		CargoCar outCar = null;			  //A cargo car from the incoming train.
		int findNameTries = 1;			  //If # of tries to find name is greater than outgoing train size, return null.
				
		if (outgoing != null) {
			LinkedListIterator<CargoCar> itr = outgoing.iterator();  //Iterator of outgoing train cars.
			
			while (itr.hasNext()) { //Cannot remove cargo if train going to destination does not exist.
				outCar = itr.next();
			
				if (outCar.getName().equalsIgnoreCase(name)) {
					outgoing.removeCargo(name);
					return outCar;
				}
				else {
					findNameTries++;
				}
			}
			//If user enters a valid destination, but with invalid cargo, then this method returns null.
			if (!(findNameTries < outgoing.numCargoCars())) {
				outCar = null;
			}
		}
		
		return outCar; //If no match between the name and destination, return null.
	}
	
	/**
	 * This method iterates through all the trains in the list 
	 * and finds the sum of weights of given cargo in all trains.
	 * 
	 * @param name Name of the cargo
	 * @return Total weight of given cargo in all departing trains.
	 */
	public int getWeight(String name){
		int totalWeight = 0; //Total weight of a given cargo in all departing trains.
		CargoCar car;		 //The cargo cars of all trains in the Train Hub.
		
		if (!trains.isEmpty()) {
			
			for (Train trns : trains) {
				LinkedListIterator<CargoCar> itr = trns.iterator();	 
				
				while (itr.hasNext()) {
					car = itr.next();
					
					if (car.getName().equalsIgnoreCase(name.trim())) {
						totalWeight += car.getWeight();
					}
				}
			}
		}
		
		return totalWeight;
	}
	
	/**
	 * This method is used to depart the train to the given destination. 
	 * To depart the train, one needs to delete the train from the list. 
	 * 
	 * @param dest Destination city for which corresponding train has to be departed/deleted.
	 * @return True if train to the given destination city exists. If not, then return false. 
	 */
	public boolean departTrain(String dest){
		boolean depart = false;		      //This boolean variable returns false iff the destination city does not exist.
		int posOfTrain = 0;				  //The position of the train in hub depending on the # of iterations.
		Train outTrain = new Train(null); //Each outgoing train housed in the TrainHub.
		Train trn = findTrain(dest);	  //Checking if the parameter destination exists between our outgoing trains.
		LinkedListIterator<Train> itr = trains.iterator();
		
	    while (itr.hasNext() && trn != null && depart == false) {
	    	outTrain = itr.next();

	    	if (outTrain.getDestination().equalsIgnoreCase(dest)) {
	    		trains.remove(posOfTrain);
	    		depart = true;
	    	}
	    	else { //Search through next trains if not destination match was found to depart the current outgoing train.
	    		posOfTrain++;
	    	}
	    }
	    
	    return depart;
	}
	
	/**
	 * This method deletes all the trains.
	 * 
	 * @return True if there was at least one train to delete. False if there was no train to delete.
	 */
	public boolean departAllTrains(){
		boolean allGone = false; 	//Returns true iff all of the trains were removed from the TrainHub.
		LinkedListIterator<Train> itr = trains.iterator(); //Iterating through all of the trains in the hub.
		Train outTrain =  new Train(null);				   //Each train we are iterating over in the hub.
				
		if (!trains.isEmpty()) {
			allGone = true;
			
			while (itr.hasNext()) {
	    		outTrain = itr.next(); //Return Train at position pos.
	    		trains.remove(0);      //Remove each train after each shift to the left in the list.
			}
		}
		
		return allGone;
	}

	/**
	 * Display the specific train for a destination.
	 * 
	 * @param dest Destination city for the train then to be displayed.
	 * @return True if train to the given destination city exists. If not, then return false.
	 */
	public boolean displayTrain(String dest){
		boolean goingTo = false; 	 //Breaks search for train with destination if it returns true.
		
		if (dest == null) {
			throw new IllegalArgumentException();
		}
		
		Train trn = findTrain(dest.trim()); //Any train from the trains list we are iterating over. 

		if (trn != null) {
			System.out.println(trn.toString()); //Displaying the train destination.
			goingTo = true;
		}
		
		return goingTo;
	}

	/**
	 * This method is used to display all the departing trains in the train hub.
	 * Train should be displayed in the specified format. 
	 * 
	 * @return True if there is at least one train to print. False if there is no train to print.
	 */
	public boolean displayAllTrains(){
		boolean anyGoingTo = false; //Returns true iff there are is at least one train in the hub.
		LinkedListIterator<Train> itr = trains.iterator();
		Train trn = new Train(null); //Any train from the hub we are iterating over.
		
		if (!trains.isEmpty()) {
			anyGoingTo = true;
			
			while (itr.hasNext()) { 
				trn = itr.next();
				System.out.println(trn.toString()); //Displaying the train destination.
			}
		}
	
		return anyGoingTo;
	}
	
	/**
	 * Move all cargo cars that match the cargo name from a 
	 * source (src) train to a destination (dst) train.  
	 * 
	 * The matching cargo cars are added before the first cargo car
	 * with a name match in the destination train.
	 * 
	 * All matching cargo is to be moved as one chain of nodes and inserted
	 * into the destination train's chain of nodes before the first cargo matched node.
	 * 
	 * PRECONDITION: there is a source train and a destination train,
	 * and the source train of nodes has at least one node with
	 * matching cargo.  We will not test other conditions.
	 * 
	 * NOTE: This method requires direct access to the chain of nodes of a
	 * Train object.  Therefore, the Train class has a method in addition to 
	 * ListADT methods so that you can get direct access to header node 
	 * of the train's chain of nodes.   
	 *
	 * @param src a reference to a Train that contains at least one node with cargo.  
	 * 
	 * @param dst a reference to an existing Train.  The destination is the 
	 * train that will have the cargo added to it.  If the destination chain 
	 * does not have any matching cargo, add the chain at its correct location 
	 * alphabetically.  Otherwise, add the chain of cargo nodes before the
	 * first matching cargo node.
	 * 
	 * @param cargoName The name of cargo to be moved from one chain to another.
	 */
	public static void moveMultipleCargoCars(Train srcTrain, Train dstTrain, String cargoName) {		
		// get references to train header nodes
		// get references to train header nodes
		Listnode<CargoCar> srcHeader, dstHeader, prev, curr;
		srcHeader = srcTrain.getHeaderNode();
		dstHeader = dstTrain.getHeaderNode();
		
		Listnode<CargoCar> first_prev = null, first = null, last = null;
		boolean hasFound = false;
		int pos = 0;
		
		// 1. Find references to the node BEFORE the first matching cargo node
		//    and a reference to the last node with matching cargo.
		prev = srcHeader;
		while (prev.getNext() != null) {
			if (prev.getNext().getData().getName().equalsIgnoreCase(cargoName.trim())) {
				break;
			}
			else {
				prev = prev.getNext();
			}
		}
		
		first_prev = prev;
		curr = prev;
		prev = prev.getNext();

		while (curr.getNext() != null) {
			if (!curr.getNext().getData().getName().equalsIgnoreCase(cargoName.trim())) {
				break;
			}
			else {
				curr = curr.getNext();
			}
		}
		
		last = curr;
	
		// 2. Remove from matching chain of nodes from src Train
		//    by linking node before match to node after matching chain
		first_prev.setNext(last.getNext());
		
		// 3-1. Find reference to first matching cargo in dst Train
		first = dstHeader.getNext();
		
		while (first.getNext() != null) {
			if (first.getData().getName().equalsIgnoreCase(cargoName.trim())) {
				hasFound = true;
				break;
			}
			else {
				first = first.getNext();
				pos++;
			}
		}
		// 3-2. If found, insert them before cargo found in dst
		if (hasFound) {
			while (prev != last.getNext()) {
				dstTrain.add(pos, prev.getData());
				prev = prev.getNext();
				pos++;
			}
		}
		// 3-3. If no matching cargo, add them at the end of train
		//Must add in alphabetical order.
		else {
			first = dstHeader.getNext();
			pos = 0;
			while (first != null) {
				int value = prev.getData().getName().compareTo(first.getData().getName());
				
				//If the cargo is alphabetically before a cargo car in dst train, then add at car's position.
				if (value <= 0) {
					while (prev != last.getNext()) {
						dstTrain.add(pos, prev.getData());
						prev = prev.getNext();
						pos++;
					}
					break;
				}
				//Add cargo to end of dst if it's alphabetically last to all cargo cars in that train.
				else if (pos == dstTrain.numCargoCars() - 1) {
					while (prev != last.getNext()) {
						dstTrain.add(prev.getData());
						prev = prev.getNext();
						pos++;
					}
					break;
				}
				else {
					first = first.getNext();
					pos++;
				}
			}
		}
	}
}