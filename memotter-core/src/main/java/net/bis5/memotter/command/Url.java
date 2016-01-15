/*
 * @(#) net.bis5.memotter.subcommand.Url
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.bis5.memotter.core.ArgsList;
import net.bis5.memotter.core.Command;
import net.bis5.memotter.core.Memotter;
import net.bis5.memotter.model.ErrorObject;
import net.bis5.memotter.model.MemotterObject;
import twitter4j.TwitterException;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/14
 */
public class Url extends Command<MemotterObject> {

    public ErrorObject generateHelp() {
        ErrorObject help = new ErrorObject();
        help.setErrorDetail( "Usage: Url <URL>");
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
    public MemotterObject execute( ArgsList argsList) {
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
            return memotter.memo( title + " " + argsList.getFirst());
        }
        catch ( TwitterException e) {
            e.printStackTrace();
            return generateHelp();
        }
    }

}
