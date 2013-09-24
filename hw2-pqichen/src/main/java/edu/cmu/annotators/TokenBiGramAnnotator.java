/*
 * @author Qichen Pan (Andrew_Id: pqichen)
 * 
 * TokenBiGramAnnotator: This Annotator is responsible for annotating BiGrams.
 * 
 * It maintains a array to store BiGrams information.
 * 
 * Previous Question and Answers need to be retrieved to calculate BiGram Number.
 * 
 * Previous Tokens need to be retrieved.
 * 
 * Pilot and Pilot_tk are designed to record array positions of BiGram and Token respectively.
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
import edu.cmu.deiis.types.NGram;
import edu.cmu.deiis.types.Question;
import edu.cmu.deiis.types.Token;

public class TokenBiGramAnnotator extends JCasAnnotator_ImplBase {
  public NGram [] Bi_NG_List;
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    
    // Initialize Token_List.
    Token [] Token_List;
    int num = 0;
    FSIndex Token_Index = aJCas.getAnnotationIndex(Token.type);
    Iterator Token_Iter = Token_Index.iterator();   
    while (Token_Iter.hasNext()) 
    {
      Token_Iter.next();
      num++;
    }
    Token_List = new Token[num];
    Token_Iter = Token_Index.iterator();   
    int pos = 0;
    while (Token_Iter.hasNext()) 
    {
      Token_List[pos] = (Token)Token_Iter.next();
      pos++;
    }
    
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
    pos = 0;
    while (Answer_Iter.hasNext()) 
    {
      Test_Element_Asw_List[pos] = (Answer)Answer_Iter.next();
      pos++;
    }
    
    // Get text content.
    String Text_Content = aJCas.getDocumentText();
   
    // Declare Bi_NG_List.
    int Bi_NG_Num = Token_List.length - Test_Element_Q_Num - Test_Element_A_Num;
    Bi_NG_List = new NGram[Bi_NG_Num];
    
    // Get Bi_NGs in question.
    int begin = 0, end = 0;
    int pilot = 0, pilot_tk = 0;
    pilot = Text_Content.substring(Test_Element_Qst.getBegin() + 1,
            Test_Element_Qst.getEnd()).split(" ").length - 1;
    FSArray tmp_fs = new FSArray(aJCas, 2);
    for(int i = 0; i < pilot; i++)
    {
      begin = Token_List[i].getBegin();
      end = Token_List[i + 1].getEnd();
      Bi_NG_List[i] = new NGram(aJCas, begin, end);
      Bi_NG_List[i].setCasProcessorId("TokenBiGramAnnotator");
      Bi_NG_List[i].setConfidence(1.0);
      tmp_fs.copyFromArray(Token_List, i, 0, 2);
      Bi_NG_List[i].setElements(tmp_fs);
      Bi_NG_List[i].setElementType("edu.cmu.deiis.types.Token");
      Bi_NG_List[i].addToIndexes();    
    }
    pilot_tk = pilot + 1;
    
    // Get Bi_NGs in answers.
    for(int j = 0; j < Test_Element_Asw_List.length; j++)
    {
      int lr = pilot;
      pilot = pilot + Text_Content.substring(Test_Element_Asw_List[j].getBegin(),
              Test_Element_Asw_List[j].getEnd() - 1).trim().split(" ").length - 1;
      for(int i = lr; i < pilot; i++)
      {
        begin = Token_List[pilot_tk++].getBegin();
        end = Token_List[pilot_tk].getEnd();
        Bi_NG_List[i] = new NGram(aJCas, begin, end);
        Bi_NG_List[i].setCasProcessorId("TokenBiGramAnnotator");
        Bi_NG_List[i].setConfidence(1.0);
        tmp_fs.copyFromArray(Token_List, pilot_tk - 1, 0, 2);
        Bi_NG_List[i].setElements(tmp_fs);
        Bi_NG_List[i].setElementType("edu.cmu.deiis.types.Token");
        Bi_NG_List[i].addToIndexes();    
      }
      pilot_tk = pilot_tk + 1;
    }
  }
}
