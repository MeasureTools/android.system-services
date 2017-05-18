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

import java.util.HashMap;
import java.util.Map;
import java.lang.RuntimeException;
import android.os.IValueSupplierService;
import android.os.IValueSupplierManager;
import com.android.server.ValueSupplierService;

import com.android.server.valuesupplier.odroidxu3.*;

public class ValueSupplierManager extends IValueSupplierManager.Stub {

	private Map<String, ValueSupplierService> suppliers;

	protected ValueSupplierManager() {
		this.suppliers = new HashMap<String, ValueSupplierService>(50);
		{
			ValueSupplierService supplier;
			for (int i = 0; i < 7; i++) {
				supplier = new CPUnFrequencySupplierService(i);
				this.suppliers.put(supplier.name(), supplier);
				supplier = new CPUnUsageSupplierService(i);
				this.suppliers.put(supplier.name(), supplier);
			}
			supplier = new CPUTemperatureSupplierService();
			this.suppliers.put(supplier.name(), supplier);
			
			supplier = new CPUUsageSupplierService();
			this.suppliers.put(supplier.name(), supplier);
			
			supplier = new FanUsageSupplierService();
			this.suppliers.put(supplier.name(), supplier);
			
			supplier = new MemoryUsageSupplierService();
			this.suppliers.put(supplier.name(), supplier);
			
			supplier = new SoCTemperatureSupplierService();
			this.suppliers.put(supplier.name(), supplier);
		}
	}

	public String getName() {
		return "VALUE_SUPPLIER_MANAGER";
	}

	public void add(ValueSupplierService supplier) {
		this.suppliers.put(supplier.name(), supplier);
	}

	public String[] list() {
		return this.suppliers.keySet().toArray(new String[this.suppliers.size()]);
	}
	
	public byte[] value(String name) {
		if (!this.suppliers.containsKey(name)) {
			throw new RuntimeException("No ValueSupplierService found with name '"+name+"'");
		}
		return this.suppliers.get(name).value();
	}
	
	public int size(String name) {
		if (!this.suppliers.containsKey(name)) {
			throw new RuntimeException("No ValueSupplierService found with name '"+name+"'");
		}
		return this.suppliers.get(name).size();
	}
	
	public String type(String name) {
		if (!this.suppliers.containsKey(name)) {
			throw new RuntimeException("No ValueSupplierService found with name '"+name+"'");
		}
		return this.suppliers.get(name).type();
	}

	public String unit(String name) {
		if (!this.suppliers.containsKey(name)) {
			throw new RuntimeException("No ValueSupplierService found with name '"+name+"'");
		}
		return this.suppliers.get(name).unit();
	}
	
	public String version() {
		return "v3_"+this.suppliers.size()+"_ESet_"+this.suppliers.entrySet().size();
	}
}
