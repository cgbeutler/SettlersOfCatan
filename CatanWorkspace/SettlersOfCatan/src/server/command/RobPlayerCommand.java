package server.command;

import shared.CatanModel;

public class RobPlayerCommand implements ICommand<CatanModel>{

	/**
	 * Executes "Rob Player", player picks from a list of robbable player.  The selected player
	 * must give the player one card at random.
	 *
	 * @pre The victim is built on the hex and has at least one card 
	 * @post completes the rob player
	 * 
	 * @param a PlayerIndex, a VictimeIndex, a Location
	 */
	@Override
	public CatanModel execute(CatanModel catanModel) {
		// TODO Auto-generated method stub
		return null;
	}

}
