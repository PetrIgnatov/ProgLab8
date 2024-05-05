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
#include "dragonField.h"
#include <QTimer>

class mainPage : public QWidget {
	public:
		mainPage(QWidget* parent = 0, JNIEnv* env = nullptr, jclass* cl = nullptr, QString loginArg = "", QString passwordArg = "");
		~mainPage();
	protected:
		void paintEvent(QPaintEvent* event);
		void closeEvent(QCloseEvent* event);
	private:
		int dragCount;
		QGridLayout* grid;
		JNIEnv* jnienv;
		jclass* jcl;
		QLabel* username;
		QHBoxLayout* hbox;
		QHBoxLayout* mainhbox;
		QVBoxLayout* vbox;
		QPushButton* tryComs;
		QPushButton* exit;
		QPushButton* refresh;
		QScrollArea* area;
		QWidget* viewport;
		QWidget*** dragon;
		QString loginStr;
		QString passwordStr;
		QTimer* timer;
		dragonField* dragonfield;
		bool firstUpdate;
		int vscroll;
		int hscroll;
		void drawBackground();
		void deleteDragon(QString num);
		void changeDragon(QString num, int n);
		void checkUpd();
	private slots:
		void drawGui();
		void openComW();
		void destroy();
};
