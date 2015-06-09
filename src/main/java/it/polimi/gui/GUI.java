package it.polimi.gui;

import it.polimi.client.TipoInterface;
import it.polimi.model.sector.Settore;
import it.polimi.model.sector.TipoSettore;
import it.polimi.model.tabellone.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


/** 
 *  Italian: Classe adibita alla generazione dell'interfaccia grafica che l'utente utilizzerà
 *  	per interagire con il gioco.
 *  English: Class whose function is to create the graphical interface that the user will
 *  	adopt to interact with the game. 
 */
public class GUI {
	
	private final static String lettere[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W"};
	private final static String numeri[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14"};
	private final static int altezzaPulsanteSettore = 37;
	private final static int larghezzaPulsanteSettore = 57;
	private final static int numeroColonne = lettere.length;
	private final static int numeroRighe = numeri.length;
	private final static int xIniziale = 15;
	private final static int yIniziale = 20;
	private static int xCorrente = xIniziale;
	private static int yCorrente = yIniziale;
	private static Tabellone galilei = TabelloneFactory.createTabellone("GALILEI");
	private ArrayList<PulsanteSettore> listaPulsantiSettore;
	private ArrayList<AltroPulsante> listaAltriPulsanti;
	private static int indicePulsante = 0;
	private static ListaSettore settoriInaccessibili;
	private static ListaSettore settoriSicuri;
	private static ListaSettore settoriPericolosi;
	private static ListaSettore basi;
	private static ListaSettore scialuppe;
	private final AltroPulsante attacco = new AltroPulsante("Attacco");
	private final AltroPulsante pescaCarta = new AltroPulsante("Pesca una carta");
	private JLabel cartaPescata = new JLabel();
	private final JFrame finestraIniziale = new JFrame("Start");
	private final String startConSocket = new String("Start con Socket");
	private final String startConRMI = new String("Start con RMI");
	private final JLabel legenda = new JLabel("Settori sicuri: colore bianco \tSettori pericolosi: colore grigio\t Basi: colore nero\t Scialuppe: colore azzurro");
	private TipoInterface tipoInterfaccia;
	private String nomeGiocatore;
	private String razzaGiocatore;
	private String posizioneAttuale;
	
	private void creaListaPulsantiSettore() {
		listaPulsantiSettore = new ArrayList<PulsanteSettore>();
		for (int j = 0; j < numeroColonne; j++) {
			for (int i = 0; i < numeroRighe; i++) {
				listaPulsantiSettore.add(new PulsanteSettore(lettere[j] + numeri[i]));
				listaPulsantiSettore.get(indicePulsante).getButton().setFont(new Font("Dialog", Font.BOLD, 11));
				listaPulsantiSettore.get(indicePulsante).setOrdinata(yCorrente);
				listaPulsantiSettore.get(indicePulsante).setAscissa(xCorrente); 
				yCorrente += altezzaPulsanteSettore;
				indicePulsante++;
			}
			// se la colonna ha indice pari, abbassa la posizione dei pulsanti della colonna successiva
			if (j%2 == 0) {  
				yCorrente = yIniziale + altezzaPulsanteSettore/2;
			}
			else {
				yCorrente = yIniziale;
			}
			xCorrente += larghezzaPulsanteSettore;
		}
		indicePulsante = 0;
		xCorrente = xIniziale;
		yCorrente = yIniziale;
	}

	/**
	 * Italian: Metodo utilizzato per la definizione della GUI vera e propria.
	 * English: Method used to actually define the GUI.
	 */
	public void creaGUI() {
		listaAltriPulsanti = new ArrayList<AltroPulsante>();
		PulsanteSettore pulsanteI;
		JFrame frame = new JFrame("Escape from the aliens");
		JLabel topLabel = new JLabel("Giocatore corrente: " + nomeGiocatore + " (" + razzaGiocatore + ")", SwingConstants.CENTER);
		JPanel centralPanel = new JPanel();
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		// usare label.setText() nel metodo che aggiorna la GUI ("ricavaInfoIniziali", non qui)
		JLabel bottomLabel = new JLabel("Posizione attuale: " + posizioneAttuale, SwingConstants.CENTER);
		bottomLabel.setBorder(new EmptyBorder(0, 300, 20, 0));
		frame.setLayout(new BorderLayout());
		topPanel.setLayout(new BorderLayout());
		topPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
		topPanel.add(topLabel, BorderLayout.CENTER);
		FlowLayout flow = new FlowLayout();
		flow.setHgap(20);
		bottomPanel.setLayout(flow);
		bottomPanel.setBorder(new EmptyBorder(0, 30, 20, 200)); 
		attacco.getButton().setEnabled(false);
		pescaCarta.getButton().setEnabled(false);
		listaAltriPulsanti.add(attacco);
		listaAltriPulsanti.add(pescaCarta);
		cartaPescata.setVisible(false);
		bottomPanel.add(attacco.getButton());
		bottomPanel.add(pescaCarta.getButton());
		bottomPanel.add(cartaPescata);
		bottomPanel.add(bottomLabel);
		frame.add(topPanel, BorderLayout.NORTH);
		centralPanel.setLayout(null);
		creaListaPulsantiSettore();
		for (int i = 0; i < listaPulsantiSettore.size(); i++) {
			pulsanteI = listaPulsantiSettore.get(i);
			pulsanteI.getButton().setBounds(pulsanteI.getAscissa(), pulsanteI.getOrdinata(), larghezzaPulsanteSettore, altezzaPulsanteSettore);
			pulsanteI.setGUIProprietaria(this); 	//...
			centralPanel.add(pulsanteI.getButton());
		}
		frame.add(centralPanel, BorderLayout.CENTER);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.getContentPane();
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private static void ricavaSettori() {
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
		PulsanteSettore pulsanteI;
		Settore settoreJ;
		for (int i = 0; i < listaPulsantiSettore.size(); i++) {
			for (int j = 0; j < listaSettori.getLista().size(); j++) {
				pulsanteI = listaPulsantiSettore.get(i);
				settoreJ = listaSettori.getLista().get(j);
				if (pulsanteI.getNomePulsante().equals(settoreJ.getNome())) {
					setColorePulsante(listaSettori.getNomeLista(), pulsanteI.getButton());
				}
			}
		}
	}
	
	/**
	 * Italian: Metodo che colora i pulsanti corrispondenti ai settori, in base alla loro tipologia.
	 * English: Method used to color the buttons corresponding to the sectors, according to their type.
	 */
	public void coloraGUI() {
		ricavaSettori();
		coloraSettoriPerTipo(settoriInaccessibili);
		coloraSettoriPerTipo(settoriSicuri);
		coloraSettoriPerTipo(settoriPericolosi);
		coloraSettoriPerTipo(basi);
		coloraSettoriPerTipo(scialuppe);
	}
	
	/**
	 * Italian: Metodo che genera una finestra dotata di pulsanti. Cliccando su un pulsante, 
	 * 		l'utente si 'registra' come partecipante alla partita utilizzando una delle
	 * 		tecnologie fornite per la giocabilità online.
	 * English: Method which creates a frame endowed with buttons. Clicking on a button
	 * 		the user will join to the list of players, using one of the available technologies
	 * 		to play online.
	 */
	public TipoInterface sceltaTecnologiaDiComunicazione() {
		ArrayList<AltroPulsante> pulsantiStart = new ArrayList<AltroPulsante>();
		AltroPulsante inizioPartitaConSocket = new AltroPulsante(startConSocket);
		AltroPulsante inizioPartitaConRMI = new AltroPulsante(startConRMI);
		pulsantiStart.add(inizioPartitaConSocket);
		pulsantiStart.add(inizioPartitaConRMI);
		for (int i = 0; i < pulsantiStart.size(); i++) {
			final AltroPulsante modalità = pulsantiStart.get(i);
			modalità.getButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					comunicaTecnologiaDiComunicazione(modalità.getNomePulsante());
				}
			});
		}
		finestraIniziale.setLayout(new FlowLayout());
		finestraIniziale.add(inizioPartitaConSocket.getButton());
		finestraIniziale.add(inizioPartitaConRMI.getButton());
		finestraIniziale.getContentPane();
		finestraIniziale.pack();
		finestraIniziale.setVisible(true);
		finestraIniziale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		do {  
			// SwingTimer ??
			// Label in alto per avvertire l'utente del timer
		}
		while (tipoInterfaccia == null); // while (tipoInterfaccia == null && timer < valoreDiSoglia)
		return tipoInterfaccia; // è nullo finché non viene eseguito 'comunicaTecnologiaDiComunicazione'
	}
	
	private void comunicaTecnologiaDiComunicazione(String nomePulsante) {
		finestraIniziale.setVisible(false);
		if (nomePulsante.equals(startConSocket)) {
			tipoInterfaccia = TipoInterface.SOCKET_GUI;
		}
		else if (nomePulsante.equals(startConRMI)) {
			tipoInterfaccia = TipoInterface.RMI_GUI;
		}
		visualizzaTabellone();
	}
	
	public void visualizzaTabellone() {
		creaGUI();
		coloraGUI();
	}
	
	/**
	 * Italian: Metodo utilizzato per attivare il pulsante "attacco" dopo che un giocatore di 
	 * 		razza aliena ha effettuato il movimento.
	 * English: Method used to activate the button "attacco" after the movement of
	 * 		 an alien player.
	 */
	public void attivaPulsanteAttacco() {
		attacco.getButton().setEnabled(true);
	}
	
	/**
	 * Italian: Metodo utilizzato per:
	 * 	1) 	attivare il pulsante "pescaCarta" se uno spostamento porta il giocatore in un 
	 * 		settore pericoloso;
	 * 	2)	visualizzare l'etichetta che informa il giocatore riguardo la tipologia 
	 * 		della carta pescata;
	 * English: Method used to:
	 * 	1)	activate the button "pescaCarta" if a player ends up in a dangerous sector 
	 * 		after a movement;
	 * 	2)	inform the player about the kind of card received;
	 */	
	public void attivaPulsantePescaCarta() {
		pescaCarta.getButton().setEnabled(true);
		tipoCartaPescata();
		cartaPescata.setVisible(true);
	}
	
	private void tipoCartaPescata() {
		pescaCarta.getButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cartaPescata = new JLabel("Carta pescata: ");
				
			}
		});
	} 
	
	/**
	 * Italian: Metodo che restituisce la lista contenente tutti i pulsanti corrispondenti a settori.
	 * English: Method which returns the list containing all the buttons that correspond to sectors.
	 * @return listaPulsantiSettore
	 */
	public ArrayList<PulsanteSettore> getListaPulsantiSettore() {
		return this.listaPulsantiSettore;
	}
	
	/**
	 * Italian: Metodo che restituisce la lista contenente tutti i pulsanti della schermata di gioco non 
	 * 	corrispondenti a settori.
	 * English: Method which returns the list containing all the buttons in the game screen that
	 * 	do not represent sectors.
	 * @return listaAltriPulsanti
	 */
	public ArrayList<AltroPulsante> getListaAltriPulsanti() {
		return this.listaAltriPulsanti;
	}
	
	/**
	 * Italian: metodo che ricava le informazioni iniziali per il giocatore corrente, quali
	 * 	il nome, la razza e la posizione iniziale del giocatore.
	 * English: method which extracts the starting informations about the current player, such
	 * 	as its name, its race and its initial position.
	 * @param informazioniIniziali ArrayList contenente le informazioni iniziali
	 */
	public void ricavaInformazioniIniziali(ArrayList<String> informazioniIniziali) {
		nomeGiocatore = informazioniIniziali.get(0);
		posizioneAttuale = informazioniIniziali.get(1);
		razzaGiocatore = informazioniIniziali.get(2);
		evidenziaPosizioneIniziale(posizioneAttuale);
	}
	
	private void evidenziaPosizioneIniziale(String nomeSettore) {
		for (int i = 0; i < listaPulsantiSettore.size(); i++) {
			Pulsante pulsante = listaPulsantiSettore.get(i);
			if (pulsante.getNomePulsante().equals(nomeSettore)) {
				pulsante.getButton().setBackground(Color.GREEN);
			}
		}
		visualizzaTabellone();
	}
	
	/**
	 * Italian: metodo che informerà il giocatore riguardo eventi che possono verificarsi 
	 * 	durante la partita.
	 * English: method used to inform the player about events that can possibly happen during 
	 * 	the game.
	 * @param tipoMessaggio messaggio da comunicare
	 */
	public void comunicaMessaggio(String tipoMessaggio) {
		JFrame frame = new JFrame();
		JLabel messaggio = new JLabel(tipoMessaggio, SwingConstants.CENTER);
		frame.add(messaggio);
		frame.getContentPane();
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Italian: metodo che può abilitare i pulsanti 'attacco' e/o 'pescaCarta', al verificarsi 
	 * 	di determinate condizioni di gioco.
	 * English: method whose function is to enable 'attacco' and/or 'pescaCarta' buttons, 
	 * 	assuming that some game conditions are satisfied.
	 */
	public void abilitaAltriPulsanti() {
		if (razzaGiocatore == "ALIEN") {
			attivaPulsanteAttacco();
		}
		ArrayList<Settore> pericolosi = settoriPericolosi.getLista();
		for (Settore p : pericolosi) {
			if (posizioneAttuale.equals(p.getNome())) {
				attivaPulsantePescaCarta();
			}
		}
	}
	
}
