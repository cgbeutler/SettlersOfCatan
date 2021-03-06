package client.controller.map;

import java.io.IOException;
import java.util.*;

import org.apache.log4j.Logger;

import shared.CanCan;
import shared.CatanModel;
import shared.Hex;
import shared.Player;
import shared.Port;
import shared.TurnType;
import shared.definitions.*;
import shared.locations.*;
import client.CatanGame;
import client.view.base.*;
import client.view.data.*;
import client.view.map.IMapView;
import client.view.map.IRobView;


/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController, Observer
{
	private static final Logger log = Logger.getLogger(MapController.class.getName());
	
	private CatanModel catanModel = null;
	private IRobView robView;
	private CatanGame catanGame;
	//private boolean playingRoadBuildingCard;
	//private int numRoadsPlaced;
	private int playerIndex;
	private CatanColor playerColor;
	private TurnType mapState;
	private Player player;
	private boolean isBuilding;
	private HexLocation robber;
	
	private EdgeLocation firstRoad;
	private EdgeLocation secondRoad;
	
	public MapController(CatanGame catanGame, IMapView view, IRobView robView) 
	{
		super(view);
		
		this.catanGame = catanGame;
		
		//initFromModel();

		//playingRoadBuildingCard = false;
		catanGame.addObserver(this);
		setRobView(robView);
		isBuilding = false;
		robber = null;
	}
	
	@Override
	public IMapView getView() {
		
		return (IMapView)super.getView();
	}
	
	private IRobView getRobView() {
		return robView;
	}
	
	private void setRobView(IRobView robView) {
		this.robView = robView;
	}
	
	private CatanColor getColor()
	{
		return playerColor;
	}
	
	private void setColor(CatanColor color)
	{
		playerColor = color;
	}
	
	
	
	protected void initFromModel() 
	{
		//initFromModel gets the initCatanModel, adds the hexes, numbers, and ports
		for (int i = 0; i < catanModel.getMap().getHexes().size(); i++)
		{
			Hex hex = catanModel.getMap().getHexes().get(i);
			if (catanModel.getMap().getHexes().get(i).getResource() == null)
			{
				// desert
				getView().addHex(hex.getLocation(), HexType.DESERT);
				getView().placeRobber(hex.getLocation());
			}
			else
			{
				// not desert
				getView().addHex(hex.getLocation(), hex.getResource());
				getView().addNumber(hex.getLocation(), hex.getNumber());
			}
			getView().addHex(new HexLocation(-3, 3), HexType.WATER);
			getView().addHex(new HexLocation(-3, 2), HexType.WATER);
			getView().addHex(new HexLocation(-3, 1), HexType.WATER);
			getView().addHex(new HexLocation(-3, 0), HexType.WATER);
			getView().addHex(new HexLocation(-2, -1), HexType.WATER);
			getView().addHex(new HexLocation(-2, 3), HexType.WATER);
			getView().addHex(new HexLocation(-1, 3), HexType.WATER);
			getView().addHex(new HexLocation(-1, -2), HexType.WATER);
			getView().addHex(new HexLocation(0, 3), HexType.WATER);
			getView().addHex(new HexLocation(0, -3), HexType.WATER);
			getView().addHex(new HexLocation(1, -3), HexType.WATER);
			getView().addHex(new HexLocation(1, 2), HexType.WATER);
			getView().addHex(new HexLocation(2, 1), HexType.WATER);
			getView().addHex(new HexLocation(2, -3), HexType.WATER);
			getView().addHex(new HexLocation(3, -3), HexType.WATER);
			getView().addHex(new HexLocation(3, -2), HexType.WATER);
			getView().addHex(new HexLocation(3, -1), HexType.WATER);
			getView().addHex(new HexLocation(3, 0), HexType.WATER);
		}
		
		for (int i = 0; i < catanModel.getMap().getPorts().size(); i++)
		{
			Port port = catanModel.getMap().getPorts().get(i);
			if (port.getType() == null) {
				// Three port
				getView().addPort(new EdgeLocation(port.getLocation().getX(), port.getLocation().getY(), port.getDirection()), PortType.THREE);
			}
			else
			{
				// Two port
				getView().addPort(new EdgeLocation(port.getLocation().getX(), port.getLocation().getY(), port.getDirection()), port.getType());
			}
		}

	}
	
	private void updateFromModel()
	{
		setColor(catanModel.getPlayers()[playerIndex].getColor());
		
		for (int i = 0; i < catanModel.getMap().getRoads().size(); i++)
			for (int j = 0; j < catanModel.getPlayers().length; j++)
				if (catanModel.getPlayers()[j].getPlayerIndex() == catanModel.getMap().getRoads().get(i).getOwner())
					getView().placeRoad(new EdgeLocation(catanModel.getMap().getRoads().get(i).getLocation().getX(), catanModel.getMap().getRoads().get(i).getLocation().getY(), catanModel.getMap().getRoads().get(i).getLocation().getDir()), catanModel.getPlayers()[j].getColor());
		
		for (int i = 0; i < catanModel.getMap().getSettlements().size(); i++)
			for (int j = 0; j < catanModel.getPlayers().length; j++)
				if (catanModel.getPlayers()[j].getPlayerIndex() == catanModel.getMap().getSettlements().get(i).getOwner())
					getView().placeSettlement(catanModel.getMap().getSettlements().get(i).getLocation(), catanModel.getPlayers()[j].getColor());
		
		for (int i = 0; i < catanModel.getMap().getCities().size(); i++)
			for (int j = 0; j < catanModel.getPlayers().length; j++)
				if (catanModel.getPlayers()[j].getPlayerIndex() == catanModel.getMap().getCities().get(i).getOwner())
					getView().placeCity(catanModel.getMap().getCities().get(i).getLocation(), catanModel.getPlayers()[j].getColor());
		
		getView().placeRobber(new HexLocation(catanModel.getMap().getRobber().getX(), catanModel.getMap().getRobber().getY()));
			
	}

	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) 
	{
		return CanCan.canBuildRoad(player, edgeLoc, catanModel.getTurnTracker(), catanModel.getMap());
	}

	@Override
	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		
		return CanCan.canBuildSettlement(player, vertLoc, catanModel.getTurnTracker(), catanModel.getMap());
	}

	@Override
	public boolean canPlaceCity(VertexLocation vertLoc) {
		
		return CanCan.canBuildCity(player, vertLoc, catanModel.getTurnTracker(), catanModel.getMap());
	}

	@Override
	public boolean canPlaceRobber(HexLocation hexLoc) {
		Hex robberHex = null;
		
		for (int i = 0; i < catanModel.getMap().getHexes().size(); i++)
			if (hexLoc.getX() == catanModel.getMap().getHexes().get(i).getLocation().getX() && hexLoc.getY() == catanModel.getMap().getHexes().get(i).getLocation().getY())
				robberHex = catanModel.getMap().getHexes().get(i);
		
//		System.out.println("canPlaceRobber in MapController: " + catanModel.getTurnTracker().toString());
		return CanCan.canPlaceRobber(robberHex, catanModel.getMap().getRobber(), catanModel.getTurnTracker(), player);
	}

	@Override
	public void placeRoad(EdgeLocation edgeLoc) 
	{
		setColor(catanModel.getPlayers()[playerIndex].getColor());
		if (player.getIsRoadBuilding())
		{
			if (firstRoad == null){
				firstRoad = edgeLoc;
				catanGame.getModel().getMap().buildRoad(playerIndex, firstRoad);
				getView().placeRoad(firstRoad, playerColor);	
				getView().startDrop(PieceType.ROAD, playerColor, false);
			}
			else if (secondRoad == null)
				secondRoad = edgeLoc;
			
			if (firstRoad != null && secondRoad != null)
			{
				try {
					catanGame.getProxy().movesRoadBuilding(playerIndex, firstRoad, secondRoad);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				firstRoad = null;
				secondRoad = null;
				player.setIsRoadBuilding(false);
			}
		}
		else
		{
			try 
			{
				catanGame.updateModel(catanGame.getProxy().movesBuildRoad(playerIndex, edgeLoc, (mapState == TurnType.FIRST_ROUND || mapState == TurnType.SECOND_ROUND)));
			} catch (IOException e) 
			{
				e.printStackTrace();
			}
		}
	}

	@Override
	public void placeSettlement(VertexLocation vertLoc) 
	{
		try 
		{
			if (mapState == TurnType.FIRST_ROUND || mapState == TurnType.SECOND_ROUND)
			{
				catanGame.getProxy().movesBuildSettlement(playerIndex, vertLoc, true);
				catanGame.updateModel(catanGame.getProxy().movesFinishTurn(playerIndex));
				updateState();
			}
			else
			{
				catanGame.updateModel(catanGame.getProxy().movesBuildSettlement(playerIndex, vertLoc, false));
			}
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public void placeCity(VertexLocation vertLoc) 
	{
		try 
		{
			catanGame.updateModel(catanGame.getProxy().movesBuildCity(playerIndex, vertLoc, false));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	public RobPlayerInfo[] getVictims(int player,HexLocation spot) {
		VertexLocation []vertices={new VertexLocation(spot.getX(), spot.getY() ,VertexDirection.NorthEast),new VertexLocation(spot.getX(), spot.getY() ,VertexDirection.NorthWest),
								new VertexLocation(spot.getX() - 1, spot.getY() + 1 ,VertexDirection.NorthEast),new VertexLocation(spot.getX() + 1, spot.getY() ,VertexDirection.NorthWest),
								new VertexLocation(spot.getX(), spot.getY() + 1,VertexDirection.NorthEast),new VertexLocation(spot.getX(), spot.getY() + 1,VertexDirection.NorthWest)};

		List<RobPlayerInfo> vics=new ArrayList<RobPlayerInfo>();
		VertexLocation building;
		int owner;
		for(int i=0;i<vertices.length;i++){
			for(int s=0;s<catanModel.getMap().getSettlements().size();s++){
				building=catanModel.getMap().getSettlements().get(s).getLocation().getNormalizedLocation();
				owner=catanModel.getMap().getSettlements().get(s).getOwner();
				if(building.equals(vertices[i]) && owner!=player && catanModel.getPlayers()[owner].getResources().totalCount()>0)
				{

						RobPlayerInfo ri=new RobPlayerInfo();
						ri.setNumCards(catanModel.getPlayers()[owner].getResources().totalCount());
						ri.setName(catanModel.getPlayers()[owner].getName());
						ri.setPlayerIndex(owner);
						ri.setColor(catanModel.getPlayers()[owner].getColor());
						vics.add(ri);
				}
			}
			for(int c=0;c<catanModel.getMap().getCities().size();c++){
				building=catanModel.getMap().getCities().get(c).getLocation().getNormalizedLocation();
				owner=catanModel.getMap().getCities().get(c).getOwner();
				if(building.equals(vertices[i]) && owner!=player && catanModel.getPlayers()[owner].getResources().totalCount()>0)
				{
						RobPlayerInfo ri=new RobPlayerInfo();
						ri.setNumCards(catanModel.getPlayers()[owner].getResources().totalCount());
						ri.setName(catanModel.getPlayers()[owner].getName());
						ri.setPlayerIndex(owner);
						ri.setColor(catanModel.getPlayers()[owner].getColor());
						vics.add(ri);
				}
			}
		}
		
		List<RobPlayerInfo> vicsClean = new ArrayList<RobPlayerInfo>();
		
		for(int i = 0; i < vics.size(); i++)
		{
			boolean isUnique = true;
			
			for (int j = 0; j < vics.size(); j++)
			{
				if (vics.get(i).getPlayerIndex() == vics.get(j).getPlayerIndex() && i != j)
					isUnique = false;
			}
			
			if (isUnique)
				vicsClean.add(vics.get(i));
		}
		
		if(vicsClean == null || vicsClean.size()==0)
			return null;
		
		return vicsClean.toArray(new RobPlayerInfo[vicsClean.size()]);
	}
	
	@Override
	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) 
	{
		if (catanModel.getTurnTracker().getStatus().equals(TurnType.FIRST_ROUND) || catanModel.getTurnTracker().getStatus().equals(TurnType.SECOND_ROUND))
			allowDisconnected = false;
		else
			allowDisconnected = true;
		getView().startDrop(pieceType, getColor(), allowDisconnected);
	}
	
	@Override
	public void cancelMove() 
	{
		
	}
	
	@Override
	public void playSoldierCard() 
	{
		// Checks handled in DevCardController
		player.setIsPlayingSoldier(true);
		getView().startDrop(PieceType.ROBBER, playerColor, true);
//		if(catanModel.getTurnTracker().getStatus().equals(TurnType.ROBBING) && catanModel.getTurnTracker().getCurrentTurn() == playerIndex){
//			getView().startDrop(PieceType.ROBBER, playerColor, false);
//		}
	}
	
	@Override
	public void playRoadBuildingCard() 
	{	
		player.setIsRoadBuilding(true);
		getView().startDrop(PieceType.ROAD, playerColor, true);		
	}

	@Override
	public void placeRobber(HexLocation hexLoc) 
	{
		log.trace("Placing robber on hex");
		
		getView().placeRobber(hexLoc);
		getRobView().setPlayers(getVictims(playerIndex, hexLoc));
		log.trace("Showing rob view modal --\\");
		getRobView().showModal();
		robber = hexLoc;
		player.setIsPlayingSoldier(false);

	}
	
	@Override
	public void robPlayer(RobPlayerInfo victim) 
	{
		try 
		{
			if(catanModel.getTurnTracker().getStatus().equals(TurnType.ROBBING))
			{
					if(victim!=null)
					{
						catanGame.updateModel(catanGame.getProxy().movesRobPlayer(playerIndex, victim.getPlayerIndex(), robber));
						updateState();
					}
					else
					{
						catanGame.updateModel(catanGame.getProxy().movesRobPlayer(playerIndex, -1, robber));
						updateState();
					}
			}
			else
			
			{
				if(victim!=null)
				{
					catanGame.updateModel(catanGame.getProxy().movesSoldier(playerIndex, victim.getPlayerIndex(), robber));
				}
				else
				{
					catanGame.updateModel(catanGame.getProxy().movesSoldier(playerIndex, - 1, robber));
				}
			}
			
			robber=null;
		}
		 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateState()
	{
		switch(catanModel.getTurnTracker().getStatus()){
		case FIRST_ROUND:
			mapState = TurnType.FIRST_ROUND;
			break;
		case SECOND_ROUND:
			mapState = TurnType.SECOND_ROUND;
			break;
		case ROLLING:
			mapState = TurnType.ROLLING;
			break;
		case ROBBING:
			mapState = TurnType.ROBBING;
			break;
		case PLAYING:
			mapState = TurnType.PLAYING;
			break;
		case DISCARDING:
			mapState = TurnType.DISCARDING;
			break;
		default:
			log.error("Somthing has gone terribly, horribly wrong in Update() in mapController.java");
			break;
		}		
	}

	@Override
	public void update(Observable obs, Object obj) 
	{
		if (obs instanceof CatanGame) 
		{
			if (catanModel == null)
			{
				log.trace("Initializing model");
				catanModel = ((CatanGame) obs).getModel();
				initFromModel();
			}
//			else 
//			{
				log.trace("Updating current view");
				setColor(catanModel.getPlayers()[playerIndex].getColor());
				playerIndex = catanGame.getPlayerInfo().getPlayerIndex();
				player = catanModel.getPlayers()[playerIndex];
				
				//System.out.println("Updating model");
				catanModel = ((CatanGame) obs).getModel();
				updateState();
				updateFromModel();
				
				if(catanModel.getTurnTracker().getStatus().equals(TurnType.ROBBING) && catanModel.getTurnTracker().getCurrentTurn() == playerIndex){
					getView().startDrop(PieceType.ROBBER, playerColor, false);
				}
				
				if (player != null && playerIndex == catanModel.getTurnTracker().getCurrentTurn())
				{
					log.trace("Game state: " + mapState.toString());
//					if ((player.getRoads() == 14 && player.getSettlements() == 4) && mapState.equals(TurnType.FIRST_ROUND))
//					{
//						try {
//							catanGame.updateModel(catanGame.getProxy().movesFinishTurn(playerIndex));
//							updateState();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
//					else if ((player.getRoads() == 13 && player.getSettlements() == 3) && mapState.equals(TurnType.SECOND_ROUND))
//					{
//						try {
//							catanGame.updateModel(catanGame.getProxy().movesFinishTurn(playerIndex));
//							updateState();
//						} catch (IOException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
					
					if (player.getRoads() == 15 && player.getSettlements() == 5 && mapState.equals(TurnType.FIRST_ROUND))
					{ 
						if (!getView().isDropping())
							startMove(PieceType.ROAD, true, false);
					}
					else if (player.getRoads() == 14 && player.getSettlements() == 5 && mapState.equals(TurnType.FIRST_ROUND))
					{
						if (!getView().isDropping())
							startMove(PieceType.SETTLEMENT, true, false);
					}
					else if (player.getRoads() == 14 && player.getSettlements() == 4 && mapState.equals(TurnType.SECOND_ROUND))
					{
						if (!getView().isDropping())
							startMove(PieceType.ROAD, true, false);
					}
					else if (player.getRoads() == 13 && player.getSettlements() == 4 && mapState.equals(TurnType.SECOND_ROUND))
					{ 
						if (!getView().isDropping())
							startMove(PieceType.SETTLEMENT, true, false);
					}
				}
				
//			}
		}
		
	}
	
}

