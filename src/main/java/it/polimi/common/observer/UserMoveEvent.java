package it.polimi.common.observer;

/**
 * Evento che notifica che l'utente ha inserito una mossa
 *
 */
public final class UserMoveEvent extends Event {
	
	private final String SETTORE_DESTINO;
	public static final String CLASS_NAME = UserMoveEvent.class.getSimpleName();
	
	/**
	 * Costruttore
	 * @param to string col nome del settore
	 */
	public UserMoveEvent(String to){
		super(buildMsg(to));
		this.SETTORE_DESTINO = to;
	}
	
	/**
	 * Genera messaggio che descrive l'evento
	 * @param to
	 * @return
	 */
	private static String buildMsg(String to){
		return new StringBuilder().append("Inserita mossa a ").append(to).toString();
	}
	
	/**
	 * @return il nome del settore di destinazione
	 */
	public String settoreDestinazione(){
		return this.SETTORE_DESTINO;
	}
	
}
