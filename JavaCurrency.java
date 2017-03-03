import java.net.*;
import java.io.*;
import java.util.*;
import org.json.*;

public class JavaCurrency {
  
  public static String apiEndpoint = "http://api.fixer.io/latest";
  public static String base = "USD", symbols = "";

  public static String getApiEndPoint() {
    return apiEndpoint;
  }

  public static void setBase(String b) {
    base = b;
  }

  public static String getBase() {
    return base;
  }

  public static void setSymbols(String s) {
    symbols = s;
  }

  public static String getSymbols() {
    return symbols;
  }

  JavaCurrency() {
  }

  JavaCurrency(String b) {
    setBase(b);
  }

  JavaCurrency(String b, String s) {
    setBase(b);
    setSymbols(s);
  }

  public static void Do() {
    JSONObject obj;
    try {
      obj = sendRequest(getBase(), getSymbols());
      parse(obj);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  public static void parse(JSONObject obj) {
    System.out.println(String.format("\nBase currency: %s, Date: %s\n\n", obj.getString("base"), obj.getString("date")));

    System.out.print(String.format("| %-12s|-%-12s |\n", "------------", "------------"));
    System.out.print(String.format("| %-12s| %-12s |\n", "Symbol", "Rate"));
    System.out.print(String.format("| %-12s|-%-12s |\n", "------------", "------------"));

    JSONObject jsonRates = obj.getJSONObject("rates");
    for (final Iterator<String> it = jsonRates.keys(); it.hasNext();) {
      final String key = it.next();

      double r = -1;
      try {
        r = jsonRates.getDouble(key);
      } catch (JSONException e) {
        System.out.println(String.format("Error: %s", e));
      }
      System.out.print(String.format("| %-12s| %-12.4f |\n", key, r));
    }
    System.out.print(String.format("| %-12s|-%-12s |\n", "------------", "------------"));
  }

  public static JSONObject sendRequest(String base, String symbols) throws Exception {
    String url = getApiEndPoint() + "?";
    url = url + "base=" + base;
    if (!symbols.equals("")) {
      url = url + "&symbols=" + symbols;
    }

    URL fixer = new URL(url);
    URLConnection fc = fixer.openConnection();
    BufferedReader in = new BufferedReader(new InputStreamReader(fc.getInputStream()));
    String inputLine;
    String json = new String();
    while ((inputLine = in.readLine()) != null) {
      json += inputLine;
    }
    in.close();

    JSONObject obj = new JSONObject(json);
    return obj;
  }

  public static void main(String[] args) {
    JavaCurrency app;

    switch (args.length) {
      case 0:
        app = new JavaCurrency();
        break;
      case 1:
        app = new JavaCurrency(args[0]);
        break;
      case 2:
        app = new JavaCurrency(args[0], args[1]);
        break;
      default:
        app = new JavaCurrency();
        break;
    }

    app.Do();
  }

}