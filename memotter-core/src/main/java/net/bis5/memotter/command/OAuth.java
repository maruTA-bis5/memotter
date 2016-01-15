/*
 * @(#) net.bis5.memotter.subcommand.OAuth
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
package net.bis5.memotter.command;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import net.bis5.memotter.core.ArgsList;
import net.bis5.memotter.core.Command;
import net.bis5.memotter.core.Memotter;
import net.bis5.memotter.model.ErrorObject;
import net.bis5.memotter.model.MemotterObject;
import twitter4j.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/14
 */
public class OAuth extends Command<JSONObject> {

    private static final String HELP = "Usage: " + OAuth.class.getSimpleName() + " [request|pin]";

    private static final String COMMAND_REQUEST = "request";

    private static final String COMMAND_PIN = "pin";

    private static final Collection<String> COMMANDS = Arrays.asList( COMMAND_REQUEST, COMMAND_PIN);

    protected JSONObject doOAuthRequest( List<String> args) throws TwitterException {
        Twitter twtr = TwitterFactory.getSingleton();
        Memotter memotter = Memotter.getSingleton();
        try {
            twtr.setOAuthConsumer( memotter.getConsumerKey(), memotter.getConsumerSecret());
        }
        catch ( IllegalStateException e) {
            // oauth consumer are already set.
            // no op
        }
        RequestToken requestToken = twtr.getOAuthRequestToken();
        Memotter.getSingleton().putStorageItem( getClass().getCanonicalName().concat( "requestToken"), requestToken);

        return new MemotterObject() {

            @Override
            public String getMessage() {
                return "Please Access the URL and get you PIN! \n"
                        + requestToken.getAuthorizationURL();
            }
        };
    }

    protected JSONObject doPinInput( List<String> args) throws TwitterException {
        if ( args.isEmpty()) {
            String help = "Usage: " + OAuth.class.getSimpleName() + " pin <YOUR PIN>";
            ErrorObject helpResult = new ErrorObject();
            helpResult.setErrorDetail( help);
            return helpResult;
        }
        Stream<String> argsStream = args.stream();
        RequestToken requestToken = Memotter.getSingleton().getStorageItem( getClass().getCanonicalName().concat( "requestToken"));
        String pin = argsStream.findFirst().orElse( null);

        Twitter twtr = TwitterFactory.getSingleton();
        AccessToken accessToken;
        if ( pin != null) {
            accessToken = twtr.getOAuthAccessToken( requestToken, pin);
        }
        else {
            accessToken = twtr.getOAuthAccessToken();
        }

        Memotter.getSingleton().setAccessToken( accessToken);

        return new MemotterObject() {
            @Override
            public String getMessage() {
                return "Authorized!";
            }
        };
    }

    public ErrorObject generateHelp() {
        ErrorObject help = new ErrorObject();
        help.setErrorDetail( HELP);
        return help;
    }

    /**
     * @see net.bis5.memotter.core.Command#getCommandPackage()
     */
    @Override
    protected String getCommandPackage() {
        return null;
    }

    /**
     * @see net.bis5.memotter.core.Command#execute(net.bis5.memotter.core.ArgsList)
     */
    @Override
    public JSONObject execute( ArgsList argsList) {
        String command = argsList.getFirst();
        if ( !COMMANDS.contains( command)) {
            System.err.println( "no command: " + command);
            return generateHelp();
        }

        List<String> subArgs = argsList.subList( 1, argsList.size());
        JSONObject result;
        try {
            switch ( command) {
                case COMMAND_REQUEST:
                    result = doOAuthRequest( subArgs);
                    break;
                case COMMAND_PIN:
                    result = doPinInput( subArgs);
                    break;
                default:
                    System.err.println( "unknown command: " + command);
                    result = generateHelp();
            }
        }
        catch ( TwitterException e) {
            e.printStackTrace();
            result = generateHelp();
        }
        return result;
    }

}
