﻿if (!window.console) window.console = {};
if (!window.console.log) window.console.log = function () { };

var _serverStatus = new Array();
var _currentRenderLine = 0;
var _startTime = 0;
var _serverEnd = 0;

function setCanvas() {
    $("#frameBuffer").width($("#canvasWidth").val() + "px");
    $("#frameBuffer").height($("#canvasHeight").val() + "px");
    $('#frameBuffer').attr({ width: $("#canvasWidth").val(), height: $("#canvasHeight").val() });

    //scrollToTop();
}

function scrollToTop() {
    if (document.body.scrollTop != 0 || document.documentElement.scrollTop != 0) {
        window.scrollBy(0, -50);
        timeOut = setTimeout('scrollToTop()', 10);
    }
    else clearTimeout(timeOut);
}

function sendRequest(url, serverNo, lineNo) {
    var oReq = new XMLHttpRequest();
    oReq.open("GET", url, true);
    oReq.responseType = "arraybuffer";

    oReq.onload = function (oEvent) {

        var arrayBuffer = oReq.response; // Note: not oReq.responseText
        if (arrayBuffer) {
            var byteArray = new Uint8Array(arrayBuffer);
            drawLine(byteArray, lineNo);
            _currentRenderLine += 1;
            _serverStatus[serverNo][2]++;
            processLine(serverNo);

        }
    };


    oReq.send(null);

}

function sleep(ms) {
    var start = new Date().getTime(), expire = start + ms;
    while (new Date().getTime() < expire) { }
    return;
}

function drawLine(byteArray, line) {
    var w = $("#frameBuffer").width();
    var h = $("#frameBuffer").height();
    var ctx = $("#frameBuffer")[0].getContext("2d");
    var imageData = ctx.getImageData(0, 0, w, h);
    var data = imageData.data;
    var offset = line * w * 4;

    for (var x = 0; x < w * 4; x += 4) {

        data[x + offset + 1] = byteArray[x + 2];
        data[x + offset] = byteArray[x + 1];
        data[x + offset + 2] = byteArray[x + 3];
        data[x + offset + 3] = 255;
    }



    ctx.putImageData(imageData, 0, 0);
}

function getLine(url) {
    var urlArray = url.split("-");
    return urlArray[urlArray.length - 2];
}

function render() {
    _serverEnd = 0;
    _currentRenderLine = 0;
    setCanvas();
    var totalServers = prepareServerList();
    _startTime = new Date().getTime();





    for (var i = 0; i < totalServers; i++) {
        processLine(i);
    }

    /* while (line < cHeight) {
 
          var lineProcessed = false;
  
          for (var j = 0; j < totalServers; j++) {
              if (_serverStatus[j][2] == 1) {
                  lineProcessed = true;
                  sendRequest("http://" + _serverStatus[j][0] + ":" + _serverStatus[j][1] + "/render-" + cWidth + "-" + cHeight + "-" + line + "-" + maxRef, j, line);
  
                  break;
              }
          }
          if (lineProcessed) {
              line++;
          }
          sleep(5000);
      }*/


}

function processLine(serverNo) {
    var cWidth = $("#frameBuffer").width();
    var cHeight = $("#frameBuffer").height();
    var maxRef = $("#maxReflex").val();
    if (_currentRenderLine < cHeight) {
        sendRequest("http://" + _serverStatus[serverNo][0] + ":" + _serverStatus[serverNo][1] + "/render-" + cWidth + "-" + cHeight + "-" + _currentRenderLine + "-" + maxRef, serverNo, _currentRenderLine);
    } else {
        _serverEnd++;

    }

    if (_serverEnd == _serverStatus.length) {

        evaluate(new Date().getTime());
    }
}

function evaluate(endTime) {

    var timeDiff = endTime - _startTime;
    $("#statServers").val(_serverStatus.length);
    $("#statRenderTime").val((timeDiff / 1000) + "s");
    $("#statAvgRenderTime").val(Math.round(($("#frameBuffer").height() / (timeDiff / 10000))) / 10);

    var bestServer = new Array();
    var bestVal = 0;

    for (var i = 0; i < _serverStatus.length; i++) {
        if (_serverStatus[i][2] > bestVal) {
            bestVal = _serverStatus[i][2];
            bestServer = _serverStatus[i];
        }
    }

    $("#statTopServerName").val(bestServer[0] + ":" + bestServer[1]);
    $("#statTopServerCount").val(bestServer[2]);

    var worstServer = new Array();
    var worstVal = 99999999;

    for (var i = 0; i < _serverStatus.length; i++) {
        if (_serverStatus[i][2] < worstVal) {
            worstVal = _serverStatus[i][2];
            worstServer = _serverStatus[i];
        }
    }

    $("#statFlopServerName").val(worstServer[0] + ":" + worstServer[1]);
    $("#statFlopServerCount").val(worstServer[2]);
}

function evalTopServer() {

}

function prepareServerList() {
    _serverStatus = getServerList();
    var totalServers = _serverStatus.length;
    if (totalServers > 0) {

        for (var i = 0; i < totalServers; i++) {
            _serverStatus[i][2] = 0;
        }
        return totalServers;
    } else {
        alert("Please add at least one server");
        return 0;
    }

}

function addServer() {
    var srvList = getServerList();
    var newServer = [];

    newServer[0] = $("#serverAddress").val();
    newServer[1] = $("#serverPort").val();

    var row = document.createElement('tr');
    var counter = parseInt($("#serverList").attr("entries"));

    row.id = "S" + counter;
    counter += 1;
    $("#serverList").attr("entries", counter);

    row.innerHTML = displayArrayAsTable(newServer, 0, 1, row.id);
    document.getElementById('serverList').appendChild(row);

    var removeButton = $(document.createElement('button'));
    removeButton.text("Remove");
    removeButton.attr({ type: "button", onclick: "removeServer(this)" }).val("button");
    removeButton.addClass("standardButtonColor");
    removeButton.id = "#remButton" + row.id;
    removeButton.appendTo("#rem" + row.id);
}

function removeServer(btndel) {
    if (typeof (btndel) == "object") {
        $(btndel).closest("tr").remove();
    } else {
        return false;
    }
}

function getServerList() {
    var serverList = [];

    $("table#serverList tr[id^=S]").each(function () {
        var arrayOfThisRow = [];
        var tableData = $(this).find('td');
        if (tableData.length > 0) {
            tableData.each(function () { arrayOfThisRow.push($(this).text()); });
            serverList.push(arrayOfThisRow);
        }
    });
    return serverList;
}


function displayArrayAsTable(array, from, to, id) {


    var array = array || [],
        from = from || 0,
        to = to || 0;

    var html = '';

    if (array.length < 1) {
        return;
    }

    if (to == 0) {
        to = array.length - 1;
    }

    for (var x = from; x < to + 1; x++) {

        html += '<td>' + array[x] + '</td>';

    }

    html += '<td id="rem' + id + '"></td>';





    return html;

}


