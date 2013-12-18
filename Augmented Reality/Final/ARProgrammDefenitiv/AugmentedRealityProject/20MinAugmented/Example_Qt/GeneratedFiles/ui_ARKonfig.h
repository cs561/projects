/********************************************************************************
** Form generated from reading UI file 'ARKonfig.ui'
**
** Created by: Qt User Interface Compiler version 4.8.5
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_ARKONFIG_H
#define UI_ARKONFIG_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QApplication>
#include <QtGui/QButtonGroup>
#include <QtGui/QGridLayout>
#include <QtGui/QHeaderView>
#include <QtGui/QLabel>
#include <QtGui/QMainWindow>
#include <QtGui/QPushButton>
#include <QtGui/QRadioButton>
#include <QtGui/QSpinBox>
#include <QtGui/QStatusBar>
#include <QtGui/QWidget>

QT_BEGIN_NAMESPACE

class Ui_ARKonfigWindow
{
public:
    QWidget *centralwidget;
    QPushButton *StartButton;
    QWidget *layoutWidget;
    QGridLayout *gridLayout;
    QLabel *label_4;
    QLabel *label;
    QSpinBox *camIdLeft;
    QLabel *label_2;
    QSpinBox *camIdRight;
    QPushButton *StopButton;
    QLabel *label_5;
    QWidget *layoutWidget_2;
    QGridLayout *gridLayout_2;
    QRadioButton *StrongEyeL;
    QRadioButton *StrongEyeR;
    QLabel *label_3;
    QStatusBar *statusbar;

    void setupUi(QMainWindow *ARKonfigWindow)
    {
        if (ARKonfigWindow->objectName().isEmpty())
            ARKonfigWindow->setObjectName(QString::fromUtf8("ARKonfigWindow"));
        ARKonfigWindow->resize(179, 298);
        centralwidget = new QWidget(ARKonfigWindow);
        centralwidget->setObjectName(QString::fromUtf8("centralwidget"));
        StartButton = new QPushButton(centralwidget);
        StartButton->setObjectName(QString::fromUtf8("StartButton"));
        StartButton->setGeometry(QRect(10, 250, 161, 23));
        layoutWidget = new QWidget(centralwidget);
        layoutWidget->setObjectName(QString::fromUtf8("layoutWidget"));
        layoutWidget->setGeometry(QRect(10, 50, 154, 73));
        gridLayout = new QGridLayout(layoutWidget);
        gridLayout->setObjectName(QString::fromUtf8("gridLayout"));
        gridLayout->setContentsMargins(0, 0, 0, 0);
        label_4 = new QLabel(layoutWidget);
        label_4->setObjectName(QString::fromUtf8("label_4"));
        QFont font;
        font.setPointSize(12);
        label_4->setFont(font);

        gridLayout->addWidget(label_4, 0, 0, 1, 2);

        label = new QLabel(layoutWidget);
        label->setObjectName(QString::fromUtf8("label"));

        gridLayout->addWidget(label, 1, 0, 1, 1);

        camIdLeft = new QSpinBox(layoutWidget);
        camIdLeft->setObjectName(QString::fromUtf8("camIdLeft"));
        camIdLeft->setValue(1);

        gridLayout->addWidget(camIdLeft, 1, 1, 1, 1);

        label_2 = new QLabel(layoutWidget);
        label_2->setObjectName(QString::fromUtf8("label_2"));

        gridLayout->addWidget(label_2, 2, 0, 1, 1);

        camIdRight = new QSpinBox(layoutWidget);
        camIdRight->setObjectName(QString::fromUtf8("camIdRight"));

        gridLayout->addWidget(camIdRight, 2, 1, 1, 1);

        StopButton = new QPushButton(centralwidget);
        StopButton->setObjectName(QString::fromUtf8("StopButton"));
        StopButton->setGeometry(QRect(10, 220, 161, 23));
        label_5 = new QLabel(centralwidget);
        label_5->setObjectName(QString::fromUtf8("label_5"));
        label_5->setGeometry(QRect(10, 10, 161, 31));
        QFont font1;
        font1.setPointSize(16);
        label_5->setFont(font1);
        layoutWidget_2 = new QWidget(centralwidget);
        layoutWidget_2->setObjectName(QString::fromUtf8("layoutWidget_2"));
        layoutWidget_2->setGeometry(QRect(10, 130, 95, 67));
        gridLayout_2 = new QGridLayout(layoutWidget_2);
        gridLayout_2->setObjectName(QString::fromUtf8("gridLayout_2"));
        gridLayout_2->setContentsMargins(0, 0, 0, 0);
        StrongEyeL = new QRadioButton(layoutWidget_2);
        StrongEyeL->setObjectName(QString::fromUtf8("StrongEyeL"));

        gridLayout_2->addWidget(StrongEyeL, 2, 0, 1, 1);

        StrongEyeR = new QRadioButton(layoutWidget_2);
        StrongEyeR->setObjectName(QString::fromUtf8("StrongEyeR"));

        gridLayout_2->addWidget(StrongEyeR, 3, 0, 1, 1);

        label_3 = new QLabel(layoutWidget_2);
        label_3->setObjectName(QString::fromUtf8("label_3"));
        label_3->setFont(font);

        gridLayout_2->addWidget(label_3, 1, 0, 1, 1);

        ARKonfigWindow->setCentralWidget(centralwidget);
        statusbar = new QStatusBar(ARKonfigWindow);
        statusbar->setObjectName(QString::fromUtf8("statusbar"));
        ARKonfigWindow->setStatusBar(statusbar);

        retranslateUi(ARKonfigWindow);

        QMetaObject::connectSlotsByName(ARKonfigWindow);
    } // setupUi

    void retranslateUi(QMainWindow *ARKonfigWindow)
    {
        ARKonfigWindow->setWindowTitle(QApplication::translate("ARKonfigWindow", "Konfig", 0, QApplication::UnicodeUTF8));
        StartButton->setText(QApplication::translate("ARKonfigWindow", "Starten", 0, QApplication::UnicodeUTF8));
        label_4->setText(QApplication::translate("ARKonfigWindow", "Kamera Einstellungen", 0, QApplication::UnicodeUTF8));
        label->setText(QApplication::translate("ARKonfigWindow", "Linke Kamera", 0, QApplication::UnicodeUTF8));
        label_2->setText(QApplication::translate("ARKonfigWindow", "Rechte Kamera", 0, QApplication::UnicodeUTF8));
        StopButton->setText(QApplication::translate("ARKonfigWindow", "Beenden", 0, QApplication::UnicodeUTF8));
        label_5->setText(QApplication::translate("ARKonfigWindow", "AR Konfiguration", 0, QApplication::UnicodeUTF8));
        StrongEyeL->setText(QApplication::translate("ARKonfigWindow", "Links", 0, QApplication::UnicodeUTF8));
        StrongEyeR->setText(QApplication::translate("ARKonfigWindow", "Rechts", 0, QApplication::UnicodeUTF8));
        label_3->setText(QApplication::translate("ARKonfigWindow", "Starkes Auge", 0, QApplication::UnicodeUTF8));
    } // retranslateUi

};

namespace Ui {
    class ARKonfigWindow: public Ui_ARKonfigWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_ARKONFIG_H
