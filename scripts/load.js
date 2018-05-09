db.dropDatabase();

db.User.createIndex( { "username" : 1 }, { unique : true } );

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
	sender : ObjectId("5af33e3fc424068e1559ee12"),
	receiver : ObjectId("5af33e3ec424068e1559ee10"),
	timestamp : 1525898183,
	content : 'Tepicu macane nisi ti za bacanje!!'
});

db.Message.insert({
	type : 0,
	sender : ObjectId("5af33e3ec424068e1559ee10"),
	receiver : ObjectId("5af33e3fc424068e1559ee12"),
	timestamp : 1525898303,
	content : 'Dobra fora'
});

db.Group.insert({
	name : "Agenti, xml, sbnz",
	members : [ObjectId("5af33e3ec424068e1559ee10"), ObjectId("5af33e3fc424068e1559ee11"), ObjectId("5af33e3fc424068e1559ee11")]
});

db.Friendship.insert({
	userId : ObjectId("5af33e3ec424068e1559ee10"),
	userId2 : ObjectId("5af33e3fc424068e1559ee11"),
	status : 'F'
});

db.Friendship.insert({
	userId : ObjectId("5af33e3ec424068e1559ee10"),
	userId2 : ObjectId("5af33e3fc424068e1559ee12"),
	status : 'P'
});