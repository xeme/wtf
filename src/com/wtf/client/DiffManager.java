package com.wtf.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

public class DiffManager {
  private static String _old_string = null;
  private static Integer[] _old_to_new = null; //-1 if line was deleted 
  public enum DIR { UP, LEFT, UP_LEFT };                         

  public static Integer[] getOldToNew() {
    return _old_to_new;
  }

  public static void computeDiff(final String new_string, final Command if_diffrent, 
      final Command if_not_diffrent) {
    if(_old_to_new != null) {
      return;
    }
    Command after_fetching = new Command() {
      public void execute() {
        if(doComputeDiff(new_string)) {
          if_diffrent.execute();
        } else {
          if_not_diffrent.execute();
        }
      }
    };

    fetchDiff(after_fetching);
  }

  private static void fetchDiff(final Command callback) {
    if(_old_string != null) {
      callback.execute();
      return;
    }

    //simulator (async deferred command instead rpc)
    DeferredCommand.addCommand(new Command() {
      public void execute() {
        
        _old_string = "<DIV>__wtf__\n__wtf__<H2>__wtf__WTF__wtf__</H2>__wtf__\n\n\n__wtf__<BR>__wtf__</BR>__wtf__\n__wtf__<H2>__wtf__jakis__wtf__sobie__wtf__h2__wtf__</H2>__wtf__\n__wtf__<P>__wtf__Developers__wtf__often__wtf__asked__wtf__what's__wtf__the__wtf__best__wtf__way__wtf__to__wtf__go__wtf__\n__wtf__about__wtf__building__wtf__custom__wtf__widgets.__wtf__There__wtf__are__wtf__of__wtf__course__wtf__a__wtf__number__wtf__of__wtf__best\n__wtf__practices__wtf__to__wtf__consider__wtf__when__wtf__answering__wtf__this__wtf__question.__wtf__In__wtf__this__wtf__post__wtf__I'll__wtf__\n__wtf__highlight__wtf__a__wtf__few__wtf__I__wtf__like__wtf__to__wtf__start__wtf__off__wtf__with.__wtf__\n__wtf__Prefer__wtf__composition__wtf__to__wtf__inheritance.__wtf__Unnecessarily__wtf__\n__wtf__exposing__wtf__implementation__wtf__details__wtf__is__wtf__generally__wtf__a__wtf__bad__wtf__\n__wtf__idea,__wtf__and__wtf__it's__wtf__no__wtf__different__wtf__when__wtf__you__wtf__are__wtf__building__wtf__custom\n__wtf__widgets.__wtf__In__wtf__GWT__wtf__terms,__wtf__this__wtf__means__wtf__that__wtf__your__wtf__custom__wtf__widgets__wtf__\n__wtf__should__wtf__typically__wtf__extend__wtf__Composite.\n__wtf__</P>__wtf__\n__wtf__<P>__wtf__Here__wtf__the__wtf__HorizontalPanel__wtf__implementation__wtf__is__wtf__exposed__wtf__\n__wtf__bez_spacji__wtf__\n__wtf__Others__wtf__may__wtf__come__wtf__to__wtf__depend__wtf__\n__wtf__on__wtf__the__wtf__presence__wtf__of__wtf__one__wtf__or__wtf__more__wtf__inherited__wtf__methods__wtf__</P>__wtf__\n__wtf__\n__wtf__<BR>__wtf__</BR>__wtf__\n__wtf__<DIV>__wtf__he__wtf__use__wtf__of__wtf__HorizontalPanel__wtf__is__wtf__a__wtf__hidden__wtf__implementation__wtf__detail\n__wtf__We__wtf__are__wtf__free__wtf__to__wtf__change__wtf__the__wtf__implementation__wtf__without__wtf__affecting__wtf__others__wtf__public\n__wtf__<P>__wtf__Extending__wtf__Composite__wtf__also__wtf__ensures__wtf__that__wtf__you__wtf__do__wtf__not__wtf__inadvertently__wtf__\n__wtf__expose__wtf__methods__wtf__inherited__wtf__from__wtf__the__wtf__parent__wtf__classes__wtf__into__wtf__your__wtf__new__wtf__widget's__wtf__API.__wtf__Doing__wtf__so__wtf__\n__wtf__can__wtf__quickly__wtf__lead__wtf__to__wtf__other__wtf__classes__wtf__depending__wtf__on__wtf__not__wtf__only__wtf__your__wtf__class'__wtf__implementation,__wtf__but__wtf__\n__wtf__also__wtf__the__wtf__implementation__wtf__of__wtf__any__wtf__parent__wtf__classes.__wtf__Remember,__wtf__you__wtf__can__wtf__always__wtf__add__wtf__to__wtf__your__wtf__API.__wtf__\n__wtf__It's__wtf__generally__wtf__impossible__wtf__to__wtf__take__wtf__anything__wtf__away__wtf__without__wtf__introducing__wtf__a__wtf__breaking__wtf__change.__wtf__\n__wtf__</P>__wtf__\n__wtf__Be__wtf__wary__wtf__of__wtf__multiple__wtf__constructors__wtf__as__wtf__they__wtf__can__wtf__quickly__wtf__get__wtf__out__wtf__of__wtf__hand\n__wtf__</DIV>__wtf__\n__wtf__\n__wtf__<OBJECT>__wtf__\n__wtf__<PARAM>__wtf__</PARAM>__wtf__<BR>__wtf__</BR>__wtf__\n__wtf__<EMBED>__wtf__</EMBED>__wtf__\n__wtf__</OBJECT>__wtf__\n__wtf__</DIV>__wtf__";

        //after fetching
        callback.execute();
      }
    });
  }

  //computing Longest Common Subsequence
  private static boolean doComputeDiff(String new_string) {
    StatusBar.setStatus("Looking for changes...");
    Debug.log_time("start _old_string.equals(new_string) ");
    //equal?
    if(_old_string.equals(new_string)) {
      Debug.log_time("end ");
      return false;
    }
    Debug.log_time("end ");
    StatusBar.setStatus("Computing diff...");
    Debug.log_time("start diff... ");

    String[] x = _old_string.split("__wtf__");
    String[] y = new_string.split("__wtf__");

    int n = y.length;
    int m = x.length;
    _old_to_new = new Integer[m];

    Integer[] c1 = new Integer[n+1];
    Integer[] c2 = new Integer[n+1];
    DIR[][] b = new DIR[m+1][n+1];

    for(int i = 0; i <= n; i++) {
      c1[i] = 0;
    }
    c2[0] = 0;

    for(int i = 1; i <= m; i++) {
      for(int j = 1; j <= n; j++) {
        if(x[i-1].equals(y[j-1])) {
          c2[j] = c1[j - 1] + 1;
          b[i][j] = DIR.UP_LEFT;
        } else if(c1[j] >= c2[j - 1]){
          c2[j] = c1[j];
          b[i][j] = DIR.UP;
        } else {
          c2[j] = c2[j - 1];
          b[i][j] = DIR.LEFT;
        }
      }
      //swap
      Integer[] tmp = c1;
      c1 = c2;
      c2 = tmp;
    }

    //LCS Pair<position in x, position in y> 
    List<Pair<Integer, Integer>> LCS = new LinkedList<Pair<Integer, Integer>>();
    int i = m, j = n;
    while(true) {
      if(i == 0 || j == 0)
        break;
      if(b[i][j] == DIR.UP_LEFT) {
        LCS.add(0, new Pair<Integer, Integer>(i - 1, j - 1));
        i--;
        j--;
      } else if(b[i][j] == DIR.UP) {
        i--;
      } else {
        j--;
      }
    }

    //compute _old_to_new
    for(i = 0; i < m; i++) {
      _old_to_new[i] = -1;
    }
    for(Pair<Integer, Integer> k : LCS) {
      _old_to_new[k.first()] = k.second();
    }
    Debug.log_time("end ");
    Debug.log("new: " + n + " old: " + m + " LCS.size(): " + LCS.size());
    String tmp = "";
    for(i = 0; i < m; i++) {
      String tx = x[i].replace("<", "&lt;");
      tx = tx.replace(">", "&gt;");
      tmp += i + " : " + _old_to_new[i] + " " + tx + "<br>";
    }
    //Debug.log(tmp);
    return true;
  }
}
