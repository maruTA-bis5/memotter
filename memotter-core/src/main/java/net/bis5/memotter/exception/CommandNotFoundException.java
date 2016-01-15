/*
 * @(#) net.bis5.memotter.exception.CommandNotFoundException
 * Copyright (c) 2016 T.Maruyama
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * 	Unless required by applicable law or agreed to in writing, software
 * 	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * 	See the License for the specific language governing permissions and
 * 	limitations under the License.
 */
package net.bis5.memotter.exception;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/15
 */
public class CommandNotFoundException extends MemotterException {

    /** serial */
    private static final long serialVersionUID = 1568766332509260316L;

    public CommandNotFoundException( String command) {
        this( command, null);
    }

    public CommandNotFoundException( String command, String usage) {
        super( generateMessage( command, usage));
    }

    private static String generateMessage( String command, String usage) {
        String message = "Command[" + command + "] is not found.";
        if ( usage != null) {
            message += String.format( "%n%s", usage);
        }
        return message;
    }

}
