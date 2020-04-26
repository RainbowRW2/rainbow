package com.milchstrabe;

import com.milchstrabe.interpret.CMDS;
import com.milchstrabe.interpret.Expression;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * rainbow client test program
 *
 */
public class App{

    public static void main( String[] args ) throws IOException, InterruptedException, ExecutionException, TimeoutException {
        Scanner inp = new Scanner(System.in);

        login(inp);

        while (true){
            System.out.print("$ ");
            String str = inp.nextLine();

            if(str == null || "".equals(str = str.trim())){
                str = "command is error";
            }

            String regex = "\\s+";
            String[] split = str.split(regex);
            if(split.length>=1){
                Expression expression = CMDS.C_M_D_S.get(split[0]);
                if(expression != null){
                    expression.interpret(str);
                }
            }
            System.out.println("command not found: "+ split[0]);
        }

    }

    private static void login(Scanner inp){
        System.out.print("username: ");
        String username = inp.nextLine();
        System.out.print("password: ");
        String password = inp.nextLine();
        if("admin".equals(username) && "123".equals(password)){
            return;
        }
        login(inp);
    }
}
