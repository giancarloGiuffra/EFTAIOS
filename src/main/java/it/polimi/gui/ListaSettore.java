package it.polimi.gui;

import it.polimi.model.sector.Settore;
import java.util.ArrayList;

/**
 * Classe utilizzata per assegnare ad un ArrayList di settori un nome che lo identifichi. 
 */
public class ListaSettore {
	
	private ArrayList<Settore> lista;
	private String nomeLista;
	
	/**
	 * Costruttore della classe 'ListaSettore'.
	 * @param lista ArrayList di settori per un determinato tipo
	 * @param nomeLista Nome da assegnare all'ArrayList
	 */
	public ListaSettore(ArrayList<Settore> lista, String nomeLista) {
		this.lista = lista;
		this.nomeLista = nomeLista;
	}
	
	/**
	 * Metodo che restituisce il nome assegnato all'ArrayList.
	 * @return Nome dell'ArrayList di settori
	 */
	public String getNomeLista() {
		return this.nomeLista;
	}
	
	/**
	 * Metodo che restituisce l'ArrayList di settori desiderato.
	 * @return ArrayList di settori
	 */
	public ArrayList<Settore> getLista() {
		return this.lista;
	}

}
