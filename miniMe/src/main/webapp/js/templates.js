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
    output.append('<li><span class="band">', soy.$$escapeHtml(songData10.artist), '<img src="images/purple-star.png" class="right"/></span><br/>', soy.$$escapeHtml(songData10.title), '<br /><span class="album-w">From ', soy.$$escapeHtml(songData10.album), '</span></li><br />');
  }
  output.append('</ul></div></div>');
  if (!opt_sb) return output.toString();
};


miniMe.search = function(opt_data, opt_sb) {
  var output = opt_sb || new soy.StringBuilder();
  output.append('<ul>', (opt_data.results.query) ? '<li class="headline">Search Results for "' + soy.$$escapeHtml(opt_data.results.query) + '"</li>' : '', (opt_data.results.title) ? '<li class="headline">' + soy.$$escapeHtml(opt_data.results.title) + '</li>' : '');
  var songList32 = opt_data.results.songs;
  var songListLen32 = songList32.length;
  if (songListLen32 > 0) {
    for (var songIndex32 = 0; songIndex32 < songListLen32; songIndex32++) {
      var songData32 = songList32[songIndex32];
      output.append('<li><span style="cursor:pointer" onclick="enqueue(', soy.$$escapeHtml(songData32.id), ', humanMsg.displayMsg);"><span class="band">', soy.$$escapeHtml(songData32.artist), ' &#8194;</span>', soy.$$escapeHtml(songData32.title), '\t&#8194;<span class="green-plus">+</span></span>', (opt_data.results.admin) ? '<a href="/miniMe/ban?songId=' + soy.$$escapeHtml(songData32.id) + '"><img src="/miniMe/images/x.png"></a>' : '', '</li>');
    }
  } else {
    output.append('<li>No results were found</li>');
  }
  output.append('</ul></div></div>');
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
  var setList56 = opt_data.sets.sets;
  var setListLen56 = setList56.length;
  for (var setIndex56 = 0; setIndex56 < setListLen56; setIndex56++) {
    var setData56 = setList56[setIndex56];
    output.append('<li class="band2" style="cursor:pointer" onclick="fetchSet(', soy.$$escapeHtml(setData56.id), ')">', soy.$$escapeHtml(setData56.name), '</li>');
  }
  output.append('</ul>');
  if (!opt_sb) return output.toString();
};
