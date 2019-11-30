var express = require('express');
var router = express.Router();
var models = require('../models/models');
const Sequelize = require('sequelize');

const Op = Sequelize.Op;

/*CREATE*/

/* POST NEW MEMBER */
router.post('/', async function(req, res, next) {
	
});

/*READ*/
/* GET MEMBERS */
router.get('/', async function(req, res, next) {
	if (req.query.user_id !== undefined && req.query.user_pw !== undefined) {
		// BY ID & PW
		try {
			var members = await models.Member.findAll({
				where: {
					user_id: { [Op.eq]: String(req.query.user_id) },
					user_pw: { [Op.eq]: String(req.query.user_pw) }
				}
			});
			res.json(members);
			console.log(`response : ${members}`);
		} catch (err) {
			console.log(`GET MEMBERS (by user_id & user_pw) ERROR!!! : ${err}`);
		}
	} else {
		// else if (req.query.user_name !== undefined) {
		// 	// BY NAME
		// 	try {
		// 		var theMember = await models.Members.findAll({
		// 			where: { user_name: String(req.query.user_name) }
		// 		});
		// 		res.json(theMember);
		// 	} catch (err) {
		// 		console.log(`GET MEMBERS (by name) ERROR!!! : ${err} `);
		// 	}
		// }
		// BY NONE (ALL)
		try {
			var members = await models.Member.findAll();
			res.json(members);
			console.log(`response : ${members}`);
		} catch (err) {
			console.log(`GET ALL MEMBERS ERROR!!! : ${err} `);
		}
	}
});

/*GET A MEMBER BY ID*/
router.get('/:id', async function(req, res, next) {
	// BY ID
	try {
		var theMember = await models.Member.findOne({
			where: { mem_id: String(req.params.id) }
		});
		console.log(theMember);
		res.json(theMember);
	} catch (err) {
		console.log(`GET THE MEMBER (BY ID) ERROR!!! : ${res}`);
	}
});

module.exports = router;