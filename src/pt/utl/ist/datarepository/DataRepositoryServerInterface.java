package pt.utl.ist.datarepository;

import java.io.IOException;
import java.io.OutputStreamWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import pt.utl.ist.datarepository.utils.ServletUtils;

@SuppressWarnings("serial")
public class DataRepositoryServerInterface extends HttpServlet {
	private static Logger _log = Logger.getLogger(DataRepositoryServerInterface.class);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);                       // all gets redirected as posts
    }
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        OutputStreamWriter outputWriter = ServletUtils.prepareResponse(response);
        StringBuilder output = new StringBuilder();
        output.append("<response>");
        output.append(processPostQuery(request));
        output.append("</response>");
        ServletUtils.finalizeResponse(outputWriter, output);
    }
	
	private String processPostQuery(HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        String sessionHandle = request.getParameter("sessionHandle");
        String action = request.getParameter("action");
        String userID = request.getParameter("userID");
        String password = request.getParameter("password");

        try {
            if (action != null) { // connect
                if("connect".equals(action)) {
                    int interval = request.getSession().getMaxInactiveInterval();
                    msg.append(DataRepository.get().connect(userID, password, interval));
                }
                else if("checkConnection".equals(action)) { // checkConnection
                    msg.append(DataRepository.get().checkConnection(sessionHandle));
                }
                else if("createNewModelInstance".equals(action)) {
                	String dataModelURI = request.getParameter("dataModelURI");
                	String yawlSpecURI = request.getParameter("yawlSpecURI");
        			String yawlInstanceID = request.getParameter("yawlInstanceID");
        			String goalSpecURI = request.getParameter("goalSpecURI");
                	String goalInstanceID = request.getParameter("goalInstanceID");
                	msg.append(DataRepository.get().createModelInstance(dataModelURI, yawlSpecURI, yawlInstanceID,
                			goalSpecURI, goalInstanceID));
                }
                else if("getParameterMapping".equals(action)) {
                	String dataModelURI = request.getParameter("dataModelURI");
                	String parameter = request.getParameter("parameter");
                	msg.append(DataRepository.get().getParameterMapping(dataModelURI, parameter));
                }
                else if("getElementData".equals(action)) {
                	String dataModelURI = request.getParameter("dataModelURI");
                	String elementURI = request.getParameter("elemURI");
                	String instanceID = request.getParameter("instanceID"); 	
                	msg.append(DataRepository.get().getElementData(dataModelURI, elementURI, instanceID));
                } 
                else if("submitData".equals(action)) {
                	String dataModelURI = request.getParameter("dataModelURI");
                	String instanceID = request.getParameter("instanceID");
        			String dataName = request.getParameter("dataName");
        			String dataType = request.getParameter("dataType");
        			String value = request.getParameter("value");
        			String restrictions = request.getParameter("restrictions");
        			boolean isSkipped = Boolean.parseBoolean(request.getParameter("isSkipped"));
                	msg.append(DataRepository.get().submitData(dataModelURI, instanceID, dataName, dataType, value, restrictions, isSkipped));
                }
                else if("skipData".equals(action)) {
                	String dataModelURI = request.getParameter("dataModelURI");
                	String dataModelInstanceID = request.getParameter("instanceID");
                	String elementURI = request.getParameter("elementURI");
                	msg.append(DataRepository.get().skipData(dataModelURI, dataModelInstanceID, elementURI));
                }
                else if("addData".equals(action)) {
                	String dataModelURI = request.getParameter("dataModelURI");
                	String instanceID = request.getParameter("instanceID");
                	String dataName = request.getParameter("dataName");
                	String dataType = request.getParameter("dataType");
                	String isKey = request.getParameter("isKey");
                	String relation = request.getParameter("relation");
                	
                	if(dataType == null && isKey == null) {
                		// add entity
                		msg.append(DataRepository.get().addEntity(dataModelURI, instanceID, dataName, relation));
                	} else {
                		// add attribute
                		msg.append(DataRepository.get().addAttribute(dataModelURI, instanceID, dataName, dataType, isKey));
                	}
                }
                else if("getDataModel".equals(action)) {
                	String dataModelURI = request.getParameter("dataModelURI");
                	String instanceID = request.getParameter("instanceID");
                	msg.append(DataRepository.get().getDataModelInXML(dataModelURI, instanceID));
                }
            }
        }
        catch (Exception e) {
            _log.error("Exception in Interface B with action: " + action, e);
        }
        if (msg.length() == 0) {
            msg.append("<failure><reason>Invalid action or exception was thrown." +
                       "</reason></failure>");
        }
        return msg.toString();
    }
}
