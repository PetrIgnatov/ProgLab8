#include <QWidget>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <jni.h>
#include <QGridLayout>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QScrollArea>
#include "creationWindow.h"
#include <QPainter>
#include <cmath>
#include <algorithm>
#include <QFormLayout>
#include <iostream>
#include "helpfulStuff.h"
#include <QComboBox>

creationWindow::creationWindow(QWidget* parent, JNIEnv* env, jclass* cl, QString loginArg, QString passwordArg, QString* txt, mainPage* mainpage) : QWidget(parent) {
	this->setFixedWidth(480);
	this->setFixedHeight(360);
	mp = mainpage;
        QPalette backgroundPal = QPalette();
        backgroundPal.setColor(QPalette::Window, Qt::white);
        this->setAutoFillBackground(true);
        this->setPalette(backgroundPal);
	loginStr = loginArg;
	passwordStr = passwordArg;
	jnienv = env;
	jcl = cl;
	text = txt;
	line = new QWidget*[9];
	vbox = new QVBoxLayout(this);
	form = new QFormLayout();
	save = new QPushButton(txt[5]);
	error = new QLabel("");
	label = new QLabel*[9];
        error->setStyleSheet("color: black; background: transparent; border: none; font-size: 12px");
	QObject::connect(save, &QPushButton::clicked, this, &creationWindow::createDragon);
	for (int i = 0; i < 9; ++i) {
		switch(i) {
		case 4:
			line[i] = new QComboBox();
			line[i]->setMinimumWidth(200);

			line[i]->setStyleSheet("background-color: white; border: none; color: black; QComboBox QAbstractItemView { background-color: white; }");

					((QComboBox*)(line[i]))->addItems({"GREEN", "ORANGE", "YELLOW", "WHITE"});
			break;
		case 5:
			line[i] = new QComboBox();
			line[i]->setMinimumWidth(200);

			line[i]->setStyleSheet("background-color: white; border: none; color: black; QComboBox QAbstractItemView { background-color: white; }");

					((QComboBox*)(line[i]))->addItems({"WATER", "UNDERGROUND", "AIR"});
			break;
		case 6:
			line[i] = new QComboBox();
			line[i]->setMinimumWidth(200);

			line[i]->setStyleSheet("background-color: white; border: none; color: black; QComboBox QAbstractItemView { background-color: white; }");

					((QComboBox*)(line[i]))->addItems({"EVIL", "GOOD", "CHAOTIC", "CHAOTIC_EVIL", "FICKLE"});
			break;
			default:
				line[i] = new QLineEdit("");
				break;
		}
		label[i] = new QLabel(txt[8+i+(i>=3?1:0)]);
		label[i]->setStyleSheet("color: black; background: transparent; border: none");
		form->addRow(label[i], line[i]);
	}
	vbox->addLayout(form);
	vbox->addWidget(save);
	vbox->addWidget(error);
}

void creationWindow::setText(QString* txt) {
	for (int i = 0; i < 9; ++i) {
		label[i]->setText(txt[8+i+(i>=4?1:0)]);
	}
	save->setText(txt[5]);
}

void creationWindow::closeEvent(QCloseEvent* event) {
	for (int i = 0; i < 9; ++i) {
		delete line[i];
		delete label[i];
	}
	delete[] label;
	delete[] line;
	delete form;
	delete save;
	// std::cout << "save deleted\n";
	delete error;
	// std::cout << "Error deleted\n";
	delete vbox;
	mp->creationDestroy();
	// std::cout << "Edit window closed\n";
}

void creationWindow::paintEvent(QPaintEvent* e) {
	Q_UNUSED(e);
	drawBackground();
}

void creationWindow::drawBackground() {
	QPainter painter(this);
	painter.setPen(Qt::NoPen);
	painter.setBrush(QBrush("#FFFFFF"));
	painter.drawPolygon(new QPointF[] {QPointF(0, 0), QPointF(480, 0), QPointF(480, 360), QPointF(0, 360)}, 4);
}

void creationWindow::createDragon() {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "create", "([Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}
	jobjectArray dr = jnienv->NewObjectArray(9, jnienv->FindClass("java/lang/String"), NULL);
	for (int i = 0; i < 9; ++i) {
		jstring jstr;
//		// std::cout << ((QLineEdit*)(dragon[n][(i < 3 ? i+1 : i+2)]))->text().toStdString() << "\n";
		if (i >= 4 && i <= 6) {
			jstr = QStr_to_jstr(jnienv, ((QComboBox*)(line[i]))->currentText());
		}
		else {
		jstr = QStr_to_jstr(jnienv, ((QLineEdit*)(line[i]))->text());
		}
		jnienv->SetObjectArrayElement(dr, i, jstr);
		jnienv->DeleteLocalRef(jstr);
	}
	jboolean response = jnienv->CallStaticBooleanMethod(*jcl, method, dr, QStr_to_jstr(jnienv, loginStr), QStr_to_jstr(jnienv, passwordStr));
	jnienv->DeleteLocalRef(dr);
	if (!response) {
		error->setText(text[22]);
	}
	else {
		mp->drawGuiTable();
		this->close();
	}
}
