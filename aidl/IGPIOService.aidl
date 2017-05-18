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

package android.os;
/**{@hide}*/
interface IGPIOService {
	String  getName();
	void    export(int gpio);
	void    unexport(int gpio);
	void    setDirectionIn(int gpio);
	void    setDirectionOut(int gpio);
	String  getDirection(int gpio);
	boolean getValue(int gpio);
	void    setValue(int gpio, boolean value);
}