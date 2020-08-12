package com.develdio.remotesearch.shared.commandline;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.develdio.remotesearch.exception.PropertiesFileException;
import com.develdio.remotesearch.i18n.Message;
import com.develdio.remotesearch.properties.PropertiesFile;
import com.develdio.remotesearch.shared.Property;
import com.develdio.remotesearch.shared.helper.Print;
import com.develdio.remotesearch.shared.helper.UtilityWrapper;
import com.develdio.remotesearch.shared.helper.UtilityWrapper.Grep;
import com.develdio.remotesearch.shared.helper.UtilityWrapper.Scp;

import gnu.getopt.Getopt;
import gnu.getopt.LongOpt;

/**
 * This class encapsulates and represents all interaction of user with
 * the command line. Any command and their respective parameters, will
 * be prepareds by this class.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class Parameter
{
	/** Used for parsing command line arguments passed to programs. */
	private static Getopt g;

	public Parameter() {}

	/**
	 * Constructor used only internally.
	 */
	private Parameter( String[] argv )
	{
		StringBuffer sb = new StringBuffer();
		String shortOpts = "-:g:s:p:u:hP:F:";

		LongOpt[] longopts =
		{
			new LongOpt( "server",		LongOpt.REQUIRED_ARGUMENT, null, 's' ),
			new LongOpt( "password",	LongOpt.REQUIRED_ARGUMENT, null, 'p' ),
			new LongOpt( "username",	LongOpt.REQUIRED_ARGUMENT, null, 'u' ),
			new LongOpt( "grep",		LongOpt.REQUIRED_ARGUMENT, sb, 'g' ),
			new LongOpt( "color",		LongOpt.NO_ARGUMENT, null, 'c' ),
			new LongOpt( "json",		LongOpt.NO_ARGUMENT, null, 'j' ),
			new LongOpt( "file",		LongOpt.REQUIRED_ARGUMENT, null, 'F' ),
			new LongOpt( "scp",			LongOpt.NO_ARGUMENT, null, 'S' ),
			new LongOpt( "download",	LongOpt.NO_ARGUMENT, null, 'd' ),
			new LongOpt( "profile",		LongOpt.REQUIRED_ARGUMENT, null, 'P' ),
			new LongOpt( "from",		LongOpt.REQUIRED_ARGUMENT, null, 'f' ),
			new LongOpt( "to",			LongOpt.REQUIRED_ARGUMENT, null, 't' ),
			new LongOpt( "help",		LongOpt.NO_ARGUMENT, null, 'h' )
		};

		g = new Getopt( "rsch", argv, shortOpts, longopts, true );
		g.setOpterr( false );
	}

	/**
	 * Prepares the parameters according with the list of parameters
	 * given through command line.
	 *
	 * @param argv
	 *        Vector with the arguments passed through command line
	 * @return PreparedParameter
	 *        parameters prepared to use
	 */
	public static PreparedParameter fromCommandLine( String[] argv )
	{
		Parameter.PreparedParameter parameter =
				new Parameter( argv ).new PreparedParameter();

		Grep grep = new UtilityWrapper.Grep();
		Scp scp = new UtilityWrapper.Scp();
		parameter.setGrep( grep );
		parameter.setScp( scp );

		int c;
		while( ( c = g.getopt() ) != -1 )
		{
			switch( c )
			{
				case 0:
					grep.addParameter( g.getOptarg() );
				break;

				case 1:
					if ( ! grep.hasPattern() )
						grep.addPattern( g.getOptarg() );
					else if ( ! grep.hasDirectory() )
						grep.addDirectory( g.getOptarg() );
				break;

				case 's':
					parameter.setServerName( g.getOptarg() );
				break;

				case 'u':
					parameter.setUserName( g.getOptarg() );
				break;

				case 'p':
					parameter.setPassword( g.getOptarg() );
				break;

				case 'c':
					grep.setIsColorOn( true );
				break;

				case 'd':
					parameter.setIsGrepWithDownload( true );
				break;

				case 'f':
					parameter.setSource( g.getOptarg() );
				break;

				case 't':
					parameter.setDest( g.getOptarg() );
				break;

				case 'P':
					parameter.addProfiles( g.getOptarg() );
					parameter.setHasProfile( true );
				break;

				case 'j':
					parameter.setIsJsonOutput( true );
				break;

				case 'S':
					parameter.setIsScpCommand( true );
					parameter.setIsGrepCommand( false );
				break;

				case 'F':
					if ( "".equals( g.getOptarg() ) )
						Print.translateOutAndExit( Message.
							PROPERTIES_FILE_NOT_INFORMED );
					try {
						PropertiesFile propertiesFile = PropertiesFile.
							createPropertiesFromfile(
							g.getOptarg() );
						parameter.setPropertiesFile( propertiesFile.
							getPropertiesValues() );
						parameter.setIsViaPropertiesFile( true );
					}
					catch( PropertiesFileException rse ) {
						 Print.translateOutAndExit( Message
							.PROPERTIES_FILE_LOAD_ERROR,
							g.getOptarg(), rse.getMessage() );
					}
					parameter.setFileName( g.getOptarg() );
					parameter.setHasMultiplesEndpoints( true );
				break;

				case ':':
					if ( (char)g.getOptopt() == 's' ) {
						Print.translateOutAndExit(
							Message.HOSTNAME_WAS_NOT_INFORMED );
					}

					if ( (char)g.getOptopt() == 'p' ) {
						Print.translateOutAndExit(
							Message.PASSWORD_NOT_INFORMED );
					}

					if ( (char)g.getOptopt() == 'u' ) {
						Print.translateOutAndExit(
							Message.USER_NOT_INFORMED );
					}

					if ( (char)g.getOptopt() == 'f' ) {
						Print.translateOutAndExit(
							Message.DIRECTORY_SOURCE_NOT_INFORMED );
					}

					if ( (char)g.getOptopt() == 't' ) {
						Print.translateOutAndExit(
							Message.DIRECTORY_DEST_NOT_INFORMED );
					}

					/*
					if ( (char)g.getOptopt() == 'g' ) {
						Print.translateOutAndExit(
							Message.GREP_COMMAND_NOT_INFORMED );
					}*/

					if ( (char)g.getOptopt() == 'F' ) {
						Print.translateOutAndExit(
							Message.PROPERTIES_FILE_NOT_INFORMED );
					}
				break;

				case 'h':
				case '?':
					Print.help();
				default:
					Print.syntax();
			}
		}

		// Validates the commands and their parameters.
		validateCommandAndTheirParameters( grep, scp, parameter );

		return parameter;
	}

	/**
	 * In fact, the real prepared command.
	 */
	public class PreparedParameter
	{
		private String serverName = "";
		private String password = "";
		private String userName = "";
		private String directory = "";
		private boolean isColorOff = true;
		private String help = "";
		private Grep grep = new Grep();
		private Scp scp = new Scp();
		private boolean hasMultiplesEndpoints = false;
		private boolean isGrepCommand = true; // grep command is default
		private boolean isScpCommand = false;
		private boolean isGrepWithDownload = false;
		private String src = "";
		private String dst = "";
		private String fileName = "";
		private boolean isJsonOutput = false;
		private boolean hasProfile = false;
		private String[] profiles;
		private boolean isViaPropertiesFile = false;
		private Map<String, List<Property>> propertiesMap =
				new HashMap<>();
		
		public String
		getServerName() { return serverName; }

		public void
		setServerName( String serverName ) {
			this.serverName = serverName;
		}

		public String
		getPassword() { return password; }

		public void
		setPassword( String password ) {
			this.password = password;
		}

		public String
		getUserName() { return userName; }

		public void
		setUserName( String userName ) {
			this.userName = userName;
		}

		public String
		getDirectory() { return directory; }

		public void
		setDirectory( String directory ) {
			this.directory = directory;
		}

		public boolean
		getIsColorOff() { return isColorOff; }

		public void
		setIsColorOff( boolean isColorOff ) {
			this.isColorOff = isColorOff;
		}

		public String
		getHelp() { return help; }

		public void
		setHelp( String help ) { this.help = help; }

		public Grep
		loadsGrep() { return grep; }

		public void
		setGrep( Grep grep ) { this.grep = grep; }

		public Scp
		loadsScp() { return scp; }

		public void
		setScp( Scp scp ) { this.scp = scp; }

		public boolean
		hasMultiplesEndpoints() {
			return hasMultiplesEndpoints;
		}

		public void
		setHasMultiplesEndpoints(
				boolean hasMultiplesEndpoints ) {
			this.hasMultiplesEndpoints =
					hasMultiplesEndpoints;
		}

		public
		String getSource() { return src; }

		public void
		setSource( String src ) { this.src = src; }

		public String
		getDest() { return dst; }

		public void
		setDest( String dst ) { this.dst = dst; }

		public String
		getFileName() { return fileName; }

		public void
		setFileName( String fileName ) {
			this.fileName = fileName;
		}

		public void
		setIsGrepCommand( boolean isGrepCommand ) {
			this.isGrepCommand = isGrepCommand;
		}

		public boolean
		isGrepCommand() { return isGrepCommand; }

		public void
		setIsGrepWithDownload( boolean isGrepWithDownload ) {
			this.isGrepWithDownload = isGrepWithDownload;
		}

		public boolean
		isGrepWithDownload() {
			return isGrepWithDownload;
		}

		public void
		setIsScpCommand( boolean isScpCommand ) {
			this.isScpCommand = isScpCommand;
		}

		public boolean
		isScpCommand() { return isScpCommand; }

		public void
		setIsJsonOutput( boolean isJsonOutput ) {
			this.isJsonOutput = isJsonOutput;
		}

		public boolean
		isJsonOuput() { return isJsonOutput; }

		public Map<String, List<Property>>
		getPropertiesFile() {
			return propertiesMap;
		}

		public void
		setPropertiesFile( Map<String, List<Property>>
				propertiesMap ) {
			this.propertiesMap = propertiesMap;
		}

		public void
		setIsViaPropertiesFile(
				boolean isViaPropertiesFile ) {
			this.isViaPropertiesFile =
					isViaPropertiesFile;
		}

		public boolean
		isViaPropertiesFile() {
			return isViaPropertiesFile;
		}

		public void
		addProfiles( String profile ) {
			profiles = profile.split( "," );
		}

		public String[]
		getProfiles() { return profiles; }

		public void
		setHasProfile( boolean hasProfile ) {
			this.hasProfile = hasProfile;
		}

		public boolean hasProfile() {
			return hasProfile;
		}
	}

	private static void
	validateCommandAndTheirParameters( Grep grep, Scp scp,
			PreparedParameter parameter )
	{
		if ( ! parameter.isViaPropertiesFile )
		{
			if ( null == parameter.getServerName() ||
				"".equals( parameter.getServerName() ) )
			{
				Print.translateOutAndExit(
					Message.
					HOSTNAME_WAS_NOT_INFORMED );
			}

			if ( null == parameter.getUserName() ||
				"".equals( parameter.getUserName() ) )
			{
				Print.translateOutAndExit(
					Message.USER_NOT_INFORMED );
			}
			if ( null == parameter.getPassword() ||
				"".equals( parameter.getPassword() ) )
			{
				Print.translateOutAndExit(
					Message.PASSWORD_NOT_INFORMED );
			}
		}

		if ( parameter.isScpCommand() )
		{
			if ( null == parameter.getSource() ||
				"".equals( parameter.getSource() ) )
			{
				Print.translateOutAndExit(
					Message.DIRECTORY_SOURCE_NOT_INFORMED );
			}
			if ( null == parameter.getDest() ||
				"".equals( parameter.getDest() ) )
			{
				Print.translateOutAndExit(
					Message.DIRECTORY_DEST_NOT_INFORMED );
			}
		}

		if ( parameter.isGrepCommand() )
		{
			if ( ! grep.hasPattern() ) {
				Print.translateOutAndExit(
						Message.KEYWORD_NO_INFORMED);
			}

			if( ! parameter.isViaPropertiesFile )
			{
				if ( ! grep.hasDirectory() ) {
					Print.translateOutAndExit(
							Message.LOCAL_TARGET_TO_SEARCH);
				}
			}

			if ( ( parameter.isViaPropertiesFile ) &&
				( ( parameter.getFileName() == null ) ||
					( "".equals( parameter.getFileName() ) ) ) )
			{
				Print.translateOutAndExit( Message.
						PROPERTIES_FILE_NOT_INFORMED );
			}

			if ( ( parameter.isViaPropertiesFile ) &&
					( parameter.hasProfile ) )
			{
				StringBuffer sb = new StringBuffer( "" );
				for ( Map.Entry<String, List<Property>> entry :
						parameter.propertiesMap.entrySet() )
				{
					sb.append( entry.getKey() + "," );
				}

				String profilesExisting = sb.toString().
					substring( 0, sb.toString().length() - 1 );
				List<String> listOfProfilesExistings =
					Arrays.asList( profilesExisting.split( "," ) );

				for ( int i = 0; i < parameter.profiles.length; i++ ) {
					if (!listOfProfilesExistings.
							contains(parameter.profiles[i])) {
						Print.translateOut(Print.getYellowColor(),
								Message.PROFILE_NOT_EXISTS,
								parameter.profiles[i]);
					}
				}
			}
		}
	}
}
