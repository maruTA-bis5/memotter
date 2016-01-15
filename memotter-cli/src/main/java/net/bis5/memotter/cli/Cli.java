/*
 * @(#) net.bis5.memotter.cli.Cli
 * Copyright (c) 2016 T.Maruyama
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.bis5.memotter.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import net.bis5.memotter.core.Memotter;
import net.bis5.memotter.model.MemotterObject;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/14
 */
public class Cli {
    private static final String CONSUMER_KEY = "QsrZZJrJBOOMVhiDc64sWPFaG";

    private static final String CONSUMER_SECRET = "BY2zPcs3Df4qRlDnoQMh7apUyqeZKrUeFwOjEGH1jmqHYen6Xd";

    /**
     * @param args
     */
    public static void main( String[] args) throws IOException {
        Memotter memotter = Memotter.getSingleton();
        memotter.setConsumerKey( CONSUMER_KEY);
        memotter.setConsumerSecret( CONSUMER_SECRET);
        BufferedReader reader = new BufferedReader( new InputStreamReader( System.in));
        String input = null;
        System.out.print( "Memotter> "); // prompt
        while ( (input = reader.readLine()) != null) {
            try {
                String[] inputArr = input.split( " ");
                if ( inputArr.length == 0) {
                    continue;
                }
                String command = inputArr[0];
                if ( "exit".equals( command.toLowerCase())) {
                    // TODO shutdown
                    break;
                }
                inputArr = input.substring( command.length() + 1, input.length()).split( " ");
                MemotterObject result = memotter.callSubCommand( command, inputArr);
                System.out.println( result.getMessage());
            }
            catch ( Exception e) {
                // XXX Oh my god...
                e.printStackTrace();
            }
            System.out.println();
            System.out.print( "Memotter> "); // prompt
        }
    }

}
