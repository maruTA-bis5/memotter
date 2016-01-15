/*
 * @(#) net.bis5.memotter.model.ErrorObject
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
package net.bis5.memotter.model;

import lombok.Getter;
import twitter4j.JSONException;

/**
 * TODO 型の説明
 * @author T.Maruyama
 * @since 2016/01/14
 */
public class ErrorObject extends MemotterObject {

    private static final String KEY_DETAIL = ErrorObject.class.getCanonicalName().concat( ".detail");

    private static final String KEY_TITLE = ErrorObject.class.getCanonicalName().concat( ".title");

    private static final String KEY_CAUSE = ErrorObject.class.getCanonicalName().concat( ".cause");

    @Getter
    private String errorDetail;

    public void setErrorDetail( String detail) {
        this.errorDetail = detail;
        try {
            append( KEY_DETAIL, detail);
        }
        catch ( JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Getter
    private String errorTitle;

    public void setErrorTitle( String title) {
        this.errorTitle = title;
        try {
            append( KEY_TITLE, title);
        }
        catch ( JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Getter
    private Exception cause;

    public void setCause( Exception cause) {
        this.cause = cause;
        try {
            append( KEY_CAUSE, cause);
        }
        catch ( JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public String getMessage() {
        return getErrorDetail();
    }
}
