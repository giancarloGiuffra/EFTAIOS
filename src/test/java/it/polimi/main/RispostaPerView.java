package it.polimi.main;

import it.polimi.model.Model;
import it.polimi.view.View;

public class RispostaPerView {
	
	private View view;
	private Model model;
	
	/**
	 * Costruttore
	 * @param view
	 */
	RispostaPerView(View view){
		this.view = view;
	}
	
	/**
	 * setter per il model
	 * @param model
	 */
	public void setModel(Model model){
		this.model = model;
	}
	
	/**
	 * restituisce risposte per la view
	 * @param string
	 * @return
	 */
	public Risposta risposta(String string){
		if("mossa_aleatoria".equals(string))
			return new RispostaMossa(view, string, model);
		return new Risposta(view, string);
	}
		
}
