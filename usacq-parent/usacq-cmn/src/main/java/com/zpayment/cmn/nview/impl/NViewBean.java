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
package com.zpayment.cmn.nview.impl;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zpayment.cmn.exp.BaseErrorCode;
import com.zpayment.cmn.exp.BaseException;
import com.zpayment.cmn.log.Logger;
import com.zpayment.cmn.nview.Node;
import com.zpayment.cmn.nview.intf.NView;
import com.zpayment.cmn.nview.intf.NViewExt;
import com.zpayment.cmn.persistent.jdbc.annonation.JdbcColumn;

/**
 * 
 * TODO 功能介绍
 * 
 * @author peiwang
 * @since 下午2:47:58
 * @param <T>
 */
public class NViewBean<T> extends AbsNviewBean<T> implements NView<T> {
	private static final Logger log = Logger.getLogger(NViewBean.class);

	protected volatile List<T> listData;

	protected Class<T> clazz;

	/* 缓存数据 */
	private Map<Integer, Map<NviewKey, List<T>>> indexMapData;

	private Map<Integer, List<T>> indexValueMapData;

	protected Map<String, Method> getMethods;

	private Node<NviewKey, T> root = new Node<NviewKey, T>();

	public NViewBean(int nviewFieldStyle) {
		super(nviewFieldStyle);
		indexMapData = new HashMap<Integer, Map<NviewKey, List<T>>>();
		indexValueMapData = new LinkedHashMap<Integer, List<T>>();
		nviewIndexs = new LinkedList<NviewIndexCfg>();
	}

	private NviewKey buildKey(T object, String[] columnNames) {
		List<Object> keys = new LinkedList<Object>();
		for (String columnName : columnNames) {
			Method getMethod = getMethods.get(columnName.toUpperCase());
			if (getMethod == null) {
				log.error(object.getClass().getName()
						+ " has no method for get " + columnName);
				throw new BaseException(BaseErrorCode.COMN_CACHE_TYPE_INVALID,
						new Object[] { object.getClass() });
			}
			try {
				keys.add(getMethod.invoke(object, new Object[] {}));
			} catch (Exception e) {
				throw new BaseException(BaseErrorCode.COMN_CACHE_TYPE_INVALID,
						e);
			}
		}
		return new NviewKey(keys.toArray());
	}

	private void build(NviewIndexCfg viewDefIndex) {

		if (indexMapData.containsKey(viewDefIndex.getId())) {
			throw new BaseException(BaseErrorCode.COMN_CACHE_KEY_DUPLICATE,
					new Object[] { viewDefIndex.getId() });
		}
		List<T> indexValueList = new LinkedList<T>();
		indexValueMapData.put(viewDefIndex.getId(), indexValueList);
		Map<NviewKey, List<T>> newMap = new LinkedHashMap<NviewKey, List<T>>();
		for (T pojo : listData) {
			NviewKey key = buildKey(pojo, viewDefIndex.getColumns());
			List<T> pojoList = newMap.get(key);
			if (pojoList == null) {
				pojoList = new LinkedList<T>();
				newMap.put(key, pojoList);
			}
			pojoList.add(pojo);
		}
		for (NviewKey pojoKey : newMap.keySet()) {
			T obj = newMap.get(pojoKey).get(0);
			indexValueList.add(obj);
		}
		indexMapData.put(viewDefIndex.getId(), newMap);

		log.debug("--------index[%d]--------",
				new Object[] { viewDefIndex.getId() });
		for (NviewKey viewKey : newMap.keySet()) {
			log.debug("--------key[%s][%d]--------", new Object[] { viewKey,
					viewKey.hashCode() });
			for (T obj : newMap.get(viewKey)) {
				log.debug("entity:[%s]", new Object[] { obj });
			}
		}
	}

	private void buildMap() {
		log.debug("--------build clazz[%s]--------",
				new Object[] { clazz.getSimpleName() });
		for (NviewIndexCfg viewDefIndex : nviewIndexs) {
			build(viewDefIndex);
		}
	}

	protected void initGetMethodStylePojo() {
		for (Method mthod : listData.get(0).getClass().getMethods()) {
			if (mthod.getParameterTypes().length == 0) {
				if (mthod.getName().toUpperCase().startsWith("GET")) {
					getMethods.put(mthod.getName().toUpperCase().substring(3),
							mthod);
				} else if (mthod.getName().toUpperCase().startsWith("IS")) {
					getMethods.put(mthod.getName().toUpperCase().substring(2),
							mthod);
				}
			}
		}
	}

	protected void initGetMethodStyleDb() {
		Method[] methods = listData.get(0).getClass().getMethods();
		for (Field fld : listData.get(0).getClass().getDeclaredFields()) {
			JdbcColumn anno = fld.getAnnotation(JdbcColumn.class);
			if (anno != null) {
				for (Method mthod : methods) {
					if (mthod.getName().toUpperCase().startsWith("GET")
							&& mthod.getName().toUpperCase().substring(3)
									.equals(fld.getName().toUpperCase())) {
						getMethods.put(anno.name().toUpperCase(), mthod);
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected void initGetMethod() {
		if (listData.size() <= 0) {
			log.error("listData is null,fail to initGetMethod");
			throw new BaseException(BaseErrorCode.COMN_CACHE_INIT_FAILED);
		}
		this.clazz = (Class<T>) listData.get(0).getClass();
		getMethods = new HashMap<String, Method>();
		if (this.nviewFieldStyle == NVIEW_FLD_STYLE_DB) {
			initGetMethodStyleDb();
		} else {
			initGetMethodStylePojo();
		}
	}

	@Override
	public void load(List<T> listData, NViewExt<T> extOpr) {
		this.listData = listData;
		initGetMethod();
		if (nviewType >= 1) {
			buildMap();
		}
		if (nviewType >= 2) {
			buildTree();
		}
	}

	@Override
	public List<T> getAll() {
		// TODO Auto-generated method stub
		return listData;
	}

	@Override
	public T getOneByIndexUsingArgs(int index, Object... args) {
		List<T> tmp = getListByIndexUsingArgs(index, args);
		return (tmp == null) ? null : tmp.get(0);
	}

	@Override
	public T getOneByIndexUsingKey(int index, NviewKey viewKey) {
		List<T> tmp = getListByIndexUsingKey(index, viewKey);
		return (tmp == null) ? null : tmp.get(0);
	}

	private NviewKey getParentNodeId(T row) {
		return buildKey(row, relatedColumns);
	}

	private NviewKey getNodeId(T row) {
		return buildKey(row, keyColumns);
	}

	private void buildTree() {
		root.removeChildren();

		if (listData == null || listData.isEmpty()) {
			return;
		}

		Map<NviewKey, Node<NviewKey, T>> nodeMap = new LinkedHashMap<NviewKey, Node<NviewKey, T>>();
		for (T row : listData) {
			Node<NviewKey, T> node = new Node<NviewKey, T>(getNodeId(row), row);
			nodeMap.put(node.getKey(), node);
		}

		// 组织树结构
		for (Iterator<Node<NviewKey, T>> itr = nodeMap.values().iterator(); itr
				.hasNext();) {
			Node<NviewKey, T> node = itr.next();
			NviewKey parentNodeId = getParentNodeId(node.getCurrent());
			Node<NviewKey, T> parent = nodeMap.get(parentNodeId);
			if (parent != null) {
				parent.addChild(node);
			}
		}

		// 将顶层节点数据添加到根节点中
		for (Iterator<Node<NviewKey, T>> itr = nodeMap.values().iterator(); itr
				.hasNext();) {
			Node<NviewKey, T> node = itr.next();
			if (node.getParent() == null) {
				root.addChild(node);
			}
		}
		if (log.isDebugEnabled()) {
			debugNode(root, "root");
		}
	}

	private void debugNode(Node<NviewKey, T> node, String deep) {
		if (node.getChildren() != null) {
			int idx = 1;
			for (Node<NviewKey, T> child : node.getChildren()) {
				String prefix = String.format("%-12s", deep.trim() + "." + idx);
				log.debug("%s key[%s]:value[%s]", prefix, child.getKey(),
						child.getCurrent());
				debugNode(child, prefix);
				idx++;
			}
		}
	}

	@Override
	public List<T> getListByIndexUsingKey(int index, NviewKey viewKey) {
		if (indexMapData.containsKey(index)) {
			return indexMapData.get(index).get(viewKey);
		}
		return null;
	}

	@Override
	public List<T> getListByIndexUsingArgs(int index, Object... args) {
		if (index == 0) {
			return listData;
		}
		if (indexMapData.containsKey(index)) {
			NviewKey key = new NviewKey(args);
			return indexMapData.get(index).get(key);
		}
		return null;
	}

	@Override
	public List<T> getIndexValueListByIndex(int index) {
		return indexValueMapData.get(index);
	}

	@Override
	public List<NviewKey> getIndexValueListByIndexUsingCond(int index,
			Object[] args) {
		List<NviewKey> matchKeys = new LinkedList<NviewKey>();
		Set<NviewKey> tmpKeys = indexMapData.get(index).keySet();
		NviewKey newKey = new NviewKey(args);
		for (NviewKey tmpKey : tmpKeys) {
			if (tmpKey.match(newKey)) {
				matchKeys.add(tmpKey);
			}
		}
		return matchKeys;
	}

	@Override
	public Node<NviewKey, T> getRoot() {
		if (nviewType < NVIEW_KEY_TREE) {
			throw new BaseException(BaseErrorCode.COMN_CACHE_TYPE_INVALID,
					new Object[] { nviewType });
		}
		return root;
	}
}
