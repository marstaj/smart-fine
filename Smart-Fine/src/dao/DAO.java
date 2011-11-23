package dao;

/**
 * @author Martin Stajner
 *
 */
public class DAO {

	/**
	 * Instance sama sebe - kvuli singletonu
	 */
	static DAO dao;
	
	private DAO(){}
	
	/**
	 * Singleton DAO
	 * @return
	 */
	static DAO getInstance() {
		if (dao == null) {
			dao = new DAO();
		}
		return dao;
	}
}