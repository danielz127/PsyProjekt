package simevent;

import java.util.LinkedList;
import java.util.Random;

import abstractclasses.BusEnd;
import abstractclasses.Data;
import dissimlab.simcore.BasicSimEvent;
import dissimlab.simcore.SimControlException;
import simobject.SimObjBus;

public class EventOpenBus extends BasicSimEvent<SimObjBus, Object> {

	public EventOpenBus(SimObjBus bus) throws SimControlException {
		super(bus);
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
		SimObjBus bus = getSimObj();
		bus.setOpen(true);
		
		LinkedList<BusEnd> readyBusEnd = new LinkedList<BusEnd>();
		for (BusEnd busEnd : bus.getBusEnds()) {
			if (busEnd.isNewDataReadyToSend()) {
				readyBusEnd.add(busEnd);
			}
		}

		if (readyBusEnd.isEmpty()) {
			bus.getSmo().writeLog(String.format("%8.3f",time) + " - OPEN_BUS - magistrala dostepna ale nie ma zadnych gotowych danych do przeslania.");
		} else {
			bus.getSmo().writeLog(String.format("%8.3f",time) + " - OPEN_BUS - magistrala po otwarciu dostepu zaczela otrzymywac nowe dane");
			Random busEndSelector = new Random();
			// Losowanie wartosci miedzy 0 (wlacznie) a rozmiarem listy (wylacznie)
			BusEnd selectedBusEnd = readyBusEnd.get(busEndSelector.nextInt(readyBusEnd.size()));
			Data data = selectedBusEnd.getBusData();
			//bus.setOpen(false);
			//new EventEndMovingDataThroughBus(bus ,data, bus.calculateSendingTime(data.getDataSize()));
			new EventBusStartMovingData(bus, data);
		}
	}

}
