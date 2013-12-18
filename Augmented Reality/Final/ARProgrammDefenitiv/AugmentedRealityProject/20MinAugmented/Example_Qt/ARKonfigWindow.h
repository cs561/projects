#ifndef ARKONFIG_H
#define ARKONFIG_H

#include "ui_ARKonfig.h"

QT_BEGIN_NAMESPACE
class QGraphicsView;
class QBoxLayout;
QT_END_NAMESPACE


class ARKonfigWindow : public QMainWindow, public Ui::ARKonfigWindow
{
Q_OBJECT
public:
	ARKonfigWindow();
	virtual ~ARKonfigWindow();

protected slots:
	void pressCloseButton();
	void pressStartButton();
};

#endif