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
//#define kAPIHost @"http://api.soundit.ca"//change to ip or uploading of photos
//#define kAPIPath @"backend"
#define kAPIHost @"http://127.0.0.1:8000"
#define kAPIPath @"backend"

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
        [self setDefaultHeader:@"Accept" value:@"application/text"];
    }
    
    return self;
}

-(BOOL)isAuthorized
{
    return authorized;
}

-(void)callAPIMethod:(NSString *)command withParams:(NSMutableDictionary *)params onCompletion:(JSONResponseBlock)completionBlock
{
    //build our urlCommandString with Host + Path first
    NSMutableString *urlCommandString = [NSMutableString stringWithFormat:@"%@/%@/%@/?", kAPIHost, kAPIPath, command];
    NSLog(@"urlCommandString reads:%@", urlCommandString);
    
    NSLog(@"params description:%@", [params description]);
    
    for(id key in params){
        [urlCommandString appendString:key];
        [urlCommandString appendString:@"="];
        [urlCommandString appendString:[[params objectForKey:key] description]];
        [urlCommandString appendString:@"&"];
        
        NSLog(@"urlCommandString reads:%@", urlCommandString);
        
    }
    
    //TODO:REFACTOR -- remove the last occurence of '&' due to our for loop (I know this is ugly; needs refactor
    urlCommandString = [urlCommandString substringToIndex:urlCommandString.length - 1].mutableCopy;
    
    NSLog(@"urlCommandString holds: %@", urlCommandString);
    NSMutableURLRequest *apiRequest = [self requestWithMethod:@"GET"
                                                         path:urlCommandString
                                                   parameters:nil];
    
    AFJSONRequestOperation *operation = [[AFJSONRequestOperation alloc] initWithRequest:apiRequest];
    [operation setCompletionBlockWithSuccess:^(AFHTTPRequestOperation *operation, id responseObject){
        NSLog(@"responseObject description:%@", responseObject);
        completionBlock(responseObject);
        
    }failure:^(AFHTTPRequestOperation *operation, NSError *error){
        completionBlock([NSArray arrayWithObject:[NSDictionary dictionaryWithObject:[error localizedDescription] forKey:@"Error Message"]]);
        
    }];
    
    [operation start];
    
}

@end
