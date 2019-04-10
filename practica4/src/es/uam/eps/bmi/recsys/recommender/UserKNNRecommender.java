package es.uam.eps.bmi.recsys.recommender;

import java.util.HashMap;

import es.uam.eps.bmi.recsys.data.Ratings;
import es.uam.eps.bmi.recsys.data.RatingsImpl;
import es.uam.eps.bmi.recsys.ranking.Ranking;
import es.uam.eps.bmi.recsys.ranking.RankingElement;
import es.uam.eps.bmi.recsys.ranking.RankingImpl;
import es.uam.eps.bmi.recsys.recommender.similarity.Similarity;

public class UserKNNRecommender extends AbstractRecommender {
	
	// off-line neighborhood checking  
	HashMap<Integer,Ranking> neighborhood=
			new HashMap<Integer,Ranking> ();
	
	// Name of the similarity used
	String sim = null;

	public UserKNNRecommender (Ratings rat, Similarity s, int k) {
		
		super(rat);
		// Get the similarity
		sim=s.toString();
		
		// The 'real' ratings of this class
		Ratings ratings=new RatingsImpl();
		
		// Populate the neighborhood with each user's ranking
		for (int user1 : rat.getUsers()) {
			// Rank the other users based on their similarity to the first one
			RankingImpl ranking = new RankingImpl(k);
			for (int user2 : rat.getUsers())
				if (user1!=user2) ranking.add(user2, s.sim(user1, user2));
			// Add the ranking
			neighborhood.put(user1,ranking);
			for (int item : rat.getItems(user1))
				ratings.rate(user1, item,score(user1, item));
		}
		
		this.ratings=ratings;
	}

	@Override
	public double score(int user, int item) {
		
		double score=0;
		
		// Variable that stores the sum of all neighbors' similarities
		double c_sum=0;
		
		// Iterate through the k-NN to user
		for (RankingElement rank : neighborhood.get(user)) {
			// Get the similarity and the ID of the user
			double sim=rank.getScore();
			int user_aux=rank.getID();
			
			// c=1/sum(sim(u,v), forall v in kNN(u))
			c_sum+=sim;
			// The initial score is the sum of the products of the similarities
			// and the rating the neighbor gave to item
			score+=sim*ratings.getRating(user_aux, item);
		}
		
		// r^= c * sum(rating(v,i)*sim(u,v), forall v in kNN(u))
		return score/c_sum;
	}

	public String toString() {
		return "user-based kNN ("+ sim +")";
	}
}