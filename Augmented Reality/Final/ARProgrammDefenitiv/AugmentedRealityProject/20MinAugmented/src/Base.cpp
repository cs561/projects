#include "Base.h"
#include "ARWindow.h"

#include <math.h>
#include <sstream>

#include <QtGui>
#include <QtOpenGL>
#include <QGraphicsWebView>
#include <QWebFrame>
#include <QFileInfo>

#include <GL/gl.h>

#ifndef GL_MULTISAMPLE
#define GL_MULTISAMPLE  0x809D
#endif

#include <metaioSDK/IARELInterpreter.h>
#include <metaioSDK/IMetaioSDKWin32.h>
#include <metaioSDK/GestureHandler.h>

Base::Base(ARWindow* mainWindow, bool StrongEye, int CamID) :
	QGraphicsScene(),
	m_initialized(false),
	m_pGestureHandler(0),
	m_pMetaioSDK(0)
{
	// Create a webview	and connect the load finished event
	m_StrongEye = StrongEye;
	m_camID = CamID;
	m_pWebView = new QGraphicsWebView();
	QObject::connect(m_pWebView, SIGNAL(loadFinished(bool)), this, SLOT(loadFinished(bool)));
	m_pWebView->hide();
	addItem(m_pWebView);
	// Create the AREL interpreter
	m_pArelInterpreter = metaio::CreateARELInterpreterQT(this, m_pWebView);
}


Base::~Base()
{
	// Should deinitialize SDK first so that the camera can be reused when starting another tutorial
	m_pMetaioSDK->pauseTracking();
	m_pMetaioSDK->pauseSensors();
	m_pMetaioSDK->stopCamera();
	m_pMetaioSDK->pause();

	delete m_pArelInterpreter;
	m_pArelInterpreter = 0;

	delete m_pMetaioSDK;
	m_pMetaioSDK = 0;

	delete m_pGestureHandler;
	m_pGestureHandler = 0;

	removeItem(m_pWebView);
	delete m_pWebView;
	m_pWebView = 0;
}


void Base::drawBackground(QPainter* painter, const QRectF & rect)
{
	painter->save();
	if (painter->paintEngine()->type()	!= QPaintEngine::OpenGL2)
	{
		qWarning("TutorialBaseAREL: drawBackground needs a QGLWidget to be set as viewport on the graphics view");
		return;
	}

	if(!m_initialized)
	{
		m_initialized = true;

		m_pMetaioSDK = metaio::CreateMetaioSDKWin32();
		m_pMetaioSDK->initializeRenderer(800, 600, metaio::ESCREEN_ROTATION_0, metaio::ERENDER_SYSTEM_OPENGL_EXTERNAL);
		m_pMetaioSDK->startCamera(m_camID, 640, 480);

		m_pGestureHandler = new metaio::GestureHandler(m_pMetaioSDK);
		m_pArelInterpreter->initialize(m_pMetaioSDK, m_pGestureHandler);
		m_pArelInterpreter->registerCallback(this);
	}

	// Enable anti-aliasing
	glPushAttrib(GL_ENABLE_BIT);
	glEnable(GL_MULTISAMPLE);
	glEnable(GL_LINE_SMOOTH);

	m_pArelInterpreter->update();

	// Trigger an update
	QTimer::singleShot(20, this, SLOT(update()));

	painter->restore();

	// This is a workaround to render the webpages correctly
	glDisable(GL_CULL_FACE);
	glDisable(GL_DEPTH_TEST);
}


void Base::loadFinished(bool loaded)
{
	if(loaded)
		m_pArelInterpreter->loadFinished();
}


void Base::mouseMoveEvent(QGraphicsSceneMouseEvent *mouseEvent)
{
	QGraphicsScene::mouseMoveEvent(mouseEvent);

	// Forward event to gesture handler (needed for drag gesture, just like the mouse press/release events)
	if(m_pGestureHandler)
		m_pGestureHandler->touchesMoved(mouseEvent->scenePos().x(), mouseEvent->scenePos().y());
}


void Base::mousePressEvent(QGraphicsSceneMouseEvent* mouseEvent)
{
	QGraphicsScene::mousePressEvent(mouseEvent);

	// Forward event to gesture handler
	if(m_pGestureHandler)
		m_pGestureHandler->touchesBegan(mouseEvent->scenePos().x(), mouseEvent->scenePos().y());
}


void Base::mouseReleaseEvent(QGraphicsSceneMouseEvent* mouseEvent)
{
	QGraphicsScene::mouseReleaseEvent(mouseEvent);

	// Forward event to gesture handler
	if(m_pGestureHandler)
		m_pGestureHandler->touchesEnded(mouseEvent->scenePos().x(), mouseEvent->scenePos().y());
}

void Base::loadContent()
{
	if (m_StrongEye){
		// AREL filename, e.g. "../../assets/Assets1/arelConfig1.xml"
		std::stringstream filename;
		filename << "../../../tutorialContent_crossplatform/Tutorial" << m_tutorialNumber << "/Demo/index.xml";

		m_pArelInterpreter->loadARELFile(filename.str());
	}
}

void Base::onSDKReady()
{
	loadContent();

	// Show the web view after content is loaded and splash screen is finished
	m_pWebView->show();
}
