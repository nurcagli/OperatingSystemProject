package pckg;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

import pckg.Proses.processState;

import java.util.List;

public class MultiLevelQueue {
	
	
	public MultiLevelQueue ( ) 
	{
		
		
	}

	public void processControl(UserJobQueue userjob,int time) {
		for(int i=0;i<userjob.firstQueue.size();i++) {
			Proses p=userjob.firstQueue.get(i);
			if ((time-p.askiZamani)>=20) {
				p.state=processState.zamanAşımı;
				p.process.destroy();
				p.ProsesDurum(time);
				userjob.firstQueue.remove(userjob.firstQueue.indexOf(p));
			} 
		}
		for(int i=0;i<userjob.secondQueue.size();i++) {
			Proses p=userjob.secondQueue.get(i);
			if ((time-p.askiZamani)>=20) {
				p.state=processState.zamanAşımı;
				p.process.destroy();
				p.ProsesDurum(time);
				userjob.secondQueue.remove(userjob.secondQueue.indexOf(p));
			} 
		}
		for(int i=0;i<userjob.thirdQueue.size();i++) {
			Proses p=userjob.thirdQueue.get(i);
			if((time-p.askiZamani)>=20) {
				p.state=processState.zamanAşımı;
				p.process.destroy();
				p.ProsesDurum(time);
				userjob.thirdQueue.remove(userjob.thirdQueue.indexOf(p));
			}
		}
	}

	public int RunMultiLevelQueue(UserJobQueue userjobQueue, int time,int fcfsarrival) throws IOException 
	{
		Iterator<Proses> iteratorUser1 =userjobQueue.firstQueue.iterator(); // ilk kullanici kuyrugu icin iterator
		Iterator<Proses> iteratorUser2 =userjobQueue.secondQueue.iterator();
		Iterator<Proses> iteratorUser3 =userjobQueue.thirdQueue.iterator();
		Proses firstProses=null;
		Proses secondProses=null;
		Proses thirdProses=null;
		
		if(iteratorUser1.hasNext()) firstProses=iteratorUser1.next();  
		else firstProses=new Proses(65000,0,0);
		
		if(iteratorUser2.hasNext()) secondProses=iteratorUser2.next();  
		else secondProses=new Proses(65000,0,0);
		
		if(iteratorUser3.hasNext()) thirdProses=iteratorUser3.next();  
		else thirdProses=new Proses(65000,0,0);
		
		
		if(iteratorUser1!=null && firstProses.getArrivalTime()<=time) // eger ilk kullanici kuyrugu bos degilse ilk prosesi bir sn calissin
		{

				firstProses.state=processState.başladı;
				firstProses.ProsesDurum(time);
				// user1'in kalan zamani azaltilip alt kuyruga gonderilecek.
				firstProses.updateRemainingTime(); // prosessin kalan zamanini 1 sn azaltsin
				firstProses.increasePriority();
				time++;
				processControl(userjobQueue, time);
				
				boolean atildiMi=false;
				int sayac=0;
			
			if(firstProses.getRemainingTime()!=0)
			{
				for(Proses proses :userjobQueue.secondQueue)
				{
					if(proses.getArrivalTime()>=time)
					{
						userjobQueue.secondQueue.add(userjobQueue.secondQueue.indexOf(proses),firstProses);
						firstProses.state=processState.askıda;
						firstProses.askiZamani=time;
						firstProses.ProsesDurum(time);
						sayac++;
						atildiMi=true;
						break;
					}
				}
				if(!atildiMi) {
					userjobQueue.secondQueue.add(firstProses);
					firstProses.state=processState.askıda;
					firstProses.askiZamani=time;
					firstProses.ProsesDurum(time);
				}
			}
			else {
				firstProses.state=processState.sonlandı;
				firstProses.ProsesDurum(time);
				firstProses.process.destroy();
				//userjobQueue.firstQueue.remove(0);// kuyruktaki ilk prosesi kaldirir.
			}
			iteratorUser1.remove();

			if(time>=fcfsarrival) 
			{
					firstProses.state=processState.askıda;
					firstProses.askiZamani=time;
					if(sayac==0)firstProses.ProsesDurum(time);
					return  time;
			}
			
		}
		else if  (iteratorUser2!=null && secondProses.getArrivalTime()<=time )
		{
	
				secondProses.state=processState.başladı;
				secondProses.ProsesDurum(time);
				// user1'in kalan zamani azaltilip alt kuyruga gonderilecek.
				secondProses.updateRemainingTime(); // prosessin kalan zamanini 1 sn azaltsin
				secondProses.increasePriority();
				time++;
				processControl(userjobQueue, time);
				boolean atildiMi=false;
				int sayac=0;
				
				if(secondProses.getRemainingTime()!=0)
				{
					for(Proses proses :userjobQueue.thirdQueue)
					{
						if(proses.getArrivalTime()>=time)
						{
							userjobQueue.thirdQueue.add(userjobQueue.secondQueue.indexOf(proses),secondProses);
							secondProses.state=processState.askıda;
							secondProses.askiZamani=time;
							secondProses.ProsesDurum(time);
							sayac++;
							atildiMi=true;
							break;
						}
					}
					if(!atildiMi) {
						
							userjobQueue.thirdQueue.add(secondProses);
							secondProses.state=processState.askıda;
							secondProses.askiZamani=time;
							secondProses.ProsesDurum(time);
						
					}
				}
				else {
					secondProses.state=processState.sonlandı;
					secondProses.ProsesDurum(time);
					secondProses.process.destroy();
					//userjobQueue.secondQueue.remove(0);// kuyruktaki ilk prosesi kaldirir.
				}
				iteratorUser2.remove();

				if(time>=fcfsarrival)  
				{
						secondProses.state=processState.askıda;
						secondProses.askiZamani=time;
						if(sayac==0)secondProses.ProsesDurum(time);
						System.out.println("ikinci kuyruk fcfsyle kesildi");
					return  time;
				}
				
		}
		else if (iteratorUser3!=null && thirdProses.getArrivalTime()<=time ) // buradaki prosesler 1 sn calisip kuyrugun sonuna yollanıcak , rr modu
		{

			thirdProses.state=processState.başladı;
			thirdProses.ProsesDurum(time);
			// user1'in kalan zamani azaltilip alt kuyruga gonderilecek.
			thirdProses.updateRemainingTime(); // prosessin kalan zamanini 1 sn azaltsin
			int sayac=0;
			time++;
			processControl(userjobQueue, time);
			
			userjobQueue.thirdQueue.remove(0);
				if(thirdProses.getRemainingTime()!=0)
				{
					
						userjobQueue.thirdQueue.add(thirdProses);
						thirdProses.state=processState.askıda;
						thirdProses.askiZamani=time;
						thirdProses.ProsesDurum(time);
						sayac++;
					
				}
				else {
					thirdProses.state=processState.sonlandı;
					thirdProses.ProsesDurum(time);
					thirdProses.process.destroy();
					
				}
				if(time>=fcfsarrival) {
						thirdProses.state=processState.askıda;
						thirdProses.askiZamani=time;
						if(sayac==0)thirdProses.ProsesDurum(time);
					return  time;
				}
		}
		
		return time;
	}
	
	

}

