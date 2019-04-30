package com.develdio.remotesearch.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class acts as container to data exchanged between client side
 * and server side and, vice-versa.
 * 
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 * @see com.develdio.remotesearch.data.Data
 */
public class DataContainer
{
	/** Stored data */
	private List<Data> messages = Collections.
			synchronizedList( new ArrayList<Data>() );

	/**
	 * Adds one data into container.
	 * 
	 * @param data
	 *            that should stored
	 */
	public void add( Data data )
	{
		messages.add( data );
	}

	/**
	 * @return all data into list container.
	 */
	public List<Data> getAllData()
	{
		return messages;
	}
}
