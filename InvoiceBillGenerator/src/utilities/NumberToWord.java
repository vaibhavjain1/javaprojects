package utilities;

public class NumberToWord  

{
   private static final String[] specialNames = {
       "",
       " Thousand",
       " Million",
       " Billion",
       " Trillion",
       " Quadrillion",
       " Quintillion"
   };
   
   private static final String[] tensNames = {
       "",
       " Ten",
       " Twenty",
       " Thirty",
       " Forty",
       " Fifty",
       " Sixty",
       " Seventy",
       " Eighty",
       " Ninety"
   };
   
   private static final String[] numNames = {
       "",
       " One",
       " Two",
       " Three",
       " Four",
       " Five",
       " Six",
       " Seven",
       " Eight",
       " Nine",
       " Ten",
       " Eleven",
       " Twelve",
       " Thirteen",
       " Fourteen",
       " Fifteen",
       " Sixteen",
       " Seventeen",
       " Eighteen",
       " Nineteen"
   };
   
   private String convertLessThanOneThousand(int number) {
       String current;
       
       if (number % 100 < 20){
           current = numNames[number % 100];
           number /= 100;
       }
       else {
           current = numNames[number % 10];
           number /= 10;
           
           current = tensNames[number % 10] + current;
           number /= 10;
       }
       if (number == 0) return current;
       return numNames[number] + " hundred" + current;
   }
   
   public String convert(int number) {

       if (number == 0) { return "zero"; }
       
       String prefix = "";
       
       if (number < 0) {
           number = -number;
           prefix = "negative";
       }
       
       String current = "";
       int place = 0;
       
       do {
           int n = number % 1000;
           if (n != 0){
               String s = convertLessThanOneThousand(n);
               current = s + specialNames[place] + current;
           }
           place++;
           number /= 1000;
       } while (number > 0);
       
       return (prefix + current).trim();
   }
   
	public static String NumberToCurrency(double totalAmount) {
		String amountInwords = "";

		totalAmount = totalAmount * 100;
		totalAmount = Math.round(totalAmount);
		totalAmount = totalAmount / 100;

		String totalAmountasString = String.valueOf(totalAmount);
		int ruppess = Integer.parseInt(totalAmountasString.split("\\.")[0]);
		int paise = Integer.parseInt(totalAmountasString.split("\\.")[1]);

		NumberToWord obj = new NumberToWord();
		amountInwords += "INR " + obj.convert(ruppess);
		if (paise != 0) {
			amountInwords += " and ";
			amountInwords += obj.convert(paise);
			amountInwords += " paise";
		}
		amountInwords += " only";
		
		return amountInwords;
	}
   
   public static void main(String[] args) {
       System.out.println(NumberToCurrency(12345.6789));
   }
}