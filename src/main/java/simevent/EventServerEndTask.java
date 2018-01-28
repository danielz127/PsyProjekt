package simevent;

import dissimlab.monitors.MonitoredVar;
import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import simobject.SimObjServer;
import simobject.SimObjTask;

public class EventServerEndTask extends BasicSimEvent<SimObjServer, Object> {

	public EventServerEndTask(SimObjServer server, double delay) throws SimControlException {
		super(server, delay);
		//server.setWorking(true);
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
		// Warunki czy zadanie moze byc wykonane sa sprawdzane na etapie jego
		// planowania
		
		double time = simTime();
		SimObjServer server = getSimObj();
		

		
		
		server.setWorking(false);
		if(server.doNextTask()){
			//Zmniejszenie ilo�ci zada� w systemie
			MonitoredVar taskAmount = server.getBus().getmVarTaskAmount();
			taskAmount.setValue(taskAmount.getValue()-1, time);
			
			
			server.getBus().getSmo().writeLog(String.format("%8.3f",time) + " - END_TASK - " + server.getName() +
					" wykonal zadanie");
			
			if(server.getBus().isOpen()){
				//server.getBus().setOpen(false);
				//new EventEndMovingDataThroughBus(server.getBus(), server.getBusData(), server.getBus().calculateSendingTime(server.getBusData().getDataSize()));
				new EventBusStartMovingData(server.getBus(), server.getBusData());
			}
		}else{
			server.getBus().getSmo().writeLog(String.format("%8.3f",time) + " - END_TASK - " + server.getName() +
					" nie moge wykonac zadania ze wzgledu na brak miejsca na kolejce wyjsciowej");
		}
		if (server.getNextTask() != null) {
			SimObjTask task = server.getNextTask();
			//server.removeNextTask();
			if (server.getOutputQueueEmptySpaceSize() > task.getOutputSize()) {
				if(server.isInputQueueEmpty() == false && server.isWorking() == false){
					//new EventServerHandleTask(server, server.calculateComputeingTime(task));
					new EventServerStartTask(server);
				}
			} else {
				server.getBus().getSmo().writeLog(String.format("%8.3f",time) + " - END_TASK - " + server.getName()
						+ " ma zadania do rozwiazania ale nie ma miejsca na buforze wyjsciowym");
			}
		} else {
			server.getBus().getSmo().writeLog(String.format("%8.3f",time) + " - END_TASK - " + server.getName() + " nie ma aktualnie zadan do rozwiazania");
		}
	}

}
