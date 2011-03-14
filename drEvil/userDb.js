sys = require("sys");
var Db = require('mongodb').Db,
Connection = require('mongodb').Connection,
Server = require('mongodb').Server,
BSON = require('mongodb').BSONNative;
bl = require('bl'); //custom beatlist functions
mydb = require('mydb'); //custom mongodb functions

var host = process.env['MONGO_NODE_DRIVER_HOST'] != null ? process.env['MONGO_NODE_DRIVER_HOST'] : 'localhost';
var port = process.env['MONGO_NODE_DRIVER_PORT'] != null ? process.env['MONGO_NODE_DRIVER_PORT'] : Connection.DEFAULT_PORT;

var DB_NAME = "beatlist";

var STARTING_CREDITS = 3;

//return the user for a given id, create one if none exists
function getUser(id, callback)
{
    mydb.exec(DB_NAME, function(db){
	db.collection("user", bl.onSuccess (function(collection) {
	    
	    //function for creating a new user
	    function createUser(id, callback) {
		console.log("creating new user. id = " + id);

		user = { credits:STARTING_CREDITS};
		//if id is specfied then use it
		if (id) {
		    user._id = new BSON.ObjectID(id);
		}
		collection.insert(user, bl.onSuccess(function(results) { 
		    callback(results[0]);
		    db.close();
		}));
	    }

	    //if no id is passed to create a new user or id length is invalid then create a new user
	    if (! id || id.length != 24) {
		createUser(undefined, callback);
	    }
	    //otherwise query on id
	    else {
		//make sure id is valid length
		collection.findOne({ _id:new BSON.ObjectID(id)}, bl.onSuccess(function(user) {
		    if (user) {
			//console.log("returning user " + sys.inspect(user));
			callback(user);
			db.close();
		    }
		    //create a new account with id if it doesn't exist
		    else {
			createUser(undefined, callback);
		    }
		}));
	    }
	}));
    });
}
exports.getUser = getUser;