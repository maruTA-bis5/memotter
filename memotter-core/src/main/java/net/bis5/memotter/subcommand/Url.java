/*
 * @(#) net.bis5.memotter.subcommand.Url
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.bis5.memotter.core.Memotter;
import net.bis5.memotter.model.ErrorObject;
import net.bis5.memotter.model.MemotterObject;
import twitter4j.TwitterException;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/14
 */
public class Url implements SubCommand<MemotterObject> {

    /**
     * @see net.bis5.memotter.subcommand.SubCommand#execute(java.lang.String[])
     */
    @Override
    public MemotterObject execute( String... args) {
        List<String> argsList = new ArrayList<>( args.length);
        for ( String arg : args) {
            argsList.add( arg);
        }
        Stream<String> argsStream = argsList.stream();
        String title = null;
        try {
            URL url = new URL( argsStream.findFirst().orElse( null));
            BufferedReader reader = new BufferedReader( new InputStreamReader( url.openConnection().getInputStream()));
            String content = reader.lines().collect( Collectors.joining());
            reader.close();

            Pattern titlePattern = Pattern.compile( "<title>([^<]+)</title>", Pattern.CASE_INSENSITIVE);
            Matcher matcher = titlePattern.matcher( content);
            if ( matcher.find()) {
                title = matcher.group( 1);
            }
        }
        catch ( IOException e) {
            e.printStackTrace();
            return generateHelp();
        }

        Memotter memotter = Memotter.getSingleton();
        try {
            return memotter.memo( title + " " + args[0]);
        }
        catch ( TwitterException e) {
            e.printStackTrace();
            return generateHelp();
        }
    }

    /**
     * @see net.bis5.memotter.subcommand.SubCommand#generateHelp()
     */
    @Override
    public ErrorObject generateHelp() {
        ErrorObject help = new ErrorObject();
        help.setErrorDetail( "Usage: Url <URL>");
        return help;
    }

}
