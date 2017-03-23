import java.awt.Color;

public class Connect4 {

	FenetrePrincipal fenetre;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FenetrePrincipal fenetre = new FenetrePrincipal();
		fenetre.pNord.lblTemps.setText("Chargement");
		fenetre.pAction.requestFocus();
		try {
			animationDepart();
			fenetre.btnNouvPartie.setEnabled(true);
			fenetre.btnQuitter.setEnabled(true);
			fenetre.requestFocus();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fenetre.pNord.lblTemps.setText("00 : 00 : 00");

	}

	public static void animationDepart() throws InterruptedException {

		long lngDelai = 10;

		for (int colonne = 0; colonne < FenetrePrincipal.grille.tabCouleurJetonsGrille.length; colonne++) {
			for (int ligne = 0; ligne < FenetrePrincipal.grille.tabCouleurJetonsGrille[colonne].length; ligne++) {

				FenetrePrincipal.grille.tabCouleurJetonsGrille[colonne][ligne] = 1;

				FenetrePrincipal.grille.repaint();

				Thread.sleep(lngDelai);

			}
		}

		for (int colonne = FenetrePrincipal.grille.tabCouleurJetonsGrille.length - 1; colonne >= 0; colonne--) {
			for (int ligne = FenetrePrincipal.grille.tabCouleurJetonsGrille[colonne].length - 1; ligne >= 0; ligne--) {

				FenetrePrincipal.grille.tabCouleurJetonsGrille[colonne][ligne] = 2;

				FenetrePrincipal.grille.repaint();

				Thread.sleep(lngDelai);

			}
		}

		for (int colonne = FenetrePrincipal.grille.tabCouleurJetonsGrille.length - 1; colonne >= 0; colonne--) {
			for (int ligne = FenetrePrincipal.grille.tabCouleurJetonsGrille[colonne].length - 1; ligne >= 0; ligne--) {

				FenetrePrincipal.grille.tabCouleurJetonsGrille[colonne][ligne] = 2;
				FenetrePrincipal.grille.repaint();
			}
		}

		FenetrePrincipal.grille.repaint();

		for (int colonne = 0; colonne < FenetrePrincipal.grille.tabCouleurJetonsGrille.length; colonne++) {
			for (int ligne = 0; ligne < FenetrePrincipal.grille.tabCouleurJetonsGrille[colonne].length; ligne++) {

				FenetrePrincipal.grille.tabCouleurJetonsGrille[colonne][ligne] = 1;

				FenetrePrincipal.grille.repaint();

				Thread.sleep(lngDelai);

			}
		}

		for (int colonne = FenetrePrincipal.grille.tabCouleurJetonsGrille.length - 1; colonne >= 0; colonne--) {
			for (int ligne = FenetrePrincipal.grille.tabCouleurJetonsGrille[colonne].length - 1; ligne >= 0; ligne--) {

				FenetrePrincipal.grille.tabCouleurJetonsGrille[colonne][ligne] = 2;

				FenetrePrincipal.grille.repaint();

				Thread.sleep(lngDelai);

			}
		}

		FenetrePrincipal.grille.repaint();

		for (int i = 0; i < FenetrePrincipal.grille.tabCouleurJetonsGrille.length; i++) {
			for (int j = 0; j < FenetrePrincipal.grille.tabCouleurJetonsGrille[i].length; j++) {
				FenetrePrincipal.grille.tabCouleurJetonsGrille[i][j] = 0;

			}
		}

	}

}
