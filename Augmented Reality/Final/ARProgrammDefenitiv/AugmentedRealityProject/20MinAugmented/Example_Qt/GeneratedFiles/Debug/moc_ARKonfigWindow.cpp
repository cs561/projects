/****************************************************************************
** Meta object code from reading C++ file 'ARKonfigWindow.h'
**
** Created by: The Qt Meta Object Compiler version 63 (Qt 4.8.5)
**
** WARNING! All changes made in this file will be lost!
*****************************************************************************/

#include "../../../src/ARKonfigWindow.h"
#if !defined(Q_MOC_OUTPUT_REVISION)
#error "The header file 'ARKonfigWindow.h' doesn't include <QObject>."
#elif Q_MOC_OUTPUT_REVISION != 63
#error "This file was generated using the moc from 4.8.5. It"
#error "cannot be used with the include files from this version of Qt."
#error "(The moc has changed too much.)"
#endif

QT_BEGIN_MOC_NAMESPACE
static const uint qt_meta_data_ARKonfigWindow[] = {

 // content:
       6,       // revision
       0,       // classname
       0,    0, // classinfo
       2,   14, // methods
       0,    0, // properties
       0,    0, // enums/sets
       0,    0, // constructors
       0,       // flags
       0,       // signalCount

 // slots: signature, parameters, type, tag, flags
      16,   15,   15,   15, 0x09,
      35,   15,   15,   15, 0x09,

       0        // eod
};

static const char qt_meta_stringdata_ARKonfigWindow[] = {
    "ARKonfigWindow\0\0pressCloseButton()\0"
    "pressStartButton()\0"
};

void ARKonfigWindow::qt_static_metacall(QObject *_o, QMetaObject::Call _c, int _id, void **_a)
{
    if (_c == QMetaObject::InvokeMetaMethod) {
        Q_ASSERT(staticMetaObject.cast(_o));
        ARKonfigWindow *_t = static_cast<ARKonfigWindow *>(_o);
        switch (_id) {
        case 0: _t->pressCloseButton(); break;
        case 1: _t->pressStartButton(); break;
        default: ;
        }
    }
    Q_UNUSED(_a);
}

const QMetaObjectExtraData ARKonfigWindow::staticMetaObjectExtraData = {
    0,  qt_static_metacall 
};

const QMetaObject ARKonfigWindow::staticMetaObject = {
    { &QMainWindow::staticMetaObject, qt_meta_stringdata_ARKonfigWindow,
      qt_meta_data_ARKonfigWindow, &staticMetaObjectExtraData }
};

#ifdef Q_NO_DATA_RELOCATION
const QMetaObject &ARKonfigWindow::getStaticMetaObject() { return staticMetaObject; }
#endif //Q_NO_DATA_RELOCATION

const QMetaObject *ARKonfigWindow::metaObject() const
{
    return QObject::d_ptr->metaObject ? QObject::d_ptr->metaObject : &staticMetaObject;
}

void *ARKonfigWindow::qt_metacast(const char *_clname)
{
    if (!_clname) return 0;
    if (!strcmp(_clname, qt_meta_stringdata_ARKonfigWindow))
        return static_cast<void*>(const_cast< ARKonfigWindow*>(this));
    if (!strcmp(_clname, "Ui::ARKonfigWindow"))
        return static_cast< Ui::ARKonfigWindow*>(const_cast< ARKonfigWindow*>(this));
    return QMainWindow::qt_metacast(_clname);
}

int ARKonfigWindow::qt_metacall(QMetaObject::Call _c, int _id, void **_a)
{
    _id = QMainWindow::qt_metacall(_c, _id, _a);
    if (_id < 0)
        return _id;
    if (_c == QMetaObject::InvokeMetaMethod) {
        if (_id < 2)
            qt_static_metacall(this, _c, _id, _a);
        _id -= 2;
    }
    return _id;
}
QT_END_MOC_NAMESPACE
