package server.handlers;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.sun.net.httpserver.HttpExchange;

import client.view.data.GameInfo;
import server.Server;
import server.comm.response.AbstractResponse;
import server.comm.response.JsonResponse;
import server.comm.response.MessageResponse;
import server.models.ServerUser;
import shared.comm.ServerException;
import shared.comm.cookie.GameCookie;
import shared.comm.serialization.JoinGameRequest;
import shared.comm.serialization.LoadGameRequest;

public class GamesLoadHandler extends SimpleHandler
{
	private static final Logger log = Logger.getLogger(GamesJoinHandler.class);
	
	private Server server = null;

	public GamesLoadHandler(Server server)
	{
		this.server = server;
	}

	@Override
	public void handle(HttpExchange exchange) throws IOException
	{
		try{
			AbstractResponse response = null;
			LoadGameRequest request = null;
			
			log.debug("/games/load begun");
			
			log.trace("Verifying user credentials");
			try
			{
				ServerUser user = verifyUser(exchange, server);
				log.trace("User is valid. joining game");
	
				log.trace("creating request body object");
				request = getRequest(exchange, LoadGameRequest.class);
				
				GameInfo game = server.getServerLobby().getGamesFacade().load(request);
				log.trace("Loaded game #" + game.getId() + "\"" + game.getTitle() + "\"");
				
				// The spec says to return the info, but their server returns success
				response = new MessageResponse(200, "Success");
//				response = new JsonResponse(200);
//				((JsonResponse)response).setJsonBody(game, JsonResponse.GAME_INFO_TYPE);
				
				response.addCookie(new GameCookie(game.getId()));
			}
			catch (ServerException e)
			{
				response = new MessageResponse(400, e.getMessage());
			}
	
			log.trace("Adding response headers and cookies");
			addResponseHeaders(exchange, response);
			
			log.trace("Sending response");
			sendResponse(exchange, response);
			
			log.trace("/games/load finished");
		}
		catch (Exception e){
			e.printStackTrace();
			throw(e);
		}
	}

}
