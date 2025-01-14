package pt.utl.ist.datarepository.persistency.datamodel;

import pt.utl.ist.datarepository.persistency.datamodel.MetaRelation.Cardinality;

public class RelationInstance {

	private Relation parent;
	private EntityInstance entityOne;
	private EntityInstance entityTwo;
	
	public RelationInstance(Relation parentRelation, EntityInstance one, EntityInstance two) {
		this.parent = parentRelation;
		this.entityOne = one;
		this.entityTwo = two;
	}
	
	public EntityInstance getEntityOne() { return this.entityOne; }
	public EntityInstance getEntityTwo() { return this.entityTwo; }
	
	public boolean isOneKeyEntity() { return parent.isOneKeyEntity(); }
	public boolean isTwoKeyEntity() { return parent.isTwoKeyEntity(); }
	
	public Cardinality getCardinalityOne() { return parent.getCardinalityOne(); }
	public Cardinality getCardinalityTwo() { return parent.getCardinalityTwo(); }
	
}
