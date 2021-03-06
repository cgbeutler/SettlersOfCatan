package client.view.join;

import client.view.base.*;
import shared.definitions.*;

/**
 * Interface for the select color view, which lets the user select the desired
 * color when they join a game
 */
public interface ISelectColorView extends IOverlayView
{
	
	/**
	 * Enables or disables the specified color. Colors that are already taken by
	 * other users should be disabled. Available colors should be enabled.
	 * 
	 * @param color
	 *            The color being enabled or disabled
	 * @param enable
	 *            Whether or not the color should be enabled
	 */
	void setColorEnabled(CatanColor color, boolean enable);
	
	/**
	 * Returns the color selected by the user
	 * 
	 * @return The color selected by the user
	 */
	CatanColor getSelectedColor();
	
	/**
	 * Sets the selected color
	 * 
	 * @param color
	 * 			  The color being selected
	 */
	void setSelectedColor(CatanColor color);
	
	/**
	 * Reset the dialog to be unselected
	 */
	void reset();
}

