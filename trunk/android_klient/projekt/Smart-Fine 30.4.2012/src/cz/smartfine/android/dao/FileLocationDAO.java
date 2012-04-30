package cz.smartfine.android.dao;

import java.util.ArrayList;

import cz.smartfine.android.R;
import cz.smartfine.android.MyApp;
import cz.smartfine.android.dao.interfaces.ILocationDAO;
import cz.smartfine.android.model.Waypoint;
import android.content.Context;

/**
 * Třída pro práci s DAO pro parkovací lískty TODO zmenit javadoc
 * 
 * @author Martin Štajner
 * 
 */
public class FileLocationDAO implements ILocationDAO {

	/**
	 * Kontext aplikace
	 */
	private Context context;
	/**
	 * DAO pro ukládání do souboru
	 */
	private FileDAO dao;
	/**
	 * List v�ech lok�ln� ulo�en�ch PL TODO zmenit
	 */
	private ArrayList<Waypoint> waypoints;
	/**
	 * instance aplikace
	 */
	private MyApp app;

	/**
	 * Konstruktor
	 * 
	 * @param context
	 * @throws Exception
	 */
	public FileLocationDAO(Context context) {
		super();
		this.context = context;
		this.dao = new FileDAO(context);
		this.waypoints = new ArrayList<Waypoint>();
		this.app = (MyApp) context.getApplicationContext();
	}

	public void saveWaypoint(Waypoint waypoint) throws Exception {
		if (!waypoints.contains(waypoint)) {
			waypoints.add(waypoint);
		}
		saveAllWaypoints();

	}

	public void saveAllWaypoints() throws Exception {
		dao.saveObjectToFile(waypoints, context.getString(R.string.file_waypoints));

	}

	public ArrayList<Waypoint> getAllWaypoints() {
		return waypoints;
	}

	public Waypoint getWaypoint(int index) {
		return waypoints.get(index);
	}

	public void deleteAllWaypoints() throws Exception {
		waypoints.clear();
		dao.saveObjectToFile(waypoints, context.getString(R.string.file_waypoints));

	}

	public void deleteWaypoint(Waypoint waypoint) {
		waypoints.remove(waypoint);
	}

	@SuppressWarnings("unchecked")
	public void loadAllWaypoints() throws Exception {
		Object o = dao.loadObjectFromFile(context.getString(R.string.file_waypoints));
		if (o instanceof ArrayList) {
			waypoints = (ArrayList<Waypoint>) o;
		}

	}
}