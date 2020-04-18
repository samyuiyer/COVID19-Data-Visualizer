/**
 *
 * Author: Ankur Garg Email: Agarg34@wisc.edu
 * 
 * Course: CS400 Semester: Spring2020 Lecture: 001 Date: Apr 16, 2020
 *
 * Files: Trie.java
 *
 */
package ateam_final_project;

import java.util.HashMap;

/**
 * @author ankurgarg
 * @param <K>
 *
 */
public abstract class Trie<K, S> implements DataStructureADT<K, DataPoint> {
  protected Node root;

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

  public void print() {
    print(root, 0);
  }

  public void print(Node curr, int space) {
    System.out.println(" ".repeat(space) + curr.data);
    for (S s : curr.children.keySet()) {
      print(curr.children.get(s), space + 2);
    }
  }

  @Override
  public boolean remove(K key) throws IllegalNullKeyException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public DataPoint get(K key) throws IllegalNullKeyException, KeyNotFoundException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int numKeys() {
    // TODO Auto-generated method stub
    return 0;
  }

}
