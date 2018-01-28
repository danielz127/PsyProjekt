package simobject;

import abstractclasses.BusEnd;
import abstractclasses.Data;
import dissimlab.broker.INotificationEvent;
import dissimlab.broker.IPublisher;
import dissimlab.monitors.MonitoredVar;

public class SimObjServer extends BusEnd{
	private static int globalIndex = 0;
	private int index;
	private double computingPower;
	

	private MonitoredVar isWorking = new MonitoredVar();//1 - pracuje (zaj�ty) ; 0 - nie pracuje (wolny)

	private int taskAcceptedCounter = 0;
	private int taskDeniedCounter = 0;


	private SimObjDataQueue inputQueue;
	
	public static void resetGlobalIndex(){
		globalIndex = 0;
	}
	
	
	public SimObjServer(double computingPower, int inputQueueSize, int outputQueueSize) {
		super(new SimObjDataQueue(outputQueueSize));
		this.computingPower = computingPower;
		this.inputQueue = new SimObjDataQueue(inputQueueSize);
		this.index = globalIndex++;
		isWorking.setValue(0, 0.0);
	}
	
	
	public int coutnImputTasks(){
		return inputQueue.countElements();
	}
	
	public int countOutputTasks(){
		return outputQueue.countElements();
	}
	
	public boolean isInputQueueEmpty(){
		return inputQueue.isEmpty();
	}
	
	public boolean doNextTask(){
		SimObjTask task = (SimObjTask)inputQueue.peek();
		
		//Odwrocenie nadawcy i odbiorcy i nadanie nowego rozmiaru
		Data outputData = new SimObjServerOutputData(task.getReceiver(),
				task.getSender(),
				task.getOutputSize(),
				task.getGenerationTime());
		
		if(outputQueue.add(outputData)){
			inputQueue.remove();//Zadanie z kolejki wejsciwoej usuniete dobiero jak uda sie dodac cos na wyjsciowa
			return true;
		}else{
			return false;//NIe uda�o si� doda� elementu na kolejke wyjsciowa
		}
	}
	
	public double getOutputQueueEmptySpaceSize(){
		return outputQueue.getEmptySpaceSize();
	}
	
	public double getComputingPower() {
		return computingPower;
	}
	
	public double calculateComputeingTime(SimObjTask data){
		double dataSize = data.getInstructionAmount();
		return dataSize/getComputingPower();
	}
	
	public SimObjTask getNextTask(){
		return (SimObjTask)inputQueue.peek();
	}
	
	@Deprecated
	public void removeNextTask(){
		inputQueue.remove();
	}
	
	public boolean isWorking() {
		if(isWorking.getValue() == 1){
			return true;
		}else{
			return false;
		}
	}
	
	public MonitoredVar getIsWorkingMVar() {
		return isWorking;
	}

	public void setWorking(boolean isWorking) {
		if(isWorking == true){
			this.isWorking.setValue(1,simTime());
		}else{
			this.isWorking.setValue(0,simTime());
		}
	}
	
	public int getTaskAcceptedCounter() {
		return taskAcceptedCounter;
	}


	public int getTaskDeniedCounter() {
		return taskDeniedCounter;
	}
	
	public MonitoredVar getInputQueueUsedSizeMVar(){
		return inputQueue.getUsedSpaceSizeMVar();
	}
	
	//---------BusEnd
	@Override
	public boolean reciveBusData(Data busData) {
		if(busData instanceof SimObjTask){//Wszystko co nie jest zadaniem nie musi byc obsluzone
			if(inputQueue.add(busData)){
				taskAcceptedCounter++;
				return true;
			}else{
				taskDeniedCounter++;
				return false;
			}
		}
		return false;
	}
	
	@Override
	public String getName() {
		return "Serwer" + index;
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
