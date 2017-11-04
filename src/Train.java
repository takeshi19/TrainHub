/////////////////////////////////////////////////////////////////////////////
//Semester:         CS367 Fall 2017 
//PROJECT:          CS 367 Programming Assignment 2: Train Hub
//FILE:             Train.java
//
//TEAM:    p2pair 2
//Authors: (Manuel Takeshi Gomez AND Mikayla Buford)
//Author1: (Manuel Takeshi Gomez, gomez22@wisc.edu, gomez22, 001)
//Author2: (Mikayla Buford, mbuford@wisc.edu, mbuford, 001)
//
//---------------- OTHER ASSISTANCE CREDITS 
//Persons: N/A  
//
//Online sources: N/A  
////////////////////////////80 columns wide //////////////////////////////////

/**
 * This class represents a train.  It has a destination and a linked list 
 * of CargoCar objects.  It implements Iterable<CargoCar> by returning 
 * a direct access iterator to its linked list of cargo cars. 
 * 
 * Several methods, such as getDestination(), removeCargo() and getWeight(), 
 * are provided to manage a train object. 
 *
 * <p>Bugs: N/A
 *
 * @see LinkedList
 * @see CargoCar
 * @author Manuel Takeshi Gomez
 * @author Mikayla Buford
 */
public class Train implements Iterable<CargoCar> {

	private String destination;			//The destination of the entire train.
	private LinkedList<CargoCar> train; //The linked list internal data structure of CargoCars.
	
	/**
	 * Constructs Train with its destination.
	 * 
	 * @param dest train destination
	 */
	public Train(String dest){
		this.destination = dest;
		train = new LinkedList<CargoCar>();
	}
	
	/**
	 * Get the destination of this train.
	 * 
	 * @return train destination
	 */
	public String getDestination(){
		return this.destination;
	}
	
	/**
	 * Set a new destination for this train.
	 * 
	 * @param newDest new train destination
	 */
	public void setDestination(String newDest){
		this.destination = newDest;
	}
	
	/**
	 * Get the total weight of a cargo in this train.
	 * 
	 * @param the name of the cargo to sum
	 * @return total weight of specified cargo in this train
	 */
	public int getWeight(String cargoName){
		LinkedListIterator<CargoCar> itr = train.iterator();
		int weight = 0; //The summed weight of each cargo car (specified by name) we are going to return.

		while (itr.hasNext()) {
			CargoCar car = itr.next();
			
			if (car.getName().equalsIgnoreCase(cargoName.trim())) {
				weight += car.getWeight(); 
			}
		}
		
		return weight;
	}
	
	/**
	 * Adds cargo to the end of the car.
	 * @param cargoCar The cargo we want to add into our Linked List.
	 */
	public void add(CargoCar cargoCar) {
		train.add(cargoCar);
	}

	/**
	 * 
	 * @param pos
	 * @param newCargo
	 */
	public void add(int pos, CargoCar newCargo) {
		train.add(pos, newCargo);		
	}
	
	/**
	 * Remove the first CargoCar from this train which has the same cargo name 
	 * with the argument. If there are multiple CargoCar objects with the same 
	 * name, remove the first one.
	 * 
	 * @param The name of the cargo that you wish to remove.
	 * @return removed CargoCar object if you successfully removed a cargo, 
	 * otherwise null
	 */
	public CargoCar removeCargo(String cargoName){
		int pos = 0;  //The position of the cargo car we want to remove.
		CargoCar car; //The car we are comparing the removable cargo car with.
		LinkedListIterator<CargoCar> itr = train.iterator(); //Iterator for the train we are searching through.
	
		//Increment position (pos) if cargo isn't found immediately; next position will be next cargo car in train.
		while (itr.hasNext()) {
			car = itr.next();
			
			if (car.getName().equalsIgnoreCase(cargoName.trim())) {
				car = train.remove(pos); 
				return car;
			}
			else {
				pos++; 
			}
		}
		
		return null; //Return null if CargoCar was never matched by cargo name and never removed. 
	}
	
	/**
	 * @return an instance of the LinkedListIterator.
	 */
	public LinkedListIterator<CargoCar> iterator() {
		return train.iterator();
	}

	/**
	 * Returns the number of cargo cars on this train.  
	 * 
	 * CAUTION: the number of actual cars on a train can be changed external
	 * to the Train type.  Make sure this returns a current count of the 
	 * cargo cars on this train.  Tip: call a LinkedList method from here
	 * and make sure that the LinkedList method iterates to count cars.
	 * 
	 * @return the number of cargo cars on this train.
	 */
	public int numCargoCars() {
		return train.size();
	}

	/**
	 * Returns a reference to the header node from the linked list of
	 * CargoCar nodes.
	 * 
	 * CAUTION: Returning this node allows other
	 * code to change the contents of this train without
	 * calling train methods.  
	 * 
	 * It is being returned in this program to facilitate our testing
	 * and for moving sub-chains of nodes from one train to another.  
	 * THIS METHOD MAY ONLY BE CALLED BY moveMultipleCargoCars()
	 * of the TrainHub class.
	 * 
	 * @return the header node of the chain of nodes from the linked list.
	 */
	public Listnode<CargoCar> getHeaderNode() {
		return train.getHeaderNode();
	}

	/**
	 * Returns Train with a String format as following.
	 * <p>
	 * {ENGINE_START}{destination}{ENGINE_END}{CARGO_LINK}{cargo}:{weight}{CARGO_LINK}{cargo}:{weight}...
	 * <p>
	 * By default, {ENGINE_START} = ( , {ENGINE_END} = ) and {CARGO_LINK} = -> (defined in {@link Config}).
	 * So if you did not modify Config class, it will generate a String with following format.
	 * <p>
	 * ({destination})->{cargo}:{weight}->{cargo}:{weight}...
	 * 
	 * DO NOT EDIT THIS METHOD
	 * 
	 * @return train as a string format
	 */
	@Override
	public String toString(){
		String builtStr = "";
		
		builtStr += Config.ENGINE_START+this.destination+ Config.ENGINE_END;

		LinkedListIterator<CargoCar> itr = train.iterator();
		while(itr.hasNext()){
			CargoCar item = itr.next();
			builtStr += Config.CARGO_LINK + item.getName() +":" + item.getWeight();
		}
		
		return builtStr;
	}
}
