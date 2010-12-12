// This file was automatically generated from miniMe.soy.
// Please don't edit this file by hand.

if (typeof miniMe == 'undefined') { var miniMe = {}; }


miniMe.playlist = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append('<div class="grid_6 nowplaying"><p class="nowplaying-title">Now Playing</p><div class="center"><img src="images/heroes.jpg"/><p class="med-text-white">', soy.$$escapeHtml(opt_data.playlist.nowPlaying.artist), '<br />', soy.$$escapeHtml(opt_data.playlist.nowPlaying.title), '<br/><span class="med-text-white">From the album ', soy.$$escapeHtml(opt_data.playlist.nowPlaying.album), '</span></p></div><br/><p class="nowplaying-title">Coming Up</p><ul class="body-text-white">');
  var songList10 = opt_data.playlist.songs;
  var songListLen10 = songList10.length;
  for (var songIndex10 = 0; songIndex10 < songListLen10; songIndex10++) {
    var songData10 = songList10[songIndex10];
    output.append('<li><span class="band-w">', soy.$$escapeHtml(songData10.artist), '<img src="images/purple-star.png" class="right"/></span><br/>', soy.$$escapeHtml(songData10.title), '<br /><span class="album">From the album ', soy.$$escapeHtml(songData10.album), '</span></li><br />');
  }
  output.append('</ul></div>');
  if (!opt_sb) return output.toString();
};


miniMe.search = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append('<ul><li class="nowplaying-title2">Search Results for "..."</li>');
  var songList22 = opt_data.results;
  var songListLen22 = songList22.length;
  for (var songIndex22 = 0; songIndex22 < songListLen22; songIndex22++) {
    var songData22 = songList22[songIndex22];
    output.append('<li onclick="enqueue(', soy.$$escapeHtml(songData22.id), ')"><span class="band">', soy.$$escapeHtml(songData22.artist), ' &#8194;</span>', soy.$$escapeHtml(songData22.title), '&#8194;<span class="green-plus">+</span></li>');
  }
  output.append('</ul>');
  if (!opt_sb) return output.toString();
};


miniMe.energy = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append(soy.$$escapeHtml(opt_data.currentEnergy), ' / ', soy.$$escapeHtml(opt_data.maxEnergy));
  if (!opt_sb) return output.toString();
};
