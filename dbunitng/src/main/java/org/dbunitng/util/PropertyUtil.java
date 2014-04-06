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
package org.dbunitng.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.lang.StringUtils;

/**
 * プロパティ関連のユーティリティ。
 * 
 * @author jyukutyo
 * 
 */
public class PropertyUtil {

	/**
	 * Java Beans仕様のプロパティ名にする。
	 * 
	 * <pre>
	 * decapitalizePropertyName(&quot;FirstName&quot;) = &quot;firstName&quot;
	 * decapitalizePropertyName(&quot;AName&quot;) = &quot;AName&quot;
	 * </pre>
	 * 
	 * @param name
	 * @return プロパティ名
	 */
	public static String decapitalizePropertyName(String name) {
		if (StringUtils.isEmpty(name)) {
			return name;
		}
		if (name.length() > 1 && Character.isUpperCase(name.charAt(1))
			&& Character.isUpperCase(name.charAt(0))) {

			return name;
		}
		char chars[] = name.toCharArray();
		chars[0] = Character.toLowerCase(chars[0]);
		return new String(chars);
	}

	/**
	 * インスタンスフィールドかを返す。
	 * 
	 * @param field
	 * @return インスタンスフィールドならtrue
	 */
	public static boolean isInstanceField(Field field) {
		int modifier = field.getModifiers();
		return !Modifier.isStatic(modifier) && !Modifier.isFinal(modifier);
	}

	/**
	 * パブリックフィールドかを返す。
	 * 
	 * @param field
	 * @return パブリックフィールドならtrue
	 */
	public static boolean isPublicField(Field field) {
		int modifier = field.getModifiers();
		return Modifier.isPublic(modifier);
	}
}
