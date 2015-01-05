$(document).ready(function() {	
	console.log("Starting ajax request");
	
	$.ajax({
	    type: 'POST',
	    contentType: 'application/json',
	    url: "circuit.do?action=get",
	    dataType: "json",
	    success: function(data, textStatus, jqXHR){
                console.log(data);
	        $.each(data, function(key, val) {
	        	$('<a>',{
	        	    text: val.name,
	        	    title: val.name,
	        	    href: 'circuitBuilder.jsp?edit=' + val.id,
	        	    class : 'list-group-item',
                            id : val.id
	        	}).appendTo('div.list-group');
	        });
	    },
	    error: function(jqXHR, textStatus, errorThrown){
	    	console.log('Failure: !');
	    	console.log(jqXHR + " " + textStatus + " " + errorThrown);
	    }
	});

});
