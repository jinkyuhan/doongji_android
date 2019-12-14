var express = require('express');
var router = express.Router();
var models = require('../models/models');
const Sequelize = require('sequelize');
const Op = Sequelize.Op;


router.post('/:user_id/accessto/:grp_id',async function (req, res, next){
	//message
	var yourMessage = await models.message
	// notification
	var others = await models.Belongs_to.findAll({
		where : {
			grp_id : req.params.grp_id
		}
	});
	console.log('hihihi');
});

module.exports = router;