package it.polimi.gui;

import it.polimi.model.sector.Settore;
import java.util.ArrayList;

/**
 * Italian: Classe utilizzata per assegnare ad un ArrayList di settori un nome che lo identifichi.
 * English: Class used to assign an identificative name to an ArrayList of sectors. 
 */
public class ListaSettore {
	
	private ArrayList<Settore> lista;
	private String nomeLista;
	
	/**
	 * Italian: Costruttore della classe 'ListaSettore'.
	 * English: Constructor of the class 'ListaSettore'.
	 * @param lista ArrayList di settori per un determinato tipo
	 * @param nomeLista Nome da assegnare all'ArrayList
	 */
	public ListaSettore(ArrayList<Settore> lista, String nomeLista) {
		this.lista = lista;
		this.nomeLista = nomeLista;
	}
	
	/**
	 * Italian: Metodo che restituisce il nome assegnato all'ArrayList.
	 * English: Method used to get the name of the ArrayList.
	 * @return Nome dell'ArrayList di settori
	 */
	public String getNomeLista() {
		return this.nomeLista;
	}
	
	/**
	 * Italian: Metodo che restituisce l'ArrayList di settori desiderato.
	 * English: Method used to get the desired ArrayList of sectors.
	 * @return ArrayList di settori
	 */
	public ArrayList<Settore> getLista() {
		return this.lista;
	}

}
