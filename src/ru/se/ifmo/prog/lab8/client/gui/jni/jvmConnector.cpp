#include <jni.h>
#include "jvmConnector.h"
#include <iostream>
#include <cstdlib>

jvmConnector::jvmConnector() {
	jvm = nullptr;
	env = nullptr;
}

jint jvmConnector::connect(char** optionsStrs, int optionsNum) {
	JavaVMInitArgs vm_args;
	JavaVMOption* options = new JavaVMOption[optionsNum];
	for (int i = 0; i < optionsNum; ++i) {
		options[i].optionString = optionsStrs[i];
	}
	vm_args.version = JNI_VERSION_1_6;
	vm_args.nOptions = optionsNum;
	vm_args.options = options;
	vm_args.ignoreUnrecognized = false;
	jint rc = JNI_CreateJavaVM(&jvm, (void**)&env, &vm_args);
	delete options;
	std::cout << "Options deleted!\n";
	mainClass = env->FindClass("ru/se/ifmo/prog/lab8/client/back/Main");
//	mainClass = env->FindClass("ru.se.ifmo.prog.lab8.client.back.Main");
	std::cout << "Finding class\n";
	if (mainClass == 0) {
		std::cout << "Error finding class!";
		exit(EXIT_FAILURE);
	}
	return rc;
}

JavaVM* jvmConnector::getJVM() {
	return jvm;
}

JNIEnv* jvmConnector::getEnv() {
	return env;
}

jclass* jvmConnector::getClass() {
	return &mainClass;
}
