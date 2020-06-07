package com.library.nlp;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class ApacheQuery implements NLPQuery{

    private static final Logger LOGGER = LogManager.getLogger(ApacheQuery.class);

    @Override
    public Feature getQuery(String query) {

        StringBuilder authorQuery = new StringBuilder();
        StringBuilder bookNameQuery = new StringBuilder();
        InputStream modelFile = null;
        String fileName = "en-ner-person.bin";

        try {
            modelFile = new FileInputStream(new File(fileName));
            TokenNameFinderModel finderModel = new TokenNameFinderModel(modelFile);
            NameFinderME finderME = new NameFinderME(finderModel);
            String[] sQuery = query.split(" ");
            Span nameSpan[] = finderME.find(sQuery);
            int start = 0, end = 0, diff = 0;
            for (Span s : nameSpan) {

                System.out.println(s.toString());
                start = s.getStart();
                end = s.getEnd();
                diff = s.getEnd() - s.getStart();
                for (int i = 0; i < diff; i++) {
                    authorQuery.append(sQuery[s.getStart() + i]);
                    if (i == diff - 1) {
                        continue;
                    }
                    authorQuery.append(" ");
                }
            }

            for (int i = 0; i < sQuery.length; i++) {

                if (i < start || i >= end) {
                    bookNameQuery.append(sQuery[i]);
                    if (i == sQuery.length - 1) {
                        continue;
                    }
                    bookNameQuery.append(" ");
                }
            }

        } catch (FileNotFoundException e) {
            LOGGER.error("File not found", e);
        } catch (IOException e) {
            LOGGER.error("Exception occurred", e);
        }

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
