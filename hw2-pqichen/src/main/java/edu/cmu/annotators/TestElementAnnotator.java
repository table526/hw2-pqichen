/*
 * @author Qichen Pan (Andrew_Id: pqichen)
 * 
 * TestElmentAnnotator: This Annotator is responsible for annotating Questions and Answers.
 * 
 * It maintains a Variable Qst to store Question information, for that each file only has one Question.
 * 
 * It maintains an array to store Answers information.
 * 
 * All operations are performed on DocumentsText.
 * 
 */
package edu.cmu.annotators;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;

import edu.cmu.deiis.types.Answer;
import edu.cmu.deiis.types.Question;

public class TestElementAnnotator extends JCasAnnotator_ImplBase {

  public Question Qst;
  public Answer Asw_List[];
  public int Q_Num = 1;
  public int A_Num;
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub
    
    // Get text content.
    String Text_Content = aJCas.getDocumentText();
    
    // Split sentences.
    String tmp[] = Text_Content.split("\n");
    
    // Create Question.
    int begin = 0, end = 0;
    end = Text_Content.indexOf('?');
    Qst = new Question(aJCas, begin + 1, end + 1);   
    Qst.setCasProcessorId("TestElementAnnotator");
    Qst.setConfidence(1.0);
    Qst.addToIndexes();
    
    // Create Answers.
    Asw_List = new Answer[tmp.length - 1];
    begin = 0;
    end = 0;
    for(int i = 0; i < Asw_List.length; i++)
    {
      begin = Text_Content.indexOf('A', end) + 3;
      end = Text_Content.indexOf('\n', begin);
      Asw_List[i] = new Answer(aJCas, begin, end);   
      if(Text_Content.charAt(begin - 1) == '1')
        Asw_List[i].setIsCorrect(true);
      else
        Asw_List[i].setIsCorrect(false);
      Asw_List[i].setCasProcessorId("TestElementAnnotator");
      Asw_List[i].setConfidence(0.0);
      Asw_List[i].addToIndexes();
    }
    
    Q_Num = 1;
    A_Num = Asw_List.length;
  }

}
