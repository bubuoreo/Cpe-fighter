package com.sp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import com.sp.repository.FireRepository;
//import com.sp.tools.Comm;

//import Common.cp.CardDTO;
//import Common.cp.UserDTO;

@Service
public class FireService {
	
	@Autowired
	FireRepository fireRepository;
	
	
	public CardDTO buy(Integer id_card, Integer buyer_id) {
		
		CardDTO card = Comm.getRemoteCard(id_card);
		if(card !=null) {
			UserDTO buyer = Comm.getRemoteUser(buyer_id);
			UserDTO seller = Comm.getRemoteUser(card.getPlayerId());
			if(buyer.getSold() >= card.getPrice()) {
				
				// Mise a jour du détenteur de la carte et de son statut a vendre ou non
                card.setPlayerId(buyer.getId());
                card.setIsToSell(false);
                Comm.postRemoteCard(card);
                
                // Mise a jour des portes monnaie du buyer et du seller
				buyer.setSold(buyer.getSold() - card.getPrice());
                seller.setSold(seller.getSold() + card.getPrice());
                Comm.postRemoteUser(buyer);
                Comm.postRemoteUser(seller);
                
                return card;
			}
			else {
				throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Votre solde est insuffisant");
				
			}
			
		}
		throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Carte non trouvee");
		
		
	}
	
	public CardDTO sell(Integer cardId, Integer buyer_id, Integer price) {
		CardDTO card = Comm.getRemoteCard(cardId);
        if(card != null){
            if(card.getPlayerId().equals(buyer_id)){
                card.setIsToSell(true);
                card.setPrice(price);
                Comm.postRemoteCard(card);
                return card;
            }else{
                throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Vous n'etes pas le propriétaire de cette carte");
            }
        }
        throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Carte non trouvee");
    }
	
	
	
	