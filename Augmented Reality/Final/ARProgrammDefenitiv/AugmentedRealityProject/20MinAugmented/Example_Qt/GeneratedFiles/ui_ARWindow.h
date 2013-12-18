/********************************************************************************
** Form generated from reading UI file 'ARWindow.ui'
**
** Created by: Qt User Interface Compiler version 4.8.5
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_ARWINDOW_H
#define UI_ARWINDOW_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QApplication>
#include <QtGui/QButtonGroup>
#include <QtGui/QDialog>
#include <QtGui/QGraphicsView>
#include <QtGui/QGridLayout>
#include <QtGui/QHeaderView>
#include <QtGui/QWidget>

QT_BEGIN_NAMESPACE

class Ui_ARWindow
{
public:
    QWidget *layoutWidget;
    QGridLayout *gridLayout;
    QGraphicsView *graphicsViewLeft;
    QGraphicsView *graphicsViewRight;

    void setupUi(QDialog *ARWindow)
    {
        if (ARWindow->objectName().isEmpty())
            ARWindow->setObjectName(QString::fromUtf8("ARWindow"));
        ARWindow->resize(1024, 768);
        ARWindow->setMinimumSize(QSize(1024, 768));
        ARWindow->setMaximumSize(QSize(1024, 768));
        layoutWidget = new QWidget(ARWindow);
        layoutWidget->setObjectName(QString::fromUtf8("layoutWidget"));
        layoutWidget->setGeometry(QRect(0, 0, 1021, 761));
        gridLayout = new QGridLayout(layoutWidget);
        gridLayout->setObjectName(QString::fromUtf8("gridLayout"));
        gridLayout->setContentsMargins(0, 0, 0, 0);
        graphicsViewLeft = new QGraphicsView(layoutWidget);
        graphicsViewLeft->setObjectName(QString::fromUtf8("graphicsViewLeft"));
        graphicsViewLeft->setMinimumSize(QSize(507, 759));
        graphicsViewLeft->setMaximumSize(QSize(507, 759));

        gridLayout->addWidget(graphicsViewLeft, 0, 0, 1, 1);

        graphicsViewRight = new QGraphicsView(layoutWidget);
        graphicsViewRight->setObjectName(QString::fromUtf8("graphicsViewRight"));
        graphicsViewRight->setMinimumSize(QSize(506, 759));
        graphicsViewRight->setMaximumSize(QSize(506, 759));

        gridLayout->addWidget(graphicsViewRight, 0, 1, 1, 1);


        retranslateUi(ARWindow);

        QMetaObject::connectSlotsByName(ARWindow);
    } // setupUi

    void retranslateUi(QDialog *ARWindow)
    {
        ARWindow->setWindowTitle(QApplication::translate("ARWindow", "Augmented 20 Minuten", 0, QApplication::UnicodeUTF8));
    } // retranslateUi

};

namespace Ui {
    class ARWindow: public Ui_ARWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_ARWINDOW_H
