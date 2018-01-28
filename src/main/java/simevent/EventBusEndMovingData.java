package simevent;

import abstractclasses.Data;
import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import simobject.SimObjBus;
import simobject.SimObjServer;
import simobject.SimObjTask;

public class EventBusEndMovingData extends BasicSimEvent<SimObjBus, Object> {
	private Data data;

	public EventBusEndMovingData(SimObjBus bus, Data data, double delay) throws SimControlException {
		super(bus, delay);
		this.data = data;
		//bus.setOpen(false);
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
		double time = simTime();
		if (data.getReceiver().reciveBusData(data)) {
			getSimObj().getSmo().writeLog(String.format("%8.3f",time) + " - MOVE_DATA - " + data.getReceiver().getName() + " otrzymal dane od "
					+ data.getSender().getName());
			data.getSender().removeOutputData();
			
			//Wiadomosc do serwera
			if(data.getReceiver() instanceof SimObjServer && data instanceof SimObjTask){
				SimObjServer server = (SimObjServer)data.getReceiver();

				if(server.isWorking() == false && server.isInputQueueEmpty() == false){
					new EventServerStartTask(server);
				}
			}
		} else {
			getSimObj().getSmo().writeLog(String.format("%8.3f",time) + " - MOVE_DATA - " + data.getReceiver().getName()
					+ " nie mial miejsca na dane od " + data.getSender().getName());
		}
		new EventOpenBus(this.getSimObj());
	}

	public Object getEventParams() {
		return null;
	}
}
