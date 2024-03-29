//
//  API.h
//  iReporter
//
//  Created by Samuel Chan on 2012-10-29.
//  Copyright (c) 2012 Marin Todorov. All rights reserved.
//

#import "AFHTTPClient.h"
#import "AFNetworking.h"

//block used for calls to API; takes in an NSDictionary object as inputs to JSON around
typedef void (^JSONResponseBlock)(NSArray *json);
typedef void (^StringResponseBlock)(NSMutableString *responseString);

@interface API : AFHTTPClient

//properties
@property (strong, nonatomic) NSDictionary *user;
@property BOOL authorized;

//public methods
+(API*)sharedInstance;

//instance methods
-(BOOL)isAuthorized;
-(void)callAPIMethod:(NSString *)command withParams:(NSMutableDictionary *)params onCompletion:(JSONResponseBlock)completionBlock;

@end
