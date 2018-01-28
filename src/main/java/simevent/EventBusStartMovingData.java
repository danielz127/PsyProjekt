package simevent;

import abstractclasses.Data;
import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import simobject.SimObjBus;

public class EventBusStartMovingData extends BasicSimEvent<SimObjBus, Object>{
	private Data data;
	private SimObjBus bus;
	public EventBusStartMovingData(SimObjBus bus, Data data) throws SimControlException {
		super(bus);
		this.data = data;
		this.bus = bus;
		bus.setOpen(false);
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
		new EventBusEndMovingData(bus, data, bus.calculateSendingTime(data.getDataSize()));
	}
	
}
