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

class mainPage;

class dragonInfo : public QWidget {
	public:
		dragonInfo(QWidget* parent = 0, jstring* values = nullptr, int n = 0, JNIEnv* env = nullptr, jclass* cl = nullptr, QString loginArg = "", QString passwordArg = "", QString* txt = nullptr, mainPage* mainpage = nullptr);
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
		QLabel* error;
		QLabel** label;
		QString* text;
		QString loginStr;
		QString passwordStr;
		mainPage* mp;
		void drawBackground();
	private slots:
		void changeDragon();
};
