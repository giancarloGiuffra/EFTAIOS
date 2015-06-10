package it.polimi.gui;

import javax.swing.*;

/**
 * Italian: Classe utilizzata per la creazione di un oggetto complesso, composto da un JButton e da
 * 	altri elementi identificativi come il nome del settore associato al pulsante e le 
 * 	coordinate del pulsante sullo schermo.
 * English: Class used to create a complex object, composed by a JButton and other 
 * 	characteristic elements like the name associated to the button itself and its
 * 	coordinates on the screen.
 */
public class Pulsante {
	
	private String nomePulsante;
	private final JButton button;
	private int ascissa;
	private int ordinata;  
	
	/**
	 * Italian: Costruttore della classe 'Pulsante'.
	 * English: Constructor for the class 'Pulsante'.
	 * @param nome Nome del settore associato al pulsante
	 */
	public Pulsante(String nome) {
		nomePulsante = nome;
		button = new JButton(nome);
	}
	
	/**
	 * Italian: Metodo che restituisce il nome del settore associato al pulsante.
	 * English: Method which returns the name of the sector associated to the button.
	 * @return Nome del settore associato al pulsante.
	 */
	public String getNomePulsante() {
		return nomePulsante;
	}
	
	/**
	 * Italian: Metodo utilizzato per assegnare un nome (e quindi un settore) al pulsante.
	 * English: Method used to assign a name (and so a sector) to the button.
	 */
	public void setNomePulsante(String nomePulsante) {
		this.nomePulsante = nomePulsante;
	}
	
	/**
	 * Italian: Metodo utilizzato per estrarre il JButton da un oggetto della classe 'Pulsante'.
	 * English: Method used to extract the corresponding JButton from a 'Pulsante' object.
	 * @return JButton associato all'oggetto 'Pulsante'
	 */
	public JButton getButton() {
		return button;
	}
	
	/**
	 * Italian: Metodo utilizzato per ricavare la posizione orizzontale del pulsante sullo schermo.
	 * English: Method used to get the horizontal position of a button on the screen.
	 * @return Ascissa relativa al pulsante
	 */
	public int getAscissa() {
		return ascissa;
	}
	
	/**
	 * Italian: Metodo utilizzato per settare la posizione orizzontale del pulsante sullo schermo.
	 * English: Method used to set the horizontal position of a button on the screen.
	 * @param ascissa Intero che rappresenterà l'ascissa relativa al pulsante
	 */
	public void setAscissa(int ascissa) {
		this.ascissa = ascissa;
	}
	
	/**
	 * Italian: Metodo utilizzato per ricavare la posizione verticale del pulsante sullo schermo.
	 * English: Method used to get the vertical position of a button on the screen.
	 * @return Ordinata relativa al pulsante
	 */
	public int getOrdinata() {
		return ordinata;
	}
	
	/**
	 * Italian: Metodo utilizzato per settare la posizione verticale del pulsante sullo schermo.
	 * English: Method used to set the vertical position of a button on the screen.
	 * @param ordinata Intero che rappresenterà l'ordinata relativa al pulsante
	 */
	public void setOrdinata(int ordinata) {
		this.ordinata = ordinata;
	}
	
}
