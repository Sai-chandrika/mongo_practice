package com.example.mongo.mongo_practice.dto;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chandrika.g
 * user
 * @ProjectName mongo_practice
 * @since 19-12-2023
 */
public class Validation {
      public static Boolean isValidEmailPattern(String email) {
            if (email != null && !email.isEmpty()) {
                  String ePattern = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)[a-zA-Z][a-zA-Z0-9._%+()-]*@[a-zA-Z0-9.()-]+\\.[a-zA-Z]{2,}$";
                  Pattern p = Pattern.compile(ePattern);
                  Matcher m = p.matcher(email);
                  return m.matches();
            } else return null;
      }
      public static Boolean isValidMobileNumber(String mobileNumber) {
            if (mobileNumber != null && !mobileNumber.isEmpty()) {
                  String ePattern = ("^(0|9)?[6-9]{1}[0-9]{9}+$");
                  Pattern p = Pattern.compile(ePattern);
                  Matcher m = p.matcher(mobileNumber);
                  return m.matches();
            } else return null;
      }
}
