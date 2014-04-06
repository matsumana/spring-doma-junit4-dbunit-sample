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

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dbunit.dataset.datatype.DataType;

/**
 * DataType変換。
 * 
 * @author jyukutyo
 * 
 */
public class DataTypeConverter {

	/** クラスからDataTypeへのMap */
	private static final Map<Class<?>, DataType> toDataTypeMap;
	static {
		Map<Class<?>, DataType> m = new ConcurrentHashMap<Class<?>, DataType>();
		m.put(String.class, DataType.VARCHAR);
		m.put(short.class, DataType.TINYINT);
		m.put(Short.class, DataType.TINYINT);
		m.put(int.class, DataType.INTEGER);
		m.put(Integer.class, DataType.INTEGER);
		m.put(long.class, DataType.BIGINT);
		m.put(Long.class, DataType.BIGINT);
		m.put(float.class, DataType.REAL);
		m.put(Float.class, DataType.REAL);
		m.put(double.class, DataType.DOUBLE);
		m.put(Double.class, DataType.DOUBLE);
		m.put(boolean.class, DataType.BOOLEAN);
		m.put(Boolean.class, DataType.BOOLEAN);
		m.put(BigDecimal.class, DataType.DECIMAL);
		m.put(Timestamp.class, DataType.TIMESTAMP);
		m.put(java.sql.Date.class, DataType.DATE);
		m.put(java.util.Date.class, DataType.DATE);
		m.put(Calendar.class, DataType.TIMESTAMP);
		m.put(new byte[0].getClass(), DataType.BINARY);
		toDataTypeMap = Collections.unmodifiableMap(m);
	}
	
	/**
	 * コンストラクタ。
	 */
	protected DataTypeConverter() {
		// empty
	}

	/**
	 * DataTypeを返す。対応するDataTypeがない場合はDataType.UNKNOWNを返す。
	 * 
	 * @param clazz
	 *            クラス
	 * @return DataType
	 */
	public static DataType toDataType(Class<?> clazz) {
		DataType type = toDataTypeMap.get(clazz);
		if (type == null) {
			return DataType.UNKNOWN;
		}
		return type;
	}

}
