package server.handlers;

import org.apache.log4j.Logger;

import server.Server;
import server.comm.response.MessageResponse;
import shared.comm.serialization.CredentialsRequest;

public class UserLoginHandler extends UserHandler
{
	private static final Logger log = Logger.getLogger(UserLoginHandler.class);

	public UserLoginHandler(Server server)
	{
		super(server);
	}

	@Override
	protected MessageResponse handleCredentials(CredentialsRequest request)
	{
		log.trace("Verifying user credentials");
		MessageResponse response = server.getServerLobby().getUserFacade().login(request);
		log.trace("User validation result : " + (response != null));
		return response;
	}

}