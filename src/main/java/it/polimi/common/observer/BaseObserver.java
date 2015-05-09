package it.polimi.common.observer;

public interface BaseObserver {
	
	/**
	 * Notifica al Observer l'evento lanciato dal Observable
	 * @param source
	 * @param event
	 */
	void notifyRicevuto(BaseObservable source, Event event);
}
