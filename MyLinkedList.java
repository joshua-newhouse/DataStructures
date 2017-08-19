import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class MyLinkedList<T> implements Iterable<T> {
	private int theSize;
	private int modCount = 0;
	private Node<T> beginMarker;
	private Node<T> endMarker;

	private static class Node<T> {
		public T data;
		public Node<T> next;
		public Node<T> prev;

		public Node(T data, Node<T> next, Node<T> prev) {
			this.data = data;
			this.next = next;
			this.prev = prev;
		}
	}

	public MyLinkedList() {
		doClear();
	}

	public void clear() {
		doClear();
	}

	private void doClear() {
		beginMarker = new Node<T>(null, null, null);
		endMarker = new Node<T>(null, null, beginMarker);
		beginMarker.next = endMarker;

		theSize = 0;
		modCount++;
	}

	public int size() {
		return theSize;
	}

	public boolean isEmpty() {
		return 0 == theSize;
	}

	public void add(T x) {
		try {
			addBefore(endMarker, x);
		}
		catch(IndexOutOfBoundsException ex) {
			System.out.println(ex);
		}
	}

	public void add(int idx, T x) {
		try {
			addBefore(getNode(idx), x);
		}
		catch(IndexOutOfBoundsException ex) {
			System.out.println(ex);
		}
	}

	public T get(int idx) {
		T retVal = null;

		try {
			retVal = getNode(idx).data;
		}
		catch(IndexOutOfBoundsException ex) {
			System.out.println(ex);
		}

		return retVal;
	}

	public T set(int idx, T newVal) {
		T oldVal = null;

		try {
			Node<T> temp = getNode(idx);
			oldVal = temp.data;
			temp.data = newVal;
		}
		catch(IndexOutOfBoundsException ex) {
			System.out.println(ex);
		}

		return oldVal;
	}

	public T remove(int idx) {
		T retVal = null;

		try {
			retVal = remove(getNode(idx));
		}
		catch(IndexOutOfBoundsException ex) {
			System.out.println(ex);
		}

		return retVal;
	}

	private void addBefore(Node<T> n, T x) {
		Node<T> temp = new Node<T>(x, n, n.prev);
		n.prev = temp.prev.next = temp;

		theSize++;
		modCount++;
	}

	private void addBefore(Node<T> bNode, Node<T> aNode) {
		aNode.next = bNode;
		aNode.prev = bNode.prev;
		bNode.prev = aNode.prev.next = aNode;

		theSize++;
		modCount++;
	}

	private T remove(Node<T> n) {
		n.prev.next = n.next;
		n.next.prev = n.prev;

		theSize--;
		modCount++;

		return n.data;
	}

	private Node<T> getNode(int idx) {
		String msg = "getNode(int idx): ";
		if(idx < 0 || (idx != 0 && idx >= theSize))
			throw new IndexOutOfBoundsException(msg + "idx " + idx + 
				" out of range [0 , " + (theSize - 1) + "]");

		Node<T> temp;
		if(idx < theSize / 2) {
			temp = beginMarker;
			for(int i = 0; i <= idx; i++)
				temp = temp.next;
		}
		else {
			temp = endMarker;
			for(int i = theSize; i > idx; i--)
				temp = temp.prev;
		}

		return temp;
	}

	public java.util.Iterator<T> iterator() {
		return new LinkedListIterator();
	}

	private class LinkedListIterator implements java.util.Iterator<T> {
		private Node<T> current = beginMarker.next;
		private int expectedModCount = modCount;
		private boolean okToRemove = false;

		public boolean hasNext() {
			return current != endMarker;
		}

		public T next() {
			if(modCount != expectedModCount)
				throw new java.util.ConcurrentModificationException();
			if(!hasNext())
				throw new java.util.NoSuchElementException();

			T retVal = current.data;
			current = current.next;
			okToRemove = true;
			return retVal;
		}

		public void remove() {
			if(modCount != expectedModCount)
				throw new java.util.ConcurrentModificationException();
			if(!okToRemove)
				throw new IllegalStateException();

			MyLinkedList.this.remove(current.prev);
			expectedModCount++;
			okToRemove = false;
		}
	}

	public void swap(int idx1, int idx2) {
		if(idx1 == idx2)
			return;

		if(idx2 < idx1) {
			int temp = idx2;
			idx2 = idx1;
			idx1 = temp;
		}

		Node<T> node1 = null;
		Node<T> node2 = null;

		try {
			node1 = getNode(idx1);
			node2 = getNode(idx2);
		}
		catch(IndexOutOfBoundsException ex) {
			System.out.println(ex);
			return;
		}

		Node<T> node1Next = node1.next;
		Node<T> node2Next = node2.next;

		remove(node1);
		addBefore(node2Next, node1);

		if(node2 != node1Next) {
			remove(node2);
			addBefore(node1Next, node2);
		}
	}

	public MyLinkedList<T> reverse() {
		MyLinkedList<T> reversed = new MyLinkedList<>();
		Iterator<T> itr = this.iterator();

		while(itr.hasNext())
			reversed.add(0, itr.next());

		return reversed;		
	}

	public void erase(int idx, int nElem) {
		Node<T> node = null;
		Node<T> temp = null;

		try {
			if(idx + nElem > theSize)
				throw new IndexOutOfBoundsException(
							"erase(idx, nElem) out of bounds");
			node = getNode(idx);
		}
		catch(IndexOutOfBoundsException ex) {
			System.out.println(ex);
			return;
		}

		while(nElem > 0) {
			temp = node.next;
			this.remove(node);
			node = temp;
			nElem--;
		}
	}

	public void insertList(List<T> list, int idx) {
		Node<T> current = null;
		Node<T> temp = null;

		try {
			current = getNode(idx);
		}
		catch(IndexOutOfBoundsException ex) {
			if(idx == theSize)
				current = endMarker;
			else {
				System.out.println(ex);
				return;
			}
		}

		Iterator<T> itr = list.iterator();
		while(itr.hasNext())
			addBefore(current, itr.next());
	}

	public void shift(int places) {
		boolean fromTail = places < 0;
		places = fromTail ? -1 * places : places;

		while(places > 0) {
			int idx = fromTail ? theSize - 1 : 0;
			Node<T> current = getNode(idx);

			if(fromTail) {
				current.prev.next = endMarker;
				endMarker.prev = current.prev;

				beginMarker.next.prev = current;
				current.next = beginMarker.next;

				beginMarker.next = current;
				current.prev = beginMarker;
			}
			else {
				beginMarker.next = current.next;
				current.next.prev = beginMarker;

				current.next = endMarker;
				current.prev = endMarker.prev;

				endMarker.prev.next = current;
				endMarker.prev = current;
			}
			places--;
		}
	}

	public void printList() {
		Iterator<T> itr = this.iterator();
		while(itr.hasNext())
			System.out.print(" " + itr.next() + " ");
		System.out.println("");
	}

	public static void main(String[] args) {
		MyLinkedList<Integer> list = new MyLinkedList<>();

		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		list.add(6);
		list.add(0, 0);

		System.out.println("Swapping idx 0 and 3");
		list.printList();
		list.swap(0, 3);
		list.printList();
		list.swap(0, 3);

		System.out.println("Reversing list");
		list.printList();
		MyLinkedList<Integer> reversedList = list.reverse();
		reversedList.printList();
		reversedList = null;

		System.out.println("Erasing 2 elements starting at idx 3");
		list.printList();
		list.erase(3, 2);
		list.printList();

		list.add(3, 3);
		list.add(4, 4);

		System.out.println(
			"Inserting ArrayList containing 40, 50, 60 at idx 2");
		List<Integer> l = new ArrayList<>();
		l.add(40);
		l.add(50);
		l.add(60);
		list.printList();
		list.insertList(l, 2);
		list.printList();

		list.erase(2, 3);

		System.out.println("Shifting +4");
		list.printList();
		list.shift(4);
		list.printList();
		System.out.println("Shifting -4");
		list.shift(-4);
		list.printList();
	}
}
