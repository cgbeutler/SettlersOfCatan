package shared;

public class TradeOffer 
{
	private String type;
	private int playerIndex;
	private int receiver;
	private ResourceList offer;
	
	public TradeOffer(int playerIndex, ResourceList offer, int receiver) {
		this.type = "offerTrade";
		this.playerIndex = playerIndex;
		this.receiver = receiver;
		this.offer = offer;
	}
	
	/**
	 * Accepts the offered trade
	 */
	public void accept()
	{
		// This function doesn't belong in here.  This object should just store the data 
		// while the logic happens in the CatanModel
	}
	
	/**
	 * Declines the offered trade
	 */
	public void decline()
	{
		// This function doesn't belong in here.  This object should just store the data 
		// while the logic happens in the CatanModel
	}

	/**
	 * Get the sender of the trade
	 * @return
	 */
	public int getSender() 
	{
		return playerIndex;
	}

	/**
	 * Set the sender of the trade
	 * @param sender
	 */
	public void setSender(int playerIndex) 
	{
		this.playerIndex = playerIndex;
	}

	/**
	 * Get the receiver of the trade
	 * @return
	 */
	public int getReceiver() 
	{
		return receiver;
	}

	/**
	 * Set the receiver of the trade
	 * @param receiver
	 */
	public void setReceiver(int receiver) 
	{
		this.receiver = receiver;
	}

	/**
	 * Get the offered trade
	 * @return
	 */
	public ResourceList getOffer() 
	{
		return offer;
	}

	/**
	 * Set the offered trade
	 * @param offer
	 */
	public void setOffer(ResourceList offer) 
	{
		this.offer = offer;
	}
}
