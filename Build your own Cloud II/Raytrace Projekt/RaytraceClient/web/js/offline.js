function testCanvas() {
    //alert("Test");
    var canvas = document.getElementById("frameBuffer");
    var cWidth = canvas.width;
    var cHeight = canvas.height;
    var ctx = canvas.getContext("2d");
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
    var canvas = document.getElementById("frameBuffer");
    var ctx = canvas.getContext("2d");

    canvas.style.width = document.getElementById("canvasWidth").value + "px";
    canvas.style.height = document.getElementById("canvasHeight").value + "px";
    scrollToTop();
}

function scrollToTop() {
    if (document.body.scrollTop != 0 || document.documentElement.scrollTop != 0) {
        window.scrollBy(0, -50);
        timeOut = setTimeout('scrollToTop()', 10);
    }
    else clearTimeout(timeOut);
}