#pragma once

#include <QWidget>
#include <QPushButton>
#include <QLineEdit>
#include <QLabel>
#include <jni.h>
#include <QGridLayout>
#include <QVBoxLayout>
#include <string>
#include <QHBoxLayout>

class connectionWindow : public QWidget {
	public:
		connectionWindow(QWidget* parent = 0, JNIEnv* env = nullptr, jclass* cl = nullptr);
	protected:
		void paintEvent(QPaintEvent* event);
		void closeEvent(QCloseEvent* event);
	private:
		QVBoxLayout* vbox;
		QHBoxLayout* langBox;
		QGridLayout* grid;
		QGridLayout* butGrid;
		QLabel* host;
		QLabel* port;
		QLabel* error;
		QLineEdit* hostIn;
		QLineEdit* portIn;
		QPushButton* confirm;
		QLabel* login;
		QLabel* password;
		QLineEdit* loginIn;
		QLineEdit* passwordIn;
		QPushButton* signIn;
		QPushButton* reg;
		JNIEnv* jnienv;
		jclass* jcl;
		QPushButton** langBtn;
		QString* langStr;
		QString* txt;
		int errType;
		void drawBackground();
		void changeWindow();
		void getMsg();
		void createAnother();
	private slots:
		void getText();
		void setText();
		void setLang(int l);
		void onConClick();
		void onRegClick();
		void onSignInClick();
		void destroy();
};
