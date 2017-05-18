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

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.RuntimeException;
import java.lang.Runtime;
import java.lang.Process;
import java.io.DataOutputStream;

import android.os.IGPIOService;

public class GPIOService extends IGPIOService.Stub {

	protected GPIOService() {
	}

	public String getName() {
		return "GPIO_SERVICE";
	}

	public void export(int gpio) {
		try{
			Process su = Runtime.getRuntime().exec("su");
			{
				DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
				outputStream.writeBytes("echo "+gpio+" > /sys/class/gpio/export\n");
				outputStream.flush();
				outputStream.writeBytes("exit\n");
				outputStream.flush();
			}
			su.waitFor();
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void unexport(int gpio) {
		try{
			Process su = Runtime.getRuntime().exec("su");
			{
				DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
				outputStream.writeBytes("echo "+gpio+" > /sys/class/gpio/unexport\n");
				outputStream.flush();
				outputStream.writeBytes("exit\n");
				outputStream.flush();
			}
			su.waitFor();
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	protected void direction(int gpio, String direction) {
		try{
			Process su = Runtime.getRuntime().exec("su");
			{
				DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
				outputStream.writeBytes("echo "+direction+" > /sys/class/gpio/gpio"+gpio+"/direction\n");
				outputStream.flush();
				outputStream.writeBytes("exit\n");
				outputStream.flush();
			}
			su.waitFor();
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void setDirectionIn(int gpio) {
		this.direction(gpio, "in");
	}
	
	final public void setDirectionOut(int gpio) {
		this.direction(gpio, "out");
	}
	
	public String getDirection(int gpio) {
		try {
			BufferedReader bufReader = new BufferedReader(new FileReader("/sys/class/gpio/gpio"+gpio+"/direction"));
			return bufReader.readLine();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public boolean getValue(int gpio) {
		try {
			BufferedReader bufReader = new BufferedReader(new FileReader("/sys/class/gpio/gpio"+gpio+"/value"));
			return 1 == Integer.valueOf(bufReader.readLine()).intValue();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setValue(int gpio, boolean value) {
		try{
			Process su = Runtime.getRuntime().exec("su");
			{
				DataOutputStream outputStream = new DataOutputStream(su.getOutputStream());
				if (value) {
					outputStream.writeBytes("echo 1 > /sys/class/gpio/gpio"+gpio+"/value\n");
				}
				else {
					outputStream.writeBytes("echo 0 > /sys/class/gpio/gpio"+gpio+"/value\n");
				}
				outputStream.flush();
				outputStream.writeBytes("exit\n");
				outputStream.flush();
			}
			su.waitFor();
		}
		catch(Exception e){
			throw new RuntimeException(e);
		}
	}

}

