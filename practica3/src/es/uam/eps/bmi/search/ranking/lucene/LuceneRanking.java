package es.uam.eps.bmi.search.ranking.lucene;

import org.apache.lucene.search.ScoreDoc;

import es.uam.eps.bmi.search.index.Index;
import es.uam.eps.bmi.search.ranking.SearchRanking;
import es.uam.eps.bmi.search.ranking.SearchRankingIterator;

/**
 *
 * @author pablo
 */
public class LuceneRanking implements SearchRanking {
    SearchRankingIterator iterator;
    int size;
    
    public LuceneRanking (Index index, ScoreDoc ranking[]) {
        size = ranking.length;
        iterator = new LuceneRankingIterator(index, ranking);
    }
    
    // Empty result list
    public LuceneRanking () {
        size = 0;
        iterator = new LuceneRankingIterator();
    }
    
    public SearchRankingIterator iterator() {
        return iterator;
    }

    public int size() {
        return size;
    }
}
