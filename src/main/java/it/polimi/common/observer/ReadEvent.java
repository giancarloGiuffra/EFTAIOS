package it.polimi.common.observer;

public class ReadEvent extends RMIEvent {

	private static final long serialVersionUID = -7854504115301791284L;

	public ReadEvent(){
		this("ReadEvent");
	}
	
	public ReadEvent(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
