package it.polimi.common.observer;

import java.io.Serializable;

public class RMIEvent extends Event implements Serializable {

	private static final long serialVersionUID = 3694121898827191647L;

	public RMIEvent(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
