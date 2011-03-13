var songs = [];

//remove the song from the top of the playlist
function pullFromPlaylist()
{
    return songs.shift();
}

function addToPlaylist(song)
{
    songs.push(song);
}

exports.pullFromPlaylist = pullFromPlaylist;
exports.addToPlaylist = addToPlaylist;
exports.songs = songs;