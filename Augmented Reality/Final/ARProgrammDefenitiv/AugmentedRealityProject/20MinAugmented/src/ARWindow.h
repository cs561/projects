#ifndef ARWINDOW_H
#define ARWINDOW_H

#include <vector>

#include <QVBoxLayout>
#include <QWidget>
#include <QWebView>

#include "ui_ARWindow.h"

QT_BEGIN_NAMESPACE
class QGraphicsView;
class QBoxLayout;
QT_END_NAMESPACE


class ARWindow : public QDialog, public Ui::ARWindow
{
	Q_OBJECT

public:
	ARWindow(bool, int, int);
	virtual ~ARWindow();

	QBoxLayout* getButtonBar();
	int m_pCamID;

protected:
	QGraphicsView*				m_pGraphicsView;
	QGraphicsView*				m_pGraphicsViewRight;
	
	bool						m_StrongEyeLeft;
	int							m_RightCamID;
	int							m_LeftCamID;
};


#endif
