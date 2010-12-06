$(document).ready(
    function() {
	updatePlaylist();
	updateSearch();
	window.setInterval("updatePlaylist();", 1000);
	//limit the # of results that can be returned, make sure request come back in order
	$("#search").keypress(updateSearch);
    }
);

function updatePlaylist()
{
    $.get("playlist",
	  function(data) {
	      data = JSON.parse(data);
	      $("#playlist").html(miniMe.playlist({playlist:data}));
	      $("#energy").html(miniMe.energy({maxEnergy:data.maxEnergy, currentEnergy:data.currentEnergy}));
	  }		  
	 );
}

function enqueue(songId)
{
    //TODO handle a bad request and change to post
    $.get("enqueue?songId="+songId, function(){updatePlaylist();});
    return false;
}

function updateSearch()
{
    var query = $("#searchBox").val();
    $.get("searchSongs?search="+query,
	 function(data) {
	     data = JSON.parse(data);
	     $("#searchResults").html(miniMe.search({results:data}));
	 }
	 );
}