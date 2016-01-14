/*
 * @(#) net.bis5.memotter.subcommand.OAuth
 * Copyright (c) 2016 bBreak Systems Co, Ltd. All Rights Reserved.
 *
 * THE SOFTWARE IS PROVIDED BY bBreak Systems Co, Ltd.,
 * WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR
 * A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDER BE LIABLE FOR ANY
 * CLAIM, DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 */
package net.bis5.memotter.subcommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

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
public class OAuth implements SubCommand<JSONObject> {

    private static final String HELP = "Usage: " + OAuth.class.getSimpleName() + " [request|pin]";

    private static final String COMMAND_REQUEST = "request";

    private static final String COMMAND_PIN = "pin";

    private static final Collection<String> COMMANDS = Arrays.asList( COMMAND_REQUEST, COMMAND_PIN);

    /**
     * @see net.bis5.memotter.subcommand.SubCommand#execute(java.lang.String[])
     */
    @Override
    public JSONObject execute( String... args) {
        if ( args.length < 1) {
            System.err.println( "args.legth");
            return generateHelp();
        }

        String command = args[0];
        if ( !COMMANDS.contains( command)) {
            System.err.println( "no command: " + command);
            return generateHelp();
        }

        List<String> argsList = new ArrayList<>( args.length);
        for ( String arg : args) {
            argsList.add( arg);
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

    /**
     * @see net.bis5.memotter.subcommand.SubCommand#generateHelp()
     */
    @Override
    public ErrorObject generateHelp() {
        ErrorObject help = new ErrorObject();
        help.setErrorDetail( HELP);
        return help;
    }

}
