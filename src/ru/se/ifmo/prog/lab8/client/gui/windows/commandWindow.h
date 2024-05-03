#pragma once

#include <QWidget>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <jni.h>
#include <QGridLayout>
#include <QVBoxLayout>
#include <string>
#include <QTextEdit>

class commandWindow : public QWidget {
	public:
		commandWindow(QWidget* parent = 0, JNIEnv* env = nullptr, jclass* cl = nullptr, QString login = "", QString password = "");
	protected:
		void paintEvent(QPaintEvent* event);
	private:
		QString loginStr;
		QString passwordStr;
		QVBoxLayout* vbox;
		QHBoxLayout* hbox;
		QPushButton* send;
		QTextEdit* terminalO;
		QLineEdit* terminalI;
		JNIEnv* jnienv;
		jclass* jcl;
		void drawBackground();
		void changeWindow();
		void createAnother();
	private slots:
		void onBtnClick();
};
