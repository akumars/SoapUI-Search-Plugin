package com.au.ft.helpers;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

public final class StringUtility {

 
  /**
   Replace characters having special meaning in regular expressions
   with their escaped equivalents, preceded by a '\' character.

  */
  public static String ReplaceSpecialCharacters(String aRegexFragment){
    final StringBuilder result = new StringBuilder();

    final StringCharacterIterator iterator = 
      new StringCharacterIterator(aRegexFragment)
    ;
    char character =  iterator.current();
    while (character != CharacterIterator.DONE ){
      /*
       All literals need to have backslashes doubled.
      */
      if (character == '.') {
        result.append("\\.");
      }
      else if (character == '\\') {
        result.append("\\\\");
      }
      else if (character == '?') {
        result.append("\\?");
      }
      else if (character == '*') {
        result.append("\\*");
      }
      else if (character == '+') {
        result.append("\\+");
      }
      else if (character == '&') {
        result.append("\\&");
      }
      else if (character == ':') {
        result.append("\\:");
      }
      else if (character == '{') {
        result.append("\\{");
      }
      else if (character == '}') {
        result.append("\\}");
      }
      else if (character == '[') {
        result.append("\\[");
      }
      else if (character == ']') {
        result.append("\\]");
      }
      else if (character == '(') {
        result.append("\\(");
      }
      else if (character == ')') {
        result.append("\\)");
      }
      else if (character == '^') {
        result.append("\\^");
      }
      else if (character == '$') {
        result.append("\\$");
      }
      else {
        result.append(character);
      }
      character = iterator.next();
    }
    return result.toString();
  }
  
  
  private StringUtility(){
    //empty - prevent construction
  }
  

}