
/////////////////////////////////////////////////////////////////////////////
//Semester:         CS367 Fall 2017 
//PROJECT:          CS 367 Programming Assignment 2: Train Hub
//FILE:             LinkedList.java
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
 * An Iterable list that is implemented using a singly-linked chain of nodes
 * with a header node and without a tail reference.
 * 
 * The "header node" is a node without a data reference that will 
 * reference the first node with data once data has been added to list.
 * 
 * The iterator returned is a LinkedListIterator constructed by passing 
 * the first node with data.
 * 
 * CAUTION: the chain of nodes in this class can be changed without
 * calling the add and remove methods in this class.  So, the size()
 * method must be implemented by counting nodes.  This counting must
 * occur each time the size method is called.  DO NOT USE a numItems field.
 * 
 * <p>Bugs: N/A
 *  
 * @author Manuel Takeshi Gomez
 * @author Mikayla Buford
 */
public class LinkedList<E> implements ListADT<E> {
	
	private Listnode<E> head;
	
	/**
	 * Creates a new Listnode object of this class that is to be the header node of all nodes in the Linked List.
	 */
	public LinkedList() {  
		head = new Listnode<E>(null); //The header node (no data/null) used as a pointer to our internal data structure.
	}
	
	/**
	 * Must return a reference to a LinkedListIterator for this list.
	 */
	public LinkedListIterator<E> iterator() {
		return new LinkedListIterator<E>(head.getNext()); //Pass in the first node of the list that head points to.
	}
	
	/**
	 * If the list is not empty, given data for the new node, then add a node to the end of the Linked List.
	 * @param data the Object data that is sent to the data field of the Listnode.
	 */
	public void add(E data) {
		if (data == null) {	//Null references to node data is not allowed. 
			throw new IllegalArgumentException();
		}
		
		Listnode<E> newnode = new Listnode<E>(data); //Create a new node out of the data parameter.
		Listnode<E> curr = head; 	//curr points to header node in order to traverse through internal data structure.
		
		while (curr.getNext() != null) { 
			curr = curr.getNext();		 
		}
		curr.setNext(newnode);     //Add the new node to the end of the list.
	}
	
	/**
	 * Insert the node in the given position, pos, and shift the other nodes over to the right. 
	 * @param data
	 * @param pos
	 */
	public void add(int pos, E data) {
		if (pos < 0 || pos > size()) { 	//Check for a bad position.
			throw new IndexOutOfBoundsException();
		}
		if (pos == size()) {
			add(data); 					//Call on first add method if the list is empty.
		}
		else {							//Add at a certain position (pos) if there are multiple nodes in the list.
			Listnode<E> n = head;
			
			for (int k = 0; k < pos; k++) {
				n = n.getNext();
			}
			n.setNext(new Listnode<E>(data, n.getNext()));
		}
	}

	/**
	 * Returns true iff item is in the List (i.e., there is an item x in the List 
	 * such that x.equals(item))
	 * 
	 * @param item the item to check
	 * @return true if item is in the List, false otherwise
	 */
	public boolean contains(E data) {
		LinkedListIterator<E> itr = this.iterator();
		E nodeData; 			//Data received from the node's we are iterating over.
		boolean isIn = false;   //Returns true iff there is a match between the data parameter and the nodeData. 
		
		if (this.isEmpty()) {
			return isIn;
		}
		if (data == null) {
			throw new IllegalArgumentException();
		}
		
		while (itr.hasNext() && isIn == false) { 
			nodeData = itr.next();
		
			if (nodeData.equals(data)) {
				isIn = true;
			}
		}
		return isIn;
	}
	
	/** 
	 * Returns a reference to the header node for this linked list.
	 * The header node is the first node in the chain and it does not 
	 * contain a data reference.  It does contain a reference to the 
	 * first node with data (next node in the chain). That node will exist 
	 * and contain a data reference if any data has been added to the list.
	 * 
	 * NOTE: Typically, a LinkedList would not provide direct access
	 * to the headerNode in this way to classes that use the linked list.  
	 * We are providing direct access to support testing and to 
	 * allow multiple nodes to be moved as a chain.
	 * 
	 * @return a reference to the header node of this list. 0
	 */
	public Listnode<E> getHeaderNode() {
		return head;
	}
	
	/**
	 * Returns the item at position pos in the List.
	 * 
	 * @param pos the position of the item to return
	 * @return the item at position pos
	 * @throws IndexOutOfBoundsException if pos is less than 0 or greater than
	 * or equal to size()
	 */
	public E get (int pos) {
		if (pos < 0 || pos >= this.size()) {	
			throw new IndexOutOfBoundsException();
		}
		if (this.isEmpty()) {
			throw new NullPointerException();
		}
		
		Listnode<E> curr = head.getNext(); //Get the first data node (after head), and starting traversing from there.
		
		for (int k = 0; k < pos; k++) {
			curr = curr.getNext();
		}
		return curr.getData();
	}
	
	/**
	 * Returns true iff the List does not have any data items.
	 * 
	 * @return true if the List is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return head.getNext() == null; //Checks if there are no nodes after the header node, meaning the list is empty.
	}
	
	/**
	 * Returns the number of items in the List.
	 * 
	 * @return the number of items in the List
	 */
	public int size() {
		int numNodes = 0;	//The exact number of nodes in the Linked List we are iterating through.
		LinkedListIterator<E> itr = this.iterator(); //An iterator for the Linked List internal data structure. 
		E temp = null; 		//Used to traverse through the Listnode objects
		
		if (this.isEmpty()) {
			return numNodes;
		}
		while (itr.hasNext()) {
			temp = itr.next(); 
			numNodes++;
		}
		
		return numNodes;
	}

	/**
	 * Removes and returns the item at position pos in the List, moving the items 
	 * originally in positions pos+1 through size() - 1 one place to the left to 
	 * fill in the gap.
	 * 
	 * @param pos the position at which to remove the item
	 * @return the item at position pos
	 * @throws IndexOutOfBoundsException if pos is less than 0 or greater than
	 * or equal to size()
	 */
	@Override
	public E remove(int pos) {
		E removed = null; 	//The item to remove and then return data from.
		
		if (pos < 0 || pos >= this.size()) {
			throw new IndexOutOfBoundsException();
		}
		if (this.isEmpty()) {
			throw new NullPointerException();
		}
		
		//If user wants to remove first node, then we can still skip/remove this node starting from head.
		if (pos == 0) {
			removed = head.getNext().getData();
			head.setNext(head.getNext().getNext());
		}
		else {
			Listnode<E> curr = head.getNext();
			
			for (int k = 0; k < pos-1; k++) {
				curr = curr.getNext();
			}
			removed = curr.getNext().getData();
			curr.setNext(curr.getNext().getNext()); //Skips over the node we want to remove from the Linked List.
		}
		
		return removed;	
	}
}