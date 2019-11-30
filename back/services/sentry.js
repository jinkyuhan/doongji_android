var express = require('express');
var router = express.Router();
var models = require('../models/models');
const Sequelize = require('sequelize');
const Op = Sequelize.Op;


router.post('/:user_id/accessto/:grp_id',async function (req, res, next){
	var others = await models.Belongs_to.findAll({
		where : {
			grp_id : req.params.grp_id
		}
	})
	
});

module.exports = router;