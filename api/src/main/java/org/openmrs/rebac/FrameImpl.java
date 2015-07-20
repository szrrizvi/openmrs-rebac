/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.rebac;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; /*import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;*/
import org.openmrs.AccessRelationship;
import org.openmrs.AccessRelationshipType;
import org.openmrs.Person;
import org.openmrs.Relationship;
import org.openmrs.RelationshipType;
import org.openmrs.api.db.AccessRelationshipDAO;
import org.openmrs.api.db.PersonDAO;
import org.openmrs.rebac.implicitrelations.ImplicitRelationIdentifier;

import ca.ucalgary.ispia.rebac.Direction;
import ca.ucalgary.ispia.rebac.Frame;

/**
 * Concrete implementation of the Frame used to ReBAC Authorization Checking
 */
public class FrameImpl implements Frame {
	
	private static final Log log = LogFactory.getLog(FrameImpl.class);
	
	// Relation identifiers
	/*public static enum RelTypes implements org.neo4j.graphdb.RelationshipType {
		gp, agent, referrer, appointTeam, member, registerWard, wardNurse, test3
	}*/

	// DAOs to obtain relationship objects from the database.
	// Accessing those objects throug the Service layer methods would cause an
	// infinite loop
	private final AccessRelationshipDAO accessRelationshipDao;
	
	private final PersonDAO personDao;
	
	/*private final GraphDatabaseService graphDb;
	
	private Index<Node> nodeIndex;*/

	/**
	 * Constructor, specifying to search for relationship in the TOP context.
	 * searchAllContexts is taken to be false
	 * @param accessRelationshipDao DAO to obtain AccessRelationship objects
	 * @param personDao DAO to obtain Relationship objects
	 */
	public FrameImpl(AccessRelationshipDAO accessRelationshipDao, PersonDAO personDao) {
		this.accessRelationshipDao = accessRelationshipDao;
		this.personDao = personDao;
		/*this.graphDb = RebacSettings.getGraphDb();
		
		try {
			Transaction tx = graphDb.beginTx();
			nodeIndex = graphDb.index().forNodes("nodes");
			tx.success();
			tx.close();
		}
		catch (Exception e) {
			nodeIndex = null;
		}
		
		if (nodeIndex == null) {
			System.out.println("ZAIN: Node index is null");
		}*/
	}
	
	/**
	 * Finds all the specific type neighbours (specified by the relationIdentifeir) of the given vertex, in the effective
	 * social network of the context.
	 * @param vertex The vertex to find the neighbours of.
	 * @param relationIdentifier The relation identifier.
	 * @return Specific type neighbours of the given vertex in the effective social
	 * network.
	 */
	@Override
	@SuppressWarnings( { "rawtypes" })
	public Iterable findNeighbours(Object vertex, Object relationIdentifier, Direction direction) {
		
		// Break into 2 cases; relationIdentifier represents an explicit relation identifier (AccessRelationshipType)
		// or an implicit relation identifier (String);
		if (relationIdentifier instanceof AccessRelationshipType) {
			AccessRelationshipType relId = (AccessRelationshipType) relationIdentifier;
			
			Person person = (Person) vertex;
			return getExplicitNeighbours(person, relId, direction);
			
			/*try {
				
				// Try to cast the vertex to a person object and get its neighbours
				RelTypes relType = getRelType(relId.getId());
				
				if (vertex instanceof Person) {
					long start, end;
					start = System.nanoTime();
					Person person = (Person) vertex;
					Iterable ite = getExplicitNeighbours(person.getId(), relType, direction);
					end = System.nanoTime();
					System.out.println("WEIRD " + (end - start));
					return ite;
				} else if (vertex instanceof String) {
					long start, end;
					start = System.nanoTime();
					String vert = (String) vertex;
					int vertexId = Integer.parseInt(vert);
					Iterable ite = getExplicitNeighbours(vertexId, relType, direction);
					end = System.nanoTime();
					System.out.println((end - start));
					return ite;
				}
			}
			catch (ClassCastException e) {
				log
				        .error("For relation identifier of type org.openmrs.AccessRelationshipType the vertex must be of type org.openmrs.Person.\n"
				                + "Relation Identifier=" + relId + ", Vertex=" + vertex);
				throw new IllegalRelationIdentifierException(
				        "For relation identifier of type org.openmrs.AccessRelationshipType the vertex must be of type org.openmrs.Person.\n"
				                + "Relation Identifier=" + relId + ", Vertex=" + vertex);
			}*/
		} else if (relationIdentifier instanceof RelationshipType) {
			RelationshipType relId = (RelationshipType) relationIdentifier;
			
			//try {
			// Try to cast the vertex to a person object and get its neighbours
			Person person = (Person) vertex;
			return getPatientRecordNeighbours(person, relId, direction);
			/*}
			catch (ClassCastException e) {
				log
				        .error("For relation identifier of type org.openmrs.AccessRelationshipType the vertex must be of type org.openmrs.Person.\n"
				                + "Relation Identifier=" + relId + ", Vertex=" + vertex);
				throw new IllegalRelationIdentifierException(
				        "For relation identifier of type org.openmrs.AccessRelationshipType the vertex must be of type org.openmrs.Person.\n"
				                + "Relation Identifier=" + relId + ", Vertex=" + vertex);
			}*/
		} else if (relationIdentifier instanceof String) {
			
			String relId = (String) relationIdentifier;
			return getImplicitNeighbours(vertex, (String) relId, direction);
		}
		
		return null;
	}
	
	/**
	 * Finds the nighbours, specified by the relation identifier, of the given Person (within the specified context) 
	 * @param vertex The person to find the neighbours of
	 * @param relId The relation identifier of type AccessRelationshipType
	 * @param direction The direction of the relationship
	 * @return The list of specified neighbours
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	private Iterable getExplicitNeighbours(Person vertex, AccessRelationshipType relId, Direction direction) {
		// long start = System.nanoTime();
		List<AccessRelationship> accessRelationships;
		
		List neighbours = new ArrayList();
		switch (direction) {
			case FORWARD:
				// If direction is "FORWARD," loop through all relationships and if
				// rel.getPersonA() is equal to the vertex and
				// rel.getAccessRelationshipType is equal to the relId
				// then add rel.getPersonB() to the list of neighbours
				accessRelationships = accessRelationshipDao.getAccessRelationships(vertex, null, relId);
				for (AccessRelationship rel : accessRelationships) {
					if (rel.getPersonA().equals(vertex)) {
						neighbours.add(rel.getPersonB());
					}
				}
				break;
			case BACKWARD:
				// If direction is "BACKWARD," loop through all relationships and if
				// rel.getPersonB() is equal to the vertex and
				// rel.getAccessRelationshipType is equal to the relId
				// then add rel.getPersonA() to the list of neighbours
				accessRelationships = accessRelationshipDao.getAccessRelationships(null, vertex, relId);
				for (AccessRelationship rel : accessRelationships) {
					if (rel.getPersonB().equals(vertex)) {
						neighbours.add(rel.getPersonA());
					}
				}
				break;
			case EITHER:
				// If direction is "EITHER," loop through all relationships and if
				// rel.getPersonA() is equal to the vertex and
				// rel.getAccessRelationshipType is equal to the relId
				// then add rel.getPersonB() to the list of neighbours
				
				// Else if 
				// rel.getPersonB() is equal to the vertex and
				// rel.getAccessRelationshipType is equal to the relId
				// then add rel.getPersonA() to the list of neighbours
				accessRelationships = accessRelationshipDao.getAccessRelationships(vertex, null, relId);
				accessRelationships.addAll(accessRelationshipDao.getAccessRelationships(null, vertex, relId));
				
				for (AccessRelationship rel : accessRelationships) {
					if (rel.getPersonA().equals(vertex)) {
						neighbours.add(rel.getPersonB());
					} else if (rel.getPersonB().equals(vertex)) {
						neighbours.add(rel.getPersonA());
					}
				}
				
				break;
			default:
				break;
		}
		
		// long end = System.nanoTime();
		
		//System.out.println("Find Neighbours: " + (end - start));
		return neighbours;
	}
	
	/*@SuppressWarnings( { "unchecked", "rawtypes" })
	private Iterable getExplicitNeighbours(int vertexId, RelTypes relType, Direction direction) {
		
		// long start = System.nanoTime();
		System.out.println(vertexId + " " + relType + " " + direction);
		List neighbours = new ArrayList();
		
		try {
			//long start = 0l, end = 0l;
			
			//start = System.nanoTime();
			Transaction tx = graphDb.beginTx();
			//end = System.nanoTime();
			//System.out.println("First: " + (end - start));
			
			//start = System.nanoTime();
			Node node = nodeIndex.get("id", vertexId).getSingle();
			//end = System.nanoTime();
			//System.out.println("Second: " + (end - start));
			
			Iterable<org.neo4j.graphdb.Relationship> rels = null;
			switch (direction) {
				case FORWARD:
					// If direction is "FORWARD," loop through all relationships and if
					// rel.getPersonA() is equal to the vertex and
					// rel.getAccessRelationshipType is equal to the relId
					// then add rel.getPersonB() to the list of neighbours
					
					rels = node.getRelationships(org.neo4j.graphdb.Direction.OUTGOING, relType);
					for (org.neo4j.graphdb.Relationship r : rels) {
						System.out.println("N: " + r.getEndNode().getProperty("id"));
						neighbours.add(r.getEndNode().getProperty("id"));
					}
					break;
				case BACKWARD:
					// If direction is "BACKWARD," loop through all relationships and if
					// rel.getPersonB() is equal to the vertex and
					// rel.getAccessRelationshipType is equal to the relId
					// then add rel.getPersonA() to the list of neighbours
					rels = node.getRelationships(org.neo4j.graphdb.Direction.INCOMING, relType);
					for (org.neo4j.graphdb.Relationship r : rels) {
						System.out.println("N: " + r.getEndNode().getProperty("id"));
						neighbours.add(r.getStartNode().getProperty("id"));
					}
					break;
				case EITHER:
					// If direction is "EITHER," loop through all relationships and if
					// rel.getPersonA() is equal to the vertex and
					// rel.getAccessRelationshipType is equal to the relId
					// then add rel.getPersonB() to the list of neighbours
					//start = System.nanoTime();
					rels = node.getRelationships(org.neo4j.graphdb.Direction.OUTGOING, relType);
					for (org.neo4j.graphdb.Relationship r : rels) {
						System.out.println("N: " + r.getEndNode().getProperty("id"));
						neighbours.add(r.getEndNode().getProperty("id"));
					}
					
					// Else if 
					// rel.getPersonB() is equal to the vertex and
					// rel.getAccessRelationshipType is equal to the relId
					// then add rel.getPersonA() to the list of neighbours
					rels = node.getRelationships(org.neo4j.graphdb.Direction.INCOMING, relType);
					for (org.neo4j.graphdb.Relationship r : rels) {
						System.out.println("N: " + r.getEndNode().getProperty("id"));
						neighbours.add(r.getStartNode().getProperty("id"));
					}
					
					//end = System.nanoTime();
					//System.out.println("Third: " + (end - start));
					
				default:
					break;
			}
			
			// long end = System.nanoTime();
			
			//System.out.println("Find Neighbours: " + (end - start));
			//start = System.nanoTime();
			tx.success();
			tx.close();
			//end = System.nanoTime();
			//System.out.println("Fourth: " + (end - start) + "\n");
			
		}
		catch (Exception e) {
			System.out.println("NEO4J Exception.");
			e.printStackTrace();
		}
		
		return neighbours;
	}*/

	/**
	 * Finds the nighbours, specified by the relation identifier, of the given Person (within the specified context) 
	 * @param vertex The person to find the neighbours of
	 * @param relId The relation identifier of type RelationshipType
	 * @param direction The direction of the relationship
	 * @return The list of specified neighbours
	 */
	@SuppressWarnings( { "unchecked", "rawtypes" })
	private Iterable getPatientRecordNeighbours(Person vertex, RelationshipType relId, Direction direction) {
		
		List<Relationship> relationships = personDao.getRelationships(vertex, null, relId);
		relationships.addAll(personDao.getRelationships(null, vertex, relId));
		
		List neighbours = new ArrayList();
		
		switch (direction) {
			case FORWARD:
				// If direction is "FORWARD," loop through all relationships and if
				// rel.getPersonA() is equal to the vertex and
				// rel.getAccessRelationshipType is equal to the relId
				// then add rel.getPersonB() to the list of neighbours
				
				for (Relationship rel : relationships) {
					if (rel.getPersonA().equals(vertex)) {
						neighbours.add(rel.getPersonB().getId());
					}
				}
				break;
			case BACKWARD:
				// If direction is "BACKWARD," loop through all relationships and if
				// rel.getPersonB() is equal to the vertex and
				// rel.getAccessRelationshipType is equal to the relId
				// then add rel.getPersonA() to the list of neighbours
				
				for (Relationship rel : relationships) {
					if (rel.getPersonB().equals(vertex)) {
						neighbours.add(rel.getPersonA().getId());
					}
				}
				break;
			case EITHER:
				// If direction is "EITHER," loop through all relationships and if
				// rel.getPersonA() is equal to the vertex and
				// rel.getAccessRelationshipType is equal to the relId
				// then add rel.getPersonB() to the list of neighbours
				
				// Else if 
				// rel.getPersonB() is equal to the vertex and
				// rel.getAccessRelationshipType is equal to the relId
				// then add rel.getPersonA() to the list of neighbours
				
				for (Relationship rel : relationships) {
					if (rel.getPersonA().equals(vertex)) {
						neighbours.add(rel.getPersonB().getId());
					} else if (rel.getPersonB().equals(vertex)) {
						neighbours.add(rel.getPersonA().getId());
					}
				}
				
				break;
			default:
				break;
		}
		
		return neighbours;
	}
	
	/**
	 * Finds the implicitly defined nighbours, specified by the relation identifier, of the given Person 
	 * @param vertex The person to find the neighbours of
	 * @param relId The relation identifier, an implicitly defined relation identifier
	 * @param direction The direction of the relationship
	 * @return The list of specified neighbours
	 */
	@SuppressWarnings( { "rawtypes" })
	private Iterable getImplicitNeighbours(Object vertex, String relId, Direction direction) {
		// Get the class for the relation identifier
		ImplicitRelationIdentifier iri = RebacSettings.getImplicitRelationIdentifier(relId);
		
		if (iri != null) {
			return iri.findNeighbors(vertex, direction);
		} else {
			log.error("Illegal Implicit Relation Identifier: " + relId);
			throw new IllegalRelationIdentifierException("Illegal Implicit Relation Identifier: " + relId);
		}
	}
	
	/*private RelTypes getRelType(int id) {
		
		switch (id) {
			case 4:
				return RelTypes.test3;
			case 5:
				return RelTypes.gp;
			case 6:
				return RelTypes.agent;
			case 7:
				return RelTypes.referrer;
			case 8:
				return RelTypes.appointTeam;
			case 9:
				return RelTypes.member;
			case 10:
				return RelTypes.registerWard;
			case 11:
				return RelTypes.wardNurse;
		}
		System.out.println("Error in RelType. Id= " + id);
		return null;
	}*/
}
