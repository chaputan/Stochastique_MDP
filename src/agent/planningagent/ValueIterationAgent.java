package agent.planningagent;

import java.util.*;
import java.util.stream.Collectors;

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
		
		Map<Etat, Double> VAncien = (Map<Etat, Double>) V.clone();

        List<Etat> etats = mdp.getEtatsAccessibles();

        for (Etat s: etats) {
            double Vmax = -Double.MAX_VALUE;
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
                if(Vmax < currentSum)
                    Vmax = currentSum;
            }
            if(mdp.estAbsorbant(s))
                V.put(s, 0.0);
            else
                V.put(s, Vmax);
        }
        

		// mise a jour vmax et vmin pour affichage du gradient de couleur:
		//vmax est la valeur max de V pour tout s
		//vmin est la valeur min de V pour tout s 
		// ...
        this.vmax = V.values().stream().max(Comparator.comparingDouble(v -> v)).get();
        this.vmin = V.values().stream().min(Comparator.comparingDouble(v -> v)).get();
        
        // Calcul du delta
        Set<Etat> etatsDelta = VAncien.keySet();     
        Iterator<Etat> i = etatsDelta.iterator();
        double res = 0;
        double max = 0;
        
        while(i.hasNext()) {
        	Etat s = i.next();
        	res  = VAncien.get(s) - V.get(s);
        	if(max < Math.abs(res)) {
        		max = Math.abs(res);
        	}
        }
        
      //delta est utilise pour detecter la convergence de l'algorithme
      //lorsque l'on planifie jusqu'a convergence, on arrete les iterations lorsque
      //delta < epsilon
		this.delta = max;
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
		if(meilleuresActions.size() == 0)
		    return Action2D.NONE;
        else
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
        Map<Action, Double> values = new HashMap<>();
        List<Action> actions = mdp.getActionsPossibles(_e);
        for (Action a: actions) {
            double currentSum =0;
            Map<Etat, Double> transitions = new HashMap<>();
            try {
                transitions = mdp.getEtatTransitionProba(_e, a);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Set<Etat> sPrimeList = transitions.keySet();
            for (Etat sPrime: sPrimeList){
                currentSum += transitions.get(sPrime) * (mdp.getRecompense(_e, a, sPrime) + (this.getGamma() * this.getValeur(sPrime)));
            }
            values.put(a, currentSum);
        }
        // On récupère les maximums
        if(values.size() > 1){
            double max = values.entrySet().stream().max((e1,e2)->e1.getValue() > e2.getValue() ? 1 : -1).get().getValue();
            return values.entrySet().stream().filter(e->e.getValue() == max).map(Map.Entry::getKey).collect(Collectors.toList());
        }
        else if(values.size() ==1){
            return values.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
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
