package com.wtf.client;

import java.util.LinkedList;
import java.util.List;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;

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
      StatusBar.setStatus("Done");  
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
    for(int i = 0; i < m; i++) {
      _old_to_new[i] = -1;
    }

    int x_start = 0, y_start = 0, x_end = m - 1, y_end = n - 1;
    //trim off the matching items at the beginning
    while(x_start <= x_end && y_start <= y_end && x[x_start].equals(y[y_start])) {
      _old_to_new[x_start] = y_start;
      x_start++;
      y_start++;
    }

    //trim off the matching items at the end
    while(x_start <= x_end && y_start <= y_end && x[x_end].equals(y[y_end])) {
      _old_to_new[x_end] = y_end;
      x_end--;
      y_end--;
    }
    if(x_end < x_start)
      x_end = x_start;
    if(y_end < y_start)
      y_end = y_start;
    
    Debug.log("xs: " + x_start + " xe: " + x_end + " ys: " + y_start + " ye: " + y_end);

    int x_size = x_end - x_start + 1;
    int y_size = y_end - y_start + 1;

    Integer[] c1 = new Integer[y_size + 1];
    Integer[] c2 = new Integer[y_size + 1];
    DIR[][] b = new DIR[x_size + 1][y_size + 1];

    for(int i = 0; i <= y_size; i++) {
      c1[i] = 0;
    }
    c2[0] = 0;

    int ii, jj;
    for(int i = x_start; i < x_end; i++) {
      ii = i - x_start + 1;
      for(int j = y_start; j < y_end; j++) {
        jj = j - y_start + 1;
        if(x[i].equals(y[j])) {
          c2[jj] = c1[jj - 1] + 1;
          b[ii][jj] = DIR.UP_LEFT;
        } else if(c1[jj] >= c2[jj - 1]){
          c2[jj] = c1[jj];
          b[ii][jj] = DIR.UP;
        } else {
          c2[jj] = c2[jj - 1];
          b[ii][jj] = DIR.LEFT;
        }
      }
      //swap
      Integer[] tmp = c1;
      c1 = c2;
      c2 = tmp;
    }

    //LCS Pair<position in x, position in y> 
    List<Pair<Integer, Integer>> LCS = new LinkedList<Pair<Integer, Integer>>();
    int i = x_end - x_start, j = y_end - y_start;
    while(true) {
      if(i == 0 || j == 0)
        break;
      if(b[i][j] == DIR.UP_LEFT) {
        LCS.add(0, new Pair<Integer, Integer>(i + x_start - 1, j + y_start - 1));
        i--;
        j--;
      } else if(b[i][j] == DIR.UP) {
        i--;
      } else {
        j--;
      }
    }

    for(Pair<Integer, Integer> k : LCS) {
      _old_to_new[k.first()] = k.second();
    }

    Debug.log_time("end ");
    Debug.log("new: " + n + " old: " + m + " LCS.size(): " + LCS.size());
  /*  String tmp = "";
    for(i = 0; i < m; i++) {
      String tx = x[i].replace("<", "&lt;");
      tx = tx.replace(">", "&gt;");
      tx = tx.replace("\n", "\\n");
      String ty;
      if(_old_to_new[i] != -1) {
        ty = y[_old_to_new[i]].replace("<", "&lt;");
        ty = ty.replace(">", "&gt;");
        ty = ty.replace("\n", "\\n");
      } else {
        ty = "[null]";
      }

      tmp += i + " : " + _old_to_new[i] + " " + tx + " -> " + ty + "<br>";
    }
    Debug.log(tmp);*/
    StatusBar.setStatus("Done");
    return true;
  }
}
