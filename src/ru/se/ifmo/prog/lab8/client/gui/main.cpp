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
#include "windows/mainPage.h"
#include <thread>

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

void getMsg() {
	jvmConnector connector;
	jint rc = connector.connect(new char*[1]{"-Djava.class.path=."}, 1);
	if (rc != JNI_OK) {
		std::cout << "Error launching JVM!";
		exit(EXIT_FAILURE);
	}
/*	std::cout << "Calling getMsg\n";
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "getMsg", "()Z");
        if (method == 0) {
                std::cout << "Error!";
                return;
        }       
        jnienv->CallStaticBooleanMethod(*jcl, method);*/
	jmethodID method = connector.getEnv()->GetStaticMethodID(*connector.getClass(), "connect", "(Ljava/lang/String;Ljava/lang/String;)Z");
        if (method == 0) {
                std::cout << "Error!";
                return;
        }
	/*
        jboolean connected = jnienv->CallStaticBooleanMethod(*jcl, method, "12", "12");
        if (connected) {
            //    error->setText("CONNECTED!");
          //      changeWindow();
	  	std::cout << "COnnected!\n";
                return;
        }
	std::cout << "Not connected!\n";
        //error->setText("NOT CONNECTED! TRY ANOTHER IP OR PORT!");*/
}

int buildGUI(int argc, char** argv) {
	std::cout << "Starting!\n";
	jvmConnector connector;
	std::cout << "Created connector\n";
        jint rc = connector.connect(new char*[1]{"-Djava.class.path=."}, 1);
	std::cout << "Checking connection\n";
	if (rc != JNI_OK) {
		std::cout << "Error launching JVM!";
		exit(EXIT_FAILURE);
	}
	std::cout << "Class found\n";
	QApplication app(argc, argv);
	QString login = "";
	QString password = "";
	connectionWindow connection(0, connector.getEnv(), connector.getClass());
	connection.setWindowTitle("Connect to server");
	std::cout << "Window ready!\n";
	connection.show();
	return app.exec();
}

int main(int argc, char** argv) {	
/*	std::thread th1(&getMsg);
	std::thread th2(&buildGUI, argc, argv);
	th1.join();
	th2.join();*/
	buildGUI(argc, argv);
}
