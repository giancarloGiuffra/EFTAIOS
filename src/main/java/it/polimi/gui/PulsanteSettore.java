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
				/*
				// Metodo2
				GUI guiAssociata = getGUIProprietaria();
				View viewAssociata = guiAssociata.returnView();
				viewAssociata.comunicaSpostamento(getNomePulsante());
				guiAssociata.coloraGUI();
				getButton().setBackground(colorePulsanteEvidenziato);
				//evidenziaPulsante();
				*/
				// Metodo3
				GUI guiAssociata = getGUIProprietaria();
				ModelView modelViewAssociato = guiAssociata.returnModelView();
				modelViewAssociato.moveCurrentPlayer(getNomePulsante());
				System.out.println(modelViewAssociato.currentPlayerName() + "\t" + modelViewAssociato.currentPlayerPosition());
				guiAssociata.coloraGUI();
				getButton().setBackground(colorePulsanteEvidenziato);
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
	
	/*
	// Metodo1 (barbaro): funziona, ma si può fare meglio ricavando il pulsante da evidenziare dallo spostamento
	public void evidenziaPulsante() {
		JButton pulsanteCorrente = this.getButton();
		this.colorePrecedente = pulsanteCorrente.getBackground();
		pulsanteCorrente.setBackground(colorePulsanteEvidenziato);
		resetColorePulsante();
	} 
	
	private void resetColorePulsante() {
		ArrayList<PulsanteSettore> listaPulsanti = this.getGUIProprietaria().getListaPulsantiSettore();
		for (int i = 0; i < listaPulsanti.size(); i++) {
			JButton pulsante = listaPulsanti.get(i).getButton();
			if (pulsante.getModel().isArmed() == false && pulsante.getBackground() == colorePulsanteEvidenziato) {
				pulsante.setBackground(colorePrecedente);
			}
		}
	}	*/
	
	/*private ArrayList<PulsanteSettore> getListaSettoriNonAdiacenti() {
		GUI guiAssociata = getGUIProprietaria();
		ModelView modelViewAssociato = guiAssociata.returnModelView();
		ArrayList<String> listaSettoriAdiacenti = (ArrayList<String>) modelViewAssociato.calcolaSettoriValidiForCurrentPlayer();
		ArrayList<PulsanteSettore> listaSettori = guiAssociata.getListaPulsantiSettore();
		ArrayList<PulsanteSettore> listaSettoriNonAdiacenti = new ArrayList<PulsanteSettore>();
		boolean trovato = false;
		for (int i = 0; i < listaSettori.size(); i++) {
			for (int j = 0; j < listaSettoriAdiacenti.size(); j++) {
				if (listaSettoriAdiacenti.get(j) == listaSettori.get(i).getNomePulsante()) {
					trovato = true;
					j = listaSettoriAdiacenti.size()-1;
				}
				else if (j == listaSettoriAdiacenti.size()-1 && trovato == false) {
					listaSettoriNonAdiacenti.add(listaSettori.get(i));
				}
			}
			trovato = false;
		}
		return listaSettoriNonAdiacenti;
	}*/
	
	private void disabilitaSettoriNonAdiacenti() {
		GUI guiAssociata2 = getGUIProprietaria();
		ModelView modelViewAssociato2 = guiAssociata2.returnModelView();
		ArrayList<String> listaSettoriAdiacenti = (ArrayList<String>) modelViewAssociato2.calcolaSettoriValidiForCurrentPlayer();
		ArrayList<PulsanteSettore> listaSettori = guiAssociata2.getListaPulsantiSettore();
		for (int i = 0; i < listaSettoriAdiacenti.size(); i++) {
			for (int j = 0; j < listaSettori.size(); j++) {
				if (listaSettoriAdiacenti.get(i) == listaSettori.get(j).getNomePulsante()) {
					listaSettori.get(j).getButton().setEnabled(true);
				}
				else {
					listaSettori.get(j).getButton().setEnabled(false);
				}
			}
			//listaSettori.remove(j);
		}
	}

}
