package pt.utl.ist.datarepository.persistency.datamodel;

import pt.utl.ist.datarepository.persistency.datamodel.MetaRelation.Cardinality;

/**
 * Represents the relation an entity has with another (unidirectional) in the data model.
 * Example: "I am in an entity, I want to know what is the relation of this entity with other entity."
 * @author bernardoopinto
 *
 */
public class Relation {

	private Entity entityOne;
	private Entity entityTwo;
	
	private Cardinality cardinalityOne;
	private Cardinality cardinalityTwo;
	
	private boolean isOneKeyEntity;
	private boolean isTwoKeyEntity;
	
	public Relation(Entity entOne, Entity entTwo,
			Cardinality cardOne,
			Cardinality cardTwo,
			boolean isOneKeyEnt,
			boolean isTwoKeyEnt) {
		this.entityOne = entOne;
		this.entityTwo = entTwo;
		this.cardinalityOne = cardOne;
		this.cardinalityTwo = cardTwo;
		this.isOneKeyEntity = isOneKeyEnt;
		this.isTwoKeyEntity = isTwoKeyEnt;
	}
	
	public Entity getEntityOne() { return this.entityOne; }
	public Entity getEntityTwo() { return this.entityTwo; }
	
	public boolean isOneKeyEntity() { return this.isOneKeyEntity; }
	public boolean isTwoKeyEntity() { return this.isTwoKeyEntity; }
	
	public Cardinality getCardinalityOne() { return this.cardinalityOne; }
	public Cardinality getCardinalityTwo() { return this.cardinalityTwo; }
	
	public RelationInstance createInstance(EntityInstance one, EntityInstance two) {
		return new RelationInstance(this, one, two);
	}
}
