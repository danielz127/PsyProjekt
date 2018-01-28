package simobject;

import java.util.LinkedList;

import abstractclasses.BusEnd;
import dissimlab.broker.INotificationEvent;
import dissimlab.broker.IPublisher;
import dissimlab.monitors.MonitoredVar;
import dissimlab.simcore.BasicSimObj;
import smo.AppSmo;

public class SimObjBus extends BasicSimObj{
	private double throughput;
	private LinkedList<BusEnd> busEnds = new LinkedList<BusEnd>();
	private AppSmo smo;


	//private boolean isOpen = true;
	private MonitoredVar isOpen = new MonitoredVar();//1 - magistrala otwarta, 0 - magistrala zamknieta
	


	private MonitoredVar mVarTaskAmount = new MonitoredVar();
	

	public MonitoredVar getmVarTaskAmount() {
		return mVarTaskAmount;
	}

	public SimObjBus(double throughPut, AppSmo smo) {
		this.throughput = throughPut;
		this.smo = smo;
		isOpen.setValue(1,0.0);
	}
	
	/*
	public void setBusEnds(LinkedList<BusEnd> busEnds) {
		this.busEnds = busEnds;
	}*/
	public void addBusEnd(BusEnd busEnd){
		busEnds.add(busEnd);
		busEnd.setBus(this);
	}
	

	
	public double calculateSendingTime(double dataSize){
		return dataSize/throughput;
	}
	
	public double getThroughPut() {
		return throughput;
	}
	
	public LinkedList<BusEnd> getBusEnds() {
		return busEnds;
	}
	
	public boolean isOpen() {
		if(isOpen.getValue() == 1){
			return true;
		}else{
			return false;
		}
	}

	public void setOpen(boolean isOpen) {
		if(isOpen == true){
			this.isOpen.setValue(1, simTime());
		}else{
			this.isOpen.setValue(0, simTime());
		}
	}
	
	public AppSmo getSmo() {
		return smo;
	}
	
	public MonitoredVar getIsOpenMVar() {
		return isOpen;
	}
	
	
	//------------BasicSimObj

	public boolean filter(IPublisher arg0, INotificationEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}


	public void reflect(IPublisher arg0, INotificationEvent arg1) {
		// TODO Auto-generated method stub
	}
}
