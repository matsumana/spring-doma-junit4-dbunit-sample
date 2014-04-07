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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lombok.ToString;
import org.dbunitng.exception.DbUnitNGRuntimeException;

/**
 * Beanのプロパティ。
 * 
 * @author jyukutyo
 * 
 */
@ToString
public class BeanProperty {

	/** 引数なしを表す定数 */
	private static final Object[] NO_ARGS = new Object[0];

	/** プロパティ名 */
	private String name;

	/** プロパティの型 */
	private Class<?> type;

	/** プロパティのフィールド */
	private Field field;

	/** プロパティのGetter */
	private Method getter;

	/** プロパティのSetter */
	private Method setter;

	/**
	 * コンストラクタ。
	 * 
	 * @param n
	 *            プロパティ名
	 * @param t
	 *            プロパティの型
	 * @param f
	 *            プロパティのフィールド
	 * @param g
	 *            プロパティのGetter
	 * @param s
	 *            プロパティのSetter
	 */
	public BeanProperty(String n, Class<?> t, Field f, Method g, Method s) {

		if (n == null || t == null) {
			throw new IllegalArgumentException(
				"Name, type are all required. name:" + n + " type:" + t);
		}
		name = n;
		type = t;
		field = f;
		getter = g;
		setter = s;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public void setGetter(Method getter) {
		this.getter = getter;
	}

	public void setSetter(Method setter) {
		this.setter = setter;
	}

	public String getName() {
		return name;
	}

	public Class<?> getType() {
		return type;
	}

	public Field getField() {
		return field;
	}

	public Method getGetter() {
		return getter;
	}

	public Method getSetter() {
		return setter;
	}

	/**
	 * Getterを持っているか。
	 * 
	 * @return Getterを持っているならtrue
	 */
	protected boolean hasGetter() {
		return getter != null;
	}

	/**
	 * プロパティの値を返す。
	 * 
	 * @param target
	 *            プロパティを持つオブジェクト
	 * @return プロパティの値
	 */
	public Object getValue(Object target) {
		try {
			if (hasGetter()) {
				return getter.invoke(target, NO_ARGS);
			}
			return field.get(target);
		} catch (IllegalArgumentException e) {
			throw new DbUnitNGRuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new DbUnitNGRuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new DbUnitNGRuntimeException(e);
		}
	}
}
