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

import java.util.List;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.RowOutOfBoundsException;
import org.dbunitng.beans.BeanMetaData;

/**
 * Beanのリストに対応するテーブル。
 * 
 * @author jyukutyo
 * 
 */
public class BeanListTable implements ITable {

	/** Beanのリスト */
	private List<?> list;

	/** テーブルのメタデータ */
	private ITableMetaData tableMetaData;

	/** Beanのメタデータ */
	private BeanMetaData beanMetaData;

	/**
	 * コンストラクタ。
	 * 
	 * @param metaData
	 *            Beanのメタデータ
	 * @param columns
	 *            カラム
	 * @param list
	 *            Beanのリスト
	 */
	public BeanListTable(BeanMetaData metaData, Column[] columns, List<?> list) {
		if (metaData == null || columns == null || list == null) {
			throw new IllegalArgumentException(
				"BeanMetaData and Column[], List are required. BeanMetaData:"
					+ metaData + " Column[]:" + columns + " list:" + list);
		}

		tableMetaData =
			new DefaultTableMetaData(
				metaData.getTargetClass().getSimpleName(),
				columns);
		beanMetaData = metaData;
		this.list = list;
	}

	public int getRowCount() {
		return list.size();
	}

	public ITableMetaData getTableMetaData() {
		return tableMetaData;
	}

	public Object getValue(int row, String column) throws DataSetException {

		if (getRowCount() <= row) {
			throw new RowOutOfBoundsException("table size:" + getRowCount()
				+ " argument row:" + row);
		}

		Object rowObject = list.get(row);
		return beanMetaData.getProperty(column).getValue(rowObject);
	}
}
