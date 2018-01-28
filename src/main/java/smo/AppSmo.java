package smo;

import abstractclasses.BusEnd;
import dissimlab.monitors.ChangesList;
import dissimlab.random.SimGenerator;
import dissimlab.simcore.SimControlEvent;
import dissimlab.simcore.SimControlException;
import dissimlab.simcore.SimManager;
import dissimlab.simcore.SimParameters.SimControlStatus;
import simevent.EventGenerateData;
import simobject.SimObjBus;
import simobject.SimObjServer;
import simobject.SimObjStation;
import simobject.SimObjTask;

public class AppSmo {
	private SimObjServer server;
	private SimObjBus bus;
	private SimGenerator simGenerator = new SimGenerator();
	
	//Ilosc stacji
	private double stationAmount = 120;

	//Rozmiar zadania parametry losowania
	private double expTaskSize = 1000, stdDevTaskSize = 30;
	
	//Wyjsciowy rozmiar zadania po obliczeniu
	private double expTaskOutputSize = 1000, stdDevTaskOutputSize = 100;
	
	//Iloœæ instrukcji zadania
	private double expInstructionAmount = 1000, stDevInstructionAmount = 100;
	
	
	//Parametry opoznienia generowania zadania
	private double expTaskGenerationDelay = 50, stdDevTaskGenerationDelay = 25;
	
	//Przepustowosc magistrali
	private double busThroughput = 5000;
	
	//Parametry serwera
	private double serverComputingPower = 2000;
	private int serverInputQueueSize = 15000, serverOutputQueueSize = 15000;


	public AppSmo() {
		runSimulation(100);
	}

	public void runSimulation(double timeLimit){
		SimManager.resetInstance();
		SimObjServer.resetGlobalIndex();
		SimObjStation.resetGlobalIndex();
		//Tworzenie stacji
		double taskSize, taskOutputSize, taskInstructionAmount;
		//Tworzenie magistrali
		bus = new SimObjBus(busThroughput, this);
		
		//Tworzenie serwera
		server = new SimObjServer(serverComputingPower, serverInputQueueSize, serverOutputQueueSize);
		bus.addBusEnd(server);
		
		for(int i = 0; i< stationAmount; i++){
			taskSize = simGenerator.normal(expTaskSize, stdDevTaskSize);
			taskOutputSize = simGenerator.normal(expTaskOutputSize, stdDevTaskOutputSize);
			taskInstructionAmount = simGenerator.normal(expInstructionAmount, stDevInstructionAmount);
			
			SimObjTask templateTask = new SimObjTask(taskSize, taskOutputSize, taskInstructionAmount, null, server, 0);	
			bus.addBusEnd(new SimObjStation(templateTask));
		}

		
		//ROZPOCZECIE SYMULACJI
		SimManager simManager = SimManager.getInstance();
		try {
			new SimControlEvent(timeLimit, SimControlStatus.STOPSIMULATION);
			for(BusEnd be : bus.getBusEnds()){
				if(be instanceof SimObjStation){
					SimObjStation station = (SimObjStation)be;
					double delay = simGenerator.normal(expTaskGenerationDelay, stdDevTaskGenerationDelay);
					if(delay < 0){
						delay = 0;
					}
					new EventGenerateData(station, delay, expTaskGenerationDelay, stdDevTaskGenerationDelay);
				}
			}
			simManager.startSimulation();
		} catch (SimControlException e) {
			e.printStackTrace();
		}
		writeLog("\n\n\tPodusmowanie\n");
		writeLog("Czasy realizacji okreslonych typow zadan");

		//Wypisanie srednich czasów wykonania zadania
		for(BusEnd be : bus.getBusEnds()){
			if(be instanceof SimObjStation){
				SimObjStation station = (SimObjStation)be;
				ChangesList cl = station.getTaskHandleTimeMVar().getChanges();
				if(cl.size()>0){
					double avg = 0;
					double max;
					double min;
					double last = max = min = cl.get(cl.size()-1).getValue();
					for(int i=0;i<cl.size();i++){
						double currVal = cl.get(i).getValue();
						avg += currVal;
						max = (max < currVal) ? currVal : max;
						min = (min > currVal) ? currVal : min;
					}
					avg = avg/cl.size();
					writeLog(station.getName() + " - Czasy wykonania zadania - "
							+ "srednia:" + String.format("%8.2f",avg)
							+ ", maksimum:" + String.format("%8.2f",max)
							+ ", minimum:" + String.format("%8.2f",min)
							+ ", ostatnie zadanie:" + String.format("%8.2f",last)
							+ ", liczba wykonanych zadañ: " + cl.size()
							+ ", liczba wygenerowanych zadañ: " + station.getGeneratedTasksCounter());
				}else{
					writeLog(station.getName() + " - Info o czasach wykonania zadania - nieznane, brak wykonanych zadan");
				}
				
			}
		}
		
		
		{//Maksymalna liczba zadan w systemie
			writeLog("\nMaksymalna liczba zadan w systemie");
			ChangesList cl = bus.getmVarTaskAmount().getChanges();
			double maxTasks = 0;
			for(int i=0;i< cl.size(); i++){
				maxTasks = (maxTasks < cl.get(i).getValue()) ? cl.get(i).getValue() : maxTasks;
			}
			writeLog("W systemie bylo maksymalnie: " + (int)maxTasks + " zadan");
		}
		
		
		{//Wypisywanie informacji o zajêtoœci magistrali
			writeLog("\n\tZajetosc magistrali");
			ChangesList cl = bus.getIsOpenMVar().getChanges();
			double openTime = 0;//Ile czasu magistrala otwarta
			if(cl.size() > 1){
				for(int i=1;i<cl.size();i++){
					if(cl.get(i-1).getValue() == 1){
						openTime += cl.get(i).getTime() - cl.get(i-1).getTime();
					}
				}
			}else{
				openTime = timeLimit;
			}
			
			writeLog("Czas pracy magistrali: " + String.format("%8.2f",(timeLimit - openTime)));
			writeLog("Czas oczekiwania magistrali: " + String.format("%8.2f",openTime));
			writeLog("Zajetosc magistrali (zajetosc w stosunku do czasu calej symulacji): " + String.format("%8.2f",((timeLimit - openTime)/timeLimit*100)) + "%");
		}

		{//Info o odrzuconych zadaniach przez serwer
			writeLog("\nZadanie odrzucone przez serwer");
			double deniead = server.getTaskDeniedCounter();
			double accepted = server.getTaskAcceptedCounter();
			writeLog("Serwer przyjal zadanie " +
					(int)accepted + " razy  i odrzucil zadanie " +
					(int)deniead + " razy");
			writeLog("Prawdopodobieñstwo odrzucenia zadania to: " + String.format("%8.2f",(deniead/(deniead+accepted))));
			writeLog("*Odrzucone zadanie trafia spowrotem do stacji roboczej i moze zostac wyslane (i odrzucone) ponownie");
		}
		
		{//Info o zajêtoœci bufora wejœciowego serwera
			writeLog("\nBufro wejsiowy serwera");
			ChangesList cl = server.getInputQueueUsedSizeMVar().getChanges();
			if(cl.size() > 0){
				double avg = 0;
				double max;
				double min;
				double last = max = min = cl.get(cl.size()-1).getValue();
				for(int i=0;i<cl.size();i++){
					double currVal = cl.get(i).getValue();
					avg += currVal;
					max = (max < currVal) ? currVal : max;
					min = (min > currVal) ? currVal : min;
				}
				avg = avg/cl.size();
				writeLog("Bufor wejsiowy - zajetosc - min: " + String.format("%8.2f",min)
						+ ", max: " + String.format("%8.2f",max) 
						+ ", srednia: "+ String.format("%8.2f",avg)
						+ ", sstatnia chwila: " + String.format("%8.2f",last));
			}else{
				writeLog("Brak zadan w buforze");
			}
		}
		
		
		
		{//Info o zajêtoœci bufora wyjœciowego serwera
			writeLog("\nBufor wejsciowy serwera");
			ChangesList cl = server.getOutputQueueUsedSizeMVar().getChanges();
			if(cl.size() > 0){
				double avg = 0;
				double max;
				double min;
				double last = max = min = cl.get(cl.size()-1).getValue();
				for(int i=0;i<cl.size();i++){
					double currVal = cl.get(i).getValue();
					avg += currVal;
					max = (max < currVal) ? currVal : max;
					min = (min > currVal) ? currVal : min;
				}
				avg = avg/cl.size();
				writeLog("Bufor wyjsciowy serwera zajetosc - min: " + String.format("%8.2f",min)
					+ ", max: " + String.format("%8.2f",max) 
					+ ", srednia: "+ String.format("%8.2f",avg)
					+ ", ostatnia chwila: " + String.format("%8.2f",last));
			}else{
				writeLog("Do bufora nie dotatlo zadne zadanie");
			}
		}
	}
	
	


	public SimGenerator getSimGenerator() {
		return simGenerator;
	}


	public void writeLog(String message){
		System.out.println(message);
	}
}
