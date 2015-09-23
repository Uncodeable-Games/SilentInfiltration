package de.mih.core.engine.navigation;

import com.badlogic.gdx.math.MathUtils;

public class BalancedBinaryTree<T>
{

	Node<T> root;

	public void setRoot(Node<T> node)
	{
		this.root = node;
	}

	public void printL()
	{
		System.out.println("printL");
		printL(root);
		System.out.println();
	}

	private void printL(Node<T> root)
	{
		if (root == null)
			return;
		System.out.print("(" + root.getKey() + ":");
		printL(root.getLeft());
		System.out.print(", ");
		printL(root.getRight());
		System.out.print(" )");
	}

	public void insert(float key, T value)
	{
		Node<T> node = new Node<>();
		node.setKey(key);
		node.setValue(value);
		insert(root, node);
	}

	public void insert(Node<T> node)
	{
		insert(root, node);
	}

	private void insert(Node<T> root, Node<T> node)
	{
		if (this.root == null)
		{
			this.root = node;
			return;
		}
		if (node.getKey() <= root.getKey())
		{
			Node<T> left = root.getLeft();
			if (left == null)
			{
				root.setLeft(node);
				// node.setParent(root);
			}
			else
			{
				insert(left, node);
			}
		}
		else
		{
			Node<T> right = root.getRight();
			if (right == null)
			{
				root.setRight(node);
				// node.setParent(root);
			}
			else
			{
				insert(right, node);
			}
		}
	}

	public void remove(float key)
	{
		this.root = remove(root, key);
	}

	public void remove(Node<T> node)
	{
		this.root = remove(root, node);
	}
	private Node<T> remove(Node<T> node, Node<T> delete)
	{
		// if(root.getLeft() != node && root.getLeft() != node && root != node)
		// {
		if (node == null)
			return node;
		if (delete.key <= node.getKey() && !delete.equals(node))
		{
			node.setLeft(remove(node.getLeft(), delete.key));
		}
		else if (delete.key > node.getKey())
		{
			node.setRight(remove(node.getRight(), delete.key));
		}
		else
		{
			// }
			Node<T> left = node.getLeft();
			Node<T> right = node.getRight();
			if (left == null && right == null) // no childs
			{
				return null;
			}
			else if (left == null && right != null) // one child
			{
				return node.getRight();
				// if(node == parent.getLeft())
				// parent.setLeft(node.getRight());
				// else
				// parent.setRight(node.getRight());
			}
			else if (left != null && right == null)
			{
				return node.getLeft();
				// if(node == parent.getLeft())
				// parent.setLeft(node.getLeft());
				// else
				// parent.setRight(node.getLeft());
			}
			else // two childs
			{
				Node<T> tmp = getMax(left);
				//printL(left);
				left = remove(left, tmp);
				//printL(left);
				tmp.setLeft(left);
				tmp.setRight(right);
//				if (node == this.root)
//					this.root = tmp;
				return tmp;
			}
		}
		return node;
	}
	private Node<T> remove(Node<T> node, float key)
	{
		// if(root.getLeft() != node && root.getLeft() != node && root != node)
		// {
		if (node == null)
			return node;
		if (key < node.getKey())
		{
			node.setLeft(remove(node.getLeft(), key));
		}
		else if (key > node.getKey())
		{
			node.setRight(remove(node.getRight(), key));
		}
		else
		{
			// }
			Node<T> left = node.getLeft();
			Node<T> right = node.getRight();
			if (left == null && right == null) // no childs
			{
				return null;
			}
			else if (left == null && right != null) // one child
			{
				return node.getRight();
				// if(node == parent.getLeft())
				// parent.setLeft(node.getRight());
				// else
				// parent.setRight(node.getRight());
			}
			else if (left != null && right == null)
			{
				return node.getLeft();
				// if(node == parent.getLeft())
				// parent.setLeft(node.getLeft());
				// else
				// parent.setRight(node.getLeft());
			}
			else // two childs
			{
				Node<T> tmp = getMax(left);
				//printL(left);
				left = remove(left, tmp.key);
				//printL(left);
				tmp.setLeft(left);
				tmp.setRight(right);
//				if (node == this.root)
//					this.root = tmp;
				return tmp;
			}
		}
		return node;
	}

	public Node<T> getMax()
	{
		return getMax(root);
	}

	private Node<T> getMax(Node<T> root)
	{
		Node<T> right = root.getRight();
		Node<T> left = root.getLeft();
		if (right != null)
			return getMax(right);
		else if (left != null)
			return getMax(left);
		return root;
	}

	public Node<T> getMin()
	{
		return getMin(root);
	}

	private Node<T> getMin(Node<T> root)
	{
		if (root == null)
			return null;
		Node<T> left = root.getLeft();
		if (left != null)
			return getMin(left);
		return root;
	}

	public Node<T> getMostLeft()
	{
		return getMostLeft(root);
	}

	private Node<T> getMostLeft(Node<T> root)
	{
		if (root == null)
			return null;
		Node<T> left = root.getLeft();
		if (left != null)
			return getMostLeft(left);
		return root;
	}

	public Node<T> searchNode(float key)
	{
		return searchNode(key, root);
	}

	private Node<T> searchNode(float key, Node<T> root)
	{
		if (root == null)
		{
			return null;
		}
		if (key < root.getKey())
		{
			return searchNode(key, root.getLeft());
		}
		else if (key > root.getKey())
		{
			return searchNode(key, root.getRight());
		}
		return root;
	}

	public void balanceTree()
	{
		Node<T> pseudoRoot = new Node<T>();
		pseudoRoot.setKey(-1);
		pseudoRoot.setRight(root);
		int size = convertToVine(pseudoRoot);
		convertToTree(pseudoRoot, size);
		this.root = pseudoRoot.getRight();
		pseudoRoot = null;
	}

	private int convertToVine(Node<T> root)
	{
		Node<T> tail = root;
		Node<T> rest = tail.right;
		int size = 0;
		while (rest != null)
		{
			if (rest.getLeft() == null)
			{
				tail = rest;
				rest = rest.getRight();
				size++;
			}
			else
			{
				Node<T> tmp = rest.getLeft();
				rest.setLeft(tmp.getRight());
				tmp.setRight(rest);
				rest = tmp;
				tail.setRight(tmp);
			}
		}
		return size;
	}

	private void convertToTree(Node<T> root, int size)
	{
		int leaves = (int) (size + 1 - Math.pow(2, Math.floor(MathUtils.log2(size + 1))));
		compress(root, leaves);
		size -= leaves;
		while (size > 1)
		{
			compress(root, Math.floorDiv(size, 2));
			size = Math.floorDiv(size, 2);
		}

	}

	private void compress(Node<T> root, int count)
	{
		Node<T> scanner = root;
		for (int i = 0; i < count; i++)
		{
			Node<T> child = scanner.getRight();
			scanner.setRight(child.getRight());
			scanner = scanner.getRight();
			child.setRight(scanner.getLeft());
			scanner.setLeft(child);
		}
	}

	public boolean isBalanced()
	{
		return isBalanced(root);
	}

	private boolean isBalanced(Node<T> root)
	{
		int heightLeft, heightRight;
		if (root == null)
			return true;
		Node<T> left = root.getLeft();
		Node<T> right = root.getRight();
		heightLeft = heightOf(left);
		heightRight = heightOf(right);

		if (Math.abs(heightLeft - heightRight) <= 1 && isBalanced(left) && isBalanced(right))
		{
			return true;
		}

		return false;
	}

	private int heightOf(Node<T> node)
	{
		if (node == null)
		{
			return 0;
		}
		return 1 + Math.max(heightOf(node.getLeft()), heightOf(node.getRight()));
	}

}
