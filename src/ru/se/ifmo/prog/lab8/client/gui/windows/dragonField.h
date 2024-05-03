#pragma once

#include <QWidget>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <jni.h>
#include <QGridLayout>
#include <QVBoxLayout>
#include <QHBoxLayout>
#include <QScrollArea>

class dragonField : public QWidget {
	public:
		dragonField(QWidget* parent = 0);
	protected:
		void paintEvent(QPaintEvent* event);
	private:
		void drawBackground();
};
