package it.polimi.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import it.polimi.client.Comando;
import it.polimi.model.carta.TipoCartaSettore;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class AltroPulsante extends Pulsante {

	public AltroPulsante(String nome) {
		super(nome);
	}

	@Override
	public void azionePulsante() {
		this.getButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				possibiliAzioniPulsante(getNomePulsante());
			}
		});
	}
	
	private void possibiliAzioniPulsante(String nomePulsante) {
		switch(nomePulsante) {
		case "attacco":   // meglio delle costanti?
			setComando(Comando.RISULTATO_ATTACCO);
			break;
		case "pescaCarta":
			setComando(Comando.PESCA_CARTA);
			//cartaPescata(...);
			break;
		}
	}
	
	public void cartaPescata(TipoCartaSettore tipoCarta) {
		JFrame frame = new JFrame("Carta pescata");
		JLabel messaggio = new JLabel("Carta pescata: ");
		frame.add(messaggio);
		frame.getContentPane();
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
