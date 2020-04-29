///////////////////////////////////////////////////////////////////////////////
//
// 	Title: ateam_final_project
// 	Author: Ankur Garg, Eric Ertl, Justin Chan, Samyu Lyer, Sudeep Reddy, 
//
// 	Course: CS400
//	Semester: Spring 2020
//	Lecture Number: 001
//
//	Date: 4/29/2020
//
// 	Description: 	A project that displays statistics relating to COVID-19 in a
//					variety of ways. 
//
///////////////////////////////////////////////////////////////////////////////

package application;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author ankurgarg
 * @param <K>
 *
 */
public abstract class Trie<K, S> implements DataStructureADT<K, DataPoint> {
  protected Node root;
  protected int size;

  protected class Node {
    protected DataPoint data;
    protected HashMap<S, Node> children;

    public Node(DataPoint data) {

      this.data = data == null ? null : new DataPoint(data);
      this.children = new HashMap<>();
    }
  }

  public Trie() {
    root = new Node(null);
  }

  public abstract void insert(K key, DataPoint value) throws IllegalNullKeyException;

  public void print() {
    print(root, 0);
  }

  public void print(Node curr, int space) {
    System.out.println(" ".repeat(space) + curr.data);
    for (S s : curr.children.keySet()) {
      print(curr.children.get(s), space + 2);
    }
  }

  public List<DataPoint> getAll() {
    List<DataPoint> l = new LinkedList<>();
    getAll(root, l);
    return l;
  }

  public void getAll(Node curr, List<DataPoint> l) {
    if (curr != null)
      l.add(curr.data);
    for (S s : curr.children.keySet()) {
      getAll(curr.children.get(s), l);
    }
  }

  @Override
  public boolean remove(K key) throws IllegalNullKeyException {
    return true;
  }

  @Override
  public DataPoint get(K key) throws IllegalNullKeyException, KeyNotFoundException {
    return null;
  }

  @Override
  public int numKeys() {
    return size;
  }

}
