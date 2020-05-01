///////////////////////////////////////////////////////////////////////////////
//
// 	Title: ateam_final_project
// 	Author: Ankur Garg, Eric Ertl, Justin Chan, Samyu Iyer, Sudeep Reddy, 
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

import java.util.LinkedList;
import java.util.List;
import application.Trie.Node;

/**
 * AlphaTrie is a variant of Trie
 * 
 * @author ankurgarg
 *
 */
public class AlphaTrie extends Trie<String, Character> {

	/**
	 * Constructor method for AlphaTrie
	 */
	public AlphaTrie() {
		root = new Node(null);
	}

	/**
	 * Insert method to insert a (key, value) into the tree
	 * 
	 * @param key   - String key
	 * @param value - DataPoint value
	 */
	public void insert(String key, DataPoint value) throws IllegalNullKeyException {
		if (key == null)
			throw new IllegalNullKeyException();
		if (root.data != null)
			root.data.increment(value);
		insert(0, root, key, value);
	}

	/**
	 * Protected Helper method to recursively insert into the AlphaTrie
	 * 
	 * @param level
	 * @param curr
	 * @param key
	 * @param value
	 * @throws IllegalNullKeyException - if the key is null
	 */
	protected void insert(int level, Node curr, String key, DataPoint value) throws IllegalNullKeyException {
		// TODO: is null key exception ever thrown?

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

	/**
	 * Suggest method
	 * 
	 * @param key
	 * @return
	 * @throws IllegalNullKeyException - if the key is null
	 */
	public List<DataPoint> suggest(String key) throws IllegalNullKeyException {
		if (key == null)
			throw new IllegalNullKeyException();

		return suggest(root, key);
	}

	/**
	 * Suggest method
	 * 
	 * @param curr
	 * @param key
	 * @return
	 */
	private List<DataPoint> suggest(Node curr, String key) {
		if (curr == null)
			return new LinkedList<>();
		if (key.length() == 0) {
			List<DataPoint> l = new LinkedList<>();
			getAll(curr, l);
			return l;
		}
		return suggest(curr.children.get(key.charAt(0)), key.substring(1));
	}

}
