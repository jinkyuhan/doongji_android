var express = require('express');
var router = express.Router();
var models = require('../models/models');
const Sequelize = require('sequelize');

const Op = Sequelize.Op;

/*READ*/
/* GET MEMBERS */
router.get('/', async function (req, res, next) {
	try {
		var members = await models.Member.findAll();
		res.json(members);
		console.log(`GET ALL MEMBERS RESPONSE: ${members}`);
	} catch (err) {
		console.log(`GET ALL MEMBERS ERROR!!!: ${err} `);
	}
});

/*GET A MEMBER BY token*/
router.get('/isDup', async function (req, res, next) {
	// BY MEM_ID
	try {
		var theMember = await models.Member.findOne({
			where: {
				token: String(req.query.token)
			}
		});
		res.send(theMember);
		console.log(`GET A MEMBER BY TOKEN RESPONSE: ${theMember}`);
	} catch (err) {
		console.log(`GET THE MEMBER (BY TOKEN) ERROR!!! : ${theMember}`);
	}
});

// /*GET A MEMBER BY token & USER_PW*/
// router.get('/:token/:user_pw', async function (req, res, next) {
// 	// BY ID & PW
// 	try {
// 		var members = await models.Member.findAll({
// 			where: {
// 				token: {
// 					[Op.eq]: String(req.params.token)
// 				},
// 				user_pw: {
// 					[Op.eq]: String(req.params.user_pw)
// 				}
// 			}
// 		});
// 		res.json(members);
// 		console.log(`GET MEMBERS (by token & user_pw) RESPONSE : ${members}`);
// 	} catch (err) {
// 		console.log(`GET MEMBERS (by token & user_pw) ERROR!!! : ${err}`);
// 	}
// });
/*GET A GROUP BY token*/
router.get('/:token/belongs/groups', async function (req, res, next) {
	try {
		var query = `
				SELECT grp_id, grp_name, grp_xpos, grp_ypos, grp_radius 
				FROM Belongs_View 
				WHERE token = '${req.params.token}'
		`;
		var ownGroups = await models.sequelize.query(query, {
			type: Sequelize.QueryTypes.SELECT,
			raw: true
		});
		console.log(`GET A GROUP BY token RESPONSE: ${ownGroups}`);
		res.json(ownGroups);
	} catch (err) {
		console.log(`GET A GROUP BY token ERROR: ${err}`);
	}
});


/*CREATE*/
/* POST NEW MEMBER */
router.post('/add', async function (req, res, next) {
	try {
		var newMember = await models.Member.create({
			token: req.body.token,
			user_name: req.body.user_name
		});
		console.log(req.body.token);
		console.log(req.body.user_id);
		res.json({
			success: true,
			newMember: newMember
		});
	} catch (err){
		console.log(`POST NEW USER ERROR: ${err}`);
	}
});


module.exports = router;