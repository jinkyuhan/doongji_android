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
		var query = `SELECT * FROM belongs_views WHERE grp_id=${req.params.grp_id}`;
		var otherMembers = await models.sequelize.query(query,{
			type: Sequelize.QueryTypes.SELECT,
			raw: true
		});

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