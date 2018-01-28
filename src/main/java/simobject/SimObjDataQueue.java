package simobject;


import java.util.LinkedList;

import abstractclasses.Data;
import dissimlab.broker.INotificationEvent;
import dissimlab.broker.IPublisher;
import dissimlab.monitors.MonitoredVar;
import dissimlab.simcore.BasicSimObj;

public class SimObjDataQueue extends BasicSimObj{
	LinkedList<Data> queue = new LinkedList<Data>();
	private int size;
	MonitoredVar usedSpaceSizeMVar = new MonitoredVar();

	private boolean isLimited = true;

	public SimObjDataQueue(int size) {//Nadaje rozmiar kolejce
		this.size = size;
		usedSpaceSizeMVar.setValue(0, 0.0);
	}
	
	
	public SimObjDataQueue(){//Zak�ada �e kolejka nie ma limitu
		isLimited = false;
		usedSpaceSizeMVar.setValue(0, 0.0);
	}
	
	public boolean add(Data data){		
		double currSize = getUsedSpaceSize();
		if(isLimited){
			if(currSize + data.getDataSize() > size){
				return false;
			}
		}
		queue.addLast(data);
		usedSpaceSizeMVar.setValue(getUsedSpaceSize(), simTime());
		return true;
	}
	
	public double getEmptySpaceSize(){
		double currSize = 0;
		for(Data t : queue){
			currSize += t.getDataSize();
		}
		return (size-currSize);
	}
	
	public Data remove(){
		Data toReturn = queue.poll();
		usedSpaceSizeMVar.setValue(getUsedSpaceSize(), simTime());
		return toReturn;
	}
	
	public Data peek(){
		return queue.peek();
	}
	
	public double getUsedSpaceSize(){
		double usedSpace = 0;
		for(Data t : queue){
			usedSpace += t.getDataSize();
		}
		return usedSpace;
	}
	
	
	public boolean isEmpty(){
		return queue.isEmpty();
	}
	
	public int countElements(){
		return queue.size();
	}
	
	public boolean isLimited(){
		return isLimited;
	}
	
	public MonitoredVar getUsedSpaceSizeMVar(){
		return usedSpaceSizeMVar;
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
