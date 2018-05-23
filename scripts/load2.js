db.dropDatabase();

db.User.createIndex( { "username" : 1 }, { unique : true } );

db.User.insert({
	username : 'mitep',
	password : '123',
	name : 'Milos',
	surname : 'Tepic',
	friends : ['masato', 'nikola', 'nemiljenik'],
	receivedRequests : ['egipatski', 'siva'],
	sentRequests : [],
});

db.User.insert({
	username : 'masato',
	password : '123',
	name : 'Julija',
	surname : 'Mirkovic',
	friends : ['mitep', 'nikola'],
	receivedRequests : ['siva'],
	sentRequests : [],
});

db.User.insert({
	username : 'nikola',
	password : '123',
	name : 'Nikola',
	surname : 'Stojanovic',
	friends : ['mitep', 'masato'],
	receivedRequests : [],
	sentRequests : [],
});

db.User.insert({
	username : 'nemiljenik',
	password : '123',
	name : 'Nemiljenik',
	surname : 'Tepic',
	friends : ['mitep'],
	receivedRequests : [],
	sentRequests : [],
});

db.User.insert({
	username : 'siva',
	password : '123',
	name : 'Siva',
	surname : 'Snezic',
	friends : ['egipatski'],
	receivedRequests : [],
	sentRequests : ['mitep', 'masato'],
});

db.User.insert({
	username : 'egipatski',
	password : '123',
	name : 'Egipatski',
	surname : 'Tepic',
	friends : ['siva'],
	receivedRequests : [],
	sentRequests : ['mitep'],
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
