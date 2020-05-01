///////////////////////////////////////////////////////////////////////////////
//
// Title: ateam_final_project
// Author: Ankur Garg, Eric Ertl, Justin Chan, Samyu Iyer, Sudeep Reddy
//
// Course: CS400
// Semester: Spring 2020
// Lecture Number: 001
//
// Date: 4/29/2020
//
// Description: A project that displays statistics relating to COVID-19 in a
// variety of ways.
//
///////////////////////////////////////////////////////////////////////////////

package application;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class that represent a generic Trie, an implementation of a data structure that sorts by
 * prefix
 */
public abstract class Trie<K, S> {
  protected Node root; // the root of the tree
  protected int size; // the number of elements in the key

  /**
   * an inner class that repesents treeNodes in the the trie
   */
  protected class Node {
    protected DataPoint data; // the dataPoint for the given node
    protected HashMap<S, Node> children; // a list of the node's children

    /**
     * constructor for a node that creates an empty set of chilrens with a given dataPoint
     * 
     * @param data
     */
    public Node(DataPoint data) {
      this.data = data == null ? null : new DataPoint(data); // checks for null data & creates data
      this.children = new HashMap<>();
    }
  }

  /**
   * default trie constructor that created an empty tree with null root
   */
  public Trie() {
    root = new Node(null);
    size = 0;
  }

  /**
   * clears the tries back to an empty state
   */
  public void clear() {
    root.children.clear();
    size = 0;
  }

  /**
   * insert the given dataPoint in the trie at the given key
   * 
   * @param key,   the key to insert at
   * @param value, the data point associated with the key
   * 
   * @throws IllegalNullKeyException if key is null
   */
  public abstract void insert(K key, DataPoint value) throws IllegalNullKeyException;

  /**
   * print the full tree to the console in pre-order traversal using a recurive helper, and
   * indentation to represent a new level
   */
  public void print() {
    print(root, 0);
  }

  /**
   * recursive helper for the print function
   * 
   * @param curr  - the current node
   * @param space - the amount to indent the given entry
   */
  public void print(Node curr, int space) {
    System.out.println(" ".repeat(space) + curr.data); // print curr's data
    for (S s : curr.children.keySet()) { // print all children of curr
      print(curr.children.get(s), space + 2);
    }
  }

  /**
   * gets a list of all elements in the tree in pre-order isong a recursive helper
   * 
   * @return the list of all elements
   */
  public List<DataPoint> getAll() {
    List<DataPoint> l = new LinkedList<>();
    getAll(root, l);
    return l;
  }

  /**
   * recursively adds all non-null elements to the list
   * 
   * @param curr
   * @param l
   */
  public void getAll(Node curr, List<DataPoint> l) {
    if (curr != null)
      l.add(curr.data); // add the current
    for (S s : curr.children.keySet()) {// adds all of the chilrend to the list
      getAll(curr.children.get(s), l);
    }
  }
}
