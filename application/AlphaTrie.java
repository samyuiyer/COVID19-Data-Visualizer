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


/**
 * @author ankurgarg
 *
 */
public class AlphaTrie extends Trie<String, Character> {

  /**
   * private helper method to insert
   */
  public AlphaTrie() {
    root = new Node(null);
  }
  
  public void insert(String key, DataPoint value) throws IllegalNullKeyException {
    if (key == null)
      throw new IllegalNullKeyException();
    if (root.data != null)
      root.data.increment(value);
    insert(0, root, key, value);
  }
  
  protected void insert(int level, Node curr, String key, DataPoint value) {
   
    if (curr.children.containsKey(key.charAt(level))) {
      if (curr.children.get(key.charAt(level)).children.isEmpty()) {
        Node oldChild = curr.children.remove(key.charAt(level));
        Node newCurr = new Node(null);
        curr.children.put(key.charAt(level), newCurr);
        newCurr.children.put(oldChild.data.key.charAt(level+1), oldChild);
      }
      insert(level + 1, curr.children.get(key.charAt(level)), key, value);

    } else {
      curr.children.put(key.charAt(level), new Node(new DataPoint(key, value)));
      size++;
    }

  }
}
