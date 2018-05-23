db.dropDatabase();

//db.User.createIndex( { "username" : 1 }, { unique : true } );

db.User.insert({
	_id : ObjectId("5b055d2718066269ec047a21"),
	username : 'mitep',
	password : '123',
	name : 'Milos',
	surname : 'Tepic',
	groups : ['5b055d2718066269ec047a26']
});

db.User.insert({
	_id : ObjectId("5b055d2718066269ec047a22"),
	username : 'masato',
	password : '123',
	name : 'Julija',
	surname : 'Mirkovic',
	groups : ['5b055d2718066269ec047a26']
});

db.User.insert({
	_id : ObjectId("5b04260ca9d8dd22a4ca3944"),
	username : 'nikola',
	password : '123',
	name : 'Nikola',
	surname : 'Stojanovic',
	groups : ['5b055d2718066269ec047a26']
});

db.Message.insert({
	_id : ObjectId("5b055d2718066269ec047a23"),
	type : 0,
	sender : 'nikola',
	receiver : 'mitep',
	timestamp : 1525898183,
	content : 'Tepicu macane nisi ti za bacanje!!'
});

db.Message.insert({
	_id : ObjectId("5b055d2718066269ec047a24"),
	type : 0,
	sender : 'mitep',
	receiver : 'nikola',
	timestamp : 1525898303,
	content : 'Dobra fora'
});

db.Message.insert({
	_id : ObjectId("5b055d2718066269ec047a25"),
	type : 1,
	sender : 'nikola',
	receiver : '5b055d2718066269ec047a26',
	timestamp : 1525898183,
	content : 'Kako je?'
});

db.Group.insert({
	_id : ObjectId("5b055d2718066269ec047a26"),
	name : "Agenti, xml, sbnz",
	members : ['nikola', 'masato', 'mitep'],
	messages : ['5b055d2718066269ec047a25']
});