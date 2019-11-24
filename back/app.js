// Web application server to connect to client Application with HTTP
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var somethingApiRouter = require('./api/something');

var app = express();



// Database
/* mySQL connection init */


// Middlewares
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());


// API 
app.use('/api/something', somethingApiRouter);

// exports for www script to start listening
module.exports = app;
