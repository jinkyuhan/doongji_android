var express = require('express');
var router = express.Router();
var models = require('../models/models');
var admn = require('./fcmAdmin');
const Sequelize = require('sequelize');
const Op = Sequelize.Op;



router.post('/:user_id/accessto/:grp_id', async function (req, res, next) {
	console.log("hihi");
	//message
	// var yourMessage = await models.message
	admn.sendMessageToTopic('제목입니다', '내용입니다.', 'test_group2');
	// notification
	// var others = await models.Belongs_to.findAll({
	// 	where : {
	// 		grp_id : req.params.grp_id
	// 	}
	// });	
});

module.exports = router;