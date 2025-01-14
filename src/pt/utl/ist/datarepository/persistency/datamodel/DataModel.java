package pt.utl.ist.datarepository.persistency.datamodel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.XMLOutputter;

public class DataModel {

	private String dataModelURI; // must be the same as the meta model
	private String instanceID;
	
	private boolean activityCaseClaimed = false;
	private String activityCaseInstanceID;
	
	private String goalCaseInstanceID; 
	
	private HashMap<String, Entity> entities = new HashMap<String, Entity>(); 
	private ArrayList<Relation> relations = new ArrayList<Relation>();
	
	private DataModelInstance instance;
	
	public DataModel(String uri, String instID) {
		this.dataModelURI = uri;
		this.instanceID = instID;
		this.instance = new DataModelInstance(this.dataModelURI, this.instanceID);
	}
	
	public void addEntity(Entity ent) {
		this.entities.put(ent.getName(), ent);
		
		// create the instance and add it to the data model instance
		EntityInstance inst = ent.createEmptyInstance(this.instance);
		
		this.instance.addEntityInstance(inst);
		
	}
	
	public void claimActivityCase() { this.activityCaseClaimed = true; }
	public boolean isActivityCaseClaimed() { return this.activityCaseClaimed; }
	
	public void setActivityCaseInstanceID(String instanceID) {
		this.activityCaseInstanceID = instanceID;
	}
	public String getActivityCaseInstanceID() {
		return this.activityCaseInstanceID;
	}
	
	public void setGoalCaseInstanceID(String instanceID) { this.goalCaseInstanceID = instanceID; }
	public String getGoalCaseInstanceID() { return this.goalCaseInstanceID; }
	
	public String getInstanceID() { return this.instanceID; }
	public void setInstanceID(String id) { this.instanceID = id; }
	
	public DataModelInstance getInstance() {
		return this.instance;
	}
	
	public Entity getEntity(String entityName) {
		return this.entities.get(entityName);
	}
	
	public void addRelation(Relation rel) {
		this.relations.add(rel);
		
		rel.getEntityOne().addRelation(rel);
		rel.getEntityTwo().addRelation(rel);
		
		// add relation instance to the data model instance
		EntityInstance one = rel.getEntityOne().getEmptyInstance();
		if(one == null) one = rel.getEntityOne().createEmptyInstance(this.instance);
		
		EntityInstance two = rel.getEntityTwo().getEmptyInstance();
		if(two == null) two = rel.getEntityTwo().createEmptyInstance(this.instance);
		
		this.instance.addRelationInstance(rel.createInstance(one, two));
 	}
	
	public String getElementAsXMLString(String elementURI) { // tested
		Document doc = getElementAsXML(elementURI);
		return new XMLOutputter().outputString(doc);
	}
	
	public Document getElementAsXML(String elementURI) {
		String[] elementArray = elementURI.split("\\.");
		String entityName = elementArray[0];
		String entityAttribute = null;
		
		if(elementArray.length > 1) {
			 entityAttribute = elementArray[1];
		}
		
		if(entityName == null) {
			return null;
		}
		
		Document doc = new Document();
		Element rootElement = new Element("Element");
		Entity ent = this.entities.get(entityName);
		if(entityAttribute == null) {
			EntityInstance entityInst = ent.getInstances().get(0);  //FIXME padeirada
			rootElement.addContent(entityInst.toXMLElement());
//			ArrayList<EntityInstance> entInstances = ent.getInstances();
//			for (EntityInstance entityInstance : entInstances) {
//				rootElement.addContent(entityInstance.toXMLElement());
//			}
		}
		else {
			// wants the info about an attribute
			Attribute att = ent.getAttribute(elementURI); //FIXME if there are many entity instances, there can be many attributes
			rootElement.addContent(att.getInstance().toXMLElement());
		}
		doc.setRootElement(rootElement);
		
		return doc;
	}
	
	public boolean validateAttribute(String attributeURI, String type) {
		String[] elementArray = attributeURI.split("\\.");
		Entity ent = this.entities.get(elementArray[0]);
		return type.equalsIgnoreCase(ent.getAttribute(attributeURI).getType().toString());
	}
	
	public void insertAttributeValue(String attributeURI, String value) {
		String[] elementArray = attributeURI.split("\\.");
		Entity ent = this.entities.get(elementArray[0]);
		Attribute att = ent.getAttribute(attributeURI);
		
		att.getInstance().setValue(value);
	}
	
	public void skipElement(String elementURI) {
		if(elementURI.split("\\.").length > 1) {
			skipAttribute(elementURI);
		} else {
			Entity ent = this.entities.get(elementURI);
			ent.skip();
		}
	}
	
	public void skipAttribute(String dataName) {
		String[] elementArray = dataName.split("\\.");
		Entity ent = this.entities.get(elementArray[0]);
		Attribute att = ent.getAttribute(dataName);
		
		att.getInstance().skip();
	}
	
	public String getURI() { return this.dataModelURI; }
	
	public String toXMLString() {
		return new XMLOutputter().outputString(toXMLElement());
	}
	
	public Element toXMLElement() {
		Element dataModel = new Element("DataModel");
		dataModel.setAttribute("dataModelURI", this.dataModelURI);
		dataModel.setAttribute("instanceID", this.instanceID);
		
		Collection<Entity> entityCollection = this.entities.values();
		ArrayList<Entity> entityArray = new ArrayList<Entity>(entityCollection);
		for (Entity entity : entityArray) {
			dataModel.addContent(entity.toXMLElement());
		}
		
		return dataModel;
	}
}
