package pt.utl.ist.datarepository.persistency.datamodel;

import java.util.ArrayList;
import java.util.Iterator;

import org.jdom.Element;

public class Entity {

	private String name;
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>(); // key = attribute name
	private ArrayList<Relation> relations = new ArrayList<Relation>();
	private boolean singleInstance = false; // if the Entity has a singleton (only one instance in all cases);
	private int instanceCounter = 0;
	
	private ArrayList<EntityInstance> instances = new ArrayList<EntityInstance>();
	
	public Entity(String name) {
		this.name = name;
	}
	
	public ArrayList<Relation> getRelations() {
		return this.relations;
	}
	
	public ArrayList<Attribute> getAttributes() {
		return this.attributes;
	}
	
	/**
	 * Gets an attribute from the entity
	 * @param attrName the attribute name (in the form [Entity].[Attribute]
	 * @return the attribute or null if not found
	 */
	public Attribute getAttribute(String attrName) {
		for (Attribute it : this.attributes) {
			if(it.getName().equals(attrName)) {
				return it;
			}
		}
		return null;
	}
	
	public void addRelation(Relation rel) {
		this.relations.add(rel);
	}
	
	public Attribute addAttribute(Attribute att) {
		att.setName(this.getName() + "." + att.getName());
		this.attributes.add(att);
		
		for (EntityInstance entityInstance : this.instances) {
			entityInstance.addAttributeInstance(att.createEmptyInstance(
					entityInstance.getDataModelInstance(),
					entityInstance));
		}
		
		return att;
	}

	public String getName() {
		return name;
	}
	
	public ArrayList<EntityInstance> getInstances() {
		ArrayList<EntityInstance> a = new ArrayList<EntityInstance>();
		a.addAll(this.instances);
		return a;
	}
	
	public ArrayList<EntityInstance> getDefinedInstances() {
		ArrayList<EntityInstance> a = new ArrayList<EntityInstance>();
		for (EntityInstance instance : this.instances) {
			if(instance.isDefined()) {
				a.add(instance);
			}
		}
		return a;
	}
	
	public EntityInstance getEmptyInstance() {
		for (EntityInstance instance : this.instances) {
			if(!instance.isDefined()) {
				return instance;
			}
		}
		
		return null;
	}
	
	public void skip() {
		for (EntityInstance instance : this.instances) {
			instance.skip();
		}
	}
	
	public void evaluateIfSingleInstance() {
		//TODO see if,
		// for every entity that points to this
		// this has a cardinality of one
	}
	
	public boolean isSingleInstance() {
		return this.singleInstance;
	}
	
	public void setSingleInstance(boolean single) {
		this.singleInstance = single;
	}
	
	public EntityInstance createEmptyInstance(DataModelInstance dataModelInstance) {
		// create an empty instance of the entity plus the attributes
		EntityInstance entityInstance = new EntityInstance(dataModelInstance, this, new ArrayList<AttributeInstance>(), instanceCounter++);
		this.instances.add(entityInstance);
		
		for (Attribute attribute : this.attributes) {
			entityInstance.addAttributeInstance(attribute.createEmptyInstance(dataModelInstance, entityInstance));
		}
		
		return entityInstance;
	}
	
	public boolean isKeyEntitiesDefined() {
		for (Relation relation : this.relations) {
			if(isOtherInRelationKey(relation) && !getOtherInRelation(relation).isDefined()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean hasKeyAttibutes() {
		for (Attribute attribute : this.attributes) {
			if(attribute.isKeyAttribute()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isDefined() {
		for (EntityInstance instance : this.instances) {
			if(instance.isDefined()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean instanceExists() {
		if(this.instances.size() > 0) {
			return true;
		}
		return false;
	}

	public ArrayList<Entity> getRelatedEntities() {
		ArrayList<Entity> entities = new ArrayList<Entity>();
		for (Relation rel : this.relations) {
			entities.add(getOtherInRelation(rel));
		}
		return entities;	
	}

	public ArrayList<Entity> getRelatedKeyEntities() {
		ArrayList<Entity> keyEntities = new ArrayList<Entity>();
		for (Relation rel : this.relations) {
			if(isOtherInRelationKey(rel)) {
				keyEntities.add(getOtherInRelation(rel));
			}
		}
		return keyEntities;
	}
	
	public Element toXMLElement() {
		Element entity = new Element("Entity");
		entity.setAttribute("Name", this.getName());
		for (Attribute att : this.attributes) {
			Element attEl = new Element("Attribute");
			attEl.setAttribute("Name", att.getName());
			entity.addContent(attEl);
		}
		return entity;
	}
	
	///// INTERNAL METHODS /////
	protected Entity getOtherInRelation(Relation rel) {
		if(rel.getEntityOne().equals(this)) {
			return rel.getEntityTwo();
		} else {
			return rel.getEntityOne();
		}
	}
	
	protected boolean isOtherInRelationKey(Relation rel) {
		if(rel.getEntityOne().equals(this)) {
			return rel.isTwoKeyEntity();
		} else {
			return rel.isOneKeyEntity();
		}
	}
}
