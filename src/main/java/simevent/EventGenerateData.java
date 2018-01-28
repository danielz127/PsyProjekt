package simevent;

import dissimlab.monitors.MonitoredVar;
import dissimlab.random.SimGenerator;
import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import simobject.SimObjStation;

public class EventGenerateData extends BasicSimEvent<SimObjStation, Object>{
	private double delayStdDev;
	private double delayExpVal;
	public EventGenerateData(SimObjStation station, double delay, double nextDelayExpVal, double nextDelayStdDev) throws SimControlException {
		super(station, delay);
		delayExpVal = nextDelayExpVal;
		delayStdDev = nextDelayStdDev;
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
		double time = simTime();
		SimObjStation station = getSimObj();
		station.generateTask(time);
		
		//Zwiekszenie ilo�ci zada� w systemie
		MonitoredVar taskAmount = station.getBus().getmVarTaskAmount();
		taskAmount.setValue(taskAmount.getValue()+1, time);
		
		station.getBus().getSmo().writeLog(String.format("%8.3f",time) + " - GENERATE_DATA - " + station.getName() + " wygenerowala nowe zadanie");
		if(station.getBus().isOpen()){
			//station.getBus().setOpen(false);
			//new EventEndMovingDataThroughBus(station.getBus(), station.getBusData(), station.getBus().calculateSendingTime(station.getBusData().getDataSize()));
			new EventBusStartMovingData(station.getBus(), station.getBusData());
		}
		
		
		SimGenerator generator = station.getBus().getSmo().getSimGenerator();
		double delay = generator.normal(delayExpVal, delayStdDev);
		if(delay < 0){
			delay = 0;
		}
		
		new EventGenerateData(station, delay, delayExpVal, delayStdDev);
	}
	
}
