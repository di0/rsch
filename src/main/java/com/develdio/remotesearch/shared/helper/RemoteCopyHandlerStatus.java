package com.develdio.remotesearch.shared.helper;

import com.develdio.remotesearch.i18n.Message;

/**
 * This class handles the status from remote copy, offering to the user
 * the progress about the transfer that being performed by secure copy
 * between client and server.
 *
 *
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 */
public final class RemoteCopyHandlerStatus
{
	/** Represents the progress of the transfer */
	private StringBuilder progress;

	/** Stores the percent already calculate on general */
	private static int overall = 0;

	/**
	 * We want a private constructor because we create our
	 * factory method.
	 */
	private RemoteCopyHandlerStatus()
	{
		this.progress = new StringBuilder( 60 );
	}

	/**
	 * Starts the handling of status of the transfer.
	 *
	 * @return this reference.
	 */
	public static
	RemoteCopyHandlerStatus init()
	{
		return new RemoteCopyHandlerStatus();
	}

	/**
	 * This method compute and update the transfer status every
	 * iteration.
	 *
	 * The update is based on value done, represented by parameter
	 * of same name.
	 *
	 * The calculate is perfomed on multiplication of the done by
	 * 100 and division by total size, given on parameter total.
	 *
	 * This method prints on output, the progress of transfer,
	 * writing on default output, the sharp symbol(#), until
	 * that the value of 100% is not reached.
	 *
	 * @param done
	 *            the value already progressed
	 * @param total
	 *            the real total size
	 */
	public void
	update( int done, int total )
	{
		char[] phases = { '|', '/', '-', '\\' };
		String format = "\r" + Print.greenAndBlack( Print
				.translateOut( Message.PROGRESS_DOWNLOAD )
						+":[%3d%%]" ) + " %s %c";

		// Value percent starts in decreasing form. With this
		// calculate, we invert to growing form.
		int percent = (int)( ( done * 100.0f ) / total );
		percent = 100 - percent;

		/*
		 * We can have case in that the result of an equation
		 * is the same previous percentage result. To avoid
		 * that the calculate will be done once again, we use
		 * the code snippet above to control this behavior.
		 * 
		 * So, for instance, if we have hypothetically the
		 * result of 88% of conclusion, in multiples
		 * iterations, we only go forward once more during in
		 * the next iteration whose result is not equal 88%.
		 */
		if ( percent != overall )
		{
			int extrachars =
					( percent / 2 ) - this.progress.length();

			while( extrachars-- > 0 )
				progress.append( '#' );

			Print.formmatedText( format, percent,
					Print.purple( progress.toString() ),
						phases[ percent % phases.length ] );

			if ( percent == 100 )
			{
				Print.flush();
				Print.getNewLine();
			}
		}
		overall = percent;
	}
}
