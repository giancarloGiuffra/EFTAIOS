package it.polimi.gui;

import it.polimi.client.ClientGUI;
import it.polimi.client.TipoInterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GUISceltaInterfaccia {
    
    private final JFrame finestraIniziale = new JFrame("Start");
    private final String startConSocket = new String("Start con Socket");
    private final String startConRMI = new String("Start con RMI");
    private final JPanel contenitorePulsanti = new JPanel();
    private final JPanel bottomPanel = new JPanel();
    private final JTextField spazioPerIP = new JTextField(12);
    private final JLabel messaggioIP = new JLabel("Inserire l'indirizzo IP del server", SwingConstants.CENTER);
    private String ipInserito;
    private TipoInterface tipoInterfaccia;
    private ClientGUI clientGUI;
    
    /**
     * Costruttore
     * @param clientGUI 
     */
    public GUISceltaInterfaccia(ClientGUI clientGUI){
        this.clientGUI = clientGUI;
    }
    
    /**
     * lancia la GUI per scegliere l'interfaccia di connessione
     */
    public void start(){
    	this.sceltaTecnologiaDiComunicazione();
    }
    
    /**
     * Italian: Metodo che genera una finestra dotata di pulsanti e un TextField per l'inserimento
     * 		dell'indirizzo IP del server. Cliccando su un pulsante, 
     *      l'utente si 'registra' come partecipante alla partita utilizzando una delle
     *      tecnologie fornite per la giocabilità online.
     * English: Method which creates a frame endowed with buttons and a TextField to insert the
     * 		IP address. Clicking on a button the user will join to the list of players, 
     *      using one of the available technologies to play online.
     */
    public void sceltaTecnologiaDiComunicazione() {
        List<Pulsante> pulsantiStart = new ArrayList<Pulsante>();
        Pulsante inizioPartitaConSocket = new Pulsante(startConSocket);
        Pulsante inizioPartitaConRMI = new Pulsante(startConRMI);
        pulsantiStart.add(inizioPartitaConSocket);
        pulsantiStart.add(inizioPartitaConRMI);
        for (final Pulsante modalità: pulsantiStart) {
        	modalità.getButton().addActionListener(new ActionListener() {
                @Override
            	public void actionPerformed(ActionEvent e) {
                	tentaConnessione(modalità);
                }
            });
        }
        finestraIniziale.setLayout(new BorderLayout());
        finestraIniziale.add(contenitorePulsanti, BorderLayout.NORTH);
        finestraIniziale.add(bottomPanel, BorderLayout.CENTER);
        contenitorePulsanti.setLayout(new FlowLayout());
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contenitorePulsanti.add(inizioPartitaConSocket.getButton());
        contenitorePulsanti.add(inizioPartitaConRMI.getButton());
        bottomPanel.add(messaggioIP, BorderLayout.NORTH);
        bottomPanel.add(spazioPerIP, BorderLayout.CENTER);
        finestraIniziale.getContentPane();
        finestraIniziale.pack();
        finestraIniziale.setLocationRelativeTo(null);
        finestraIniziale.setVisible(true);
        finestraIniziale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    private void tentaConnessione(Pulsante modalità) {
    	if (isPossibileIP(spazioPerIP.getText()) == true) {
    		ipInserito = spazioPerIP.getText();
            comunicaTecnologiaDiComunicazione(modalità.getNomePulsante());
    	}
    	else {
    		comunicaMessaggio("Indirizzo IP non valido");
    	}
    }
    
    private boolean isPossibileIP(String ipInserito) {
    	final Pattern formatoIndirizzoIP = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");
        Matcher matcher = formatoIndirizzoIP.matcher(ipInserito);
        return matcher.matches();
    }
    
    /**
     * Italian: metodo che restituisce l'indirizzo IP inserito dall'utente.
     * English: method which returns the IP address inserted by the user. 
     * @return indirizzo IP
     */
    public String getIP() {
    	return this.ipInserito;
    }
    
    private void comunicaTecnologiaDiComunicazione(String nomePulsante) {
        finestraIniziale.setVisible(false);
        if (nomePulsante.equals(startConSocket)) {
            tipoInterfaccia = TipoInterface.SOCKET_GUI;
        }
        else if (nomePulsante.equals(startConRMI)) {
            tipoInterfaccia = TipoInterface.RMI_GUI;
        }
        this.clientGUI.runNetworkInterface(tipoInterfaccia);
    }
    
	/**
	 * Italian: metodo che informerà l'utente riguardo eventuali problemi con la connessione al server.
	 * English: method used to inform the user about possible problems with his attempt to connect
	 * 	to the server.
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
     * 
     * @param nomePulsante
     */
    public void comunicaTecnologiaDiComunicazioneHelpTest(String nomePulsante) {
    	finestraIniziale.setVisible(false);
        if (nomePulsante.equals(startConSocket)) {
            tipoInterfaccia = TipoInterface.SOCKET_GUI;
        }
        else if (nomePulsante.equals(startConRMI)) {
            tipoInterfaccia = TipoInterface.RMI_GUI;
        }
    }
    
    /**
     * Italian: metodo di supporto per i test, che restituisce il tipo di interfaccia utilizzato per la comunicazione.
     * English: method used as support for tests, which returns the type of interface used to communicate.
     * @return
     */
    public TipoInterface getTipoInterfaccia() {
    	return this.tipoInterfaccia;
    }

}
