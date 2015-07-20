package org.openmrs.web.controller.rebac;

import org.openmrs.AccessRelationshipType;
import org.openmrs.RelationshipType;
import org.openmrs.api.context.Context;
import org.openmrs.rebac.AtImpl;
import org.openmrs.rebac.BindImpl;
import org.openmrs.rebac.BoxImpl;
import org.openmrs.rebac.ConjunctionImpl;
import org.openmrs.rebac.DiamondImpl;
import org.openmrs.rebac.DisjunctionImpl;
import org.openmrs.rebac.FalseImpl;
import org.openmrs.rebac.IllegalRebacPolicyException;
import org.openmrs.rebac.NegationImpl;
import org.openmrs.rebac.OwnerImpl;
import org.openmrs.rebac.PolicyImpl;
import org.openmrs.rebac.RebacSettings;
import org.openmrs.rebac.RequestorImpl;
import org.openmrs.rebac.ResourceImpl;
import org.openmrs.rebac.TrueImpl;
import org.openmrs.rebac.VariableImpl;
import org.openmrs.rebac.implicitrelations.ImplicitRelationIdentifier;
import org.springframework.web.context.request.WebRequest;

import ca.ucalgary.ispia.rebac.Direction;

public class PolicyParser {
	
	/**
	 * Recursively generates the PolicyImpl object. 
	 * Precondition: The input string, policyStr, represents a well formed ReBAC policy
	 * The structure of a policy string is as follows:
	 * {variant,relationIdentifier,direction,variable,subpolicyA,subpolicyB}
	 * 
	 * If a variant does not use a field, then the corresponding value is the string "null"
	 * 
	 * For example: The policy "Negation(False)" is represented by the string
	 * {"not","null","null","null",{"false","null","null","null",{"null"},{"null"}},{"null"}}
	 * 
	 * 
	 * @param request
	 * @param policyStr The string representing the policy
	 * @return The ReBAC policy represented by policyStr
	 * @throws IllegalRebacPolicyException
	 */
	public static PolicyImpl parsePolicyString(WebRequest request, String policyStr) throws IllegalRebacPolicyException {
		
		//Check if input is null or empty
		if (policyStr == null || policyStr.isEmpty()) {
			return null;
		}
		
		//Trim the surrounding curly braces
		policyStr = policyStr.substring(1, policyStr.length() - 1);
		
		//Check if string is "null"
		if (policyStr.equals("null")) {
			return null;
		}
		
		// Get the start index for substring for childA
		int startChildA = policyStr.indexOf("{");
		
		// Get the start index for substring for childB
		
		//(s2.length() + start + 1);
		
		//Obtain the first part of the string, this part contains the local fields
		//And split the string by "," 
		String valsStr = policyStr.substring(0, startChildA - 1);
		String[] vals = valsStr.split(",");
		
		// Check the variant
		if (vals[0].equals("true")) {
			//"True" variant
			return new TrueImpl();
		} else if (vals[0].equals("false")) {
			//"False" variant
			return new FalseImpl();
		} else if (vals[0].equals("resource")) {
			//"Resource" variant
			return new ResourceImpl();
		} else if (vals[0].equals("requestor")) {
			//"Requestor" variant
			return new RequestorImpl();
		} else if (vals[0].equals("owner")) {
			//"Owner" variant
			return new OwnerImpl();
		} else if (vals[0].equals("not")) {
			//"Negation" variant
			
			//Obtain the substring for subpolicy
			String subPolicy = getChildSubstring(policyStr, startChildA);
			//Recursively generate the subpolicy
			PolicyImpl policyA = parsePolicyString(request, subPolicy);
			
			//Generate and return the policy
			return new NegationImpl(policyA);
		} else if (vals[0].equals("and") || vals[0].equals("or")) {
			//Either Conjunction or Disjunction variant
			
			//Obtain the substring for subpolicy
			String subPolicyA = getChildSubstring(policyStr, startChildA);
			//Recursively generate the subpolicy
			PolicyImpl policyA = parsePolicyString(request, subPolicyA);
			
			//Obtain the start index for substring for childB
			int startChildB = startChildA + subPolicyA.length() + 1;
			//Obtain the substring for subpolicy
			String subPolicyB = getChildSubstring(policyStr, startChildB);
			//Recursively generate the subpolicy
			PolicyImpl policyB = parsePolicyString(request, subPolicyB);
			
			//Generate and return the policy
			if (vals[0].equals("and")) {
				//Conjunction variant
				return new ConjunctionImpl(policyA, policyB);
			} else {
				//Disjunction variant
				return new DisjunctionImpl(policyA, policyB);
			}
		} else if (vals[0].equals("box") || vals[0].equalsIgnoreCase("diamond")) {
			//Either Box or Diamond variant
			
			//Obtain the substring for subpolicy
			String subPolicy = getChildSubstring(policyStr, startChildA);
			//Recursively generate the subpolicy
			PolicyImpl policyA = parsePolicyString(request, subPolicy);
			
			// Get direction
			String dir = request.getParameter(vals[2]);
			Direction direction;
			if (dir.equals("forward")) {
				direction = Direction.FORWARD;
			} else if (dir.equals("backward")) {
				direction = Direction.BACKWARD;
			} else if (dir.equals("either")) {
				direction = Direction.EITHER;
			} else {
				throw new IllegalRebacPolicyException("Illegal direction: " + dir);
			}
			
			// Get relation identifier
			String relId = request.getParameter(vals[1]);
			String[] relIdVal = relId.split(" ");
			
			// Check if the relation identifier is either implicit relation identifier or access relationship type
			// or patient record type
			
			if (relIdVal[0].equals("a:")) {
				// Access Relationship Type identifier
				int artId = Integer.parseInt(relIdVal[1]);
				AccessRelationshipType art = Context.getAccessRelationshipService().getAccessRelationshipType(artId);
				
				if (vals[0].equals("box")) {
					// box variant
					return new BoxImpl(policyA, art, direction);
				} else {
					// diamond variant
					return new DiamondImpl(policyA, art, direction);
				}
				
			} else if (relIdVal[0].equals("p:")) {
				// Patient Record Type identifier
				int rtId = Integer.parseInt(relIdVal[1]);
				RelationshipType rt = Context.getPersonService().getRelationshipType(rtId);
				
				if (vals[0].equals("box")) {
					// box variant
					return new BoxImpl(policyA, rt, direction);
				} else {
					// diamond variant
					return new DiamondImpl(policyA, rt, direction);
				}
				
			} else {
				// relIdValp[0].equals("i:")
				// Implicit Relational identifier
				
				// Check if the relation identifier is valid: 
				// 1) The implicit relation identifier must exist in the system
				// 2) If this policy specified either Backwards or Either directions,
				// then the corresponding Implicit Relation Identifier class must support Backwards direction.
				ImplicitRelationIdentifier iri = RebacSettings.getImplicitRelationIdentifier(relIdVal[1]);
				if (iri == null) {
					throw new IllegalRebacPolicyException("Implicit Relation Identifier does not exist: " + relIdVal[1]);
				} else {
					if (direction != Direction.FORWARD && !iri.supportsBackwardsDirection()) {
						throw new IllegalRebacPolicyException(
						        "Implicit Relation Identifier does not support backwards direction: " + relIdVal[1]);
					}
				}
				
				if (vals[0].equals("box")) {
					// box variant
					return new BoxImpl(policyA, relIdVal[1], direction);
				} else {
					// diamond variant
					return new DiamondImpl(policyA, relIdVal[1], direction);
				}
			}
			
		} else if (vals[0].equals("bind") || vals[0].equals("at")) {
			//Either Bind or At variant
			
			//Extract variable name
			String varName = request.getParameter(vals[3]);
			
			//Obtain the substring for subpolicy
			String subPolicy = getChildSubstring(policyStr, startChildA);
			//Recursively generate the subpolicy
			PolicyImpl policyA = parsePolicyString(request, subPolicy);
			
			//Generate and return the policy
			if (vals[0].equals("bind")) {
				//Bind variant
				return new BindImpl(varName, policyA);
			} else {
				//At variant
				return new AtImpl(varName, policyA);
			}
		} else if (vals[0].equals("variable")) {
			//Variable variant
			
			//Extract variable name
			String varName = request.getParameter(vals[3]);
			
			//Generate and return the policy
			return new VariableImpl(varName);
		} else {
			throw new IllegalRebacPolicyException("Illegal Policy variant: " + vals[0]);
		}
	}
	
	/**
	 * Returns the substring representing the subpolicy starting at startIndex
	 * Precondition: The string correctly represents a well formed policy
	 * @param policyStr The parent policy
	 * @param startIndex The start index of the subpolicy
	 * @return the substring representing the subpolicy starting at startIndex
	 */
	private static String getChildSubstring(String policyStr, int startIndex) throws IllegalRebacPolicyException {
		int pos = startIndex + 1; //Starting position
		int count = 1; //Counter variable
		boolean done = false; //Loop checker
		
		// The subpolicy is enclosed within "{" and "}"
		//Therefore, starting at position startIndex, count the number of "{" and "}"
		// where "{" = +1 and "}" = -1. When the count equals 0, that denotes the end
		// of the sub policy
		
		while (!done) {
			if (policyStr.charAt(pos) == '{') {
				//Increment counter if "{" is encountered
				count++;
			} else if (policyStr.charAt(pos) == '}') {
				//Decrement counter if "}" is encountered
				count--;
			}
			
			if (count == 0) {
				//If the counter is 0, then policy is done
				done = true;
			}
			
			//Increment position
			pos++;
			
			//If reached the end of string, that means that the string is not well formed
			if (pos > policyStr.length()) {
				throw new IllegalRebacPolicyException("Policy strnig not well formed: " + policyStr);
			}
		}
		//Obtain and return the substring denoting the subpolicy starting at
		//startIndex
		return policyStr.substring(startIndex, pos);
	}
}
