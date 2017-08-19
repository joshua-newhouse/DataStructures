/* Joshua Newhouse | jbn160130 | CS3345-0U1 | Project 4 */
public class LinearProbingHashTable<K, V> {
	private static class Entry<K, V> {
		K key;
		V value;
		boolean isActive;

		Entry(K key, V value) {
			this.key = key;
			this.value = value;
			this.isActive = true;
		}

		public String toString() {
			return "Key: " + key.toString() + " Value: " +
										value.toString();
		}
	}

	private Entry<K, V>[] table;
	private int currentSize;
	
	public LinearProbingHashTable(int size) {
		size = size < 10 ? 11 : size * 2 + 1;
		table = new Entry[size];
		currentSize = 0;
	}

	public int getHashLocation(K k) {
		int hash = k.hashCode();
		return hash < 0 ? (hash * -1) % table.length : hash % table.length;
	}

	public int getProbingLocation(K k) {
		int i = getHashLocation(k);
		for(int counter = 0;
					table[i] != null && table[i].isActive;
											counter++) {
			if(counter > table.length - 1) {
				i = -1;
				break;
			}

			i = i < table.length - 1 ? i + 1 : 0;
		}

		return i;
	}

	private Entry<K, V> findEntry(K k) {
		Entry<K, V> retVal = null;

		int i = getHashLocation(k);
		if(i < 0)
			throw new IndexOutOfBoundsException("Negative hash value");

		while(table[i] != null) {

			if(table[i].key.equals(k)) {
				retVal = table[i];
				break;
			}

			i = i < table.length - 1 ? i + 1 : 0;
		}

		return retVal;
	}

	public V find(K k) {
		V retVal = null;
		Entry<K, V> temp = findEntry(k);

		if(temp == null)
			retVal = null;
		else if(!temp.isActive)
			retVal = null;
		else
			retVal = temp.value;

		return retVal;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder("Table size " + table.length
												+ '\n');
		for(int i = 0; i < table.length; i++)
			sb.append(String.valueOf(i) + ' '
				+ (table[i] != null && table[i].isActive ?
								table[i].toString() : "null")
				+ '\n');

		return sb.toString();
	}

	public boolean insert(K k, V v) {
		boolean retVal;

		if(find(k) != null) {
			System.out.println("Key: " + k.toString() +
										" already in use");
			retVal = false;
		}
		else {
			int idx = getProbingLocation(k);
			if(idx < 0)
				retVal = false;
			else {
				table[idx] = new Entry<K, V>(k, v);
				currentSize++;
				retVal = true;
				if(currentSize > table.length / 2)
					this.rehash();
			}
		}

		return retVal;
	}

	public boolean delete(K k) {
		boolean retVal = false;

		Entry<K, V> temp = findEntry(k);
		if(temp != null) {
			temp.isActive = false;
			currentSize--;
			retVal = true;
		}

		return retVal;
	}

	public void rehash() {
		LinearProbingHashTable<K, V> temp =
				new LinearProbingHashTable<>(this.table.length);

		for(int i = 0; i < table.length; i++) {
			Entry<K, V> e = this.table[i];
			if(e != null && e.isActive)
				temp.insert(e.key, e.value);
		}

		this.table = null;
		this.table = temp.table;
		this.currentSize = temp.currentSize;
		temp = null;
	}

	public static void main(String[] args) {
/*		LinearProbingHashTable<String, Integer> ht =
								new LinearProbingHashTable<>(5);

		System.out.println("Inserting the following key/value pairs: " +
			"{\"first\", 10}, {\"second\", 20}, {\"third\", 30}, " +
			"{\"fourth\", 40}, {\"fifth\", 50}");
		ht.insert("first", 10);
		ht.insert("second", 20);
		ht.insert("third", 30);
		ht.insert("fourth", 40);
		ht.insert("fifth", 50);

		System.out.println(ht.toString());

		ht.delete("fourth");
		System.out.println("Deleted key: fourth");
		System.out.println(ht.toString());

		System.out.println("Find value for key: fifth");
		System.out.println(ht.find("fifth"));

		System.out.println("Hash location for key: twentieth");
		System.out.println(ht.getHashLocation("twentieth"));

		System.out.println("Probing location for key: twentieth");
		System.out.println(ht.getProbingLocation("twentieth"));

		System.out.println("Inserting the following key/value pairs to " +
			"force a rehash: {\"twentieth\", 200}, {\"eleventh\", 110}");
		ht.insert("twentieth", 200);
		ht.insert("eleventh", 110);

		System.out.println(ht.toString());
*/
		// table size = 10;
        LinearProbingHashTable taTest = new LinearProbingHashTable<>(10);

        System.out.println(" A. Insert");

        System.out.println("New entry insert 89: true ? " + taTest.insert(89, 89));
        System.out.println("Duplicate entry: false ? " + taTest.insert(89, 89));
        //System.out.println("null key insertion ? " + taTest.insert(null, 89));

        System.out.println("insert 18: " + taTest.insert(18, 18));
        System.out.println("insert 49: " + taTest.insert(49, 49));

        System.out.println("insert 58: " + taTest.insert(58, 58));
        System.out.println("insert 69: " + taTest.insert(69, 69));

        System.out.println("insert 27: " + taTest.insert(27, 27));
        System.out.println("insert 77: " + taTest.insert(77, 77));

        System.out.println(" Check whether rehash method is called and works in insert method.");
        
        System.out.println(" After insertion : G. toString : index   key  format");
        System.out.println(taTest.toString());

        System.out.println(" B. Find");
        System.out.println("Key for 89: 89   ?" + taTest.find(89));
        System.out.println("Key for 58: 58   ?" + taTest.find(58));
        System.out.println("Key for 38: null ? " + taTest.find(38));

        System.out.println(" C. Delete");
        System.out.println("Delete Key for 27 : true  ? " + taTest.delete(27));
        System.out.println("Delete Ket for 28 : false ? " + taTest.delete(28));
        System.out.println("After deletion: the entry for key 27 should be null");
        System.out.println(taTest.toString());

        System.out.println(" D. Rehash : double the table size ??");
        System.out.println("Before rehash: " + taTest.toString());
        System.out.println("Before rehash: tableSize " + taTest.table.length);
        taTest.rehash();
        System.out.println("After rehash: " + taTest.toString());
        System.out.println("After rehash: tableSize " + taTest.table.length);


        System.out.println(" E. Hash Location");
        System.out.println("Get hash location for 89 : " + taTest.getHashLocation(89));
        System.out.println("Get hash location for 49 : " + taTest.getHashLocation(49));
        System.out.println("Get hash location for 50 : -1 ? " + taTest.getHashLocation(50));

        System.out.println(" F. Probing Location");
        System.out.println("Get probing location for 89 : " + taTest.getProbingLocation(89));
        System.out.println("Get probing location for 49 : " + taTest.getProbingLocation(49));
        System.out.println("Get probing location for 50 : -1 ? " + taTest.getProbingLocation(50));

	}
}
