var builderCols, builderRows;
var canvasWidth, canvasHeight;
var cellWidth = 110;
var cellHeight = 110;
var builder = $('#builder');
var tileTypes = ['', 'EARTH', 'START', 'STRAIGHT', 'STRAIGHT_UP', 'L_TURN', 'L_TURN_90', 'L_TURN_180', 'L_TURN_270', 'CROSS'];
var spriteValues = [0, 0, 110, 220, 330, 440, 550, 660, 770, 880];
var drawOnBuilder = false, mouseUp = false, mouseDown = false;
var prevTile, prevPrevTile;
var circuitId, originalTiles;
var checks = [], originalChecks;
var startTile = null;
var obstacles;
var obstacleTypes = ['MELON', 'STRAWBERRY', 'PADDO', 'EGGPLANT'];

$(document).ready(function() {
    var buttons = $(".btn-default");
    buttons.on("click",function(){
        checkTiles();
    });
    $('.masterTooltip').hover(function() {
        // Hover over code
        var title = $(this).attr('title');
        $(this).data('tipText', title).removeAttr('title');
        $('<p class="tooltipObstacle"></p>')
                .text(title)
                .appendTo('body')
                .fadeIn('slow');
    }, function() {
        // Hover out code
        $(this).attr('title', $(this).data('tipText'));
        $('.tooltipObstacle').remove();
    }).mousemove(function(e) {
        var mousex = e.pageX + 20; //Get X coordinates
        var mousey = e.pageY + 10; //Get Y coordinates
        $('.tooltipObstacle')
                .css({top: mousey, left: mousex})
    });

    if (typeof circuitId !== 'undefined') {
        loadCircuit();
    }

});

function checkArrayContains(array, item) {
    var index = $.grep(array, function(e) {
        return (e.col === item.col && e.row === item.row);
    });
    return (index.length === 1) ? true : false;
}

function getIndexFromCheckArray(array, checkItem) {
    var index;
    $.grep(array, function(e, i) {
        if (e.col === checkItem.col && e.row === checkItem.row) {
            index = i;
            return i;
        }
    });
    return (typeof index !== 'undefined') ? index + 1 : 0;
}


function removeCheck(checkItem) {
    var index = getIndexFromCheckArray(checks, checkItem);
    if (index > 0) {
        checks.splice(index - 1, 1);
        $("#checks_holder").empty();
        $.each(checks, function(key, entry) {
            $("#checks_holder").append('<li><span>Row: ' + entry.row + '</span> <span>Column: ' + entry.col + '</span></li>');
        });
    }
}

function outOfBounds(tile) {
    // check if out of bounds
    if (tile.col < 0 || tile.col >= builderCols || tile.row < 0 || tile.row >= builderRows) {
        return true;
    }
    else
        return false;
}
function checkTiles(newCheck, saveMode) {
    // console.log("check tiles");
    var right = [2, 3, 7, 8, 9];
    var down = [4, 5, 8, 9];
    var left = [2, 3, 5, 6, 9];
    var up = [4, 6, 7, 9];
    var message = "Circuit is correct opgebouwd.", allOk = true;
    if (startTile === null) {
        message = "Er is nog geen start-tegel geplaatst.";
        allOk = false;
        $("#message").show().text(message).addClass("alert alert-warning");
        return allOk;
    }

    // show loader
    $("#loader").show();
    var newTile = {col: null, row: null}, pTile = startTile, newChecks = [], condition = true;
    var direction = $('.active input[name=direction]').val();
    do {
        // get newTile coordinates
        if (direction === 'right') {
            newTile = {row: pTile.row, col: pTile.col + 1};
            if (outOfBounds(newTile)) {
                message = "Circuit gaat buiten de grenzen van de dimensies.";
                allOk = false;
                break;
            }
            else {
                newTile['type'] = builder.getLayer(newTile.col + '-' + newTile.row).data.tileType;
                if ($.inArray(newTile.type, right) === -1) {
                    message = "De tegel op kolom " + newTile.col + ", rij " + newTile.row + " is foutief.";
                    allOk = false;
                    break;
                }
                else if (newTile.type === 7)
                    direction = "down";
                else if (newTile.type === 8)
                    direction = "up";
                else
                    direction = "right";

            }
        }
        else if (direction === 'left') {
            newTile = {row: pTile.row, col: pTile.col - 1};
            if (outOfBounds(newTile)) {
                message = "Circuit gaat buiten de grenzen van de dimensies.";
                allOk = false;
                break;
            }
            else {
                newTile['type'] = builder.getLayer(newTile.col + '-' + newTile.row).data.tileType;
                if ($.inArray(newTile.type, left) === -1) {
                    message = "De tegel op kolom " + newTile.col + ", rij " + newTile.row + " is foutief.";
                    allOk = false;
                    break;
                }
                else if (newTile.type === 5)
                    direction = "up";
                else if (newTile.type === 6)
                    direction = "down";
                else
                    direction = "left";
            }
        }
        else if (direction === 'up') {
            newTile = {row: pTile.row - 1, col: pTile.col};
            if (outOfBounds(newTile)) {
                message = "Circuit gaat buiten de grenzen van de dimensies.";
                allOk = false;
                break;
            }
            else {
                newTile['type'] = builder.getLayer(newTile.col + '-' + newTile.row).data.tileType;
                if ($.inArray(newTile.type, up) === -1) {
                    message = "De tegel op kolom " + newTile.col + ", rij " + newTile.row + " is foutief.";
                    allOk = false;
                    break;
                }
                else if (newTile.type === 4)
                    direction = "up";
                else if (newTile.type === 6)
                    direction = "right";
                else if (newTile.type === 7)
                    direction = "left";
            }
        }
        else if (direction === 'down') {
            newTile = {row: pTile.row + 1, col: pTile.col};
            if (outOfBounds(newTile)) {
                message = "Circuit gaat buiten de grenzen van de dimensies.";
                allOk = false;
                break;
            }
            else {
                newTile['type'] = builder.getLayer(newTile.col + '-' + newTile.row).data.tileType;
                if ($.inArray(newTile.type, down) === -1) {
                    message = "De tegel op kolom " + newTile.col + ", rij " + newTile.row + " is foutief.";
                    allOk = false;
                    break;
                }
                else if (newTile.type === 4)
                    direction = "down";
                else if (newTile.type === 5)
                    direction = "right";
                else if (newTile.type === 8)
                    direction = "left";
            }
        }

        if (allOk && newTile.type === 9) {
            // search for checkpoint
            var rightDir = {row: newTile.row, col: newTile.col + 1};
            var leftDir = {row: newTile.row, col: newTile.col - 1};
            var upDir = {row: newTile.row - 1, col: newTile.col};
            var downDir = {row: newTile.row + 1, col: newTile.col};
            var item = {col: newTile.col, row: newTile.row};
            if (!(rightDir.row === pTile.row && rightDir.col === pTile.col) && (checkArrayContains(checks, rightDir) && !checkArrayContains(newChecks, rightDir))
                    || (typeof newCheck !== 'undefined' && !checkArrayContains(newChecks, rightDir) && (rightDir.row === newCheck.row && rightDir.col === newCheck.col))) {
                direction = "right";
            }
            else if (!(leftDir.row === pTile.row && leftDir.col === pTile.col) && (checkArrayContains(checks, leftDir) && !checkArrayContains(newChecks, leftDir))
                    || (typeof newCheck !== 'undefined' && !checkArrayContains(newChecks, leftDir) && (leftDir.row === newCheck.row && leftDir.col === newCheck.col))) {
                direction = "left";
            }
            else if (!(upDir.row === pTile.row && upDir.col === pTile.col) && (checkArrayContains(checks, upDir) && !checkArrayContains(newChecks, upDir))
                    || (typeof newCheck !== 'undefined' && !checkArrayContains(newChecks, upDir) && (upDir.row === newCheck.row && upDir.col === newCheck.col))) {
                direction = "up";
            }
            else if (!(downDir.row === pTile.row && downDir.col === pTile.col) && (checkArrayContains(checks, downDir) && !checkArrayContains(newChecks, downDir))
                    || (typeof newCheck !== 'undefined' && !checkArrayContains(newChecks, downDir) && (downDir.row === newCheck.row && downDir.col === newCheck.col))) {
                direction = "down";
            }
            else if (typeof newCheck === 'undefined' && (checkArrayContains(newChecks, rightDir) || checkArrayContains(newChecks, leftDir) || checkArrayContains(newChecks, upDir) || checkArrayContains(newChecks, downDir))) {
                message = "Er bevindt zich een loop rond het kruispunt op kolom " + item.col + ", rij " + item.row;
                allOk = false;
                break;
            }
            else {
                message = "Plaats een checkpoint op de eerste tegel na het kruispunt op kolom " + item.col + ", rij " + item.row;
                allOk = false;
                break;
            }

        }

        if (allOk) {
            // push newTile to checks
            var item = {col: newTile.col, row: newTile.row};

            if (checkArrayContains(checks, item)) {
                newChecks.push(item);
            }
            else if (typeof newCheck !== 'undefined' && (item.row === newCheck.row && item.col === newCheck.col))
                newChecks.push(item);
        }
        pTile = newTile;
        if (pTile.col === startTile.col && pTile.row === startTile.row && pTile.type === startTile.type) {
            condition = false;
        }
    } while (condition)

    // return result
    if (allOk) {
        // console.log(newChecks);
        checks = newChecks;
        $("#checks_holder").empty();
        $.each(checks, function(key, entry) {
            $("#checks_holder").append('<li><span>Row: ' + entry.row + '</span> <span>Column: ' + entry.col + '</span></li>');
        });

        // show message
        $("#message").show().text(message).removeClass("alert-warning").addClass("alert alert-success");
    }
    else {
        $("#message").show().text(message).addClass("alert alert-warning");
    }
    $("#loader").hide();
    return allOk;
}

function loadCircuit() {
    var url = "circuit.do?action=getCircuit&id=" + circuitId;
    var name, direction;
    $.ajax({
         url: url
         , type: 'GET'
         , contentType: 'application/json'
         , dataType: 'json'
     }).then(function (data) {
        circuitId = parseInt(circuitId);
        builderCols = data.columns;
        builderRows = data.rows;
        $("#inputRows").val(builderRows);
        $("#inputCols").val(builderCols);
        name = data.name;
        direction = data.direction;
        originalTiles = data.tiles;

        $("#saveCircuitButton").removeAttr('disabled');
        $("#circuitName").attr("placeholder", name);
        $("#circuitName").val(name);
        if (direction === "RIGHT") {
            $('#direction .btn').first().removeClass("active");
            $('#direction .btn').last().addClass("active");
        }

        originalObstacles = data.obstacles;

        generateBuilder();
        // console.log("CHECKS");
        // console.log(checks);
        originalChecks = checks.slice();
    })
}

$('#generateCanvas').click(function(e) {
    e.preventDefault();
    checks = [];
    startTile = null;
    $("#saveCircuitButton").removeAttr('disabled');
    builderRows = parseInt($('#inputRows').val());
    builderCols = parseInt($('#inputCols').val());
    generateBuilder();
});
$("#components img").draggable({
    helper: 'clone',
    start: function(event, ui) {
        var newSize = (cellWidth / 110) * ui.helper.width() + 'px';
        ui.helper.css({'width': newSize, 'height': newSize});
    }
});
$("#saveCircuit").submit(function(event) {
// Stop form from submitting normally
    event.preventDefault();
    if (!checkTiles())
        return;
    $("#loader").show();
    var circuitName = ($("#circuitName").val() == "") ? "Default Circuit" : $("#circuitName").val();
    var startDirection = $('.active input[name=direction]').val();
    // Get tiles in array
    var rows = builderRows;
    var cols = builderCols;
    var tiles = [], insertTiles = [], deleteTiles = [];
    var layers = builder.getLayerGroup('tiles');
    // console.log(checks, originalChecks);

    $.each(layers, function(index, layer) {
        var coord = layer.name.split('-');
        var xVal = parseInt(coord[0]);
        var yVal = parseInt(coord[1]);
        var tileType = layer.data.tileType;
        if (typeof circuitId !== 'undefined') {
            var tileId = layer.data.tileId;
            if ((typeof tileId !== "undefined")) {
                if (tileType !== layer.data.tileTypeOrig || (getIndexFromCheckArray(checks, {col: xVal, row: yVal}) !== getIndexFromCheckArray(originalChecks, {col: xVal, row: yVal}))) {
                    var data = {
                        id: tileId,
                        x: xVal,
                        y: yVal,
                        type: tileTypes[tileType],
                        circuitid: circuitId,
                        checkpoint: getIndexFromCheckArray(checks, {col: xVal, row: yVal})
                    };
                    if (tileType > 1 && tileType <= 9) {
                        // console.log("update");
                        tiles.push(data);
                    }
                    else {
                        // console.log("delete");
                        deleteTiles.push(data);
                    }
                }
            }
            else if (tileType > 1 && tileType <= 9) {
                // console.log("insert");
                insertTiles.push(
                        {
                            x: xVal,
                            y: yVal,
                            type: tileTypes[tileType],
                            circuitid: circuitId,
                            checkpoint: getIndexFromCheckArray(checks, {col: xVal, row: yVal})
                        });
            }
        }
        else if (tileType > 1 && tileType <= 9) {
            // console.log("create");
            tiles.push(
                    {
                        x: xVal,
                        y: yVal,
                        type: tileTypes[tileType],
                        checkpoint: getIndexFromCheckArray(checks, {col: xVal, row: yVal})
                    });
        }
    });
    var obstacle_layers = builder.getLayerGroup('obstacles');
    var save_obstacles = [], update_obstacles = [], delete_obstacles = [];
    if (typeof obstacle_layers !== 'undefined') {
        $.each(obstacle_layers, function(index, layer) {
            var coord = layer.name.split(':');
            var xVal = parseInt(coord[0]);
            var yVal = parseInt(coord[1]);
            var obstacle_data = layer.data;

            if (typeof circuitId !== 'undefined') {
                //update
                var tileId = builder.getLayer(xVal + '-' + yVal).data.tileId;

                var obstacleResult = $.grep(originalObstacles, function(e) {
                    // console.log(e.id, obstacle_data['id']);
                    return (e.id === obstacle_data['id']);
                });

                if ((typeof tileId !== "undefined") && obstacleResult.length === 1 && (obstacleResult[0].place !== obstacle_data['place'] || obstacleResult[0].obstacleType !== obstacle_data['type'])) {
                    // update obstacle
                    obstacle_data['tileId'] = tileId;
                    update_obstacles.push(obstacle_data);

                }
                else if (obstacleResult.length !== 1) {
                    // new obstacle
                    obstacle_data['col'] = xVal;
                    obstacle_data['row'] = yVal;
                    save_obstacles.push(obstacle_data);
                }

                if (obstacleResult.length === 1) {
                    // console.log(obstacleResult[0].place, obstacle_data['place'],
                    // obstacleResult[0].obstacleType, obstacle_data['type']);
                    var index;
                    $.grep(originalObstacles, function(e, i) {
                        if (e.id === obstacle_data['id']) {
                            index = i;
                            return i;
                        }
                    });
                    if (index >= 0) {
                        originalObstacles.splice(index, 1);
                    }
                }

            }
            else {
                //insert
                obstacle_data['col'] = xVal;
                obstacle_data['row'] = yVal;
                save_obstacles.push(obstacle_data);
            }


        });
    }
    if (typeof circuitId !== 'undefined') {
        delete_obstacles = originalObstacles.slice();
    }
    // console.log(delete_obstacles);
    var sendInfo;
    if (typeof circuitId === 'undefined') {
        sendInfo = {name: circuitName,
            direction: startDirection,
            rows: rows,
            cols: cols,
            tiles: JSON.stringify(tiles),
            obstacles: JSON.stringify(save_obstacles),
            action: "create"
        };
    }
    else {
        sendInfo = {id: circuitId,
            name: circuitName,
            direction: startDirection,
            rows: rows,
            cols: cols,
            tiles: JSON.stringify(tiles),
            insertTiles: JSON.stringify(insertTiles),
            deleteTiles: JSON.stringify(deleteTiles),
            obstacles: JSON.stringify(save_obstacles),
            update_obstacles: JSON.stringify(update_obstacles),
            delete_obstacles: JSON.stringify(delete_obstacles),
            action: "update"
        };
    }


    //ajax call
    $.post("circuit.do", sendInfo).success(function(data, textStatus, jqXHR) {
        if (typeof (data.id) !== 'undefined') {
            circuitId = data.id;
        }
        checks = [],
                originalChecks = [],
                obstacles = [],
                originalObsticals = [];
        $("#checks_holder").empty();
        loadCircuit();
        $("#loader").hide();
        $("#message").show().text("Circuit is opgeslaan").removeClass("alert-warning").addClass("alert alert-success");
    }).error(function(jqXHR, textStatus, errorThrown) {
        loadCircuit();
        $("#loader").hide();
        $("#message").show().text("Er is een fout bij het opslaan").addClass("alert alert-warning");
        console.log(sendInfo);
    });
});
builder.droppable({
    accept: "img",
    drop: function(event, ui) {
        var tile = ui.draggable.attr("id");
        var x = (ui.helper.offset().left - $(this).offset().left) - ui.helper.width() / 2;
        var y = (ui.helper.offset().top - $(this).offset().top) - ui.helper.height() / 2;
        x = Math.min(x, $(this).width());
        y = Math.min(y, $(this).height());
        var layerName = Math.min(Math.round(x / cellWidth), builderCols - 1) + '-' + Math.min(Math.round(y / cellHeight), builderRows - 1);
        var data = builder.getLayer(layerName).data;
        var newTile = {col: parseInt(layerName.split('-')[0]), row: parseInt(layerName.split('-')[1])};
        if (tile === 'checkpoint' && $.inArray(data.tileType, [3, 4, 5, 6, 7, 8]) !== -1) {
            if (checkTiles(newTile)) {
                builder.setLayer(layerName, {
                    source: $('#checks')[0]
                });
            }
        }
        else if (tile.match(/^obstacle/)) {
            // bonus or obstacle
            var onLayer = builder.getLayer(layerName);
            var layerX = onLayer.x;
            var layerY = onLayer.y;
            var coord = onLayer.name.split('-');
            var col = coord[0];
            var row = coord[1];
            var scale = (cellWidth / 110);
            var origWidth = ui.helper.width(), origHeight = ui.helper.height();
            var tileId, obstacleId;
            if ($.inArray(builder.getLayer(col + '-' + row).data.tileType, [3, 4, 5, 6, 7, 8]) === -1)
                return;
            if (typeof builder.getLayer(col + ':' + row) !== 'undefined') {
                tileId = builder.getLayer(col + ':' + row).data.tileId;
                obstacleId = builder.getLayer(col + ':' + row).data.id;
                builder.removeLayer(col + ':' + row);
            }
            x += ui.helper.width();
            var drawingObject = {
                source: $('#' + tile)[0],
                width: origWidth, height: origHeight,
                fromCenter: false,
                layer: true,
                groups: ['obstacles'],
                name: col + ':' + row,
                bringToFront: true,
                disableEvents: true
            };
            if (builder.getLayer(col + '-' + row).data.tileType === 3) {
                // console.log(y, layerY + cellHeight);
                if (x > layerX && y > layerY && y <= (layerY + cellHeight / 3)) {
                    // place left
                    drawingObject['y'] = layerY + (20 * scale);
                    drawingObject['x'] = layerX + (scale * 55) - (scale * 16);
                    drawingObject['data'] = {
                        place: 'LEFT',
                        obstacleType: obstacleTypes[parseInt(tile.substr(tile.length - 1))],
                        tileId: tileId,
                        id: obstacleId
                    };
                    builder.drawImage(drawingObject);
                }
                else if (y > (layerY + cellWidth / 3) && y <= layerY + cellWidth && x > layerX) {
                    // place right
                    drawingObject['y'] = layerY + (58 * scale);
                    drawingObject['x'] = layerX + (scale * 55) - (16 * scale);
                    drawingObject['data'] = {
                        place: 'RIGHT',
                        obstacleType: obstacleTypes[parseInt(tile.substr(tile.length - 1))],
                        tileId: tileId,
                        id: obstacleId
                    };
                    builder.drawImage(drawingObject);
                }
            }
            else {
                if (x > layerX && x <= (layerX + cellWidth / 2) && y > layerY) {
                    // place left
                    drawingObject['x'] = layerX + (20 * scale);
                    drawingObject['y'] = layerY + (scale * 55) - (scale * 16);
                    drawingObject['data'] = {
                        place: 'LEFT',
                        obstacleType: obstacleTypes[parseInt(tile.substr(tile.length - 1))],
                        tileId: tileId,
                        id: obstacleId
                    };
                    builder.drawImage(drawingObject);
                }
                else if (x > (layerX + cellWidth / 2) && x <= layerX + cellWidth && y > layerY) {
                    // place right
                    drawingObject['x'] = layerX + (58 * scale);
                    drawingObject['y'] = layerY + (scale * 55) - (16 * scale);
                    drawingObject['data'] = {
                        place: 'RIGHT',
                        obstacleType: obstacleTypes[parseInt(tile.substr(tile.length - 1))],
                        tileId: tileId,
                        id: obstacleId
                    };
                    builder.drawImage(drawingObject);
                }
            }

        }
        else if ($.inArray(parseInt(tile), [1, 2, 3, 4, 5, 6, 7, 8, 9]) !== -1) {
            data.tileType = parseInt(tile);
            builder.setLayer(layerName, {
                source: $('#tiles')[0],
                sx: spriteValues[tile],
                data: data
            });
            var coord = layerName.split('-');
            var col = coord[0];
            var row = coord[1];
            if (typeof builder.getLayer(col + ':' + row) !== 'undefined') {
                builder.removeLayer(col + ':' + row);
            }
            removeCheck(newTile);
            checkTiles();
        }
    }
});
function generateBuilder() {

    if (builderRows > 6 || builderCols > 7) {
        var sugSize1 = Math.floor(800 / builderCols);
        var sugSize2 = Math.floor(680 / builderRows);
        var size = Math.min(sugSize1, sugSize2);
        cellWidth = size;
        cellHeight = size;
    }
    else {
        cellWidth = 110;
        cellHeight = 110;
    }
    canvasWidth = (builderCols * cellWidth) + 1;
    canvasHeight = (builderRows * cellHeight) + 1;
    builder.clearCanvas();
    builder.removeLayers();
    builder.attr("width", canvasWidth);
    builder.attr("height", canvasHeight);
    var row = 0, col = 0;
    for (var y = 0; y < (canvasHeight - 1); y += cellHeight) {
        for (var x = 0; x < (canvasWidth - 1); x += cellWidth) {
            var data, sx, source = $('#tiles')[0];
            if (typeof circuitId !== 'undefined') {
                var tileResult = $.grep(originalTiles, function(e) {
                    return (e.x === col && e.y === row);
                });
                if (tileResult.length === 1) {
                    if (tileResult[0].checkpoint > 0) {
                        source = $('#checks')[0];
                        checks.splice(tileResult[0].checkpoint, 0, {col: col, row: row});
                    }
                    sx = spriteValues[$.inArray(tileResult[0].type, tileTypes)];
                    data = {
                        tileId: tileResult[0].id,
                        tileType: $.inArray(tileResult[0].type, tileTypes),
                        tileTypeOrig: $.inArray(tileResult[0].type, tileTypes)
                    };
                    if (tileResult[0].type === "START") {
                        startTile = {col: col, row: row, type: 2};
                    }

                }
                else {
                    sx = 0;
                    data = {
                        tileType: 1
                    };
                }



            }
            else {
                sx = 0;
                data = {
                    tileType: 1
                };
            }
            builder.drawImage({
                source: source,
                x: x, y: y,
                width: cellWidth, height: cellHeight,
                sWidth: 110,
                sHeight: 110,
                sx: sx, sy: 0,
                cropFromCenter: false,
                fromCenter: false,
                layer: true,
                groups: ['tiles'],
                name: col + '-' + row,
                bringToFront: false,
                data: data,
                cursors: {
                    mouseover: 'pointer',
                    mousedown: 'pointer'
                },
                click: function(layer) {
                    if (!mouseUp) {
                        if (layer.data.tileType < 9) {
                            var newImg = layer.sx + 110;
                            // console.log(layer.data);
                            var data = layer.data;
                            data.tileType += 1;
                            $(this).setLayer(layer.name, {
                                sx: newImg,
                                data: data
                            });
                        }
                        else {
                            var data = layer.data;
                            data.tileType = 1;
                            $(this).setLayer(layer.name, {
                                sx: 0,
                                data: data
                            });
                        }
                        var coord = layer.name.split('-');
                        var col = coord[0];
                        var row = coord[1];
                        if (typeof builder.getLayer(col + ':' + row) !== 'undefined') {
                            builder.removeLayer(col + ':' + row);
                        }
                        mouseDown = false;
                    }
                    else {
                        mouseUp = false;
                        mouseDown = false;
                    }

                },
                mousedown: function(layer) {
                    mouseDown = true;
                },
                mouseup: function(layer) {
                    if (drawOnBuilder) {
                        drawOnBuilder = false;
                        prevPrevTile = null;
                        prevTile = null;
                        mouseUp = true;
                        mouseDown = false;
                        checkTiles();
                    }
                    else {
                        mouseUp = false;
                    }
                },
                mouseover: function(layer) {
                    if (drawOnBuilder && prevTile && prevPrevTile) {
                        var newType = getNewTileType(prevPrevTile, prevTile, layer);
                        if (newType > 0) {
                            var newImg = spriteValues[newType];
                            var data = $(this).getLayer(prevTile.name).data;
                            data.tileType = newType;
                            $(this).setLayer(prevTile.name, {
                                sx: newImg,
                                data: data
                            });
                            var coord = prevTile.name.split('-');
                            var col = coord[0];
                            var row = coord[1];
                            if (typeof builder.getLayer(col + ':' + row) !== 'undefined') {
                                builder.removeLayer(col + ':' + row);
                            }
                        }
                    }

                },
                mouseout: function(layer) {
                    if (mouseDown) {
                        if (prevTile) {
                            prevPrevTile = prevTile;
                            drawOnBuilder = true;
                        }
                        prevTile = layer;
                    }

                },
                change: function(layer) {
                    if (layer.data.tileType === 2) {
                        if (startTile !== null) {
                            var data = $(this).getLayer(startTile.col + '-' + startTile.row).data;
                            data.tileType = 3;
                            $(this).setLayer(startTile.col + '-' + startTile.row, {
                                sx: spriteValues[3],
                                data: data
                            });
                        }
                        startTile = {col: parseInt(layer.name.split('-')[0]), row: parseInt(layer.name.split('-')[1]), type: 2};
                    }
                    else if (startTile !== null && layer.name === startTile.col + '-' + startTile.row) {
                        startTile = null;
                    }
                    $(this).drawLayers();
                }


            });
            if (typeof circuitId !== 'undefined') {
                var tileResult = $.grep(originalTiles, function(e) {
                    return (e.x === col && e.y === row);
                });
                if (tileResult.length === 1) {
                    var obstacleResult = $.grep(originalObstacles, function(e) {
                        return (e.tileId === tileResult[0].id);
                    });
                    if (obstacleResult.length === 1) {
                        // console.log('ok');
                        var onLayer = builder.getLayer(col + '-' + row);
                        var layerX = onLayer.x;
                        var layerY = onLayer.y;
                        var scale = (cellWidth / 110);
                        var origWidth = 32 * scale, origHeight = 32 * scale;
                        if ($.inArray(builder.getLayer(col + '-' + row).data.tileType, [3, 4, 5, 6, 7, 8]) === -1)
                            return;
                        if (typeof builder.getLayer(col + ':' + row) !== 'undefined') {
                            builder.removeLayer(col + ':' + row);
                        }
                        //x += 32*scale;
                        var drawingObject = {
                            source: $('#obstacle' + obstacleTypes.indexOf(obstacleResult[0].obstacleType))[0],
                            width: origWidth, height: origHeight,
                            fromCenter: false,
                            layer: true,
                            groups: ['obstacles'],
                            name: col + ':' + row,
                            bringToFront: true,
                            disableEvents: true
                        };
                        if (builder.getLayer(col + '-' + row).data.tileType === 3) {
                            if (obstacleResult[0].place === "LEFT") {
                                // place left
                                drawingObject['y'] = layerY + (20 * scale);
                                drawingObject['x'] = layerX + (scale * 55) - (scale * 16);
                                drawingObject['data'] = {place: 'LEFT', obstacleType: obstacleResult[0].obstacleType, id: obstacleResult[0].id};
                                builder.drawImage(drawingObject);
                            }
                            else {
                                // place right
                                drawingObject['y'] = layerY + (58 * scale);
                                drawingObject['x'] = layerX + (scale * 55) - (16 * scale);
                                drawingObject['data'] = {place: 'RIGHT', obstacleType: obstacleResult[0].obstacleType, id: obstacleResult[0].id};
                                builder.drawImage(drawingObject);
                            }
                        }
                        else {
                            if (obstacleResult[0].place === "LEFT") {
                                // place left
                                drawingObject['x'] = layerX + (20 * scale);
                                drawingObject['y'] = layerY + (scale * 55) - (scale * 16);
                                drawingObject['data'] = {place: 'LEFT', obstacleType: obstacleResult[0].obstacleType, id: obstacleResult[0].id};
                                builder.drawImage(drawingObject);
                            }
                            else {
                                // place right
                                drawingObject['x'] = layerX + (58 * scale);
                                drawingObject['y'] = layerY + (scale * 55) - (16 * scale);
                                drawingObject['data'] = {place: 'RIGHT', obstacleType: obstacleResult[0].obstacleType, id: obstacleResult[0].id};
                                builder.drawImage(drawingObject);
                            }
                        }
                    }
                }
            }
            col++;
        }
        col = 0;
        row++;
    }
    generateGrid();
    $.each(checks, function(key, entry) {
        $("#checks_holder").append('<li><span>Row: ' + entry.row + '</span> <span>Column: ' + entry.col + '</span></li>');
    });
}

function getNewTileType(tile1, tile2, tile3) {
    var right = [3, 7, 8];
    var down = [4, 5, 8];
    var left = [3, 5, 6];
    var up = [4, 6, 7];
    // get coordinates
    var coord1 = tile1.name.split('-');
    var x1 = coord1[0];
    var y1 = coord1[1];
    var type1 = tile1.data.tileType;
    var coord2 = tile2.name.split('-');
    var x2 = coord2[0];
    var y2 = coord2[1];
    var coord3 = tile3.name.split('-');
    var x3 = coord3[0];
    var y3 = coord3[1];
    // left
    if (x1 > x2 && y1 === y2) {
        if (x3 < x2 && y3 === y2) {
// check for cross
            var tile1 = {col: parseInt(x2), row: parseInt(y2) - 1};
            var tile2 = {col: parseInt(x2), row: parseInt(y2) + 1};
            if (crossTile(tile1, up, tile2, down))
                return 9;
            return 3;
        }
        else if (x3 === x2 && y3 < y2) {
// check for cross
            var tile1 = {col: parseInt(x2) - 1, row: parseInt(y2)};
            var tile2 = {col: parseInt(x2), row: parseInt(y2) + 1};
            if (crossTile(tile1, left, tile2, down))
                return 9;
            return 5;
        }
        else if (x3 === x2 && y3 > y2) {
// check for cross
            var tile1 = {col: parseInt(x2) - 1, row: parseInt(y2)};
            var tile2 = {col: parseInt(x2), row: parseInt(y2) - 1};
            if (crossTile(tile1, left, tile2, up))
                return 9;
            return 6;
        }
    }
// up
    else if (x1 === x2 && y1 < y2) {
        if (x3 === x2 && y3 > y2) {
// check for cross
            var tile1 = {col: parseInt(x2) + 1, row: parseInt(y2)};
            var tile2 = {col: parseInt(x2) - 1, row: parseInt(y2)};
            if (crossTile(tile1, right, tile2, left))
                return 9;
            return 4;
        }
        else if (x3 > x2 && y3 === y2) {
// check for cross
            var tile1 = {col: parseInt(x2) + 1, row: parseInt(y2)};
            var tile2 = {col: parseInt(x2), row: parseInt(y2) - 1};
            if (crossTile(tile1, right, tile2, up))
                return 9;
            return 5;
        }
        else if (x3 < x2 && y3 === y2) {
// check for cross
            var tile1 = {col: parseInt(x2) + 1, row: parseInt(y2)};
            var tile2 = {col: parseInt(x2), row: parseInt(y2) + 1};
            if (crossTile(tile1, right, tile2, down))
                return 9;
            return 8;
        }
    }
// right
    else if (x1 < x2 && y1 === y2) {
        if (x3 > x2 && y3 === y2) {
// check for cross
            var tile1 = {col: parseInt(x2), row: parseInt(y2) - 1};
            var tile2 = {col: parseInt(x2), row: parseInt(y2) + 1};
            if (crossTile(tile1, up, tile2, down))
                return 9;
            return 3;
        }
        else if (x3 === x2 && y3 > y2) {
// check for cross
            var tile1 = {col: parseInt(x2), row: parseInt(y2) - 1};
            var tile2 = {col: parseInt(x2) + 1, row: parseInt(y2)};
            if (crossTile(tile1, up, tile2, right))
                return 9;
            return 7;
        }
        else if (x3 === x2 && y3 < y2) {
// check for cross
            var tile1 = {col: parseInt(x2) + 1, row: parseInt(y2)};
            var tile2 = {col: parseInt(x2), row: parseInt(y2) + 1};
            if (crossTile(tile1, right, tile2, down))
                return 9;
            return 8;
        }
    }
// down
    else if (x1 === x2 && y1 > y2) {
        if (x3 === x2 && y3 < y2) {
// check for cross
            var tile1 = {col: parseInt(x2) + 1, row: parseInt(y2)};
            var tile2 = {col: parseInt(x2) - 1, row: parseInt(y2)};
            if (crossTile(tile1, right, tile2, left))
                return 9;
            return 4;
        }
        else if (x3 < x2 && y3 === y2) {
// check for cross
            var tile1 = {col: parseInt(x2), row: parseInt(y2) - 1};
            var tile2 = {col: parseInt(x2) + 1, row: parseInt(y2)};
            if (crossTile(tile1, up, tile2, right))
                return 9;
            return 7;
        }
        else if (x3 > x2 && y3 === y2) {
// check for cross
            var tile1 = {col: parseInt(x2) - 1, row: parseInt(y2)};
            var tile2 = {col: parseInt(x2), row: parseInt(y2) - 1};
            if (crossTile(tile1, left, tile2, up))
                return 9;
            return 6;
        }
    }
// invalid
    else {
        prevTile = null;
        drawOnBuilder = false;
        return 0;
    }
}

function crossTile(tile1, types1, tile2, types2) {
    var type1 = (typeof (builder.getLayer(tile1.col + '-' + tile1.row)) !== 'undefined') ? builder.getLayer(tile1.col + '-' + tile1.row).data.tileType : 0;
    var type2 = (typeof (builder.getLayer(tile2.col + '-' + tile2.row)) !== 'undefined') ? builder.getLayer(tile2.col + '-' + tile2.row).data.tileType : 0;
    if (type1 > 0 && type2 > 0) {
        return ($.inArray(type1, types1) !== -1
                && $.inArray(type2, types2) !== -1);
    }
    else
        return false;
}

function generateGrid() {

    var obj = {
        layer: true,
        strokeStyle: '#ccc',
        strokeWidth: 1,
        rounded: true,
        bringToFront: true,
        disableEvents: true
    };
    /* vertical lines */
    for (var x = 0.5; x <= canvasWidth; x += cellWidth) {
        obj['x1'] = x;
        obj['y1'] = 0;
        obj['x2'] = x;
        obj['y2'] = canvasHeight;
        builder.drawLine(obj);
    }

    /* horizontal lines */
    for (var y = 0.5; y <= canvasHeight; y += cellHeight) {
        obj['x1'] = 0;
        obj['y1'] = y;
        obj['x2'] = canvasWidth;
        obj['y2'] = y;
        builder.drawLine(obj);
    }
}
