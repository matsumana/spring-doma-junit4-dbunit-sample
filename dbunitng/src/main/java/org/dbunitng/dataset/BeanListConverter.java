/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbunitng.dataset;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.DataType;
import org.dbunitng.beans.BeanMetaData;
import org.dbunitng.beans.BeanMetaDataFactory;
import org.dbunitng.beans.BeanProperty;

/**
 * Beanのリストのコンバータ。
 * 
 * @author jyukutyo
 * 
 */
public class BeanListConverter {

	/** テーブルのマップ */
	private Map<String, BeanListTable> tableMap;

	/** Beanのリスト */
	private List<?> list;

	/**
	 * コンストラクタ。
	 * 
	 * @param object
	 *            オブジェクト
	 */
	public BeanListConverter(Object object) {
		List<Object> list = new ArrayList<Object>();
		list.add(object);
		this.list = list;
		tableMap = new HashMap<String, BeanListTable>();
	}

	/**
	 * コンストラクタ。
	 * 
	 * @param list
	 *            Beanのリスト
	 */
	public BeanListConverter(List<?> list) {
		this.list = list;
		tableMap = new HashMap<String, BeanListTable>();
	}

	/**
	 * データセットにコンバートする。
	 * 
	 * @return データセット
	 */
	public IDataSet convert() {

		if (list == null || list.isEmpty()) {
			return new DefaultDataSet();
		}

		addTable();
		return new DefaultDataSet(tableMap.values().toArray(
			new BeanListTable[tableMap.size()]));
	}

	/**
	 * テーブルを追加する。
	 */
	protected void addTable() {
		addTable(list);
	}

	/**
	 * デーブルを追加する。
	 * 
	 * @param property
	 *            BeanProperty
	 * @param target
	 *            プロパティを持つオブジェクト
	 */
	@SuppressWarnings("unchecked")
	protected void addTable(BeanProperty property, List<?> target) {
		Class<?> propertyClass = property.getType();
		List<Object> list = new ArrayList<Object>();
		if (Collection.class.isAssignableFrom(propertyClass)) {
			// Beanのプロパティとしてコレクションを持つ場合
			for (Object object : target) {
				Collection c = (Collection<Object>) property.getValue(object);
				if (c == null || c.isEmpty()) {
					continue;
				}
				list.addAll(c);
			}

		} else if (propertyClass.isArray()) {
			// Beanのプロパティとして配列を持つ場合
			for (Object object : target) {
				Object[] objects = (Object[]) property.getValue(object);
				if (objects == null || objects.length == 0) {
					continue;
				}
				list.addAll(Arrays.asList(objects));
			}
		} else {
			// Beanのプロパティとして単一の他のBeanがある場合
			for (Object object : target) {
				list.add(property.getValue(object));
			}
		}
		addTable(list);
	}

	/**
	 * テーブルを追加する。
	 * 
	 * @param list
	 *            Beanのリスト
	 */
	protected void addTable(List<?> list) {

		Object target = list.get(0);
		Class<?> targetClass = target.getClass();

		BeanMetaData metaData =
			BeanMetaDataFactory.getBeanMetaData(targetClass);

		List<String> names = new ArrayList<String>(metaData.getNameSet());
		List<Column> columns = new ArrayList<Column>();
		for (Iterator<String> i = names.iterator(); i.hasNext();) {
			String propertyName = (String) i.next();
			BeanProperty property = metaData.getProperty(propertyName);
			DataType type = DataTypeConverter.toDataType(property.getType());

			if (DataType.UNKNOWN == type) {
				addTable(property, list);
				continue;
			}
			Column column = new Column(propertyName, type);
			columns.add(column);
		}

		BeanListTable table =
			new BeanListTable(metaData, columns.toArray(new Column[columns
				.size()]), list);
		tableMap.put(targetClass.getSimpleName(), table);
	}
}
