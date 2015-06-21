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
import java.util.List;

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
	private String nomeGiocatore;
	private String razzaGiocatore;
	private String posizioneAttuale;
	private String settoreAnnunciato;
	private JLabel topLabel = new JLabel("Giocatore corrente: " + nomeGiocatore + " (" + razzaGiocatore + ")", SwingConstants.CENTER);
	private JLabel bottomLabel = new JLabel("Posizione attuale: " + posizioneAttuale, SwingConstants.CENTER);
	private JPanel centralPanel = new JPanel();
	private boolean inputInserito = false;
	private String inputDaInviare;
	private NetworkInterfaceForClient interfaccia;
	private Timer timer;
	private int countdownPerMossa;   // il giocatore ha a disposizione 30 secondi per effettuare la sua mossa
	private int tempoAggiornamentoCountdown = 1;  
	private JLabel mostraCountdown = new JLabel("" + countdownPerMossa);
	
	/**
	 * Costruttore
	 * @param interfaccia
	 */
	public GUI(NetworkInterfaceForClient interfaccia){
	    this.interfaccia = interfaccia;
	    this.countdownPerMossa = this.interfaccia.timeLimit();
	}
	
	private void getLookAndFeel() {
		try {
		    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch (Exception e) {
		    e.printStackTrace();
		}
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

	private void creaGUI() {
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
		contenitoreAltriElementi.add(nessunAttacco.getButton());
		contenitoreAltriElementi.add(attacco.getButton());  
		contenitoreAltriElementi.add(pescaCarta.getButton()); 
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
			public void windowClosing(WindowEvent e) {	
				interfaccia.close();
				System.exit(1);
			}
		});
	}
	
	private void setAspettoPulsante() {
		for (Pulsante p : listaPulsantiSettore) {
			p.getButton().setBounds(p.getAscissa(), p.getOrdinata(), larghezzaPulsanteSettore, altezzaPulsanteSettore);
			p.getButton().setFont(new Font("Dialog", Font.BOLD, (larghezzaPulsanteSettore-5)/5));
			p.getButton().setEnabled(false);
			centralPanel.add(p.getButton());
		}
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
        countdownPerMossa = this.interfaccia.timeLimit();
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
	
	private void coloraGUI() {
		ricavaSettori();
		coloraSettoriPerTipo(settoriInaccessibili);
		coloraSettoriPerTipo(settoriSicuri);
		coloraSettoriPerTipo(settoriPericolosi);
		coloraSettoriPerTipo(basi);
		coloraSettoriPerTipo(scialuppe);
	}
	
	public void visualizzaTabellone() {
		getLookAndFeel();
		creaGUI();
		coloraGUI();
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
	
	private void ricavaInformazioniIniziali(List<String> informazioniIniziali) {
		nomeGiocatore = informazioniIniziali.get(0);
		posizioneAttuale = informazioniIniziali.get(1);
		razzaGiocatore = informazioniIniziali.get(2);
		evidenziaPosizioneIniziale(posizioneAttuale);
		topLabel.setText("Giocatore corrente: " + nomeGiocatore + " (" + razzaGiocatore + ")");
		bottomLabel.setText("Posizione attuale: " + posizioneAttuale);
	}
	
	private void evidenziaPosizioneIniziale(String nomeSettore) {
		for (Pulsante p : listaPulsantiSettore) {
			if (p.getNomePulsante().equals(nomeSettore)) {
				p.getButton().setBackground(Color.GREEN);
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
	
	private void abilitaAltriPulsanti(List<String> azioniPossibili) {
		for (String azione : azioniPossibili) {
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
	
	private void attivaPulsanteAttacco() {
		attacco.getButton().setEnabled(true);
	}
	
	private void attivaPulsantePescaCarta() {
		pescaCarta.getButton().setEnabled(true);
	}
	
	private void attivaPulsanteNessunAttacco() {
		nessunAttacco.getButton().setEnabled(true);
	}
	
	/**
	 * Italian: metodo che, ad ogni turno, abilita per il giocatore corrente solo i settori in cui gli è possibile muoversi.
	 * English: method that will enable, for the current player, only the sectors where he is allowed to move.
	 * @param settoriAdiacenti lista dei settori su cui è possibile muoversi, a partire da quello su cui ci si trova
	 */
	public void abilitaSettoriAdiacenti(List<String> settoriAdiacenti) {
		for (int i = 0; i < settoriAdiacenti.size(); i++) {
			for (int j = 0; j < listaPulsantiSettore.size(); j++) {
				if (settoriAdiacenti.get(i).equals(listaPulsantiSettore.get(j).getNomePulsante())) {
					listaPulsantiSettore.get(j).getButton().setEnabled(true);
				}
			}
		}
	}
	
	private void evidenziaPulsante(Pulsante pulsante) {
		pulsante.getButton().setBackground(Color.GREEN);
	}
	
	private void evidenziaSpostamento(Pulsante pulsante) {
		coloraGUI();
		evidenziaPulsante(pulsante);
	}
	
	private void registraSpostamento(Pulsante destinazione) {
		this.inputDaInviare = new String("move to: " + destinazione.getNomePulsante());
		this.inputInserito = true;
	}
	
	/**
	 * Italian: metodo che controlla se il giocatore corrente ha inserito un input, ad esempio cliccando su un pulsante.
	 * English: method that checks if the current player inserted an input, by clicking on a button for example.
	 * @return
	 */
	public boolean isInputInserito() {
		return this.inputInserito;
	}
	
	/**
	 * Italian: metodo utilizzato per informare il server riguardo l'input inserito dall'utente interagendo con il gioco.
	 * English: method used to inform the server about the input inserted by the player interacting with the game.
	 * @return
	 */
	public String annunciaInput() {
		this.inputInserito = false;
		return this.inputDaInviare;
	}
	
	private void actionListenerAnnuncioSettore() {
		for (final Pulsante p : listaPulsantiSettore) {
			p.getButton().addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					settoreAnnunciato = p.getNomePulsante();
					impedisciAltriMovimenti();
					timer.stop();
					mostraCountdown.setVisible(false);
					annunciaRumore();
				}
			});
		}
	}
	
	private void annunciaRumore() {
		this.inputDaInviare = new String("announce: " + this.settoreAnnunciato);
		this.inputInserito = true;
		synchronized(interfaccia){
		    interfaccia.notifyAll();
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
	
	/**
	 * Italian: metodo che riceve dei comandi dal server e li decodifica, estraendone le informazioni utili.
	 * English: method that decodes the commands received from the server, in order to extract useful informations from them.
	 * @param list
	 */
	public void decoderComando(List<String> list) {
        String nomeComando = list.get(0);
        switch(nomeComando) {
            case "INIZIO":
                List<String> informazioniIniziali = estraiInformazioniDaComando(list);
                ricavaInformazioniIniziali(informazioniIniziali);
                break;
            case "ABILITA_SETTORI":
                List<String> settoriAdiacenti = estraiInformazioniDaComando(list);
                removeAllActionsListeners();
                assegnaActionListenerSpostamento();
                abilitaSettoriAdiacenti(settoriAdiacenti);
                break;
            case "CONNESSIONE_PERSA":	
            	String giocatore = list.get(1);
                comunicaMessaggio("Connessione interrotta con " + giocatore);
                break;
            case "SCEGLIE_AZIONE":		
            	List<String> azioniPossibili = estraiInformazioniDaComando(list);
            	removeAllActionsListeners();
            	assegnaActionListenerAltriPulsanti(azioniPossibili);
                abilitaAltriPulsanti(azioniPossibili);
                break;
            case "PESCA_CARTA":
                removeAllActionsListeners();
                assegnaActionListenerPulsanteCarta();
                attivaPulsantePescaCarta();
                break;
            case "SILENZIO_DICHIARATO":
                //TODO gestire due casi: 1) arriva solo silenzio dichiarato, 2) arriva silenzio dichiarato + nome (estrai le info dal comando!!!)
                comunicaSilenzioDichiarato(list);
                break;
            case "SETTORE_ANNUNCIATO":
                //TODO gestire due casi: leggi comando.java (estrai le info dal comando!!!)
                comunicaSettoreAnnunciato(list);
                break;
            case "SETTORE_DA_ANNUNCIARE":
            	removeAllActionsListeners();
            	actionListenerAnnuncioSettore();
                abilitaSettori();
                comunicaMessaggio("cliccare sul settore in cui si vuole dichiarare un rumore");
                break;
            case "NESSUNA_GAMEROOM_DISPONIBILE":
                comunicaMessaggio("Non ci sono sale libere al momento: riprovare più tardi. La connessione verrà chiusa");
                break;
            case "RISULTATO_ATTACCO":
            	List<String> informazioniAttacco = estraiInformazioniDaComando(list);
            	comunicaRisultatoAttacco(informazioniAttacco);
                break;
            case "CARTA_PESCATA":
                String cartaPescata = list.get(1);
                comunicaMessaggio("Carta pescata: " + cartaPescata);
                break;
            case "TURNO_FINITO":
                comunicaMessaggio("Non ci sono altre mosse possibili per il turno corrente");
                break;
            case "MORTO":
                comunicaMessaggio("Il tuo personaggio è morto in seguito ad un attacco");
                break;
            case "GIOCO_FINITO":					
            	ArrayList<String> datiVincitori = new ArrayList<String>(estraiInformazioniDaComando(list));
            	datiVincitori.remove(0);	// il primo termine dell'ArrayList contiene il tipo di fine partita
            	String nomiVincitori = estraiNomiGiocatori(datiVincitori);
                comunicaMessaggio("Partita terminata " + "(" + list.get(0) + "). I vincitori sono: " + nomiVincitori); 
        }
    }
	
	private void assegnaActionListenerPulsanteCarta() {
		for (final Pulsante p : listaAltriPulsanti) {
    		p.getButton().addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				inputDaInviare = "";
					disabilitaAltriPulsanti();
					inputInserito = true;
					timer.stop();
					mostraCountdown.setVisible(false);
					synchronized(interfaccia){
					    interfaccia.notifyAll(); //l'intero blocco dovrà essere inserito in ogni metodo che generi un input per il server
					}
    			}
    		});
		}
	}

	private void comunicaSettoreAnnunciato(List<String> list) {
        if(list.size()==3)
            comunicaMessaggio(list.get(1) + "ha dichiarato un RUMORE in " + list.get(2));
        else
            comunicaMessaggio("hai dichiarato un RUMORE in " + list.get(1));
    }

    private void comunicaSilenzioDichiarato(List<String> list) {
        if(list.size()==2)
            comunicaMessaggio( list.get(1) + "ha dichiarato 'SILENZIO' ");
        else
            comunicaMessaggio("hai dichiarato 'SILENZIO' ");
    }

    private void abilitaSettori() {
		for (Pulsante p : listaPulsantiSettore) {
			p.getButton().setEnabled(true);
		}
	}
	
	private void comunicaRisultatoAttacco(List<String> informazioniAttacco) {
		if (informazioniAttacco.size() == 2) {
    		comunicaMessaggio(informazioniAttacco.get(0) + " ha effettuato un attacco in " + informazioniAttacco.get(1) + ". Non ci sono stati morti");
    	}
    	else {
    		List<String> datiGiocatoriMorti = new ArrayList<String>(informazioniAttacco);	// datiGiocatoriMorti = informazioniAttacco - (primi 2 elementi dell'ArrayList)
    		datiGiocatoriMorti.remove(0);
    		datiGiocatoriMorti.remove(0);
    		String giocatoriMorti = estraiNomiGiocatori(datiGiocatoriMorti);
    		comunicaMessaggio(informazioniAttacco.get(0) + " ha effettuato un attacco in " + informazioniAttacco.get(1) + ". Giocatori morti: " + giocatoriMorti);
    	}
	}
	
	private String estraiNomiGiocatori(List<String> nomi) {
		StringBuilder elencoNomi = new StringBuilder();
		int contatore = 0;
		while (contatore < nomi.size()) {
			elencoNomi.append(nomi.get(contatore) + ";  ");
			contatore++;
		}
		return elencoNomi.toString();
	}
	
	private List<String> estraiInformazioniDaComando(List<String> list) {
		list.remove(0);	// tolto il nome del comando nella prima posizione, restano solo i parametri
		return list;
	}
    
    private void assegnaActionListenerAltriPulsanti(final List<String> azioniPossibili) {
    	for (final Pulsante p : listaAltriPulsanti) {
    		p.getButton().addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				switch(p.getNomePulsante()) {
    					case "Attacco":
    						inputDaInviare = getIndiceAzione(azioniPossibili, "ATTACCA");
    						disabilitaAltriPulsanti();
    						inputInserito = true;
    						break;
    					case "Pesca una carta":	// sistemare: server si aspetta un ulteriore input
    						inputDaInviare = getIndiceAzione(azioniPossibili, "PESCA_CARTA");
    						disabilitaAltriPulsanti();
    						inputInserito = true;
    						//confermaPescaCarta();//TODO l'annidamento dei notify genera il problema mi sembra
    						break;
    					case "Nessun attacco":
    						inputDaInviare = getIndiceAzione(azioniPossibili, "NON_ATTACCA");
    						disabilitaAltriPulsanti();
    						inputInserito = true;
    						break;
    				}
    				timer.stop();
					mostraCountdown.setVisible(false);
					synchronized(interfaccia){
					    interfaccia.notifyAll(); //l'intero blocco dovrà essere inserito in ogni metodo che generi un input per il server
					}
    			}
    		});
    	}
    }
    
    private void disabilitaAltriPulsanti() {
    	for (Pulsante p : listaAltriPulsanti) {
    		p.getButton().setEnabled(false);
    	}
    }
    
    private String getIndiceAzione(List<String> azioniPossibili, String azioneCercata) {
    	int indice = 0;
    	for (int i = 0; i < azioniPossibili.size(); i++) {
    		if (azioniPossibili.get(i).equals(azioneCercata)) {
    			indice = i+1;
    		}
    	}
    	return String.valueOf(indice);
    }
    
    /**
     * da migliorare
     */
    private void removeAllActionsListeners(){
        //altri pulsanti
        for (final Pulsante p : listaAltriPulsanti){
            for(ActionListener act : p.getButton().getActionListeners()) {
                p.getButton().removeActionListener(act);
            }
        }
        //pulsanti settori
        for (final Pulsante p : listaPulsantiSettore){
            for(ActionListener act : p.getButton().getActionListeners()) {
                p.getButton().removeActionListener(act);
            }
        }
    }
    
    /**
     * Italian: metodo di supporto per i test, che crea una nuova lista di pulsanti associati ai settori.
     * English: method used as support for tests, whose purpose is to create a new list of buttons associated to the sectors.
     * @return listaPulsantiSettore
     */
    public List<Pulsante> creaListaPulsantiSettoreHelpTest() {
    	creaListaPulsantiSettore();
    	ArrayList<Pulsante> listaPerTest = new ArrayList<Pulsante>();
    	listaPerTest = this.listaPulsantiSettore;
    	return listaPerTest;
    }
    
    /**
     * Italian: metodo di supporto per i test, utilizzato semplicemente per chiamare il metodo "setColorePulsante".
     * English: method used as support for tests, whose function only consists in calling the method "setColorePulsante".
     * @param nomeLista 
     * @param pulsante 
     */
    public void setColorePulsanteHelpTest(String nomeLista, JButton pulsante) {
    	setColorePulsante(nomeLista, pulsante);
    }
    
    /**
     * Italian: metodo di supporto per i test, utilizzato semplicemente per chiamare il metodo "evidenziaSpostamento".
     * English: method used as support for tests, whose function only consists in calling the method "evidenziaSpostamento".
     * @param pulsante
     */
    public void evidenziaSpostamentoHelpTest(Pulsante pulsante) {
    	evidenziaSpostamento(pulsante);
    }
    
    /**
     * Italian: metodo di supporto per i test, utilizzato semplicemente per chiamare il metodo "getIndiceAzione". Tale metodo
     * 	cerca un'azione tra un elenco di azioni possibili e restituisce l'indice dell'azione cercata (partendo da 1).
     * English: method used as support for tests, whose function only consists in calling the method "getIndiceAzione". This last
     * 	method looks for an action in a list of possible actions and returns the corresponding index of the action (the count starts from from 1).
     * @param azioniPossibili
     * @param azioneCercata
     * @return indice dell'azione cercata
     */
    public String getIndiceAzioneHelpTest(List<String> azioniPossibili, String azioneCercata) {
    	return getIndiceAzione(azioniPossibili, azioneCercata);
    }
    
    /**
     * Italian: metodo di supporto per i test, utilizzato semplicemente per chiamare il metodo "registraSpostamento".
     * English: method used as support for tests, whose function only consists in calling the method "registraSpostamento".
     * @param pulsante
     */
    public void registraSpostamentoHelpTest(Pulsante pulsante) {
    	registraSpostamento(pulsante);
    }
    
    /**
     * Italian: metodo di supporto per i test, utilizzato semplicemente per chiamare il metodo "abilitaAltriPulsanti".
     * English: method used as support for tests, whose function only consists in calling the method "abilitaAltriPulsanti".
     */
    public void abilitaAltriPulsantiHelpTest(List<String> azioniPossibili) {
    	abilitaAltriPulsanti(azioniPossibili);
    }
    
    /**
     * Italian: metodo di supporto per i test, utilizzato semplicemente per chiamare il metodo "creaGUI".
     * English: method used as support for tests, whose function only consists in calling the method "creaGUI".
     */
    public void creaGUIHelpTest() {
    	creaGUI();
    }
    
    /**
     * Italian: metodo di supporto per i test, utilizzato semplicemente per chiamare il metodo "ricavaInformazioniIniziali".
     * English: method used as support for tests, whose function only consists in calling the method "ricavaInformazioniIniziali".
     * @param informazioniIniziali
     */
    public void ricavaInformazioniInizialiHelpTest(List<String> informazioniIniziali) {
    	ricavaInformazioniIniziali(informazioniIniziali);
    }
    
    /**
     * Italian: metodo di supporto per i test, che restituisce le etichette "bottomLabel" e "topLabel" in un array.
     * English: method used as support for tests, which returns the "bottomLabel" and "topLabel" JLabels as array.
     * @return bottomLabel and topLabel
     */
    public JLabel[] getLabelsHelpTest() {
    	JLabel[] labels = {bottomLabel, topLabel};
    	return labels;
    }
    
    /**
     * Italian: metodo di supporto per i test, utilizzato semplicemente per chiamare il metodo "estraiNomiGiocatori". Tale metodo
     * 	riceve in ingresso una lista di nomi di giocatori e restituisce questi nomi in un'unica stringa, usando il simbolo ";" come
     * 	separatore.
     * English: method used as support for tests, whose function only consists in calling the method "estraiNomiGiocatori". This last
     * 	method gets as input a list containing a certain amount of names for players and elaborates this input in order to return all
     * 	those names in a single string, using the ";" symbol as separator.
     * @param nomi
     */
    public String estraiNomiGiocatoriHelpTest(List<String> nomi) {
    	return estraiNomiGiocatori(nomi);
    }
	
}
