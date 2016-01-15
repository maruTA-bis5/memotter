/*
 * @(#) net.bis5.memotter.core.Memotter
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
package net.bis5.memotter.core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import net.bis5.memotter.exception.CommandNotFoundException;
import net.bis5.memotter.exception.MemotterException;
import net.bis5.memotter.model.MemotterObject;
import net.bis5.memotter.model.MessageObject;
import twitter4j.JSONException;
import twitter4j.JSONObject;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/14
 */
public class Memotter extends Command<MemotterObject> {
    @Getter
    @Setter
    private String consumerSecret;

    @Getter
    @Setter
    private String consumerKey;

    private static final Memotter INSTANCE = new Memotter();

    private final ConcurrentMap<String, Object> temporaryStorage = new ConcurrentHashMap<>();

    protected Memotter() {
        try {
            Path path = Paths.get( System.getProperty( "user.home"), ".memotter", "memotter.json");
            if ( !Files.isReadable( path)) {
                return;
            }
            BufferedReader reader = Files.newBufferedReader( path, Charset.forName( "UTF-8"));
            String json = reader.lines().collect( Collectors.joining());
            reader.close();

            JSONObject jsonObj = new JSONObject( json);
            @SuppressWarnings( "unchecked")
            Iterator<String> iter = jsonObj.keys();
            while ( iter.hasNext()) {
                String key = iter.next();
                temporaryStorage.put( key, jsonObj.get( key));
            }
        }
        catch ( Exception e) {
            throw new RuntimeException( e);
        }
    }

    public static Memotter getSingleton() {
        return INSTANCE;
    }

    public Map<String, Object> getStorage() {
        return temporaryStorage;
    }

    @SuppressWarnings( "unchecked")
    public <T> T getStorageItem( String key) {
        return ( T) temporaryStorage.get( key);
    }

    public <T> void putStorageItem( String key, T value) {
        temporaryStorage.put( key, value);
        try {
            persist();
        }
        catch ( Exception e) {
            e.printStackTrace();
        }
    }

    public void persist() throws IOException, JSONException {
        Path path = Paths.get( System.getProperty( "user.home"), ".memotter");
        if ( !Files.isDirectory( path)) {
            Files.createDirectory( path);
        }
        JSONObject json = new JSONObject();
        for ( Entry<String, Object> entry : temporaryStorage.entrySet()) {
            json.append( entry.getKey(), entry.getValue());
        }
        BufferedWriter writer = Files.newBufferedWriter( Paths.get( path.toString(), "memotter.json"), Charset.forName( "UTF-8"));
        writer.write( json.toString());
        writer.flush();
        writer.close();
    }

    private static final String KEY_ACCESS_TOKEN = "accessToken";

    private static final String KEY_ACCESS_TOKEN_SECRET = "accessTokenSecret";

    private static final String KEY_ACCESS_TOKEN_USERID = "accessTokenUserId";

    public void setAccessToken( AccessToken accessToken) throws TwitterException {
        String token = accessToken.getToken();
        String tokenSecret = accessToken.getTokenSecret();
        Long userId = TwitterFactory.getSingleton().verifyCredentials().getId();
        temporaryStorage.put( KEY_ACCESS_TOKEN, token);
        temporaryStorage.put( KEY_ACCESS_TOKEN_SECRET, tokenSecret);
        temporaryStorage.put( KEY_ACCESS_TOKEN_USERID, userId);
        try {
            persist();
        }
        catch ( IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public AccessToken getAccessToken() {
        String token = getStorageItem( KEY_ACCESS_TOKEN);
        String tokenSecret = getStorageItem( KEY_ACCESS_TOKEN_SECRET);
        return new AccessToken( token, tokenSecret);
    }

    private static final String KEY_PREFIX = "prefix";

    private static final String KEY_SUFFIX = "suffix";

    public void setPrefix( String prefix) {
        putStorageItem( KEY_PREFIX, prefix);
    }

    public String getPrefix( boolean useDefault) {
        String prefix = getStorageItem( KEY_PREFIX);
        if ( useDefault && prefix == null) {
            prefix = "φ(．．)メモメモ(test) / ";
        }
        return prefix;
    }

    public void setSuffix( String suffix) {
        putStorageItem( KEY_SUFFIX, suffix);
    }

    public String getSuffix() {
        String suffix = getStorageItem( KEY_SUFFIX);
        return suffix != null ? suffix : "";
    }

    /**
     * @param string
     */
    public MemotterObject memo( String content) throws TwitterException {
        Twitter twtr = TwitterFactory.getSingleton();
        twtr.setOAuthAccessToken( getAccessToken());
        try {
            twtr.setOAuthConsumer( getConsumerKey(), getConsumerSecret());
        }
        catch ( IllegalStateException e) {
            // no op
        }
        Status status = twtr.updateStatus( getPrefix( true) + content + getSuffix());
        String userName = status.getUser().getScreenName();
        String id = Long.toString( status.getId());
        return new MessageObject( "https://twitter.com/" + userName + "/statuses/" + id);
    }

    /**
     * @see net.bis5.memotter.core.Command#getCommandPackage()
     */
    @Override
    protected String getCommandPackage() {
        return "net.bis5.memotter.command";
    }

    /**
     * @see net.bis5.memotter.core.Command#execute(net.bis5.memotter.core.ArgsList)
     */
    @Override
    public MemotterObject execute( ArgsList args) {
        String command = args.getFirst();
        ArgsList commandArgs = args.shift();
        try {
            MemotterObject result = callSubCommand( command, commandArgs);
            return result;
        }
        catch ( CommandNotFoundException e) {
            return new MessageObject( Memotter.class.getSimpleName() + ": Command not found");
        }
        catch ( MemotterException e) {
            throw e;
        }
    }

}
