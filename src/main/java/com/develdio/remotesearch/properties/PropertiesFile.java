package com.develdio.remotesearch.properties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.develdio.remotesearch.exception.PropertiesFileException;
import com.develdio.remotesearch.shared.Property;
import com.develdio.remotesearch.shared.helper.PropertiesFileHelper;

/**
 * This class provides mechanism that configure a file properties and
 * loads it into a data structure.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.1
 */
public class PropertiesFile
{
	private static Map<String, List<Property>> propertiesMap =
			new HashMap<>();

	/**
	 * We want use the factory static method by conversion.
	 */
	private PropertiesFile( final String filename )
			throws PropertiesFileException
	{
		try
		{
			String contents = PropertiesFileHelper.
					readFileContents( filename );
			propertiesMap = PropertiesFileHelper.
					convertFileToPropertiesObjectMap( contents );
		}
		catch( Exception e )
		{
			throw new PropertiesFileException( e.getMessage() );
		}
	}

	/**
	 * This static method creates the properties from target file
	 * specified by name and give back the new created instance.
	 *
	 * @param filename
	 *            name of the file target that will be created
	 * @return the new instance to this properties
	 * @throws PropertiesFileException
	 *             if cannot by some reason, loads the file
	 */
	public static PropertiesFile
	createPropertiesFromfile( final String filename )
			throws PropertiesFileException
	{
		return new PropertiesFile( filename );
	}

	/** @return a map which the key has bind to a list of
	 *  properties. */
	public Map<String, List<Property>>
	getPropertiesValues()
	{
		return propertiesMap;
	}
}
