var eventsLimit1 = 0, eventsLimit2 = 10;
$(document).ready(function() {
    $('#userId').css('display', 'none');
    
    $('#more').click(function(ev){
        loadEvents();
    });
    loadEvents();
});

function loadEvents() {
    var sendInfo = {
        limit1: eventsLimit1.toString(),
        limit2: eventsLimit2.toString()
    };

    $.ajax({
        type: "POST",
        url: "api/events/" + $('#userId').text(),
        dataType: "json",
        success: function(data) {
            console.log(data);
            var events = data;
            events.forEach(function(entry) {
                var $item = $("<li>").appendTo('#recentEvents');
                var action = entry.action.replace('User ' + $('#userId').text(), 'You');
                $item.append($("<span>").addClass("action").text(action));
                $item.append($("<span>").addClass("time").text(entry.timestamp));
            });
            eventsLimit1 = eventsLimit2 + 1;
            eventsLimit2 += 5;
        },
        data: sendInfo
    });
}