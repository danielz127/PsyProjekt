package abstractclasses;

import dissimlab.simcore.BasicSimObj;

public abstract class Data extends BasicSimObj{
	protected double size;
	protected BusEnd sender;
	protected BusEnd receiver;
	
	public Data(double size, BusEnd sender, BusEnd receiver) {
		if(size < 0){
			size = 0;
		}
		this.size = size;
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public double getDataSize(){
		return size;
	}
	public BusEnd getSender(){
		return sender;
	}
	public BusEnd getReceiver(){
		return receiver;
	}
}
