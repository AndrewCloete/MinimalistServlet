package acza.stellee.geyserm2m;

import org.eclipse.om2m.commons.utils.XmlMapper;
import org.eclipse.om2m.commons.resource.*;


public class M2MxmlFactory {

	//URI localhost:8181/om2m/gscl/applications
	public static String registerApplication(String appID, String apoc){
		Application app = new Application();
		APoCPaths apocpaths = new APoCPaths();
		APoCPath apocpath = new APoCPath();
		
		apocpath.setPath(apoc);
		apocpaths.getAPoCPath().add(apocpath);
		app.setAPoCPaths(apocpaths);
		app.setAppId(appID);
		
		return XmlMapper.getInstance().objectToXml(app);
	}
	
		
	public static String addContainer(String containerID){
		Container container = new Container(containerID);
		//container.setMaxNrOfInstances((long)1);
		//For some reason, if you set this, NO content instances show up!!
		
		return XmlMapper.getInstance().objectToXml(container);
	}
}