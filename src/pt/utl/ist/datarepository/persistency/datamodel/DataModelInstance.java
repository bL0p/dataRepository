package pt.utl.ist.datarepository.persistency.datamodel;

import java.util.ArrayList;

import pt.utl.ist.datarepository.DataRepository;

public class DataModelInstance {

	private String dataModelURI; // must be the same as the meta model
	private String dataModelInstanceID; //must be the same as the data model
	
	private ArrayList<EntityInstance> entities = new ArrayList<EntityInstance>();
	private ArrayList<RelationInstance> relations = new ArrayList<RelationInstance>();
	
	public DataModelInstance(String uri, String instanceID) {
		this.dataModelURI = uri;
		this.dataModelInstanceID = instanceID;
	}
	
	public String getDataModelURI() { return this.dataModelURI; }
	public String getDataModelInstanceID() { return this.dataModelInstanceID; }
	
	public void addEntityInstance(EntityInstance entity) {
		this.entities.add(entity);
	}
	
	public void addRelationInstance(RelationInstance relation) {
		this.relations.add(relation);
		
		relation.getEntityOne().addRelation(relation);
		relation.getEntityTwo().addRelation(relation);
	}
	
	public void broadcastChange(String elementURI) {
		DataRepository.get().notifyDataUpdate(dataModelURI, dataModelInstanceID, elementURI);
	}
}
