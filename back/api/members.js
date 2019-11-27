var express = require('express');
// var mysql = require('mysql');
var router = express.Router();
var models = require('../models/models');
/* GET home page. */
router.get('/', async function (req, res, next) {
	models.Member.findAll().then((members) => {
		console.log(members);
	});
});

module.exports = router;