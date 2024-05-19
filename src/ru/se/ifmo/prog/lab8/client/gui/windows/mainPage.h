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
#include "dragonInfo.h"
#include "commandWindow.h"
#include <map>
#include <string> 
#include "creationWindow.h"
#include "filter.h"
#include <set>
#include <QProgressBar>
#include <QPropertyAnimation>

class dragonInfo;

class creationWindow;

class filter;

class commandWindow;

class mainPage : public QWidget {
	public:
		mainPage(QWidget* parent = 0, JNIEnv* env = nullptr, jclass* cl = nullptr, QString loginArg = "", QString passwordArg = "");
		~mainPage();
		void infoDestroy();
		void creationDestroy();
		void destroyFilter();
		void destroyCommand();
		void changeUser(QString login, QString password);
	protected:
		void paintEvent(QPaintEvent* event);
		void closeEvent(QCloseEvent* event);
	private:
		QPushButton** langBtn; //+
		QString* langStr; //+
		int dragCountTable;
		int dragCountField;
		QPushButton* addTable; //+
		QString* title; //+
		QLabel** titleLabel; //+
		QPushButton** sortButton; //+
		QPushButton** filterButton; //+
		QGridLayout* grid; //+
		JNIEnv* jnienv;
		jclass* jcl;
		QLabel* username; //+
		QHBoxLayout* hbox; //+
		QHBoxLayout* mainhbox; //+
		QVBoxLayout* vbox; //+
		QVBoxLayout* fieldvbox; //*
		QWidget* fieldview; //+
		QPushButton* tryComs; //+
		QPushButton* exit; //+
		QPushButton* refresh; //+
		QPushButton** dragonBtn; //+
		QScrollArea* area; //+
		QWidget* viewport; //+
		QWidget*** dragon; //+
		QString loginStr;
		QString passwordStr;
		QTimer* timer; //+
		QProgressBar* progress;
		QPropertyAnimation* anim;
		jstring** dragonStr; //+
		dragonField* dragonfield; //+
		dragonInfo* dragoninfo; //+
		filter* filterWindow; //+
		creationWindow* creationwindow; //+
		std::map <std::string, std::string> color;
		std::set<jstring>* valueSet;
		QString* txt; //+
		QLabel* error; //+
		int curErr;
		commandWindow* commandwindow; 
		bool updateLock;
		bool firstUpdate;
		bool firstUpdateField;
		int vscroll;
		int hscroll;
		void drawBackground();
		void deleteDragon(QString num);
		void changeDragon(QString num, int n);
		void checkUpd();
		void initElements();
		jobjectArray getDragons();
		jobjectArray getAllDragons();
	private slots:
		void setLang(int l);
		void getText();
		void setText();
		void drawGuiField();
		void openComW();
		void setSort(int field);
		void showInfo(jstring* data, int n);
		void showCreation();
		void createFilter(int i);
		void back();
	public slots:
		void drawGuiTable();
		void destroy();
};
