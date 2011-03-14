var repl = require("repl");
sys = require("sys");
songDb = require("songDb");
userDb = require("userDb");
playlist = require("playlist");
var http = require('http');

songDb.ensureIndices();

http.createServer(function (req, res) {
    url = require('url').parse(req.url, true);

    handlers = {
	"/allSongs" : function() {
	    res.writeHead(200, {'Content-Type': 'text/plain'});
	    songDb.getAll(function(songs){
		res.end(JSON.stringify(songs));
	    });
	},

	"/addSong" : function() {
	    var song = {title:url.query.title, artist:url.query.artist, album:url.query.album, filename: url.query.filename};
	    songDb.add(song, function() {
		res.end("songAdded");
	    });
	},

	"/search" : function() {
	    songDb.search(url.query.search, function(songs) {
		res.end(JSON.stringify(songs));
	    });
	},

	"/add" : function() {
	    songDb.findById(url.query.id, function(song) {
		if (song != null && song != undefined){
		    playlist.addToPlaylist(song);
		    res.end(JSON.stringify({success:true}));
		}
		else {
		    res.end(JSON.stringify({success:false}));
		}
	    });
	},

	"/playlist" : function() {
	    res.end(JSON.stringify(playlist.songs));
	},

	"/pullFromPlaylist" : function() {
	    res.end(JSON.stringify(playlist.pull()));
	}
    };

    
    var cookie = req.headers["cookie"];
    var uid = undefined;
    if (cookie != null) {
	uid = cookie.split("=")[1];
    }
    var user = userDb.getUser(uid, function(user) {
	console.log("user = " + sys.inspect(user));
	//TODO only set cookie when a new users is created
	res.setHeader("Set-Cookie", "uid=" + user._id + "; Expires=Wed, 13-Jan-2021 22:23:01 GMT;HttpOnly");	
	handler = handlers[url.pathname];
	if (handler) {
	    handler()
	}
	else {
	    console.log("attempt to access " + url.pathname);
	    res.writeHead(404, {'Content-Type': 'text/html'});
	    res.end("<h1>404</h1>");	
	}
    });
}).listen(8124, "127.0.0.1");

ctx = repl.start().context;

console.log(songDb.test);
songDb.test = 234;
console.log('Server running at http://127.0.0.1:8124/');
console.log(songDb.test);