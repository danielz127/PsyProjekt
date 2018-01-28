package simobject;

import abstractclasses.BusEnd;
import abstractclasses.Data;
import dissimlab.broker.INotificationEvent;
import dissimlab.broker.IPublisher;

public class SimObjTask extends Data{
	private double outputSize;
	private double instructionAmount;
	private double generationTime;
	



	public SimObjTask(double size, double outputSize, double instructionAmount, SimObjStation sender, SimObjServer receiver, double generationTime) {
		super(size, sender, receiver);
		this.instructionAmount = instructionAmount;
		this.outputSize = outputSize;
		this.generationTime = generationTime;
	}
	

	public double getInstructionAmount() {
		return instructionAmount;
	}
	
	
	public double getOutputSize() {
		return outputSize;
	}
	
	public double getGenerationTime() {
		return generationTime;
	}
	
	//--------BusData

	@Override
	public BusEnd getSender() {
		return sender;
	}

	@Override
	public BusEnd getReceiver() {
		return receiver;
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
