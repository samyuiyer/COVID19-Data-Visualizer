/**
 *
 * Author: Ankur Garg Email: Agarg34@wisc.edu
 * 
 * Course: CS400 Semester: Spring2020 Lecture: 001 Date: Apr 16, 2020
 *
 * Files: Trie.java
 *
 */
package application;

import java.util.Arrays;

/**
 * @author ankurgarg
 *
 */
public class GeoTrie extends Trie<String, String> {

  /**
   * private helper method to insert
   */
  public GeoTrie() {
    clear();
  }
  
  public void clear() {
    Integer[] zeros = new Integer[95];
    Arrays.fill(zeros, 0);
    root = new Node(new DataPoint("Global", new String[] {"Global", "", "", "", "0", "0"}, zeros,
        zeros, zeros));
  }

  public void insert(String key, DataPoint value) throws IllegalNullKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    root.data.increment(value);
    String[] keySplit = key.replace(", ", ",").split(",");
    insert(keySplit.length - 1, root, keySplit, "", value);
  }

  protected void insert(int level, Node curr, String[] key, String fullKey, DataPoint value) {
    fullKey = key[level] + fullKey;
    if (curr.children.containsKey(key[level]))
      curr.children.get(key[level]).data.increment(value);
    else {
      curr.children.put(key[level], new Node(new DataPoint(fullKey, value)));
      size++;
    }
    if (level > 0)
      insert(level - 1, curr.children.get(key[level]), key, ", " + fullKey, value);
  }

}
