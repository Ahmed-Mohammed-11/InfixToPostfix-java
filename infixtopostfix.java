import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

interface IExpressionEvaluator {
public String infixToPostfix(String expression);
public int evaluate(String expression);
}

public class Evaluator implements IExpressionEvaluator {
    public class MyStack{
        public class Node{ //node class with data and next
            Object data;
            Node next;
            public Node(Object elem , Node nxt){
                data = elem ;
                next = nxt;
            }
        }
        public Node top; 
        public int size;
        public MyStack(){
            top = null ;
            size = 0 ;
        }
        public Object pop(){
            //tmp to store top data to return it at the end of the method 
            Object tmp = top.data;
            top = top.next ; 
            size -- ;
            return tmp;
        }
        public Object peek(){
            return top.data;
        }
        public void push(Object element){
            //method to push element to the top of the stack 
            Node n = new Node(element , top);
            top = n ;
            size ++;
        }
        public boolean isEmpty(){return top == null;}
        public int size(){return size;}
    }
    public static String a , b , c ;
    public static String first , second , third ;
    public int priority(char a){
        //function to check the priority of the operator to know wheter to push to the stack or to pop to the output
        if(a == '+' || a == '-'){
            return 1 ;
        }else if(a == '*' || a == '/'){
            return 2 ;
        }else if(a == '^'){
            return 3 ;
        }else if(a == '(' || a == 'a' || a == 'b' || a == 'c'){
            return 0 ;
        }else if(a == ')'){return 0 ;} //extra case to use in validity check
         else{
            System.out.println("Error");
            System.exit(0);
        }
        return 0 ; 
    }
    public void validityCheck(String e){
        for(int c = 0 ; c < e.length() ; c ++){
            //if there is an operator * , / , ^ and followed by another operator of same type
            if((e.charAt(c)=='*'||e.charAt(c)=='/'||e.charAt(c)=='^')&&(e.charAt(c+1)=='*'||e.charAt(c+1)=='/'||e.charAt(c+1)=='^')){
                System.out.println("Error");System.exit(0);   
            }
        }
        //check the validity of parentheses so if nom of openP are not equal to closingP this would print error
        int opPcount = 0 ;
        int clPcount = 0 ; 
        for(int i = 0 ; i < e.length() ; i++){
            if(e.charAt(i) == '('){
                opPcount ++;
            }else if(e.charAt(i) == ')'){
                clPcount ++;
            }
        }
        if(opPcount != clPcount){
            System.out.println("Error");System.exit(0);
        }
    }
    public String infixToPostfix(String expression){
        MyStack stack = new MyStack(); //instance of out stack
        String e = expression ; //shorten the name of the input 
        String outString = ""; //initializing the output as empty string
        if(priority(e.charAt(0)) == 2 || priority(e.charAt(0)) == 3){
            System.out.println("Error");
            System.exit(0);
        }
        for(int c = 0 ; c < e.length() ; c ++){
            if(e.charAt(c) == 'a' || e.charAt(c) == 'b' ||  e.charAt(c) == 'c' ){
                outString += e.charAt(c);
            }else if(e.charAt(c) == '('){
                stack.push(e.charAt(c));
            }else if(e.charAt(c) == ')'){
                while((char)stack.peek() != '('){
                    outString += stack.pop();
                }
                stack.pop(); //pop the '('
            }else{
                //here with checking the priority of the operator
                while( !stack.isEmpty() && priority(e.charAt(c)) <= priority((char) stack.peek())){
                   outString += (char) stack.pop();
                }stack.push(e.charAt(c));
            }
        }
        while(! stack.isEmpty()){
            outString += stack.pop();
        }
        return outString;
    }
    //operator function to check for the operation and return the result
    public int operatorCheck(int x , int y , char op){
        int res = 0 ;
        switch(op){
            case '+' : res =  x + y; break;
            case '-' : res =  x - y; break;
            case '*' : res =  x * y; break;
            case '/' : res =  x / y; break;
            case '^' : res = (int) Math.pow((double)x,(double)y) ; break;
            default : System.out.println("Error");System.exit(0);
        }
        return res ;
    }
    public int evaluate(String expression){
        String e = expression;
        MyStack stack = new MyStack();
        int a = Integer.parseInt(first); //to transform the string to int  
        int b = Integer.parseInt(second); //to transform the string to int
        int c = Integer.parseInt(third)  ; //to transform the string to int
        for(int ct = 0 ; ct < e.length() ; ct++){
            if(e.charAt(ct) == 'a'){stack.push(a);} //digit 1
            else if(e.charAt(ct) == 'b'){stack.push(b);} //digit 2
            else if(e.charAt(ct) == 'c'){stack.push(c);} //digit 3
            else{ //operator
                if(stack.size() == 1 && e.charAt(ct) == '+'){ct++;} //unary +
                else if (stack.size() == 1 && e.charAt(ct) == '-'){ //unary -
                    int x = (int) stack.pop();
                    stack.push(-x);
                }else{ //binary operators
                    int secondOperand = (int) stack.pop();                 
                    int firstOperand = (int) stack.pop();
                    int res = operatorCheck(firstOperand , secondOperand , (char) e.charAt(ct));
                    stack.push(res);
                }
            }
         }   
        return (int) stack.peek(); //return the result
    }
    public static void main(String[] args) {
        Evaluator exp = new Evaluator();
        Scanner myScanner = new Scanner(System.in);
        //some edit on the input string to make it easier to handle in case of "--"
        String input = myScanner.nextLine().replace("^--" , "^").replace("*--" , "*").replace("/--","/").replace("+--","+");
        first = myScanner.nextLine().replace("a" , "").replace("=" , ""); //taking input a=val and then trim to be val
        second = myScanner.nextLine().replace("b" , "").replace("=" , ""); //taking input b=val and then trim to be val 
        third = myScanner.nextLine().replace("c" , "").replace("=" , ""); //taking input c=val and then trim to be val
        if(input.charAt(0) == '-' && input.charAt(1) == '-'){
            input = input.replaceFirst( "--" , ""); //handling the case if the input string starts with --
        }input = input.replace("--" , "+"); // the appearance of -- in any other place in the string without any other operator before it
        if(exp.priority(input.charAt(input.length()-1)) != 0){
            System.out.println("Error");System.exit(0);
        }
        exp.validityCheck(input);
        String output = exp.infixToPostfix(input);
        System.out.println(output);
        int res = exp.evaluate(output);
        System.out.println(res);
    }
}
