#include "ARWindow.h"
#include "Base.h"

#include <QtGui>
#include <QGraphicsScene>
#include <QGLWidget>
#include <QSyntaxHighlighter>
#include <QTabWidget>
#include <QFile>
#include <QWebInspector>

ARWindow::ARWindow(bool StrongEyeLeft, int RightCamID, int LeftCamID){
	setupUi(this);	

	m_StrongEyeLeft = StrongEyeLeft;
	m_RightCamID = RightCamID;
	m_LeftCamID = LeftCamID;

	QGLWidget *glWidget = new QGLWidget(QGLFormat(QGL::SampleBuffers));
	QGLWidget *glWidgetRight = new QGLWidget(QGLFormat(QGL::SampleBuffers));
	m_pGraphicsViewLeft = graphicsViewLeft;
	m_pGraphicsViewLeft->setViewport(glWidget);
	m_pGraphicsViewLeft->setFrameShape(QFrame::NoFrame);

	m_pGraphicsViewRight = graphicsViewRight;
	m_pGraphicsViewRight->setViewport(glWidgetRight);
	m_pGraphicsViewRight->setFrameShape(QFrame::NoFrame);


	// Do not show context menu in web view
	m_pGraphicsViewLeft->setContextMenuPolicy(Qt::NoContextMenu);
	m_pGraphicsViewRight->setContextMenuPolicy(Qt::NoContextMenu);

	Base *ImplLeft = new Base(this, m_StrongEyeLeft, m_LeftCamID);
	Base *ImplRight = new Base(this, !m_StrongEyeLeft, m_RightCamID);	

	m_pGraphicsViewLeft->setScene(ImplLeft);
	m_pGraphicsViewRight->setScene(ImplRight);

};

ARWindow::~ARWindow(){
	delete m_pGraphicsViewLeft;
	m_pGraphicsViewLeft = 0;

	delete m_pGraphicsViewRight;
	m_pGraphicsViewRight = 0;
};
