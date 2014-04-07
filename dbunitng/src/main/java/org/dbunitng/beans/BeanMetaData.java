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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.google.common.base.CaseFormat;
import lombok.ToString;
import org.dbunitng.util.PropertyUtil;

/**
 * クラスのメタ情報。
 * 
 * @author jyukutyo
 * 
 */
@ToString
public class BeanMetaData {

	/** メタ情報の対象クラス */
	private Class<?> targetClass;

	/** プロパティへのMap */
	private Map<String, BeanProperty> propertyMap;

	/**
	 * コンストラクタ。プロパティのメタ情報を抽出する。
	 * 
	 * @param targetClass
	 *            メタ情報を抽出するクラス。
	 */
	public BeanMetaData(Class<?> targetClass) {
		this.targetClass = targetClass;
		propertyMap = new HashMap<String, BeanProperty>();

		retrievePropertiesByMethod();

		retrievePropertiesByField();
	}

	/**
	 * フィールド名からプロパティ情報を抽出する。
	 */
	protected void retrievePropertiesByField() {
		Field[] fields = targetClass.getFields();

		for (int i = 0; i < fields.length; ++i) {

			Field field = fields[i];
			field.setAccessible(true);

			if (PropertyUtil.isInstanceField(field)) {
				String fname = field.getName();
				BeanProperty property = propertyMap.get(fname);
				if (property == null) {
					property =
						new BeanProperty(
							fname,
							field.getType(),
							field,
							null,
							null);
                    propertyMap.put(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fname), property);
				} else if (PropertyUtil.isPublicField(field)) {
					property.setField(field);
				}
			}
		}
	}

	/**
	 * メソッド名からプロパティ情報を抽出する。
	 */
	protected void retrievePropertiesByMethod() {
		Method[] methods = targetClass.getMethods();

		for (int i = 0; i < methods.length; i++) {

			Method method = methods[i];
			String methodName = method.getName();

			if (methodName.startsWith("get")) {

				if (method.getParameterTypes().length != 0
					|| methodName.equals("getClass")
					|| method.getReturnType() == void.class) {
					// 引数を取るものや戻り値の型がvoid、getClass()は getterメソッドではないので追加しない
					continue;
				}

				// メソッド名が「get」で始まるので4文字目以降の文字列をプロパティ名とする
				String propertyName =
					PropertyUtil.decapitalizePropertyName(methodName
						.substring(3));

				addPropertyWithGetter(propertyName, method);

			} else if (methodName.startsWith("is")) {

				Class<?> type = method.getReturnType();

				if (method.getParameterTypes().length != 0
					|| !type.equals(Boolean.TYPE)
					&& !type.equals(Boolean.class)) {
					// 引数を取るものや戻り値の型がbooleanまたはBooleanでないものは追加しない
					continue;
				}

				// メソッド目以外「is」で始まるので3文字目以降の文字列をプロパティ名とする
				String propertyName =
					PropertyUtil.decapitalizePropertyName(methodName
						.substring(2));

				addPropertyWithGetter(propertyName, method);

			} else if (methodName.startsWith("set")) {

				if (method.getParameterTypes().length != 1
					|| methodName.equals("setClass")
					|| method.getReturnType() != void.class) {
					continue;
				}

				String propertyName =
					PropertyUtil.decapitalizePropertyName(methodName
						.substring(3));

				addPropertyWithSetter(propertyName, method);
			}
		}
	}

	/**
	 * クラスを返す。
	 * 
	 * @return クラス
	 */
	public Class<?> getTargetClass() {
		return targetClass;
	}

	/**
	 * プロパティを追加する。
	 * 
	 * @param name
	 *            プロパティ名
	 * @param setter
	 *            Setterメソッド
	 */
	protected void addPropertyWithSetter(String name, Method setter) {
		String lowerName = name.toLowerCase();
		BeanProperty property = propertyMap.get(lowerName);
		Class<?> type = setter.getParameterTypes()[0];
		if (property == null) {
			property = new BeanProperty(name, type, null, null, setter);
            propertyMap.put(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name), property);
		} else {
			property.setSetter(setter);
		}
	}

	/**
	 * プロパティを追加する。
	 * 
	 * @param name
	 *            プロパティ名
	 * @param getter
	 *            Getterメソッド
	 */
	protected void addPropertyWithGetter(String name, Method getter) {
		String lowerName = name.toLowerCase();
		BeanProperty property = propertyMap.get(lowerName);
		Class<?> type = getter.getReturnType();
		if (property == null) {
			property = new BeanProperty(name, type, null, getter, null);
            propertyMap.put(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name), property);
		} else {
			property.setGetter(getter);
		}
	}

	/**
	 * プロパティ名のSetを返す。
	 * 
	 * @return プロパティ名のSet
	 */
	public Set<String> getNameSet() {
		return propertyMap.keySet();
	}

	/**
	 * {@link BeanProperty} を返す。
	 * 
	 * @param name
	 *            プロパティ名
	 * @return BeanProperty
	 */
	public BeanProperty getProperty(String name) {
		if (name == null) {
			return null;
		}
		return propertyMap.get(name.toLowerCase());
	}
}
