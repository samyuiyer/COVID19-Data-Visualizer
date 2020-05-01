package application;

import java.util.LinkedList;
import java.util.List;

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

  protected void insert(int level, Node curr, String key, DataPoint value)
      throws IllegalNullKeyException {

    if (curr.children.containsKey(key.charAt(level))) {
      if (curr.children.get(key.charAt(level)).data != null) {

        Node oldChild = curr.children.remove(key.charAt(level));
        if (oldChild.data.key.length() > level + 1 && key.length() > level + 1) {
          Node newCurr = new Node(null);
          curr.children.put(key.charAt(level), newCurr);
          newCurr.children.put(oldChild.data.key.charAt(level + 1), oldChild);
        } else if (oldChild.data.key.length() == level + 1 && key.length() > level + 1) {
          curr.children.put(key.charAt(level), oldChild);
        } else if (oldChild.data.key.length() > level + 1 && key.length() == level + 1) {
          curr.children.put(key.charAt(level), new Node(new DataPoint(key, value)));
          key = oldChild.data.key;
          value = oldChild.data;

        } else {
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

  public List<DataPoint> suggest(String key) throws IllegalNullKeyException {
    if (key == null)
      throw new IllegalNullKeyException();

    return suggest(root, key);
  }


  private List<DataPoint> suggest(Node curr, String key) {
    if(curr==null)
      return new LinkedList<>();
    if(key.length() ==0) {
      List<DataPoint> l = new LinkedList<>();
      getAll(curr, l);
      return l;
    }
    return suggest(curr.children.get(key.charAt(0)),key.substring(1));
  }
}
