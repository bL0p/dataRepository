package pt.utl.ist.datarepository.persistency.datamodel;

public class MetaRelation {
	
	public enum Cardinality { MANY, ZERO_OR_ONE, ONE}

	//entities
	private MetaEntity entityOne;
	private MetaEntity entityTwo;
	
	// relation cardinality
	private Cardinality cardinalityOne;
	private Cardinality cardinalityTwo;
	
	// key entities
	private boolean isOneKeyEntity = false;
	private boolean isTwoKeyEntity = false;

	//// GETTERS ////
	/**
	 * Gets entity one
	 * @return the meta entity
	 */
	public MetaEntity getEntityOne() { return this.entityOne; }
	/**
	 * Gets entity two
	 * @return the meta entity
	 */
	public MetaEntity getEntityTwo() { return this.entityTwo; }
	/**
	 * Gets the cardinality from one to two
	 * @return the cardinality
	 */
	public Cardinality getCardinalityOne() { return this.cardinalityOne; }
	/**
	 * Gets the cardinality from two to one
	 * @return the cardinality
	 */
	public Cardinality getCardinalityTwo() { return this.cardinalityTwo; }
	/**
	 * Gets if entity one is key entity for entity two
	 * @return true or false
	 */
	public boolean isOneKeyEntity() { return this.isOneKeyEntity; }
	/**
	 * Gets if entity two is key entity for entity one
	 * @return true or false
	 */
	public boolean isTwoKeyEntity() { return this.isTwoKeyEntity; }
	
	//// SETTERS ////
	public void setEntityOne(MetaEntity ent) { this.entityOne = ent; }
	public void setEntityTwo(MetaEntity ent) { this.entityTwo = ent; }
	
	public void setCardinalityOne(Cardinality card) { this.cardinalityOne = card; }
	public void setCardinalityOne(String card) {
		if(card.equals(Cardinality.ONE.toString())) {
			this.setCardinalityOne(Cardinality.ONE);
		} else if(card.equals(Cardinality.MANY.toString())) {
			this.setCardinalityOne(Cardinality.MANY);
		} else if(card.equals(Cardinality.ZERO_OR_ONE.toString())) {
			this.setCardinalityOne(Cardinality.ZERO_OR_ONE);
		}
	}
	
	public void setCardinalityTwo(Cardinality card) { this.cardinalityTwo = card; }
	public void setCardinalityTwo(String card) { 
		if(card.equals(Cardinality.ONE.toString())) {
			this.setCardinalityTwo(Cardinality.ONE);
		} else if(card.equals(Cardinality.MANY.toString())) {
			this.setCardinalityTwo(Cardinality.MANY);
		} else if(card.equals(Cardinality.ZERO_OR_ONE.toString())) {
			this.setCardinalityTwo(Cardinality.ZERO_OR_ONE);
		}
	}
	
	public void isOneKeyEntity(boolean isKey) { this.isOneKeyEntity = isKey; }
	public void isTwoKeyEntity(boolean isKey) { this.isTwoKeyEntity = isKey; }
	
	
	public Relation generateRelation(Entity one, Entity two) {
		if(one == null ||
				two == null ||
				cardinalityOne == null ||
				cardinalityTwo == null) {
			return null;
		}
		return new Relation(one, two, this.cardinalityOne, this.cardinalityTwo, 
				this.isOneKeyEntity, this.isTwoKeyEntity);
	}
}
