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

function sendRequest(url) {
    var oReq = new XMLHttpRequest();
    oReq.open("GET", url, true);
    oReq.responseType = "arraybuffer";

    oReq.onload = function (oEvent) {

        var arrayBuffer = oReq.response; // Note: not oReq.responseText
        if (arrayBuffer) {
            var byteArray = new Uint8Array(arrayBuffer);
            console.log(byteArray);
            drawLine(byteArray, getLine(url));

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

    for (var x = 0; x < w; ++x) {
        var index = (line * w + x) * 4;
        var value = byteArray[index];

        data[index] = byteArray[index];	// red
        data[++index] = byteArray[index];	// green
        data[++index] = byteArray[index];	// blue
        data[++index] = 255;	// alpha
    }

    ctx.putImageData(imageData, 0, 0);
}

function getLine(url) {
    var urlArray = url.split("-");
    return urlArray[urlArray.length - 2];
}

function testSend() {
    setCanvas();
    var cWidth = $("#frameBuffer").width();
    var cHeight = $("#frameBuffer").height();
    var maxRef = $("#maxReflex").val();

    for (var i = 0; i < cHeight; i++) {
        sendRequest("http://localhost:1337/render-" + cWidth + "-" + cHeight + "-" + i + "-" + maxRef);
    }
}

function addServer() {
    var srvList = getServerList();
    var newServer = [];

    newServer[0] = $("#serverAddress").val();
    newServer[1] = $("#serverPort").val();
    newServer[2] = $("#serverQuality").val();



    var row = document.createElement('tr');
    var counter = parseInt($("#serverList").attr("entries"));
    
    row.id = "S" + counter;
    counter += 1;
    $("#serverList").attr("entries", counter);
    



    row.innerHTML = displayArrayAsTable(newServer, 0, 2, row.id);
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

    $("table#serverList tr").each(function () {
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


