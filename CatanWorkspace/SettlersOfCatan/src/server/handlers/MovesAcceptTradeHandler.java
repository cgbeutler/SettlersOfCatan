package server.handlers;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;

import server.Server;
import server.ServerGame;
import server.comm.response.AbstractResponse;
import server.comm.response.JsonResponse;
import server.comm.response.MessageResponse;
import shared.comm.ServerException;
import shared.comm.serialization.AcceptTradeRequest;

public class MovesAcceptTradeHandler extends SimpleHandler
{
	private static final Logger log = Logger.getLogger(MovesAcceptTradeHandler.class);
	
	private Server server = null;

	public MovesAcceptTradeHandler(Server server)
	{
		this.server = server;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException
	{
		AbstractResponse response = null;
		
		log.debug("/moves/sendChat begun");
		
		log.trace("Verifying user credentials");
		try
		{
			verifyUser(exchange, server);
			log.trace("User is valid");

			log.trace("creating request body object");
			AcceptTradeRequest request = getRequest(exchange, AcceptTradeRequest.class);
			
			ServerGame game = getGame(exchange, server);
			
			game.getMovesFacade().acceptTrade(request);
			
			log.trace("Chat message added to model");
			response = new JsonResponse(200);
			((JsonResponse)response).setJsonBody(game.getModel());
		}
		catch (ServerException e)
		{
			response = new MessageResponse(400, e.getMessage());
		}

		log.trace("Adding response headers and cookies");
		addResponseHeaders(exchange, response);
		
		log.trace("Sending response");
		sendResponse(exchange, response);
		
		log.trace("/moves/sendChat finished");
	}

}
