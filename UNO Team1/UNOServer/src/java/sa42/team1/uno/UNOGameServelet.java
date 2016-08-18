
package sa42.team1.uno;


import java.util.UUID;
import javax.enterprise.context.RequestScoped;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import UNOModel.UNOGame;
import UNOModel.UNOPlayer;
import UNOModel.UNOCard;
import java.io.Console;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.smartcardio.Card;
import javax.ws.rs.GET;
import javax.ws.rs.PathParam;
/**
 *
 * @author Yamin
 */
@RequestScoped
@Path("/uno")
public class UNOGameServelet {
   public static Map<String,UNOGame> hashmap = new HashMap<String, UNOGame>();
   public static String CurrentGameID="";
   public static String CurrentPlayerID="";
   private final String localhostport = "http://10.10.18.5:8383";
    @POST
    @Path("/addgame")
    @Produces("text/plain")
    public Response AddGame(@FormParam("gamename") String gamename )
    {
        JsonObject jObj;
        CurrentGameID = UUID.randomUUID().toString().substring(0,8);
        UNOGame game = new UNOGame(CurrentGameID,gamename,"waiting");
        hashmap.put(CurrentGameID,game);
        System.out.println("Game ID>>>" +CurrentGameID);       
        System.out.println("Game Name>>>" +gamename);       
        
        jObj = Json.createObjectBuilder()
            .add("gameid",game.getGameID())
            .add("gamename", game.getGameName())
            .add("Status", game.getStatus())
            .build();
        
        
        Response resp = Response.ok(jObj.toString())
                .build();
        return resp;
        
    }

    @GET
    @Path("/availablegame")
    @Produces("text/plain")
    public Response AvailableGameList(){
        JsonObject Jobj ;
        JsonArrayBuilder arrBuilder = Json.createArrayBuilder();

        Iterator entries = hashmap.entrySet().iterator();
        while (entries.hasNext()) {
          Map.Entry thisEntry = (Map.Entry) entries.next();
          Object key = thisEntry.getKey();
          Object value = thisEntry.getValue();
          
           Jobj = Json.createObjectBuilder()
            .add("GameId",thisEntry.getKey().toString())
            .add("GameName", ((UNOGame)thisEntry.getValue()).getGameName())
            .add("Status", ((UNOGame)thisEntry.getValue()).getStatus())
            .build();
           
           arrBuilder.add(Jobj);
        }
         
        Response resp = Response.ok(arrBuilder.build().toString())
                .build();
       
        return resp;
        
    }
    
    @POST
    @Path("/joinplayer")
    @Produces("text/plain")
    public Response JoinPlayer(@FormParam("GameId") String gid, @FormParam("PlayerName") String pname){
        
        CurrentGameID = gid;
        String playerName = pname;
        String playerId = UUID.randomUUID().toString().substring(0,8);
        JsonObject Jobj;
 
        
        UNOPlayer player = new UNOPlayer(playerId, playerName);
        CurrentPlayerID = playerId;
        
        hashmap.get(CurrentGameID).addPlayer(player);
        System.out.println(">>>>>> Player List " + hashmap.get(CurrentGameID).getPlayerList().toString());
   
        UNOGame game = hashmap.get(CurrentGameID);
        
        Jobj = Json.createObjectBuilder()
            .add("GameId",game.getGameID())
            .add("GameName",game.getGameName())
            .add("PlayerId",CurrentPlayerID)
            .build();

        Response resp = Response.ok(Jobj.toString())
                .build();
        return resp;
        
    }
  
    @GET
    @Path("/playerlist")
    @Produces("text/plain")
    public Response listPlayer(){
        JsonObject Jobj ;
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        
        UNOGame game = hashmap.get(CurrentGameID); 
                
        game.setStatus("start");
        ArrayList<UNOPlayer> pList = game.getPlayerList();
        
        for (int i = 0; i < pList.size(); i++) {
            UNOPlayer player = pList.get(i);
            
            Jobj = Json.createObjectBuilder()
            .add("pid",player.getPlayerID())
            .add("name", player.getPlayerName())
            .add("status",game.getStatus())
            .build();
           
           arrayBuilder.add(Jobj);
            
        }
        
        Response resp = Response.ok(arrayBuilder.build().toString())
                    .build();
        System.out.println(">>>> Player List" + arrayBuilder.toString());
        return resp;
    }
    
    @GET
    @Path("/discardcard/{gameid}")
    @Produces("text/plain")
    public Response discardCards(@PathParam("gameid") String gid){
        JsonObject jso ;
        CurrentGameID=gid;
        UNOGame game = hashmap.get(CurrentGameID);
        game.startPlay();
        UNOCard card = game.getUnocard();
        
        jso = Json.createObjectBuilder()
            .add("gid",game.getGameID())
            .add("gname",game.getGameName())
            .add("color",card.getColor())
            .add("type", card.getType())
            .add("value",card.getScore())
            .add("image",card.getImage())
            .build();

       
        Response resp = Response.ok(jso.toString())
                   .header("Access-Control-Allow-Origin", "*")
                   .build();
        return resp;
    }
    
    //deck
    @GET
    @Path("/deckcard/{gameid}")
    @Produces("text/plain")
    public Response deckCards(@PathParam("gameid") String gid){
        JsonObject jso ;
        JsonArrayBuilder jsa = Json.createArrayBuilder();
        
        CurrentGameID=gid;
        UNOGame game = hashmap.get(CurrentGameID);

        UNOCard card = game.getUnocard();
        ArrayList<UNOCard> deckcard = game.getUnodeck().getDeck();
        
        for(int i = 0; i < deckcard.size(); i++){
            jso = Json.createObjectBuilder()
            .add("gid",game.getGameID())
            .add("gname",game.getGameName())
            .add("cid", deckcard.get(i).getCardid())
            .add("color",deckcard.get(i).getColor())
            .add("type", deckcard.get(i).getType())
            .add("value",deckcard.get(i).getScore())
            .add("image",deckcard.get(i).getImage())
            .build();

            jsa.add(jso);
        }
        
        
       
        Response resp = Response.ok(jsa.build().toString())
                   .header("Access-Control-Allow-Origin", "*")
                   .build();
        return resp;
    }
    
    @GET
    @Path("/playcard/{gameid}/{playerid}")
    @Produces("text/plain")
    public Response showPlayerCards(@PathParam("gameid") String gid ,@PathParam("playerid") String pid ){
        JsonObject jso ;
        JsonArrayBuilder jsa = Json.createArrayBuilder();
        UNOPlayer player=null;
        CurrentGameID = gid;
        CurrentPlayerID = pid;
        UNOGame game = hashmap.get(CurrentGameID);
        
        System.out.println("Show Player Current Game ID >>>>" + CurrentGameID);
        System.out.println("Show Player Current Player ID >>>>" + CurrentPlayerID);
        
        
        
        ArrayList<UNOPlayer> pList = game.getPlayerList();
        for (int i = 0; i < pList.size(); i++) {
            
            player = pList.get(i);
            if(player.getPlayerID().equals(CurrentPlayerID)){
                break;
            }
            else{
                player =null;
            }
            
        }
        
        ArrayList<UNOCard> cardinhand = player.getHoldCard();
        
        for (int i = 0; i < cardinhand.size(); i++) {
            UNOCard card = cardinhand.get(i);
            
            jso = Json.createObjectBuilder()
            .add("id", card.getCardid())
            .add("color",card.getColor())
            .add("type", card.getType())
            .add("value",card.getScore())
            .add("image",card.getImage())
            .build();

           jsa.add(jso);
            
        }
        //System.out.println(jsa.build().toString());
        Response resp = Response.ok(jsa.build().toString())
                 .build();
        
        return resp;
    }
    
    
//     //player to table
//    @POST
//    @Path("/cardtodiscardpile")
//    @Produces("text/plain")
//   // public void discardCardPlayerToTable(@PathParam("gid") String gid, @PathParam("pid") String pid, @PathParam("cid") String cid){
//    public void discardCardPlayerToTable(@FormParam("gid") String gid, @FormParam("pid") String pid, @FormParam("cid") String cid){
////        JsonObject jso ;
////        JsonArrayBuilder jsa = Json.createArrayBuilder();
////        
//        System.out.println("I am in disccar UNOGame");
//        CurrentGameID = gid;
//        CurrentPlayerID = pid;
//        String strdiscardcid = cid;
//        
//        System.out.println(">>>>>IN discardcard Card " + gid +": " + pid+": " + cid);
//        UNOPlayer player=null;
//        UNOGame g = hashmap.get(CurrentGameID);
//        
//        System.out.println(">>>game: "+CurrentGameID);
//        //System.out.println(">>>"+g);
//        
//        ArrayList<UNOPlayer> pList = g.getPlayerList();
//        for (int i = 0; i < pList.size(); i++) {
//            
//            player = pList.get(i);
//            if(player.getPlayerID().equals(CurrentPlayerID)){
//                break;
//            }
//            else{
//                player =null;
//            }
//            
//        }
//        
//        ArrayList<UNOCard> cardinhand = player.getHoldCard();
//        
//        for (int i = 0; i < cardinhand.size(); i++) {
//            UNOCard card = cardinhand.get(i);
//            
//            if(card.getCardid().equals(strdiscardcid)){
//                g.setDiscardPile(player.getCardFromHand(i));
//                g.s
//            }
//        }
//        System.err.println("Discard:"+g.getDiscardPile()+": hand"+player.getCardInHand());
//
////        Response resp = Response.ok(jsa.build().toString())
////                //.header("Access-Control-Allow-Origin", "*")
////                .build();
////        
////        return resp;
//    }
//    
   
  
}
