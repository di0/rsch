package com.develdio.remotesearch.shared.helper;

import java.util.List;
import java.util.Map;

import com.develdio.remotesearch.shared.Info;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class plays with JSON Notation, providing the final user the possibility
 * of writes to standard output the JSON format.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.1
 */
public final class JSONNotation
{
	/**
	 * This class provides a map with list of Info, into String.
	 *
	 * @param mapListInfo
	 *            map with list of info that will be writen into String
	 * @return the String with Info data
	 */
	public static String
	notationInfo( final Map<String, List<Info>> mapListInfo )
	{
		String out = "";
		try
		{
			out = new ObjectMapper().writeValueAsString( mapListInfo );
		}
		catch( JsonProcessingException ignored )
		{
			return "";
		}

		return out;
	}
}