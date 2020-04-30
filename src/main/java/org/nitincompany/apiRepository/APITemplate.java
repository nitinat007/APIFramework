package org.nitincompany.apiRepository;

import java.util.HashMap;

/**
 * Author: nitinkumar
 * Created Date: 12/11/19
 * Info: This class acts as a template to create API Class. Change this only after discussion.
 **/

public abstract class APITemplate {
    public String httpVerb ;
    public String relativePath;
    public String baseURL;
    public HashMap<String,String> additionalHeaders;
    public HashMap<String,String> additionalBody;
    public HashMap<String,String> parameters;
    public String parameterType;

    public APITemplate(String httpVerb, String relativePath, String baseURL) {
        this.httpVerb = httpVerb;
        this.relativePath = relativePath;
        this.baseURL = baseURL;
    }

    public APITemplate(String httpVerb, String relativePath, String baseURL, HashMap<String,String> additionalHeaders, HashMap<String,String> additionalBody, String parameterType, HashMap<String,String> parameters) {
        this.httpVerb = httpVerb;
        this.relativePath = relativePath;
        this.baseURL = baseURL;
        this.additionalBody = additionalBody;
        this.additionalHeaders = additionalHeaders;
        this.parameters = parameters;
        this.parameterType=parameterType;
    }
    /* preserved for future reference
    public APITemplate(String httpVerb, String relativePath, String baseURL, HashMap<String,String> additionalHeaders) {
        this.httpVerb = httpVerb;
        this.relativePath = relativePath;
        this.baseURL = baseURL;
        this.additionalHeaders = additionalHeaders;
    }

    public APITemplate(String httpVerb, String relativePath, String baseURL, HashMap<String,String> additionalHeaders, List<String> additionalBody,String parameterType, String paramValue) {
        this.httpVerb = httpVerb;
        this.relativePath = relativePath;
        this.baseURL = baseURL;
        this.additionalBody = additionalBody;
        this.additionalHeaders = additionalHeaders;
    }
    */

    public String toString(){
        return "httpVerb:"+httpVerb+"\n  baseURL:"+baseURL+ "\n  relativePath:"+relativePath+ "\n  additionalBody:"+ additionalBody+"\n  additionalHeaders:"+additionalHeaders+" \n parameters:"+parameters +" parameterType:"+parameterType;
    }
}
