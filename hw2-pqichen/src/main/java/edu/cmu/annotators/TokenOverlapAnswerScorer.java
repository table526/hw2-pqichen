/*
 * @author Qichen Pan (Andrew_Id: pqichen)
 * 
 * TokenOverlapAnswerScorer: This Annotator is responsible for annotating AnswerScores in TokenOverlap Algorithm.
 * 
 * It maintains a array to store TokenOverlap AnswerScore information.
 * 
 * Previous Question and Answers need to be retrieved.
 * 
 * This algorithm assign a score the same value as the hit ratio of AnswerTokens and QuestionTokens. 
 * 
 */
package edu.cmu.annotators;

import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.AnswerScore;
import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.Question;
import edu.cmu.deiis.types.Token;

public class TokenOverlapAnswerScorer extends JCasAnnotator_ImplBase {
  public AnswerScore [] Token_AS_List;
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    
    // Initialize question and answers.
    Question Test_Element_Qst;
    FSIndex Question_Index = aJCas.getAnnotationIndex(Question.type);
    Iterator Question_Iter = Question_Index.iterator();   
    Test_Element_Qst = (Question)Question_Iter.next();
    int Test_Element_Q_Num = 1;
    
    Answer [] Test_Element_Asw_List;
    int Test_Element_A_Num = 0;
    FSIndex Answer_Index = aJCas.getAnnotationIndex(Answer.type);
    Iterator Answer_Iter = Answer_Index.iterator();   
    while (Answer_Iter.hasNext()) 
    {
      Answer_Iter.next();
      Test_Element_A_Num++;
    }
    Test_Element_Asw_List = new Answer[Test_Element_A_Num];
    Answer_Iter = Answer_Index.iterator();   
    int pos = 0;
    while (Answer_Iter.hasNext()) 
    {
      Test_Element_Asw_List[pos] = (Answer)Answer_Iter.next();
      pos++;
    }
    
    // Get text content.
    String Text_Content = aJCas.getDocumentText();
    
    // Declare Token_AS_List.
    int AS_Num = Test_Element_Asw_List.length;
    Token_AS_List = new AnswerScore[AS_Num];
    
    // Initialize AS_List using Asw_List.
    for(int i = 0; i < AS_Num; i++)
    {
      Token_AS_List[i] = new AnswerScore(aJCas, Test_Element_Asw_List[i].getBegin(), Test_Element_Asw_List[i].getEnd());
      Token_AS_List[i].setCasProcessorId("TokenOverlapAnswerScorer");
      Token_AS_List[i].setConfidence(0.0);
      Token_AS_List[i].setAnswer(Test_Element_Asw_List[i]);
      Token_AS_List[i].addToIndexes();    
    }
    
    // Calculate Scores for each AS.
    int begin = 0, end = 0;
    int pilot = 0;
    
    // Get Question Tokens.
    end = Test_Element_Qst.getEnd();
    String Q_Token[] = Text_Content.substring(begin + 1, end - 1).trim().toLowerCase().split(" ");
    
    // Get Answer Tokens and calculate overlap.
    int match = 0;
    for(int i = 0; i < Token_AS_List.length; i++)
    {
      match = 0;
      begin = Text_Content.indexOf('A', end) + 3;
      end = Text_Content.indexOf('\n', begin) - 2;
      String A_Token[] = Text_Content.substring(begin, end).trim().toLowerCase().split(" ");
      for(int j = 0; j < Q_Token.length; j++)
      {
        for(int k = 0; k < A_Token.length; k++)
        {
          if(Q_Token[j].equals(A_Token[k]))
            match++;
        }
      }
      Token_AS_List[i].setScore((double)(match) / (double)(A_Token.length));
    }
  }

}
