//
//  API.m
//  iReporter
//
//  Created by Samuel Chan on 2012-10-29.
//  Copyright (c) 2012 Marin Todorov. All rights reserved.
//

#import "API.h"

#import "SBJson.h"

//the web location of the service
#define kAPIHost @"http://ec2-107-22-139-35.compute-1.amazonaws.com:11080"//change to ip or uploading of photos
#define kAPIPath @"backend"
//#define kAPIHost @"http://localhost"
//#define kAPIPath @"/~samuelchan/Web/ios_interface/"

@implementation API
@synthesize user;
@synthesize authorized;

#pragma mark - Singleton methods
/**
 * Singleton methods
 */
+(API*)sharedInstance
{
    static API *sharedInstance = nil;
    static dispatch_once_t oncePredicate;
    dispatch_once(&oncePredicate, ^{
        //below is equivalent to: AFHTTPClient *httpClient = [[AFHTTPClient alloc] initWithBaseURL:[NSURL URLWithString:@"http://samwize.com/"]];
        sharedInstance = [[self alloc] initWithBaseURL:[NSURL URLWithString:kAPIHost]];
        
    });
    
    return sharedInstance;
}

#pragma mark - init
//intialize the API class with the destination host name

-(API*)init
{
    //call super init
    self = [super init];
    
    if (self != nil) {
        //initialize the object
        user = nil;
        authorized = NO;
        
        //below is equivalent to: [httpClient registerHTTPOperationClass:[AFHTTPRequestOperation class]];
        [self registerHTTPOperationClass:[AFHTTPRequestOperation class]];
        
        // Accept HTTP Header; see http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.1
//        [self setDefaultHeader:@"Accept" value:@"application/text"];
    }
    
    return self;
}

-(BOOL)isAuthorized
{
    return authorized;
}

-(void)callAPIMethod:(NSString *)command withParams:(NSMutableDictionary *)params onCompletion:(StringResponseBlock)responseBlock
{
    //build our urlCommandString with Host + Path first
    NSMutableString *urlCommandString = [NSMutableString stringWithFormat:@"%@/%@/%@/?", kAPIHost, kAPIPath, command];
    
    for(id key in params){
        [urlCommandString appendString:key];
        [urlCommandString appendString:@"="];
        [urlCommandString appendString:[params objectForKey:key]];
        [urlCommandString appendString:@"&"];
        
    }
    
    urlCommandString = [urlCommandString substringToIndex:urlCommandString.length - 1].mutableCopy;
    
    NSLog(@"urlString holds: %@", urlCommandString);
    NSMutableURLRequest *apiRequest = [self requestWithMethod:@"GET"
                                                         path:urlCommandString
                                                   parameters:nil];
    
    AFHTTPRequestOperation *operation = [[AFHTTPRequestOperation alloc] initWithRequest:apiRequest];
    [operation setCompletionBlockWithSuccess:^(AFHTTPRequestOperation *operation, id responseObject){
        
//        NSMutableString *content = [[NSMutableString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
//        
//        NSLog(@"content reads %@", content);
//        
//        [content replaceOccurrencesOfString:@"\\" withString:@"" options:NSCaseInsensitiveSearch range:NSMakeRange(0, content.length)];
//        
//        NSLog(@"content reads %@", content);
//        
//        SBJsonParser *parser = [[SBJsonParser alloc] init];
//        NSArray *json = [parser objectWithString:content];
//        
//        NSDictionary *jsonUser = [json objectAtIndex:0];
//        
//        NSLog(@"jsonUser reads: %@", [jsonUser description]);
        
        NSMutableString *responseString = [[NSMutableString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
//        
//        NSLog(@"responseeString %@", responseString);
//        
//        [responseString replaceOccurrencesOfString:@"\\" withString:@"" options:NSCaseInsensitiveSearch range:NSMakeRange(0, responseString.length - 1)];
//        [responseString replaceOccurrencesOfString:@"\"" withString:@"" options:NSCaseInsensitiveSearch range:NSMakeRange(0, responseString.length - 1)];
//        
//        NSLog(@"responseString is: %@", responseString);
//        
//        NSError *error;
//        NSData *jsonData = [responseString dataUsingEncoding:NSUTF8StringEncoding];
//        NSDictionary *results = [NSJSONSerialization JSONObjectWithData:jsonData options:0 error:&error];
//        
//        NSLog(@"results: %@", [results description]);
//        
        responseBlock(responseString);
        
    }failure:^(AFHTTPRequestOperation *operation, NSError *error){
        responseBlock([error localizedDescription].mutableCopy);
        
    }];
    
    [operation start];
    
}

@end
