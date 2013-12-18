#include "ARKonfigWindow.h"
//#include "ARWindow.h"

ARKonfigWindow::ARKonfigWindow()
{
	setupUi(this);

	QObject::connect(StopButton, SIGNAL(clicked()), this, SLOT(pressCloseButton()));
	QObject::connect(StartButton, SIGNAL(clicked()), this, SLOT(pressStartButton()));
}


ARKonfigWindow::~ARKonfigWindow()
{

}

void ARKonfigWindow::pressCloseButton(){
	close();
}

void ARKonfigWindow::pressStartButton(){

}