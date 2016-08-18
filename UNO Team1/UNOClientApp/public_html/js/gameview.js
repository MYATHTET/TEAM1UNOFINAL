$(function () {
//    alert("FUNCTION");
    var CurrentGameId;
    var CurrentGameName;
    var CurrentPlayerId;
    var connection;
//    var playlistTemplate = Handlebars.compile($("#showplayerTemplate").html());

    var displayChat = function (msg) {
        $("#chatarea").prepend(
                $("<div>").text(msg)
                );
    };


    $("#BtnCreate").on("singletap", function () {
        

        $.post("http://localhost:8080/UNOServerApp/api/uno/addgame",
                {gamename: $("#gname").val()})
                .done(function (result) {
                    var json = $.parseJSON(result);
                    CurrentGameId = json.gameid;
                    CurrentGameName = json.gamename;
                   
//                  console.info(">>>>> Create Game ID " +json.gameid);     
//                  console.info(">>>>> Create Game Name "+json.gamename); 
                    $("#createdgameid").text(json.gameid);
                    $("#createdgname").text(json.gamename);
                    $.UIGoToArticle("#game-list");

                    //websocket

                    connection = new WebSocket("ws://localhost:8080/UNOServerApp/unows/" + CurrentGameId);
                    connection.onopen = function () {
                        displayChat("websocket is connected");
                    };

                    connection.onclose = function () {
                        displayChat("websocket is closed");
                    };

                    connection.onmessage = function (msg) {
                        console.info("incoming: ", msg.data);
                        var newMessage = JSON.parse(msg.data);
                        displayChat("Player Name: " + newMessage.pname);
                        
                        var flag = false;
                        flag = newMessage.flag;
                
                        if(flag){
                            $('#discard').append('<img src="images/' + newMessage.image + '.png" alt="'+newMessage.cid+'" title="' + newMessage.image +'" class="peppable3" />');

                        }
                    };


                });


    });

    $("#BtnStart").on("singletap", function () {
//        var playlistTemplate = Handlebars.compile($("#showplayerTemplate").html());
        $.getJSON("http://localhost:8080/UNOServerApp/api/uno/playerlist")
                .done(function (result) {
                   // $("#playerlists").append(playlistTemplate({players: result}));
                    
                    for (var i = 0; i < result.length; i++)
                    {
                        //delete when ui finish
                         //$("#playerlists").append('<div>'+ result[i].pid + " : " + result[i].name + '</div>');
                         $('#playerlists').append('<div class="pname">' + result[i].pid + " : " + result[i].name + '</div>');
                         
                         CurrentPlayerId = result[i].pid;
                    }

                });

        $.getJSON("http://localhost:8080/UNOServerApp/api/uno/discardcard/" + CurrentGameId)
                .done(function (data) {
                    console.info(">>>>>> Result Discard Card ID " + CurrentGameId);
                    //var json = $.parseJSON(data);
                    //console.info("Json data>>>>" + data);
//                    $("#game").append('<h1>' + json.gname + '</h1>');
                    $("#discard").append('<img src="images/' + data.image + '.png" />');
//                    $("#gid").val(json.gid);
                    console.log(data);
                });
           
        //deck
        $.getJSON("http://localhost:8080/UNOServerApp/api/uno/deckcard/" + CurrentGameId)
                .done(function (data) {
                    console.info(">>>>>> Result Discard Card ID " + CurrentGameId);
                    //var json = $.parseJSON(data);
                    //console.info("Json data>>>>" + data);
//                    $("#game").append('<h1>' + json.gname + '</h1>');
                    //$("#game").append('<img src="images/' + data.image + '.png" />');
                    //$("#deck").append('<img src="images/back.png" alt="'++'" title="'++'" />');
//                    $("#gid").val(json.gid);
                    for (var i = 0; i < data.length; i++)
                    {
                        $('#desk').append('<img src="images/back.png" alt="'+data[i].cid+'" title="' + data[i].image +'" class="peppable3" />');
                        $('#desk img').draggable();
                    }
                    console.log(data);
                });
                
        var message = {
            gid: CurrentGameId,
            status: "start"
        };
        connection.send(JSON.stringify(message));
        $.UIGoToArticle("#listplayer");

    });
    
    // deck to player
    $("#deck").on("singletap", "img", function(){
        var deckCid =$(this).attr("alt");
        var deckImage = $(this).attr("title");
        console.info("Current Player ID>>>>" + CurrentPlayerId);
        //send message
            var message ={
                gid: CurrentGameId,
                pid: CurrentPlayerId,
                cid:deckCid,
                image: deckImage,
                serverflag: true
            };
            connection.send(JSON.stringify(message));

            console.info(CurrentGameId+"/"+CurrentPlayerId+"/"+deckCid);


    });
});

