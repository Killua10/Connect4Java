import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.Timer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Grille extends JPanel {

	// Tableau Rectangle & CouleurJetons
	private Rectangle tabRectHaut[] = new Rectangle[7];
	static int[] tabCouleurJetonsHaut = new int[7];
	private Rectangle tabRect[][] = new Rectangle[7][6];
	static int[][] tabCouleurJetonsGrille = new int[7][6];
	private int X = 5;
	private int Y = 10;
	private int largeurRectangle = 100;
	private int hauteurRectangle = 100;
	private int largeurCercle = 90;
	private int hauteurCercle = 90;
	private int XCercle = 5;
	private int YCercle = 5;

	private int intLigne;
	private int intColonne;
	private int intIndex;
	
	FenetrePrincipal fenetre;

	static Color couleurJetonJoeur1 = new Color(0, 191, 255);
	static Color couleurJetonJoeur2 = new Color(255, 215, 0);
	Color couleurVictoire = new Color(220, 20, 60);
	private Color couleurGrille = new Color(112, 128, 144);

	public Grille() {
		int espaceX = largeurRectangle;
		int espaceY = hauteurRectangle;

		// Création des rectangle de la rangée du haut
		for (int i = 0; i < tabRectHaut.length; i++) {
			tabRectHaut[i] = new Rectangle(X + espaceX * i, Y, largeurRectangle, hauteurRectangle);
			tabCouleurJetonsHaut[i] = 0;
		}

		// Création des rectangle de la grille de jeu
		for (int i = 0; i < tabRect.length; i++) {

			for (int j = 0; j < tabRect[i].length; j++) {
				tabRect[i][j] = new Rectangle(X + espaceX * i, Y + hauteurRectangle + 10 + espaceY * j,
						largeurRectangle, hauteurRectangle);
				tabCouleurJetonsGrille[i][j] = 0;
			}

		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		// Dessiner les rectangle de la rangée du haut
		for (int i = 0; i < tabRectHaut.length; i++) {
			g.setColor(couleurGrille);

			// Couleur par defaut des cercle
			g.setColor(Color.WHITE);

			// Vérification de la couleur des cercle
			if (tabCouleurJetonsHaut[i] == 0) {
				g.setColor(this.getBackground());
			} else if (tabCouleurJetonsHaut[i] == 1) {
				g.setColor(couleurJetonJoeur1);
			} else if (tabCouleurJetonsHaut[i] == 2) {
				g.setColor(couleurJetonJoeur2);
			}

			// Dessiner les cercle
			g.fillOval((int) tabRectHaut[i].getX() + XCercle, (int) tabRectHaut[i].getY() + YCercle, largeurCercle,
					hauteurCercle);
		}

		// Dessiner les rectangle de la grille de jeu
		for (int i = 0; i < tabRect.length; i++) {
			for (int j = 0; j < tabRect[i].length; j++) {
				g.setColor(couleurGrille);

				g.fillRect((int) tabRect[i][j].getX(), (int) tabRect[i][j].getY(), (int) tabRect[i][j].getWidth(),
						(int) tabRect[i][j].getHeight());

				g.drawRect((int) tabRect[i][j].getX(), (int) tabRect[i][j].getY(), (int) tabRect[i][j].getWidth(),
						(int) tabRect[i][j].getHeight());

				// Couleur par defaut des cercle
				g.setColor(Color.WHITE);

				// Vérification de la couleur des cercle
				if (tabCouleurJetonsGrille[i][j] == 0) {
					g.setColor(Color.WHITE);
				} else if (tabCouleurJetonsGrille[i][j] == 1) {
					g.setColor(couleurJetonJoeur1);
				} else if (tabCouleurJetonsGrille[i][j] == 2) {
					g.setColor(couleurJetonJoeur2);
				} else if (tabCouleurJetonsGrille[i][j] == 3) {
					g.setColor(couleurVictoire);
				}

				// Dessiner les cercle
				g.fillOval((int) tabRect[i][j].getX() + XCercle, (int) tabRect[i][j].getY() + YCercle, largeurCercle,
						hauteurCercle);
			}
		}
	}

	/**
	 * <b>Applique la couleur voulue sur la grille jetons du haut.</b>
	 * <p>
	 * Commence par vidé la grille du haut du haut. Ensuite modifie la valeur de
	 * l'index du tableau ce qui a pour effet de modifier la couleur selon la
	 * valeur rentrer en paramètre
	 * 
	 * @param couleurJoueur
	 * @param intIndex
	 * @return intIndex
	 */
	public int setCouleurGrilleHaut(int couleurJoueur, int intIndex) {

		for (int i = 0; i < tabCouleurJetonsHaut.length; i++) {
			tabCouleurJetonsHaut[i] = 0;
		}

		tabCouleurJetonsHaut[intIndex] = couleurJoueur;
		this.intIndex = intIndex;
		repaint();
		return intIndex;
	}

	public void setCouleurGrille(int couleur, int colonne, int ligne) {
		tabCouleurJetonsGrille[colonne][ligne] = couleur;

		repaint();
	}

	/**
	 * <b> Vérifie victoire.</b>
	 * <p>
	 * Cette méthode parcourt la grille du bas vers le haut, ensuite de gauche à
	 * droite pour vérifier si un joueur a gagné la partie
	 * 
	 * @param grille
	 *            tableau de int a deux dimensions
	 * @return joueur ou intVide
	 */
	public synchronized int verifierVictoire(int[][] grille) {

		final int intHauteur = grille.length;
		final int intLargeur = grille[0].length;
		final int intVide = 0;
		int intLongeurAnimation = 10;
		long lngDelai2 = 20;
		
		

		for (int ligne = 0; ligne < intHauteur; ligne++) {
			for (int colonne = 0; colonne < intLargeur; colonne++) {

				int joueur = grille[ligne][colonne];
				if (joueur == intVide)
					continue; // evite de verifier les cases 0 vide

				// Verifie haut et bas
				if (colonne + 3 < intLargeur && joueur == grille[ligne][colonne + 1]
						&& joueur == grille[ligne][colonne + 2] && joueur == grille[ligne][colonne + 3]) {
					this.requestFocus(false);
					for (int i = 0; i < intLongeurAnimation; i++) {
						PanneauNord.thChronometre.pause();
						grille[ligne][colonne] = 3;
						grille[ligne][colonne + 1] = 3;
						grille[ligne][colonne + 2] = 3;
						grille[ligne][colonne + 3] = 3;

						repaint();
						try {
							Thread.sleep(lngDelai2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					for (int i = 0; i < intLongeurAnimation; i++) {
						grille[ligne][colonne] = joueur;
						grille[ligne][colonne + 1] = joueur;
						grille[ligne][colonne + 2] = joueur;
						grille[ligne][colonne + 3] = joueur;

						repaint();
						try {
							Thread.sleep(lngDelai2);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					return joueur;
				}

				if (ligne + 3 < intHauteur) {
					// Verifier gauche et droite
					if (joueur == grille[ligne + 1][colonne] && joueur == grille[ligne + 2][colonne]
							&& joueur == grille[ligne + 3][colonne]) {
						this.requestFocus(false);

						for (int i = 0; i < intLongeurAnimation; i++) {
							PanneauNord.thChronometre.pause();
							grille[ligne][colonne] = 3;
							grille[ligne + 1][colonne] = 3;
							grille[ligne + 2][colonne] = 3;
							grille[ligne + 3][colonne] = 3;

							repaint();

							try {
								Thread.sleep(lngDelai2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						for (int i = 0; i < intLongeurAnimation; i++) {
							grille[ligne][colonne] = joueur;
							grille[ligne + 1][colonne] = joueur;
							grille[ligne + 2][colonne] = joueur;
							grille[ligne + 3][colonne] = joueur;

							repaint();
							try {
								Thread.sleep(lngDelai2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						return joueur;
					}

					// Verifie la diagonal de gauche
					if (colonne + 3 < intLargeur && joueur == grille[ligne + 1][colonne + 1]
							&& joueur == grille[ligne + 2][colonne + 2] && joueur == grille[ligne + 3][colonne + 3]) {
						this.requestFocus(false);

						for (int i = 0; i < intLongeurAnimation; i++) {
							PanneauNord.thChronometre.pause();
							grille[ligne][colonne] = 3;
							grille[ligne + 1][colonne + 1] = 3;
							grille[ligne + 2][colonne + 2] = 3;
							grille[ligne + 3][colonne + 3] = 3;

							repaint();

							try {
								Thread.sleep(lngDelai2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						for (int i = 0; i < intLongeurAnimation; i++) {
							grille[ligne][colonne] = joueur;
							grille[ligne + 1][colonne + 1] = joueur;
							grille[ligne + 2][colonne + 2] = joueur;
							grille[ligne + 3][colonne + 3] = joueur;

							repaint();

							try {
								Thread.sleep(lngDelai2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						return joueur;
					}

					// Verifie la diagonal de droite
					if (colonne - 3 >= 0 && joueur == grille[ligne + 1][colonne - 1]
							&& joueur == grille[ligne + 2][colonne - 2] && joueur == grille[ligne + 3][colonne - 3]) {
						this.requestFocus(false);

						for (int i = 0; i < intLongeurAnimation; i++) {
							PanneauNord.thChronometre.pause();
							grille[ligne][colonne] = 3;
							grille[ligne + 1][colonne - 1] = 3;
							grille[ligne + 2][colonne - 2] = 3;
							grille[ligne + 3][colonne - 3] = 3;

							repaint();

							try {
								Thread.sleep(lngDelai2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						for (int i = 0; i < intLongeurAnimation; i++) {
							grille[ligne][colonne] = joueur;
							grille[ligne + 1][colonne - 1] = joueur;
							grille[ligne + 2][colonne - 2] = joueur;
							grille[ligne + 3][colonne - 3] = joueur;

							repaint();

							try {
								Thread.sleep(lngDelai2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

						return joueur;
					}
				}
			}
		}

		return intVide; // retourne 0 si aucune vitoire n'est detecter
	}

}
