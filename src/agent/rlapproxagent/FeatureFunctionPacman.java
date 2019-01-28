package agent.rlapproxagent;

import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.environnementRL.EnvironnementPacmanMDPClassic;
import pacman.environnementRL.EtatPacmanMDPClassic;
import environnement.Action;
import environnement.Etat;
/**
 * Vecteur de fonctions caracteristiques pour jeu de pacman: 4 fonctions phi_i(s,a)
 *  
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionPacman implements FeatureFunction{
	private double[] vfeatures ;
	
	private static int NBACTIONS = 5;//5 avec NONE possible pour pacman, 4 sinon 
	//--> doit etre coherent avec EnvironnementPacmanRL::getActionsPossibles


	public FeatureFunctionPacman() {

	}

	@Override
	public int getFeatureNb() {
		return 4;
	}

	@Override
	public double[] getFeatures(Etat e, Action a) {
		vfeatures = new double[4];
		StateGamePacman stategamepacman ;
		//EnvironnementPacmanMDPClassic envipacmanmdp = (EnvironnementPacmanMDPClassic) e;

		//calcule pacman resulting position a partir de Etat e
		if (e instanceof StateGamePacman){
			stategamepacman = (StateGamePacman)e;
		}
		else{
			System.out.println("erreur dans FeatureFunctionPacman::getFeatures n'est pas un StateGamePacman");
			return vfeatures;
		}
	
		StateAgentPacman pacmanstate_next= stategamepacman.movePacmanSimu(0, new ActionPacman(a.ordinal()));
		 
		//*** VOTRE CODE
		
		// Fonctions caractéristiques
		
		// 1. Biais
		//*** A définir
		vfeatures[0] = 0;
		
		// 2. Nombre de fantômes proches
		int x = pacmanstate_next.getX();
		int y = pacmanstate_next.getY();
		
		if(stategamepacman.isGhost(x-1, y)) {
			vfeatures[1] += 1;
		}
		if(stategamepacman.isGhost(x+1, y)) {
			vfeatures[1] += 1;
		}
		if(stategamepacman.isGhost(x, y-1)) {
			vfeatures[1] += 1;
		}
		if(stategamepacman.isGhost(x, y+1)) {
			vfeatures[1] += 1;
		}
		
		// 3. Pac-dot présent
		if(stategamepacman.getMaze().isFood(pacmanstate_next.getLastX(), pacmanstate_next.getY())) {
			vfeatures[2] = 1;
		}
		
		// 4. Distance entre le Pacman et le Pac Dot
		vfeatures[3] = stategamepacman.getClosestDot(pacmanstate_next);
		
		
		
		return vfeatures;
	}

	public void reset() {
		vfeatures = new double[4];
		
	}

}
