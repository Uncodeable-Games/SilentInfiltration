package de.mih.core.engine.navigation;


public class Node<T>
{
	Node<T> left, right;//, parent;
	float key;
	T value;
	
	public Node<T> getLeft() {
		return left;
	}
	public void setLeft(Node<T> left) {
		this.left = left;
	}
	public Node<T> getRight() {
		return right;
	}
	public void setRight(Node<T> right) {
		this.right = right;
	}
//	public Node<T> getParent() {
//		return parent;
//	}
//	public void setParent(Node<T> parent) {
//		this.parent = parent;
//	}
	public float getKey() {
		return key;
	}
	public void setKey(float key) {
		this.key = key;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}
}