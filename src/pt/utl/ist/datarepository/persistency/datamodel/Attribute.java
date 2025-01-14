package pt.utl.ist.datarepository.persistency.datamodel;

import java.util.ArrayList;

/**
 * Represents an attribute from an Entity in the Data Model
 * @author bernardoopinto
 *
 */
public class Attribute {
	
	private String name;
	private final MetaAttribute.Type type;
	private AttributeInstance instance;
	private int instanceCounter = 0;
	
	private Entity entity;
	
	private boolean keyAttribute;
	
	public Attribute(Entity entity, MetaAttribute.Type tp, String name, boolean key) {
		this.entity = entity;
		this.type = tp;
		this.name = name;
		this.keyAttribute = key;
	}
	
	public boolean isKeyAttribute() {
		return this.keyAttribute;
	}
	
	public MetaAttribute.Type getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String newName) {
		this.name = newName;
	}
	
	public AttributeInstance getInstance() {
		return this.instance;
	}
	
	public boolean isInstanceDefined() {
		if(this.instance == null) {
			return false;
		}
		return this.instance.isDefined();
	}
	
	public AttributeInstance createEmptyInstance(DataModelInstance dataModel, EntityInstance entity) {
		if(instance == null) {
			this.instance = new AttributeInstance(this, entity, dataModel);
		}
		if(this.instance.isDefined()) {
			return null;
		}
		return this.instance;
	}
	
	public void updateInstanceValue(String value) {
		if(instance.getValue().equals("N/A")) {
			instance.setValue(value);
			return;
		}
	}
}
