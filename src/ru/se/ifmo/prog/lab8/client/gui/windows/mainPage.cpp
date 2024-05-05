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
#include <QTimer> 
#include <QScrollBar>

mainPage::mainPage(QWidget* parent, JNIEnv* env, jclass* cl, QString loginArg, QString passwordArg) : QWidget(parent) {
	jnienv = env;
	jcl = cl;
	this->setFixedWidth(1280);
	this->setFixedHeight(920);
	dragCount = 0;
	loginStr = loginArg;
	passwordStr = passwordArg;
	QPalette backgroundPal = QPalette();
	backgroundPal.setColor(QPalette::Window, Qt::white);
	this->setAutoFillBackground(true);
	this->setPalette(backgroundPal);
	timer = new QTimer();
	timer->setInterval(1000);
	QObject::connect(timer, &QTimer::timeout, this, &mainPage::drawGui);
	timer->start();
	vscroll = 0;
	hscroll = 0;
	firstUpdate = true;
	drawGui();
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
}

void mainPage::changeDragon(QString num, int n) {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "change", "(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z");
        if (method == 0) {
                std::cout << "Error!";
                return;
        }
	jobjectArray dr = jnienv->NewObjectArray(9, jnienv->FindClass("java/lang/String"), NULL);
	for (int i = 0; i < 9; ++i) {
//		std::cout << ((QLineEdit*)(dragon[n][(i < 3 ? i+1 : i+2)]))->text().toStdString() << "\n";
		jstring jstr = QStr_to_jstr(jnienv, ((QLineEdit*)(dragon[n][(i < 3 ? i+1 : i+2)]))->text());
		jnienv->SetObjectArrayElement(dr, i, jstr);
		jnienv->DeleteLocalRef(jstr);
	}
	jboolean response = jnienv->CallStaticBooleanMethod(*jcl, method, QStr_to_jstr(jnienv,num), dr, QStr_to_jstr(jnienv, loginStr), QStr_to_jstr(jnienv, passwordStr));
	jnienv->DeleteLocalRef(dr);
}

void mainPage::drawGui() {
	std::cout << "Drawing gui\n";
	/*if (area != NULL && area != nullptr) {
		vscroll = area->verticalScrollBar()->value();
		hscroll = area->horizontalScrollBar()->value();
	}*/
	if (!firstUpdate) {
		std::cout << vscroll << " " << hscroll << "\n";
		vscroll = area->verticalScrollBar()->value();
		hscroll = area->horizontalScrollBar()->value();
		for (int i = 0; i < dragCount; ++i) {
			for (int j = 0; j < 13; ++j) {
				delete dragon[i][j];
			}
			delete[] dragon[i];
		}
		delete grid;
		delete viewport;
	}
	else {
		vbox = new QVBoxLayout(this);
		hbox = new QHBoxLayout();
		tryComs = new QPushButton("TRY COMMANDS");
		tryComs->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 40px");
		QObject::connect(tryComs, &QPushButton::clicked, this, &mainPage::openComW);
		exit = new QPushButton("EXIT");
		exit->setStyleSheet("color: white; background: #F00000; border: none; font-size: 40px");
		refresh = new QPushButton("REFRESH");
		refresh->setStyleSheet("color: white; background: #0B00DA; border: none; font-size: 40px");
		QObject::connect(refresh, &QPushButton::clicked, this, &mainPage::drawGui);
		username = new QLabel(loginStr);

		mainhbox = new QHBoxLayout();
		area = new QScrollArea();
		dragonfield = new dragonField();
		hscroll = 0;
		vscroll = 0;
		hbox->addStretch();
		hbox->addWidget(tryComs);
		hbox->addWidget(refresh);
		hbox->addWidget(exit);
		hbox->addWidget(username);
		vbox->addLayout(hbox);
		vbox->addSpacing(50);
		//Тут поменял
		mainhbox->addWidget(area);
		mainhbox->addWidget(dragonfield);
		vbox->addLayout(mainhbox);
		firstUpdate = false;
	}
	std::cout << "Memory cleared\n";
	grid = new QGridLayout();
	viewport = new QWidget;
	std::cout << "Reinitialized\n";
	QLabel** header = new QLabel*[11];
	header[0] = new QLabel("  ID  ");
	header[1] = new QLabel("  NAME  ");
	header[2] = new QLabel("  X  ");
	header[3] = new QLabel("  Y  ");
	header[4] = new QLabel("  CREATION DATE  ");
	header[5] = new QLabel("  AGE  ");
	header[6] = new QLabel("  COLOR  ");
	header[7] = new QLabel("  TYPE  ");
	header[8] = new QLabel("  CHARACTER  ");
	header[9] = new QLabel("  DEPTH  ");
	header[10] = new QLabel("  NUMBER OF TREASURES  ");
	for (int i = 0; i < 11; ++i) {
		header[i]->setStyleSheet("color: white; background: black; border: none; font-size: 12px");
		std::cout << "Adding to grid\n";
		grid->addWidget(header[i], 0, i);
	}
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "getDragons", "(Ljava/lang/String;Ljava/lang/String;)[[Ljava/lang/String;");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}
	jobjectArray dragons = jobjectArray(jnienv->CallStaticObjectMethod(*jcl, method, jnienv->NewStringUTF("show"), QStr_to_jstr(jnienv, loginStr), QStr_to_jstr(jnienv, passwordStr)));
	if (dragons == NULL) {
		dragCount = 0;
	}
	else {
		dragCount = jnienv->GetArrayLength(dragons);
	}
	dragon = new QWidget**[dragCount]; 
	for (int i = 0; i < dragCount; ++i) {
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
					dragon[i][j] = new QPushButton("DEL", nullptr);
					dragon[i][j]->setStyleSheet("color: white; background: #f00000; border: none; font-size: 12px");
					break;
				case 11:
					dragon[i][j] = new QPushButton("SAVE", nullptr);
					dragon[i][j]->setStyleSheet("color: white; background: #00BA18; border: none; font-size: 12px");
					break;
				default:
					dragon[i][j] = new QLineEdit(jnienv->GetStringUTFChars(jstring(jnienv->GetObjectArrayElement(curDr, j)), nullptr));
					dragon[i][j]->setStyleSheet("color: black; background: transparent; border: none; font-size: 12px");
					break;
			}
			std::cout << "Adding dragons to grid\n";
			grid->addWidget(dragon[i][j], i+1, j);
		}
		std::cout << "Dragon added!\n";
		QString id = ((QLabel*)(dragon[i][0]))->text();
		QObject::connect((QPushButton*)(dragon[i][11]), &QPushButton::clicked, this, [id, i, this](){changeDragon(id, i);});
		QObject::connect((QPushButton*)(dragon[i][12]), &QPushButton::clicked, this, [id, this](){deleteDragon(id);});
	}
	std::cout << "Adding grid to viewport\n";
	viewport->setLayout(grid);
	area->setWidget(viewport);
	area->verticalScrollBar()->setValue(vscroll);
	area->horizontalScrollBar()->setValue(hscroll);
}

void mainPage::openComW() {
	commandWindow* commandwindow = new commandWindow(nullptr, jnienv, jcl, loginStr, passwordStr);
	commandwindow->show();	
}

mainPage::~mainPage() {
	std::cout << "Deleting\n";
}

void mainPage::destroy() {
        for (int i = 0; i < dragCount; ++i) {
                delete[] dragon[i];
        }
       	delete tryComs;
	delete exit;
	delete refresh;
	delete username;
	delete hbox;
	delete grid;
        delete viewport;
	delete area;
	delete mainhbox;
	delete vbox;
	delete timer;
	std::cout << "Everything disappeared\n";
}
