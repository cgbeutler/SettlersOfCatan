package client.comm;

import java.io.IOException;

import client.controller.join.JoinGameController;

/**
 * Server Poller polls the game model from the server
 * every few seconds and uses that to update the model
 * held locally.
 * @author Cory Beutler
 *
 */
public class LobbyPoller extends AbstractPoller
{
	JoinGameController joinGameController;
	
	boolean _gameRunning = true;
	
    public LobbyPoller(JoinGameController joinGameController)
    {
    	super(4000);
    	this.joinGameController = joinGameController;
    		
    }

    @Override
    public void poll() throws IOException
    {
    	joinGameController.updateGameList();
    }
	        	
}
