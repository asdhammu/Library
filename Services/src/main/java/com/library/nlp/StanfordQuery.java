package com.library.nlp;

import com.library.services.BorrowerServiceImpl;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.util.Triple;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

public class StanfordQuery implements NLPQuery {

    private static final Logger LOGGER = LogManager.getLogger(StanfordQuery.class);

    AbstractSequenceClassifier<CoreLabel> classifier;

    public StanfordQuery() {
        try {
            LOGGER.debug("Classifier loading started");
            ClassLoader classLoader = BorrowerServiceImpl.class.getClassLoader();
            File file = new File(classLoader.getResource("nlp/english.all.3class.distsim.crf.ser.gz").getFile());
            this.classifier = CRFClassifier.getClassifier(file);
            LOGGER.debug("Classifier loaded successfully");
        } catch (Exception e) {
            LOGGER.error("Error while loading classifier", e);
        }
    }

    @Override
    public Feature getQuery(String query) {

        StringBuilder capitalizeString = new StringBuilder();
        String[] str = query.split(" ");
        for (int i = 0; i < str.length; i++) {
            capitalizeString.append(str[i].substring(0, 1).toUpperCase() + str[i].substring(1) + " ");
        }
        capitalizeString.deleteCharAt(capitalizeString.length() - 1);
        query = capitalizeString.toString();
        StringBuilder authorQuery = new StringBuilder();
        StringBuilder bookNameQuery = new StringBuilder();
        try {

            int startIndex = 0, endIndex = 0;
            List<Triple<String, Integer, Integer>> triples = classifier
                    .classifyToCharacterOffsets(query);
            for (Triple<String, Integer, Integer> trip : triples) {
                startIndex = trip.second();
                endIndex = trip.third();

            }

            authorQuery.append(query, startIndex, endIndex);
            bookNameQuery.append(query, 0, startIndex);
            bookNameQuery.append(query.substring(endIndex));

            LOGGER.debug("Author Query " + authorQuery + " :: Book query ::  " + bookNameQuery);

        } catch (Exception e) {
            LOGGER.error("Exception while extracting features ", e);
        }

        bookNameQuery.toString();
        Feature feature = new Feature();
        if(bookNameQuery.toString().isEmpty()){
            feature.setFeatureType(FeatureType.AUTHOR);
            feature.setQuery(authorQuery.toString());
        }else{
            feature.setFeatureType(FeatureType.BOOK_NAME);
            feature.setQuery(bookNameQuery.toString());
        }

        return feature;
    }

}
