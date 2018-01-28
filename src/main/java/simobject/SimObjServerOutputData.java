package simobject;

import abstractclasses.BusEnd;
import abstractclasses.Data;
import dissimlab.broker.INotificationEvent;
import dissimlab.broker.IPublisher;

public class SimObjServerOutputData extends Data{
	private double taskGenerationTime;



	public SimObjServerOutputData(BusEnd sender, BusEnd receiver,double size, double taskGenerationTime) {
		super(size, sender, receiver);
		this.taskGenerationTime = taskGenerationTime;
	}
	
	public double getTaskGenerationTime() {
		return taskGenerationTime;
	}
	
	
	//---------BasicSimObj

	public boolean filter(IPublisher arg0, INotificationEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}


	public void reflect(IPublisher arg0, INotificationEvent arg1) {
		// TODO Auto-generated method stub
		
	}



}
