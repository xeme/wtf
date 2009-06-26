package com.wtf.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;

public class DiffManager {
  public static String _old_string = null;
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
    if (doComputeDiff(new_string)) {
      if_diffrent.execute();
    } else {
      if_not_diffrent.execute();
    }
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
