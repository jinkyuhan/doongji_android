var admin = require('firebase-admin');
var serviceAccount = require('./doongji-e3b61-firebase-adminsdk-4jwxq-0c38dd4ab8');	// 서버 비밀 키

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount)
});

admin.sendMessageToTopic = async function (_title, _message, _topic) {
	var message = {
		notification: {
			title: _title,
			body: _message
		},
		topic: _topic
	};

	try {
		result = admin.messaging().send(message);
		result.then(response =>
			console.log(`Successfully sent message: ${response}`));

	} catch (err) {
		console.log(`Erorr sending message: ${err}`);
	}
}

module.exports = admin;
