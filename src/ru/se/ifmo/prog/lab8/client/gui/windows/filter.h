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
#include <set>
#include <QCheckBox>
#include <QString>

class mainPage;

class filter : public QWidget {
	public:
		filter(QWidget* parent = 0, std::set<jstring>* values = nullptr, int col = 0, QString title = "", JNIEnv* env = nullptr, jclass* cl = nullptr, QString loginArg = "", QString passwordArg = "", QString* txt = nullptr, mainPage* mainpage = nullptr);
	protected:
		void paintEvent(QPaintEvent* event);
		void closeEvent(QCloseEvent* event);
	private:
		int datasize;
		int column;
		JNIEnv* jnienv;
		jclass* jcl;
		QWidget** line;
		QFormLayout* form;
		QVBoxLayout* vbox;
		QPushButton* save;
		QString loginStr;
		QString passwordStr;
		QLabel* titleLabel;
		QCheckBox** checkbox;
		QLabel** label;
		QVBoxLayout* scrollable;
		QWidget* viewport;
		QScrollArea* area;
		mainPage* mp;
		std::set<jstring>* val;
		void drawBackground();
	private slots:
		void changeFilter();
};
