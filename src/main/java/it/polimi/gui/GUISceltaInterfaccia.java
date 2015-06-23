package it.polimi.gui;

import it.polimi.client.ClientGUI;
import it.polimi.client.TipoInterface;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GUISceltaInterfaccia {
    
    private final JFrame finestraIniziale = new JFrame("Start");
    private final String startConSocket = new String("Start con Socket");
    private final String startConRMI = new String("Start con RMI");
    private final JPanel contenitorePulsanti = new JPanel();	//
    private final JPanel bottomPanel = new JPanel();			//
    private final JTextField spazioPerIP = new JTextField(12);	//
    private final JLabel messaggioIP = new JLabel("Inserire l'indirizzo IP del server", SwingConstants.CENTER);
    private String ipInserito;									//
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
     * Italian: Metodo che genera una finestra dotata di pulsanti. Cliccando su un pulsante, 
     *      l'utente si 'registra' come partecipante alla partita utilizzando una delle
     *      tecnologie fornite per la giocabilità online.
     * English: Method which creates a frame endowed with buttons. Clicking on a button
     *      the user will join to the list of players, using one of the available technologies
     *      to play online.
     */
    public void sceltaTecnologiaDiComunicazione() {
        List<Pulsante> pulsantiStart = new ArrayList<Pulsante>();
        Pulsante inizioPartitaConSocket = new Pulsante(startConSocket);
        Pulsante inizioPartitaConRMI = new Pulsante(startConRMI);
        pulsantiStart.add(inizioPartitaConSocket);
        pulsantiStart.add(inizioPartitaConRMI);
        for (int i = 0; i < pulsantiStart.size(); i++) {
            final Pulsante modalità = pulsantiStart.get(i);
            modalità.getButton().addActionListener(new ActionListener() {
                @Override
            	public void actionPerformed(ActionEvent e) {
                    comunicaTecnologiaDiComunicazione(modalità.getNomePulsante());
                }
            });
        }
        //finestraIniziale.setLayout(new FlowLayout());
        finestraIniziale.setLayout(new BorderLayout());					//
        finestraIniziale.add(contenitorePulsanti, BorderLayout.NORTH);	//
        finestraIniziale.add(bottomPanel, BorderLayout.CENTER);			//
        contenitorePulsanti.setLayout(new FlowLayout());				//
        bottomPanel.setLayout(new BorderLayout());						//
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contenitorePulsanti.add(inizioPartitaConSocket.getButton());	//
        contenitorePulsanti.add(inizioPartitaConRMI.getButton());		//
        bottomPanel.add(messaggioIP, BorderLayout.NORTH); 				//
        bottomPanel.add(spazioPerIP, BorderLayout.CENTER); 				//
        //finestraIniziale.add(inizioPartitaConSocket.getButton());
        //finestraIniziale.add(inizioPartitaConRMI.getButton());
        finestraIniziale.getContentPane();
        finestraIniziale.pack();
        finestraIniziale.setLocationRelativeTo(null);
        finestraIniziale.setVisible(true);
        finestraIniziale.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
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
     * Italian: metodo che informerà il giocatore riguardo eventi che possono verificarsi 
     *  durante la partita.
     * English: method used to inform the player about events that can possibly happen during 
     *  the game.
     * @param tipoMessaggio messaggio da comunicare
     */
    public void comunicaConnessioneFallita() {
        JFrame frame = new JFrame();
        String tipoMessaggio = "Connessione Fallita";
        JLabel messaggio = new JLabel(tipoMessaggio, SwingConstants.CENTER);
        messaggio.setBorder(new EmptyBorder(10, 20, 10, 20));
        frame.add(messaggio);
        frame.getContentPane();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    /*
    private void inserimentoIP() {
    	JFrame frame = new JFrame();
    	JLabel messaggio = new JLabel("Inserire l'indirizzo IP del server", SwingConstants.CENTER);
    	JTextField spazioPerIP = new JTextField(12); 
    	messaggio.setBorder(new EmptyBorder(10, 20, 10, 20));
    	frame.setLayout(new BorderLayout());
    	frame.add(messaggio, BorderLayout.NORTH);
    	frame.add(spazioPerIP, BorderLayout.CENTER);
    	frame.getContentPane();
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    } */
    
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
