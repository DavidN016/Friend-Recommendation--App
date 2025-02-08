import java.util.NoSuchElementException;

public class Queue<T> implements Q<T> {
    private class Node {
        private T data;
        private Node next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private int size;
    private Node front;
    private Node end;

    /****CONSTRUCTORS****/

    /**
     * Default constructor for the Queue class
     * @postcondition a new Queue object with all fields
     * assigned default values
     */
    public Queue() {
        front = end = null;
        size = 0;
    }

    /**
     * Converts an array into a Queue
     * @param array the array to copy into
     * the Queue
     */
    public Queue(T[] array) {
      this();
      if (array != null) {
         for (int i = 0; i < array.length; i++) {
            enqueue(array[i]);
         }
      }
    }

    /**
     * Copy constructor for the Queue class
     * Makes a deep copy of the parameter
     * @param original the Queue to copy
     * @postcondition <You fill in here>
     */
    public Queue(Queue<T> original) {
      if (original == null) {
         front = null;
         end = null;
         size = 0;
      }
      else if (original.isEmpty()) {
         size = 0;
         front = null;
         end = null;
      }
      else {
         Node temp = original.front;
         while (temp != null) {
            enqueue(temp.data);
            temp = temp.next;
         }
      }
    }

    /****ACCESSORS****/

    /**
     * Returns the value stored at the front
     * of the Queue
     * @return the value at the front of the queue
     * @precondition isEmpty()
     * @throws NoSuchElementException when the
     * precondition is violated
     */
    public T getFront() throws NoSuchElementException {
        if (isEmpty()) throw new NoSuchElementException();
        return front.data;
    }

    /**
     * Returns the size of the Queue
     * @return the size from 0 to n
     */
    public int getSize() {
        return size;
    }

    /**
     * Determines whether a Queue is empty
     * @return whether the Queue contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /****MUTATORS****/

    /**
     * Inserts a new value at the end of the Queue
     *
     * @param data the new data to insert
     * @postcondition <You fill in here>
     */
    public void enqueue(T data) {
        Node newNode = new Node(data);
        if (isEmpty()) {
           front = newNode;
           end = newNode;
        }
        else {
           end.next  = newNode;
           end = newNode;
        }
        size++;
    }

    /**
     * Removes the front element in the Queue
     * @precondition <You fill in here>
     * @throws NoSuchElementException when
     * the precondition is violated
     * @postcondition <You fill in here>
     */
    public void dequeue() throws NoSuchElementException {
       if (front == null) throw new NoSuchElementException();
       if (size == 1) {
          front = null;
          end = null;
       }
       else {
          Node temp = front;
          temp = temp.next;
          front = temp;
       }
       size--;
    }

    /****ADDITONAL OPERATIONS****/

    /**
     * Returns the values stored in the Queue
     * as a String, separated by a blank space
     * with a new line character at the end
     * @return a String of Queue values
     */
    @Override public String toString() {
        StringBuilder result = new StringBuilder();
        Node temp = front;
        while (temp != null) {
            result.append(temp.data).append(" ");
            temp = temp.next;
        }
        result.append("\n");
        return result.toString();
    }

    /**
     * Determines whether two Queues contain
     * the same values in the same order
     * @param obj the Object to compare to this
     * @return whether obj and this are equal
     */
    @SuppressWarnings("unchecked") // good practice to remove warning here
    @Override public boolean equals(Object obj)  {
        if (obj == this) {
           return true;
        }
        else if (obj == null) {
           return false;
        }
        else if (!(obj instanceof Queue)) {
           return false;
        }
        else {
           Queue<T> iL = (Queue<T>) obj;
           if (size != iL.getSize()) {
              return false;
           }
           else {
              Node temp1 = this.front;
              Node temp2 = iL.front;
              while (temp1 != null || temp2 != null) {
                 if (temp1.data == null || temp2.data == null) {
                    if (temp1.data != temp2.data) {
                       return false;
                    }
                 }
                 else if (!(temp1.data.equals(temp2.data))) {
                    return false;
                 }
                 temp1 = temp1.next;
                 temp2 = temp2.next;
              }
              return true;
           }
        }
    }
}