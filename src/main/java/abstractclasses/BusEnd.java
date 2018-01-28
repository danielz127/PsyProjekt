package abstractclasses;

import dissimlab.monitors.MonitoredVar;
import dissimlab.simcore.BasicSimObj;
import simobject.SimObjBus;
import simobject.SimObjDataQueue;

public abstract class BusEnd extends BasicSimObj{
	protected SimObjDataQueue outputQueue;
	protected SimObjBus bus;



	public abstract boolean reciveBusData(Data busData);
	public abstract String getName();
	
	public BusEnd(SimObjDataQueue outputQueue) {
		this.outputQueue = outputQueue;
	}
	
	public void setBus(SimObjBus bus) {
		this.bus = bus;
	}
	
	public SimObjBus getBus() {
		return bus;
	}
	
	public boolean isNewDataReadyToSend(){
		return !outputQueue.isEmpty();
	}
	
	public Data getBusData(){
		return outputQueue.peek();
	}

	public void removeOutputData(){
		outputQueue.remove();
	}
	
	public MonitoredVar getOutputQueueUsedSizeMVar(){
		return outputQueue.getUsedSpaceSizeMVar();
	}
	
}
