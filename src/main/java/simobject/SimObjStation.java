package simobject;

import abstractclasses.BusEnd;
import abstractclasses.Data;
import dissimlab.broker.INotificationEvent;
import dissimlab.broker.IPublisher;
import dissimlab.monitors.MonitoredVar;

public class SimObjStation extends BusEnd{
	private static int globalIndex = 0;
	private int index;
	private SimObjTask taskTemplate;
	private MonitoredVar taskHandleDuration = new MonitoredVar();
	private int generatedTasksCounter = 0;




	public static void resetGlobalIndex(){
		globalIndex = 0;
	}

	public SimObjStation(SimObjTask taskTemplate) {
		super(new SimObjDataQueue());
		this.taskTemplate = taskTemplate;
		index = globalIndex++;
	}

	public SimObjTask generateTask(double currTime) {
		SimObjTask task = new SimObjTask(taskTemplate.getDataSize(),
				taskTemplate.getOutputSize(),
				taskTemplate.getInstructionAmount(),
				this, 
				(SimObjServer)taskTemplate.getReceiver(),
				currTime);
		outputQueue.add(task);
		generatedTasksCounter++;
		return task;
	}
	
	public SimObjDataQueue getOutputQueue() {
		return outputQueue;
	}
	
	public MonitoredVar getTaskHandleTimeMVar() {
		return taskHandleDuration;
	}
	
	public int getGeneratedTasksCounter() {
		return generatedTasksCounter;
	}
	
	
	//--------BusEnd

	@Override
	public boolean reciveBusData(Data busData) {//Stacje zawsze maja miejsce na nowe dane
		double currTime = simTime();
		double taskGenerationTime = ((SimObjServerOutputData)busData).getTaskGenerationTime();
		taskHandleDuration.setValue(currTime-taskGenerationTime, currTime);
		return true;
	}
	
	@Override
	public String getName() {
		return "Stacja" + index;
	}
	
	//----------BasicSimObj

	public boolean filter(IPublisher arg0, INotificationEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}


	public void reflect(IPublisher arg0, INotificationEvent arg1) {
		// TODO Auto-generated method stub
		
	}





}
