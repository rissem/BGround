// This file was automatically generated from miniMe.soy.
// Please don't edit this file by hand.

if (typeof miniMe == 'undefined') { var miniMe = {}; }


miniMe.playlist = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append('<div class="grid_5 nowplaying"><p class="title-w">Now Playing</p><img src="images/heroes.jpg"/><p class="list-white"><span class="band">', soy.$$escapeHtml(opt_data.playlist.nowPlaying.artist), '</span><br />', soy.$$escapeHtml(opt_data.playlist.nowPlaying.title), '<br/><span class="album-w">From the album ', soy.$$escapeHtml(opt_data.playlist.nowPlaying.album), '</span></p></div><div class="grid_5 comingup"><p class="title-w">COMING UP</p><ul class="body-text-white">');
  var songList10 = opt_data.playlist.songs;
  var songListLen10 = songList10.length;
  for (var songIndex10 = 0; songIndex10 < songListLen10; songIndex10++) {
    var songData10 = songList10[songIndex10];
    output.append('<li><span class="band">', soy.$$escapeHtml(songData10.artist), '<img src="images/purple-star.png" class="right"/></span><br/>', soy.$$escapeHtml(songData10.title), '<br /><span class="album-w">From ', soy.$$escapeHtml(songData10.album), '</span></li><br />');
  }
  output.append('</ul></div></div>');
  if (!opt_sb) return output.toString();
};


miniMe.search = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append('<ul><li class="headline">Desi\'s List</li>');
  var songList22 = opt_data.results.songs;
  var songListLen22 = songList22.length;
  for (var songIndex22 = 0; songIndex22 < songListLen22; songIndex22++) {
    var songData22 = songList22[songIndex22];
    output.append('<li onclick="enqueue(', soy.$$escapeHtml(songData22.id), ', humanMsg.displayMsg);"><span class="band">', soy.$$escapeHtml(songData22.artist), ' &#8194;</span>', soy.$$escapeHtml(songData22.title), '\t&#8194;<span class="green-plus">+</span></li>');
  }
  output.append('</ul></div></div>');
  if (!opt_sb) return output.toString();
};


miniMe.energy = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append(soy.$$escapeHtml(opt_data.currentEnergy), ' / ', soy.$$escapeHtml(opt_data.maxEnergy));
  if (!opt_sb) return output.toString();
};
