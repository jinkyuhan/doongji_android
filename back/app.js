// Web application server to connect to client Application with HTTP
var express = require('express');
var cookieParser = require('cookie-parser');
var bodyParser = require('body-parser');
var logger = require('morgan');
var membersApiRouter = require('./api/members');
var groupsApiRouter = require('./api/groups');
// var sentryRouter = require('./services/sentry');
var postmanRouter = require('./services/postman')
var models = require('./models/models');
var app = express();


// Connect and sync to DB
console.log('DB connecting...');
models.sequelize.sync({ force: false }).then(() => {
	console.log('database successful sync');
}).catch((err) => {
	console.log(err);
})


// Middlewares
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({
	extended: false
}));
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({extended: true}));
app.use(cookieParser());


// API 
app.use('/api/members', membersApiRouter);
app.use('/api/groups', groupsApiRouter);
// app.use('/services/sentry', sentryRouter);
app.use('/services/postman', postmanRouter);
// exports for www script to start listening
module.exports = app;
