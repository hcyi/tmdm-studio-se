package com.amalto.core.delegator;

import java.util.Collection;

import com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJO;
import com.amalto.core.objects.synchronization.ejb.SynchronizationObjectPOJOPK;
import com.amalto.core.util.XtentisException;


public class SynchronizationObjectCtrlBeanDefaultDelegator implements
		ISynchronizationObjectCtrlBeanDelegator {


	public SynchronizationObjectPOJO existsSynchronizationObject(
			SynchronizationObjectPOJOPK pk) throws XtentisException {
		throw new XtentisException("Not Support!");
	}


	public SynchronizationObjectPOJO getSynchronizationObject(
			SynchronizationObjectPOJOPK pk) throws XtentisException {
		throw new XtentisException("Not Support!");
	}


	public Collection<SynchronizationObjectPOJOPK> getSynchronizationObjectPKs(
			String regex) throws XtentisException {
		throw new XtentisException("Not Support!");
	}


	public SynchronizationObjectPOJOPK putSynchronizationObject(
			SynchronizationObjectPOJO synchronizationObject)
			throws XtentisException {
		throw new XtentisException("Not Support!");
	}


	public SynchronizationObjectPOJOPK removeSynchronizationObject(
			SynchronizationObjectPOJOPK pk) throws XtentisException {
		throw new XtentisException("Not Support!");
	}
	
}