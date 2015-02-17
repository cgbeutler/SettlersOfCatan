package client.comm;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import client.comm.ServerProxy;

import com.google.gson.Gson;

import shared.comm.serialization.GameResponse;
import shared.comm.serialization.PlayerResponse;
import shared.ResourceList;
import shared.TradeOffer;
import shared.definitions.CatanColor;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

public class ServerProxyTest
{
	
	ServerProxy proxy;
	Gson gson = new Gson();

	@Before
	public void setUp() throws Exception
	{
		proxy = new ServerProxy("http://localhost:8081");
	}

	@After
	public void tearDown() throws Exception
	{
		
	}
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testUser() throws IOException
	{
		proxy.userRegister("JJ", "JJ");
		proxy.userRegister("Jeff", "Jeff");
		proxy.userRegister("Jordan", "Jordan");
		proxy.userRegister("Cory", "Cory");
		
		proxy.userLogin("Cory", "Cory");
		proxy.userLogin("Sam", "sam");

	    exception.expect(IOException.class);
		proxy.userRegister("Cory", "Cory");
		proxy.userLogin("fakeman", "jake");
	}
	
	@Test
	public void testGames() throws IOException
	{
		proxy.userRegister("James", "Franco");

		GameResponse[] games = proxy.gamesList();
		assertEquals("There should be 3 games", games.length, 3);
		
		GameResponse game = proxy.gamesCreate("Coolio Game Yo", true, true, true);
		
		assertTrue("Game title comparison", game.getTitle().equals("Coolio Game Yo"));
		assertEquals("Should be the third game on the server", game.getId(), 3);
		for (PlayerResponse player : game.getPlayers())
		{
			assertEquals(player.getId(), -1);
			assertTrue(player.getName() == null);
			assertTrue(player.getColor() == null);
		}
		
		games = proxy.gamesList();
		assertEquals("There should be 4 games now", games.length, 4);
		
		proxy.gamesJoin(CatanColor.BLUE, 3);
	}
	
	@Test
	public void testMoves() throws IOException
	{
		proxy.userLogin("Sam", "sam");
		proxy.gamesJoin(CatanColor.ORANGE, 0);
		
		proxy.gameModel();
		testMovesSendChat();
		testMovesRollNumber(0,7);
		testMovesRobPlayer();
		testMovesBuyDevCard(0);
		testMovesYearOfPlenty(0);
		testMovesFinishTurn(0);
		
		testMovesRollNumber(1,4);
		testMovesBuyDevCard(1);
		testMovesSoldier(1);
		testMovesFinishTurn(1);
		
		testMovesRollNumber(2,8);
		testMovesBuyDevCard(2);
		testMovesMonument(2);
		testMovesFinishTurn(2);
		
		testMovesRollNumber(3,2);
		testMovesBuyDevCard(3);
		testMovesMonopoly(3);
		testMovesFinishTurn(3);
		
		testMovesRollNumber(0,5);
		testMovesBuyDevCard(0);
		testMovesRoadBuilding();
		testMovesBuildRoad();
		testMovesBuildSettlement();
		testMovesBuildCity();
		testMovesOfferTrade();
		testMovesAcceptTrade();
		testMovesMaritimeTrade();
		testMovesDiscardCards();
		
		testMovesFinishTurn(0);		
	}
	
	public void testGamesSaving() throws IOException
	{
		
	}
	
	public void testGameModel() throws IOException
	{
		
	}
	
	public void testGameReset() throws IOException
	{
		
	}
	
	public void testGameCommandsPost() throws IOException
	{
		
	}
	
	public void testGameCommandsGet() throws IOException
	{
		
	}
	
	public void testGameAddAI() throws IOException
	{

	}
	
	public void testGameListAI() throws IOException
	{
		
	}

	public void testMovesSendChat() throws IOException
	{
		proxy.movesSendChat(0, "GUINEA PIGS");
	}
	
	public void testMovesRollNumber(int playerIndex, int roll) throws IOException
	{
		proxy.movesRollNumber(playerIndex, roll);
	}
	
	public void testMovesRobPlayer() throws IOException
	{
		proxy.movesRobPlayer(0, 1, new HexLocation(-2, 0));
	}

	public void testMovesFinishTurn(int playerIndex) throws IOException
	{
		proxy.movesFinishTurn(playerIndex);
	}
	
	public void testMovesBuyDevCard(int playerIndex) throws IOException
	{
		proxy.movesBuyDevCard(playerIndex);
	}
	
	public void testMovesYearOfPlenty(int playerIndex) throws IOException
	{
		proxy.movesYearOfPlenty(playerIndex, ResourceType.BRICK, ResourceType.ORE);
	}
	
	public void testMovesRoadBuilding() throws IOException
	{
		proxy.movesRoadBuilding(0, new EdgeLocation(new HexLocation(1,0), EdgeDirection.South), new EdgeLocation(new HexLocation(1,1), EdgeDirection.SouthWest));
	}
	
	public void testMovesSoldier(int playerIndex) throws IOException
	{
		proxy.movesSoldier(playerIndex, 1, new HexLocation(-2, 1));
	}

	public void testMovesMonopoly(int playerIndex) throws IOException
	{
		proxy.movesMonopoly(playerIndex, ResourceType.WOOD);
	}
	
	public void testMovesMonument(int playerIndex) throws IOException
	{
		proxy.movesMonument(playerIndex);
	}
	
	public void testMovesBuildRoad() throws IOException
	{
		proxy.movesBuildRoad(0, new EdgeLocation(new HexLocation(-1,1),EdgeDirection.SouthEast), true);
	}
	
	public void testMovesBuildSettlement() throws IOException
	{
		proxy.movesBuildSettlement(0, new VertexLocation(new HexLocation(0,2), VertexDirection.West), true);
	}
	
	public void testMovesBuildCity() throws IOException
	{
		proxy.movesBuildCity(0, new VertexLocation(new HexLocation(1,1), VertexDirection.West), true);
	}
	
	public void testMovesOfferTrade() throws IOException
	{
		TradeOffer trade = new TradeOffer(0,new ResourceList(0,0,0,0,0),1);
		proxy.movesOfferTrade(trade);
	}
	
	public void testMovesAcceptTrade() throws IOException
	{
		proxy.movesAcceptTrade(1, true);
	}
	
	public void testMovesMaritimeTrade() throws IOException
	{
		proxy.movesMaritimeTrade(0, 3, ResourceType.BRICK, ResourceType.WOOD);
	}

	public void testMovesDiscardCards() throws IOException
	{
		proxy.movesDiscardCards(0, new ResourceList(0,0,-1,0,0)); 
	}
	
	public void testUtilChangeLogLevel() throws IOException
	{
		
	}

}