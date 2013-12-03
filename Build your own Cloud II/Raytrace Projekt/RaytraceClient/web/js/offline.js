var _serverStatus = new Array();
var _currentRenderLine = 0;
var _renderFinished = false;

function testCanvas() {
    var cWidth = $("#frameBuffer").width();
    var cHeight = $("#frameBuffer").height();
    $('#frameBuffer').attr({ width: cWidth, height: cHeight });
    var ctx = $("#frameBuffer")[0].getContext("2d");
    var imageData = ctx.getImageData(0, 0, cWidth, cHeight);
    var data = imageData.data;
    for (var y = 0; y < cHeight; ++y) {

        for (var x = 0; x < cWidth; ++x) {
            var index = (y * cWidth + x) * 4;
            var value = x * y & 0xff;

            data[index] = value;	// red
            data[++index] = value;	// green
            data[++index] = value;	// blue
            data[++index] = 255;	// alpha
        }
    }

    ctx.putImageData(imageData, 0, 0);
}

function setCanvas() {

    $("#frameBuffer").width($("#canvasWidth").val() + "px");
    $("#frameBuffer").height($("#canvasHeight").val() + "px");
    $('#frameBuffer').attr({ width: $("#canvasWidth").val(), height: $("#canvasHeight").val() });
    scrollToTop();
}

function scrollToTop() {
    if (document.body.scrollTop != 0 || document.documentElement.scrollTop != 0) {
        window.scrollBy(0, -50);
        timeOut = setTimeout('scrollToTop()', 10);
    }
    else clearTimeout(timeOut);
}

function sendRequest(url, serverNo, lineNo) {
    if (!_renderFinished) {
        var oReq = new XMLHttpRequest();
        oReq.open("GET", url, true);
        oReq.responseType = "arraybuffer";

        oReq.onload = function (oEvent) {

            var arrayBuffer = oReq.response; // Note: not oReq.responseText
            if (arrayBuffer) {
                var byteArray = new Uint8Array(arrayBuffer);
                drawLine(byteArray, lineNo);
                _currentRenderLine += 1;
                processLine(serverNo);

            }
        };


        oReq.send(null);
    }
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
    setCanvas();
    var totalServers = prepareServerList();






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
        renderFinished = true;
    }
}

function prepareServerList() {
    _serverStatus = getServerList();
    var totalServers = _serverStatus.length;
    if (totalServers > 0) {

        for (var i = 0; i < totalServers; i++) {
            _serverStatus[i][2] = 1;
        }
        console.log(_serverStatus);
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


