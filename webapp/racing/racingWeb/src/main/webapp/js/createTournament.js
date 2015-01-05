$(document).ready(function(){
    
    $.getJSON("api/circuits", function(result) {
    var options = $("#circuits");
    //don't forget error handling!
    $.map(result, function(item) {
        options.append($("<option />").val(item.id).text(item.name));
    });
    });
    
    if (!$("#txtTournamentId").val()) { 
        $("#btnRaces").attr("disabled", "disabled");
    } else {
        $("#btnRaces").attr("enabled", "enabled");
    }
});

$(function() {
    $( "#datepicker" ).datepicker();
    $('#addCircuit').bind("click", function(e) { 
        var circuit = $("#circuits").find(":selected");
        var laps = $("#txtLaps").val();
        $('#overviewCircuits').append($('<li>')
                .append($('<span>').val(circuit.val()).text(circuit.text()).addClass('circuitid'))
                .append($('<span>').val(laps).text("laps: " + laps).addClass('laps').css('float', 'right'))
                ).addClass('list-group-item'); 
        
    });
});

$( "#races-form" ).submit(function( event ) {
    
  // Stop form from submitting normally
  event.preventDefault();
  var userId = $("#txtUserId").val();
  var tournamentId = $("#txtTournamentId").val();
  
  // Get races in array
  var races = [];
    $('#overviewCircuits li').each(function(i, elem) {
        races.push(
        {
            tournament_id : parseInt(tournamentId),
            circuit_id : parseInt($(elem).find(".circuitid").val()),
            laps :  parseInt($(elem).find(".laps").val())
        });
    });
    console.log(races);
    var apiUrl = "/api/tournaments/" + tournamentId + "/insert/";
    // ajax call
    $.ajax({
        url: apiUrl
    ,   type: 'POST'
    ,   contentType: 'application/json'
    ,   data: { user_id: userId, races: JSON.stringify(races)}
    }).success(function(data, textStatus, jqXHR) {
        $("#message").text("Races zijn succesvol toegevoegd.");
        $("#message").addClass("alert alert-success");
        $("ul#overviewCircuits").empty()
    }).error(function(jqXHR, textStatus, errorThrown) {
        $("#message").text("Er is een fout bij het opslagen van de races.");
        $("#message").addClass("alert alert-warning");
    });
});