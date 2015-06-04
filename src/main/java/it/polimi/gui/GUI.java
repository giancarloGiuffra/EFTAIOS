package it.polimi.gui;

import it.polimi.gioco.Partita;
import it.polimi.model.sector.Settore;
import it.polimi.model.sector.TipoSettore;
import it.polimi.model.tabellone.*;
import it.polimi.model.gioco.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

// per il posizionamento pulsanti: https://docs.oracle.com/javase/tutorial/uiswing/layout/none.html

/** 
 *  Classe adibita alla generazione dell'interfaccia grafica che l'utente utilizzerà
 *  per interagire con il gioco.
 */
public class GUI {
	
	private final static String lettere[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W"};
	private final static String numeri[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14"};
	private final static int numeroMassimoPulsanti = lettere.length * numeri.length;
	private final static int altezzaPulsante = 40;
	private final static int larghezzaPulsante = 58;
	private final static int numeroColonne = lettere.length;
	private final static int numeroRighe = numeri.length;
	private final static int xIniziale = 15;
	private final static int yIniziale = 20;
	private static int xCorrente = xIniziale;
	private static int yCorrente = yIniziale;
	private static Tabellone galilei = TabelloneFactory.createTabellone("GALILEI");
	private static String numeroGiocatori;
	private static Partita nuovaPartita;
	private ArrayList<Pulsante> listaPulsanti;
	private static int indicePulsante = 0;
	private static ListaSettore settoriInaccessibili;
	private static ListaSettore settoriSicuri;
	private static ListaSettore settoriPericolosi;
	private static ListaSettore basi;
	private static ListaSettore scialuppe;
	private boolean isSettorePericoloso = false;
	private boolean giocaAlieno = false;
	private Gioco gioco;
	
	private void creaListaPulsanti() {
		listaPulsanti = new ArrayList<Pulsante>();
		for (int j = 0; j < numeroColonne; j++) {
			for (int i = 0; i < numeroRighe; i++) {
				listaPulsanti.add(new Pulsante(lettere[j] + numeri[i], indicePulsante));
				listaPulsanti.get(indicePulsante).setOrdinata(yCorrente);
				listaPulsanti.get(indicePulsante).setAscissa(xCorrente); 
				listaPulsanti.get(indicePulsante).evidenziaPosizione(); // eliminare
				yCorrente += altezzaPulsante;
				indicePulsante++;
			}
			// se la colonna ha indice pari, abbassa la posizione dei pulsanti della colonna successiva
			if (j%2 == 0) {  
				yCorrente = yIniziale + altezzaPulsante/2;
			}
			else {
				yCorrente = yIniziale;
			}
			xCorrente += larghezzaPulsante;
		}
		
	}

	/**
	 * Metodo utilizzato per la definizione della GUI vera e propria.
	 */
	public void creaGUI() {
		JFrame frame = new JFrame("Escape from the aliens");
		JLabel topLabel = new JLabel("Giocatore corrente: ", SwingConstants.CENTER);
		JPanel centralPanel = new JPanel();
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JLabel bottomLabel = new JLabel("Posizione attuale: ", SwingConstants.CENTER);
		frame.setLayout(new BorderLayout());
		topPanel.setLayout(new BorderLayout());
		topPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
		topPanel.add(topLabel, BorderLayout.CENTER);
		//bottomPanel.setLayout(new BorderLayout());
		FlowLayout flow = new FlowLayout();
		flow.setHgap(20);
		bottomPanel.setLayout(flow);
		bottomPanel.setBorder(new EmptyBorder(0, 30, 20, 200));
		creaPulsanteAttacco(bottomPanel);
		creaPulsantePescaCarta(bottomPanel);
		bottomPanel.add(bottomLabel);
		frame.add(topPanel, BorderLayout.NORTH);
		centralPanel.setLayout(null);
		creaListaPulsanti();
		for (int i = 0; i < listaPulsanti.size(); i++) {
			listaPulsanti.get(i).getButton().setBounds(listaPulsanti.get(i).getAscissa(), listaPulsanti.get(i).getOrdinata(), larghezzaPulsante, altezzaPulsante);
			centralPanel.add(listaPulsanti.get(i).getButton());
		}
		frame.add(centralPanel, BorderLayout.CENTER);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.getContentPane();
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	/**
	 * Metodo che raccoglie i settori in appositi ArrayList, in base alla loro tipologia.
	 */
	public static void ricavaSettori() {
		settoriInaccessibili = new ListaSettore((ArrayList<Settore>) galilei.getSettoriDiTipo(TipoSettore.INACCESSIBILE), "Inaccessibili");
		settoriSicuri = new ListaSettore((ArrayList<Settore>) galilei.getSettoriDiTipo(TipoSettore.SICURO), "Sicuri");
		settoriPericolosi = new ListaSettore((ArrayList<Settore>) galilei.getSettoriDiTipo(TipoSettore.PERICOLOSO), "Pericolosi");
		basi = new ListaSettore(new ArrayList<Settore>(), "Basi");
		basi.getLista().addAll((ArrayList<Settore>) galilei.getSettoriDiTipo(TipoSettore.BASE_ALIEN));
		basi.getLista().addAll((ArrayList<Settore>) galilei.getSettoriDiTipo(TipoSettore.BASE_HUMAN));
		scialuppe = new ListaSettore((ArrayList<Settore>) galilei.getSettoriDiTipo(TipoSettore.SCIALUPPA), "Scialuppe"); 
		
	}
	
	private void setColorePulsante(String nomeLista, JButton pulsante) {
		switch(nomeLista) {
			case "Inaccessibili":
				pulsante.setVisible(false);
				break;
			case "Sicuri":
				pulsante.setBackground(Color.WHITE);
				break;
			case "Pericolosi":
				pulsante.setBackground(Color.LIGHT_GRAY);
				break;
			case "Basi":
				pulsante.setBackground(Color.BLACK);
				break;
			case "Scialuppe":
				pulsante.setBackground(Color.CYAN);
				break;
		}
	}
	
	private void coloraSettoriPerTipo(ListaSettore listaSettori) {
		for (int i = 0; i < listaPulsanti.size(); i++) {
			for (int j = 0; j < listaSettori.getLista().size(); j++) {
				if (listaPulsanti.get(i).getNomePulsante().equals(listaSettori.getLista().get(j).getNome())) {
					setColorePulsante(listaSettori.getNomeLista(), listaPulsanti.get(i).getButton());
				}
			}
		}
	}
	
	/**
	 * Metodo che colora i pulsanti corrispondenti ai settori, in base alla loro tipologia.
	 */
	public void coloraGUI() {
		ricavaSettori();
		coloraSettoriPerTipo(settoriInaccessibili);
		coloraSettoriPerTipo(settoriSicuri);
		coloraSettoriPerTipo(settoriPericolosi);
		coloraSettoriPerTipo(basi);
		coloraSettoriPerTipo(scialuppe);
	}

	public void setNumeroGiocatori() { // necessario?
		JFrame finestra = new JFrame("Numero dei giocatori");
		JLabel etichetta = new JLabel("Inserire il numero dei giocatori: ");
		final JTextField inserimentoNumero = new JTextField(" " , 5);
		inserimentoNumero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numeroGiocatori = inserimentoNumero.getText();
				creaGUI();
				coloraGUI();
				//nuovaPartita = new Partita(GUI.getNumeroGiocatori());  //qui?
			}
		});
		finestra.setLayout(new FlowLayout());
		finestra.add(etichetta);
		finestra.add(inserimentoNumero);
		finestra.setSize(300, 80);
		finestra.getContentPane();
		finestra.setVisible(true);
		finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	/**
	 * Metodo che genera una finestra dotata di pulsante. Cliccando sul pulsante, 
	 * l'utente si 'registra' come partecipante alla partita.
	 */
	public void partecipazionePartita() {
		final JFrame finestra = new JFrame();
		JButton inizioPartita = new JButton("Start");
		inizioPartita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creaGUI();
				coloraGUI();
				finestra.setVisible(false);
			}
		});
		finestra.setLayout(new FlowLayout());
		finestra.add(inizioPartita);
		finestra.getContentPane();
		finestra.pack();
		finestra.setVisible(true);
		finestra.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static int getNumeroGiocatori() {  // necessario?
		return Integer.parseInt(numeroGiocatori);
	}
	
	public static Partita creaNuovaPartita() {
		return nuovaPartita;
	}
	
	private void creaPulsantePescaCarta(JPanel pannello) {
		Pulsante pescaCarta = new Pulsante("Pesca una carta", indicePulsante+1);
		JLabel cartaPescata = new JLabel("Carta pescata: ");
		JPanel contenitore = new JPanel();
		contenitore.setBorder(new EmptyBorder(0, 0, 0, 100));
		contenitore.add(cartaPescata);
		if (isSettorePericoloso == false) {
			cartaPescata.setVisible(false);
		}
		pannello.add(pescaCarta.getButton());
		pannello.add(contenitore);
		// if (settore non pericoloso) pulsante disabilitato
		// + label con carta pescata
	}
	
	private void creaPulsanteAttacco(JPanel pannello) {
		Pulsante attacco = new Pulsante("Attacco", indicePulsante+1);
		if (giocaAlieno == false) {
			attacco.getButton().setEnabled(false);
		}
		pannello.add(attacco.getButton());
	}
	
}
