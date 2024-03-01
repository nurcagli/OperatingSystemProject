package pckg;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.LinkedList;
import java.util.List;

public class Proses //extends ProsesBuilder
{
	public int askiZamani;
	private int arrivalTime;
	private int priority;
	private int burstTime;
	private int remainingTime;
	public Process process;
	public enum processState{
		ready,
		başladı,
		askıda,
		yürütülüyor,
		sonlandı,
		zamanAşımı
	}
	public processState state;
	public String renk;
	
	
	public Proses(int arrivalTime,int priority,int burstTime)
	{
		ArrayList<String> renkler = new ArrayList<String>();
		Random rnd = new Random();
		   renkler.add("\u001B[35m");
	        renkler.add("\u001B[30m");
	        renkler.add("\u001B[31m");
	        renkler.add("\u001B[32m");
	        renkler.add("\u001B[33m");
	        renkler.add("\u001B[34m");
	        renkler.add("\u001B[35m");
	        renkler.add("\u001B[36m");
	        renkler.add("\u001B[37m");
		
		this.arrivalTime=arrivalTime;
		this.priority=priority;
		this.burstTime=burstTime;
		this.remainingTime=burstTime;
		this.state=processState.ready;
		this.askiZamani=arrivalTime;
		this.renk=renkler.get(rnd.nextInt(renkler.size()-1));
		
		ProcessBuilder build2 = new ProcessBuilder("cmd.exe","/c","echo");
		try {
			process = build2.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	  
	}
	
	
	public void ProsesDurum(int time) {
		
	        
		 System.out.println (this.renk +( time+".0000" +" "+"sn proses "+this.state +"( id:"+this.process.pid()+"  öncelik : "+this.priority+"  kalan süre : "+ this.getRemainingTime() +"sn)")); 	
		
	}
	
	
	public int getArrivalTime() 
	{
		return arrivalTime;
	}
	
	public void incArrivalTime() 
	{
		arrivalTime++;
	}


	public int getPriority() 
	{
		return priority;
	}


	public int getBurstTime() 
	{
		return burstTime;
	}
	
	public int getRemainingTime()
	{
		return remainingTime;
	}
	
	public void updateRemainingTime()
	{ 
		remainingTime--;
	}
	
	public void increasePriority()
	{ 
		priority++;
	}
}

