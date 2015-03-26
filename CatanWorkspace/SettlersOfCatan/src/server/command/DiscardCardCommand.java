package server.command;

import org.apache.log4j.Logger;

import shared.CatanModel;
import shared.Player;
import shared.ResourceList;
import shared.TurnType;
import shared.comm.serialization.DiscardCardsRequest;

public class DiscardCardCommand implements ICommand<CatanModel> {
	private static final Logger log = Logger.getLogger(OfferTradeCommand.class);
	
	private CatanModel model;
	private ResourceList inventory;
	private ResourceList bank;
	private ResourceList discard;
	private DiscardCardsRequest request;

	public DiscardCardCommand(DiscardCardsRequest request) {
		this.request = request;
	}

	/**
	 * Executes "Discard Card", removes the assigned card(s) from the players
	 * hand
	 *
	 * @pre The player has the assigned cards to discard
	 * @post completes the discard card
	 * 
	 * @param a PlayerIndex, Cards
	 */

	@Override
	public CatanModel execute(CatanModel catanModel) {
		log.trace("Initiate Discard");
		initialize(catanModel);
		ResourceList.moveResources(inventory, bank, discard);
		boolean isDiscarding = true;
		while (isDiscarding == true)
		{
			int count = 0;
			for (Player p : catanModel.getPlayers())
			{
				if (p.hasDiscarded() == true);
				{
					count++;
				}
			}
			if (count == 4)
			{
				isDiscarding = false;
			}
			else
			{
				count = 0;
			}
		}
		catanModel.getTurnTracker().setStatus(TurnType.PLAYING);
		return catanModel;
	}

	public void initialize(CatanModel catanModel) {
		model = catanModel;
		bank = catanModel.getBank();
		inventory = getInventory();
		discard = getDiscard();
	}

	public ResourceList getInventory() {
		int index = request.getPlayerIndex();
		Player player = model.getPlayers()[index];
		return player.getResources();
	}

	public ResourceList getDiscard() {
		ResourceList offer = request.getDiscardedCards();
		ResourceList.invertResources(offer);
		return offer;
	}

}
