/**
 * 
 */
package com.flatironschool.javacs;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

/**
 * Implementation of a HashMap using a collection of MyLinearMap and
 * resizing when there are too many entries.
 * 
 * @author downey
 * @param <K>
 * @param <V>
 *
 */
public class MyHashMap<K, V> extends MyBetterMap<K, V> implements Map<K, V> {
	
	// average number of entries per map before we rehash
	protected static final double FACTOR = 1.0;
	private int size = 0;

	@Override
	public void clear() {
		super.clear();
		size = 0;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public V remove(Object key) {
		MyLinearMap<K,V> map = chooseMap(key);
		size -= map.size();
		V value = map.remove(key);
		size += map.size();
		return value;
	}

	@Override
	public V put(K key, V value) {
		MyLinearMap<K,V> map = chooseMap(key);
		size -= map.size();
		V oldValue = super.put(key, value);
		size += map.size();
		
		//System.out.println("Put " + key + " in " + map + " size now " + map.size());
		
		// check if the number of elements per map exceeds the threshold
		if (size() > maps.size() * FACTOR) {
			size = 0;
			rehash();
		}
		return oldValue;
	}

	/**
	 * Doubles the number of maps and rehashes the existing entries.
	 */
	protected void rehash() {
		// current number of maps
		int currSize = maps.size();
		int size = 2 * currSize;

		List<Entry> entries = new ArrayList<Entry>();

		// save old entries
		for(MyLinearMap<K,V> map: maps) {
			List<Entry> newEntries = (List)map.getEntries();
			boolean inserted = entries.addAll(newEntries);
		}

		this.makeMaps(size);

		// insert from entries into new maps
		for(Entry entry: entries) {
			K key = (K)entry.getKey();
			V value = (V)entry.getValue();
			V oldValue = put(key, value);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Map<String, Integer> map = new MyHashMap<String, Integer>();
		for (int i=0; i<10; i++) {
			map.put(new Integer(i).toString(), i);
		}
		Integer value = map.get("3");
		System.out.println(value);
	}
}
