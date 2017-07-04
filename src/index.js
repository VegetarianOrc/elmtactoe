'use strict';

// Require index.html so it gets copied to dist
require('./index.html');

var Elm = require('./Main.elm');
var mountNode = document.getElementById('main');

// .embed() can take an optional second argument. This would be an object describing the data we need to start a program, i.e. a userID or some token
var app = Elm.Main.embed(mountNode);

app.ports.toSvgCoords.subscribe(function(coords) {
  //https://www.sitepoint.com/how-to-translate-from-dom-to-svg-coordinates-and-back-again/
  var svg = document.getElementById('board')
  var pt = svg.createSVGPoint();
  pt.x = coords[0];
  pt.y = coords[1];
  var svgP = pt.matrixTransform(svg.getScreenCTM().inverse());
  app.ports.receiveSvgCoords.send([svgP.x, svgP.y]);
});
