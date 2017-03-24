/*
 * 
 * Copyright 2017, ZPayment Co., Ltd. All right reserved.
 * 
 * THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF ZPayment CO., LTD. THE CONTENTS OF THIS FILE MAY NOT BE
 * DISCLOSED TO THIRD PARTIES, COPIED OR DUPLICATED IN ANY FORM, IN WHOLE OR IN PART, WITHOUT THE PRIOR WRITTEN
 * PERMISSION OF ZPayment CO., LTD.
 * 
 * 2017年3月24日 - Create By peiwang
 */
package com.zpayment.cmn.nview;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.zpayment.cmn.Const;

/**
 * 通用树节点
 * 
 * @author peiwang
 * @version
 * @since
 * 
 */
public class Node<K, T> implements Serializable {

	private static final long serialVersionUID = Const.VERSION_ID;

	/** 假节点，无实际数据 */
	protected boolean dummy;

	/** 当前节点数据 */
	protected T current;

	/** id */
	protected K key;

	/** 子节点 */
	protected List<Node<K, T>> children;

	/** 父节点 */
	protected Node<K, T> parent;

	protected boolean authorized = true;

	public Node() {
		this.dummy = true;
	}

	public Node(K key, T current) {
		this.dummy = false;
		this.current = current;
		this.key = key;
	}

	/**
	 * 添加子节点
	 * 
	 * @since
	 * @param child
	 */
	public void addChild(Node<K, T> child) {
		if (children == null) {
			children = new LinkedList<Node<K, T>>();
		}

		children.add(child);
		child.parent = this;
	}

	/**
	 * 获取当前节点的数据
	 * 
	 * @since
	 * @return
	 */
	public T getCurrent() {
		return current;
	}

	/**
	 * 清空孩子节点
	 * 
	 * @since
	 */
	public void removeChildren() {
		if (children != null) {
			children.clear();
		}
	}

	/**
	 * 根据ID查找结点
	 * 
	 * @since
	 * @param nodeKey
	 * @return
	 */
	public Node<K, T> search(K nodeKey) {
		if (match(this, nodeKey)) {
			return this;
		}

		if (children == null) {
			return null;
		}

		for (Node<K, T> child : children) {
			Node<K, T> target = child.search(nodeKey);
			if (target != null) {
				return target;
			}
		}
		return null;
	}

	/**
	 * 判断是否匹配
	 * 
	 * @since
	 * @param node
	 * @param key
	 * @return
	 */
	private boolean match(Node<K, T> node, K key) {
		K k = node.getKey();
		if (k == null || key == null) {
			return false;
		}
		return k.equals(key);
	}

	/**
	 * 判断是否叶子结点
	 * 
	 * @since
	 * @return
	 */
	public boolean isLeaf() {
		return children == null || children.isEmpty();
	}

	public void setCurrent(T current) {
		this.current = current;
	}

	public List<Node<K, T>> getChildren() {
		return children;
	}

	public void setChildren(List<Node<K, T>> children) {
		this.children = children;
	}

	public Node<K, T> getParent() {
		return parent;
	}

	public void setParent(Node<K, T> parent) {
		this.parent = parent;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public boolean isDummy() {
		return dummy;
	}

	public void setDummy(boolean dummy) {
		this.dummy = dummy;
	}

	/**
	 * @return the authorized
	 */
	public boolean isAuthorized() {
		return authorized;
	}

	/**
	 * @param authorized
	 *            the authorized to set
	 */
	public void setAuthorized(boolean authorized) {
		this.authorized = authorized;
	}
}
