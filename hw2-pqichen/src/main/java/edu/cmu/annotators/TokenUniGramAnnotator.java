/*
 * @author Qichen Pan (Andrew_Id: pqichen)
 * 
 * TokenUniGramAnnotator: This Annotator is responsible for annotating UniGrams.
 * 
 * It maintains a array to store UniGrams information.
 * 
 * Previous Tokens need to be retrieved.
 * 
 * Actually, UniGrams are another form of Tokens. So I just transfer them from Tokens.
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
import edu.cmu.deiis.types.Token;

public class TokenUniGramAnnotator extends JCasAnnotator_ImplBase {

  public NGram [] Uni_NG_List;
  @Override
  public void process(JCas aJCas) throws AnalysisEngineProcessException {
    // TODO Auto-generated method stub

    // Initialize Token_List.
    Token [] Token_Anno_Token_List;
    int num = 0;
    FSIndex Token_Index = aJCas.getAnnotationIndex(Token.type);
    Iterator Token_Iter = Token_Index.iterator();   
    while (Token_Iter.hasNext()) 
    {
      Token_Iter.next();
      num++;
    }
    Token_Anno_Token_List = new Token[num];
    Token_Iter = Token_Index.iterator();   
    int pos = 0;
    while (Token_Iter.hasNext()) 
    {
      Token_Anno_Token_List[pos] = (Token)Token_Iter.next();
      pos++;
    }
    
     // Declare Uni_NG_List.
    int Uni_NG_Num = Token_Anno_Token_List.length;
    Uni_NG_List = new NGram[Uni_NG_Num];
    
    // Initialize Uni_NG using Token_List.
    FSArray tmp_fs = new FSArray(aJCas, 1);
    for(int i = 0; i < Uni_NG_Num; i++)
    {
      Uni_NG_List[i] = new NGram(aJCas, Token_Anno_Token_List[i].getBegin(), Token_Anno_Token_List[i].getEnd());
      Uni_NG_List[i].setCasProcessorId("TokenUniGramAnnotator");
      Uni_NG_List[i].setConfidence(1.0);
      tmp_fs.copyFromArray(Token_Anno_Token_List, i, 0, 1);
      Uni_NG_List[i].setElements(tmp_fs);
      Uni_NG_List[i].setElementType("edu.cmu.deiis.types.Token");
      Uni_NG_List[i].addToIndexes();    
    }
  }

}
