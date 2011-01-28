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
	      $("#nowPlaying").html(miniMe.playlist({playlist:data}));
	      $("#energy").html(miniMe.energy({maxEnergy:data.maxEnergy, currentEnergy:data.currentEnergy}));
	  }		  
	 );
}

function enqueue(songId, alertFunction)
{
    //TODO handle a bad request and change to post
    $.get("enqueue?songId="+songId, function(data){updatePlaylist(); alertFunction(data);});
    return false;
}

function updateSearch()
{
    var query = $("#searchBox").val();
    $.get("searchSongs?search="+query,
	 function(data) {
	     data = JSON.parse(data);
	     if (query == $("#searchBox").val()) {
		 $("#searchResults").html(miniMe.search({results:data}));
		 $("#collectionTitle").html("Search Results for \"" + query + "\"");		 
	     }
	 }
	 );
}
