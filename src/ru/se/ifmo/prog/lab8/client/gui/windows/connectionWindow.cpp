#include <QWidget>
#include <string>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <jni.h>
#include <QGridLayout>
#include "connectionWindow.h"
#include <QVBoxLayout>
#include "helpfulStuff.h"
#include <iostream>
#include <QPainter>
#include <QPen>
#include <QColor>
#include "mainPage.h"
#include <thread>
#include <QHBoxLayout>
#include <fstream>

void connectionWindow::getMsg() {
	/*jmethodID method = jnienv->GetStaticMethodID(*jcl, "getMsg", "()V");
	if (method == 0) {
		// std::cout << "Error!";
		return;
	}	
	jnienv->CallStaticVoidMethod(*jcl, method);*/
	while(true) {
		// std::cout << "Ok\n";
	}
}

connectionWindow::connectionWindow(QWidget* parent, JNIEnv* env, jclass* cl) : QWidget(parent) {
	jnienv = env;
	jcl = cl;
	errType = 0;
	QPalette backgroundPal = QPalette();
	std::ifstream f("conndata.conf");
	std::string fAddr = "", fPort = "";
	f >> fAddr >> fPort;
	f.close();
	this->setFixedHeight(500);
	this->setFixedWidth(800);
	backgroundPal.setColor(QPalette::Window, Qt::white);
	this->setAutoFillBackground(true);
	this->setPalette(backgroundPal);
	langBox = new QHBoxLayout();
	langStr = new QString[]{"РУССКИЙ", "ESPANOL", "HRVATSKI", "SLOVENČINA"};
	langBtn = new QPushButton*[4];
	for (int i = 0; i < 4; ++i) {
		langBtn[i] = new QPushButton(langStr[i]);
		QObject::connect(langBtn[i], &QPushButton::clicked, this, [this, i](){setLang(i);});
		langBox->addWidget(langBtn[i]);
	}
	vbox = new QVBoxLayout(this);
	grid = new QGridLayout();
	txt = nullptr;
	getText();
	host = new QLabel(txt[0], this);
	host->setStyleSheet("color: black; background: #a0ffffff; font-size: 48px");
	hostIn = new QLineEdit(QString::fromStdString(fAddr), this);
	hostIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	port = new QLabel(txt[1], this);
	port->setStyleSheet("color: black; background: #a0ffffff; font-size: 48px");
	portIn = new QLineEdit(QString::fromStdString(fPort), this);
	portIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	error = new QLabel("", this);
	error->setStyleSheet("color: black; background: #a0ffffff; font-size: 24px");
	confirm = new QPushButton(txt[4], this);
	confirm->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 48px");
	QObject::connect(confirm, &QPushButton::clicked, this, &connectionWindow::onConClick);
	butGrid = new QGridLayout();
	login = new QLabel(txt[2]);
	login->setStyleSheet("color: black; background: #a0ffffff; font-size: 48px");
	loginIn = new QLineEdit();
	loginIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	password = new QLabel(txt[3]);
	password->setStyleSheet("color: black; background: #a0ffffff; font-size: 48px");
	passwordIn = new QLineEdit();
	passwordIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	signIn = new QPushButton(txt[6]);
	signIn->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 48px");
	reg = new QPushButton(txt[5]);
	reg->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 48px");
	QObject::connect(reg, &QPushButton::clicked, this, &connectionWindow::onRegClick);
	QObject::connect(signIn, &QPushButton::clicked, this, &connectionWindow::onSignInClick);
	grid->setHorizontalSpacing(10);
	grid->addWidget(host, 0, 0, Qt::AlignRight);
	grid->addWidget(hostIn, 0, 1);
	grid->addWidget(port, 1, 0, Qt::AlignRight);
	grid->addWidget(portIn, 1, 1);
	vbox->addStretch(1);
	vbox->addLayout(langBox);
	vbox->addLayout(grid);
	vbox->addWidget(confirm, 1, Qt::AlignBottom);
	vbox->addWidget(error, 1, Qt::AlignBottom | Qt::AlignHCenter);
}

connectionWindow::connectionWindow(QWidget* parent, int ph, JNIEnv* env, jclass* cl) : QWidget(parent) {
	jnienv = env;
	jcl = cl;
	errType = 0;
	QPalette backgroundPal = QPalette();
	this->setFixedHeight(500);
	this->setFixedWidth(800);
	backgroundPal.setColor(QPalette::Window, Qt::white);
	this->setAutoFillBackground(true);
	this->setPalette(backgroundPal);
	langBox = new QHBoxLayout();
	langStr = new QString[]{"РУССКИЙ", "ESPANOL", "HRVATSKI", "SLOVENČINA"};
	langBtn = new QPushButton*[4];
	for (int i = 0; i < 4; ++i) {
		langBtn[i] = new QPushButton(langStr[i]);
		QObject::connect(langBtn[i], &QPushButton::clicked, this, [this, i](){setLang(i);});
		langBox->addWidget(langBtn[i]);
	}
	vbox = new QVBoxLayout(this);
	grid = new QGridLayout();
	txt = nullptr;
	getText();
	host = new QLabel(txt[0], this);
	host->setStyleSheet("color: black; background: #a0ffffff; font-size: 48px");
	hostIn = new QLineEdit(this);
	hostIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	port = new QLabel(txt[1], this);
	port->setStyleSheet("color: black; background: #a0ffffff; font-size: 48px");
	portIn = new QLineEdit(this);
	portIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	error = new QLabel("", this);
	error->setStyleSheet("color: black; background: #a0ffffff; font-size: 24px");
	confirm = new QPushButton(txt[4], this);
	confirm->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 48px");
	QObject::connect(confirm, &QPushButton::clicked, this, &connectionWindow::onConClick);
	butGrid = new QGridLayout();
	login = new QLabel(txt[2]);
	login->setStyleSheet("color: black; background: #a0ffffff; font-size: 48px");
	loginIn = new QLineEdit();
	loginIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	password = new QLabel(txt[3]);
	password->setStyleSheet("color: black; background: #a0ffffff; font-size: 48px");
	passwordIn = new QLineEdit();
	passwordIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	signIn = new QPushButton(txt[6]);
	signIn->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 48px");
	reg = new QPushButton(txt[5]);
	reg->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 48px");
	QObject::connect(reg, &QPushButton::clicked, this, &connectionWindow::onRegClick);
	QObject::connect(signIn, &QPushButton::clicked, this, &connectionWindow::onSignInClick);
	if (ph == 0) {
	grid->setHorizontalSpacing(10);
	grid->addWidget(host, 0, 0, Qt::AlignRight);
	grid->addWidget(hostIn, 0, 1);
	grid->addWidget(port, 1, 0, Qt::AlignRight);
	grid->addWidget(portIn, 1, 1);
	vbox->addStretch(1);
	vbox->addLayout(langBox);
	vbox->addLayout(grid);
	vbox->addWidget(confirm, 1, Qt::AlignBottom);
	vbox->addWidget(error, 1, Qt::AlignBottom | Qt::AlignHCenter);
	}
	else {
		delete host;
        delete hostIn;
        delete port;
        delete portIn;
        delete confirm;
        host = nullptr;
        grid->setHorizontalSpacing(10);
        grid->addWidget(login, 0, 0, Qt::AlignRight);
        grid->addWidget(loginIn, 0, 1);
        grid->addWidget(password, 1, 0, Qt::AlignRight);
        grid->addWidget(passwordIn, 1, 1);
        butGrid->addWidget(reg, 0, 0);
        butGrid->addWidget(signIn, 0, 1);
	vbox->addStretch(1);
	vbox->addLayout(langBox);
	vbox->addLayout(grid);
        vbox->addLayout(butGrid);
        vbox->addWidget(error, 1, Qt::AlignBottom | Qt::AlignHCenter);
	}
}

void connectionWindow::paintEvent(QPaintEvent* e) {
	Q_UNUSED(e);
	drawBackground();
}

void connectionWindow::drawBackground() {
	QPainter painter(this);
	painter.setPen(Qt::NoPen);
	painter.setBrush(QBrush("#0b00da"));
	painter.drawPolygon(new QPointF[] {QPointF(640, 420), QPointF(380, 260), QPointF(530, 10), QPointF(790, 160)}, 4);
	painter.setBrush(QBrush("#f00000"));
	painter.drawPolygon(new QPointF[] {QPointF(40, 15), QPointF(15, 220), QPointF(240, 250)}, 3);
	painter.drawPolygon(new QPointF[] {QPointF(350, 390), QPointF(370, 430), QPointF(720, 230), QPointF(690, 190)}, 4);
	painter.drawPolygon(new QPointF[] {QPointF(410, 150), QPointF(450, 120), QPointF(645, 455), QPointF(615, 495)}, 4);
	painter.setBrush(QBrush("#e6e000"));
	painter.drawPolygon(new QPointF[] {QPointF(20, 450), QPointF(60, 250), QPointF(310, 295), QPointF(270, 490)}, 4);
	painter.drawPolygon(new QPointF[] {QPointF(330, 20), QPointF(480, 30), QPointF(360, 180)}, 3);
	painter.setBrush(QBrush("#000000"));
	painter.drawPolygon(new QPointF[] {QPointF(240, 12), QPointF(260, 5), QPointF(365, 365), QPointF(345, 372)}, 4);
	painter.drawPolygon(new QPointF[] {QPointF(190, 12), QPointF(210, 5), QPointF(350, 480), QPointF(330, 487)}, 4);
}

void connectionWindow::getText() {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "getConnectionText", "()[Ljava/lang/String;");
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

void connectionWindow::setLang(int l) {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "setLanguage", "(I)V");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}
	jnienv->CallStaticVoidMethod(*jcl, method, l);
	getText();
	setText();
}

void connectionWindow::setText() {
	if (host != nullptr) {
		host->setText(txt[0]);
		port->setText(txt[1]);
		confirm->setText(txt[4]);
	}
	login->setText(txt[2]);
	password->setText(txt[3]);
	signIn->setText(txt[6]);
	reg->setText(txt[5]);
	if (errType != 0) {
		error->setText(txt[errType]);
	}
}

void connectionWindow::onConClick() {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "connect", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (method == 0) {
		// std::cout << "Error!";
		return;
	}	
	jboolean connected = jnienv->CallStaticBooleanMethod(*jcl, method, QStr_to_jstr(jnienv, hostIn->text()), QStr_to_jstr(jnienv, portIn->text()));
	if (connected) {
		error->setText(txt[11]);
		errType = 11;
		std::ofstream f("conndata.conf");
		f << hostIn->text().toStdString() << " " << portIn->text().toStdString();
		f.close();
		changeWindow();
		return;
	}
	error->setText(txt[12]);
	errType = 12;
}

void connectionWindow::closeEvent(QCloseEvent* event) {
	this->destroy();
}

void connectionWindow::changeWindow() {
	delete host;
	delete hostIn;
	delete port;
	delete portIn;
	delete confirm;
	host = nullptr;
	grid->setHorizontalSpacing(10);
	grid->addWidget(login, 0, 0, Qt::AlignRight);
	grid->addWidget(loginIn, 0, 1);
	grid->addWidget(password, 1, 0, Qt::AlignRight);
	grid->addWidget(passwordIn, 1, 1);
	butGrid->addWidget(reg, 0, 0);
	butGrid->addWidget(signIn, 0, 1);
	vbox->addLayout(butGrid);
	vbox->addWidget(error, 1, Qt::AlignBottom | Qt::AlignHCenter);
}

void connectionWindow::onRegClick() {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "register", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}	
	jboolean success = jnienv->CallStaticBooleanMethod(*jcl, method, QStr_to_jstr(jnienv, loginIn->text()), QStr_to_jstr(jnienv, passwordIn->text()));
	if (success) {
		error->setText(txt[9]);
		errType = 9;
		this->createAnother();
		this->close();
	}
	error->setText(txt[10]);
	errType = 10;
}

void connectionWindow::onSignInClick() {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "signIn", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}	
	jboolean success = jnienv->CallStaticBooleanMethod(*jcl, method, QStr_to_jstr(jnienv, loginIn->text()), QStr_to_jstr(jnienv, passwordIn->text()));
	if (success) {
		error->setText(txt[7]);
		errType = 7;
		this->createAnother();
		this->close();
		return;
	}
	error->setText(txt[8]);
	errType = 8;
	return;
}

void connectionWindow::createAnother() {
	mainPage* page = new mainPage(0, jnienv, jcl, loginIn->text(), passwordIn->text());
	page->setWindowTitle("Main Page");
	page->show();
}

void connectionWindow::destroy() {
	// std::cout << "Destroying connection window\n";
	if (host != nullptr) {
		// std::cout << "Deleting host\n";
		delete host;
		// std::cout << "Deleting hostin\n";
		delete hostIn;
		// std::cout << "Deleting port\n";
		delete port;
		// std::cout << "Deleting portin\n";
		delete portIn;
		// std::cout << "Deleting confirm\n";
		delete confirm;
	}
	for (int i = 0; i < 4; ++i) {
		delete langBtn[i];
	}
	delete[] langBtn;
	delete langBox;
	// std::cout << "Deleted 1\n";
	delete error;
		delete login;
		delete password;
		delete loginIn;
		delete passwordIn;
		delete signIn;
		delete reg;
		delete butGrid;
	delete grid;
	delete vbox;
	delete[] txt;
	// std::cout << "Everything gone\n";
}
