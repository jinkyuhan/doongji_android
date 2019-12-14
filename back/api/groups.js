var express = require('express');
var router = express.Router();
var models = require('../models/models');
var request = require('request');
const Sequelize = require('sequelize');

const Op = Sequelize.Op;

/*READ*/
/* GET GROUPS */
router.get('/', async function(req, res, next) {
	// BY NONE (ALL)
	try {
		var groups = await models.Group.findAll();
		res.json(groups);
		console.log(`GET ALL GROUPS RESPONSE : ${groups}`);
	} catch (err) {
		console.log(`GET ALL GROUPS ERROR : ${err}`);
	}
});

/*GET A GROUP BY GRP ID*/
router.get('/:grp_id', async function(req, res, next) {
	try {
		var theMember = await models.Group.findAll({
			where: { grp_id: String(req.params.grp_id) }
		});
	} catch (err) {
		console.log(`GET A GROUP BY GROUP_ID ERROR : ${err}`);
	}
	res.json(theMember);
});

/*GET A MEMBER BY GRP_ID*/
router.get('/:grp_id/members', async function(req, res, next) {
	// BY GRP ID
	try {
		var query = `
				SELECT user_id, user_name 
				FROM Belongs_View 
				WHERE grp_id = ${req.params.grp_id}
		`;
		var groupMembers = await models.sequelize.query(query, {
			type: Sequelize.QueryTypes.SELECT,
			raw: true
		});
		res.json(groupMembers);
		console.log(`GET MEMBERS (by grp_id) RESPONSE : ${groupMembers}`);
	} catch (err) {
		console.log(`GET MEMBERS (by grp_id) ERROR!!! : ${err}`);
	}
});

/*CREATE*/
/* POST NEW GROUP */
router.post('/:grp_name/:grp_xpos/:grp_ypos/:grp_creator', async function(req, res, next) {
	try {
		var newGroup = await models.Group.create({
			grp_name: req.params.grp_name,
			grp_xpos: req.params.grp_xpos,
			grp_ypos: req.params.grp_ypos,
			grp_creator: req.params.grp_creator
		});
		await models.Belongs_to.create({
			user_id: req.params.grp_creator,
			grp_id: newGroup.grp_id
		});
		console.log(`POST INSERT ROW TO GROUPS TABLE SUCCESS: ${newGroup}`);
		res.json({
			success: true,
			newGroup: newGroup
		});
		
	} catch (err) {
		console.log(`DB INSERT ROW TO GROUPS TABLE ERROR!: ${err}`);
	}
});
/*POST NEW MEMBER */
router.post('/:grp_id/:user_id', async function(req, res, next){
	try{
		var newBelong = await models.Belongs_to.create({
			user_id: req.params.user_id,
			grp_id: req.params.grp_id
		});
		console.log(`POST INSERT ROW TO BELONGSTO TABLE SUCCESS: ${newBelong}`);
		res.json({
			success: true,
			newGroup: newBelong
		});
	} catch(err){
		res.json({
			success: false,
			newGroup:{}
		})
		console.log(`POST INSERT ROW TO BELONGSTO TABLE CONSTRAINT WARNINGS!`);
	}
})
/*UPDATE*/
/* PUT GROUP BY ID */
router.put('/:grp_id/:grp_name/:grp_xpos/:grp_ypos/:grp_radius', async function(req, res, next) {
	console.log(req.params);
	try {
		var updatedGroup = await models.Group.update(
			{
				grp_name: req.params.grp_name,
				grp_xpos: parseFloat(req.params.grp_xpos),
				grp_ypos: parseFloat(req.params.grp_ypos),
				grp_radius: parseInt(req.params.grp_radius)
			},
			{
				where: { grp_id: parseInt(req.params.grp_id) },
				returning: true,
				plain: true
			});
		console.log(`PUT UPDATE ROW IN GROUPS TABLE RESPONSE: ${updatedGroup}`);
		if(updatedGroup[1] == 0){
			res.json({success: false});
		}else{
			res.json({success: true});
		}
	} catch (err) {
		console.log(`PUT UPDATE ROW IN GROUPS TABLE ERROR!: ${err}`);
	}
});

module.exports = router;