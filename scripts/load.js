db.dropDatabase();

//db.User.createIndex( { "username" : 1 }, { unique : true } );

db.User.insert({
	username : 'mitep',
	password : '123',
	name : 'Milos',
	surname : 'Tepic',
});

db.User.insert({
	username : 'masato',
	password : '123',
	name : 'Julija',
	surname : 'Mirkovic',
});

db.User.insert({
	username : 'nikola',
	password : '123',
	name : 'Nikola',
	surname : 'Stojanovic',
});

db.Message.insert({
	type : 0,
	sender : 'nikola',
	receiver : 'mitep',
	timestamp : 1525898183,
	content : 'Tepicu macane nisi ti za bacanje!!'
});

db.Message.insert({
	type : 0,
	sender : 'mitep',
	receiver : 'nikola',
	timestamp : 1525898303,
	content : 'Dobra fora'
});

db.Group.insert({
	name : "Agenti, xml, sbnz",
	members : ['nikola', 'masato', 'mitep']
});

db.Friendship.insert({
	userId : 'masato',
	userId2 : 'mitep',
	status : 'F'
});

db.Friendship.insert({
	userId : 'nikola',
	userId2 : 'masato',
	status : 'P'
});