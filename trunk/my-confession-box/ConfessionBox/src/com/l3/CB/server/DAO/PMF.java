package com.l3.CB.server.DAO;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public class PMF {

	private static final PersistenceManagerFactory pmfInstance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	private PMF() {
		super();
	}

	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}
}
