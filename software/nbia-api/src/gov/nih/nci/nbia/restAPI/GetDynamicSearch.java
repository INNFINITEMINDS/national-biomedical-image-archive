//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=json
//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=html
//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=xml
//To Test: http://localhost:8080/nbia-api/api/v1/getCollectionValues?format=csv


package gov.nih.nci.nbia.restAPI;

import java.util.List;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MultivaluedMap;

import gov.nih.nci.nbia.dynamicsearch.DynamicSearchCriteria;
import gov.nih.nci.nbia.dynamicsearch.Operator;
import gov.nih.nci.nbia.dynamicsearch.QueryHandler;
import gov.nih.nci.nbia.lookup.StudyNumberMap;
import gov.nih.nci.nbia.searchresult.PatientSearchResult;
import gov.nih.nci.nbia.util.SpringApplicationContext;
import gov.nih.nci.nbia.security.*;
import gov.nih.nci.nbia.util.SiteData;
import gov.nih.nci.nbia.restUtil.JSONUtil;
@Path("/getDynamicSearch")
public class GetDynamicSearch extends getData{
	private static final String column="Collection";
	public final static String TEXT_CSV = "text/csv";

	@Context private HttpServletRequest httpRequest;
	/**
	 * This method get a set of all collection names
	 *
	 * @return String - set of collection names
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)

	public Response constructResponse(MultivaluedMap<String, String> inFormParams) {

		try {	
		String stateRelation=inFormParams.get("stateRelation").get(0);
		String userName=inFormParams.get("userName").get(0);
		AuthorizationManager am = new AuthorizationManager(userName);
		List<SiteData> authorizedSiteData = am.getAuthorizedSites();
		List<String> seriesSecurityGroups = am.getAuthorizedSeriesSecurityGroups();
		List <DynamicSearchCriteria> criteria=new ArrayList<DynamicSearchCriteria>();
		int i=0;
		while (inFormParams.get("dataGroup"+i)!=null)
		{
			DynamicSearchCriteria dcriteria = new DynamicSearchCriteria();
			dcriteria.setDataGroup(inFormParams.get("sourceName"+i).get(0));
			dcriteria.setField(inFormParams.get("itemName"+i).get(0));
			String operator=inFormParams.get("operator"+i).get(0);
			Operator doperator=new Operator();
			doperator.setValue(operator);
			dcriteria.setOperator(doperator);
			dcriteria.setValue(inFormParams.get("value"+i).get(0));
			criteria.add(dcriteria);
			i++;
		}
		QueryHandler qh = (QueryHandler)SpringApplicationContext.getBean("queryHandler");
		qh.setStudyNumberMap(new StudyNumberMap());

			qh.setQueryCriteria(criteria, stateRelation, authorizedSiteData, seriesSecurityGroups);
			qh.query();

		List<PatientSearchResult> patients = qh.getPatients();
		
		return Response.ok(JSONUtil.getJSONforPatients(patients)).type("application/json")
				.build();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.status(500)
				.entity("Server was not able to process your request").build();
	}
}