package it.polimi.gui;

import javax.swing.*;

public class Pulsante {
	
	private static int idPulsante;
	private String nomePulsante;
	private final JButton button;
	private int ascissa;
	private int ordinata;

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

	public String getNomePulsante() {
		return nomePulsante;
	}

	public void setNomePulsante(String nomePulsante) {
		this.nomePulsante = nomePulsante;
	}

	public JButton getButton() {
		return button;
	}

	public int getAscissa() {
		return ascissa;
	}

	public void setAscissa(int ascissa) {
		this.ascissa = ascissa;
	}
	
	public int getOrdinata() {
		return ordinata;
	}

	public void setOrdinata(int ordinata) {
		this.ordinata = ordinata;
	}
	
}
