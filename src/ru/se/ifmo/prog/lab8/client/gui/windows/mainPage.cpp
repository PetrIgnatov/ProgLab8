#include <QWidget>
#include <functional>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <jni.h>
#include <QGridLayout>
#include "mainPage.h"
#include <QVBoxLayout>
#include <iostream>
#include <QPainter>
#include <QPen>
#include <QColor>
#include <QHBoxLayout>
#include "helpfulStuff.h"
#include <QScrollArea>
#include "dragonField.h"
#include "commandWindow.h"
#include "dragonInfo.h"
#include "creationWindow.h"
#include <QTimer> 
#include <QScrollBar>
#include <cmath>
#include <algorithm>
#include <ctime>
#include <string>
#include <utility>
#include <set>
#include <QProgressBar>
#include <QPropertyAnimation>

mainPage::mainPage(QWidget* parent, JNIEnv* env, jclass* cl, QString loginArg, QString passwordArg) : QWidget(parent) {
	jnienv = env;
	jcl = cl;
	this->setFixedWidth(1280);
	this->setFixedHeight(920);
	dragCountTable = 0;
	dragCountField = 0;
	loginStr = loginArg;
	passwordStr = passwordArg;
	QPalette backgroundPal = QPalette();
	backgroundPal.setColor(QPalette::Window, Qt::white);
	this->setAutoFillBackground(true);
	this->setPalette(backgroundPal);
	timer = new QTimer();
	timer->setInterval(1000);
	QObject::connect(timer, &QTimer::timeout, this, &mainPage::drawGuiField);
	timer->start();
	vscroll = 0;
	hscroll = 0;
	firstUpdate = true;
	title = new QString[]{"  ID  ", "  NAME  ", "  X  ", "  Y  ", "  CREATION DATE  ", "  AGE  ", "  COLOR  ", "  TYPE  ", "  CHARACTER  ", "  DEPTH  ", "  NUMBER OF TREASURES  "};	
	initElements();
	setText();
	drawGuiField();
}

void mainPage::initElements() {
	dragoninfo = nullptr;
	creationwindow = nullptr;
	filterWindow = nullptr;
	updateLock = false;
	valueSet = new std::set<jstring>[11];
	for (int i = 0; i < 11; ++i) {
		valueSet[i] = std::set<jstring>();
	} 
	vbox = new QVBoxLayout(this);
	hbox = new QHBoxLayout();
	tryComs = new QPushButton("TRY COMMANDS");
	tryComs->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 40px");
	QObject::connect(tryComs, &QPushButton::clicked, this, &mainPage::openComW);
	exit = new QPushButton("EXIT");
	exit->setStyleSheet("color: white; background: #F00000; border: none; font-size: 40px");
	refresh = new QPushButton("REFRESH");
	refresh->setStyleSheet("color: white; background: #0B00DA; border: none; font-size: 40px");
	QObject::connect(refresh, &QPushButton::clicked, this, &mainPage::drawGuiTable);
	username = new QLabel(loginStr);
	mainhbox = new QHBoxLayout();
	area = new QScrollArea();
	fieldview = new QWidget();
	fieldvbox = new QVBoxLayout();
	txt = nullptr;
	getText();
	curErr = -1;
	hscroll = 0;
	vscroll = 0;
	progress = new QProgressBar();
	anim = new QPropertyAnimation(progress, "value");
	addTable = new QPushButton(txt[19]);
	addField = new QPushButton(txt[19]);
	langStr = new QString[]{"РУССКИЙ", "ESPANOL", "HRVATSKI", "SLOVENČINA"};
        langBtn = new QPushButton*[4];
	commandwindow = nullptr;
        for (int i = 0; i < 4; ++i) {
                langBtn[i] = new QPushButton(langStr[i]);
                QObject::connect(langBtn[i], &QPushButton::clicked, this, [this, i](){setLang(i);});
                hbox->addWidget(langBtn[i]);
        }
	hbox->addWidget(tryComs);
	hbox->addWidget(refresh);
	hbox->addWidget(exit);
	hbox->addWidget(username);
	vbox->addLayout(hbox);
	vbox->addSpacing(50);
	fieldvbox->addWidget(progress);
	fieldview->setLayout(fieldvbox);
	//Тут поменял
	mainhbox->addWidget(area);
//	dragonfield = new dragonField();
	mainhbox->addWidget(fieldview);
	vbox->addLayout(mainhbox);
	error = new QLabel("");
	error->setStyleSheet("color: black; background: transparent; border: none; font-size: 12px");
	vbox->addWidget(error, Qt::AlignHCenter);
//	setText();
	firstUpdate = true;
	firstUpdateField = true;
}

void mainPage::checkUpd() {
	std::cout << "UPDATE!\n";
}

void mainPage::paintEvent(QPaintEvent* e) {
	Q_UNUSED(e);
	drawBackground();
}

void mainPage::drawBackground() {
	QPainter painter(this);
	painter.setPen(Qt::NoPen);
	painter.setBrush(QBrush("#000000"));
	painter.drawPolygon(new QPointF[] {QPointF(0, 0), QPointF(1280, 0), QPointF(1280, 100), QPointF(0, 100)}, 4);
}

void mainPage::closeEvent(QCloseEvent* event) {
	this->destroy();
}

void mainPage::deleteDragon(QString num) {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "remove", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z");
        if (method == 0) {
                std::cout << "Error!";
                return;
        }
        jboolean response = jnienv->CallStaticBooleanMethod(*jcl, method, QStr_to_jstr(jnienv,num), QStr_to_jstr(jnienv, loginStr), QStr_to_jstr(jnienv, passwordStr));
	if (!response) {
		error->setText(txt[21]);
		curErr = 21;
	}
	else {
		error->setText("");
		curErr = -1;
		drawGuiTable();
	}
}

void mainPage::changeDragon(QString num, int n) {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "change", "(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z");
        if (method == 0) {
                std::cout << "Error!";
                return;
        }
	jobjectArray dr = jnienv->NewObjectArray(9, jnienv->FindClass("java/lang/String"), NULL);
	srand(time(NULL));
	for (int i = 0; i < 9; ++i) {
//		std::cout << ((QLineEdit*)(dragon[n][(i < 3 ? i+1 : i+2)]))->text().toStdString() << "\n";
		jstring jstr = QStr_to_jstr(jnienv, ((QLineEdit*)(dragon[n][(i < 3 ? i+1 : i+2)]))->text());
		jnienv->SetObjectArrayElement(dr, i, jstr);
		jnienv->DeleteLocalRef(jstr);
	}
	jboolean response = jnienv->CallStaticBooleanMethod(*jcl, method, QStr_to_jstr(jnienv,num), dr, QStr_to_jstr(jnienv, loginStr), QStr_to_jstr(jnienv, passwordStr));
	if (!response) {
		error->setText(txt[20]);
		curErr = 20;
	}
	else {
		error->setText("");
		curErr = -1;
	}
	jnienv->DeleteLocalRef(dr);
}

void mainPage::drawGuiField() {
	if (updateLock) {
		return;
	}
	anim->setStartValue(progress->minimum());
	anim->setEndValue(progress->maximum());
	anim->setDuration(200);
	anim->start();
	if (!firstUpdateField) {
		for (int i = 0; i < dragCountField; ++i) {
			std::cout << i << " button deleted\n";
			delete dragonBtn[i];
			delete[] dragonStr[i];
		}
		delete[] dragonStr;
		delete[] dragonBtn;
	//	delete addField;
		delete dragonfield;
	}
//	vscroll = area->verticalScrollBar()->value();
//	hscroll = area->horizontalScrollBar()->value();
	std::cout << "scroll " << vscroll << " " << hscroll << "\n";
	dragonfield = new dragonField();
	//addField = new QPushButton("ADD");
	//QObject::connect(addField, &QPushButton::clicked, this, &mainPage::showCreation);
	firstUpdateField = false;
	jobjectArray dragons = getDragons();
	if (dragons == NULL) {
		dragCountField = 0;
	}
	else {
		dragCountField = jnienv->GetArrayLength(dragons);
	}
	dragonStr = new jstring*[dragCountField];
	dragonfield = new dragonField();
	dragonBtn = new QPushButton*[dragCountField];
	float xmax = -(1e9);
	float xmin = 1e9;
	float ymax = -(1e9);
	float ymin = 1e9;
	float xcoord[dragCountField], ycoord[dragCountField];
	for (int i = 0; i < dragCountField; ++i) {
		std::cout << i << " i\n";
		jobjectArray curDr = jobjectArray(jnienv->GetObjectArrayElement(dragons, i));
		int curDrSize = jnienv->GetArrayLength(curDr);
		dragonStr[i] = new jstring[curDrSize];
		for (int j = 0; j < curDrSize; ++j) {
			dragonStr[i][j] = ((jstring)(jnienv->GetObjectArrayElement(curDr, j)));
		}
		float x = atof(jnienv->GetStringUTFChars(dragonStr[i][2], NULL));
		float y = atof(jnienv->GetStringUTFChars(dragonStr[i][3], NULL));
		xmax = std::max(x, xmax);
		xmin = std::min(x, xmin);
		ymax = std::max(y, ymax);
		ymin = std::min(y, ymin);
		xcoord[i] = x;
		ycoord[i] = y;
	}
	int xdiff = 8*std::max(1.0,float(xmax-xmin)/180.0), ydiff = 10*std::max(1.0,float(ymax-ymin)/225.0);
	xmax += xdiff;
	xmin -= xdiff;
	ymax += ydiff;
	ymin -= ydiff;
	std::string hex[16] = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
	std::cout << "Starting for " << dragCountField << "\n";
	for (int i = 0; i < dragCountField; ++i) {
		std::cout << "Creating button " << i << "\n";
		dragonBtn[i] = new QPushButton("AA", dragonfield);
		dragonBtn[i]->move(int(xcoord[i]-xmin)*600/(xmax-xmin), 750*int(ymax-ycoord[i])/(ymax-ymin));
		if (color.find(jnienv->GetStringUTFChars(dragonStr[i][11], nullptr)) == color.end()) {
			std::cout << jnienv->GetStringUTFChars(dragonStr[i][11], nullptr);
			int curCol = 0;
			std::string colStr = "";
			bool used = true;
			while (used || curCol == 15130624) {
				colStr = "";
				curCol = rand()%16777215;
				for (int cif = 0; cif < 6; ++cif) {
					std::cout << hex[curCol%16] << " ";
					colStr = hex[curCol%16] + colStr;
					curCol /= 16;
				}
				std::cout << colStr << "\n";
				used = false;
				for (auto c : color) {
					if (c.second == colStr) {
						used = true;
						break;
					}
				}
			}
			color[jnienv->GetStringUTFChars(dragonStr[i][11], nullptr)] = colStr;	
		}
		dragonBtn[i]->setStyleSheet(QString::fromStdString("color: transparent; background: #" + color[jnienv->GetStringUTFChars(dragonStr[i][11], nullptr)]+"; border: none; font-size: 12px"));
		QObject::connect(dragonBtn[i], &QPushButton::clicked, this, [i, this](){showInfo(this->dragonStr[i], 11);});
//		dragonBtn[i]->raise();
/*		dragonBtn[i] = new QPushButton("DRAGON");
		hbox->addWidget(dragonBtn[i]);*/
//		dragon[i][j] = new QLabel(jnienv->GetStringUTFChars(jstring(jnienv->GetObjectArrayElement(curDr, j)), nullptr));
	}
	//fieldvbox->addWidget(addField);
	fieldvbox->addWidget(dragonfield);
//	area->verticalScrollBar()->setValue(vscroll);
//	area->horizontalScrollBar()->setValue(hscroll);
}

void mainPage::drawGuiTable() {
	std::cout << "Drawing gui\n";
	if (!firstUpdate) {
		std::cout << vscroll << " " << hscroll << "\n";
		vscroll = area->verticalScrollBar()->value();
		hscroll = area->horizontalScrollBar()->value();
		for (int i = 0; i < dragCountTable; ++i) {
			for (int j = 0; j < 13; ++j) {
				delete dragon[i][j];
			}
			delete[] dragon[i];
		}
		delete[] dragon;
		std::cout << "Dragons deleted\n";
		for (int i = 0; i < 11; ++i) {
			std::cout << i << "\n";
			delete titleLabel[i];
			delete sortButton[i];
			delete filterButton[i];
		}
		std::cout << "For finished\n";
		delete[] titleLabel;
		delete[] sortButton;
		delete[] filterButton;
		delete addTable;
		std::cout << "Titles deleted\n";
		delete grid;
		delete viewport;
	}
	firstUpdate = false; 
	std::cout << "Memory cleared\n";
	grid = new QGridLayout();
	viewport = new QWidget;
	std::cout << "Reinitialized\n";
	titleLabel = new QLabel*[11];
	sortButton = new QPushButton*[11];
	filterButton = new QPushButton*[11];
	addTable = new QPushButton(txt[19]);
	QObject::connect(addTable, &QPushButton::clicked, this, &mainPage::showCreation);
	for (int i = 0; i < 11; ++i) {
		std::cout << i << "\n";
		titleLabel[i] = new QLabel(txt[7+i]);
		titleLabel[i]->setMinimumWidth(txt[7+i].length()*10+10);
		titleLabel[i]->setStyleSheet("color: white; background: black; border: none; font-size: 12px");
		sortButton[i] = new QPushButton(txt[3]);
		sortButton[i]->setStyleSheet("color: white; background: #0B00DA; border: none; font-size: 12px");
		filterButton[i] = new QPushButton(txt[4]);
		filterButton[i]->setStyleSheet("color: white; background: #F00000; border: none; font-size: 12px");
		QObject::connect(sortButton[i], &QPushButton::clicked, this, [i, this](){setSort(i);});
		QObject::connect(filterButton[i], &QPushButton::clicked, this, [i, this](){createFilter(i);});
/*		titleLayout[i] = new QHBoxLayout();
		titleLayout[i]->addWidget(titleLabel[i]);
		titleLayout[i]->addWidget(sortButton[i]);
		titleLayout[i]->addWidget(filterButton[i]);
		titleViewport[i] = new QWidget();
		titleViewport[i]->setLayout(titleLayout[i]); */
		std::cout << "Adding to grid\n";;
		grid->addWidget(titleLabel[i], 0, i*3);
		grid->addWidget(sortButton[i], 0, i*3+1);
		grid->addWidget(filterButton[i], 0, i*3+2);
	}
	jobjectArray dragons = getDragons();
	if (dragons == NULL) {
		dragCountTable = 0;
	}
	else {
		dragCountTable = jnienv->GetArrayLength(dragons);
	}
	dragon = new QWidget**[dragCountTable]; 
	for (int i = 0; i < dragCountTable; ++i) {
		dragon[i] = new QWidget*[13];
		jobjectArray curDr = jobjectArray(jnienv->GetObjectArrayElement(dragons, i));
		for (int j = 0; j < 13; ++j) {
			switch (j) {
				case 0:
					dragon[i][j] = new QLabel(jnienv->GetStringUTFChars(jstring(jnienv->GetObjectArrayElement(curDr, j)), nullptr));
					dragon[i][j]->setStyleSheet("color: black; background: transparent; border: none; font-size: 12px");
					break;
				case 4:
					dragon[i][j] = new QLabel(jnienv->GetStringUTFChars(jstring(jnienv->GetObjectArrayElement(curDr, j)), nullptr));
					dragon[i][j]->setStyleSheet("color: black; background: transparent; border: none; font-size: 12px");
					break;
				case 12:
					dragon[i][j] = new QPushButton(txt[6], nullptr);
					dragon[i][j]->setStyleSheet("color: white; background: #f00000; border: none; font-size: 12px");
					break;
				case 11:
					dragon[i][j] = new QPushButton(txt[5], nullptr);
					dragon[i][j]->setStyleSheet("color: white; background: #00BA18; border: none; font-size: 12px");
					break;
				default:
					dragon[i][j] = new QLineEdit(jnienv->GetStringUTFChars(jstring(jnienv->GetObjectArrayElement(curDr, j)), nullptr));
					dragon[i][j]->setStyleSheet("color: black; background: transparent; border: none; font-size: 12px");
					break;
			}
			std::cout << "Adding dragons to grid\n";
			grid->addWidget(dragon[i][j], i+1, j*3, 1, 3);
		}
		std::cout << "Dragon added!\n";
		QString id = ((QLabel*)(dragon[i][0]))->text();
		QObject::connect((QPushButton*)(dragon[i][11]), &QPushButton::clicked, this, [id, i, this](){changeDragon(id, i);});
		QObject::connect((QPushButton*)(dragon[i][12]), &QPushButton::clicked, this, [id, this](){deleteDragon(id);});
	}
	grid->addWidget(addTable, dragCountTable+1, 0, 1, 39);
	std::cout << "Adding grid to viewport\n";
	std::cout << "scroll " << vscroll << " " << hscroll << "\n";
	viewport->setLayout(grid);
	area->setWidget(viewport);
	area->verticalScrollBar()->setValue(vscroll);
	area->horizontalScrollBar()->setValue(hscroll);
}

void mainPage::openComW() {
	if (commandwindow != nullptr) {
		delete commandwindow;
		commandwindow = nullptr;
		updateLock = false;
	}
	if (!updateLock) {
		commandwindow = new commandWindow(nullptr, jnienv, jcl, loginStr, passwordStr, txt, &updateLock, this);
		commandwindow->show();	
	}
}

void mainPage::setSort(int field) {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "setSort", "(I)V");
        if (method == 0) {
                std::cout << "Error!";
                return;
        }
        jnienv->CallStaticVoidMethod(*jcl, method, field);
	this->drawGuiTable();
}

mainPage::~mainPage() {
	std::cout << "Deleting\n";
}

jobjectArray mainPage::getDragons() {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "getDragons", "(Ljava/lang/String;Ljava/lang/String;)[[Ljava/lang/String;");
	if (method == 0) {
		std::cout << "Error!";
		return NULL;
	}
	return jobjectArray(jnienv->CallStaticObjectMethod(*jcl, method, jnienv->NewStringUTF("show"), QStr_to_jstr(jnienv, loginStr), QStr_to_jstr(jnienv, passwordStr)));
}

jobjectArray mainPage::getAllDragons() {
        jmethodID method = jnienv->GetStaticMethodID(*jcl, "getAllDragons", "(Ljava/lang/String;Ljava/lang/String;)[[Ljava/lang/String;");
        if (method == 0) {
                std::cout << "Error!";
                return NULL;
        }
        return jobjectArray(jnienv->CallStaticObjectMethod(*jcl, method, jnienv->NewStringUTF("show"), QStr_to_jstr(jnienv, loginStr), QStr_to_jstr(jnienv, passwordStr)));
}

void mainPage::getText() {
        jmethodID method = jnienv->GetStaticMethodID(*jcl, "getMainText", "()[Ljava/lang/String;");
        if (method == 0) {
                std::cout << "Error!";
                return;
        }
        jobjectArray txtArr = jobjectArray(jnienv->CallStaticObjectMethod(*jcl, method));
        int txtsize = jnienv->GetArrayLength(txtArr);
        if (txt != nullptr) {
                delete[] txt;
        }
        txt = new QString[txtsize];
        for (int i = 0; i < txtsize; ++i) {
                txt[i] = jnienv->GetStringUTFChars((jstring)(jnienv->GetObjectArrayElement(txtArr, i)), NULL);
	}               
}

void mainPage::setLang(int l) {
        jmethodID method = jnienv->GetStaticMethodID(*jcl, "setLanguage", "(I)V");
        if (method == 0) {
                std::cout << "Error!";
                return;
        }
        jnienv->CallStaticVoidMethod(*jcl, method, l);
        getText();
        setText();
}

void mainPage::setText() {
	std::cout << "setting text\n";
	tryComs->setText(txt[0]);
	std::cout << "0\n";
	refresh->setText(txt[1]);
	std::cout << "1\n";
	exit->setText(txt[2]);
	std::cout << "ifs\n";
/*	if (creationwindow != nullptr) {
		creationwindow->setText(txt);
	} */
	if (dragoninfo != nullptr) {
		delete dragoninfo;
		dragoninfo = nullptr;
	}
	std::cout << "Creation\n";
	if (creationwindow != nullptr) {
		delete creationwindow;
		creationwindow = nullptr;
	}
	std::cout << "Filter\n";
	if (filterWindow != nullptr) {
		std::cout << "Deleting\n";
		delete filterWindow;
		filterWindow = nullptr;
	}
	std::cout << "commandWindow\n";
	if (commandwindow != nullptr) {
		std::cout << "Deleting\n";
		delete commandwindow;
		commandwindow = nullptr;
		updateLock = false;
	}
	if (curErr != -1) {
		error->setText(txt[curErr]);
	}
	std::cout << "Fin\n";
	/*
	for (int i = 0; i < 11; ++i) {
		std::cout << i << "\n";
		titleLabel[i]->setMinimumWidth(txt[7+i].length()*10+10);
		titleLabel[i]->setText(txt[7+i]);
		sortButton[i]->setText(txt[3]);
		filterButton[i]->setText(txt[4]);
	}
	for (int i = 0; i < dragCountTable; ++i) {
		std::cout << "\n";
		((QPushButton*)(dragon[i][11]))->setText(txt[4]);
		((QPushButton*)(dragon[i][12]))->setText(txt[5]);
	}
	*/
	drawGuiTable();
}

void mainPage::destroy() {
	for (int i = 0; i < dragCountTable; ++i) {
		for (int j = 0; j < 13; ++j) {
			delete dragon[i][j];
		}
		delete[] dragon[i];
	}
	for (int i = 0; i < dragCountField; ++i) {
		delete[] dragonStr[i];
		delete dragonBtn[i];
	}
	delete[] dragonStr;
	delete[] dragonBtn;
	delete[] dragon;
	for (int i = 0; i < 11; ++i) {
		delete titleLabel[i];
		delete sortButton[i];
		delete filterButton[i];
	}
	std::cout << "For finished\n";
	delete[] titleLabel;
	std::cout << "tl\n";
	delete[] sortButton;
	std::cout << "sb\n";
	std::cout << "tv\n";
	delete[] filterButton;
	std::cout << "fb\n";
       	delete tryComs;
	std::cout << "tc\n";
	delete exit;
	std::cout << "e\n";
	delete refresh;
	std::cout << "r\n";
	delete username;
	std::cout << "u\n";
	delete hbox;
	std::cout << "hb\n";
	delete grid;
        delete viewport;
	delete area;
	delete mainhbox;
	delete error;
	delete vbox;
	delete timer;
	std::cout << "Going to ifs\n";
	if (dragoninfo != nullptr) {
		delete dragoninfo;
	}
	if (creationwindow != nullptr) {
		delete creationwindow;
	}
	if (filterWindow != nullptr) {
		delete filterWindow;
	}
	if (commandwindow != nullptr) {
		delete commandwindow;
	}
	if (txt != nullptr) {
		delete[] txt;
	}
	std::cout << "Everything disappeared\n";
}

void mainPage::showInfo(jstring* data, int n) {
	if (dragoninfo != nullptr) {
		delete dragoninfo;
		dragoninfo = nullptr;
	}	
	dragoninfo = new dragonInfo(nullptr, data, n, jnienv, jcl, loginStr, passwordStr, txt, this);
	dragoninfo->show();
}

void mainPage::infoDestroy() {
	dragoninfo = nullptr;
}

void mainPage::showCreation() {
	if (creationwindow != nullptr) {
		delete creationwindow;
		creationwindow = nullptr;
	}
	std::cout << "Deleted\n";	
	creationwindow = new creationWindow(nullptr, jnienv, jcl, loginStr, passwordStr, txt, this);
	std::cout << "Created\n";
	creationwindow->show();
}

void mainPage::creationDestroy() {
	creationwindow = nullptr;
}

void mainPage::destroyCommand() {
	commandwindow = nullptr;
}

void mainPage::createFilter(int n) {
	jobjectArray dragons = getAllDragons();
	int dct;
	if (dragons == NULL) {
		dct = 0;
	}
	else {
		dct = jnienv->GetArrayLength(dragons);
	}
	valueSet[n].clear();
	for (int i = 0; i < dct; ++i) {
                jobjectArray curDr = jobjectArray(jnienv->GetObjectArrayElement(dragons, i));
		valueSet[n].insert(jstring(jnienv->GetObjectArrayElement(curDr, n)));
	}
	std::cout << "Set size " << valueSet[n].size() << "\n";
	if (filterWindow != nullptr) {
		delete filterWindow;
		filterWindow = nullptr;
	}
	std::cout << "Deleted\n";	
	filterWindow = new filter(nullptr, &valueSet[n], n, txt[n+7], jnienv, jcl, loginStr, passwordStr, txt, this);
	std::cout << "Created\n";
	filterWindow->show();
}

void mainPage::destroyFilter() {
	filterWindow = nullptr;
}

void mainPage::changeUser(QString login, QString password) {
	loginStr = login;
	passwordStr = password;
	username->setText(loginStr);
}
