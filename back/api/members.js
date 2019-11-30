var express = require('express');
var router = express.Router();
var models = require('../models/models');
const Sequelize = require('sequelize');

const Op = Sequelize.Op;

/*READ*/
/* GET MEMBERS */
router.get('/', async function(req, res, next) {
	try {
		var members = await models.Member.findAll();
		res.json(members);
		console.log(`GET ALL MEMBERS RESPONSE: ${members}`);
	} catch (err) {
		console.log(`GET ALL MEMBERS ERROR!!!: ${err} `);
	}
});

/*GET A MEMBER BY USER_ID*/
router.get('/:user_id', async function(req, res, next) {
	// BY MEM_ID
	try {
		var theMember = await models.Member.findOne({
			where: {
				user_id: String(req.params.user_id)
			}
		});
		
		console.log(`GET A MEMBER BY USER_ID RESPONSE: ${theMember}`);
		res.json(theMember);
	} catch (err) {
		console.log(`GET THE MEMBER (BY ID) ERROR!!! : ${res}`);
	}
});

/*GET A MEMBER BY USER_ID & USER_PW*/
router.get('/:user_id/:user_pw', async function(req, res, next) {
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
		console.log(`GET MEMBERS (by user_id & user_pw) RESPONSE : ${members}`);
	} catch (err) {
		console.log(`GET MEMBERS (by user_id & user_pw) ERROR!!! : ${err}`);
	}
});
/*GET A GROUP BY USER_ID*/
router.get('/:user_id/belongs/groups', async function(req, res, next) {
	try {
		var query = `
				SELECT grp_id, grp_name, grp_xpos, grp_ypos, grp_radius 
				FROM Belongs_View 
				WHERE user_id = '${req.params.user_id}'
		`;
		var ownGroups = await models.sequelize.query(query, {
			type: Sequelize.QueryTypes.SELECT,
			raw: true
		});
		console.log(`GET A GROUP BY USER_ID RESPONSE: ${ownGroups}`);
		res.json(ownGroups);
	} catch (err) {
		console.log(`GET A GROUP BY USER_ID ERROR: ${err}`);
	}
});


/*CREATE*/
/* POST NEW MEMBER */
router.post('/:user_id/:user_pw/:user_name', async function(req, res, next) {
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
				res.json({
					success: true,
					newMember: newMember
				});				
				console.log(`POST INSERT ROW TO MEMBER TABLE RESPONSE: ${newMember}`);
			} catch (err) {
				console.log(`POST INSERT ROW TO MEMBERS TABLE ERROR!!!: ${err}`);
			}
		} else {
			console.log('POST INSERT ROW TO MEMBER TABLE RESPONSE: EMPTY MEMBER');
			res.json({
				success: false,
				newMember: {}
			});
		}
	} catch (err) {
		console.log(`POST NEW MEMBER ERROR!!!: ${err}`);
	}
});


module.exports = router;