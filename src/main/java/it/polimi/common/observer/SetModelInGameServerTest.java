package it.polimi.common.observer;

import it.polimi.model.Model;

public class SetModelInGameServerTest extends Event {
	
	private Model model;

	public SetModelInGameServerTest(Model model) {
		super("setting model in GameServerTest");
		this.model = model;
	}
	
	public Model model(){
		return this.model;
	}

}
