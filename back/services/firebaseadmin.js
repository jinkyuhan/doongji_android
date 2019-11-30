
function Notification(){

	//constructor
	this.fcmAdmin = require('firebase-admin');
	this.serviceAccount = require('./doongji-34bfb-firebase-adminsdk-nq10e-a914fe32a6.json');
	this.admin.initializeApp({
		credential: admin.credential.cert(serviceAccount);
	});
	this.fcmTargetToken = "안드로이드 타겟 토큰"
	
	//method: setContext
	this.setContext = function(_title, _message){
		this.fcmMessage = {
			notification: {
				title: _title,
				body: _message
			},
			data:{},
			token: this.fcmTargetToken
		}
	}
	
	//method: send
	this.send = async function(){
		try{
			var response = async this.fcmAdmin.messaging().send(this.fcmMessage);
			console.log('success sending' + response);
		} catch(err){
			console.log(`Message sending error ${err}`);
		}
	}
}

moudle.exports = Notification;


