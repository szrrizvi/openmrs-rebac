package org.openmrs.aop;

import java.util.ArrayList;
import java.util.List;

import org.openmrs.PrivilegeAssignment;
import org.openmrs.api.context.Context;

public class RebacAAUtil {
	
	private static List<PrivilegeAssignment> privilegeAssignments;
	
	private static boolean loaded = false;
	
	public static void load(List<PrivilegeAssignment> paList) {
		
		if (!loaded) {
			privilegeAssignments = new ArrayList<PrivilegeAssignment>();
			
			for (PrivilegeAssignment pa : paList) {
				PrivilegeAssignment temp = new PrivilegeAssignment(pa);
				privilegeAssignments.add(temp);
			}
		}
		
		loaded = true;
	}
	
	public static List<PrivilegeAssignment> getPrivilegeAssignments() {
		List<PrivilegeAssignment> temp = new ArrayList<PrivilegeAssignment>();
		
		for (PrivilegeAssignment pa : privilegeAssignments) {
			PrivilegeAssignment temp2 = new PrivilegeAssignment(pa);
			temp.add(temp2);
		}
		
		return temp;
	}
	
}
