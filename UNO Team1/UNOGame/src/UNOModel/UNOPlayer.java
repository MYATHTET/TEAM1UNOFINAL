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
public class UNOPlayer {
    private String playerID;
    private String playerName;
    private ArrayList<UNOCard> HoldCard = new ArrayList<UNOCard>();

    public UNOPlayer() {
    }

    public UNOPlayer(String playerID, String playerName) {
        this.playerID = playerID;
        this.playerName = playerName;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public ArrayList<UNOCard> getHoldCard() {
        return HoldCard;
    }

    public void setHoldCard(ArrayList<UNOCard> HoldCard) {
        this.HoldCard = HoldCard;
    }
    
    public void setCardToHand(UNOCard unc)
    {
        this.HoldCard.add(unc);
    }
    
    public void getCardFromHand(int i)
    {
        this.HoldCard.remove(i);
    }
    
    public int CalculateScore()
    {
         int totalScore = 0;
        
         for(UNOCard card : HoldCard) {
             totalScore += card.getScore();
         }

        return totalScore;
    }
    @Override
    public String toString() {
        return "UNOPlayer:" + "playerID=" + playerID + ", playerName=" + playerName + ", HoldCard=" + HoldCard + "\n\t\t Total Score=" +CalculateScore()+ "\n\t";
    }
    
}
