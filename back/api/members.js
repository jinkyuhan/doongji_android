var express = require('express');
var router = express.Router();
var models = require('../models/models');
const Sequelize = require('sequelize');

const Op = Sequelize.Op;
/*CREATE*/
/* POST NEW MEMBER */
router.put('/', async function (req, res, next) {


});

/* GET MEMBERS */
router.get('/', async function (req, res, next) {
	console.log(Op);
	if (req.query.user_id !== undefined && req.query.user_pw !== undefined) {
		 models.Member.findAll({
			where: {
				user_id: {[Op.eq] : String(req.query.user_id)}, 
				user_pw: {[Op.eq] : String(req.query.user_pw)}
			}
		}).then((members)=>{
			res.json(members);
			console.log(`!!!!!!!response : ${members}`);
		});
	} else if(req.query.user_name !== undefined){

	} else {
		models.Member.findAll()
		.then((members)=>{
			res.json(members);
			console.log(`!!!!!!response : ${members}`);
		});
	}
});

/*GET A MEMBER BY ID*/
router.get('/:id', async function (req, res, next) {
	var theMember = await models.Member.findOne({
		where: { mem_id: String(req.params.id) }
	});
	res.json(theMember);
});

module.exports = router;