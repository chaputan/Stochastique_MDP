package pacman.environnementRL;

import environnement.Etat;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;

import java.util.Objects;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

    private StateAgentPacman fantome;
    private boolean[][] dot;

	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){
	    this.fantome = _stategamepacman.getGhostState(0);
	    dot= new boolean[_stategamepacman.getMaze().getSizeX()][_stategamepacman.getMaze().getSizeY()];
	    for(int x=0; x<_stategamepacman.getMaze().getSizeX();x++){
	        for(int y=0;y<_stategamepacman.getMaze().getSizeY();y++)
	            dot[x][y] = _stategamepacman.getMaze().isCapsule(x, y);
        }
        int x=0;
	}
	
	@Override
	public String toString() {
		
		return "";
	}
	
	
	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}
		// on renvoie le clone
		return clone;
	}

    @Override
    public int hashCode() {
        return Objects.hash(fantome, dot);
    }
}
