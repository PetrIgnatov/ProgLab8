#include <QWidget>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <jni.h>
#include <QGridLayout>
#include "commandWindow.h"
#include <QVBoxLayout>
#include "helpfulStuff.h"
#include <iostream>
#include <QPainter>
#include <QPen>
#include <QColor>
#include <QTextEdit>
#include "mainPage.h"
#include <QKeyEvent>

commandWindow::commandWindow(QWidget* parent, JNIEnv* env, jclass* cl, QString login, QString password, QString* txt, bool* lock, mainPage* mp) : QWidget(parent) {
	jnienv = env;
	jcl = cl;
	mainpage = mp;
	loginStr = login;
	passwordStr = password;
	updateLock = lock;
	*updateLock = true;
	QPalette backgroundPal = QPalette();
	this->setFixedHeight(500);
	this->setFixedWidth(800);
	backgroundPal.setColor(QPalette::Window, Qt::white);
	this->setAutoFillBackground(true);
	this->setPalette(backgroundPal);
	vbox = new QVBoxLayout(this);
	hbox = new QHBoxLayout();
	terminalO = new QTextEdit();
	terminalO->setReadOnly(true);
	terminalO->setStyleSheet("color: white; background: #60000000; border: none; font-size: 12px");
	terminalI = new QLineEdit();
	terminalI->setStyleSheet("color: white; background: #60000000; border: none; font-size: 12px");
	send = new QPushButton(txt[18]);
	send->setStyleSheet("color: white; background: #00ba18; border: none; font-size: 12px");
	QObject::connect(send, &QPushButton::clicked, this, &commandWindow::onBtnClick);
	vbox->addWidget(terminalO);
	hbox->addWidget(terminalI);
	hbox->addWidget(send);
	vbox->addLayout(hbox);
	
}

void commandWindow::closeEvent(QCloseEvent* event) {
	delete send;
	delete terminalO;
	delete terminalI;
	delete hbox;
	delete vbox;
	mainpage->destroyCommand();
	*updateLock = false;
}

void commandWindow::paintEvent(QPaintEvent* e) {
	Q_UNUSED(e);
	drawBackground();
}

void commandWindow::drawBackground() {
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

void commandWindow::keyPressEvent(QKeyEvent* event) {
	if (event->key() == Qt::Key_Enter || event->key() == Qt::Key_Return) {
		onBtnClick();
	}
}

void commandWindow::onBtnClick() {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "sendCommand", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}	
	QString inp = terminalI->text();
	jstring response = jstring(jnienv->CallStaticObjectMethod(*jcl, method, QStr_to_jstr(jnienv, inp), QStr_to_jstr(jnienv, loginStr), QStr_to_jstr(jnienv, passwordStr)));
	QString rsp = jnienv->GetStringUTFChars(response, nullptr);
	terminalO->setText(rsp);
	if (rsp == "Вы успешно зашли в систему") {
		int sp = 0;
		QString l = "", p = "";
		for (int i = 0; i < inp.size(); ++i) {
			if (inp[i] == ' ') {
				sp+=1;
			}
			else if (sp == 1) {
				l += inp[i];
			}
			else if (sp == 2) {
				p += inp[i];
			}
		}
		mainpage->changeUser(l, p);
	}
	else if (rsp == "exit") {
		mainpage->destroy();
	}
}
