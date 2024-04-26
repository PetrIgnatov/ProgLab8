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

connectionWindow::connectionWindow(QWidget* parent, JNIEnv* env, jclass* cl) : QWidget(parent) {
	jnienv = env;
	jcl = cl;
	vbox = new QVBoxLayout(this);
	grid = new QGridLayout();
	host = new QLabel("IP", this);
	hostIn = new QLineEdit(this);
	port = new QLabel("Port", this);
	portIn = new QLineEdit(this);
	error = new QLabel("a", this);
	confirm = new QPushButton("CONFIRM", this);
	QObject::connect(confirm, &QPushButton::clicked, this, &connectionWindow::onClick);
	grid->addWidget(host, 0, 0);
	grid->addWidget(hostIn, 0, 1);
	grid->addWidget(port, 1, 0);
	grid->addWidget(portIn, 1, 1);
	vbox->addLayout(grid);
	vbox->addWidget(confirm);
	//vbox->addWidget(error);
}

void connectionWindow::paintEvent(QPaintEvent* e) {
	Q_UNUSED(e);
	drawBackground();
}

void connectionWindow::drawBackground() {
	QPainter painter(this);
	painter.setPen(Qt::NoPen);
	painter.setBrush(QBrush("#ffffff"));
	painter.drawRect(0,0,800,500);
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

void connectionWindow::onClick() {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "connect", "(Ljava/lang/String;)Z");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}	
	jboolean connected = jnienv->CallStaticBooleanMethod(*jcl, method, QStr_to_jstr(jnienv, hostIn->text()));
	if (connected) {
		error->setText("CONNECTED!");
	}
	else {
		error->setText("NOT CONNECTED! TRY ANOTHER IP OR PORT!");
	}
}
