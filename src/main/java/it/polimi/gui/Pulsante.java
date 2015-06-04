package it.polimi.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
 * Classe utilizzata per la creazione di un oggetto complesso, composto da un JButton e da
 * altri elementi identificativi come il nome del settore associato al pulsante e le 
 * coordinate del pulsante sullo schermo.
 */
public class Pulsante {
	
	private static int idPulsante;
	private String nomePulsante;
	private final JButton button;
	private int ascissa;
	private int ordinata;
	private static String pulsanteEvidenziato;
	
	/**
	 * Costruttore della classe 'Pulsante'.
	 * @param nome Nome del settore associato al pulsante
	 * @param indicePulsante
	 */
	public Pulsante(String nome, int indicePulsante) {
		nomePulsante = nome;
		button = new JButton(nome);
		setIdPulsante(indicePulsante);
	}

	public static int getIdPulsante() {
		return idPulsante;
	}

	public void setIdPulsante(int indicePulsante) {
		Pulsante.idPulsante = indicePulsante;
	}
	
	/**
	 * Metodo che restituisce il nome del settore associato al pulsante.
	 * @return Nome del settore associato al pulsante.
	 */
	public String getNomePulsante() {
		return nomePulsante;
	}
	
	/**
	 * Metodo utilizzato per assegnare un nome (e quindi un settore) al pulsante.
	 */
	public void setNomePulsante(String nomePulsante) {
		this.nomePulsante = nomePulsante;
	}
	
	/**
	 * Metodo utilizzato per estrarre il JButton da un oggetto della classe 'Pulsante'.
	 * @return JButton associato all'oggetto 'Pulsante'
	 */
	public JButton getButton() {
		return button;
	}
	
	/**
	 * Metodo utilizzato per ricavare la posizione orizzontale del pulsante sullo schermo.
	 * @return Ascissa relativa al pulsante
	 */
	public int getAscissa() {
		return ascissa;
	}
	
	/**
	 * Metodo utilizzato per settare la posizione orizzontale del pulsante sullo schermo.
	 * @param ascissa Intero che rappresenterà l'ascissa relativa al pulsante
	 */
	public void setAscissa(int ascissa) {
		this.ascissa = ascissa;
	}
	
	/**
	 * Metodo utilizzato per ricavare la posizione verticale del pulsante sullo schermo.
	 * @return Ordinata relativa al pulsante
	 */
	public int getOrdinata() {
		return ordinata;
	}
	
	/**
	 * Metodo utilizzato per settare la posizione verticale del pulsante sullo schermo.
	 * @param ordinata Intero che rappresenterà l'ordinata relativa al pulsante
	 */
	public void setOrdinata(int ordinata) {
		this.ordinata = ordinata;
	}
	
	/**
	 * Metodo utilizzato per evidenziare la posizione corrente del giocatore.
	 */
	public void evidenziaPosizione() {
		this.button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				button.setBackground(Color.GREEN);
				//gioco.moveCurrentPlayer(pulsante.getNomePulsante());
			}
		});
	}
	
}
