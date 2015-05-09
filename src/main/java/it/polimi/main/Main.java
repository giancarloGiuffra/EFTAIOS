public class Main {

	private Model model;
	private View view;
	private Controller controller;
	
	private Main() {
		this.model = new Model(8); //TODO dovrà essere modificato per gestire nro giocatore a seconda degli utenti connessi
		this.view = new View(System.in, System.out); //NOSONAR si vuole usare System.out per interagire con l'utente
		this.controller = new Controller(this.model, this.view);
		view.addObserver(controller);
		model.addObserver(view);
	}


	private void run() {		
		(new Thread(view)).start();		
	}	

}
