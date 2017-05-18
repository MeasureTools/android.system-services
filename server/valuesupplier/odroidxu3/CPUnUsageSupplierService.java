/**
 * Copyright 2016 Daniel "Dadie" Korner
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *	 http://www.apache.org/licenses/LICENSE-2.0
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 * Unless required by applicable law or agreed to in writing, software
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 **/

package com.android.server.valuesupplier.odroidxu3;

import java.lang.RuntimeException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.android.server.ValueSupplierService;

//Usage of /proc/stat
//For more Information: http://www.linuxhowtos.org/System/procstat.htm

public class CPUnUsageSupplierService extends ValueSupplierService  {
	
	private int        cpuNumber;
	private ByteBuffer valueByteBuffer;
	
	public CPUnUsageSupplierService(int cpuNumber) {
		super("ODROID_XU3_CPU_"+cpuNumber+"_USAGE");
		this.cpuNumber  = cpuNumber;
		this.valueByteBuffer = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
	}

	private Double readValue() {
		BufferedReader bufReader;
		try {
			bufReader = new BufferedReader(new FileReader("/proc/stat"));
			for (int i = 0; i < this.cpuNumber; i++) {
				bufReader.readLine();
			}
			String[] probe1 = bufReader.readLine().split("\\s+");
			bufReader.close();
			//Sleep for 100ns ~= (Samplerate == 10kHz)
			Thread.sleep(0, 100);
			bufReader = new BufferedReader(new FileReader("/proc/stat"));
			for (int i = 0; i < this.cpuNumber; i++) {
				bufReader.readLine();
			}
			String[] probe2 = bufReader.readLine().split("\\s+");
			bufReader.close();
			
			Double user1   = Double.valueOf(probe1[1]);
			Double nice1   = Double.valueOf(probe1[2]);
			Double system1 = Double.valueOf(probe1[3]);
			Double idle1   = Double.valueOf(probe1[4]);

			Double user2   = Double.valueOf(probe2[1]);
			Double nice2   = Double.valueOf(probe2[2]);
			Double system2 = Double.valueOf(probe2[3]);
			Double idle2   = Double.valueOf(probe2[4]);

			Double totalDiff = (user2 + nice2 + system2 + idle2) - (user1 + nice1 + system1 + idle1);
			Double idleDiff  = idle2 - idle1;

			return (totalDiff - idleDiff) / totalDiff;
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public byte[] value() {
		this.valueByteBuffer.clear();
		return this.valueByteBuffer.putDouble((this.readValue()).doubleValue()).array();
	}
	
	@Override
	public int size() {
		return 8;
	}
	
	@Override
	public String type() {
		return "f64";
	}
	
	@Override
	public String unit() {
		return "";
	}
}

