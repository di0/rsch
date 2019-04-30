package com.develdio.remotesearch.shared.helper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.develdio.remotesearch.i18n.Message;
import com.develdio.remotesearch.shared.Property;

/**
 * This class provides methods that helps perform tasks related
 * with properties file.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.1
 */
public class PropertiesFileHelper
{
	private enum PROPERTY_FILE_ENTRY
	{
		SERVER, DIRECTORY, USER, PASSWORD, HOSTNAME;

		@Override
		public String toString()
		{
			return this.name();
		}
	}

	/**
	 * Reads contents from an input stream according name of the
	 * file given.
	 *
	 * @param filename
	 *            from where will be read.
	 * @return the contents read
	 * @throws Exception
	 *             if could not read the given file
	 */
	public final static String
	readFileContents( final String filename ) throws Exception
	{
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;

		try {
			bufferedReader = new BufferedReader( new FileReader(
					filename ) );
			int line = -1;
			while ( ( line = bufferedReader.read() ) != -1 )
				stringBuilder.append( (char) line );
		}
		catch( IOException e )
		{
			throw new Exception( e.getMessage() );
		}
		finally
		{
			if ( bufferedReader != null )
			{
				try {
					bufferedReader.close();
				} catch ( IOException ignored ) {
				}
			}
		}

		return stringBuilder.toString();
	}

	/**
	 * Creates a Map menu with profile name as key of map and a
	 * list of Property as values.
	 *
	 * @param contentsFromFile
	 *            stream with contents of file properties
	 * @return a map with profile key and a respective list of
	 *            property theirs
	 * @throws Exception
	 *             If is not possible to parser the file properties
	 */
	public final static Map<String, List<Property>>
	convertFileToPropertiesObjectMap( final String contentsFromFile )
			throws Exception
	{
		Map<String, List<Property>> propertiesMap = new HashMap<>();
		List<Property> propertiesList = new ArrayList<>();

		Object stubPropertiesObject = createInstance();

		String profile = "";

		Pattern patternCompile = Pattern.compile(
				"\\s*(\\S+)\\s*=\\s*(\\S+\\s*,?\\s*\\S+\\s*)\\s*" );

		BufferedReader br = new BufferedReader(
				new InputStreamReader( new ByteArrayInputStream(
						contentsFromFile.getBytes() ) ) );
		String lineString = "";
		while ( ( lineString = br.readLine() ) != null )
		{
			// We have a commented line, so let's go ignore it.
			if ( lineString.matches( "\\s*#\\s*.*\\S*" ) )
				continue;

			// We have a new profile, so let's go create a new key
			// on map.
			if ( ( lineString.contains( "[") ) &&
					( lineString.contains( "]" ) ) )
			{
				propertiesList = new ArrayList<>();
				stubPropertiesObject = (Property) createInstance();
				propertiesList.add( (Property) stubPropertiesObject );
				profile = lineString.substring( ( lineString.
						indexOf( '[' ) + 1 ),
						lineString.indexOf( ']' ) );
				propertiesMap.put( profile, propertiesList );
				continue;
			}

			// We have an another property entries split by "-", let's
			// go create a new object Property and to add on current Map
			// the profile key.
			if ( lineString.matches( "\\s*-" ) )
			{
				stubPropertiesObject = (Property) createInstance();
				propertiesList.add( (Property) stubPropertiesObject );
				propertiesMap.put( profile, propertiesList );
				continue;
			}

			Matcher matcher = patternCompile.matcher( lineString );
			while ( matcher.find() )
			{
				if ( ! isValidKey( matcher.group( 1 ) ) )
				{
					throw new IOException( Print.translateOut(
							Message.KEY_IS_NOT_ALLOWED,
							matcher.group( 1 ) ) );
				}

				Method method = createMethod( stubPropertiesObject,
						createNameFromMethodSet( matcher.group( 1 ) ),
						String.class );

				// Here we have a value with multiples directories
				// separated by comma.
				if ( PROPERTY_FILE_ENTRY.DIRECTORY.toString().
						equalsIgnoreCase( matcher.group( 1 ) ) &&
						matcher.group( 2 ).contains( "," ) )
				{
					String[] multiplesDirectories = matcher.group( 2 ).
							split( "\\s*,\\s*" );
					int i;
					for ( i = 0; i < multiplesDirectories.length; i++ )
						invokeMethodWithParameters( stubPropertiesObject,
								method, multiplesDirectories[ i ] );
				}
				else
					invokeMethodWithParameters( stubPropertiesObject,
							method, matcher.group( 2 ) );
			}
		}

		return propertiesMap;
	}

	private static Object
	createInstance() throws Exception
	{
		Class<?> clazz = Property.class;
		try {
			return clazz.newInstance();
		} catch ( InstantiationException |
				IllegalAccessException e ) {
			throw new Exception( e.getMessage() );
		}
	}

	private static String
	createNameFromMethodSet( String methodName )
	{
		return ( "set" + methodName
			.substring( 0, 1 ).toUpperCase() +
			methodName.substring( 1 ).toLowerCase() );
	}

	private static Method
	createMethod( Object stubPropertiesObject,
			String methodName, Class<?> type ) throws Exception
	{
		try {
			return stubPropertiesObject.getClass().
					getMethod( methodName, type );
		} catch ( NoSuchMethodException | SecurityException e ) {
			throw new Exception( e.getMessage() );
		}
	}

	private static void
	invokeMethodWithParameters( Object stubPropertiesObject,
			Method method, String parameters )
			throws Exception
	{
		try {
			method.invoke( stubPropertiesObject, parameters );
		} catch ( IllegalAccessException |
				IllegalArgumentException |
				InvocationTargetException e )
		{
			throw new Exception( e.getMessage() );
		}
	}

	private static boolean
	isValidKey( String key )
	{
		for ( PROPERTY_FILE_ENTRY propertyEntry :
			PROPERTY_FILE_ENTRY.values() )
		{
			if ( key.equalsIgnoreCase(
					propertyEntry.toString() ) )
				return true;
		}
		return false;
	}
}
