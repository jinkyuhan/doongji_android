const Sequelize = require('sequelize');
const config = require('./dbconfig');
const sequelize = new Sequelize('DOONG_JI', 'admin', '12345678', {
	host: 'mysqldb.cv9byhmyfmai.us-east-1.rds.amazonaws.com', //config.host,
	dialect: 'mysql'
});

const Member = sequelize.define(
	'member',
	{
		mem_id: {
			type: Sequelize.INTEGER,
			primaryKey: true,
			autoIncrement: true
		},
		user_id: {
			type: Sequelize.STRING,
			unique: true,
			allowNull: false
		},
		user_pw: {
			type: Sequelize.STRING,
			allowNull: false
		},
		user_name: {
			type: Sequelize.STRING,
			allowNull: false
		}
	},
	{
		charset: 'utf8',
		collate: 'utf8_unicode_ci'
	}
);

const Group = sequelize.define(
	'group',
	{
		grp_id: {
			type: Sequelize.INTEGER,
			primaryKey: true,
			autoIncrement: true
		},
		grp_name: {
			type: Sequelize.STRING,
			allowNull: false
		},
		grp_xpos: {
			type: Sequelize.FLOAT,
			allowNull: false
		},
		grp_ypos: {
			type: Sequelize.FLOAT,
			allowNull: false
		},
		grp_radius: {
			type: Sequelize.INTEGER,
			defaultValue: 500
		},
		grp_creator: {
			type: Sequelize.STRING,
			allowNull: true,
			references: {
				model: 'members',
				key: 'user_id',
				onDelete: 'SET NULL'
			}
		}
	},
	{
		charset: 'utf8',
		collate: 'utf8_unicode_ci'
	}
);

const Belongs_to = sequelize.define(
	'belongs_to',
	{
		user_id: {
			type: Sequelize.STRING,
			references: {
				model: 'members',
				key: 'user_id',
				onDelete: 'SET NULL'
			},
			primaryKey: true
		},
		grp_id: {
			type: Sequelize.INTEGER,
			references: {
				model: 'groups',
				key: 'grp_id',
				onDelete: 'SET NULL'
			},
			primaryKey: true
		}
	},
	{
		charset: 'utf8',
		collate: 'utf8_unicode_ci'
	}
);

const Message_box = sequelize.define(
	'message_box',
	{
		msg_id: {
			type: Sequelize.INTEGER,
			primaryKey: true,
			autoIncrement: true
		},
		msg_body: {
			type: Sequelize.STRING
		},
		msg_sender: {
			type: Sequelize.STRING,
			allowNull: false,
			references: {
				model: 'members',
				key: 'user_id'
			}
		},
		msg_receiver: {
			type: Sequelize.STRING,
			allowNull: false,
			references: {
				model: 'members',
				key: 'user_id'
			}
		}
	},
	{
		charset: 'utf8',
		collate: 'utf8_unicode_ci'
	}
);

module.exports = {
	sequelize: sequelize,
	Member: Member,
	Group: Group,
	Belongs_to: Belongs_to,
	Message_box: Message_box
};