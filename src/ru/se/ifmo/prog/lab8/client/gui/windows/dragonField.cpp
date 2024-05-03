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
#include <QPainter>

dragonField::dragonField(QWidget* parent) : QWidget(parent) {
	this->setFixedWidth(600);
	this->setFixedHeight(750);
        QPalette backgroundPal = QPalette();
        backgroundPal.setColor(QPalette::Window, Qt::white);
        this->setAutoFillBackground(true);
        this->setPalette(backgroundPal);
}

void dragonField::paintEvent(QPaintEvent* e) {
	Q_UNUSED(e);
	drawBackground();
}

void dragonField::drawBackground() {
	QPainter painter(this);
	painter.setPen(Qt::NoPen);
	painter.setBrush(QBrush("#E6E000"));
	painter.drawPolygon(new QPointF[] {QPointF(0, 0), QPointF(600, 0), QPointF(600, 750), QPointF(0, 750)}, 4);
}
