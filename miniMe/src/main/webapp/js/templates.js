// This file was automatically generated from miniMe.soy.
// Please don't edit this file by hand.

if (typeof miniMe == 'undefined') { var miniMe = {}; }


miniMe.playlist = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append('\t  <strong>', soy.$$escapeHtml(opt_data.playlist.nowPlaying.title), ' - ', soy.$$escapeHtml(opt_data.playlist.nowPlaying.title), ' </strong><br />');
  var songList8 = opt_data.playlist.songs;
  var songListLen8 = songList8.length;
  for (var songIndex8 = 0; songIndex8 < songListLen8; songIndex8++) {
    var songData8 = songList8[songIndex8];
    output.append(soy.$$escapeHtml(songData8.title), ' - ', soy.$$escapeHtml(songData8.artist), '<br />');
  }
  if (!opt_sb) return output.toString();
};


miniMe.search = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append('\t  ');
  var songList16 = opt_data.results;
  var songListLen16 = songList16.length;
  for (var songIndex16 = 0; songIndex16 < songListLen16; songIndex16++) {
    var songData16 = songList16[songIndex16];
    output.append('<a onclick="enqueue(', soy.$$escapeHtml(songData16.id), ')" href="#">', soy.$$escapeHtml(songData16.title), ' - ', soy.$$escapeHtml(songData16.artist), '</a> <br />');
  }
  if (!opt_sb) return output.toString();
};
