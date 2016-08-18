/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UNOModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 *
 * @author e0000708
 */
public class UNODeck 
{
    
     private ArrayList<UNOCard> deck = new ArrayList<UNOCard>();

    public UNODeck() {
       addDeck();
    }

    public ArrayList<UNOCard> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<UNOCard> deck) {
        this.deck = deck;
    }
    
    public void setCardToDeck(UNOCard und){
        this.getDeck().add(und);
    }
    public UNOCard getCardFromDeck()
    {
        return this.getDeck().remove(0);
    }
    public void addDeck()
    {
        String color[] = {"Red", "Yellow", "Green", "Blue", "RYGB"};
        String type[] = {"Normal","Skip","Reverse","Draw2","Wild", "WildDrawFour"};
        int cardId = 1;
        UNOCard ucard;
         
       for(int c = 0; c < color.length; c++){
            
            int typeIndex = 0;
            
            for(int n = 0; n <= 12; n++ ){
                
                int value = n;
                
                if(n > 9){
                    typeIndex++;
                    value = 20;
                }
                                
                if(c !=4){
                   ucard = new UNOCard(cardId+"",color[c], type[typeIndex], value, color[c]+type[typeIndex]+value);
                   this.setCardToDeck(ucard);
                   cardId++;
                
                if(n != 0) this.setCardToDeck(ucard); 
                }
                   
            }
            
            if(c == 4 ){
                int counter =1;
                while(counter <= 4){
                    
                    for(int t = 4; t <type.length; t++){
                        ucard = new UNOCard(cardId +"",color[c], type[t], 50, color[c]+type[t]+50);
                        this.setCardToDeck(ucard);   
                        cardId++;
                    }
                    
                    counter++;
                }
            }
        }

       
    }    

    
    public void ShuffleDeck(){
        Collections.shuffle(getDeck());
    }
    
    @Override
    public String toString() 
    {
       return "UNO Card on Deck: " + this.getDeck().size()+"\n"+deck;
    }  
        
}    
    
    

