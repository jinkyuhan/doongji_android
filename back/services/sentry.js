var express = require('express');
var router = express.Router();
var models = require('../models/models');
var mailbox = require('./mailbox');
var fcm = require('./fcm');
const Sequelize = require('sequelize');
const Op = Sequelize.Op;


router.post('', async function (req, res, next) {

});

router.post('/:user_id/come/:grp_id/public', async function (req, res, next) {
	try {
		//notification
		//message
		var unreadMessages = await mailbox.getUnreadMessages(req.params.grp_id, req.params.user_id);
		for (var msg in unreadMessages) {
			fcm.sendMessageToTopic(unreadMessages[msg].msg_sender, unreadMessages[msg].msg_body, unreadMessages[msg].msg_receiver);
		}
	} catch (err) {
		console.log(`! ERROR getUnreadMessage fail : ${err}`);
	}
});

router.post('/:user_id/come/:grp_id/private', async function (req, res, next) {
	var unreadMessages = mailbox.getUnreadMessages(req.params.user_id);
	res.json({
		visit_notification: false, // push the notification to others of the group
		messages: unreadMessages
	})
});

module.exports = router;