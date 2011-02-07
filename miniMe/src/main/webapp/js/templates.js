// This file was automatically generated from miniMe.soy.
// Please don't edit this file by hand.

if (typeof miniMe == 'undefined') { var miniMe = {}; }


miniMe.playlist = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append('<div class="grid_5 nowplaying"><p class="title-w">Now Playing</p><p class="list-white"><span class="band">', soy.$$escapeHtml(opt_data.playlist.nowPlaying.artist), '</span><br />', soy.$$escapeHtml(opt_data.playlist.nowPlaying.title), '<br/><span class="album-w">From the album ', soy.$$escapeHtml(opt_data.playlist.nowPlaying.album), '</span></p></div><div class="grid_5 comingup"><p class="title-w">COMING UP</p><ul class="body-text-white">');
  var songList10 = opt_data.playlist.songs;
  var songListLen10 = songList10.length;
  for (var songIndex10 = 0; songIndex10 < songListLen10; songIndex10++) {
    var songData10 = songList10[songIndex10];
    output.append('<li><span class="band">', soy.$$escapeHtml(songData10.artist), '<img src="images/purple-star.png" class="right"/></span><br/>', soy.$$escapeHtml(songData10.title), '<br />', (songData10.album) ? '<span class="album-w">From ' + soy.$$escapeHtml(songData10.album) + '</span></li>' : '', '<br />');
  }
  output.append('</ul></div></div>');
  if (!opt_sb) return output.toString();
};


miniMe.search = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append((! opt_data.results.query && ! opt_data.results.title) ? '<div class="headline">All Songs</div>' : '', (opt_data.results.query) ? '<div class="headline">Search Results for "' + soy.$$escapeHtml(opt_data.results.query) + '"</div>' : '', (opt_data.results.title) ? '<div class="headline">' + soy.$$escapeHtml(opt_data.results.title) + '</div>' : '', (opt_data.results.title || opt_data.results.query) ? '<div style="width:250px;margin-top:-15px"><a href="javascript:showAll();">Show all songs</a></div>' : '', '<table width="100%">');
  var songList42 = opt_data.results.songs;
  var songListLen42 = songList42.length;
  if (songListLen42 > 0) {
    for (var songIndex42 = 0; songIndex42 < songListLen42; songIndex42++) {
      var songData42 = songList42[songIndex42];
      output.append('<tr', (songIndex42 % 2 == 0) ? ' class="songRow" ' : ' class="songRowAlt" ', '><td><span class="green-plus">+&nbsp;&nbsp;</span></td><td style="cursor:pointer; padding-left:5px; padding-right:5px" onclick="enqueue(', soy.$$escapeHtml(songData42.id), ', humanMsg.displayMsg);"><strong>', soy.$$escapeHtml(songData42.artist), '</strong>&nbsp&nbsp;&nbsp;</td><td style="cursor:pointer" onclick="enqueue(', soy.$$escapeHtml(songData42.id), ', humanMsg.displayMsg);">', soy.$$escapeHtml(songData42.title), '</td></tr>');
    }
  } else {
    output.append('No results were found');
  }
  output.append('</table>');
  if (!opt_sb) return output.toString();
};


miniMe.energy = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append(soy.$$escapeHtml(opt_data.currentEnergy), ' / ', soy.$$escapeHtml(opt_data.maxEnergy));
  if (!opt_sb) return output.toString();
};


miniMe.sets = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append('<p class="title">Sets</p><ul>');
  var setList68 = opt_data.sets.sets;
  var setListLen68 = setList68.length;
  for (var setIndex68 = 0; setIndex68 < setListLen68; setIndex68++) {
    var setData68 = setList68[setIndex68];
    output.append('<li class="band2" style="cursor:pointer" onclick="fetchSet(', soy.$$escapeHtml(setData68.id), ')">', soy.$$escapeHtml(setData68.name), '</li>');
  }
  output.append('</ul>');
  if (!opt_sb) return output.toString();
};
