#include <QWidget>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <jni.h>
#include <QGridLayout>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QScrollArea>
#include "filter.h"
#include <QPainter>
#include <cmath>
#include <algorithm>
#include <QFormLayout>
#include <iostream>
#include "helpfulStuff.h"
#include <set>
#include <QCheckBox>
#include <iterator>

filter::filter(QWidget* parent, std::set<jstring>* values, int col, QString title, JNIEnv* env, jclass* cl, QString loginArg, QString passwordArg, QString* txt, mainPage* mainpage) : QWidget(parent) {
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
	mp = mainpage;
	val = values;
	column = col;
	checkbox = new QCheckBox*[values->size()];
	titleLabel = new QLabel(title);
	titleLabel->setStyleSheet("color: black");
	vbox = new QVBoxLayout(this);
	save = new QPushButton(txt[5]);
	scrollable = new QVBoxLayout(this);
	area = new QScrollArea();
	viewport = new QWidget();
	QObject::connect(save, &QPushButton::clicked, this, &filter::changeFilter);
	std::set<jstring>::iterator it = values->begin();
	vbox->addWidget(titleLabel);
	for (int i = 0; (i < values->size() && it != values->end()); ++i) {
		checkbox[i] = new QCheckBox(jnienv->GetStringUTFChars(*it, nullptr));
		checkbox[i]->setStyleSheet("color: black");
		++it;
		std::cout << "Adding\n";
		scrollable->addWidget(checkbox[i]);
	}
	viewport->setLayout(scrollable);
	area->setWidget(viewport);
	vbox->addWidget(area);
	vbox->addSpacing(1);	
	vbox->addWidget(save);
}

void filter::closeEvent(QCloseEvent* event) {
	for (int i = 0; i < val->size(); ++i) {
		delete checkbox[i];
	}
	delete[] checkbox;
	delete save;
	delete vbox;
	if (mp != nullptr) {
		mp->destroyFilter();
	}
	std::cout << "Edit window closed\n";
}

void filter::paintEvent(QPaintEvent* e) {
	Q_UNUSED(e);
	drawBackground();
}

void filter::drawBackground() {
	QPainter painter(this);
	painter.setPen(Qt::NoPen);
	painter.setBrush(QBrush("#FFFFFF"));
	painter.drawPolygon(new QPointF[] {QPointF(0, 0), QPointF(480, 0), QPointF(480, 360), QPointF(0, 360)}, 4);
}

void filter::changeFilter() {
	jmethodID method = jnienv->GetStaticMethodID(*jcl, "setFilter", "(I[Ljava/lang/String;)Z");
	if (method == 0) {
		std::cout << "Error!";
		return;
	}
	int checkSize = 0;
	for (int i = 0; i < val->size(); ++i) {
		if (checkbox[i]->checkState() == Qt::Checked) {
			checkSize++;
		}
	}
	jobjectArray filter = jnienv->NewObjectArray(checkSize, jnienv->FindClass("java/lang/String"), NULL);
	std::set<jstring>::iterator it = val->begin();
	int loccount = 0;
	for (int i = 0; (i < val->size() && it != val->end()); ++i) {
		if (checkbox[i]->checkState() == Qt::Checked) {
			std::cout << "FILTER " << jnienv->GetStringUTFChars(*it, nullptr);
			jnienv->SetObjectArrayElement(filter, loccount, *it);
			++loccount;
		}
		++it;
	}
	jboolean response = jnienv->CallStaticBooleanMethod(*jcl, method, column, filter);
	mp->drawGuiTable();
	this->close();
	//jnienv->DeleteLocalRef(dr);
}
