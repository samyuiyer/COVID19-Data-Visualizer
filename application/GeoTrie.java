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

import java.util.Arrays;

/**
 * A GeoTrie is a variant of Trie
 * 
 * @author ankurgarg
 *
 */
public class GeoTrie extends Trie<String, String> {

	/**
	 * private helper method to insert
	 */
	public GeoTrie() {
		Integer[] zeros = new Integer[95];
		Arrays.fill(zeros, 0);
		root = new Node(new DataPoint("Global", new String[] { "Global", "", "", "", "0", "0" }, zeros, zeros, zeros));
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
