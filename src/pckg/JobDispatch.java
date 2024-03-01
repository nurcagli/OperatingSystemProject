package pckg;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import pckg.Proses.processState;

import java.util.Iterator;

public class JobDispatch 
{
	private FCFS fcfs;
	public List<Proses> ProsesList;
	public List<Proses> RealTimeQueue,UserQueue,gecicireal;

	public JobDispatch()
	{
		ProsesList=new ArrayList<Proses>();
		UserQueue=new LinkedList<Proses>();
		RealTimeQueue=new LinkedList<Proses>();
		gecicireal=new LinkedList<Proses>();
		
	}
	
	public void processControl(UserJobQueue userjob,int time) {
		for(int i=0;i<userjob.firstQueue.size();i++) {
			Proses p=userjob.firstQueue.get(i);
			if((time-p.askiZamani)>=20) {
				p.state=processState.zamanAşımı;
				p.process.destroy();
				p.ProsesDurum(time);
				userjob.firstQueue.remove(userjob.firstQueue.indexOf(p));
			} 
		}
		for(int j=0;j<userjob.secondQueue.size();j++) {
			Proses p=userjob.secondQueue.get(j);
			if((time-p.askiZamani)>=20) {
				p.state=processState.zamanAşımı;
				p.process.destroy();
				p.ProsesDurum(time);
				userjob.secondQueue.remove(userjob.secondQueue.indexOf(p));
			} 
		}
		for(int k=0;k<userjob.thirdQueue.size();k++) {
			Proses p=userjob.thirdQueue.get(k);
			if( (time-p.askiZamani)>=20) {
				p.state=processState.zamanAşımı;
				p.process.destroy();
				p.ProsesDurum(time);
				userjob.thirdQueue.remove(userjob.thirdQueue.indexOf(p));
			}
		}
	}
	
	
	public void addProsesToList(Proses Proses)
	{
		ProsesList.add(Proses);
	}
	
	public void scanProsesList ( JobDispatch jobDispatch) // proses listesini dolarasak gercek zamanli ya da user job kuyruguna aktarir.
	{
		int time=0;
		for (Proses Proses : ProsesList) 
		{  
			
			if( Proses.getPriority()==0 && Proses.getArrivalTime()<=time)
			{
				jobDispatch.RealTimeQueue.add(Proses);
				time+= Proses.getBurstTime();
			}
			
			 else if (Proses.getArrivalTime()<=time){
				jobDispatch.UserQueue.add(Proses);
				time++;
			}
		}
	}
	
	public void scanQueues(UserJobQueue userjob) throws IOException //kuyruklari dolasarak konsola yazdiriyoruz
	{
		
		MultiLevelQueue multilevelQueue;
		multilevelQueue = new MultiLevelQueue();
		Iterator<Proses> iterator=RealTimeQueue.iterator();

		int sonuc=0;
		int time=0;
		//realtimequeue yu geziyor
		Proses pro=null;
		if(iterator.hasNext()) pro=iterator.next(); // gercek zamanli kuyruktaki ilk proses aliniyor
		while(true)
		{
			if(pro!=null) // en yuksek oncelıklı kuyruk dolu ıse 
			{
					
					//Proseslerin varis  zamani ile time Ä± karÅŸÄ±laÅŸtÄ±rÄ±yor varÄ±ÅŸ zamanÄ± timeden bÃ¼yÃ¼kse dÃ¶ngÃ¼den Ã§kÄ±yor.
					if(pro.getArrivalTime()>time) // yuksek oncelikli kuyrugun zamani gelmediyse
					{
						time=multilevelQueue.RunMultiLevelQueue(userjob , time,pro.getArrivalTime());
				
						if(pro.getArrivalTime()<=time)
						{
							
							// gecicireal.add(pro);
							int sayacStart=0;
							//burada yuksek oncelikli prosesı direkt calistirsin
							while(pro.getRemainingTime()> 0) // kalan zaman 0 olana kadar calıstırıp kalan zaman guncellensın
							{

								
								if (sayacStart==0)
								{
									pro.state=processState.başladı;
									pro.ProsesDurum(time);
									sayacStart++;
									time++;
									processControl(userjob,time);
								}
								else 
								{
									pro.state=processState.yürütülüyor;
									pro.ProsesDurum(time);
									time++;
									processControl(userjob,time);
								}
								pro.updateRemainingTime();
							}
							//iterator.remove(); 
							
							pro.state=processState.sonlandı;
							pro.ProsesDurum(time);
							pro.process.destroy();
						
							if(iterator.hasNext()) pro=iterator.next();
							else pro=null;
						}

					}
					else
					{
						if(pro.getArrivalTime()>time)
						{
							break;
						}
						// gecicireal.add(pro);
						int sayacStart=0;
						//burada yuksek oncelikli prosesı direkt calistirsin
						while(pro.getRemainingTime()> 0) // kalan zaman 0 olana kadar calıstırıp kalan zaman guncellensın
						{

							if (sayacStart==0)
							{
								pro.state=processState.başladı;
								pro.ProsesDurum(time);
								sayacStart++;
								time++;
								processControl(userjob,time);
							}
							else 
							{
								pro.state=processState.yürütülüyor;
								pro.ProsesDurum(time);
								time++;
								processControl(userjob,time);
							}
							
							pro.updateRemainingTime();
							
							
						}
						//iterator.remove(); 
						
					
						pro.state=processState.sonlandı;
						pro.ProsesDurum(time);
						pro.process.destroy();
						
						if(iterator.hasNext()) pro=iterator.next();
						else pro=null;
						
					}
						
			}
			else {	//realtime boşsa/bitmişse
				if(!userjob.IsEmpty()) {

					time=multilevelQueue.RunMultiLevelQueue(userjob ,time,65000);
				}
			}
		
			
		}iterator.remove();
		
	}


	}
