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
#include "mainPage.h"
#include <QKeyEvent>

class mainPage;

class commandWindow : public QWidget {
	public:
		commandWindow(QWidget* parent = 0, JNIEnv* env = nullptr, jclass* cl = nullptr, QString login = "", QString password = "", QString* txt = nullptr, bool* lock = nullptr, mainPage* mp = nullptr);
	protected:
		void paintEvent(QPaintEvent* event);
		void closeEvent(QCloseEvent* event);
		void keyPressEvent(QKeyEvent* event);
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
		mainPage* mainpage;
		bool* updateLock;
		void drawBackground();
		void changeWindow();
		void createAnother();
	private slots:
		void onBtnClick();
};
