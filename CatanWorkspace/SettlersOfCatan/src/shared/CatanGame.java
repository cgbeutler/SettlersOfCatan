package shared;

import java.util.Observable;

public class CatanGame extends Observable {
	
	/** The log. */
	private CatanModel model;
	
	/**
	 * Instantiates a new Catan Game.
	 */
	private CatanGame() 
	{
		model = new CatanModel();
	}
	
	public CatanModel getModel() 
	{
		return model;
	}

	public void setModel(CatanModel model) 
	{
		this.model = model;
		setChanged();
		notifyObservers(model);
	}	
}
