/**
 * 
 */
package com.amalto.core.objects.configurationinfo.assemble;

public class AssembleConcreteBuilder implements AssembleBuilder {

	
	private final AssembleProc assembleProc = new AssembleProc();

	/** 
	 * This construtor creates a ConcreteBuilder instance.
	 */
	public AssembleConcreteBuilder() {
		super();
	}

	
	public void buildPartCleanJob() {
		
		assembleProc.add(new CleanJobSubProc());
		
	}


	public void buildPartInitData() {
		
		assembleProc.add(new InitDataSubProc());
		
	}


	public void buildPartMigrateVersion() {
		
		assembleProc.add(new MigrateVersionSubProc());
		
	}


	public void buildPartStartEngine() {
		
		assembleProc.add(new StartEngineSubProc());
		
	}
	
	/** 
	 * This method returns the Proc instance.
	 */
	public AssembleProc getAssembleProc() {
		return assembleProc;
	}


}