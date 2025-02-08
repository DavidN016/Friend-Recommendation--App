/**
 * Defines a doubly-linked list class
 *
 */

import java.util.NoSuchElementException;


public class LinkedList<T> {
    private class Node {
        private T data;
        private Node next;
        private Node prev;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }

    private int length;
    private Node first;
    private Node last;
    private Node iterator;

    /**** CONSTRUCTORS ****/

    /**
     * Instantiates a new LinkedList with default values
     *
     * @postcondition <Initializes an empty linked list.>
     */
    public LinkedList() {

        first = null;
        last = null;
        iterator = null;
        length = 0;

    }

    /**
     * Converts the given array into a LinkedList
     *
     * @param array the array of values to insert into this LinkedList
     * @postcondition <a new List object, which is an identical data,copy of the array original>
     */
    public LinkedList(T[] array) {
        length = 0;
        first = null;
        last = null;
        iterator = null;
        if (array != null) {
            for (T item : array) {
                addLast(item);
            }
        }

    }

    /**
     * Instantiates a new LinkedList by copying another List
     *
     * @param original the LinkedList to copy
     * @postcondition a new List object, which is an identical,
     * but separate, copy of the LinkedList original
     */
    public LinkedList(LinkedList<T> original) {
        length = 0;
        first = null;
        last = null;
        iterator = null;
        if (original != null) {
            Node current = original.first;
            while (current != null) {
                addLast(current.data);
                current = current.next;
            }
        }

    }

    /**** ACCESSORS ****/

    /**
     * Returns the value stored in the first node
     *
     * @return the value stored at node first
     * @throws NoSuchElementException <If the listy is empty, throw a NoSuchElementException>
     * @precondition <Retrieves the data from the first node in the list>
     */
    public T getFirst() throws NoSuchElementException {
        if (isEmpty()) {

            throw new NoSuchElementException("The list is empty");

        }
        return first.data;
    }

    /**
     * Returns the value stored in the last node
     *
     * @return the value stored in the node last
     * @throws NoSuchElementException <If the listy is empty, throw a NoSuchElementException>
     * @precondition <Retrieves the data from the last node in the list>
     */
    public T getLast() throws NoSuchElementException {
        if (isEmpty()) {

            throw new NoSuchElementException("The list is empty");

        }
        return last.data;
    }

    /**
     * Returns the data stored in the iterator node
     *
     * @return the data stored in the iterator node
     * @precondition  <iterator != nulL>
     * @throw NullPointerException <Iterator is off the end opf the list.>
     */
    public T getIterator() throws NullPointerException {
        if (iterator != null) {

            return iterator.data;

        } else {

            throw new NullPointerException("getIterator Iterator is off the end of the list.");

        }

    }

    /**
     * Returns the current length of the LinkedList
     *
     * @return the length of the LinkedList from 0 to n
     */
    public int getLength() {
        return length;
    }

    /**
     * Returns whether the LinkedList is currently empty
     *
     * @return whether the LinkedList is empty
     */
    public boolean isEmpty() {
        return length == 0;
    }

    /**
     * Returns whether the iterator is offEnd, i.e. null
     *
     * @return whether the iterator is null
     */
    public boolean offEnd() {
        return iterator == null;
    }

    /**** MUTATORS ****/

    /**
     * Creates a new first element
     *
     * @param data the data to insert at the front of the LinkedList
     * @postcondition <Add a new node with the specified data at the beginning of thg list>
     */
    public void addFirst(T data) {
        Node newNode = new Node(data);

        if (isEmpty()) {

            first = newNode;
            last = newNode;

        } else {

            newNode.next = first;
            first.prev = newNode;
            first = newNode;

        }
        length++;
    }

    /**
     * Creates a new last element
     *
     * @param data the data to insert at the end of the LinkedList
     * @postcondition <Add a new node with the specified data at the end of thg list>
     */
    public void addLast(T data) {
        Node newNode = new Node(data);

        if (isEmpty()) {

            first = newNode;
            last = newNode;

        } else {

            last.next = newNode;
            newNode.prev = last;
            last = newNode;

        }
        length++;

    }

    /**
     * Inserts a new element after the iterator
     *
     * @param data the data to insert
     * @throws NullPointerException <Iterator is off the end opf the list.>
     * @precondition  <==offEnd()>
     */
    public void addIterator(T data) throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("addIterator Iterator is off end.");
        }
        if (iterator == last) {
            addLast(data);
        } else {
            Node newNode = new Node(data);
            Node next = iterator.next;
            newNode.next = next;
            newNode.prev = iterator;
            iterator.next = newNode;
            next.prev = newNode;
            length++;
        }
    }

    /**
     * removes the element at the front of the LinkedList
     *
     * @throws NoSuchElementException <If the listy is empty, throw a NoSuchElementException>
     * @precondition <Removes the first node from the list.>
     * @postcondition <Handles cases for an empty list and lists with only one node>
     */
    public void removeFirst() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is empty");
        }

        if (length == 1) {
            first = null;
            last = null;
            iterator = null;
        } else {
            if (iterator == first) {
                iterator = null;
            }
            first = first.next;
            first.prev = null;
        }
        length--;
    }

    /**
     * removes the element at the end of the LinkedList
     *
     * @precondition <Removes the last node from the list.>
     * @postcondition <Handles cases for an empty list and lists with only one node>
     */
    public void removeLast() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is empty");
        }

        if (length == 1) {
            first = null;
            last = null;
            iterator = null;
        } else {
            if (iterator == last) {
                iterator = null;
            }
            last = last.prev;
            last.next = null;
        }
        length--;
    }

    /**
     * removes the element referenced by the iterator
     *
     * @throws NullPointerException <Iterator is off the end opf the list.>
     * @precondition <!=offEnd()>
     * @postcondition <length--;>
     */
    public void removeIterator() throws NullPointerException {

        if (offEnd()) {
            throw new NullPointerException("Iterator is off end.");
        }

        if (iterator == first) {
            removeFirst();
        } else if (iterator == last) {
            removeLast();
        } else {
            Node prev = iterator.prev;
            Node next = iterator.next;
            prev.next = next;
            next.prev = prev;
            iterator = null;
            length--;
        }

    }

    /**
     * places the iterator at the first node
     *
     * @postcondition <iterator = first;>
     */
    public void positionIterator() {

        iterator = first;

    }

    /**
     * Moves the iterator one node towards the last
     *
     * @throws NullPointerException <Iterator is off the end opf the list.>
     * @precondition <!=offEnd()>
     * @postcondition <iterator = iterator.next;>
     */
    public void advanceIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("advanceIterator Iterator is off end.");
        }
        iterator = iterator.next;
    }

    /**
     * Moves the iterator one node towards the first
     *
     * @throws NullPointerException <Iterator is off the end opf the list.>
     * @precondition <!=offEnd()>
     * @postcondition < iterator = iterator.prev>
     */
    public void reverseIterator() throws NullPointerException {
        if (offEnd()) {
            throw new NullPointerException("Iterator is off end.");
        }

        iterator = iterator.prev;

    }

    /**** ADDITIONAL OPERATIONS ****/

    /**
     * Re-sets LinkedList to empty as if the
     * default constructor had just been called
     */
    public void clear() {
        length = 0;
        first = null;
        last = null;
        iterator = null;
    }

    /**
     * Converts the LinkedList to a String, with each value separated by a blank
     * line At the end of the String, place a new line character
     *
     * @return the LinkedList as a String
     */
    @Override
    public String toString() {

        StringBuilder result = new StringBuilder();
        Node temp = first;

        while (temp != null) {

            result.append(temp.data + " ");
            temp = temp.next;

        }
        return result.toString() + "\n";
    }

    /**
     * Determines whether the given Object is
     * another LinkedList, containing
     * the same data in the same order
     *
     * @param obj another Object
     * @return whether there is equality
     */
    @SuppressWarnings("unchecked") //good practice to remove warning here
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        LinkedList<T> otherList = (LinkedList<T>) obj;
        if (length != otherList.length) {
            return false;
        }

        Node thisCurrent = first;
        Node otherCurrent = otherList.first;
        while (thisCurrent != null) {
            if (otherCurrent == null) return false;
            if (thisCurrent.data == null && otherCurrent.data != null) return false;
            if (thisCurrent.data != null && otherCurrent.data == null) return false;
            if (thisCurrent.data != null && !thisCurrent.data.equals(otherCurrent.data)) return false;

            thisCurrent = thisCurrent.next;
            otherCurrent = otherCurrent.next;
        }
        return true;
    }

    /**CHALLENGE METHODS*/

    /**
     * Moves all nodes in the list towards the end
     * of the list the number of times specified
     * Any node that falls off the end of the list as it
     * moves forward will be placed the front of the list
     * For example: [1, 2, 3, 4, 5], numMoves = 2 -> [4, 5, 1, 2 ,3]
     * For example: [1, 2, 3, 4, 5], numMoves = 4 -> [2, 3, 4, 5, 1]
     * For example: [1, 2, 3, 4, 5], numMoves = 7 -> [4, 5, 1, 2 ,3]
     *
     * @param numMoves the number of times to move each node.
     * @throws IllegalArgumentException when numMoves < 0
     * @precondition numMoves >= 0
     * @postcondition iterator position unchanged (i.e. still referencing
     * the same node in the list, regardless of new location of Node)
     */
    public void spinList(int numMoves) throws IllegalArgumentException {
        if (numMoves < 0) {
            throw new IllegalArgumentException("Number of moves cannot be negative.");
        }
        if (numMoves == 0 || isEmpty() || length == 1) {
            return;
        }
        for (int i = 0; i < numMoves % length; i++) {
            // move last to first
            if (iterator == last) {
                iterator = null;
            }
            Node newLast = last.prev;
            last.next = first;
            last.prev = null;

            newLast.next = null;
            first.prev = last;
            first = last;
            last = newLast;
        }
    }


    /**
     * Splices together two LinkedLists to create a third List
     * which contains alternating values from this list
     * and the given parameter
     * For example: [1,2,3] and [4,5,6] -> [1,4,2,5,3,6]
     * For example: [1, 2, 3, 4] and [5, 6] -> [1, 5, 2, 6, 3, 4]
     * For example: [1, 2] and [3, 4, 5, 6] -> [1, 3, 2, 4, 5, 6]
     *
     * @param list the second LinkedList
     * @return a new LinkedList, which is the result of
     * interlocking this and list
     * @postcondition this and list are unchanged
     */
    public LinkedList<T> altLists(LinkedList<T> list) {
        if (list == null) {
            return new LinkedList<>(this);
        }
        LinkedList<T> result = new LinkedList<>();
        Node thisNode = first;
        Node otherNode = list.first;
        while (thisNode != null || otherNode != null) {
            if (thisNode != null) {
                result.addLast(thisNode.data);
                thisNode = thisNode.next;
            }
            if (otherNode != null) {
                result.addLast(otherNode.data);
                otherNode = otherNode.next;
            }
        }
        return result;
    }

    /**
     * Returns each element in the LinkedList along with its
     * numerical position from 1 to n, followed by a newline.
     * @return the String representation of the LinkedList
     */
    public String numberedListString() {
        StringBuilder result = new StringBuilder();
        Node current = first;
        int position = 1;

        while (current != null) {
            result.append(position).append(". ").append(current.data).append("\n");
            current = current.next;
            position++;
        }
        result.append("\n");
        return result.toString();
    }

    /**
     * Searches the LinkedList for a given element's index.
     * @param data the data whose index to locate.
     * @return the index of the data or -1 if the data is not contained
     * in the LinkedList.
     */
    public int findIndex(T data) {
        Node current = first;
        int index = 0;

        while (current != null) {
            if (current.data.equals(data)) {
                return index;
            }
            current = current.next;
            index++;
        }

        return -1;
    }

    /**
     * Advances the iterator to location within the LinkedList
     * specified by the given index.
     * @param index the index at which to place the iterator.
     * @precondition Must position iterator before calling this method.
     * @throws NullPointerException when the precondition is violated.
     */
    public void advanceIteratorToIndex(int index) throws NullPointerException {

        if (index < 0 || index >= length || iterator != first) {
            throw new NullPointerException("Index is out of bounds.");
        }

        for (int i = 0; i < index; i++) {
            advanceIterator();
        }

    }


}
