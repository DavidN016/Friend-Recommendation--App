/**
 * BST.java
 *
 * @author 
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.NoSuchElementException;

public class BST<T> {
    private class Node {
        private T data;
        private Node left;
        private Node right;

        public Node(T data) {
            this.data = data;
            left = null;
            right = null;
        }
    }

    private Node root;

    /***CONSTRUCTORS***/

    /**
     * Default constructor for BST sets root to null.
     */
    public BST() {
        // fill in here
        root = null;
    }

    /**
     * Copy constructor for BST.
     *
     * @param bst the BST of which to make a copy.
     * @param cmp the way the tree is organized.
     */
    public BST(BST<T> bst, Comparator<T> cmp) {
        // fill in here
        if (bst != null && cmp != null) {
            copyHelper(bst.root, cmp);
        }
    }

    /**
     * Helper method for copy constructor.
     *
     * @param node the node containing data to copy.
     * @param cmp  the way the tree is organized.
     */
    private void copyHelper(Node node, Comparator<T> cmp) {
        // fill in here
        if (node != null) {
            insert(node.data, cmp);
            copyHelper(node.left, cmp);
            copyHelper(node.right, cmp);
        }
    }

    /**
     * Creates a BST of minimal height from an array of values.
     *
     * @param array the list of values to insert.
     * @param cmp   the way the tree is organized.
     * @throws IllegalArgumentException when the array is unsorted.
     * @precondition array must be sorted in ascending order.
     */
    public BST(T[] array, Comparator<T> cmp) throws IllegalArgumentException {
        // fill in here
        if (array == null || array.length == 0) {
            return;
        }
        if (!isSorted(array, cmp)) {
            throw new IllegalArgumentException("Array must be sorted in ascending order.");
        }

        root = arrayHelper(0, array.length - 1, array);
    }

    /**
     * Private helper method for array constructor
     * to check for a sorted array.
     *
     * @param array the array to check.
     * @param cmp   the way the tree is organized.
     * @return whether the array is sorted.
     */
    private boolean isSorted(T[] array, Comparator<T> cmp) {
        // fill in here
        for (int i = 1; i < array.length; i++) {
            if (cmp.compare(array[i], array[i - 1]) < 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Recursive helper for the array constructor.
     *
     * @param begin beginning array index.
     * @param end   ending array index.
     * @param array array to search.
     * @return the newly created Node.
     */
    private Node arrayHelper(int begin, int end, T[] array) {
        // fill in here
        if (begin > end) {
            return null;
        }
        int mid = (begin + end) / 2;
        Node node = new Node(array[mid]);
        node.left = arrayHelper(begin, mid - 1, array);
        node.right = arrayHelper(mid + 1, end, array);
        return node;
    }

    /***ACCESSORS***/

    /**
     * Returns the data stored in the root.
     *
     * @return the data stored in the root.
     * @throws NoSuchElementException when precondition is violated.
     * @precondition !isEmpty()
     */
    public T getRoot() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Tree is empty.");
        }
        return root.data;
    }

    /**
     * Determines whether the tree is empty.
     *
     * @return whether the tree is empty.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Returns the current size of the tree (number of nodes).
     *
     * @return the size of the tree.
     */
    public int getSize() {
        return getSize(root);
    }

    /**
     * Helper method for the getSize method.
     *
     * @param node the current node to count.
     * @return the size of the tree.
     */
    private int getSize(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + getSize(node.left) + getSize(node.right);
    }

    /**
     * Returns the height of tree by counting edges.
     *
     * @return the height of the tree.
     */
    public int getHeight() {
        return getHeight(root);

    }

    /**
     * Helper method for getHeight method.
     *
     * @param node the current node whose height to count.
     * @return the height of the tree.
     */
    private int getHeight(Node node) {
        if (node == null) {
            return -1;
        }
        int leftHeight = getHeight(node.left);
        int rightHeight = getHeight(node.right);
        return 1 + Math.max(leftHeight, rightHeight);
    }

    /**
     * Returns the smallest value in the tree.
     *
     * @return the smallest value in the tree.
     * @throws NoSuchElementException when the precondition is violated.
     * @precondition !isEmpty()
     */
    public T findMin() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Tree is empty.");
        }
        return findMin(root);
    }

    /**
     * Recursive helper method to findMin method.
     *
     * @param node the current node to check if it is the smallest.
     * @return the smallest value in the tree.
     */
    private T findMin(Node node) {
        if (node.left == null) {
            return node.data;
        }
        return findMin(node.left);
    }

    /**
     * Returns the largest value in the tree
     *
     * @return the largest value in the tree.
     * @throws NoSuchElementException when the precondition is violated.
     * @precondition !isEmpty()
     */
    public T findMax() throws NoSuchElementException {
        if (isEmpty()) {
            throw new NoSuchElementException("Tree is empty.");
        }
        return findMax(root);
    }

    /**
     * Recursive helper method to findMax method.
     *
     * @param node the current node to check if it is the largest.
     * @return the largest value in the tree.
     */
    private T findMax(Node node) {
        if (node.right == null) {
            return node.data;
        }
        return findMax(node.right);
    }

    /**
     * Searches for a specified value in the tree.
     *
     * @param data the value to search for.
     * @param cmp  the Comparator that indicates the way
     *             the data in the tree was ordered.
     * @return the data stored in that Node of the tree, otherwise null.
     */
    public T search(T data, Comparator<T> cmp) {
        return search(data, root, cmp);

    }

    /**
     * Helper method for the search method.
     *
     * @param data the data to search for.
     * @param node the current node to check.
     * @param cmp  the Comparator that determines how the BST is organized.
     * @return the data stored in that Node of the tree, otherwise null.
     */
    private T search(T data, Node node, Comparator<T> cmp) {
        if (node == null) {
            return null;
        }
        int cmpResult = cmp.compare(data, node.data);
        if (cmpResult == 0) {
            return node.data;
        } else if (cmpResult < 0) {
            return search(data, node.left, cmp);
        } else {
            return search(data, node.right, cmp);
        }
    }


    public ArrayList<T> searchAll(T data, Comparator<T> cmp) {
        ArrayList<T> result = new ArrayList<>();
        searchAll(data, root, cmp, result);
        return result;
    }

    private void searchAll(T data, Node node, Comparator<T> cmp, ArrayList<T> result) {
        if (node == null) {
            return;
        }
        int cmpResult = cmp.compare(data, node.data);
        if (cmpResult == 0) {
            result.add(node.data);
            searchAll(data, node.left, cmp, result);
            searchAll(data, node.right, cmp, result);
        } else if (cmpResult < 0) {
            searchAll(data, node.left, cmp, result);
        } else {
            searchAll(data, node.right, cmp, result);
        }
    }

    /**
     * return the ArrayList representation of the BST using an inorder traversal.
     * @return
     */
    public ArrayList<T> convertToList() {
        ArrayList<T> result = new ArrayList<>();
        convertToList(root, result);
        return result;
    }

    public void convertToList(Node node,ArrayList<T> result) {
        if (node == null) {
            return;
        }
        convertToList(node.left, result);
        result.add(node.data);
        convertToList(node.right, result);
    }

    /***MUTATORS***/

    /**
     * Inserts a new node in the tree.
     *
     * @param data the data to insert.
     * @param cmp  the Comparator indicating how data in the tree is ordered.
     */
    public void insert(T data, Comparator<T> cmp) {
        insert(data, root, cmp);
    }

    /**
     * Helper method to insert.
     * Inserts a new value in the tree.
     *
     * @param data the data to insert.
     * @param node the current node in the search for the correct insert
     *             location.
     * @param cmp  the Comparator indicating how data in the tree is ordered.
     */
    private void insert(T data, Node node, Comparator<T> cmp) {
        if (node == null) {
            root = new Node(data);
        } else {
            int cmpResult = cmp.compare(data, node.data);
            if (cmpResult < 0) {
                if (node.left == null) {
                    node.left = new Node(data);
                } else {
                    insert(data, node.left, cmp);
                }
            } else if (cmpResult > 0) {
                if (node.right == null) {
                    node.right = new Node(data);
                } else {
                    insert(data, node.right, cmp);
                }
            }
        }
    }

    /**
     * Removes a value from the BST
     *
     * @param data the value to remove
     * @param cmp  the Comparator indicating how data in the tree is organized.
     *             Note: updates nothing when the element is not in the tree.
     */
    public void remove(T data, Comparator<T> cmp) {
        root = remove(data, root, cmp);
    }

    /**
     * Helper method to the remove method.
     *
     * @param data the data to remove.
     * @param node the current node.
     * @param cmp  the Comparator indicating how data in the tree is organized.
     * @return an updated reference variable.
     */
    private Node remove(T data, Node node, Comparator<T> cmp) {
        if (node == null) {
            return null;
        }
        int cmpResult = cmp.compare(data, node.data);
        if (cmpResult < 0) {
            node.left = remove(data, node.left, cmp);
        } else if (cmpResult > 0) {
            node.right = remove(data, node.right, cmp);
        } else {
            if (node.left == null) {
                return node.right;
            } else if (node.right == null) {
                return node.left;
            }
            node.data = findMin(node.right);
            node.right = remove(node.data, node.right, cmp);
        }
        return node;
    }

    /***ADDITONAL OPERATIONS***/

    /**
     * Returns a String containing the data in pre order
     * followed by a new line.
     *
     * @return a String of data in pre order.
     */
    public String preOrderString() {
        StringBuilder preOrder = new StringBuilder();
        preOrderString(root, preOrder);
        return preOrder.toString() + "\n";
    }

    /**
     * Helper method to preOrderString method.
     * Prints the data in pre order to the console followed by a new line.
     *
     * @param node     the current Node
     * @param preOrder a StringBuilder containing the data
     */
    private void preOrderString(Node node, StringBuilder preOrder) {
        if (node != null) {
            preOrder.append(node.data + " ");
            preOrderString(node.left, preOrder);
            preOrderString(node.right, preOrder);
        }
    }

    /**
     * Returns a String containing the data in order followed by a new line.
     *
     * @return a String of data in order
     */
    public String inOrderString() {
        StringBuilder inOrder = new StringBuilder();
        inOrderString(root, inOrder);
        return inOrder.toString();
    }

    /**
     * Helper method to inOrderString.
     * Inserts the data in order into a String in order.
     *
     * @param node    the current Node
     * @param inOrder a String containing the data
     */
    private void inOrderString(Node node, StringBuilder inOrder) {
        if (node != null) {
            inOrderString(node.left, inOrder);
            inOrder.append(node.data + "\n");
            inOrderString(node.right, inOrder);
        }
    }

    /**
     * Returns a String containing the data in post order.
     *
     * @return a String of data in post order
     */
    public String postOrderString() {
        StringBuilder postOrder = new StringBuilder();
        postOrderString(root, postOrder);
        return postOrder.toString() + "\n";
    }

    /**
     * Helper method to postOrderString
     * Inserts the data in post order into a String
     *
     * @param node      the current Node
     * @param postOrder a String containing the data
     */
    private void postOrderString(Node node, StringBuilder postOrder) {
        if (node != null) {
            postOrderString(node.left, postOrder);
            postOrderString(node.right, postOrder);
            postOrder.append(node.data + " ");
        }
    }

    /**
     * Creates a String that is a height order
     * traversal of the data in the tree starting at
     * the Node with the largest height (the root)
     * down to Nodes of smallest height - with
     * Nodes of equal height added from left to right.
     *
     * @return the level order traversal as a String
     */
    public String levelOrderString() {
        Queue<Node> que = new Queue<>();
        StringBuilder sb = new StringBuilder();
        que.enqueue(root);
        levelOrderString(que, sb);
        return sb.toString() + "\n";
    }

    /**
     * Helper method to levelOrderString.
     * Inserts the data in level order into a String.
     *
     * @param que            the Queue in which to store the data.
     * @param heightTraverse a StringBuilder containing the data.
     */
    private void levelOrderString(Queue<Node> que, StringBuilder heightTraverse) {
        if (!que.isEmpty()) {
            Node nd = que.getFront();
            que.dequeue();
            if (nd != null) {
                que.enqueue(nd.left);
                que.enqueue(nd.right);
                heightTraverse.append(nd.data + " ");
            }
            levelOrderString(que, heightTraverse);
        }
    }


    /**Challenge Methods */

    /**
     * Returns the data of the Node who is the shared precursor to the two
     * Nodes containing the given data. If either data1 or data2 is a
     * duplicate value, the method will find the precursor of the duplicate
     * with greatest height.
     *
     * @param data1 the data contained in one Node of the tree.
     * @param data2 the data contained in one Node of the tree.
     * @return the data stored by the shared precursor or null if no
     * precursor exists.
     * @throws IllegalArgumentException when one or both values do not exist
     *                                  in the BST.
     * @precondition data1 and data2 must exist in the BST.
     */
    public T sharedPrecursor(T data1, T data2, Comparator<T> cmp) throws IllegalArgumentException {
        if (search(data1, cmp) == null || search(data2, cmp) == null) {
            throw new IllegalArgumentException("One or both values do not exist in the BST.");
        }
        return sharedPrecursor(data1, data2, root, cmp);
    }

    /**
     * Private helper method to sharedPrecursor, which recursively locates
     * the shared precursor.
     *
     * @param data1     the data contained in one Node of the tree.
     * @param data2     the data contained in one Node of the tree.
     * @param currLevel the current Node.
     * @return the data stored by the shared precursor.
     */
    private T sharedPrecursor(T data1, T data2, Node currLevel, Comparator<T> cmp) {
        if (currLevel == null) {
            return null;
        }

        if (cmp.compare(data1, currLevel.data) < 0 && cmp.compare(data2, currLevel.data) < 0) {
            return sharedPrecursor(data1, data2, currLevel.left, cmp);
        }

        if (cmp.compare(data1, currLevel.data) > 0 && cmp.compare(data2, currLevel.data) > 0) {
            return sharedPrecursor(data1, data2, currLevel.right, cmp);
        }

        return currLevel.data;
    }
}
