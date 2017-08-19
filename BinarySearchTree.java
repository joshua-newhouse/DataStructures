public class BinarySearchTree<T extends Comparable<? super T>> {
	private BinaryNode<T> root;
	private int height;	//height of the tree

	private static class BinaryNode<T> {
		T element;			// The data in the node
		BinaryNode<T> left;	// Left child
		BinaryNode<T> right;	// Right child

		// Constructors
		BinaryNode(T theElement) {
			this(theElement, null, null);
		}

		BinaryNode(T theElement, BinaryNode<T> lt, BinaryNode<T> rt) {
			element = theElement;
			left = lt;
			right = rt;
		}
	}

	public BinarySearchTree() {
		root = null;
		height = -1;
	}

	public void makeEmpty()	{
		root = null;
	}

	public boolean isEmpty() {
		return root == null;
	}

	public boolean contains(T x) {
		return contains(x, root);
	}

	public T findMin() throws Exception {
		if(isEmpty())
			throw new Exception("UnderFlowException: findMin()");

		return findMin(root).element;
	}

	public T findMax() throws Exception {
		if(isEmpty())
			throw new Exception("UnderFlowException: findMax()");

		return findMax(root).element;
	}

	public void insert(T x) {
		root = insert(x, root);
	}

	public void remove(T x)	{
		root = remove(x, root);
	}

	public void printTree()	{
		int nTabs = 0;
		printWithTabs(root, nTabs);
	}

	private void printWithTabs(BinaryNode<T> node, int tabs) {
		if(node == null)
			return;

		int n = tabs;
		while(n-- != 0)
			System.out.print("   ");

		System.out.println(node.element.toString());
		printWithTabs(node.left, tabs + 1);
		printWithTabs(node.right, tabs + 1);
	}

	private boolean contains(T x, BinaryNode<T> n) {
		if(n == null)
			return false;

		int compareResult = x.compareTo(n.element);
		if(compareResult < 0)
			return contains(x, n.left);
		else if(compareResult > 0)
			return contains(x, n.right);
		else
			return true;	// Match
	}

	private BinaryNode<T> findMin(BinaryNode<T> n) {
		return (n != null && n.left != null) ? findMin(n.left) : n;
	}

	private BinaryNode<T> findMax(BinaryNode<T> n) {
		if(n != null)
			while(n.right != null)
				n = n.right;
		return n;
	}

	private BinaryNode<T> insert(T x, BinaryNode<T> n) {
		if(n == null)
			return new BinaryNode<T>(x, null, null);

		int compareResult = x.compareTo(n.element);
		if(compareResult < 0)
			n.left = insert(x, n.left);
		else if(compareResult > 0)
			n.right = insert(x, n.right);
		else
			;	// Duplicate; do nothing

		return n;
	}

	private BinaryNode<T> remove(T x, BinaryNode<T> n) {
		if(n == null)
			return n;	// Item not found; do nothing

		int compareResult = x.compareTo(n.element);
		if(compareResult < 0)
			n.left = remove(x, n.left);
		else if(compareResult > 0)
			n.right = remove(x, n.right);
		else if(n.left != null && n.right != null) {	// Two children
			n.element = findMin(n.right).element;
			n.right = remove(n.element, n.right);
		}
		else
			n = (n.left != null) ? n.left : n.right;

		return n;
	}

	public int size() {
		return countSize(root);
	}

	private int countSize(BinaryNode<T> n) {
		if(n == null)
			return 0;
		else
			return 1 + countSize(n.left) + countSize(n.right);
	}

	public int numLeaves() {
		return countLeaves(root);
	}

	private int countLeaves(BinaryNode<T> n) {
		if(n == null)
			return 0;
		else if(n.left == null && n.right == null)
			return 1;
		else
			return + countLeaves(n.left) + countLeaves(n.right);
	}

	public int numLeftChildren() {
		return countLeftChildren(root);
	}

	private int countLeftChildren(BinaryNode<T> n) {
		if(n == null)
			return 0;

		int num = n.left != null ? 1 : 0;
		return num + countLeftChildren(n.left) + countLeftChildren(n.right);
	}

	public boolean isPerfect() {
		height(root, -1);
		int s = size();

		return (1 << (height + 1)) - 1 == s;
	}

	/* Recursively determins the height of the tree */
	private void height(BinaryNode<T> n, int curHeight) {
		if(n == null) {
			height = curHeight > height ? curHeight : height;
			return;
		}

		height(n.left, curHeight + 1);
		height(n.right, curHeight + 1);
	}

	public void printByLevels() {
		if(root == null) {
			System.out.println("Tree is empty");
			return;
		}

		/* Array with the nodes in order of level and associated indecies */
		BinaryNode[] arrayOfNodes = new BinaryNode[size()];
		int currentIdx = 0;
		int endIdx = 0;
		arrayOfNodes[currentIdx] = root;

		/* Array to keep track of level changes for printing a newline */
		height(root, -1);
		int[] levelMarker = new int[height + 2];
		int lvlIdx = 0;

		/* Prints the nodes to stdout by level */
		do {
			System.out.print(arrayOfNodes[currentIdx].element + " ");

			if(arrayOfNodes[currentIdx].left != null)
				arrayOfNodes[++endIdx] = arrayOfNodes[currentIdx].left;

			if(arrayOfNodes[currentIdx].right != null)
				arrayOfNodes[++endIdx] = arrayOfNodes[currentIdx].right;

			if(currentIdx == levelMarker[lvlIdx]) {
				System.out.println("");
				levelMarker[++lvlIdx] = endIdx;
			}

		} while(++currentIdx <= endIdx);
	}

	public boolean isFull() {
		return isFullRecurs(root);
	}

	private boolean isFullRecurs(BinaryNode<T> n) {
		boolean hasLeft = n.left != null;
		boolean hasRight = n.right != null;

		if(!hasLeft && !hasRight)
			return true;
		else if((!hasLeft && hasRight) || (hasLeft && !hasRight))
			return false;
		else
			return isFullRecurs(n.left) && isFullRecurs(n.right);
	}

	/* Demo a tree with the added methods */
	public static void test(BinarySearchTree tr, String msg) {
		System.out.println("========================================" +
					"========================================\n");
		System.out.println(msg);

		System.out.println("The current tree structure:");
		tr.printTree();

		System.out.println("Tree size: " + tr.size());
		System.out.println("Number of Leaves: " + tr.numLeaves());
		System.out.println("Number of left children: "
									+ tr.numLeftChildren());
		System.out.println("Tree is full? " + tr.isFull());
		System.out.println("Tree is perfect? " + tr.isPerfect());
		System.out.println("Print by levels:");
		tr.printByLevels();
	}

	public static void main(String args[]) {
		System.out.println("This will test the binary tree methods on 3 " +
					"trees: tr1, tr2, tr3");

		BinarySearchTree<Integer> tr1 = new BinarySearchTree<>();
		tr1.insert(10);
		tr1.insert(7);
		tr1.insert(9);
		tr1.insert(1);
		tr1.insert(19);
		tr1.insert(13);
		tr1.insert(12);
		tr1.insert(16);
		tr1.insert(2);
		tr1.insert(4);
		tr1.insert(6);
		tr1.insert(17);
		tr1.insert(18);
		tr1.insert(14);
		tr1.insert(15);
		String msg1 = "tr1 created by inserting: 10,7,9,1,19,13,12,16,2,4" +
				",6,17,18,14,15";

		test(tr1, msg1);
		tr1 = null;
		msg1 = null;

		BinarySearchTree<Integer> tr2 = new BinarySearchTree<>();
		tr2.insert(50);
		tr2.insert(25);
		tr2.insert(75);
		tr2.insert(20);
		tr2.insert(30);
		tr2.insert(70);
		tr2.insert(80);
		String msg2 = "tr2 created by inserting: 50,25,75,20,30,70,80";

		test(tr2, msg2);
		tr2 = null;
		msg2 = null;

		BinarySearchTree<Integer> tr3 = new BinarySearchTree<>();
		tr3.insert(50);
		tr3.insert(25);
		tr3.insert(75);
		tr3.insert(20);
		tr3.insert(30);
		String msg3 = "tr3 created by inserting: 50,25,75,20,30";

		test(tr3, msg3);
		tr3 = null;
		msg3 = null;
	}
}
