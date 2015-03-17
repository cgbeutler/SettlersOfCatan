package server.command;

import shared.CatanModel;

public class DiscardCardCommand implements ICommand<CatanModel>{

	/**
	 * Executes "Discard Card", removes the assigned card(s) from the players hand
	 *
	 * @pre The player has the assigned cards to discard
	 * @post completes the discard card
	 * 
	 * @param a PlayerIndex, Cards
	 */
	@Override
	public CatanModel execute(CatanModel catanModel) {
		// TODO Auto-generated method stub
		return null;
	}

}
