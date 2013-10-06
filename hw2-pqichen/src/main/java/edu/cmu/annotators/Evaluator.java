/*
 * @author Qichen Pan (Andrew_Id: pqichen)
 * 
 * Evaluator: This class is responsible for evaluating the performance of three algorithms.
 * 
 * It first retrieves Gold_AS info to get the right answer number say 'n'.
 * 
 * It then sorts the two other AnswerScore_List and picks the top n ones.
 * 
 * It calculate the precision of three different algorithms and output them on the screen.
 * 
 * The precisions are stored in precision_TokenOverlap, precision_NGramOverlap, precision_Gold.
 * 
 */
package edu.cmu.annotators;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;

public class Evaluator extends JCasAnnotator_ImplBase {  
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    
    // Initialize Gold_AS.
    AnswerScore [] Gold_AS;
    int num = 0;
    FSIndex Gold_AS_Index = aJCas.getAnnotationIndex(AnswerScore.type);
    Iterator Gold_AS_Iter = Gold_AS_Index.iterator();   
    while (Gold_AS_Iter.hasNext()) 
    {
      Gold_AS_Iter.next();
      num++;
    }
    Gold_AS = new AnswerScore[num];
    Gold_AS_Iter = Gold_AS_Index.iterator(); 
    int pos = 0;
    while (Gold_AS_Iter.hasNext()) 
    {
      Gold_AS[pos] = (AnswerScore)Gold_AS_Iter.next();
      pos++;
    }
    
    // Initialize Token_AS.
    AnswerScore [] Token_AS;
    Token_AS = new AnswerScore[num];
    FSIndex Token_AS_Index = aJCas.getAnnotationIndex(AnswerScore.type);
    Iterator Token_AS_Iter = Token_AS_Index.iterator();   
    pos = 0;
    while (Token_AS_Iter.hasNext()) 
    {
      Token_AS[pos] = (AnswerScore)Token_AS_Iter.next();
      pos++;
    }
    
    // Initialize NGram_AS.
    AnswerScore [] NGram_AS;
    NGram_AS = new AnswerScore[num];
    FSIndex NGram_AS_Index = aJCas.getAnnotationIndex(AnswerScore.type);
    Iterator NGram_AS_Iter = NGram_AS_Index.iterator();   
    pos = 0;
    while (NGram_AS_Iter.hasNext()) 
    {
      NGram_AS[pos] = (AnswerScore)NGram_AS_Iter.next();
      pos++;
    }

    // Get correct Answer Num.
    int Correct_Num = 0;
    for(int i = 0; i < Gold_AS.length; i++)
    {
      if(Gold_AS[i].getAnswer().getIsCorrect() == true)
        Correct_Num++;
    }
    
    // Calculate Precision of TokenOverlap Method.
    double tmp = 0.0;
    double Threshold = 2.0;
    boolean judge = true;
    int pred_true = 0;
    for(int i = 0; i < Correct_Num; i++)
    {
      for(int j = 0; j < Token_AS.length; j++)
      {
        if(tmp <= Token_AS[i].getScore() && Token_AS[i].getScore() < Threshold)
          tmp = Token_AS[i].getScore();
          judge = Token_AS[i].getAnswer().getIsCorrect();
      }
      Threshold = tmp;
      if(judge == true)
        pred_true++;
      tmp = 0.0;
    }
    double precision_TokenOverlap = (double)pred_true / (double)Correct_Num;
    
    // Calculate Precision of NGramOverlap Method.
    tmp = 0.0;
    Threshold = 2.0;
    judge = true;
    pred_true = 0;
    for(int i = 0; i < Correct_Num; i++)
    {
      for(int j = 0; j < NGram_AS.length; j++)
      {
        if(tmp <= NGram_AS[i].getScore() && NGram_AS[i].getScore() < Threshold)
          tmp = NGram_AS[i].getScore();
          judge = NGram_AS[i].getAnswer().getIsCorrect();
      }
      Threshold = tmp;
      if(judge == true)
        pred_true++;
      tmp = 0.0;
    }
    double precision_NGramOverlap = (double)pred_true / (double)Correct_Num;
    
    // print out results.
    File outputf = new File("/home/pan/git/hw2-pqichen/hw2-pqichen/src/main/resources/inputData/processedData/Output.txt.xmi"); 
    FileWriter fw = null;
    try {
      fw = new FileWriter("/home/pan/git/hw2-pqichen/hw2-pqichen/src/main/resources/inputData/processedData/Output.txt.xmi");
      //fw = new FileWriter(mOutputDir);
    } catch (IOException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    
    double precision_Gold = 1.00;
    try {
      fw.write("Gold_AnswerScore_Precision:\t" + precision_Gold + "\n" 
              + "TokenOverlap_AnswerScore_Precision:\t" + precision_TokenOverlap + "\n"
              + "NGramOverlap_AnswerScore_Precision:\t" + precision_NGramOverlap + "\n");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    try {
      fw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  

}


