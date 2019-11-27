const Sequelize = require('sequelize');
const config = require('./dbconfig');
const sequelize = new Sequelize(
    'DOONG_JI',
    'admin',
    '12345678', {
    host: 'mysqldb.cv9byhmyfmai.us-east-1.rds.amazonaws.com',//config.host,
    dialect: 'mysql'
});

const Member = sequelize.define('member', {
    mem_id: {
        type: Sequelize.INTEGER,
        primaryKey: true
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
});

const Location = sequelize.define('location', {
    loc_id: {
        type: Sequelize.INTEGER,
        primaryKey: true
    },
    x_pos: {
        type: Sequelize.STRING,
        allowNull: false,
        unique: true
    },
    y_pos: {
        type: Sequelize.STRING,
        allowNull: false,
        unique: true
    }
});

const Group = sequelize.define('group', {
    grp_id: {
        type: Sequelize.INTEGER,
        primaryKey: true
    },
    grp_name: {
        type: Sequelize.STRING,
        allowNull: false
    },
    grp_loc: {
        type: Sequelize.INTEGER,
        allowNull: false,
        references : {
            model : 'locations',
            key : 'loc_id'
        }
    },
    grp_radius: {
        type: Sequelize.INTEGER,
        defaultValue: 500
    }
});

const Belongs_to = sequelize.define('belongs_to', {
    mem_id : {
        type: Sequelize.INTEGER,
        references : {
            model : 'members',
            key : 'mem_id'
        },
        primaryKey: true
    },
    grp_id : {
        type: Sequelize.INTEGER,
        references : {
            model : 'groups',
            key : 'grp_id'
        },
        primaryKey: true
    },
});

const Message_box = sequelize.define('message_box',{
    msg_id : {
        type: Sequelize.INTEGER,
        primaryKey: true
    },
    msg_body : {
        type: Sequelize.STRING
    },
    msg_sender: {
        type: Sequelize.INTEGER,
        allowNull: false,
        references : {
            model : 'members',
            key : 'mem_id'
        }
    },
    msg_receiver: {
        type: Sequelize.INTEGER,
        allowNull: false,
        references : {
            model : 'members',
            key : 'mem_id'
        }
    }
});

module.exports = {
    sequelize: sequelize,
    Member : Member,
    Location : Location,
    Group : Group,
    Belongs_to : Belongs_to,
    Message_box : Message_box
}