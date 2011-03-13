var songs = [];

//remove the song from the top of the playlist
function pull()
{
    return songs.shift();
}

function addToPlaylist(song)
{
    songs.push(song);
}

exports.pull = pull;
exports.addToPlaylist = addToPlaylist;
exports.songs = songs;