import java.util.Iterator;
import java.util.NoSuchElementException;

/////////////////////////////////////////////////////////////////////////////
//Semester:         CS367 Fall 2017 
//PROJECT:          CS 367 Programming Assignment 2: Train Hub
//FILE:             LinkedListIterator.java
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
 * The iterator implementation for LinkedList.  The constructor for this
 * class requires that a reference to a Listnode with the first data
 * item is passed in.
 * 
 * If the Listnode reference used to create the LinkedListIterator is null,
 * that implies there is no data in the LinkedList and this iterator
 * should handle that case correctly.
 * 
 * <p>Bugs: N/A
 * 
 * @author Manuel Takeshi Gomez
 * @author Mikayla Buford
 */
public class LinkedListIterator<T> implements Iterator<T> {
	private Listnode<T> curr;
	
	/**
	 * Constructs a LinkedListIterator when given the first node
	 * with data for a chain of nodes.
	 * 
	 * Tip: do not construct with a "blank" header node.
	 * 
	 * @param a reference to a List node with data. 
	 */
	public LinkedListIterator(Listnode<T> head) {
		curr = head; //Gets the first node from the LinkedList class iterator constructor.
	}
	
	/**
	 * Returns the next element in the iteration and then advances the
	 * iteration reference.
	 * 
	 * @return the next data item in the iteration that has not yet been returned 
	 * @throws NoSuchElementException if the iteration has no more elements 
	 */
	@Override
	public T next() {
		if (curr == null) {
			throw new NoSuchElementException();
		}
		
		T data = curr.getData(); //Get data from the node that the curr object was pointing to.
		curr = curr.getNext();   //Advances the pointer to the next node to operate on.
		return data;
	}
	
	/**
	 * 
	 * @return true if their are any remaining data items to iterate through
	 */
	@Override
	public boolean hasNext() {
		return curr != null; 
	}
       
    /**
     * The remove operation is not supported by this iterator
     * @throws UnsupportedOperationException if the remove operation is not 
     * supported by this iterator
     */
    @Override
	public void remove() {
	  throw new UnsupportedOperationException();
	}

}