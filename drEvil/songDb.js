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

function ensureIndices() {
    mydb.exec(DB_NAME, function(db){
	db.collection("song",  bl.onSuccess (function(collection) {
	    //TODO worry about case insensitivity
	    //matching parts of a query..lonely should match "show me the meaning of being lonely"
	    collection.createIndex([['artist', 1]], bl.onSuccess());
	    collection.createIndex([['title', 1]], bl.onSuccess());
	    collection.createIndex([['filename', 1]], bl.onSuccess(function(index) {console.log("created index " + index)}));
	    db.close();
	}));
    });
}

function add (song, callback) {
    mydb.exec(DB_NAME, function(db){
	db.collection("song", bl.onSuccess (function(collection) {
	    collection.insert(song);
	    callback(song);
	    db.close();
	}));
    });
}

function getAll (callback) {
    var songs = [];
    mydb.exec(DB_NAME, function(db) {
	db.collection("song", bl.onSuccess (function(collection) {
	    collection.find(bl.onSuccess(function(cursor) {
		cursor.toArray(bl.onSuccess(function(results) {
		    callback(results);
		    db.close();
		}));
	    }));
	}));
    });
}

function search (query, callback) {
    mydb.exec(DB_NAME, function(db) {
	db.collection("song", bl.onSuccess(function(collection) {
	    var regex = new RegExp(query, "i");
	    collection.find({ $or:[{artist:{$regex : regex}}, {title:{$regex : regex}}]}, bl.onSuccess(function(cursor) {
		cursor.toArray(bl.onSuccess(function(results) {
		    callback(results);
		    db.close();
		}));
	    }));	    
	}));
    });
}

function findById(id, callback) {
    mydb.exec(DB_NAME, function(db) {
	db.collection("song", bl.onSuccess(function(collection) {
	    collection.findOne({ _id:new BSON.ObjectID(id)}, bl.onSuccess(function(song) {
		callback(song);
		db.close();
	    }));
	}));
    });
}

exports.add = add;
exports.findById = findById;
exports.getAll = getAll;
exports.search = search;
exports.ensureIndices = ensureIndices;
