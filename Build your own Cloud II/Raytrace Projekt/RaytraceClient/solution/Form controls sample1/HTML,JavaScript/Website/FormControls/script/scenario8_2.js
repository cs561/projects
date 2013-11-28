//// THIS CODE AND INFORMATION IS PROVIDED "AS IS" WITHOUT WARRANTY OF
//// ANY KIND, EITHER EXPRESSED OR IMPLIED, INCLUDING BUT NOT LIMITED TO
//// THE IMPLIED WARRANTIES OF MERCHANTABILITY AND/OR FITNESS FOR A
//// PARTICULAR PURPOSE.
////
//// Copyright (c) Microsoft Corporation. All rights reserved

var status = null;
var progress = null;
var completeTimer = null;

(function () {

    function initialize() {
        initializeProgress();   // initialize the file download example
    }

    document.addEventListener("DOMContentLoaded", initialize, false);
})();

// helper functions for CSS class
function hasClass(ele, cls) {
    return ele.className.match(new RegExp('(\\s|^)' + cls + '(\\s|$)'));
}

function addClass(ele, cls) {
    if (!this.hasClass(ele, cls)) {
        ele.className += " " + cls;
    }
}

function removeClass(ele, cls) {
    if (hasClass(ele, cls)) {
        var reg = new RegExp('(\\s|^)' + cls + '(\\s|$)');
        ele.className = ele.className.replace(reg, ' ');
    }
}

function initializeProgress() {
    progress = document.getElementById("downloadProgress");
    status = document.getElementById("downloadStatus");
}

function progressPauseResume() {
    var btn = document.getElementById("downloadPauseButton");
    var container = document.getElementById("downloadContainer");

    // Position -1 means the progress control is indeterminate. When determinate, position equals value/max.
    if (("hidden" === btn.style.visibility) || (-1 === progress.position)) {
        // still during connecting. can't pause/resume.
        return;
    }

    if (hasClass(btn, "paused")) {
        // the download was paused, therefore now we resume it.

        // Add your code here to resume download

        removeClass(progress, "paused");
        status.innerText = "Downloading";

        removeClass(btn, "paused");
        addClass(btn, "downloading");

        // add a tooltip to the container to tell what will happen if the user clicks again.
        container.title = "Pause";

        document.getElementById("increaseValue").disabled = false;
    }
    else if (hasClass(btn, "downloading")) {
        // the download was running, therefore now we pause it.

        // Add your code here to pause download

        addClass(progress, "paused");
        status.innerText = "Paused";

        removeClass(btn, "downloading");
        addClass(btn, "paused");

        // add a tooltip to the container to tell what will happen if the user clicks again.
        container.title = "Resume";

        document.getElementById("increaseValue").disabled = true;
    }
}

function progressSwitchDeterminate() {
    // Position -1 means the progress control is indeterminate.
    if (-1 === progress.position) {
        // Add your code here to finish the initialization/estimation and start the download

        // Do a fade-in animation whenever switching from indeterminate progress bar to determinate progress bar
        removeClass(progress, "switchDeterminate");
        addClass(progress, "switchDeterminate");

        progress.setAttribute("value", "0");

        status.innerText = "Downloading";
        document.getElementById("downloadPauseButton").style.visibility = "visible";    // show the pause/resume button
        document.getElementById("downloadContainer").title = "Pause";                   // can be paused now
        document.getElementById("increaseValue").disabled = false;                      // allow increasing the value of progress
        document.getElementById("switchDeterminate").disabled = true;
    }
}

function progressIncrease() {
    progress.value = progress.value + 0.2;  // default max is 1.0

    if (parseInt(progress.value) >= 1) {
        // Since the progress fill may need more time to animate to 100%, please wait for 1s here before hiding the download UI.
        completeTimer = setTimeout("downloadComplete();", 1000);
    }
}

function downloadComplete() {
    document.getElementById("downloadPauseButton").style.visibility = "hidden";
    document.getElementById("downloadControl").style.visibility = "hidden";
    status.innerText = "Completed";
    document.getElementById("increaseValue").disabled = true;
}

function progressReset() {
    // avoid the completion timer changing status after this is called
    clearTimeout(completeTimer);

    progress.setAttribute("value", "0");
    progress.removeAttribute("value");

    var btn = document.getElementById("downloadPauseButton");
    removeClass(btn, "paused");
    addClass(btn, "downloading");
    btn.style.visibility = "hidden"; // because the indeterminate activity of connecting to the server can't be paused/resumed.

    document.getElementById("downloadControl").style.visibility = "visible";

    status.innerText = "Connecting";

    removeClass(progress, "paused");

    document.getElementById("downloadContainer").title = "";    // remove the "pause"/"resume" tooltip since it's not allowed in this state.

    document.getElementById("increaseValue").disabled = true; // should switch to determinate first.
    document.getElementById("switchDeterminate").disabled = false;
}