/*
 * @author Qichen Pan (Andrew_Id: pqichen)
 * 
 * TokenAnnotator: This Annotator is responsible for annotating Tokens.
 * 
 * It maintains a array to store Tokens information.
 * 
 * Previous Question and Answers Annotations produced by TestElementAnnotator need to be retrieved.
 * 
 */
package edu.cmu.annotators;

import java.util.Iterator;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Token;
import edu.cmu.deiis.types.Question;
import edu.cmu.deiis.types.Answer;

public class TokenAnnotator extends JCasAnnotator_ImplBase {

  public Token [] Token_List;
  
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
    
    // Declare Token_List.
    int Token_Num = Text_Content.split(" ").length - Test_Element_Q_Num - Test_Element_A_Num;
    Token_List = new Token[Token_Num];
    
    // Get Tokens in question.
    int begin = 0, end = 0;
    int pilot = 0;
    end = Test_Element_Qst.getEnd();
    String tmp_q[] = Text_Content.substring(begin + 1, end).trim().split(" ");
    begin = Text_Content.indexOf(tmp_q[0]);
    end = begin + tmp_q[0].length();
    Token_List[0] = new Token(aJCas, begin, end);
    Token_List[0].setCasProcessorId("TokenAnnotator");
    Token_List[0].setConfidence(1.0);
    Token_List[0].addToIndexes();
    for(int i = 1; i < tmp_q.length; i++)
    {
      begin = Text_Content.indexOf(tmp_q[i], Token_List[i - 1].getEnd());
      end = begin + tmp_q[i].length();
      Token_List[i] = new Token(aJCas, begin, end);
      Token_List[i].setCasProcessorId("TokenAnnotator");
      Token_List[i].setConfidence(1.0);
      Token_List[i].addToIndexes();
    }
    Token_List[tmp_q.length - 1].setEnd(Token_List[tmp_q.length - 1]. getEnd() - 1);
    pilot = pilot + tmp_q.length;
    
    // Get Tokens in answers.
    for(int i = 0; i < Test_Element_A_Num; i++)
    {
      begin = Text_Content.indexOf('A', end) + 3;
      end = Text_Content.indexOf('\n', begin) - 2;
      String tmp_a[] = Text_Content.substring(begin, end).trim().split(" ");
      for(int j = 0; j < tmp_a.length; j++)
      {
        begin = Text_Content.indexOf(tmp_a[j], Token_List[j + pilot - 1].getEnd());
        end = begin + tmp_a[j].length();
        Token_List[j + pilot] = new Token(aJCas, begin, end);
        Token_List[j + pilot].setCasProcessorId("TokenAnnotator");
        Token_List[j + pilot].setConfidence(1.0);
        Token_List[j + pilot].addToIndexes();
      }
      pilot = pilot + tmp_a.length;
    }
  }

}
