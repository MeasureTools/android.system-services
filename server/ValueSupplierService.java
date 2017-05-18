/**
 * Copyright 2016 Daniel "Dadie" Korner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 * Unless required by applicable law or agreed to in writing, software
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package com.android.server;

import android.os.IValueSupplierService;

abstract public class ValueSupplierService extends IValueSupplierService.Stub {

	private final String name;

	protected ValueSupplierService(String name) {
		this.name = name;
	}

	public String name() {
		return this.name;
	}

	public abstract byte[] value();
	public abstract int    size();
	public abstract String type();
	public abstract String unit();
}

