$(function () {
    var gid;
    var playerId;
    var name;
    var connection = null;
    var displayChat = function (msg) {
        $("#chatarea").prepend(
                $("<div>").text(msg)
                );
    };


    $.getJSON("http://localhost:8080/UNOServerApp/api/uno/availablegame")
            .done(function (result) {
                console.info(">> result >>" + result.length);
                for (var i = 0; i < result.length; i++)
                {
                    console.info(">> result create game:", result[i].GameId);
                    if (result[i].Status === "waiting")
                    {
                        var h3 = "<li><h3 class='gid' id='" + result[i].GameId + "'>" + result[i].GameId + "</h3>";
                        $("#gamelists").append(h3 + "<h4 class='gname' id='" + result[i].GameName + "'>" + result[i].GameName + "</h4></li>");

                    }
                }
               

            });


    $("#gamelists").on("singletap", "li", function () {

        gid = $(this).find("h3").attr("id");
        name = $(this).find("h4").text();
        console.info("game id>>>" + gid);
        console.info("game name>>>" + name);
        $("#gameid").text(gid);
        $("#gamename").text(name);

        $.UIGoToArticle("#newplayer");


    });

    $("#BtnAdd").on("singletap", function () {
        //websocket open
        connection = new WebSocket("ws://localhost:8080/UNOServerApp/unows/" + gid);

        connection.onopen = function () {
            console.info(">>" + connection.toString());
            displayChat("websocket is connected");

            //send message
            var message = {
                gid: gid,
                pname: $("#playername").val()
            };
            connection.send(JSON.stringify(message));
        };

        connection.onclose = function () {
            displayChat("websocket is closed");
        };

        connection.onmessage = function (msg) {
            console.info("incoming: ", msg.data);
            var newMessage = JSON.parse(msg.data);
            displayChat("Player Name: " + newMessage.pname);
        };

        $.post("http://localhost:8080/UNOServerApp/api/uno/joinplayer",
                {GameId: gid,
                    PlayerName: $("#playername").val()})
                .done(function (result) {
                    var json = $.parseJSON(result);
                    console.info(json.GameName);
                    playerId = json.PlayerId;
                    $("#gname").append("Waiting For " + json.GameName + " to start");
                    $.UIGoToArticle("#waitingGame");
                });

    });


    //timer
    var timer = setInterval(myTimer, 1000);

    function myTimer() {
        var d = new Date();
        var status = "";
        connection.onmessage = function (msg) {
            console.info("incoming: ", msg.data);
            var newMessage = JSON.parse(msg.data);
            status = newMessage.status;

            if (status == "start") {
                alert("started");
                $.getJSON("http://localhost:8080/UNOServerApp/api/uno/playcard/" + gid + "/" + playerId)
                        .done(function (result) {
                            console.info(">> result create game>>" + result.length);
                            for (var i = 0; i < result.length; i++)
                            {
                                console.info(">> result create game:", result[i].image);
                                $("#card").append('<img src="images/' + result[i].image + '.png" alt="'+ result[i].id +'" title="'+ result[i].image + '" class="absolute' + i + '" />');

                            }

                        });
                $.UIGoToArticle("#playgame");
            }
            
            var serverflag = false;
            serverflag = newMessage.serverflag;
                console.info(">>>>>>>Before Card to Player" + playerId + " "+newMessage.pid);
                if(serverflag && newMessage.pid === playerId){
                    console.info(">>>>>>> Card to Player" + newMessage.image);
                    $('#card').append('<img src="images/' + newMessage.image + '.png" alt="'+newMessage.cid+'" title="' + newMessage.image + '"/>');

            }

        };
//        console.info("this is status : " + status + ": new message:" + newMessage.status + ": msg.data :" + msg.data);
//        document.getElementById("demo").innerHTML = d.toLocaleTimeString() + ": " + status;
    }

    // chose discard card to table
    $("#card").on("singletap", "img", function(){
        var cardid =$(this).attr("alt");
        var cardimage = $(this).attr("title");
        console.info("cid:"+cardid+":img"+cardimage);
        //send message
            var message ={
                gid: gid,
                cid:cardid,
                image: cardimage,
                flag: true
            };
            connection.send(JSON.stringify(message));


    });

});

