/*
 * @(#) net.bis5.memotter.exception.MemotterException
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
 * Memotter Exception
 * @author T.Maruyama
 * @since 2016/01/15
 */
public class MemotterException extends RuntimeException {

    /** serial */
    private static final long serialVersionUID = 3488893739773465095L;

    /**
     * @see {@link RuntimeException#RuntimeException()}
     */
    public MemotterException() {
        super();
    }

    /**
     * @see {@link RuntimeException#RuntimeException(String)}
     * @param message
     */
    public MemotterException( String message) {
        super( message);
    }

    /**
     * @see {@link RuntimeException#RuntimeException(Throwable)}
     * @param cause
     */
    public MemotterException( Throwable cause) {
        super( cause);
    }

    /**
     * @see {@link RuntimeException#RuntimeException(String, Throwable)}
     * @param message
     * @param cause
     */
    public MemotterException( String message, Throwable cause) {
        super( message, cause);
    }

}
