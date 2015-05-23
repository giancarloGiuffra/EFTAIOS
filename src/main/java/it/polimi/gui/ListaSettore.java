package it.polimi.gui;

import it.polimi.model.sector.Settore;
import java.util.ArrayList;

public class ListaSettore {
	
	private ArrayList<Settore> lista;
	private String nomeLista;
	
	public ListaSettore(ArrayList<Settore> lista, String nomeLista) {
		this.lista = lista;
		this.nomeLista = nomeLista;
	}
	
	public String getNomeLista() {
		return this.nomeLista;
	}
	
	public ArrayList<Settore> getLista() {
		return this.lista;
	}

}
