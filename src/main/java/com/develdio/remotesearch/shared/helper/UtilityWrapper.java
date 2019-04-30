package com.develdio.remotesearch.shared.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * For convenience, this class wrappers the utilities available
 * in this application. Therefore, this permits the shared this
 * utilities with multiples layers of the system.
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public class UtilityWrapper
{
	/*
	 * Represents the scp command.
	 */
	public static class Scp
	{
	}

	/*
	 * Represents the grep command.
	 */
	public static class Grep
	{
		private String parameter = "";
		private boolean hasParameter = false;

		private List<String> directories = new ArrayList<>();
		private boolean hasDirectory = false;

		private String pattern = "";
		private boolean hasPattern = false;

		private boolean isColorOn = false;

		private boolean isVerbose = true;

		public void addParameter( String parameter )
		{
			this.parameter = parameter;
			this.hasParameter = true;
		}

		public String getParameter()
		{
			return parameter;
		}

		public boolean hasParameter()
		{
			return this.hasParameter;
		}

		public void addDirectory( String directory )
		{
			this.directories.add( directory );
			this.hasDirectory = true;
		}

		public boolean hasDirectory()
		{
			return hasDirectory;
		}

		public void addPattern( String pattern )
		{
			this.pattern = pattern;
			this.hasPattern = true;
		}

		public boolean hasPattern()
		{
			return this.hasPattern;
		}

		public String getPattern()
		{
			return pattern;
		}

		public void setIsColorOn( boolean isColorOn )
		{
			this.isColorOn = isColorOn;
		}

		public boolean isColorOn()
		{
			return isColorOn;
		}

		public void setIsVerbose( boolean isVerbose )
		{
			this.isVerbose = isVerbose;
		}

		public boolean isVerbose()
		{
			return isVerbose;
		}

		/**
		 * This method computes the command that will be executed soon later by
		 * some participant.
		 *
		 * @return String
		 *         the output formatted with the syntax needed by the
		 *         grep command
		 */
		public String toString()
		{
			StringBuilder sb = new StringBuilder( "" );

			sb.append( "find " );
			for ( String directory : directories )
				sb.append( directory + " " );

			sb.append( "-type f -exec " );
			sb.append( "grep -n " );

			if ( hasParameter )
			{
				sb.setLength( sb.length() - 1 );
				sb.append( parameter + " " );
			}

			sb.append( "\'"+pattern+"\'" + " " );
			sb.append( "/dev/null {} +" );
			sb.append( " 2> /dev/null" );

			return sb.toString();
		}
	}
}
