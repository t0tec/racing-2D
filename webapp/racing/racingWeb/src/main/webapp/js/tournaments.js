var upcomingLimit1 = 0, upcomingLimit2 = 10;
$(document).ready(function() {

    var sendInfo = {
        limit1: upcomingLimit1.toString(),
        limit2: upcomingLimit2.toString(),
        action: "upcoming"
    };

    $.ajax({
        type: "POST",
        url: "tournament.do",
        dataType: "json",
        success: function(data) {
            var tournaments = data[0], enrolled = data[1], statuses = data[2];
            console.log(statuses);
            tournaments.forEach(function(entry) {
                var $tr = $("<tr>").appendTo('#tournamentsView');
                $tr.append($("<td>").text(entry.name)).append($("<td>").text(entry.formule)).append($("<td>").text(entry.date.toLocaleString()));
                var filtered = $(enrolled).filter(function() {
                    return this.id == entry.id;
                });
                if (filtered.length > 0) {
                    $tr.append($("<td>").append($("<a>").text("Uitschrijven").attr("href", "tournament.do?action=resign&tournamentId=" + entry.id).click({id : entry.id},resignTournament))).addClass("warning");
                } else {
                    $tr.append($("<td>").append($("<a>").text("Inschrijven").attr("href", "tournament.do?action=enroll&tournamentId=" + entry.id).click({id : entry.id},enrollTournament)));
                }
                $tr.append($("<td>").append($("<a>").text("Details")));
            });
        },
        data: sendInfo
    });
});

function enrollTournament(event) {
    event.preventDefault();
    var url = $(this).attr('href');
    $(this).text("Uitschrijven").attr("href", "tournament.do?action=resign&tournamentId=" + event.data.id).click({id : event.data.id},resignTournament);

    $(this).parent().parent().addClass('warning');
    $.get(url, function(data) {
        $("#dialog p").text('Je bent ingeschreven voor dit toernooi');
        $("#dialog").dialog();
        console.log(data);
    });
}
;

function resignTournament (event) {
    event.preventDefault();

    var url = $(this).attr('href');
    $(this).parent().parent().removeClass('warning');
    $(this).text("Inschrijven").attr("href", "tournament.do?action=enroll&tournamentId=" + event.data.id).click({id : event.data.id},enrollTournament);

    $.get(url, function(data) {
        $("#dialog p").text('Je bent uitgeschreven voor dit toernooi');
        $("#dialog").dialog();
        console.log(data);
    });
};
