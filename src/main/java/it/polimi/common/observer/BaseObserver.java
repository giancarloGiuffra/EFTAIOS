package it.polimi.common.observer;

public interface BaseObserver {
	
	/**
	 * Notifica al Observer l'evento lanciato dal Observable
	 * @param source
	 * @param event
	 */
	void notify(BaseObservable source, Event event);
}
