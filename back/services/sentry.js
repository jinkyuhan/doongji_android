var express = require('express');
var router = express.Router();
var models = require('../models/models');
var mailbox = require('./mailbox');
var fcm = require('./fcm');
const Sequelize = require('sequelize');
const Op = Sequelize.Op;


router.post('', async function (req, res, next) {

});

router.post('/:token/come/:grp_id/public', async function (req, res, next) {
	try {
		//notification
		var invader = await models.Member.findOne({
			where: {
				token: req.params.token
			}
		});
		var query = `SELECT * FROM Belongs_View WHERE grp_id=${req.params.grp_id}`;
		var otherMembers = await models.sequelize.query(query, {
			type: Sequelize.QueryTypes.SELECT,
			raw: true
		});
		console.log("OtherMembers IS :::"+otherMembers);
		var tokens_of_otherMembers = [];
		for (var member in otherMembers) {
			if (otherMembers[member].token != req.params.token)
				tokens_of_otherMembers.push(otherMembers[member].token);
		}


		if (tokens_of_otherMembers.length != 0) {
			fcm.sendMessageToManyTokens(invader.user_name, `현재 ${otherMembers[0].grp_name}에 근접하였습니다.`, tokens_of_otherMembers);
		}


		//message
		var unreadMessages = await mailbox.getUnreadMessages(req.params.grp_id, req.params.token);
		for (var msg in unreadMessages) {
			fcm.sendMessageToOneToken(unreadMessages[msg].msg_sender, unreadMessages[msg].msg_body, unreadMessages[msg].msg_receiver);
		}
	} catch (err) {
		console.log(`! ERROR getUnreadMessage fail : ${err}`);
	}
	res.json({
		visit_notification: true,
		messages: unreadMessages
	});
});

router.post('/:token/come/:grp_id/private', async function (req, res, next) {
	var unreadMessages = mailbox.getUnreadMessages(req.params.token);
	res.json({
		visit_notification: false, // push the notification to others of the group
		messages: unreadMessages
	})
});

module.exports = router;