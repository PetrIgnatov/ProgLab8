#include <iostream>
#include <jni.h>
#include "jni/jvmConnector.h"
#include "windows/connectionWindow.h"
#include <QApplication>
#include <QWidget>
#include <QVBoxLayout>
#include <QPushButton>
#include <string> 
#include <functional>
#include <QFormLayout>
#include <QLineEdit>

/*
void onClick(char* funcName) {
	jmethodID method = connector.getEnv()->GetStaticMethodID(*(connector.getClass()), funcName, "()V");
	if (method == 0) {
                std::cout << "Error finding method!";
                exit(EXIT_FAILURE);
        }

	connector.getEnv()->CallStaticVoidMethod(*(connector.getClass()), method, nullptr);
}
*/
int main(int argc, char** argv) {
	jvmConnector connector;
	jint rc = connector.connect(new char*[1]{"-Djava.class.path=."}, 1);
	if (rc != JNI_OK) {
		std::cout << "Error launching JVM!";
		exit(EXIT_FAILURE);
	}	
	QApplication app(argc, argv);
	connectionWindow connection(0, connector.getEnv(), connector.getClass());
	connection.resize(800,500);
	connection.setWindowTitle("Connect to server");
	connection.show();
	return app.exec();
}
