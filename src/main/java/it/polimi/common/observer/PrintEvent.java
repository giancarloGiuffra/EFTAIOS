package it.polimi.common.observer;

public class PrintEvent extends RMIEvent {

	private static final long serialVersionUID = 8526386669331948696L;

	public PrintEvent(String msg) {
		super(msg);
	}

}
