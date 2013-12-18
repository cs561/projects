#include <QApplication>
#include "ARKonfigWindow.h"
#include "ARWindow.h"

int main(int argc, char *argv[])
{
	#if _DEBUG
		// Do not print memory leaks at end of program (QtWebKit causes some invalid leaks, see Menu class)
		_CrtSetDbgFlag(_crtDbgFlag &~ _CRTDBG_LEAK_CHECK_DF);
	#endif

	QApplication app(argc, argv);

	app.setApplicationName("Example_Qt");


	//ARKonfigWindow window;


	ARWindow window(true,1,0);
	window.show();
	return app.exec();
}