import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JLabel;

public class ThreadChronometre extends Thread {
	JLabel lblTemps;
	boolean blnDemarre;
	long lngTempsInitial;
	long lngTempsEcoule;

	public ThreadChronometre(JLabel lblTemps) {
		this.lblTemps = lblTemps;
		lblTemps.setText("00 : 00 : 00");
	}
	
	public synchronized void  pause()
	{
		this.lngTempsEcoule = System.currentTimeMillis()-lngTempsInitial;
		blnDemarre=false;
	}
	public synchronized void reprendre()
	{
		lngTempsInitial = System.currentTimeMillis()-lngTempsEcoule;
		blnDemarre=true;
	}

	public synchronized void demarrer() {
		blnDemarre = true;
		lngTempsInitial = System.currentTimeMillis();
		lngTempsEcoule = 0L;
		start();
	}

	public synchronized void arreter() {
		blnDemarre = false;
		lngTempsEcoule = 0L;
		lblTemps.setText("00 : 00 : 00");
		lblTemps.repaint();
	}

	public void run() {
		while (true) {
			synchronized (ThreadChronometre.this) {

				if (blnDemarre) {
					
				GregorianCalendar maDate = new GregorianCalendar();
				lngTempsEcoule = System.currentTimeMillis() - lngTempsInitial;
				maDate.setTimeInMillis(lngTempsEcoule);
				DecimalFormat df = new DecimalFormat("00");
				DecimalFormat df2 = new DecimalFormat("#000");

				lblTemps.setText(df.format(maDate.get(Calendar.MINUTE)) + " : " + df.format(maDate.get(Calendar.SECOND))
						+ " : " + df2.format(maDate.get(Calendar.MILLISECOND)).substring(0, 2));
				}
			}
		}
	}

}
