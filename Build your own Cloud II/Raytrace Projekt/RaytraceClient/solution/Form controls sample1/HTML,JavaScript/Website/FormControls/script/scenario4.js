//// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF
//// ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO
//// THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
//// PARTICULAR PURPOSE.
////
//// Copyright (c) Microsoft Corporation. All rights reserved

function makeBold() {
    // make the selected text bold
    document.execCommand('bold', false, null);
}

function spellCheckChange() {
    document.getElementById("spellCheckText").spellcheck = document.getElementById("spellCheckSetting").checked;
}

function reply() {
    var inputBox = document.getElementById("spellCheckText");

    if (!inputBox.readOnly) {
        return;     // already in reply mode
    }

    // a readonly text input control will not be spellchecked since it's not editable. It's good for displaying original email. Here readonly is turned off for reply.
    inputBox.readOnly = false;

    // temporarily turn off spellcheck, so that the following content edit will not be spellchecked.
    inputBox.spellcheck = false;
    inputBox.value = "\n\n--Original Message--\n" + inputBox.value;

    // turn back to user setting.
    inputBox.spellcheck = document.getElementById("spellCheckSetting").checked;
}
