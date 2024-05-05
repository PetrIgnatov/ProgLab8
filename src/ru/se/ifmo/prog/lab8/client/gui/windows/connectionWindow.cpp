#include <QWidget>
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

void connectionWindow::getMsg() {
	/*jmethodID method = jnienv->GetStaticMethodID(*jcl, "getMsg", "()V");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}	
	jnienv->CallStaticVoidMethod(*jcl, method);*/
	while(true) {
		std::cout << "Ok\n";
	}
}

connectionWindow::connectionWindow(QWidget* parent, JNIEnv* env, jclass* cl) : QWidget(parent) {
	jnienv = env;
	jcl = cl;
	QPalette backgroundPal = QPalette();
	this->setFixedHeight(500);
	this->setFixedWidth(800);
	backgroundPal.setColor(QPalette::Window, Qt::white);
	this->setAutoFillBackground(true);
	this->setPalette(backgroundPal);
	vbox = new QVBoxLayout(this);
	grid = new QGridLayout();
	host = new QLabel("IP", this);
	host->setStyleSheet("color: black; background: transparent; font-size: 48px");
	hostIn = new QLineEdit(this);
	hostIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	port = new QLabel("Port", this);
	port->setStyleSheet("color: black; background: transparent; font-size: 48px");
	portIn = new QLineEdit(this);
	portIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	error = new QLabel("", this);
	error->setStyleSheet("color: black; background: #a0ffffff; font-size: 24px");
	confirm = new QPushButton("CONFIRM", this);
	confirm->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 48px");
	QObject::connect(confirm, &QPushButton::clicked, this, &connectionWindow::onConClick);
	grid->setHorizontalSpacing(10);
	grid->addWidget(host, 0, 0, Qt::AlignRight);
	grid->addWidget(hostIn, 0, 1);
	grid->addWidget(port, 1, 0, Qt::AlignRight);
	grid->addWidget(portIn, 1, 1);
	vbox->addStretch(1);
	vbox->addLayout(grid);
	vbox->addWidget(confirm, 1, Qt::AlignBottom);
	vbox->addWidget(error, 1, Qt::AlignBottom | Qt::AlignHCenter);
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

void connectionWindow::onConClick() {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "connect", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}	
	jboolean connected = jnienv->CallStaticBooleanMethod(*jcl, method, QStr_to_jstr(jnienv, hostIn->text()), QStr_to_jstr(jnienv, portIn->text()));
	if (connected) {
		error->setText("CONNECTED!");
		changeWindow();
		return;
	}
	error->setText("NOT CONNECTED! TRY ANOTHER IP OR PORT!");
}

void connectionWindow::changeWindow() {
	delete host;
	delete hostIn;
	delete port;
	delete portIn;
	delete confirm;
	butGrid = new QGridLayout();
	login = new QLabel("Login", this);
	login->setStyleSheet("color: black; background: transparent; font-size: 48px");
	loginIn = new QLineEdit(this);
	loginIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	password = new QLabel("Password", this);
	password->setStyleSheet("color: black; background: transparent; font-size: 48px");
	passwordIn = new QLineEdit(this);
	passwordIn->setStyleSheet("color: black; background: #60ffffff; font-size:48px");
	signIn = new QPushButton("SIGN IN", this);
	signIn->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 48px");
	reg = new QPushButton("REGISTER", this);
	reg->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 48px");
	QObject::connect(reg, &QPushButton::clicked, this, &connectionWindow::onRegClick);
	QObject::connect(signIn, &QPushButton::clicked, this, &connectionWindow::onSignInClick);
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
		error->setText("REGISTERED SUCCESSFULLY!");
		this->createAnother();
		this->close();
	}
	error->setText("TRY ANOTHER LOGIN OR PASSWORD!");
}

void connectionWindow::onSignInClick() {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "signIn", "(Ljava/lang/String;Ljava/lang/String;)Z");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}	
	jboolean success = jnienv->CallStaticBooleanMethod(*jcl, method, QStr_to_jstr(jnienv, loginIn->text()), QStr_to_jstr(jnienv, passwordIn->text()));
	if (success) {
		error->setText("SIGNED IN SUCCESSFULLY!");
		this->createAnother();
		this->close();
		return;
	}
	error->setText("TRY ANOTHER LOGIN OR PASSWORD!");
	return;
}

void connectionWindow::createAnother() {
	mainPage* page = new mainPage(0, jnienv, jcl, loginIn->text(), passwordIn->text());
	page->setWindowTitle("Main Page");
	page->show();
}
