package com.library.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.Span;

public class NLP {

	public static void main(String[] args) {
		InputStream modelFile = null;
		try {
			String fileName = "C://Users/asdha/workspace/DBProject/Library/LibraryManagement/src/main/java/com/library/main/en-ner-person.bin";
			
			modelFile = new FileInputStream(new File(fileName));
			TokenNameFinderModel finderModel = new TokenNameFinderModel(modelFile);
			
			NameFinderME finderME = new NameFinderME(finderModel);
			
			String []sentence = new String[]{
					"charles","dickens"
				    
				    
				    };
			
			Span nameSpan[] = finderME.find(sentence);
			
			int start=0, end=0;
			for(Span s:nameSpan){
				
				System.out.println(s.toString());
				start = s.getStart();
				end = s.getEnd();
				int a = s.getEnd() - s.getStart();
				for(int i=0;i<a;i++){
					System.out.println(sentence[s.getStart()+i]);
				}
				//System.out.println(sentence[s.getStart()] + " " + sentence[s.getEnd()-1]);
			}
			
			StringBuffer restQuery = new StringBuffer();
			for(int i=0;i<sentence.length;i++){
				
				if(i<start || i>=end){
					restQuery.append(sentence[i] + " ");
				}
				
			}
			
			
				
			System.out.println(restQuery.toString());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			  if (modelFile != null) {
				    try {
				    	modelFile.close();
				    }
				    catch (IOException e) {
				    }
				  }
				}
					
	}
}
