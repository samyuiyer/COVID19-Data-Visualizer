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


/**
 * @author ankurgarg
 *
 */
public class GeoTrie extends Trie<String[], String> {

  /**
   * private helper method to insert
   */
  public GeoTrie() {
    root = new Node(new DataPoint("Global", new String[12]));
  }

  public void insert(String[] key, DataPoint value) throws IllegalNullKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    if (root.data != null)
      root.data.increment(value);
    insert(key.length - 1, root, key, "", value);
  }

  protected void insert(int level, Node curr, String[] key, String fullKey, DataPoint value) {
    fullKey = key[level] + fullKey;
    if (curr.children.containsKey(key[level]))
      curr.children.get(key[level]).data.increment(value);
    else {
      curr.children.put(key[level], new Node(new DataPoint(fullKey, value)));
    }
    if (level > 0)
      insert(level - 1, curr.children.get(key[level]), key, ", " + fullKey, value);
  }
}
