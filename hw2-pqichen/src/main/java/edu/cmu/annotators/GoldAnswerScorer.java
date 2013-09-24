/*
 * @author Qichen Pan (Andrew_Id: pqichen)
 * 
 * GoldAnswerScorer: This Annotator is responsible for annotating AnswerScores in Gold Rule.
 * 
 * It maintains a array to store Gold AnswerScore information.
 * 
 * Previous Answers need to be retrieved.
 * 
 * This algorithm simply assign 'true' to an answer if it is true. 
 * 
 * So Gold AnswerScore is guaranteed to have a 1.00 precision.
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
import edu.cmu.deiis.types.Question;

public class GoldAnswerScorer extends JCasAnnotator_ImplBase {
  
  public AnswerScore [] Gold_AS_List;
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    
    // Initialize answers.
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
    
    // Declare Gold_AS_List.
    int AS_Num = Test_Element_Asw_List.length;
    Gold_AS_List = new AnswerScore[AS_Num];
    
    // Initialize AS_List using Asw_List.
    for(int i = 0; i < AS_Num; i++)
    {
      Gold_AS_List[i] = new AnswerScore(aJCas, Test_Element_Asw_List[i].getBegin(), Test_Element_Asw_List[i].getEnd());
      Gold_AS_List[i].setCasProcessorId("GoldAnswerScorer");
      Gold_AS_List[i].setConfidence(1.0);
      Gold_AS_List[i].setAnswer(Test_Element_Asw_List[i]);
      if(Test_Element_Asw_List[i].getIsCorrect())
        Gold_AS_List[i].setScore(1.0);
      else
        Gold_AS_List[i].setScore(0.0);
      Gold_AS_List[i].addToIndexes();    
    }
  }

}
