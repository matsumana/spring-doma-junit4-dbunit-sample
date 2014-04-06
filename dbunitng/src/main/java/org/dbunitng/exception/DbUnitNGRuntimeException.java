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
package org.dbunitng.exception;

/**
 * DbUnitNG実行時例外。
 * @author jyukutyo
 *
 */
public class DbUnitNGRuntimeException extends RuntimeException {

	public DbUnitNGRuntimeException() {
		super();
	}

	public DbUnitNGRuntimeException(String message) {
		super(message);
	}

	public DbUnitNGRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public DbUnitNGRuntimeException(Throwable cause) {
		super(cause);
	}
}
