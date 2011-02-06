$(document).ready(
    function() {
	updatePlaylist();
	updateSets();
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

function updateSets(){
$.get("sets",
      function(data) {
	  data = JSON.parse(data);
	  $("#sets").html(miniMe.sets({sets:data}));
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

    if ($("#searchBox").val() != "") {
	$("#searchResults").html("Searching for " + $("#searchBox").val() + "...");
    }

    $.get("searchSongs?search="+escape(query),
	 function(data) {
	     data = JSON.parse(data);
	     if (query == $("#searchBox").val()) {
		 $("#searchResults").html(miniMe.search({results:data}));
	     }
	 }
	 );
}

function fetchSet(setId)
{
    $.get("fetchSet?setId=" + setId,
	 function(data) {
	     data = JSON.parse(data);
	     $("#searchResults").html(miniMe.search({results:data}));
	 }
	 );
}