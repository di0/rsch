package com.develdio.remotesearch.shared.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.develdio.remotesearch.data.Data;
import com.develdio.remotesearch.shared.Info;

/**
 * This class has as responsibility converts Data type into Info type.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class ConvertData
{
	/**
	 * Converts Data type inside list of Info type.
	 *
	 * @param datas
	 *            list of data to be converted
	 * @return map with list of data converted to Info type
	 */
	public static Map<String, List<Info>>
	toInfo( List<Data> datas )
	{
		Map<String, List<Info>> mapListInfo = new HashMap<>();

		try
		{
			for ( Data data : datas )
			{
				List<Info> infos = splitInfo( data.getOrigin(),
						data.getText() );
				if ( mapListInfo.containsKey( data.getOrigin() ) )
				{
					for ( Info info : infos )
						mapListInfo.get( data.getOrigin() ).
								add( info );
				}
				else
				{
					mapListInfo.put( data.getOrigin(), infos );
				}
			}
		}
		catch( IOException ignored ) {}

		return mapListInfo;
	}

	private static List<Info>
	splitInfo( String h, String s ) throws IOException
	{
		List<Info> listOfInfo = new ArrayList<>();

		Reader reader = new StringReader( s );
		BufferedReader br = new BufferedReader( reader );
		for ( String line = br.readLine(); line != null;
				line = br.readLine() )
		{
			Info info = new Info();
			info.setHost( h );
			String absolutPath = line.
					substring( 0, line.indexOf( ':' ) );
			File file = new File( absolutPath );
			info.setPath( file.getParentFile().toString() );
			info.setName( file.getName() );
			String remaining = line.substring( line.indexOf( ':' ),
					line.length() );
			String patternFrom = ":(\\d+):(\\s*.*)";
			Pattern patternTo = Pattern.compile( patternFrom );
			Matcher m = patternTo.matcher( remaining );
			while ( m.find() )
			{
				info.setLine( m.group( 1 ) );
				info.setContent( m.group( 2 ) +
						System.getProperty( "line.separator" ) );
			}
			listOfInfo.add( info );
		}

		return listOfInfo;
	}
}
