package cz.smartfine.networklayer.model;

/**
 * Obsahuje informace o stavu p�enosn� parkovac� karty (PPK, angl. SPC).
 * @author Pavel Bro�
 * @version 1.0
 * @created 14-4-2012 18:48:49
 */
public class SPCInfo {

	/**
	 * ��slo p�enosn� parkovac� karty, ke kter� se vztahuje informace o stavu.
	 */
	private String spcNumber;
	/**
	 * Stav p�enosn� parkovac� karty tj. zda je hl�ena jako kraden� �i nikoliv.
	 */
	private SPCStatus spcStatus;

	public SPCInfo(){

	}

	
	/**
	 * @param spcNumber
	 * @param spcStatus
	 */
	public SPCInfo(String spcNumber, SPCStatus spcStatus) {
		super();
		this.spcNumber = spcNumber;
		this.spcStatus = spcStatus;
	}


	public void finalize() throws Throwable {

	}

	/**
	 * @return the spcNumber
	 */
	public String getSpcNumber() {
		return spcNumber;
	}

	/**
	 * @param spcNumber the spcNumber to set
	 */
	public void setSpcNumber(String spcNumber) {
		this.spcNumber = spcNumber;
	}

	/**
	 * @return the spcStatus
	 */
	public SPCStatus getSpcStatus() {
		return spcStatus;
	}

	/**
	 * @param spcStatus the spcStatus to set
	 */
	public void setSpcStatus(SPCStatus spcStatus) {
		this.spcStatus = spcStatus;
	}

	
}