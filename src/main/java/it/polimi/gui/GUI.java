package it.polimi.gui;

import it.polimi.client.NetworkInterfaceForClient;
import it.polimi.model.sector.Settore;
import it.polimi.model.sector.TipoSettore;
import it.polimi.model.tabellone.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
	private static Dimension dimensioneSchermo = Toolkit.getDefaultToolkit().getScreenSize(); 
	private static int altezzaSchermo = dimensioneSchermo.height;  
	private static int larghezzaSchermo = dimensioneSchermo.width; 
	private final static int numeroColonne = lettere.length;
	private final static int numeroRighe = numeri.length;
	private final static int altezzaPulsanteSettore = (altezzaSchermo - altezzaSchermo/3)/numeroRighe;
	private final static int larghezzaPulsanteSettore = (larghezzaSchermo - larghezzaSchermo/45)/numeroColonne;
	private final static int xIniziale = 15;
	private final static int yIniziale = 20;
	private static int xCorrente = xIniziale;
	private static int yCorrente = yIniziale;
	private static Tabellone galilei = TabelloneFactory.createTabellone("GALILEI");
	private ArrayList<Pulsante> listaPulsantiSettore;
	private ArrayList<Pulsante> listaAltriPulsanti;
	private static int indicePulsante = 0;
	private static ListaSettore settoriInaccessibili;
	private static ListaSettore settoriSicuri;
	private static ListaSettore settoriPericolosi;
	private static ListaSettore basi;
	private static ListaSettore scialuppe;
	private final Pulsante attacco = new Pulsante("Attacco");
	private final Pulsante pescaCarta = new Pulsante("Pesca una carta");
	private final Pulsante nessunAttacco = new Pulsante("Nessun attacco");
	private JLabel cartaPescata = new JLabel();
	private String nomeGiocatore;
	private String razzaGiocatore;
	private String posizioneAttuale;
	private JLabel topLabel = new JLabel("Giocatore corrente: " + nomeGiocatore + " (" + razzaGiocatore + ")", SwingConstants.CENTER);
	private JLabel bottomLabel = new JLabel("Posizione attuale: " + posizioneAttuale, SwingConstants.CENTER);
	private JPanel centralPanel = new JPanel();
	private boolean inputInserito = false;
	private String inputDaInviare;
	private NetworkInterfaceForClient interfaccia;
	private Timer timer;
	private int countdownPerMossa = 30;   // il giocatore ha a disposizione 30 secondi per effettuare la sua mossa
	private int tempoAggiornamentoCountdown = 1;  
	private JLabel mostraCountdown = new JLabel("" + countdownPerMossa);
	
	/**
	 * Costruttore
	 * @param interfaccia
	 */
	public GUI(NetworkInterfaceForClient interfaccia){
	    this.interfaccia = interfaccia;
	}
	
    private void creaListaPulsantiSettore() {
		listaPulsantiSettore = new ArrayList<Pulsante>();
		for (int j = 0; j < numeroColonne; j++) {
			for (int i = 0; i < numeroRighe; i++) {
				listaPulsantiSettore.add(new Pulsante(lettere[j] + numeri[i]));
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
		listaAltriPulsanti = new ArrayList<Pulsante>();
		JLabel spiegazioneColori = new JLabel("Settori sicuri: colore bianco   |   Settori pericolosi: colore grigio   |   Basi: colore nero   |   Scialuppe: colore azzurro");
		JFrame frame = new JFrame("Escape from the aliens");
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JPanel contenitoreLegenda = new JPanel(); 
		JPanel contenitoreAltriElementi = new JPanel(); 
		frame.setLayout(new BorderLayout());
		topPanel.setLayout(new BorderLayout());
		topPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
		topPanel.add(topLabel, BorderLayout.CENTER);
		frame.add(topPanel, BorderLayout.NORTH);
		centralPanel.setLayout(null);
		creaListaPulsantiSettore();
		setAspettoPulsante();
		assegnaActionListenerSpostamento();	
		contenitoreAltriElementi.add(nessunAttacco.getButton());
		contenitoreAltriElementi.add(attacco.getButton());  
		contenitoreAltriElementi.add(pescaCarta.getButton()); 
		//contenitoreAltriPulsanti.add(cartaPescata); 
		contenitoreAltriElementi.add(bottomLabel); 
		FlowLayout flow = new FlowLayout();
		flow.setHgap(20);
		contenitoreAltriElementi.setLayout(flow); 
		contenitoreLegenda.add(spiegazioneColori); 
		bottomPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
		mostraCountdown.setBorder(new EmptyBorder(0, 0, 0, larghezzaSchermo/13));
		BorderLayout border = new BorderLayout(); 
		border.setVgap(10); 
		bottomPanel.setLayout(border); 
		bottomPanel.add(contenitoreAltriElementi, BorderLayout.CENTER); 
		bottomPanel.add(contenitoreLegenda, BorderLayout.SOUTH);
		bottomPanel.add(mostraCountdown, BorderLayout.EAST);
		attacco.getButton().setEnabled(false);
		pescaCarta.getButton().setEnabled(false);
		nessunAttacco.getButton().setEnabled(false);
		listaAltriPulsanti.add(nessunAttacco);
		listaAltriPulsanti.add(attacco);
		listaAltriPulsanti.add(pescaCarta);
		//cartaPescata.setVisible(false);
		bottomLabel.setBorder(new EmptyBorder(0, larghezzaSchermo/10, 0, larghezzaSchermo/5));
		mostraCountdown.setFont(new Font("Dialog", Font.BOLD, 18));
		mostraCountdown.setVisible(false);
		spiegazioneColori.setBorder(new EmptyBorder(0, larghezzaSchermo/12, 0, 0)); 
		frame.add(centralPanel, BorderLayout.CENTER);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.getContentPane();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {	// risolvere: se è il secondo giocatore ad uscire, si ha errore dal server
				interfaccia.close();  
			}
		});
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
		Pulsante pulsanteI;
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
	private void attivaPulsanteAttacco() {
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
	public ArrayList<Pulsante> getListaPulsantiSettore() {
		return this.listaPulsantiSettore;
	}
	
	/**
	 * Italian: Metodo che restituisce la lista contenente tutti i pulsanti della schermata di gioco non 
	 * 	corrispondenti a settori.
	 * English: Method which returns the list containing all the buttons in the game screen that
	 * 	do not represent sectors.
	 * @return listaAltriPulsanti
	 */
	public ArrayList<Pulsante> getListaAltriPulsanti() {
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
		topLabel.setText("Giocatore corrente: " + nomeGiocatore + " (" + razzaGiocatore + ")");
		bottomLabel.setText("Posizione attuale: " + posizioneAttuale);
	}
	
	private void evidenziaPosizioneIniziale(String nomeSettore) {
		for (int i = 0; i < listaPulsantiSettore.size(); i++) {
			Pulsante pulsante = listaPulsantiSettore.get(i);
			if (pulsante.getNomePulsante().equals(nomeSettore)) {
				pulsante.getButton().setBackground(Color.GREEN);
			}
		}
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
		messaggio.setBorder(new EmptyBorder(10, 20, 10, 20));
		frame.add(messaggio);
		frame.getContentPane();
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	/**
	 * Italian: metodo che può abilitare i pulsanti 'attacco' e/o 'pescaCarta', al verificarsi 
	 * 	di determinate condizioni di gioco.
	 * English: method whose function is to enable 'attacco' and/or 'pescaCarta' buttons, 
	 * 	assuming that some game conditions are satisfied.
	 */
	public void abilitaAltriPulsanti(ArrayList<String> azioni) {
		for (String azione : azioni) {
			switch(azione) {
				case "PESCA_CARTA":
					attivaPulsantePescaCarta();
					break;
				case "ATTACCA":
					attivaPulsanteAttacco();
					break;
				case "NON_ATTACCA":
					attivaPulsanteNessunAttacco();
					break;
			}
		}
	}
	
	private void attivaPulsanteNessunAttacco() {
		nessunAttacco.getButton().setEnabled(true);
	}
	
	/**
	 * Italian: metodo che, ad ogni turno, abilita per il giocatore corrente solo i settori in cui gli è possibile muoversi.
	 * English: method that will enable, for the current player, only the sectors where he is allowed to move.
	 * @param settoriAdiacenti lista dei settori su cui è possibile muoversi, a partire da quello su cui ci si trova
	 */
	public void abilitaSettoriAdiacenti(ArrayList<String> settoriAdiacenti) {
		for (int i = 0; i < settoriAdiacenti.size(); i++) {
			for (int j = 0; j < listaPulsantiSettore.size(); j++) {
				if (settoriAdiacenti.get(i).equals(listaPulsantiSettore.get(j).getNomePulsante())) {
					listaPulsantiSettore.get(j).getButton().setEnabled(true);
				}
			}
		}
	}
	
	private void evidenziaSpostamento(Pulsante pulsante) {
		coloraGUI();
		evidenziaPulsante(pulsante);
	}
	
	/**
	 * Italian: metodo che controlla se il giocatore corrente ha inserito un input, ad esempio cliccando su un pulsante.
	 * English: method that checks if the current player inserted an input, by clicking on a button for example.
	 * @return
	 */
	public boolean isInputInserito() {
		return this.inputInserito;
	}
	
	private void registraSpostamento(Pulsante destinazione) {
		this.inputDaInviare = new String("move to: " + destinazione.getNomePulsante());
		this.inputInserito = true;
	}
	
	public String annunciaInput() {
		this.inputInserito = false;
		return this.inputDaInviare;
	}
	
	private void evidenziaPulsante(Pulsante pulsante) {
		pulsante.getButton().setBackground(Color.GREEN);
	}
	
	private void setAspettoPulsante() {
		for (Pulsante p : listaPulsantiSettore) {
			p.getButton().setBounds(p.getAscissa(), p.getOrdinata(), larghezzaPulsanteSettore, altezzaPulsanteSettore);
			p.getButton().setFont(new Font("Dialog", Font.BOLD, larghezzaPulsanteSettore/5));
			p.getButton().setEnabled(false);
			centralPanel.add(p.getButton());
		}
	}
	
	private void assegnaActionListenerSpostamento() {
		for (final Pulsante p : listaPulsantiSettore) {
			p.getButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					evidenziaSpostamento(p);
					posizioneAttuale = p.getNomePulsante();
					bottomLabel.setText("Posizione attuale: " + posizioneAttuale);
					registraSpostamento(p);
					impedisciAltriMovimenti();
					timer.stop();
					mostraCountdown.setVisible(false);
					synchronized(interfaccia){
					    interfaccia.notifyAll(); //l'intero blocco dovrà essere inserito in ogni metodo che generi un input per il server
					} 
				}
			});
		}
	}
	
	private void impedisciAltriMovimenti() {  
		for (Pulsante p : listaPulsantiSettore) {
			p.getButton().setEnabled(false);
		}
	}
	
	public void decoderComando(ArrayList<String> comando) {
        String nomeComando = comando.get(0);
        switch(nomeComando) {
            case "INIZIO":
                ArrayList<String> informazioniIniziali = estraiInformazioniDaComando(comando);
                ricavaInformazioniIniziali(informazioniIniziali);
                break;
            case "ABILITA_SETTORI":
                ArrayList<String> settoriAdiacenti = estraiInformazioniDaComando(comando);
                abilitaSettoriAdiacenti(settoriAdiacenti);
                break;
            case "CONNESSIONE_PERSA":	
            	String giocatore = comando.get(1);
                comunicaMessaggio("Connessione interrotta con " + giocatore);
                break;
            case "SCEGLIE_AZIONE":		
            	ArrayList<String> azioniPossibili = estraiInformazioniDaComando(comando);
            	assegnaActionListenerAltriPulsanti(azioniPossibili);
                abilitaAltriPulsanti(azioniPossibili);
                break;
            case "RISULTATO_ATTACCO":
            	ArrayList<String> informazioniAttacco = estraiInformazioniDaComando(comando);
            	comunicaRisultatoAttacco(informazioniAttacco);
                break;
            case "CARTA_PESCATA":
                String cartaPescata = comando.get(1);
                comunicaMessaggio("Carta pescata: " + cartaPescata);
                break;
            case "TURNO_FINITO":
                comunicaMessaggio("Non ci sono altre mosse possibili per il turno corrente");
                break;
            case "MORTO":
                comunicaMessaggio("Il tuo personaggio è morto in seguito ad un attacco");
                break;
            case "GIOCO_FINITO":					
            	ArrayList<String> datiVincitori = new ArrayList<String>(estraiInformazioniDaComando(comando));
            	datiVincitori.remove(0);	// il primo termine dell'ArrayList contiene il tipo di fine partita
            	String nomiVincitori = estraiNomiGiocatori(datiVincitori);
                comunicaMessaggio("Partita terminata " + "(" + comando.get(0) + "). I vincitori sono: " + nomiVincitori); 
        }
    }
	
	private void comunicaRisultatoAttacco(ArrayList<String> informazioniAttacco) {
		if (informazioniAttacco.size() == 2) {
    		comunicaMessaggio(informazioniAttacco.get(0) + " ha effettuato un attacco in " + informazioniAttacco.get(1) + ". Non ci sono stati morti");
    	}
    	else {
    		ArrayList<String> datiGiocatoriMorti = informazioniAttacco;	// datiGiocatoriMorti = informazioniAttacco - (primi 2 elementi dell'ArrayList)
    		datiGiocatoriMorti.remove(0);
    		datiGiocatoriMorti.remove(1);
    		String giocatoriMorti = estraiNomiGiocatori(datiGiocatoriMorti);
    		comunicaMessaggio(informazioniAttacco.get(0) + " ha effettuato un attacco in " + informazioniAttacco.get(1) + ". Giocatori morti: " + giocatoriMorti);
    	}
	}
	
	private String estraiNomiGiocatori(ArrayList<String> nomi) {
		StringBuilder elencoNomi = new StringBuilder();
		int contatore = 0;
		while (contatore < nomi.size()) {
			elencoNomi.append(nomi.get(contatore) + ";  ");
			contatore++;
		}
		return elencoNomi.toString();
	}
	
	private ArrayList<String> estraiInformazioniDaComando(ArrayList<String> comando) {
		comando.remove(0);	// tolto il nome del comando nella prima posizione, restano solo i parametri
		return comando;
	}
	
	/**
	 * Italian: metodo che visualizza sullo schermo il tempo rimasto per effettuare una mossa.
	 * English: method that shows on the screen the time left to perform an action.
	 */
    public void countDown() {
        mostraCountdown.setVisible(true);
        ActionListener scorrimentoSecondi = new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		mostraCountdown.setText("" + countdownPerMossa);
        		if (countdownPerMossa == 0) {
        			timer.stop();
        		}
        		else {
        			countdownPerMossa--;
        		}
        	}
        };
        timer = new Timer(tempoAggiornamentoCountdown*1000, scorrimentoSecondi);
        timer.setInitialDelay(0);
        timer.start();
        countdownPerMossa = 30;
    }
    
    private void assegnaActionListenerAltriPulsanti(final ArrayList<String> azioniPossibili) {
    	for (final Pulsante p : listaAltriPulsanti) {
    	    inputDaInviare = getIndiceAzione(azioniPossibili, "ATTACCA");
    	    inputDaInviare = getIndiceAzione(azioniPossibili, "PESCA_CARTA");
    	    inputDaInviare = getIndiceAzione(azioniPossibili, "NON_ATTACCA");
    		p.getButton().addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				switch(p.getNomePulsante()) {
    					case "Attacco":
    						inputDaInviare = getIndiceAzione(azioniPossibili, "ATTACCA");
    						break;
    					case "Pesca una carta":
    						inputDaInviare = getIndiceAzione(azioniPossibili, "PESCA_CARTA");
    						break;
    					case "Nessun attacco":
    						inputDaInviare = getIndiceAzione(azioniPossibili, "NON_ATTACCA");
    						break;
    				}
    				inputInserito = true;
    				p.getButton().setEnabled(false);
    				timer.stop();
					mostraCountdown.setVisible(false);
					synchronized(interfaccia){
					    interfaccia.notifyAll(); //l'intero blocco dovrà essere inserito in ogni metodo che generi un input per il server
					}
    			}
    		});
    	}
    }
    
    private String getIndiceAzione(ArrayList<String> azioniPossibili, String azioneCercata) {
    	int indice = 0;
    	for (int i = 0; i < azioniPossibili.size(); i++) {
    		if (azioniPossibili.get(i).equals(azioneCercata)) {
    			indice = i+1;
    		}
    	}
    	return String.valueOf(indice);
    }
	
}
