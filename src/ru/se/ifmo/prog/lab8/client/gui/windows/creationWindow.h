#pragma once

#include <QWidget>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <jni.h>
#include <QGridLayout>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QScrollArea>
#include <QFormLayout>
#include "mainPage.h"
#include <QString>

class mainPage;

class creationWindow : public QWidget {
	public:
		creationWindow(QWidget* parent = 0, JNIEnv* env = nullptr, jclass* cl = nullptr, QString loginArg = "", QString passwordArg = "", QString* txt = nullptr, mainPage* mainpage = nullptr);
		void setText(QString* txt);
	protected:
		void paintEvent(QPaintEvent* event);
		void closeEvent(QCloseEvent* event);
	private:
		int datasize;
		JNIEnv* jnienv;
		jclass* jcl;
		QWidget** line;
		QFormLayout* form;
		QVBoxLayout* vbox;
		QPushButton* save;
		QString loginStr;
		QString passwordStr;
		QLabel* error;
		QLabel** label;
		mainPage* mp;
		QString* text;
		void drawBackground();
	private slots:
		void createDragon();
};
