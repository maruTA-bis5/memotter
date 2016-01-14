/*
 * @(#) net.bis5.memotter.model.ErrorObject
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
