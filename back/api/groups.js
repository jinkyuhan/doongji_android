var express = require('express');
var router = express.Router();
var models = require('../models/models');
const Sequelize = require('sequelize');

const Op = Sequelize.Op;
/*CREATE*/
/* POST NEW MEMBER */
router.put('/', async function(req, res, next) {});

/* GET GROUPS */
router.get('/', async function(req, res, next) {
	// BY NONE (ALL)
	try {
		var groups = await models.Group.findAll();
		res.json(groups);
		console.log(`response : ${groups}`);
	} catch (err) {
		console.log(`GET GROUP ERROR : ${err}`);
	}
});

/*GET A MEMBER BY GROUP ID*/
router.get('/:id', async function(req, res, next) {
	try {
		var theMember = await models.Group.findOne({
			where: { grp_id: String(req.params.id) }
		});
	} catch (err) {
		console.log(`GET A MEMBER BY GROUP ID ERROR : ${err}`);
	}
	res.json(theMember);
});

module.exports = router;