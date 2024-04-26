#pragma once

#include <QWidget>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <jni.h>
#include <QGridLayout>
#include <QVBoxLayout>

class connectionWindow : public QWidget {
	public:
		connectionWindow(QWidget* parent = 0, JNIEnv* env = nullptr, jclass* cl = nullptr);
	protected:
		void paintEvent(QPaintEvent* event);
	private:
		QVBoxLayout* vbox;
		QGridLayout* grid;
		QLabel* host;
		QLabel* port;
		QLabel* error;
		QLineEdit* hostIn;
		QLineEdit* portIn;
		QPushButton* confirm;
		JNIEnv* jnienv;
		jclass* jcl;
		void drawBackground();
	private slots:
		void onClick();
};
