package it.polimi.gui;

import it.polimi.gioco.Partita;
import it.polimi.model.sector.Settore;
import it.polimi.model.sector.TipoSettore;
import it.polimi.model.tabellone.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


// per il posizionamento pulsanti: https://docs.oracle.com/javase/tutorial/uiswing/layout/none.html

public class GUI {
	
	private final static String lettere[] = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W"};
	private final static String numeri[] = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14"};
	private final int numeroPulsanti = lettere.length * numeri.length;
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
	
	// il metodo deve essere invocato successivamente all'inserimento del numero di giocatori
	public static void creaGUI() {
		JFrame frame = new JFrame("Escape from the aliens");
		JLabel topLabel = new JLabel("Giocatore corrente: ", SwingConstants.CENTER);
		JPanel centralPanel = new JPanel();
		JPanel topPanel = new JPanel();
		JPanel bottomPanel = new JPanel();
		JLabel bottomLabel = new JLabel("Posizione attuale: ", SwingConstants.CENTER);
		JPanel bottomBottomPanel = new JPanel();
		JPanel topTopPanel = new JPanel();
		frame.setLayout(new BorderLayout());
		//frame.setSize(1350, 700);
		topPanel.setLayout(new BorderLayout());
		topPanel.add(topLabel, BorderLayout.CENTER);
		topPanel.add(topTopPanel, BorderLayout.NORTH);
		bottomPanel.setLayout(new BorderLayout());
		bottomPanel.add(bottomLabel, BorderLayout.CENTER);
		bottomPanel.add(bottomBottomPanel, BorderLayout.SOUTH);
		frame.add(topPanel, BorderLayout.NORTH);
		centralPanel.setLayout(null);
		for (int j = 0; j < numeroColonne; j++) {
			for (int i = 0; i < numeroRighe; i++ ) {
				Pulsante pulsanteSuRigaI = new Pulsante(lettere[j] + numeri[i], j+""+i);
				pulsanteSuRigaI.getButton().setBounds(xCorrente, yCorrente, larghezzaPulsante, altezzaPulsante);
				yCorrente += altezzaPulsante; 
				centralPanel.add(pulsanteSuRigaI.getButton());
				 
				//pulsanteSuRigaI.setBackground(Color.GREEN);
			}
			if (j%2 == 0) {  // se la colonna ha indice pari, abbassa la posizione dei pulsanti
				yCorrente = yIniziale + altezzaPulsante/2;
			}
			else yCorrente = yIniziale;
			xCorrente += larghezzaPulsante;
		} 
		frame.add(centralPanel, BorderLayout.CENTER);
		frame.add(bottomPanel, BorderLayout.SOUTH);
		frame.getContentPane();
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public static void ricavaSettori() {
		Settore baseUmana = galilei.baseUmana();
		String nomeSettore = baseUmana.getNome();
	}

	public static void setNumeroGiocatori() {
		JFrame finestra = new JFrame("Numero dei giocatori");
		JLabel etichetta = new JLabel("Inserire il numero dei giocatori: ");
		final JTextField inserimentoNumero = new JTextField(" " , 5);
		inserimentoNumero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				numeroGiocatori = inserimentoNumero.getText();
				creaGUI();
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
	
	public static int getNumeroGiocatori() {
		return Integer.parseInt(numeroGiocatori);
	}
	
	public static Partita creaNuovaPartita() {
		return nuovaPartita;
	}
	
}
