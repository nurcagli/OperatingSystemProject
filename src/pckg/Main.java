package pckg;


import java.io.File;

import java.io.IOException;

public class Main 
{
	  

	public static void main(String[] args) throws IOException 
	{
	
       // System.out.println(renkler.get(this.arrivalTime +" "+ this.priority +" " + this.burstTime + " " + this.getRemainingTime() + " ");
		DataReader dataReader=new DataReader(new File(args[0]));
		JobDispatch jobDispatch=new JobDispatch();
		UserJobQueue userjob = new UserJobQueue();
		
		dataReader.read(jobDispatch); // data reader data.txt yi satir satir okur. Job dispatch nesnesi ile okuma sonunda job dispatchde bulunan Proses listesine ekleme yapar.
		jobDispatch.scanProsesList(jobDispatch); // olusturdugumuz Proses listesini dolasarak prosesleri gercek zamanli kuyruga ya da user job kuyruguna ekler.
		userjob.SendMultiLevelQueue(jobDispatch); // userjob nesnesi, az once olusturudugumuz user job kuyrugundaki prosesleri onceliklerine gore multilevel kuyruklara yerlestirir.
		
		jobDispatch.scanQueues(userjob); // tum kuyruklar dolasilir ve oncelikleri konsola yazdirilir.
	
	
		//jobDispatch.writeProses();
	}
	

}
