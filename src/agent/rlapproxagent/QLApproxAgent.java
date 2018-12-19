package agent.rlapproxagent;


import java.util.ArrayList;
import java.util.List;

import agent.rlagent.QLearningAgent;
import agent.rlagent.RLAgent;
import environnement.Action;
import environnement.Environnement;
import environnement.Etat;
/**
 * Agent qui apprend avec QLearning en utilisant approximation de la Q-valeur : 
 * approximation lineaire de fonctions caracteristiques 
 * 
 * @author laetitiamatignon
 *
 */
public class QLApproxAgent extends QLearningAgent{

	private FeatureFunction featureFunction;
	private double[] listePoids;

	public QLApproxAgent(double alpha, double gamma, Environnement _env,FeatureFunction _featurefunction) {
		super(alpha, gamma, _env);
		//*** VOTRE CODE
		this.featureFunction = _featurefunction;
        listePoids = new double[_featurefunction.getFeatureNb()];
        //initialisation du tableau de poids
        for(int i=0; i<_featurefunction.getFeatureNb(); i++){
            listePoids[i] = 0;
        }
	}

	
	@Override
	public double getQValeur(Etat e, Action a) {
		//*** VOTRE CODE
        /*
        double qVal = 0;
		//On récupère les features en dehors de la boucle
		double[] features = featureFunction.getFeatures(e, a);
		for(int i=0;i<nFeatures;i++){
			qVal+= poids[i]*features[i];
		}

		return qVal;
        */
        double qVal = 0;
        int i = 0;
        //vecteur de features
        double[] functionFeatures = featureFunction.getFeatures(e, a);
        for (double poids: listePoids) {
            qVal += listePoids[i] * functionFeatures[i];
            i++;
        }

        return 0.0;

	}
	
	
	
	
	@Override
	public void endStep(Etat e, Action a, Etat esuivant, double reward) {
		if (RLAgent.DISPRL){
			System.out.println("QL: mise a jour poids pour etat \n"+e+" action "+a+" etat' \n"+esuivant+ " r "+reward);
		}
       //inutile de verifier si e etat absorbant car dans runEpisode et threadepisode 
		//arrete episode lq etat courant absorbant
        //recuperation de la qval max de l'etat suivant

		
		//*** VOTRE CODE
        this.env.setEtatCourant(esuivant);
        double[] functionFeatures = featureFunction.getFeatures(e, a);
        int i = 0;
        //mise à jour de tous les poids
        for (double poids : listePoids) {
            this.listePoids[i] += this.getAlpha() *(reward + this.getGamma()*this.getValeur(esuivant) - this.getQValeur(e, a))*functionFeatures[i];
            i++;
        }
		
	}
	
	@Override
	public void reset() {
		super.reset();
		this.qvaleurs.clear();
	
		//*** VOTRE CODE
		
		this.episodeNb =0;
		this.notifyObs();
	}
	
	
}
