/*
 * @(#) net.bis5.memotter.core.Memotter
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
import net.bis5.memotter.model.MemotterObject;
import net.bis5.memotter.subcommand.SubCommand;
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
public class Memotter {
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

    public <T> T callSubCommand( String subCommandName, String... args) {
        final String subCommandPkg = SubCommand.class.getPackage().getName() + ".";
        String commandFqcn = subCommandPkg + subCommandName;

        try {
            @SuppressWarnings( "unchecked")
            SubCommand<T> command = ( SubCommand<T>) Class.forName( commandFqcn).newInstance();
            return command.execute( args);
        }
        catch ( ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException( e);
        }

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
        return new MemotterObject() {
            @Override
            public String getMessage() {
                return "https://twitter.com/" + userName + "/" + id;
            }
        };

    }

}
