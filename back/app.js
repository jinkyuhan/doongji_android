// Web application server to connect to client Application with HTTP
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var somethingApiRouter = require('./api/something');
var mysql = require('mysql');


var app = express();



// Connection to Database
var connection = mysql.createConnection({
	host: 'mysqldb.cv9byhmyfmai.us-east-1.rds.amazonaws.com',
	user: 'admin',
	password: '12345678',
	port: 3306,
	database: 'DOONG_JI'
});
connection.connect(function(err){
	console.log('try Connecting to mySQL DB server...')
	if(err){
		console.error(err);
	} else {
		console.log('mySQL DB server successful connected');
	}
});

// var sql = `SELECT * FROM init`;
// connection.query(sql, function(err, rows, fields){
// 	if(!err){
// 		console.log(rows);
// 	} else {
// 		console.log('query error');
// 	}
// });


// Middlewares
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());


// API 
app.use('/api/something', somethingApiRouter);

// exports for www script to start listening
module.exports = app;
