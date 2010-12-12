// This file was automatically generated from miniMe.soy.
// Please don't edit this file by hand.

if (typeof miniMe == 'undefined') { var miniMe = {}; }


miniMe.playlist = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append('<div class="grid_6 nowplaying"><p class="nowplaying-title">Now Playing</p><div class="center"><img src="images/heroes.jpg"/><p class="med-text-white">', soy.$$escapeHtml(opt_data.playlist.nowPlaying.artist), '<br />', soy.$$escapeHtml(opt_data.playlist.nowPlaying.title), '<br/><span class="med-text-white">From the album Heroes</span></p></div><br/><p class="nowplaying-title">Coming Up</p><ul class="body-text-white">');
  var songList8 = opt_data.playlist.songs;
  var songListLen8 = songList8.length;
  for (var songIndex8 = 0; songIndex8 < songListLen8; songIndex8++) {
    var songData8 = songList8[songIndex8];
    output.append('<li><span class="band-w">', soy.$$escapeHtml(songData8.artist), '<img src="images/purple-star.png" class="right"/></span><br/>', soy.$$escapeHtml(songData8.title), '<br /><span class="album">From the album Oracular Spectacular</span></li><br />');
  }
  output.append('</ul></div>');
  if (!opt_sb) return output.toString();
};


miniMe.search = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append('<ul><li class="nowplaying-title2">Search Results for "..."</li>');
  var songList18 = opt_data.results;
  var songListLen18 = songList18.length;
  for (var songIndex18 = 0; songIndex18 < songListLen18; songIndex18++) {
    var songData18 = songList18[songIndex18];
    output.append('<li onclick="enqueue(', soy.$$escapeHtml(songData18.id), ')"><span class="band">', soy.$$escapeHtml(songData18.artist), ' &#8194;</span>', soy.$$escapeHtml(songData18.title), '&#8194;<span class="green-plus">+</span></li>');
  }
  output.append('</ul>');
  if (!opt_sb) return output.toString();
};


miniMe.energy = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append(soy.$$escapeHtml(opt_data.currentEnergy), ' / ', soy.$$escapeHtml(opt_data.maxEnergy));
  if (!opt_sb) return output.toString();
};
