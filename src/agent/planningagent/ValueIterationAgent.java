package agent.planningagent;

import java.util.*;

import environnement.*;
import sun.font.EAttribute;
import util.HashMapUtil;


/**
 * Cet agent met a jour sa fonction de valeur avec value iteration
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 *
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;

	/**
	 *
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		V = new HashMap<>();
		List<Etat> etats = mdp.getEtatsAccessibles();
		etats.forEach(s -> V.put(s, (double) 0));
	}




	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);

	}

	/**
	 *
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
		//delta < epsilon
		this.delta = this.getVmax();

        List<Etat> etats = mdp.getEtatsAccessibles();

        for (Etat s: etats) {
            double Vmax = 0;
            List<Action> actions = mdp.getActionsPossibles(s);
            for (Action a: actions) {
                double currentSum =0;
                Map<Etat, Double> transitions = new HashMap<>();
                try {
                     transitions = mdp.getEtatTransitionProba(s, a);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Set<Etat> sPrimeList = transitions.keySet();
                for (Etat sPrime: sPrimeList){
                    currentSum += transitions.get(sPrime) * (mdp.getRecompense(s, a, sPrime) + (this.getGamma() * this.getValeur(sPrime)));
                }
                if(Vmax< currentSum)
                    Vmax = currentSum;
            }
            V.put(s, Vmax);
        }
        

		// mise a jour vmax et vmin pour affichage du gradient de couleur:
		//vmax est la valeur max de V pour tout s
		//vmin est la valeur min de V pour tout s 
		// ...
        this.vmax = V.values().stream().max((v1,v2) -> Double.compare(v1, v2)).get();
        this.vmin = V.values().stream().min((v1,v2) -> Double.compare(v1, v2)).get();
        
		this.delta = Math.abs(this.delta - this.getVmax());
		//******************* laisser notification a la fin de la methode
		this.notifyObs();
		
	}


	/**
	 * renvoi l'action executee par l'agent dans l'etat e
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
	    List<Action> meilleuresActions = getPolitique(e);
		return meilleuresActions.get(new Random().nextInt(meilleuresActions.size()));
	}
	@Override
	public double getValeur(Etat _e) {
		return V.get(_e);
	}
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans etat
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		//*** VOTRE CODE

		// retourne action de meilleure valeur dans _e selon V,
		// retourne liste vide si aucune action legale (etat absorbant)
		List<Action> returnactions = new ArrayList<Action>();

		return returnactions;

	}

	@Override
	public void reset() {
		super.reset();


		this.V.clear();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
		this.notifyObs();
	}

	public HashMap<Etat,Double> getV() {
		return V;
	}

	public double getGamma() {
		return gamma;
	}

	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}






}
