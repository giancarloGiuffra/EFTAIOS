package it.polimi.common.observer;

/**
 * Evento che notifica che il giocatore ha fatto un annuncio
 *
 */
public final class UserAnnounceSectorEvent extends Event {

	private final String SETTORE_ANNUNCIATO;
	
	/**
	 * Costruttore
	 * @param to string col nome del settore
	 */
	public UserAnnounceSectorEvent(String settore){
		super(buildMsg(settore));
		this.SETTORE_ANNUNCIATO = settore;
	}
	
	/**
	 * Genera messaggio che descrive l'evento
	 * @param to
	 * @return
	 */
	private static String buildMsg(String settore){
		return new StringBuilder().append("Settore annunciato: ").append(settore).toString();
	}
	
	/**
	 * @return il nome del settore annunciato
	 */
	public String settoreAnnunciato(){
		return this.SETTORE_ANNUNCIATO;
	}

}
