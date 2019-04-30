package com.develdio.remotesearch.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.develdio.remotesearch.i18n.Message;
import com.develdio.remotesearch.shared.Info;
import com.develdio.remotesearch.shared.helper.ConvertData;
import com.develdio.remotesearch.shared.helper.JSONNotation;
import com.develdio.remotesearch.shared.helper.Print;

/**
 * This class works with container that stored data. This data will
 * be showned by this class.
 *  
 * @author Diogo Pinto <dio@lognull.com>
 * @since 1.0
 * @see com.develdio.remotesearch.data.DataContainer
 */
public class DataContainerDisplay implements Serializable
{
	private static final long serialVersionUID =
			7156630594339119921L;

	/** Container with data stored. */
	private DataContainer container = null;

	/**
	 * Constructs according dependency injection, a container with
	 * data stored that soon later will be used to displays contents.
	 *
	 * @param container
	 *            Container with data stored.
	 */
	public DataContainerDisplay( final DataContainer container )
	{
		this.container = container;
	}

	/**
	 * Shows synchronously the data received from endpoint in normal
	 * plain text.
	 */
	public synchronized void
	textFormat()
	{
		List<Data> datas = container.getAllData();

		for ( Data data : datas )
		{
			Print.markLineSeparation();
			Print.translateOut( Print.getYellowColor(),
					Message.FOUND_KEYWORD_SEARCHED,
					data.getOrigin() );
			Print.newLine();
			Print.markLineSeparation();
			Print.newLine();
			Print.text( data.getText() );
		}
	}

	/**
	 * Shows synchronously the data received from endpoint in JSON
	 * notation plain text.
	 *
	 * @since 1.1
	 */
	public synchronized void
	jsonFormat()
	{
		List<Data> datas = container.getAllData();
		Map<String, List<Info>> mapListInfo = ConvertData.
				toInfo( datas );
		String jsonOut = JSONNotation.notationInfo(
				mapListInfo );
		Print.textWithoutFormatting( jsonOut );
	}
}
