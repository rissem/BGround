var Db = require('mongodb').Db,
Connection = require('mongodb').Connection,
Server = require('mongodb').Server,
BSON = require('mongodb').BSONNative;

var host = process.env['MONGO_NODE_DRIVER_HOST'] != null ? process.env['MONGO_NODE_DRIVER_HOST'] : 'localhost';
var port = process.env['MONGO_NODE_DRIVER_PORT'] != null ? process.env['MONGO_NODE_DRIVER_PORT'] : Connection.DEFAULT_PORT;

function exec (dbName, callback) {
    var db = new Db(dbName, new Server(host, port, {}), {native_parser:true}); 
    try {
	db.open(bl.onSuccess(function(db) {
	    function cleanup() {db.close()}

	    try {
		callback(db, cleanup);
	    }
	    catch (e) {
		console.log(e);
		db.close();
	    }
	}));
    }
    catch (e) {
	console.log(e);
    }
}

exports.exec = exec;