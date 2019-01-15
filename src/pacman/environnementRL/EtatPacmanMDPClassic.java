package pacman.environnementRL;

import environnement.Etat;
import pacman.elements.ActionPacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;

import java.util.Objects;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

    //private StateAgentPacman fantome;
    //private boolean[][] dot;
	private int minDistGhost;
	private int minDistDot;
	private boolean isScarred;
	private boolean canEscape;
	private int nbDots;

	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){
	    //this.fantome = _stategamepacman.getGhostState(0);
		StateAgentPacman statePacman = _stategamepacman.getPacmanState(0);
	    /*dot= new boolean[_stategamepacman.getMaze().getSizeX()][_stategamepacman.getMaze().getSizeY()];
	    for(int x=0; x<_stategamepacman.getMaze().getSizeX();x++){
	        for(int y=0;y<_stategamepacman.getMaze().getSizeY();y++)
	            dot[x][y] = _stategamepacman.getMaze().isCapsule(x, y);
        }*/
        int x=0;
		minDistGhost = Integer.MAX_VALUE;
		for(int i = 0; i < _stategamepacman.getNumberOfGhosts(); i++) {
			StateAgentPacman state = _stategamepacman.getGhostState(i);
			
			int dist = Math.abs(state.getX() - statePacman.getX()) + Math.abs(state.getY() - statePacman.getY());
			if(dist < minDistGhost) {
				minDistGhost = dist;
			}
		}
		minDistDot = _stategamepacman.getClosestDot(statePacman);
		isScarred = statePacman.isScarred();
		/*Pacman peut s'échapper si fantome a proximité*/
		canEscape = false;
		if(minDistGhost == 1) {
			for(int i = 1; i < 5; i++) {
				ActionPacman action = new ActionPacman(i);
				
				int[] coordonnees = new int[2];
				
				coordonnees = _stategamepacman.getNextPosition(action, statePacman);
				
				if(_stategamepacman.isLegalMove(action, statePacman) && !_stategamepacman.isGhost(coordonnees[0], coordonnees[1])) {
					canEscape = true;
				}
			}
		}
		
		nbDots = _stategamepacman.getFoodEaten();
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
        return Objects.hash(minDistGhost, minDistDot, isScarred, canEscape, nbDots);
    	/*int hash = 1;
    	hash *= 2 + minDistDot;
    	hash *= 3 + minDistGhost;
    	hash *= 5 + Boolean.hashCode(isScarred);
    	hash *= 7 + Boolean.hashCode(canEscape);
    	
    	return hash;*/
    }
}
