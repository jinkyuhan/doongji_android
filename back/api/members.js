var express = require('express');
var router = express.Router();
var models = require('../models/models');
const Sequelize = require('sequelize');

const Op = Sequelize.Op;

/*CREATE*/

/* POST NEW MEMBER */
router.post('/:user_id/:user_pw/:user_name', async function (req, res, next) {
	try {
		var dupMemberCount = await models.Member.count({
			where: {
				user_id: String(req.params.user_id)
			}
		});
		if (dupMemberCount === 0) {
			try {
				var newMember = await models.Member.create({
					user_id: req.params.user_id,
					user_pw: req.params.user_pw,
					user_name: req.params.user_name
				});
				console.log('response: ');
				console.log(newMember);
				res.json({
					success: true,
					newMember: newMember
				})
			} catch (err) {
				console.log(`DB INSERT ROW TO MEMBERS TABLE ERROR!: ${err}`)
			}
		} else {
			console.log('response: ');
			console.log(newMember);
			res.json({
				success: false,
				newMember: {}
			})
		}
	} catch (err) {
		console.log(`POST NEW MEMBER ERROR!!!: ${err}`);
	}
});

/*READ*/
/* GET MEMBERS */
router.get('/', async function (req, res, next) {
	try {
		var members = await models.Member.findAll();
		res.json(members);
		console.log(`response : ${members}`);
	} catch (err) {
		console.log(`GET ALL MEMBERS ERROR!!! : ${err} `);
	}
});

/*GET A MEMBER BY ID*/
router.get('/:id', async function (req, res, next) {
	// BY ID
	try {
		var theMember = await models.Member.findOne({
			where: {
				mem_id: String(req.params.id)
			}
		});
		console.log(theMember);
		res.json(theMember);
	} catch (err) {
		console.log(`GET THE MEMBER (BY ID) ERROR!!! : ${res}`);
	}
});

/*GET A MEMBER BY USER_ID & USER_PW*/
router.get('/:user_id/:user_pw', async function (req, res, next) {
	// BY ID & PW
	try {
		var members = await models.Member.findAll({
			where: {
				user_id: {
					[Op.eq]: String(req.params.user_id)
				},
				user_pw: {
					[Op.eq]: String(req.params.user_pw)
				}
			}
		});
		res.json(members);
		console.log(`response : ${members}`);
	} catch (err) {
		console.log(`GET MEMBERS (by user_id & user_pw) ERROR!!! : ${err}`);
	}
});

module.exports = router;