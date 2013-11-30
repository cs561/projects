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
    scrollToTop();
}

function scrollToTop() {
    if (document.body.scrollTop != 0 || document.documentElement.scrollTop != 0) {
        window.scrollBy(0, -50);
        timeOut = setTimeout('scrollToTop()', 10);
    }
    else clearTimeout(timeOut);
}

function WebSocket() {
    var oReq = new XMLHttpRequest();
    oReq.open("GET", "");
}