package pacman.environnementRL;

import environnement.Etat;
import pacman.elements.ActionPacman;
import pacman.elements.MazePacman;
import pacman.elements.StateAgentPacman;
import pacman.elements.StateGamePacman;

import java.util.Objects;
/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire

 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	private boolean canEscape;
	private boolean isDotAccessible;
	private boolean isDeadEnd;
	private boolean onDanger;

	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){
		StateAgentPacman statePacman = _stategamepacman.getPacmanState(0);
		MazePacman maze = _stategamepacman.getMaze();
		
		/* Vérification si Pacman à un échappatoire */
		int minDistGhost = Integer.MAX_VALUE;
		for(int i = 0; i < _stategamepacman.getNumberOfGhosts(); i++) {
			StateAgentPacman state = _stategamepacman.getGhostState(i);
			
			int dist = Math.abs(state.getX() - statePacman.getX()) + Math.abs(state.getY() - statePacman.getY());
			if(dist < minDistGhost) {
				minDistGhost = dist;
			}
		}
		
		canEscape = false;
		if(minDistGhost <= 2) {
			for(int i = 0; i < 4; i++) {
				ActionPacman action = new ActionPacman(i);
				
				int[] coordonnees = new int[2];
				
				coordonnees = _stategamepacman.getNextPosition(action, statePacman);
				
				if(minDistGhost == 2) {
					coordonnees = getNextCoordinates(action, coordonnees); // On prévoit à une case supplémentaire
				}
				
				if(_stategamepacman.isLegalMove(action, statePacman) && !_stategamepacman.isGhost(coordonnees[0], coordonnees[1])) {
					canEscape = true;
				}
			}
		} else {
			canEscape = true;
		}
		
		isDotAccessible = false;
		/* Vérification de l'accessibilité d'une gomme à proximité */
		int minDistDot = _stategamepacman.getClosestDot(statePacman);
		if(minDistDot <= 2) {
			for(int i = 0; i < 4; i++) {
				ActionPacman action = new ActionPacman(i);
				
				int[] coordonnees = new int[2];
				
				coordonnees = _stategamepacman.getNextPosition(action, statePacman);
				
				if(minDistDot == 2) {
					coordonnees = getNextCoordinates(action, coordonnees); // On prévoit à une case supplémentaire
				}
				
				if(_stategamepacman.isLegalMove(action, statePacman) && (maze.isCapsule(coordonnees[0], coordonnees[1]) || maze.isFood(coordonnees[0], coordonnees[1]))) {
					isDotAccessible = true;
				}
			}
		}
		
		/* Le pacman est en danger */
		onDanger = false;
		if(minDistGhost == 1) {
			onDanger = true;
		}
		
		/* Vérification de cul-de-sac */
		int nbWalls = 0;
		isDeadEnd = false;
		for(int i = 0; i < 5; i++) {
			ActionPacman action = new ActionPacman(i);
			
			if(!_stategamepacman.isLegalMove(action, statePacman)) {
				nbWalls += 1;
			}
		}
		
		if(nbWalls == 3) {
			isDeadEnd = true;
			isDotAccessible = false;
			onDanger = true;
		}
		
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
        return Objects.hash(canEscape, isDotAccessible, isDeadEnd, onDanger);
    }
    
    public int[] getNextCoordinates(ActionPacman action, int[] coordinates) {
    	int[] res = new int[2];
    	int x = coordinates[0];
    	int y = coordinates[1];
    	
    	switch(action.getDirection()) {
    		case 0: // NORD
    			res[0] = x;
    			res[1] = y - 1;
    			break;
    		case 1: // SUD
    			res[0] = x;
    			res[1] = y + 1;
    			break;
    		case 2: // EST
    			res[0] = x + 1;
    			res[1] = y;
    			break;
    		case 3: // OUEST
    			res[0] = x - 1;
    			res[1] = y;
    			break;
    		default:
    			res = coordinates;
    	}
    	
    	return res;
    }

    public int getDimensions() {
		return 24; // 4!
	}

	public boolean isCanEscape() {
		return canEscape;
	}

	public boolean isDotAccessible() {
		return isDotAccessible;
	}

	public boolean isDeadEnd() {
		return isDeadEnd;
	}

	public boolean isOnDanger() {
		return onDanger;
	}
}
