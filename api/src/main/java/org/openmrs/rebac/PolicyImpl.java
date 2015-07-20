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

import org.openmrs.AccessRelationshipType;
import org.openmrs.BaseOpenmrsMetadata;
import org.openmrs.RelationshipType;

import ca.ucalgary.ispia.rebac.Direction;
import ca.ucalgary.ispia.rebac.Policy;
import ca.ucalgary.ispia.rebac.util.Constants;

/**
 * Abstract implementation of the ReBAC policy 
 */
public abstract class PolicyImpl extends BaseOpenmrsMetadata implements Policy, java.io.Serializable {
	
	public static final long serialVersionUID = 1L;
	
	protected Integer policyImplId;
	
	/**
	 * Default Constructor
	 */
	public PolicyImpl() {
	}
	
	/**
	 * Translates the given policy to primitive variants. The translated policy has the
	 * same semantic definition as the given policy.
	 * @param policy The policy to translate
	 * @return The translated version of the given policy.
	 */
	public static PolicyImpl translate(PolicyImpl policy) {
		PolicyImpl translatedPolicy = null;
		
		if (policy instanceof RequestorImpl) {
			// For Requestor variant.
			// Translate to Variable variant
			translatedPolicy = new VariableImpl(Constants.requestor);
			
		} else if (policy instanceof OwnerImpl) {
			// For Owner variant
			// Translate to Variable variant
			translatedPolicy = new VariableImpl(Constants.owner);
			
		} else if (policy instanceof ResourceImpl) {
			// For Resource
			// Translate to Variable variant
			translatedPolicy = new VariableImpl(Constants.resource);
			
		} else if (policy instanceof VariableImpl) {
			//For Variable variant. Already primitive
			translatedPolicy = policy;
			
		} else if (policy instanceof BindImpl) {
			//For Bind variant.
			//Already primitive, recurse on contained policy
			
			BindImpl temp = (BindImpl) policy;
			PolicyImpl tempA = temp.getPolicy();
			String var = temp.getVariable();
			
			//translate contained policy
			tempA = translate(tempA);
			
			// Create translated policy
			translatedPolicy = new BindImpl(var, tempA);
			
		} else if (policy instanceof AtImpl) {
			//For At variant
			//Already primitive, recurse on contained policy
			
			AtImpl temp = (AtImpl) policy;
			PolicyImpl tempA = temp.getPolicy();
			String var = temp.getVariable();
			
			//translate contained policy
			tempA = translate(tempA);
			
			// Create translated policy
			translatedPolicy = new AtImpl(var, tempA);
			
		} else if (policy instanceof BoxImpl) {
			// Box(policy) = Negation(Diamond(Negation(policy)))
			
			BoxImpl temp = (BoxImpl) policy;
			
			// Get contained fields
			PolicyImpl tempA = temp.getPolicy();
			Object relationID = temp.getRelationIdentifier();
			Direction direction = temp.getDirection();
			// Translate contained policy
			tempA = new NegationImpl(translate(tempA));
			// Create translated policy
			PolicyImpl tempB = null;
			if (relationID instanceof AccessRelationshipType) {
				AccessRelationshipType relId = (AccessRelationshipType) relationID;
				tempB = new DiamondImpl(tempA, relId, direction);
			} else if (relationID instanceof RelationshipType) {
				RelationshipType relId = (RelationshipType) relationID;
				tempB = new DiamondImpl(tempA, relId, direction);
			} else {
				String relId = (String) relationID;
				tempB = new DiamondImpl(tempA, relId, direction);
			}
			
			translatedPolicy = new NegationImpl(tempB);
		}

		else if (policy instanceof ConjunctionImpl) {
			//Already primitive. Recurse on contained policies
			
			ConjunctionImpl temp = (ConjunctionImpl) policy;
			// Get contained fields
			PolicyImpl tempA = temp.getPolicyA();
			PolicyImpl tempB = temp.getPolicyB();
			// Translate contained policies
			tempA = new NegationImpl(translate(tempA));
			tempB = new NegationImpl(translate(tempB));
			// Create translated policy
			PolicyImpl tempC = new DisjunctionImpl(tempA, tempB);
			translatedPolicy = new ConjunctionImpl(tempA, tempB);
			
		}

		else if (policy instanceof DiamondImpl) {
			// Already primitive. Recurse on contained policy.
			
			DiamondImpl temp = (DiamondImpl) policy;
			// Get contained fields
			PolicyImpl tempA = temp.getPolicy();
			Object relationID = temp.getRelationIdentifier();
			Direction direction = temp.getDirection();
			// Translate contained policy
			tempA = translate(tempA);
			// Create translated policy
			if (relationID instanceof AccessRelationshipType) {
				AccessRelationshipType relId = (AccessRelationshipType) relationID;
				translatedPolicy = new DiamondImpl(tempA, relId, direction);
			} else if (relationID instanceof RelationshipType) {
				RelationshipType relId = (RelationshipType) relationID;
				translatedPolicy = new DiamondImpl(tempA, relId, direction);
			} else {
				String relId = (String) relationID;
				translatedPolicy = new DiamondImpl(tempA, relId, direction);
			}
		}

		else if (policy instanceof DisjunctionImpl) {
			// Disjunction(policyA, policyB) =
			//						Negation(Conjunction((Negation(policyA)), (Negation(policyB))))
			
			DisjunctionImpl temp = (DisjunctionImpl) policy;
			// Get contained fields
			PolicyImpl tempA = temp.getPolicyA();
			PolicyImpl tempB = temp.getPolicyB();
			// Translate contained policies
			tempA = new NegationImpl(translate(tempA));
			tempB = new NegationImpl(translate(tempB));
			// Create translated policy
			PolicyImpl tempC = new ConjunctionImpl(tempA, tempB);
			translatedPolicy = new NegationImpl(tempC);
		}

		else if (policy instanceof FalseImpl) {
			// False = Negation(True)
			
			// Create translated policy
			translatedPolicy = new NegationImpl(new TrueImpl());
		}

		else if (policy instanceof NegationImpl) {
			// Already primitive. Recurse on contained policy.
			NegationImpl temp = (NegationImpl) policy;
			// Get contained fields
			PolicyImpl tempA = temp.getPolicy();
			// Translate contained policy
			tempA = translate(tempA);
			// Create translated policy
			translatedPolicy = new NegationImpl(tempA);
		}

		else if (policy instanceof TrueImpl) {
			// For true variant
			translatedPolicy = policy;
		}

		else {
			// Else (not a supported policy variant), throw exception
			//System.out.println(policy.getClass().getName());
			throw new IllegalArgumentException("The given policy is not supported by the Translate method: " + policy);
		}
		
		return translatedPolicy;
	}
	
	// Field accessors
	
	/**
	 * @return The policyImplId
	 */
	public Integer getPolicyImplId() {
		return this.policyImplId;
	}
	
	/**
	 * @param policyImplId the policyImplId to set
	 */
	public void setPolicyImplId(Integer policyImplId) {
		this.policyImplId = policyImplId;
	}
	
	@Override
	public Integer getId() {
		return getPolicyImplId();
	}
	
	@Override
	public void setId(Integer id) {
		setPolicyImplId(id);
	}
	
	public abstract PolicyImpl copy();
	
}
