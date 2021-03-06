package agent.rlagent;

import java.util.*;

import javafx.util.Pair;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;

/**
 * Renvoi 0 pour valeurs initiales de Q
 *
 * @author laetitiamatignon
 */
public class QLearningAgent extends RLAgent {
    /**
     * format de memorisation des Q valeurs: utiliser partout setQValeur car cette methode notifie la vue
     */
    protected HashMap<Etat, HashMap<Action, Double>> qvaleurs;

    //AU CHOIX: vous pouvez utiliser une Map avec des Pair pour clés si vous préférez
    //protected HashMap<Pair<Etat,Action>,Double> qvaleurs;
    double alpha;
    double gamma;
    Environnement _env;

    /**
     * @param alpha
     * @param gamma
     * @param _env
     */
    public QLearningAgent(double alpha, double gamma,
                          Environnement _env) {
        super(alpha, gamma, _env);
        qvaleurs = new HashMap<>();
        this.alpha = alpha;
        this.gamma = gamma;
        this._env = env;
    }


    /**
     * renvoi action(s) de plus forte(s) valeur(s) dans l'etat e
     * (plusieurs actions sont renvoyees si valeurs identiques)
     * renvoi liste vide si aucunes actions possibles dans l'etat (par ex. etat absorbant)
     */
    @Override
    public List<Action> getPolitique(Etat e) {
        // retourne action de meilleures valeurs dans _e selon Q : utiliser getQValeur()
        // retourne liste vide si aucune action legale (etat terminal)
        List<Action> returnactions = new ArrayList<Action>();
        if (this.getActionsLegales(e).size() == 0) {//etat  absorbant; impossible de le verifier via environnement
            System.out.println("aucune action legale");
            return new ArrayList<Action>();
        }

        //*** VOTRE CODE
        for (Action a : this.getActionsLegales(e)){
            if(this.getQValeur(e,a) == this.getValeur(e)) returnactions.add(a);
        }
        return returnactions;


    }

    @Override
    public double getValeur(Etat e) {
        //*** VOTRE CODE
        if(this.getActionsLegales(e).isEmpty()) return 0.0;
        double max = this.getQValeur(e, this.getActionsLegales(e).get(0));
        for (Action a : this.getActionsLegales(e))
            if (this.getQValeur(e,a) > max) max = this.getQValeur(e,a);
        return max;
    }

    @Override
    public double getQValeur(Etat e, Action a) {
        //*** VOTRE CODE
        double qval = 0.0;
        if(this.qvaleurs.isEmpty()) return 0.0 ;
        else if(!this.qvaleurs.containsKey(e) || !this.qvaleurs.get(e).containsKey(a)) return 0.0 ;
        qval = this.qvaleurs.get(e).get(a);
        return qval;
    }


    @Override
    public void setQValeur(Etat e, Action a, double d) {
        //si le q(e,a) n'a pas de valeur au moment du calcul, on prend 0
        double q_val = ((1 - this.alpha) * this.getQValeur(e, a)) + this.alpha * (d + this.gamma * (this.getValeur(this._env.getEtatCourant())));

        HashMap<Action, Double> tmp = new HashMap<>();
        tmp.put(a, q_val);
        if(this.qvaleurs.containsKey(e)) this.qvaleurs.get(e).put(a,q_val);
        else this.qvaleurs.put(e, tmp);


        // mise a jour vmax et vmin pour affichage du gradient de couleur:
        //vmax est la valeur max de V pour tout s
        //vmin est la valeur min de V pour tout s
        // ...
        this.vmax = 0;
        this.vmin = 0;
        
        for(HashMap<Action, Double> map : qvaleurs.values()) {
        	for(double val : map.values()) {
        		if(val > this.vmax) {
        			this.vmax = val;
        		}
        		if(val < this.vmin) {
        			this.vmin = val;
        		}
        	}
        }

        this.notifyObs();
    }


    /**
     * mise a jour du couple etat-valeur (e,a) apres chaque interaction <etat e,action a, etatsuivant esuivant, recompense reward>
     * la mise a jour s'effectue lorsque l'agent est notifie par l'environnement apres avoir realise une action.
     *
     * @param e
     * @param a
     * @param esuivant
     * @param reward
     */
    @Override
    public void endStep(Etat e, Action a, Etat esuivant, double reward) {
        if (RLAgent.DISPRL)
            System.out.println("QL mise a jour etat " + e + " action " + a + " etat' " + esuivant + " r " + reward);

        //*** VOTRE CODE
        this._env.setEtatCourant(esuivant);
        setQValeur(e, a, reward);
    }

    @Override
    public Action getAction(Etat e) {
        this.actionChoisie = this.stratExplorationCourante.getAction(e);
        return this.actionChoisie;
    }

    @Override
    public void reset() {
        super.reset();
        qvaleurs = new HashMap<>();

        this.episodeNb = 0;
        this.notifyObs();
    }
}
