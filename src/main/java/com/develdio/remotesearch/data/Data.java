package com.develdio.remotesearch.data;

/**
 * This class encapsulates data structure send from client side to server side
 * and vice-versa.
 *  
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class Data
{
	/** Data in format text */
	private String text = "";

	/** Origin from data */
	private String origin = "";

	/**
	 * Stores data converted in text.
	 * @param text
	 *            dados em forma de texto.
	 */
	public void text( String text )
	{
		this.text = text;
	}

	/**
	 * Gets data converted in text.
	 *
	 * @return data in converted text.
	 */
	public String getText()
	{
		return text;
	}

	/**.
	 * Configures the origin data.
	 *
	 * @param origin
	 *            The data origin information
	 */
	public void addOrigin( String origin )
	{
		this.origin = origin;
	}

	/**
	 * @return origin data.
	 */
	public String getOrigin()
	{
		return origin;
	}
}
