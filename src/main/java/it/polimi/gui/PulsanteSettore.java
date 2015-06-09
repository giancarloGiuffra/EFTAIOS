// classe da modificare
package it.polimi.gui;

import it.polimi.client.Comando;
import it.polimi.model.ModelView;
import it.polimi.view.View;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;

public class PulsanteSettore extends Pulsante {
	
	private Color colorePrecedente;
	private Color colorePulsanteEvidenziato = Color.GREEN;

	public PulsanteSettore(String nome) {
		super(nome);
	}
	
	private void movimento() {
		this.getButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
	}

	@Override
	public void azionePulsante() {/*
		ArrayList<PulsanteSettore> listaSettoriNonAdiacenti = getListaSettoriNonAdiacenti();
		for (int i = 0; i < listaSettoriNonAdiacenti.size(); i++) {
			listaSettoriNonAdiacenti.get(i).getButton().setEnabled(false);
		} */
		//disabilitaSettoriNonAdiacenti();
		movimento();
		//evidenziaPulsante();
		// + trova settori adiacenti
	}
	
	
}
