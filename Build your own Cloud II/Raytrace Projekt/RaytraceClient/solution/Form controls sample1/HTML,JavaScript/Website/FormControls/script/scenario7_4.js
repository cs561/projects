//// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF
//// ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO
//// THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
//// PARTICULAR PURPOSE.
////
//// Copyright (c) Microsoft Corporation. All rights reserved

function loadMap() {
    var key = document.getElementById("developerKeyTextbox").value;
    var errorElement = document.getElementById("mapError");

    if ("" === key) {
        errorElement.innerText = "Error: no developer key.";
    } else {
        errorElement.innerText = "";

        var domain = "*";
        document.getElementById("mapContainer").contentWindow.postMessage(key, domain);
    }
}
