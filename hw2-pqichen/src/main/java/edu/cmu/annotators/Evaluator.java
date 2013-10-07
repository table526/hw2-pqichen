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
import edu.cmu.deiis.types.NGram;

public class Evaluator extends JCasAnnotator_ImplBase {  
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    
    // Initialize AS.
    AnswerScore [] AS;
    int num = 0;
    FSIndex AS_Index = aJCas.getAnnotationIndex(AnswerScore.type);
    Iterator AS_Iter = AS_Index.iterator();   
    while (AS_Iter.hasNext()) 
    {
      AS_Iter.next();
      num++;
    }
    AS = new AnswerScore[num];
    AS_Iter = AS_Index.iterator(); 
    int pos = 0;
    while (AS_Iter.hasNext()) 
    {
      AS[pos] = (AnswerScore)AS_Iter.next();
      pos++;
    }
    int Gold_AS_num = 0, Token_AS_num = 0, NGram_AS_num = 0;
    for(int i = 0; i < AS.length; i++)
    {
      if(AS[i].getCasProcessorId().equals("GoldAnswerScorer"))
      {
        Gold_AS_num++;
      }else if(AS[i].getCasProcessorId().equals("TokenOverlapAnswerScorer")) 
      {
        Token_AS_num++;
      }else if(AS[i].getCasProcessorId().equals("NGramOverlapAnswerScorer"))
      {
        NGram_AS_num++;
      }
    }
   
    // Initialize each NGram.
    AnswerScore [] Gold_AS = new AnswerScore[Gold_AS_num];
    AnswerScore [] Token_AS = new AnswerScore[Token_AS_num];
    AnswerScore [] NGram_AS = new AnswerScore[NGram_AS_num];
    Gold_AS_num = 0; Token_AS_num = 0; NGram_AS_num = 0;
    for(int i = 0; i < AS.length; i++)
    {
      if(AS[i].getCasProcessorId().equals("GoldAnswerScorer"))
      {
        Gold_AS[Gold_AS_num] = AS[i];
        Gold_AS_num++;
      }else if(AS[i].getCasProcessorId().equals("TokenOverlapAnswerScorer")) 
      {
        Token_AS[Token_AS_num] = AS[i];
        Token_AS_num++;
      }else if(AS[i].getCasProcessorId().equals("NGramOverlapAnswerScorer"))
      {
        NGram_AS[NGram_AS_num] = AS[i];
        NGram_AS_num++;
      }
    }
    
    // Get correct Answer Num.
    int Correct_Num = 0;
    int bg, ed;
    for(int i = 0; i < Gold_AS.length; i++)
    {
      if(Gold_AS[i].getAnswer().getIsCorrect() == true)
      {
        Correct_Num++;
      }
    }
    
    // Calculate Precision of TokenOverlap Method.
    double tmp = 0.0;
    boolean judge = true;
    int pred_true = 0;
    boolean [] sign_tk = new boolean[Token_AS.length];
    int sign = -1;
    for(int k = 0; k < sign_tk.length; k++)
    {
      sign_tk[k] = false;
    }
    for(int i = 0; i < Correct_Num; i++)
    {
      judge = false;
      sign = -1;
      for(int j = 0; j < Token_AS.length; j++)
      {
        if(tmp <= Token_AS[j].getScore() && sign_tk[j] == false)
        {
          tmp = Token_AS[j].getScore();
          judge = Token_AS[j].getAnswer().getIsCorrect();
          sign = j;
        }
      }
      sign_tk[sign] = true;
      if(judge == true)
        pred_true++;
      tmp = 0.0;
    }
    double precision_TokenOverlap = (double)pred_true / (double)Correct_Num;
    
    // Calculate Precision of NGramOverlap Method.
    tmp = 0.0;
    judge = true;
    pred_true = 0;
    boolean [] sign_ng = new boolean[NGram_AS.length];
    for(int k = 0; k < sign_ng.length; k++)
    {
      sign_ng[k] = false;
    }
    for(int i = 0; i < Correct_Num; i++)
    {
      sign = -1;
      judge = false;
      for(int j = 0; j < NGram_AS.length; j++)
      {
        if(tmp <= NGram_AS[j].getScore() && sign_ng[j] == false)
        {
          tmp = NGram_AS[j].getScore();
          judge = NGram_AS[j].getAnswer().getIsCorrect();
          sign = j;
        }
      }
      sign_ng[sign] = true;
      if(judge == true)
        pred_true++;
      tmp = 0.0;
    }
    double precision_NGramOverlap = (double)pred_true / (double)Correct_Num;
    
 // print out results.
    double precision_Gold = 1.00;
    System.out.println("Gold_AnswerScore_Precision:\t" + precision_Gold + "\n" 
            + "TokenOverlap_AnswerScore_Precision:\t" + precision_TokenOverlap + "\n"
            + "NGramOverlap_AnswerScore_Precision:\t" + precision_NGramOverlap + "\n");
  }
  

}


