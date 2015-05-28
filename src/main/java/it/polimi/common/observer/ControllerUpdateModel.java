package it.polimi.common.observer;

import it.polimi.model.Model;

public class ControllerUpdateModel extends Event {

	private Model model;
	
	public ControllerUpdateModel(Model model){
		this("Si procede a fare un update del model");
		this.model = model;
	}
	
	public ControllerUpdateModel(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * getter per model
	 * @return
	 */
	public Model model(){
		return this.model;
	}

}
