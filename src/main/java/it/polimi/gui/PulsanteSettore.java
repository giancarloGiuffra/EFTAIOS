package it.polimi.gui;

import it.polimi.client.Comando;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class PulsanteSettore extends Pulsante {
	
	private JButton pulsantePrecedentementeEvidenziato;

	public PulsanteSettore(String nome) {
		super(nome);
	}
	
	private void Movimento() {
		this.getButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setComando(Comando.MUOVE);
				evidenziaPulsante();
			}
		});
	}

	@Override
	public void azionePulsante() {
		Movimento();
		//evidenziaPulsante();
		// + trova settori adiacenti
	}
	
	public void evidenziaPulsante() {
		/*GUI gui = this.getGUIProprietaria();  
		gui.creaGUI();
		//gui.coloraGUI(); */
		// listaSettore:attributo PulsanteSettore, determinato al momento della creazione
		// ricava la listaSettore del pulsante->switch->coloraSettoriPerTipo (riporta il 
		// pulsante alla colorazione precedente)
		this.getButton().setBackground(Color.GREEN);
	} 

}
