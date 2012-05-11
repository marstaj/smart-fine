package cz.smartfine.android.dao;

import java.util.ArrayList;

import cz.smartfine.android.R;
import cz.smartfine.android.dao.interfaces.ILocationDAO;
import cz.smartfine.android.model.Waypoint;
import android.content.Context;

/**
 * Třída představující objekt, který ukládá geolokační data a operuje s nimi.
 * 
 * @author Martin Štajner
 * 
 */
public class FileLocationDAO implements ILocationDAO {

	/**
	 * Kontext aplikace
	 */
	private Context appContext;

	/**
	 * Instance objektu pro ukládání do souboru
	 */
	private FileDAO dao;

	/**
	 * Seznam geolokačních dat
	 */
	private ArrayList<Waypoint> waypoints;

	/**
	 * Konstruktor třídy
	 * 
	 * @param context
	 */
	public FileLocationDAO(Context context) {
		super();
		this.appContext = context;
		this.dao = new FileDAO(context);
		this.waypoints = new ArrayList<Waypoint>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.dao.interfaces.ILocationDAO#saveWaypoint(cz.smartfine
	 * .android.model.Waypoint)
	 */
	public synchronized void saveWaypoint(Waypoint waypoint) {
		if (!waypoints.contains(waypoint)) {
			waypoints.add(waypoint);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.ILocationDAO#saveAllWaypoints()
	 */
	public synchronized void saveAllWaypoints() throws Exception {
		dao.saveObjectToFile(waypoints, appContext.getString(R.string.file_waypoints));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.ILocationDAO#getAllWaypoints()
	 */
	public synchronized ArrayList<Waypoint> getAllWaypoints() {
		return waypoints;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.ILocationDAO#getWaypoint(int)
	 */
	public Waypoint getWaypoint(int index) {
		return waypoints.get(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.dao.interfaces.ILocationDAO#deleteAllWaypoints()
	 */
	public synchronized void deleteAllWaypoints() throws Exception {
		waypoints.clear();
		saveAllWaypoints();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * cz.smartfine.android.dao.interfaces.ILocationDAO#deleteWaypoint(cz.smartfine
	 * .android.model.Waypoint)
	 */
	public void deleteWaypoint(Waypoint waypoint) {
		waypoints.remove(waypoint);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.ILocationDAO#loadAllWaypoints()
	 */
	@SuppressWarnings("unchecked")
	public void loadAllWaypoints() throws Exception {
		Object o = dao.loadObjectFromFile(appContext.getString(R.string.file_waypoints));
		if (o instanceof ArrayList) {
			waypoints = (ArrayList<Waypoint>) o;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cz.smartfine.android.dao.interfaces.ILocationDAO#getLastWaypoint()
	 */
	@Override
	public Waypoint getLastWaypoint() {
		if (!waypoints.isEmpty()) {
			return waypoints.get(waypoints.size() - 1);
		}
		return null;
	}
}