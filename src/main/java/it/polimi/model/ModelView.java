package it.polimi.model;

import it.polimi.common.observer.BaseObservable;
import it.polimi.common.observer.BaseObserver;
import it.polimi.model.carta.Carta;
import it.polimi.model.player.AzioneGiocatore;
import it.polimi.model.player.Player;

import java.util.List;

public class ModelView extends BaseObservable {

	private Model model;

	/**
	 * Constructor
	 * @param model
	 */
	public ModelView(Model model){
		this.model = new Model(model);
	}
	
	public void addObserver(BaseObserver obs) {
		model.addObserver(obs);
	}

	public void deleteObserver(BaseObserver obs) {
		model.deleteObserver(obs);
	}

	public String currentPlayerName() {
		return model.currentPlayerName();
	}

	public String currentPlayerPosition() {
		return model.currentPlayerPosition();
	}

	public int currentTurnNumber() {
		return model.currentTurnNumber();
	}

	public List<Player> players() {
		return model.players();
	}

	public void moveCurrentPlayer(String nomeSettore) {
		model.moveCurrentPlayer(nomeSettore);
	}

	public List<AzioneGiocatore> getValidActionsForCurrentPlayer() {
		return model.getValidActionsForCurrentPlayer();
	}

	public List<AzioneGiocatore> getValidActionsForPlayer(Player player) {
		return model.getValidActionsForPlayer(player);
	}

	public void finishTurn() {
		model.finishTurn();
	}

	public void currentPlayerPescaCartaSettore() {
		model.currentPlayerPescaCartaSettore();
	}

	public void currentPlayerUsaCarta(Carta carta) {
		model.currentPlayerUsaCarta(carta);
	}

	public String toString() {
		return model.toString();
	}

	public void currentPlayerAnnunciaSettore(String nomeSettore) {
		model.currentPlayerAnnunciaSettore(nomeSettore);
	}

	public void currentPlayerAttacca() {
		model.currentPlayerAttacca();
	}

	/**
	 * getter per model
	 * @return
	 */
	public Model model() {
		return this.model;
	}
	
}
