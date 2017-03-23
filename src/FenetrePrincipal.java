import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * <b>Classe de la fenetre principal.</b>
 * <p>
 * Fenetre Principal qui implemente un KeyListener elle contient donc les
 * methode du KeyListener comme Keypressed etc.
 * 
 * @author Mohamed Cherifi
 *
 */
public class FenetrePrincipal extends JFrame implements KeyListener {

	// Panneaux
	JPanel pJoueur1 = new JPanel();
	JPanel pTemps = new JPanel();
	JPanel pJoueur2 = new JPanel();
	JPanel pSouth = new JPanel();
	JPanel pAction = new JPanel();
	JPanel pChronometre = new JPanel();
	JPanel pPause = new JPanel();

	// MenuBar
	JMenuBar menuBar = new JMenuBar();
	JMenu menuHelp = new JMenu("Options");
	JMenuItem miNouvPartie = new JMenuItem("Nouvel Partie");
	JMenuItem miQuitter = new JMenuItem("Quitter");

	// Boutons
	JButton btnQuitter = new JButton("Quitter");
	JButton btnNouvPartie = new JButton("Nouvelle Partie");
	JButton btnChronometre = new JButton("Pause");

	PanneauNord pNord = new PanneauNord();

	public static Grille grille;

	int intTours = 0;
	int intIndex = 3;
	int intVictoireJoueur1 = 0;
	int intVictoireJoueur2 = 0;
	boolean booVictoire = false;

	Thread threadDescendre;
	Thread threadAnimationVictoire;

	listenerQuitter listenerQuitter = new listenerQuitter();
	listenerNouvellePartie listenerNouvPartie = new listenerNouvellePartie();

	public FenetrePrincipal() {
		addKeyListener(this);
		setTitle("Connect4 - Projet 3 Mohamed Cherifi");
		setSize(720, 910);
		setLayout(new BorderLayout());

		btnChronometre.setEnabled(false);
		btnNouvPartie.setEnabled(false);
		btnQuitter.setEnabled(false);

		setJMenuBar(menuBar);
		menuBar.add(menuHelp);
		menuHelp.add(miNouvPartie);
		menuHelp.setMnemonic('O');
		menuHelp.addSeparator();
		menuHelp.add(miQuitter);
		miQuitter.setMnemonic('Q');

		miNouvPartie.addActionListener(listenerNouvPartie);
		miQuitter.addActionListener(listenerQuitter);

		pAction.setBorder(BorderFactory.createTitledBorder("Actions"));
		pChronometre.setBorder(BorderFactory.createTitledBorder("Chrono"));

		pAction.add(btnNouvPartie);
		pAction.add(btnQuitter);
		pChronometre.add(btnChronometre);

		pSouth.add(pAction);
		pSouth.add(pChronometre);

		grille = new Grille();

		btnNouvPartie.doClick();
		grille.setCouleurGrilleHaut(1, 3);

		add(pNord, BorderLayout.NORTH);
		add(pPause, BorderLayout.CENTER);
		add(grille, BorderLayout.CENTER);
		add(pSouth, BorderLayout.SOUTH);

		btnNouvPartie.addActionListener(listenerNouvPartie);
		btnQuitter.addActionListener(listenerQuitter);

		btnChronometre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				if (btnChronometre.getText() == "Pause") {
					grille.repaint();
					pNord.thChronometre.pause();
					pNord.repaint();
					btnChronometre.setText("Demarrer");
					pSouth.requestFocus();
				} else if (btnChronometre.getText() == "Demarrer") {
					pNord.thChronometre.reprendre();
					pNord.repaint();
					grille.repaint();
					btnChronometre.setText("Pause");
					methodeRequestFocus();

				}

			}
		});

		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		this.requestFocus();
	}

	/**
	 * <b>Classe prive quitter.</b>
	 * 
	 * Demande a l'utilisateur s’il veut quitter ou pas effectue l'action voulue
	 * selon le choix
	 * 
	 * @author Mohamed Cherifi
	 *
	 */
	private class listenerQuitter implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String[] buttons = { "Oui", "Non" };
			pNord.thChronometre.pause();
			btnChronometre.setEnabled(false);

			int reponse = JOptionPane.showOptionDialog(null, "Voulez-vous quitter?", "Quitter",
					JOptionPane.INFORMATION_MESSAGE, 3, null, buttons, buttons[1]);
			if (reponse == 0) {
				System.exit(0);
			} else {
				if (!pNord.lblTemps.getText().equalsIgnoreCase("00 : 00 : 00")) {
					pNord.thChronometre.reprendre();
					btnChronometre.setEnabled(true);
				}

				methodeRequestFocus();
			}
		}

	}

	/**
	 * <b>Classe privée nouvelle partie qui implémente un ActionListener </b>
	 * 
	 * Classe qui a pour but de demander si l'utilisateur veut commencer une
	 * nouvelle partie, commence par vidée les deux grille puis réinitialise les
	 * variables concerne.
	 * 
	 * @author Mohamed Cherifi
	 *
	 */
	private class listenerNouvellePartie implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String[] buttons = { "Oui", "Non" };
			pNord.thChronometre.pause();
			btnChronometre.setEnabled(false);

			if (booVictoire == false) {
				int reponse = JOptionPane.showOptionDialog(null, "Voulez-vous commencer une nouvelle partie?",
						"Nouvelle partie", JOptionPane.INFORMATION_MESSAGE, 3, null, buttons, buttons[0]);
				if (reponse == 0) {
					intTours = 0;
					pNord.thChronometre.arreter();

					for (int i = 0; i < grille.tabCouleurJetonsHaut.length; i++) {
						grille.tabCouleurJetonsHaut[i] = 0;
					}

					for (int i = 0; i < grille.tabCouleurJetonsGrille.length; i++) {
						for (int j = 0; j < grille.tabCouleurJetonsGrille[i].length; j++) {
							grille.tabCouleurJetonsGrille[i][j] = 0;

						}
					}
					grille.setCouleurGrilleHaut(1, 3);
				} else {
					if (!pNord.lblTemps.getText().equalsIgnoreCase("00 : 00 : 00")) {
						pNord.thChronometre.reprendre();
						btnChronometre.setEnabled(true);
					}

				}
			} else {
				intTours = 0;
				for (int i = 0; i < grille.tabCouleurJetonsHaut.length; i++) {
					grille.tabCouleurJetonsHaut[i] = 0;
				}

				for (int i = 0; i < grille.tabCouleurJetonsGrille.length; i++) {
					for (int j = 0; j < grille.tabCouleurJetonsGrille[i].length; j++) {
						grille.tabCouleurJetonsGrille[i][j] = 0;

					}
				}
				grille.setCouleurGrilleHaut(1, 3);
			}

			intIndex = 3;
			methodeRequestFocus();
			repaint();
		}

	}

	public void methodeRequestFocus() {
		this.requestFocus();
	}

	public void demarerChrono() {

		if (pNord.thChronometre.getState().equals(Thread.State.NEW)) {
			pNord.thChronometre.demarrer();
		} else {
			pNord.thChronometre.arreter();
			pNord.thChronometre = new ThreadChronometre(pNord.lblTemps);
			pNord.thChronometre.demarrer();
		}
	}

	boolean booNull = true;
	
	private class taskDescendre implements Runnable {

		boolean booThreadDescendre = false;
		
		public void endThread() {
			booThreadDescendre = true;
		}
		
		public void startThread() {
			booThreadDescendre = false;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!booThreadDescendre) {
				if (grille.tabCouleurJetonsGrille[intIndex][0] == 0) {
					descendre(intTours % 2 == 0 ? 1 : 2);
					intTours++;
				}
			}
			
		}
		
	}
	
	private class taskAnimationVictoire implements Runnable {

		boolean booThreadVictoire = false;

		public void endThread() {
			booThreadVictoire = true;
		}
		
		public void startThread() {
			booThreadVictoire = false;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!booThreadVictoire) {
				verifierVictoireJoueur();
			}

		
		}
		
	}
	taskDescendre taskDescendre = new taskDescendre();
	taskAnimationVictoire taskAnimation = new taskAnimationVictoire();

	/**
	 * Mouvement vers Gauche.
	 * 
	 * @see #verifierVictoireJoueur
	 * @see #verificationNull
	 * 
	 */
	@Override
	public synchronized void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		// boolean booVrai = false;
		booNull = true;

		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_DOWN:
			// Touche Bas
			if (intTours == 0) {
				demarerChrono();
			}
			btnChronometre.setEnabled(true);
			
			/*taskAnimation.startThread();
			taskDescendre.startThread();
			threadDescendre = new Thread(taskDescendre);
			threadAnimationVictoire = new Thread(taskAnimation);
			threadAnimationVictoire.start();
			threadDescendre.start();*/
			threadDescendre = new Thread(new Runnable() {
				boolean booThreadDescendre = false;

				public void endThread() {
					booThreadDescendre = true;
				}

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if (!booThreadDescendre) {

						if (grille.tabCouleurJetonsGrille[intIndex][0] == 0) {
							descendre(intTours % 2 == 0 ? 1 : 2);
							intTours++;
						}
					}

					threadAnimationVictoire = new Thread(new Runnable() {
						boolean booThreadVictoire = false;

						public void endThread() {
							booThreadVictoire = true;
						}

						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (!booThreadVictoire) {
								verifierVictoireJoueur();
							}

						}
					});
					threadAnimationVictoire.start();

					if (booMethodeExecuter == false) {
						verificationNull();
					}

				}
			});
			threadDescendre.start();
			break;

		case KeyEvent.VK_LEFT:
			// Touche Gauche

			mouvementGauche(intTours % 2 == 0 ? 1 : 2);
			break;
		case KeyEvent.VK_RIGHT:
			// Touche Droite

			mouvementDroite(intTours % 2 == 0 ? 1 : 2);
			break;
		case KeyEvent.VK_ESCAPE:
			booVictoire = false;
			btnNouvPartie.doClick();
			break;
		case KeyEvent.VK_Q:
			btnQuitter.doClick();
			break;
		}
	}

	/**
	 * <b> Vérifier si la partie est nulle.</b>
	 * <p>
	 * Parcourt toute la grille et vérifie si elle est complètement remplie et
	 * que la méthode victoire na pas été déclenche puis affiche un message qui
	 * demande a l'utilisateur s’il veut recommencer une partie ou pas
	 */
	public synchronized void verificationNull() {
		System.out.println(booMethodeExecuter);
		System.out.println(intTours);
		if (intTours == 42) {
			String[] buttons = { "Rejouer", "Quitter" };
			int reponse = JOptionPane.showOptionDialog(null, "Partie nulle!\nVoulez-vous recommencer une partie?",
					"Nulle!", JOptionPane.INFORMATION_MESSAGE, 3, null, buttons, buttons[0]);
			if (reponse == 0) {
				pNord.thChronometre.lblTemps.setText("00 : 00 : 00");
				booVictoire = true;
				btnNouvPartie.doClick();
			} else {
				System.exit(0);
			}
		}
	}

	/**
	 * Mouvement vers Gauche.
	 * 
	 * @see #setCouleurGrilleHaut
	 * 
	 */
	public synchronized void mouvementGauche(int Joueur) {

		for (int i = 0; i < grille.tabCouleurJetonsHaut.length; i++) {
			if (grille.tabCouleurJetonsHaut[i] == Joueur) {
				if (i - 1 >= 0) {
					grille.setCouleurGrilleHaut(Joueur, i - 1);
					intIndex = i - 1;
				}

			}
		}
	}

	public synchronized void mouvementDroite(int Joueur) {

		for (int i = grille.tabCouleurJetonsHaut.length - 1; i >= 0; i--) {
			if (grille.tabCouleurJetonsHaut[i] == Joueur) {
				if (i + 1 <= 6) {
					grille.setCouleurGrilleHaut(Joueur, i + 1);
					intIndex = i + 1;
				}
			}
		}
	}

	/**
	 * Mouvement vers Gauche.
	 * 
	 * @see Grille#setCouleurGrilleHaut
	 * @see Grille#setCouleurGrille
	 * 
	 */
	public synchronized void descendre(int joueur) {
		btnChronometre.setEnabled(true);
		boolean booVrai = false;
		long lngDelai2 = 25;

		grille.setCouleurGrilleHaut(joueur == 1 ? 2 : 1, intIndex);
		for (int colonne = grille.tabCouleurJetonsGrille.length - 1; colonne >= 0; colonne--) {
			for (int ligne = grille.tabCouleurJetonsGrille[colonne].length - 1; ligne >= 0; ligne--) {

				if (grille.tabCouleurJetonsGrille[intIndex][ligne] == 0 && booVrai == false) {

					for (int i = 0; i <= ligne; i++) {
						FenetrePrincipal.grille.tabCouleurJetonsGrille[intIndex][i] = joueur;

						if (i != 0) {
							FenetrePrincipal.grille.tabCouleurJetonsGrille[intIndex][i - 1] = 0;
						}

						FenetrePrincipal.grille.repaint();

						try {
							Thread.sleep(lngDelai2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					grille.setCouleurGrille(joueur, intIndex, ligne);
					booVrai = true;
				}

			}
		}
	}

	/**
	 * Véfification de victoire.
	 * http://stackoverflow.com/questions/5011291/usage-of-see-in-javadoc
	 * 
	 * @see #keyPressed
	 * @see ThreadChronometre#arreter
	 */

	boolean booMethodeExecuter = false;

	public synchronized void verifierVictoireJoueur() {
		// TODO Auto-generated method stub
		int intNumJoueur = 0;
		if (grille.verifierVictoire(grille.tabCouleurJetonsGrille) == 1
				|| grille.verifierVictoire(grille.tabCouleurJetonsGrille) == 2) {
			booMethodeExecuter = true;
			btnChronometre.setEnabled(false);
			if (grille.verifierVictoire(grille.tabCouleurJetonsGrille) == 1) {
				intNumJoueur = 1;
				intVictoireJoueur1++;
				pNord.lblScoreJoueur1.setText(Integer.toString(intVictoireJoueur1));
			} else if (grille.verifierVictoire(grille.tabCouleurJetonsGrille) == 2) {
				intNumJoueur = 2;
				intVictoireJoueur2++;
				pNord.lblScoreJoueur2.setText(Integer.toString(intVictoireJoueur2));
			}
			taskDescendre.endThread();
			taskAnimation.endThread();

			pNord.thChronometre.pause();

			booNull = false;
			String[] buttons = { "Rejouer", "Quitter" };

			int reponse = JOptionPane.showOptionDialog(null,
					"Joueur " + Integer.toString(intNumJoueur)
							+ " a gagné!\nVoulez-vous quitter ou recommencer une partie?",
					"Victoire!", JOptionPane.INFORMATION_MESSAGE, 3, null, buttons, buttons[0]);
			if (reponse == 0) {
				Grille.couleurJetonJoeur1 = new Color(0, 191, 255);
				Grille.couleurJetonJoeur2 = new Color(255, 215, 0);
				grille.repaint();
				booMethodeExecuter = false;
				pNord.thChronometre.arreter();
				pNord.lblTemps.setText("00 : 00 : 00");
				booVictoire = true;
				btnNouvPartie.doClick();

			} else {
				System.exit(0);
			}
		}

	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}
}
