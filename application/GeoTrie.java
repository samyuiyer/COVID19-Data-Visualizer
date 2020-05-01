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

import java.util.Arrays;

/**
 * an implementation of a Trie that sorts by geographic location
 *
 */
public class GeoTrie extends Trie<String, String> {

  /**
   * the constructor for a new empty geoTrie
   */
  public GeoTrie() {
    clear();
  }

  /**
   * clears the tries back to an empty state
   */
  public void clear() {
    size = 0;
    Integer[] zeros = new Integer[95];
    Arrays.fill(zeros, 0);
    root = new Node(new DataPoint("Global", new String[] {"Global", "", "", "", "0", "0"}, zeros,
        zeros, zeros));
  }

  /**
   * insert the given dataPoint in the trie at the given key
   * 
   * @param key,   the key to insert at
   * @param value, the data point associated with the key
   * 
   * @throws IllegalNullKeyException if key is null
   */
  public void insert(String key, DataPoint value) throws IllegalNullKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    root.data.increment(value);
    String[] keySplit = key.replace(", ", ",").split(",");
    insert(keySplit.length - 1, root, keySplit, "", value);
  }

  /**
   * helper method to recursively insert and increments values in the geo tree
   * 
   * @param level   - the current level of the tree we are at recursively
   * @param curr    - the current node in the tree we are at
   * @param key     - the key to insert
   * @param fullKey - the porting of the key the represents curr
   * @param value   - the value to be inserted
   */
  protected void insert(int level, Node curr, String[] key, String fullKey, DataPoint value) {
    fullKey = key[level] + fullKey; // add the the full key represent curr

    // increments the value as need to accumulate general data
    if (curr.children.containsKey(key[level]))
      curr.children.get(key[level]).data.increment(value);
    else { // if at a root, create a new node
      curr.children.put(key[level], new Node(new DataPoint(fullKey, value)));
      size++;
    }
    if (level > 0)// recursively move down a level
      insert(level - 1, curr.children.get(key[level]), key, ", " + fullKey, value);
  }

}
