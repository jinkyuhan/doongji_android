var express = require('express');
var models = require('../models/models');
const Sequelize = require('sequelize');
const Op = Sequelize.Op;

// DOWNLOAD messages from DB
module.exports.getUnreadMessages = async function (group_id, token) {
    try {
        var query = `
            SELECT *
            FROM doongji.message_boxes
            WHERE msg_sender IN (
                    SELECT token
                    FROM  doongji.belongs_tos
                    WHERE grp_id=${group_id})
                AND 
                msg_receiver='${token}';
            `
        var unreadMessages = await models.sequelize.query(query, {
            type: Sequelize.QueryTypes.SELECT,
            raw: true
        });
        deleteReadMessage(group_id, token);
        return unreadMessages;
    } catch (err) {
        console.log(`! ERROR get message from DB : ${err} `);
    }
};

var deleteReadMessage = async function (group_id, token) {
    try {
        var query = `
        DELETE FROM
            doongji.message_boxes
        WHERE
            msg_sender IN (
                SELECT token
                FROM doongji.belongs_tos
                WHERE grp_id=${group_id})
            AND
            msg_receiver = '${token}';
        `
        var deletedCount = await models.sequelize.query(query, {
            type: Sequelize.QueryTypes.DELETE,
            raw: true
        });
        return deletedCount;
    } catch (err) {
        console.log(`! ERROR delete read message : ${err}`);
    }
}
