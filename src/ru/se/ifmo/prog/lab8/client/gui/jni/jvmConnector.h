#pragma once

#include <jni.h>

class jvmConnector {
	public:
		jvmConnector();
		jint connect(char** optionsStrs, int optionsNum);
		JavaVM* getJVM();
		JNIEnv* getEnv();
		jclass* getClass();
	private:
		JavaVM* jvm;
		JNIEnv* env;
		jclass mainClass;
};
