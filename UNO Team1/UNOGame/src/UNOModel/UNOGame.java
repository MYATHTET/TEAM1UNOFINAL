/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UNOModel;

import java.util.ArrayList;
 

/**
 *
 * @author e0000708
 */
public class UNOGame {

    private String GameID;
    private String GameName;

    private ArrayList<UNOPlayer> playerList;
    private String Status;
    private UNODeck unodeck;
    private UNOCard unocard;

    public String getGameName() {
        return GameName;
    }

    public void setGameName(String GameName) {
        this.GameName = GameName;
    }

    public UNOGame(String GameID, String GameName, String Status) {
        this.GameID = GameID;
        this.GameName = GameName;
        this.playerList = new ArrayList<UNOPlayer>();
        unodeck = new UNODeck();
        this.Status = Status;
    }

    public String getGameID() {
        return GameID;
    }

    public void setGameID(String GameID) {
        this.GameID = GameID;
    }

    public ArrayList<UNOPlayer> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<UNOPlayer> playerList) {
        this.playerList = playerList;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public UNODeck getUnodeck() {
        return unodeck;
    }

    public void setUnodeck(UNODeck unodeck) {
        this.unodeck = unodeck;
    }

    public UNOCard getUnocard() {
        return unocard;
    }

    public void setUnocard(UNOCard unocard) {
        this.unocard = unocard;
    }
    
    public void addPlayer(UNOPlayer player) {
        this.playerList.add(player);
    }
    
    public void removePlayer(UNOPlayer player) {
        this.playerList.add(player);
    }
    

    public void startPlay() {
         unodeck.ShuffleDeck();
        //unodeck = this.ShuffleDeck(unodeck.addDeck());
        
        //clear
        for (UNOPlayer player : playerList) {
            player.setHoldCard(new ArrayList<UNOCard>());

        }

        int count = 1;
        while (count <= 7) {
            for (UNOPlayer player : playerList) {
                UNOCard handCard = unodeck.getCardFromDeck();
                player.setCardToHand(handCard);
            }

            count++;
        }
        unocard = unodeck.getCardFromDeck();
    }

    public int CalculateTotalScore() {
        int totalScore = 0;

        for (UNOPlayer player : playerList) {
            totalScore += player.CalculateScore();
        }

        return totalScore;
    }

    @Override
    public String toString() {
        return "Game ID=" + GameID + "\nDiscard Card=" + unocard + "\nGameStatus=" + Status + "\n" + unodeck + "\n\t" + playerList;
    }

}
