package it.polimi.gui;

import javax.swing.*;

public class Pulsante {
	
	private static String idPulsante;
	private final String nomePulsante;
	private final JButton button;
	
	public Pulsante(String nome, String id) {
		nomePulsante = nome;
		button = new JButton(nome);
		setIdPulsante(id);
	}

	public static String getIdPulsante() {
		return idPulsante;
	}

	public static void setIdPulsante(String id) {
		Pulsante.idPulsante = id;
	}

	public String getNomePulsante() {
		return nomePulsante;
	}

	public JButton getButton() {
		return button;
	}
	
}
