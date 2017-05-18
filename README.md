# How to install

## Android 4.4.x

1. Copy the content of the **aidl** folder in your android platform folder frameworks/base/core/java/android/os

2. Copy the content of the **server** folder in your android platform folder frameworks/base/services/java/com/android/server

3. Add the AIDL files to your frameworks/base/Android.mk

		LOCAL_SRC_FILES += \
		[..]
		core/java/android/os/IUpdateLock.aidl \
		[..]
		[ADD]
		core/java/android/os/IValueSupplierManager.aidl \
		core/java/android/os/IValueSupplierService.aidl \
		core/java/android/os/IGPIOService.aidl \
		[/ADD]
		[...]

4. Add the new services to your frameworks/base/services/java/com/android/server/SystemServer.java

		[..]
		[ADD]
		//ValueSupplierManager
		import com.android.server.ValueSupplierManager;
		//GPIO
		import com.android.server.GPIOService;
		[/ADD]
		[..]
		class ServerThread {
		[..]
			public void initAndLoop() { // On Android 4.4
			private void startOtherServices() { // On Android 7.1.x
		[..]
		[ADD]
				try {
					Slog.i(TAG, "Value Supplie Manager");
					ValueSupplierManager valueSupplierManager = new ValueSupplierManager();
					ServiceManager.addService(valueSupplierManager.getName(), valueSupplierManager);
				}
				catch (Throwable e) {
					Slog.e(TAG, "Failed to add Value Supplie Manager", e);
				}

				try {
					Slog.i(TAG, "GPIO Service");
					GPIOService gpioService = new GPIOService();
					ServiceManager.addService(gpioService.getName(), gpioService);
				}
				catch (Throwable e) {
					Slog.e(TAG, "Failed to add GPIO Service", e);
				}
		[/ADD]
		[..]

5. in your main android platform folder call "make api-update"

## Android 7.1.x

1. Create the folder frameworks/base/services/grimgal/

2. Copy the content of the **server** folder to the created **grimgal** folder.

3. Create a Android.mk file in the **grimgal** folder with the following content

		LOCAL_PATH := $(call my-dir)

		include $(CLEAR_VARS)

		LOCAL_MODULE := services.grimgal

		LOCAL_SRC_FILES += \
			  $(call all-java-files-under,java)

		LOCAL_JAVA_LIBRARIES := services.core

		include $(BUILD_STATIC_JAVA_LIBRARY)

4. Add the **grimgal** services to your frameworks/base/services/Android.mk

		services := \
		[..]
		[ADD]
		    grimgal
		[/ADD]

5. Copy the content of the **aidl** folder in your android platform folder frameworks/base/core/java/android/os

6. Add the aidl files to your frameworks/base/Android.mk

		LOCAL_SRC_FILES += \
		[..]
		core/java/android/os/IUpdateLock.aidl \
		[..]
		[ADD]
		core/java/android/os/IValueSupplierManager.aidl \
		core/java/android/os/IValueSupplierService.aidl \
		core/java/android/os/IGPIOService.aidl \
		[/ADD]
		[...]

7. Add the new services to your frameworks/base/services/java/com/android/server/SystemServer.java

		[..]
		[ADD]
		//ValueSupplierManager
		import com.android.server.ValueSupplierManager;
		//GPIO
		import com.android.server.GPIOService;
		[/ADD]
		[..]
		class ServerThread {
		[..]
			private void startOtherServices() {
		[..]
		[ADD]
				try {
					Slog.i(TAG, "Value Supplie Manager");
					ValueSupplierManager valueSupplierManager = new ValueSupplierManager();
					ServiceManager.addService(valueSupplierManager.getName(), valueSupplierManager);
				}
				catch (Throwable e) {
					Slog.e(TAG, "Failed to add Value Supplie Manager", e);
				}

				try {
					Slog.i(TAG, "GPIO Service");
					GPIOService gpioService = new GPIOService();
					ServiceManager.addService(gpioService.getName(), gpioService);
				}
				catch (Throwable e) {
					Slog.e(TAG, "Failed to add GPIO Service", e);
				}
		[/ADD]
		[..]

8. in your main platform folder call "make api-update"
