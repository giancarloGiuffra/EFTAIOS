package it.polimi.gui;

import it.polimi.client.TipoInterface;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class GUISceltaInterfaccia {
    
    private final JFrame finestraIniziale = new JFrame("Start");
    private final String startConSocket = new String("Start con Socket");
    private final String startConRMI = new String("Start con RMI");
    private TipoInterface tipoInterfaccia;
    
    /**
     * Costruttore
     */
    public GUISceltaInterfaccia(){}
    
    /**
     * Italian: Metodo che genera una finestra dotata di pulsanti. Cliccando su un pulsante, 
     *      l'utente si 'registra' come partecipante alla partita utilizzando una delle
     *      tecnologie fornite per la giocabilità online.
     * English: Method which creates a frame endowed with buttons. Clicking on a button
     *      the user will join to the list of players, using one of the available technologies
     *      to play online.
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
        frame.add(messaggio);
        frame.getContentPane();
        frame.pack();
        frame.setVisible(true);
    }

}
