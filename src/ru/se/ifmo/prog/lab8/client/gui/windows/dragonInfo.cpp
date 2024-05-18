#include <QWidget>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <jni.h>
#include <QGridLayout>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QScrollArea>
#include "dragonInfo.h"
#include <QPainter>
#include <cmath>
#include <algorithm>
#include <QFormLayout>
#include <iostream>
#include "helpfulStuff.h"

dragonInfo::dragonInfo(QWidget* parent, jstring* values, int n, JNIEnv* env, jclass* cl, QString loginArg, QString passwordArg, QString* txt, mainPage* mainpage) : QWidget(parent) {
	this->setFixedWidth(480);
	this->setFixedHeight(360);
        QPalette backgroundPal = QPalette();
        backgroundPal.setColor(QPalette::Window, Qt::white);
        this->setAutoFillBackground(true);
        this->setPalette(backgroundPal);
	loginStr = loginArg;
	passwordStr = passwordArg;
	jnienv = env;
	jcl = cl;
	text = txt;
	mp = mainpage;
	datasize = n;
	line = new QWidget*[datasize];
	vbox = new QVBoxLayout(this);
	form = new QFormLayout();
	save = new QPushButton(txt[5]);
	error = new QLabel("");
	label = new QLabel*[11];
	error->setStyleSheet("color: black; background: transparent; border: none; font-size: 10px");
	QObject::connect(save, &QPushButton::clicked, this, &dragonInfo::changeDragon);
	for (int i = 0; i < std::min(n, 11); ++i) {
		label[i] = new QLabel(txt[7+i]);
		if (i != 0 && i != 4) {
			line[i] = new QLineEdit(jnienv->GetStringUTFChars(values[i], nullptr));
		}
		else {
			line[i] = new QLabel(jnienv->GetStringUTFChars(values[i], nullptr));
		}
		label[i]->setStyleSheet("color: black; background: transparent; border: none");
		line[i]->setStyleSheet("color: black; background: transparent; border: none");
		form->addRow(label[i], line[i]);
	}
	vbox->addLayout(form);
	vbox->addWidget(save);
	vbox->addWidget(error);
}

void dragonInfo::closeEvent(QCloseEvent* event) {
	for (int i = 0; i < 11; ++i) {
		// std::cout << i << "\n";
		delete line[i];
		delete label[i];
	}
	delete[] label;
	delete[] line;
	// std::cout << "arrays deleted\n";
	delete form;
	delete save;
	delete error;
	delete vbox;
	if (mp != nullptr) {
		mp->infoDestroy();
	}
	// std::cout << "Edit window closed\n";
}

void dragonInfo::paintEvent(QPaintEvent* e) {
	Q_UNUSED(e);
	drawBackground();
}

void dragonInfo::drawBackground() {
	QPainter painter(this);
	painter.setPen(Qt::NoPen);
	painter.setBrush(QBrush("#FFFFFF"));
	painter.drawPolygon(new QPointF[] {QPointF(0, 0), QPointF(480, 0), QPointF(480, 360), QPointF(0, 360)}, 4);
}

void dragonInfo::changeDragon() {
	QString num = ((QLabel*)(line[0]))->text();
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "change", "(Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}
	jobjectArray dr = jnienv->NewObjectArray(9, jnienv->FindClass("java/lang/String"), NULL);
	for (int i = 0; i < 9; ++i) {
		// std::cout << (i < 3 ? i+1 : i+2) << "\n";
//		// std::cout << ((QLineEdit*)(dragon[n][(i < 3 ? i+1 : i+2)]))->text().toStdString() << "\n";
		jstring jstr = QStr_to_jstr(jnienv, ((QLineEdit*)(line[(i < 3 ? i+1 : i+2)]))->text()); //Проверить аргументы
		jnienv->SetObjectArrayElement(dr, i, jstr);
		jnienv->DeleteLocalRef(jstr);
	}
	jboolean response = jnienv->CallStaticBooleanMethod(*jcl, method, QStr_to_jstr(jnienv,num), dr, QStr_to_jstr(jnienv, loginStr), QStr_to_jstr(jnienv, passwordStr));
	jnienv->DeleteLocalRef(dr);
	if (!response) {
		// std::cout << "Error changing dragon\n";
		error->setText(text[20]);
	}
	else {
		error->setText("");
		mp->drawGuiTable();
		this->close();
	}
}
