package de.fh_bielefeld.geograph.GUI;

import de.fh_bielefeld.geograph.GUI_INTERFACE.AVLTreeInterface;
import de.fh_bielefeld.geograph.GUI_INTERFACE.ContentHolderInterface;
import de.fh_bielefeld.geograph.GUI_INTERFACE.MapNodeInterface;

/**
 * TODO Kommt zum Parser (erst im Master Branch)
 * Bei Fragen: Stefan fragen
 */
public class AVLTree<T extends Comparable<? super T>> implements AVLTreeInterface<T> {

	public AVLNode<T>				root;
	public int						countInsertions;
	public int						countSingleRotations;
	public int						countDoubleRotations;

	private ContentHolderInterface	content;

	public AVLTree(ContentHolderInterface content) {

		root = null;
		this.content = content;
		countInsertions = 0;
		countSingleRotations = 0;
		countDoubleRotations = 0;
	}

	public int height(AVLNode<T> t) {
		return t == null ? -1 : t.height;
	}

	public int max(int a, int b) {
		if (a > b)
			return a;
		return b;
	}

	public boolean insert(T x) {
		try {
			root = insert(x, root);

			countInsertions++;
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	protected AVLNode<T> insert(T x, AVLNode<T> t) throws Exception {
		if (t == null)
			t = new AVLNode<T>(x);
		else if (x.compareTo(t.element) < 0) {
			t.left = insert(x, t.left);

			if (height(t.left) - height(t.right) == 2) {
				if (x.compareTo(t.left.element) < 0) {
					t = rotateWithLeftChild(t);
					countSingleRotations++;
				} else {
					t = doubleWithLeftChild(t);
					countDoubleRotations++;
				}
			}
		} else if (x.compareTo(t.element) > 0) {
			t.right = insert(x, t.right);

			if (height(t.right) - height(t.left) == 2)
				if (x.compareTo(t.right.element) > 0) {
					t = rotateWithRightChild(t);
					countSingleRotations++;
				} else {
					t = doubleWithRightChild(t);
					countDoubleRotations++;
				}
		} else {
			throw new Exception("Attempting to insert duplicate value");
		}

		t.height = max(height(t.left), height(t.right)) + 1;
		return t;
	}

	protected AVLNode<T> rotateWithLeftChild(AVLNode<T> k2) {
		AVLNode<T> k1 = k2.left;

		k2.left = k1.right;
		k1.right = k2;

		k2.height = max(height(k2.left), height(k2.right)) + 1;
		k1.height = max(height(k1.left), k2.height) + 1;

		return (k1);
	}

	protected AVLNode<T> doubleWithLeftChild(AVLNode<T> k3) {
		k3.left = rotateWithRightChild(k3.left);
		return rotateWithLeftChild(k3);
	}

	protected AVLNode<T> rotateWithRightChild(AVLNode<T> k1) {
		AVLNode<T> k2 = k1.right;

		k1.right = k2.left;
		k2.left = k1;

		k1.height = max(height(k1.left), height(k1.right)) + 1;
		k2.height = max(height(k2.right), k1.height) + 1;

		return (k2);
	}

	protected AVLNode<T> doubleWithRightChild(AVLNode<T> k1) {
		k1.right = rotateWithLeftChild(k1.right);
		return rotateWithRightChild(k1);
	}

	public String serializeInfix() {
		StringBuilder str = new StringBuilder();
		serializeInfix(root, str, " ");
		return str.toString();
	}

	protected void serializeInfix(AVLNode<T> t, StringBuilder str, String sep) {
		if (t != null) {
			serializeInfix(t.left, str, sep);
			str.append(t.element.toString());
			str.append(sep);
			serializeInfix(t.right, str, sep);
		}
	}

	public String serializePrefix() {
		StringBuilder str = new StringBuilder();
		serializePrefix(root, str, " ");
		return str.toString();
	}

	private void serializePrefix(AVLNode<T> t, StringBuilder str, String sep) {
		if (t != null) {
			str.append(t.element.toString());
			str.append(sep);
			serializePrefix(t.left, str, sep);
			serializePrefix(t.right, str, sep);
		}
	}

	public void sendContent() {
		sendContent(root);
	}

	public void sendContent(AVLNode<T> t) {
		if (t != null) {
			content.sendData(t);
			sendContent(t.left);
			sendContent(t.right);
		}
	}

	public void makeEmpty() {
		root = null;
	}

	public boolean isEmpty() {
		return (root == null);
	}

	public T findMin() {
		if (isEmpty())
			return null;

		return findMin(root).element;
	}

	public T findMax() {
		if (isEmpty())
			return null;
		return findMax(root).element;
	}

	private AVLNode<T> findMin(AVLNode<T> t) {
		if (t == null)
			return t;

		while (t.left != null)
			t = t.left;
		return t;
	}

	private AVLNode<T> findMax(AVLNode<T> t) {
		if (t == null)
			return t;
		while (t.right != null)
			t = t.right;
		return t;
	}

	public void remove(T x) {
		root = remove(x, root);
	}

	public AVLNode<T> remove(T x, AVLNode<T> t) {
		if (t == null) {
			return null;
		}
		if (x.compareTo(t.element) < 0) {
			t.left = remove(x, t.left);
			int l = t.left != null ? t.left.height : 0;
			if ((t.right != null) && (t.right.height - l >= 2)) {
				int rightHeight = t.right.right != null ? t.right.right.height : 0;
				int leftHeight = t.right.left != null ? t.right.left.height : 0;

				if (rightHeight >= leftHeight)
					t = rotateWithLeftChild(t);
				else
					t = doubleWithRightChild(t);
			}
		} else if (x.compareTo(t.element) > 0) {
			t.right = remove(x, t.right);
			int r = t.right != null ? t.right.height : 0;
			if ((t.left != null) && (t.left.height - r >= 2)) {
				int leftHeight = t.left.left != null ? t.left.left.height : 0;
				int rightHeight = t.left.right != null ? t.left.right.height : 0;
				if (leftHeight >= rightHeight)
					t = rotateWithRightChild(t);
				else
					t = doubleWithLeftChild(t);
			}
		} else if (t.left != null) {
			t.element = findMax(t.left).element;
			remove(t.element, t.left);
			if ((t.right != null) && (t.right.height - t.left.height >= 2)) {
				int rightHeight = t.right.right != null ? t.right.right.height : 0;
				int leftHeight = t.right.left != null ? t.right.left.height : 0;
				if (rightHeight >= leftHeight)
					t = rotateWithLeftChild(t);
				else
					t = doubleWithRightChild(t);
			}
		} else
			t = (t.left != null) ? t.left : t.right;
		if (t != null) {
			int leftHeight = t.left != null ? t.left.height : 0;
			int rightHeight = t.right != null ? t.right.height : 0;
			t.height = Math.max(leftHeight, rightHeight) + 1;
		}
		return t;
	}

	public boolean contains(T x) {
		return contains(x, root);
	}

	protected boolean contains(T x, AVLNode<T> t) {
		if (t == null) {
			return false;

		} else if (x.compareTo(t.element) < 0) {
			return contains(x, t.left);
		} else if (x.compareTo(t.element) > 0) {
			return contains(x, t.right);
		}

		return true;
	}

	public AVLNode getNodeByElement(String id) {
		return getNodeByElement(id, root);
	}

	protected AVLNode getNodeByElement(String id, AVLNode<T> t) {
		if (t == null) {
			return null;
		} else if (((MapNodeInterface) (t)).getId().compareTo(id) < 0) {
			getNodeByElement(id, t.left);
		} else if (((MapNodeInterface) (t)).getId().compareTo(id) > 0) {
			getNodeByElement(id, t.right);
		}
		return t;
	}

	public boolean checkBalanceOfTree(AVLTree.AVLNode<Integer> current) {
		boolean balancedRight = true, balancedLeft = true;
		int leftHeight = 0, rightHeight = 0;
		if (current.right != null) {
			balancedRight = checkBalanceOfTree(current.right);
			rightHeight = getDepth(current.right);
		}
		if (current.left != null) {
			balancedLeft = checkBalanceOfTree(current.left);
			leftHeight = getDepth(current.left);
		}
		return balancedLeft && balancedRight && Math.abs(leftHeight - rightHeight) < 2;
	}

	public int getDepth(AVLTree.AVLNode<Integer> n) {
		int leftHeight = 0, rightHeight = 0;
		if (n.right != null)
			rightHeight = getDepth(n.right);
		if (n.left != null)
			leftHeight = getDepth(n.left);
		return Math.max(rightHeight, leftHeight) + 1;
	}

	public boolean checkOrderingOfTree(AVLTree.AVLNode<Integer> current) {
		if (current.left != null) {
			if (current.left.element.compareTo(current.element) > 0)
				return false;
			else
				return checkOrderingOfTree(current.left);
		} else if (current.right != null) {
			if (current.right.element.compareTo(current.element) < 0)
				return false;
			else
				return checkOrderingOfTree(current.right);
		} else if (current.left == null && current.right == null)
			return true;
		return true;
	}

	public static class AVLNode<T> {

		protected T				element;
		protected AVLNode<T>	left;
		protected AVLNode<T>	right;
		protected int			height;

		public AVLNode(T theElement) {
			this(theElement, null, null);
		}

		public AVLNode(T theElement, AVLNode<T> lt, AVLNode<T> rt) {
			element = theElement;
			left = lt;
			right = rt;
		}
	}

}
