import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * <b>Classe PanneauNord</b>
 * <p>
 * classe qui construit le panneau nord de la fenêtre principal.
 * Panneau qui inclut les étiquettes de point ainsi que l'étiquette du chronomètre
 * @author Mohamed Cherifi
 *
 */
public class PanneauNord extends JPanel{
	
	public static ThreadChronometre thChronometre;
	
	JPanel pNord = new JPanel();
	JPanel pJoueur1 = new JPanel();
	JPanel pTemps = new JPanel();
	JPanel pJoueur2 = new JPanel();
	
	JLabel lblTemps = new JLabel();
	JLabel lblJoueur1 = new JLabel("Joueur 1: ");
	JLabel lblJoueur2 = new JLabel("Joueur 2: ");
	JLabel lblScoreJoueur1 = new JLabel("0");
	JLabel lblScoreJoueur2 = new JLabel("0");
	
	Font defaultFont = new Font("Algerian", Font.BOLD, 32);
	Font fontScore = new Font("Algerian", Font.PLAIN, 34);
	
	public PanneauNord(){
		thChronometre = new ThreadChronometre(lblTemps);
		setLayout(new FlowLayout());
		
		pJoueur1.setBorder(BorderFactory.createEtchedBorder(Color.darkGray, Grille.couleurJetonJoeur1));
		pJoueur2.setBorder(BorderFactory.createEtchedBorder(Color.darkGray, Grille.couleurJetonJoeur2));
		pTemps.setBorder(BorderFactory.createEtchedBorder(Color.BLACK, Color.red));
		lblJoueur1.setFont(defaultFont);
		lblScoreJoueur1.setFont(fontScore);
		lblJoueur2.setFont(defaultFont);
		lblScoreJoueur2.setFont(fontScore);
		lblTemps.setFont(defaultFont);
		
		
		pTemps.add(lblTemps);
		pJoueur1.add(lblJoueur1);
		pJoueur1.add(lblScoreJoueur1);
		pJoueur2.add(lblJoueur2);
		pJoueur2.add(lblScoreJoueur2);
		
		add(pJoueur1);
		add(pTemps);
		add(pJoueur2);

	}

	public JLabel getLblScoreJoueur1() {
		return lblScoreJoueur1;
	}

	public void setLblScoreJoueur1(JLabel lblScoreJoueur1) {
		this.lblScoreJoueur1 = lblScoreJoueur1;
	}

	public JLabel getLblScoreJoueur2() {
		return lblScoreJoueur2;
	}

	public void setLblScoreJoueur2(JLabel lblScoreJoueur2) {
		this.lblScoreJoueur2 = lblScoreJoueur2;
	}

}
