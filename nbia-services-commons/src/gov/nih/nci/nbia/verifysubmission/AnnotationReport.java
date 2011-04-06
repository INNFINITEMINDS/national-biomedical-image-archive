package gov.nih.nci.nbia.verifysubmission;

import java.util.Collections;
import java.util.Date;
import java.util.SortedMap;

/**
 * This is generated by the public class AnnotationReportGenerator.  This contains
 * all the information necessary to show annotation submission counts within
 * a time frame.
 * 
 * <p>This object is meant to be immutable.
 */
public class AnnotationReport {

	/**
	 * Constructor.  All parameters feed through here
	 * to avoid setters that would violate immutability.
	 */
	public AnnotationReport(AnnotationCountReport annotationCountReport,
			                SortedMap<Date, Integer> submissionDays,
			                Date from,
			                Date to,
	                        String collectionSite) {
		this.annotationCountReport = annotationCountReport;
		this.submissionDays = submissionDays;
		this.from = from;
		this.to = to;
		this.collectionSite = collectionSite;
		
	}
	
    /**
     * For each day in the day range that has a submission relevant
     * to this report, there is an entry in this map.  Further,
     * the number of relevant submissions on that day are associated
     * with the day.
     */	
	public SortedMap<Date, Integer> getSubmissionDays() {
		return Collections.unmodifiableSortedMap(submissionDays);
	}
	
	public AnnotationCountReport getAnnotationCountReport() {
		return annotationCountReport;
	}	

    /**
     * The starting day for the report. 
     */
	public Date getFrom() {
		return new Date(from.getTime());
	}
	
    /**
     * The end day for the report (inclusive). 
     */	
	public Date getTo() {
		return new Date(to.getTime());
	}	
	
	/**
	 * The collection+site this report is relevant to
	 */
	public String getCollectionSite(){		
		return collectionSite;
	}	
	////////////////////////////////////PRIVATE//////////////////////////////////
	
	private SortedMap<Date, Integer> submissionDays;	
	
	private AnnotationCountReport annotationCountReport;
	
	private Date from;
	
	private Date to;
	
	private String collectionSite;
	
}