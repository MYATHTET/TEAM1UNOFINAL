/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sa42.team1.uno;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author Yamin
 */
@ServerEndpoint("/unows/{gid}")
public class wscontroller {

    @OnOpen
    public void onOpen(Session session, @PathParam("gid") String gid) {
        System.out.println(">>> new connection: " + session.getId());
        Map<String, Object> sessObject = session.getUserProperties();
        sessObject.put("gid", gid);
        System.out.println("gamid: " + gid);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println(">> close connection: " + session.getId());
        System.out.println("\t close reason: " + reason.getReasonPhrase());
    }

    @OnMessage
    public void onMessage(Session session, String msg) {
        InputStream is = new ByteArrayInputStream(msg.getBytes());
        JsonReader reader = Json.createReader(is);
        JsonObject data = reader.readObject();
        String topic = data.getString("gid");

        session.getOpenSessions().stream()
                .filter(s -> {
                    return (topic.equals(
                            (String) s.getUserProperties().get("gid")));
                }).forEach(s -> {
            try {
                s.getBasicRemote().sendText(msg);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
