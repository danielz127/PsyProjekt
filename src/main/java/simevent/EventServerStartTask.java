package simevent;

import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import simobject.SimObjServer;
import simobject.SimObjTask;

public class EventServerStartTask extends BasicSimEvent<SimObjServer, Object>{
	
	public EventServerStartTask(SimObjServer server) throws SimControlException {
		super(server);
	}


	public Object getEventParams() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onInterruption() throws SimControlException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onTermination() throws SimControlException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void stateChange() throws SimControlException {
		SimObjServer server = (SimObjServer)getSimObj();
		server.setWorking(true);
		SimObjTask task = server.getNextTask();
		new EventServerEndTask(server, server.calculateComputeingTime(task));
	}
	
}
