$(document).ready(function() {
	var sendInfo = {
			action: "favorites"
	};
	
	$.when(
		$.ajax({
		    type: 'GET',
		    contentType: 'application/json',
		    url: "api/circuits",
		    dataType: "json",
		    error: function(jqXHR, textStatus, errorThrown){
		    	console.log('Failure: !');
		    	console.log(jqXHR + " " + textStatus + " " + errorThrown);
		    }
		}), 
		
	    $.ajax({
	        type: "POST",
	        url: "circuit.do",
	        dataType: "json",
	        data: sendInfo
	    })
	).then(function(getCircuits, getFavorites) {
		circuits = getCircuits[0];
		favorites = getFavorites[0];
		
		circuits.forEach(function(entry) {
			var $tr = $("<tr>").appendTo("#circuitsView");
	        var filtered = $(favorites).filter(function() {
	            return this.id == entry.id;
	        });
	        
	        if (filtered.length > 0) {
	        	$tr.append($("<td>").text(entry.name)).append($("<td>").text(entry.designer));
	    		$tr.append($("<td>").append($("<a>").text("Details").attr("href","circuitView.jsp?id=" + entry.id).click({ id : entry.id}, viewCircuit)));
	    		$tr.append($("<td>").append($("<a>").attr("class","btn btn-default btn-lg").append($("<span>").addClass("glyphicon glyphicon-star")).attr("href","circuit.do?action=unfavorite&circuitId=" + entry.id).click({id : entry.id}, unfavoriteCircuit)));
	        } else {
	        	$tr.append($("<td>").text(entry.name)).append($("<td>").text(entry.designer));
	        	$tr.append($("<td>").append($("<a>").text("Details").attr("href","circuitView.jsp?id=" + entry.id).click({ id : entry.id},viewCircuit)));
	        	$tr.append($("<td>").append($("<a>").attr("class","btn btn-default btn-lg").append($("<span>").addClass("glyphicon glyphicon-star-empty")).attr("href","circuit.do?action=favorite&circuitId=" + entry.id).click({id : entry.id},favoriteCircuit)));
	        }
		});
	});

});

function viewCircuit(event){
	console.log(event);
}

function favoriteCircuit(event){
	event.preventDefault();
	var url = $(this).attr('href');
	$(this).attr("href","circuit.do?action=unfavorite&circuitId=" + event.data.id).click({id : event.data.id}, unfavoriteCircuit);
	$(this).children(":first").removeClass("glyphicon glyphicon-star-empty");
	$(this).children(":first").addClass("glyphicon glyphicon-star");
	$.get(url, function(data) {
        $("#dialog p").text('Je hebt ' + data + ' toegevoegd van je favoriete lijst');
        $("#dialog").dialog();
        console.log(data);
    });
}

function unfavoriteCircuit(event){
	event.preventDefault();
	var url = $(this).attr('href');
	$(this).children(":first").removeClass("glyphicon glyphicon-star");
	$(this).children(":first").addClass("glyphicon glyphicon-star-empty");
	$(this).attr("href","circuit.do?action=favorite&circuitId=" + event.data.id).click({id : event.data.id}, favoriteCircuit);
	$.get(url, function(data) {
        $("#dialog p").text('Je hebt ' + data + ' verwijdert van je favoriete lijst');
        $("#dialog").dialog();
        console.log(data);
    });
}
