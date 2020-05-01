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

import java.util.LinkedList;
import java.util.List;

/**
 * Stores Data in a traditional Tire- with prefixes based on letters This class is not currently
 * used in the program, but can provide a data Structure for future expansion
 */
public class AlphaTrie extends Trie<String, Character> {

  /**
   * the constructor for a new empty AlphaTrie
   */
  public AlphaTrie() {
    root = new Node(null);
  }

  /**
   * insert the given dataPoint in the trie at the given key
   * 
   * @param key,   the key to inset at
   * @param value, the data point assosiated with the key
   * 
   * @throws IllegalNullKeyException if key is null
   */
  public void insert(String key, DataPoint value) throws IllegalNullKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    if (root.data != null)
      root.data.increment(value);
    insert(0, root, key, value);
  }

  /**
   * helper method to recursively insert and increments values in the alpha tree
   * 
   * @param level - the current level of the tree we are at recursively
   * @param curr  - the current node in the tree we are at
   * @param key   - the key to insert
   * @param value - the value to be inserted
   */
  protected void insert(int level, Node curr, String key, DataPoint value) {

    if (curr.children.containsKey(key.charAt(level))) {// check for existing node
      if (curr.children.get(key.charAt(level)).data != null) {
        Node oldChild = curr.children.remove(key.charAt(level)); // store the value to be replace

        // four cases based on the the value to insert, and the curr value
        if (oldChild.data.key.length() > level + 1 && key.length() > level + 1) {

          // case 1: put the new value nex to old
          Node newCurr = new Node(null);
          curr.children.put(key.charAt(level), newCurr);
          newCurr.children.put(oldChild.data.key.charAt(level + 1), oldChild);
        } else if (oldChild.data.key.length() == level + 1 && key.length() > level + 1) {

          // case 2: put the new value below the old value
          curr.children.put(key.charAt(level), oldChild);
        } else if (oldChild.data.key.length() > level + 1 && key.length() == level + 1) {

          // case 3: put hte old value below the new value
          curr.children.put(key.charAt(level), new Node(new DataPoint(key, value)));
          key = oldChild.data.key;
          value = oldChild.data;
        } else {

          // case 4: old == new so increment old and discard new;
          curr.children.put(key.charAt(level), oldChild);
          oldChild.data.increment(value);
          return;
        }
      }
      insert(level + 1, curr.children.get(key.charAt(level)), key, value);
    } else {
      curr.children.put(key.charAt(level), new Node(new DataPoint(key, value)));
      size++;
    }

  }

  /**
   * returns a list of all elements that start with a given String
   * 
   * @param key - the trying to look for
   * @return - a list of all appropriate DataPoints
   * @throws IllegalNullKeyException
   */
  public List<DataPoint> suggest(String key) throws IllegalNullKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    return suggest(root, key);
  }


  /**
   * helper method to return a list of all elements that start with a given String
   * 
   * @param curr - the current node we are at
   * @param key  - the string that the Data dhould start with
   * @return
   */
  private List<DataPoint> suggest(Node curr, String key) {
    if (curr == null)
      return new LinkedList<>();
    if (key.length() == 0) {
      List<DataPoint> l = new LinkedList<>();
      getAll(curr, l); // gets all elements staring with curr as the 'root'
      return l;
    }
    return suggest(curr.children.get(key.charAt(0)), key.substring(1));// Recursively calls
  }
}
