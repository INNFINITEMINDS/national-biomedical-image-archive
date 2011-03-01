package gov.nih.nci.ncia.zip;


public class SeriesFileRetrieverFactory {

	public static SeriesFileRetriever getSeriesFileRetriever() {
		if(instance==null) {
			instance = createSeriesFileRetriever();
		}
		return instance;
	}
	
	/////////////////////////////////////PRIVATE////////////////////////////////////
	
	private static SeriesFileRetriever instance;
	
	
	private static SeriesFileRetriever createSeriesFileRetriever() {
		String seriesFileRetrieverClassName = System.getProperty("seriesFileRetriever.className");

		if(seriesFileRetrieverClassName==null) {
			throw new RuntimeException("seriesFileRetriever.className must be defined in system properties");
		}
		else {
			try {
				ClassLoader loader = Thread.currentThread().getContextClassLoader();
				Class clazz = Class.forName(seriesFileRetrieverClassName, false, loader);
				SeriesFileRetriever drillDown =  (SeriesFileRetriever)clazz.newInstance();
				return drillDown;
			}
			catch(Exception ex) {
				throw new RuntimeException(ex);
			}
		}			
	}	
}