package agent.rlapproxagent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import environnement.Action;
import environnement.Action2D;
import environnement.Etat;
import javafx.util.Pair;
import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;
import pacman.environnementRL.EtatPacmanMDPClassic;

/**
 * Vecteur de fonctions caracteristiques phi_i(s,a): autant de fonctions caracteristiques que de paire (s,a),
 * <li> pour chaque paire (s,a), un seul phi_i qui vaut 1  (vecteur avec un seul 1 et des 0 sinon).
 * <li> pas de biais ici 
 * 
 * @author laetitiamatignon
 *
 */
public class FeatureFunctionIdentity implements FeatureFunction {
	//*** VOTRE CODE
	private double _features[];

	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		//*** VOTRE CODE
        _features = new double[_nbAction * _nbEtat];
	}
	
	@Override
	public int getFeatureNb() {
		//*** VOTRE CODE
		return _features.length;
	}

	@Override
	public double[] getFeatures(Etat e,Action a){
		//*** VOTRE CODE

        EtatPacmanMDPClassic etatPacmanMDPClassic;

        //calcule pacman resulting position a partir de Etat e
        if (e instanceof EtatPacmanMDPClassic){
            etatPacmanMDPClassic = (EtatPacmanMDPClassic)e;
        }
        else {
            System.out.println("erreur dans FeatureFunctionIdentity::getFeatures n'est pas un EtatPacmanMDPClassic");
            return _features;
        }

        int numCase = 0;

        if(etatPacmanMDPClassic.isCanEscape() &&
				!etatPacmanMDPClassic.isDeadEnd() &&
				!etatPacmanMDPClassic.isDotAccessible() &&
				!etatPacmanMDPClassic.isOnDanger()) {
        	numCase = 0;
		} else {
        	if(etatPacmanMDPClassic.isOnDanger()) {
        		numCase += 0b1;
			}
			if(etatPacmanMDPClassic.isDotAccessible()) {
				numCase += 0b10;
			}
			if(etatPacmanMDPClassic.isDeadEnd()) {
				numCase += 0b100;
			}
			if (!etatPacmanMDPClassic.isCanEscape()) {
				numCase += 0b1000;
			}
		}

		numCase += (a.ordinal() << 4) | 0b1000;

		_features[numCase] = 1;

        return _features;
	}
	

}
