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
public class GeoTrie extends Trie<String[],String> {

  /**
   * private helper method to insert
   */
  public GeoTrie() {
    root = new Node(new DataPoint("Global", 0));
  }
  public void insert(String[] key, DataPoint value) throws IllegalNullKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    if (root.data != null)
      root.data.increment(value);
    insert(key.length-1, root, key, "", value);
  }
  
  protected void insert(int level, Node curr, String[] key,String fullKey, DataPoint value) {
    fullKey = key[level]+fullKey;
    if (curr.children.containsKey(key[level]))
      curr.children.get(key[level]).data.increment(value);
    else {
      curr.children.put(key[level], new Node(new DataPoint(fullKey,value)));
    }
    if (level > 0)
      insert(level - 1, curr.children.get(key[level]), key,", "+fullKey, value);
  }
 
  public static void main(String[] args) throws IllegalNullKeyException {
    GeoTrie t = new GeoTrie();
    t.insert(new String[]{"Mad","Wi","US"}, new DataPoint(1));
    t.insert(new String[]{"Mil","Wi","US"}, new DataPoint(5));
    t.insert(new String[]{"Gby","Wi","US"}, new DataPoint(4));
    t.insert(new String[]{"Mad","Ca","US"}, new DataPoint(7));
    t.insert(new String[]{"Sac","Ca","US"}, new DataPoint(9));
    t.insert(new String[]{"Sar","Ca","US"}, new DataPoint(2));
    t.insert(new String[]{"Sf","Ca","US"}, new DataPoint(7));
    t.insert(new String[]{"Slc","Ut","UK"}, new DataPoint(8));
    t.insert(new String[]{"Un","UK"}, new DataPoint(8));
    t.print();
  }

}
