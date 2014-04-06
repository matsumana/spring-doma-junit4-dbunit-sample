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
package org.dbunitng.beans;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * クラスのメタ情報ファクトリ。
 * 
 * @author jyukutyo
 * 
 */
public class BeanMetaDataFactory {

	/** メタ情報のキャッシュ */
	private static final Map<Class<? extends Object>, BeanMetaData> cache =
		new ConcurrentHashMap<Class<? extends Object>, BeanMetaData>();

	/**
	 * コンストラクタ。
	 */
	protected BeanMetaDataFactory() {
		// empty
	}

	/**
	 * {@link BeanMetaData}を返す。
	 * 
	 * @param clazz
	 *            メタ情報を取得したいクラス
	 * @return BeanMetaData
	 */
	public static BeanMetaData getBeanMetaData(Class<? extends Object> clazz) {

		BeanMetaData beanMetaData = (BeanMetaData) cache.get(clazz);
		if (beanMetaData == null) {
			beanMetaData = new BeanMetaData(clazz);

			cache.put(clazz, beanMetaData);
		}
		return beanMetaData;

	}

}
