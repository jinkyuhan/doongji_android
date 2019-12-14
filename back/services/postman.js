var express = require('express');
var router = express.Router();
var models = require('../models/models');
const Sequelize = require('sequelize');
const Op = Sequelize.Op;


/* Make message reservation */
router.post('/:sender_id/messages/:target_id',async function(req, res, next){
	try{
		var newMsg = await models.Message_box.create({
			msg_sender: req.params.sender_id,
			msg_receiver: req.params.target_id,
			msg_body: req.body.message
		});
		console.log(`Add ${req.params.sender_id}'s message reservation success`);
		console.log(`msg : ${newMsg.msg_body}`);
		res.json({
			success: true,
			newMsg: newMsg
		})
	} catch (err) {
		console.log(`! ERROR : cannot make message - ${err}`);
	}
});

module.exports = router;